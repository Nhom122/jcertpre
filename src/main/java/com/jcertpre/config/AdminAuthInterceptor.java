package com.jcertpre.config;

import com.jcertpre.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class AdminAuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        HttpSession session = request.getSession(false);

        if (session != null) {
            User user = (User) session.getAttribute("currentUser");
            if (user != null && user.getRole() == User.Role.ADMIN) {
                return true; // Cho phép truy cập
            }
        }

        // Nếu không có quyền, chuyển hướng đến trang đăng nhập admin
        response.sendRedirect("/jcertpre/admin/login");
        return false;
    }
}
