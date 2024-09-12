package io.iamkrishna73.notes.service;


import io.iamkrishna73.notes.constant.LoggingConstant;
import io.iamkrishna73.notes.controller.dto.LoginRequest;
import io.iamkrishna73.notes.controller.dto.LoginResponse;
import io.iamkrishna73.notes.controller.dto.SignupRequest;
import io.iamkrishna73.notes.controller.dto.UserInfoResponse;
import io.iamkrishna73.notes.entity.AppRole;
import io.iamkrishna73.notes.entity.Role;
import io.iamkrishna73.notes.entity.User;
import io.iamkrishna73.notes.error.ErrorMessages;
import io.iamkrishna73.notes.exception.BadCredentialsException;
import io.iamkrishna73.notes.exception.RoleNotFoundException;
import io.iamkrishna73.notes.exception.UserAlreadyExistsException;
import io.iamkrishna73.notes.exception.UserNotFoundException;
import io.iamkrishna73.notes.repository.RoleRepository;
import io.iamkrishna73.notes.repository.UserRepository;
import io.iamkrishna73.notes.security.jwt.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AuthService implements IAuthService {
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public LoginResponse loginUser(LoginRequest loginRequest) {
        var methodName = "AuthService:loginUser";
        log.info(LoggingConstant.START_METHOD_LOG, methodName, loginRequest);

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (AuthenticationException exception) {
            throw new BadCredentialsException(
                    ErrorMessages.PASSWORD_NOT_MATCHED.getErrorMessage(),
                    ErrorMessages.PASSWORD_NOT_MATCHED.getErrorCode()
            );

        }
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getAuthorities();

        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);

        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        LoginResponse response = new LoginResponse(userDetails.getUsername(), roles, jwtToken);
        log.info(LoggingConstant.END_METHOD_LOG, methodName);
        return response;

    }

    @Override
    public void signupUser(SignupRequest signupRequest) {
        var methodName = "AuthService:signupUser";
        log.info(LoggingConstant.START_METHOD_LOG, methodName, signupRequest);

        if (userRepository.existsByUserName(signupRequest.getUsername())) {
            log.error(LoggingConstant.ERROR_METHOD_LOG, methodName, signupRequest.getUsername() + "already exists");
            throw new UserAlreadyExistsException(
                    ErrorMessages.USER_ALREADY_EXISTS.getErrorMessage(),
                    ErrorMessages.USER_ALREADY_EXISTS.getErrorCode());
        }

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            log.error(LoggingConstant.ERROR_METHOD_LOG, methodName, signupRequest.getEmail() + "already exists");
            throw new UserAlreadyExistsException(
                    ErrorMessages.USER_ALREADY_EXISTS.getErrorMessage(),
                    ErrorMessages.USER_ALREADY_EXISTS.getErrorCode()
            );
        }
        var user = User.builder()
                .userName(signupRequest.getUsername())
                .email(signupRequest.getEmail())
                .password(passwordEncoder.encode(signupRequest.getPassword()));


        Set<String> initialRole = signupRequest.getRoles();
        Role role;
        if (initialRole == null || initialRole.isEmpty()) {
            role = roleRepository.findByRoleName(AppRole.ROLE_USER).orElseThrow(() -> {
                log.error(LoggingConstant.ERROR_METHOD_LOG, methodName, signupRequest.getRoles() + "role not found");
                throw new RoleNotFoundException(ErrorMessages.ROLE_NOT_FOUND.getErrorMessage(), ErrorMessages.USER_NOT_FOUND.getErrorCode());
            });
        } else {
            String roleStr = initialRole.iterator().next();
            if (roleStr.equals("admin")) {
                role = roleRepository.findByRoleName(AppRole.ROLE_ADMIN).orElseThrow(() -> {
                    log.error(LoggingConstant.ERROR_METHOD_LOG, methodName, signupRequest.getRoles() + "role not found");
                    throw new RoleNotFoundException(ErrorMessages.ROLE_NOT_FOUND.getErrorMessage(), ErrorMessages.USER_NOT_FOUND.getErrorCode());
                });
            } else {
                role = roleRepository.findByRoleName(AppRole.ROLE_USER).orElseThrow(() -> {
                    log.error(LoggingConstant.ERROR_METHOD_LOG, methodName, signupRequest.getRoles() + "role not found");
                    throw new RoleNotFoundException(ErrorMessages.ROLE_NOT_FOUND.getErrorMessage(), ErrorMessages.USER_NOT_FOUND.getErrorCode());
                });
            }
            user.accountNonLocked(true)
                    .accountNonLocked(true)
                    .credentialsNonExpired(true)
                    .enabled(true)
                    .credentialsExpiryDate(LocalDate.now().plusYears(1))
                    .accountExpiryDate(LocalDate.now().plusYears(1))
                    .isTwoFactorEnabled(false)
                    .signUpMethod("email").build();

        }
        user.role(role);
        log.info(LoggingConstant.END_METHOD_LOG, methodName);
        userRepository.save(user.build());
    }

    @Override
    public UserInfoResponse getUserDeatils(UserDetails userDetails) {
        var methodName = "AuthService:getUserDetails";
        log.info(LoggingConstant.START_METHOD_LOG, methodName, userDetails.getUsername());

        User user = userRepository.findByUserName(userDetails.getUsername()).orElseThrow(() -> {
            log.error(LoggingConstant.ERROR_METHOD_LOG, methodName, userDetails.getUsername() + "user not found");
            throw new UserNotFoundException(ErrorMessages.ROLE_NOT_FOUND.getErrorMessage(),
                    ErrorMessages.USER_NOT_FOUND.getErrorCode());
        });

        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());

        UserInfoResponse response = new UserInfoResponse(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                user.isAccountNonLocked(),
                user.isAccountNonExpired(),
                user.isCredentialsNonExpired(),
                user.isEnabled(),
                user.getCredentialsExpiryDate(),
                user.getAccountExpiryDate(),
                user.isTwoFactorEnabled(),
                roles
        );
        log.info(LoggingConstant.END_METHOD_LOG, methodName);

        return response;
    }

    @Override
    public String getCurrentUsername(UserDetails userDetails) {
        return (userDetails != null) ? userDetails.getUsername() : "user name is not found!";
    }
}
