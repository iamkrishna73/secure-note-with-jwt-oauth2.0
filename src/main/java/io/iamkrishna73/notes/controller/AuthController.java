package io.iamkrishna73.notes.controller;

import io.iamkrishna73.notes.constant.LoggingConstant;
import io.iamkrishna73.notes.controller.dto.*;
import io.iamkrishna73.notes.service.IAuthService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    IAuthService authService;

    @PostMapping("/public/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        var methodName = "AuthController:loginUser";
        log.info(LoggingConstant.START_METHOD_LOG, methodName, loginRequest);

        LoginResponse response = authService.loginUser(loginRequest);

        log.info(LoggingConstant.END_METHOD_LOG, methodName);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/public/signin")
    public ResponseEntity<?> signupNewUser(@Valid @RequestBody SignupRequest signupRequest) {
        var methodName = "AuthController:signupUser";
        log.info(LoggingConstant.START_METHOD_LOG, methodName, signupRequest);

        authService.signupUser(signupRequest);

        log.info(LoggingConstant.END_METHOD_LOG, methodName);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails(@AuthenticationPrincipal UserDetails userDetails) {
        var methodName = "AuthController:getUserDetails";
        log.info(LoggingConstant.START_METHOD_LOG, methodName, userDetails.getUsername());

        UserInfoResponse response = authService.getUserDetails(userDetails);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/username")
    public ResponseEntity<String> getCurrentUsername(@AuthenticationPrincipal UserDetails userDetails) {
        var methodName = "AuthController:getCurrentUsername ";
        log.info(LoggingConstant.START_METHOD_LOG, methodName, userDetails.getUsername());
        String username = authService.getCurrentUsername(userDetails);
        log.info(LoggingConstant.END_METHOD_LOG, methodName);
        return new ResponseEntity<>(username, HttpStatus.FOUND);
    }
}
