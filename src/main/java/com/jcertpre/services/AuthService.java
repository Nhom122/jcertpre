package com.jcertpre.services;

import com.jcertpre.model.User;
import com.jcertpre.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service // ✅ Bắt buộc: để Spring quản lý class này như 1 Bean
public class AuthService {

    @Autowired
    private IUserRepository userRepository;

    /**
     * Đăng ký người dùng mới
     * @param user đối tượng người dùng
     * @return true nếu đăng ký thành công, false nếu email đã tồn tại
     */
    public boolean register(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return false; // email đã tồn tại
        }
        user.setRole(User.Role.LEARNER); // mặc định là học viên
        userRepository.save(user);
        return true;
    }

    /**
     * Đăng nhập với email và mật khẩu
     * @param email email người dùng
     * @param password mật khẩu
     * @return User nếu hợp lệ, null nếu sai thông tin
     */
    public User login(String email, String password) {
        return userRepository.findByEmail(email).orElse(null);
    }
}
