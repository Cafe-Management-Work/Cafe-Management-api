package com.inn.cafe.Cafe.Management.serviceImpl;

import com.inn.cafe.Cafe.Management.constants.CafeConstants;
import com.inn.cafe.Cafe.Management.customExceptions.*;
import com.inn.cafe.Cafe.Management.dao.UserDao;
import com.inn.cafe.Cafe.Management.dto.request.*;
import com.inn.cafe.Cafe.Management.dto.response.UserResponse;
import com.inn.cafe.Cafe.Management.jwt.CustomerUsersDetailsService;
import com.inn.cafe.Cafe.Management.jwt.JwtFilter;
import com.inn.cafe.Cafe.Management.jwt.JwtUtil;
import com.inn.cafe.Cafe.Management.pojo.User;
import com.inn.cafe.Cafe.Management.service.UserService;
import com.inn.cafe.Cafe.Management.utils.EmailUtils;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@Slf4j
public class UserSeviceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomerUsersDetailsService customerUsersDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private EmailUtils emailUtils;

    // ---------------- CREATE USER ----------------
    @Override
    @Transactional
    public String createUser(UserRequestDTO dto) {

        if (userDao.existsByEmail(dto.email())) {
            throw new EmailAlreadyExistsException(
                    CafeConstants.EMAIL_ALREADY_EXISTS + dto.email()
            );
        }

        User user = new User();
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setContactNumber(dto.contactNumber());
        user.setPassword(dto.password());
        user.setRole(dto.role());
        user.setStatus(CafeConstants.STATUS_FALSE);

        userDao.save(user);
        return CafeConstants.USER_SAVED_SUCCESSFULLY;
    }

    // ---------------- LOGIN ----------------
    @Override
    public String login(AuthenticationDto authenticationDto) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationDto.email(),
                            authenticationDto.password()
                    )
            );

            var userDetails = customerUsersDetailsService.getUserDetail();

            if (!CafeConstants.STATUS_TRUE.equalsIgnoreCase(userDetails.getStatus())) {
                throw new UserUnAuthorizedAccessException(
                        CafeConstants.ACCOUNT_NOT_APPROVED
                );
            }

            String token = jwtUtil.generateToken(
                    userDetails.getEmail(),
                    userDetails.getRole()
            );

            return CafeConstants.TOKEN_JSON_PREFIX + token + CafeConstants.TOKEN_JSON_SUFFIX;

        } catch (BadCredentialsException ex) {
            throw new PasswordException(CafeConstants.INCORRECT_PASSWORD);
        } catch (UsernameNotFoundException ex) {
            throw new UserNotExists(CafeConstants.USER_NOT_FOUND);
        }
    }

    // ---------------- GET USERS ----------------
    @Override
    public List<UserResponse> getUsers() {

        if (jwtFilter.isAdmin()) {
            return userDao.getAllUser();
        }

        throw new UserUnAuthorizedAccessException(
                CafeConstants.ADMIN_ACCESS_ONLY
        );
    }

    // ---------------- UPDATE STATUS ----------------
    @Override
    public String updateStatus(StatusUpdateRequest request) {

        if (!jwtFilter.isAdmin()) {
            throw new UserUnAuthorizedAccessException(
                    CafeConstants.ADMIN_ACCESS_ONLY
            );
        }

        User user = userDao.findById(request.id())
                .orElseThrow(() ->
                        new UserNotExists(CafeConstants.USER_UNAVAILABLE)
                );

        userDao.updateStatus(request.status(), request.id());

        String adminEmail = jwtFilter.getCurrentUser();

        String userMailBody = String.format(
                CafeConstants.USER_STATUS_MAIL_TEMPLATE,
                user.getName(),
                request.status().equalsIgnoreCase(CafeConstants.STATUS_TRUE)
                        ? CafeConstants.USER_STATUS_APPROVED
                        : CafeConstants.USER_STATUS_DEACTIVATED,
                adminEmail
        );

        emailUtils.sendMail(
                user.getEmail(),
                CafeConstants.SUBJECT_ACCOUNT_STATUS,
                userMailBody
        );

        userDao.getAllAdminEmails().stream()
                .filter(email -> !email.equalsIgnoreCase(adminEmail))
                .forEach(email ->
                        emailUtils.sendMail(
                                email,
                                CafeConstants.SUBJECT_ADMIN_NOTIFICATION,
                                String.format(
                                        CafeConstants.ADMIN_STATUS_MAIL_TEMPLATE,
                                        adminEmail,
                                        user.getName(),
                                        user.getEmail(),
                                        request.status()
                                )
                        )
                );

        return CafeConstants.STATUS_UPDATED_SUCCESS;
    }

    // ---------------- CHECK TOKEN ----------------
    @Override
    public String checkToken() {
        return CafeConstants.TOKEN_VALID_RESPONSE;
    }

    // ---------------- CHANGE PASSWORD ----------------
    @Override
    public String changePassword(PasswordUpdate passwordUpdate) {

        User user = userDao.findByEmail(jwtFilter.getCurrentUser());

        if (ObjectUtils.isEmpty(user)) {
            throw new UserNotExists(CafeConstants.USER_NOT_FOUND_EMAIL);
        }

        if (!user.getPassword().equals(passwordUpdate.oldPassword())) {
            throw new PasswordException(CafeConstants.PASSWORD_MISMATCH);
        }

        user.setPassword(passwordUpdate.newPassword());
        userDao.save(user);

        return CafeConstants.PASSWORD_UPDATED_SUCCESS;
    }

    // ---------------- FORGOT PASSWORD ----------------
    @Override
    public String forgotPassword(ForgotPassword forgotPassword)
            throws MessagingException {

        User user = userDao.findByEmail(forgotPassword.email());

        if (ObjectUtils.isEmpty(user)) {
            throw new UserNotExists(CafeConstants.USER_NOT_FOUND_EMAIL);
        }


            emailUtils.forgotPassword(
                    forgotPassword.email(),
                    CafeConstants.SUBJECT_FORGOT_PASSWORD,
                    user.getPassword()
            );

        return CafeConstants.CHECK_MAIL_MESSAGE;
    }
}
