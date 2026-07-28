package com.inn.cafe.Cafe.Management.service;

import com.inn.cafe.Cafe.Management.dto.request.*;
import com.inn.cafe.Cafe.Management.dto.response.UserResponse;
import jakarta.mail.MessagingException;

import java.util.List;

public interface UserService {

    String createUser(UserRequestDTO userRequest);

    String login(AuthenticationDto authenticationDto);

    List<UserResponse> getUsers();

    String updateStatus(StatusUpdateRequest statusUpdateRequest);

    String checkToken();

    String changePassword(PasswordUpdate passwordUpdate);

    String forgotPassword(ForgotPassword forgotPassword) throws MessagingException;
}
