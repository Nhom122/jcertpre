package com.jcertpre.controller;

import com.jcertpre.model.User;
import com.jcertpre.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProfileController {

    @Autowired
    private UserService userService;

    // Hiển thị trang chỉnh sửa hồ sơ cá nhân
    @GetMapping("/profile")
    public String viewProfile(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("loggedInUser");

        if (currentUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", currentUser);
        return "profile-view"; // -> profile.html
    }

    @GetMapping("/fromprofile")
    public String fromProfile(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("loggedInUser");

        if (currentUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", currentUser);
        return "updateprofile"; // -> profile.html
    }

    // Cập nhật hồ sơ và chuyển sang trang hiển thị
    @PostMapping("/formprofileapi")
    public String updateProfile(@ModelAttribute("user") User updatedUser,
                                HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("loggedInUser");

        if (currentUser == null) {
            return "redirect:/login";
        }

        // Cập nhật các thông tin
        currentUser.setFullName(updatedUser.getFullName());
        currentUser.setEmail(updatedUser.getEmail());
        currentUser.setNote(updatedUser.getNote()); // Thêm ghi chú

        // Không cập nhật mật khẩu ở đây nữa

        // Lưu vào database
        userService.updateUser(currentUser.getId(), currentUser);

        // Cập nhật lại session
        session.setAttribute("user", currentUser);

        // Gửi sang trang hiển thị thông tin
        model.addAttribute("user", currentUser);
        model.addAttribute("successMessage", "Cập nhật hồ sơ thành công!");
        return "profile-view"; // -> profile-view.html
    }
}
