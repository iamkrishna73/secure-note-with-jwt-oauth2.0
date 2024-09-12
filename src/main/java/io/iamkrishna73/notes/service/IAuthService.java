package io.iamkrishna73.notes.service;

import io.iamkrishna73.notes.controller.dto.LoginRequest;
import io.iamkrishna73.notes.controller.dto.LoginResponse;
import io.iamkrishna73.notes.controller.dto.SignupRequest;
import io.iamkrishna73.notes.controller.dto.UserInfoResponse;
import org.springframework.security.core.userdetails.UserDetails;

public interface IAuthService {
    LoginResponse loginUser(LoginRequest loginRequest);

    void signupUser(SignupRequest signupRequest);

    UserInfoResponse getUserDeatils(UserDetails userDetails);

    String getCurrentUsername(UserDetails userDetails);
}
