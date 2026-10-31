package com.inn.cafe.Cafe.Management.rest;

import com.inn.cafe.Cafe.Management.dto.request.*;
import com.inn.cafe.Cafe.Management.dto.response.UserResponse;
import com.inn.cafe.Cafe.Management.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:4200")
public class UserRestImpl  {


    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> createUser(@Valid @RequestBody UserRequestDTO userRequest) {
        return ResponseEntity.ok(userService.createUser(userRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@Valid @RequestBody AuthenticationDto authenticationDto){
        return ResponseEntity.ok(userService.login(authenticationDto));
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateStatus(@Valid @RequestBody StatusUpdateRequest statusUpdateRequest){
        return  ResponseEntity.ok(userService.updateStatus(statusUpdateRequest));
    }


    @GetMapping("/get")
    public ResponseEntity<List<UserResponse>> getUsers(){
        return ResponseEntity.ok(userService.getUsers());
    }

    @GetMapping("/checkToken")
    public ResponseEntity<String> checkToken()
    {
        return ResponseEntity.ok(userService.checkToken());
    }

    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@Valid @RequestBody PasswordUpdate passwordUpdate){
        return ResponseEntity.ok(userService.changePassword(passwordUpdate));

    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPassword forgotPassword) throws MessagingException {
           return ResponseEntity.ok(userService.forgotPassword(forgotPassword));
    }


}
