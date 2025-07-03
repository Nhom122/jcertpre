package com.jcertpre.services;

import com.jcertpre.model.User;
import com.jcertpre.model.User.Role;
import com.jcertpre.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private IUserRepository userRepository;

    // Đăng ký tài khoản Learner mới
    public User registerLearner(String email, String password, String fullName) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already in use.");
        }
        User newUser = new User(email, password, fullName, Role.LEARNER);
        return userRepository.save(newUser);
    }

    // Đăng ký tài khoản mới
    public User registerUser(String email, String password, String fullName, User.Role role) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already in use.");
        }
        User newUser = new User(email, password, fullName, role);
        return userRepository.save(newUser);
    }

    // Đăng nhập (basic check)
    public User loginUser(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(user -> user.getPassword().equals(password))
                .orElseThrow(() -> new RuntimeException("Email hoặc mật khẩu không đúng."));
    }

    // Lấy danh sách tất cả người dùng
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Lấy danh sách người dùng theo vai trò
    public List<User> getUsersByRole(User.Role role) {
        return userRepository.findByRole(role);
    }

    // Lấy người dùng theo ID
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + id));
    }

    // Cập nhật thông tin người dùng
    public User updateUser(Long id, User updatedUser) {
        User existingUser = getUserById(id);
        existingUser.setFullName(updatedUser.getFullName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPassword(updatedUser.getPassword());
        existingUser.setRole(updatedUser.getRole());
        return userRepository.save(existingUser);
    }

    // Xóa người dùng theo ID
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Người dùng không tồn tại với ID: " + id);
        }
        userRepository.deleteById(id);
    }



}
