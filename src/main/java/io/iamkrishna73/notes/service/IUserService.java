package io.iamkrishna73.notes.service;

import io.iamkrishna73.notes.controller.dto.UserDTO;
import io.iamkrishna73.notes.entity.User;

import java.util.List;

public interface IUserService {
    List<User> getAllUsers();
    UserDTO getUserById(Long userId);
    void updateUserRole(Long userId, String roleName);

}
