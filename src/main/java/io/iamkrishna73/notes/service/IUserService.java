package io.iamkrishna73.notes.service;

import io.iamkrishna73.notes.entity.User;

import java.util.List;

public interface IUserService {
    List<User> getAllUsers();
    User getUserById(Long userId);
    void updateUserRole(Long userId, String roleName);

}
