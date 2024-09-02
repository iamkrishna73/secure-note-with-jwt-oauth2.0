package io.iamkrishna73.notes.service;

import io.iamkrishna73.notes.entity.AppRole;
import io.iamkrishna73.notes.entity.Role;
import io.iamkrishna73.notes.entity.User;
import io.iamkrishna73.notes.repository.RoleRepository;
import io.iamkrishna73.notes.repository.UserRepository;
import io.iamkrishna73.notes.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService{
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return UserMapper.convertToUserDto(user);
    }
    @Override
    public void updateUserRole(Long userId, String roleName) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new RuntimeException("User not found"));
        AppRole appRole = AppRole.valueOf(roleName);
        Role role = roleRepository.findByRoleName(appRole)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        user.setRole(role);
        userRepository.save(user);
    }
}
