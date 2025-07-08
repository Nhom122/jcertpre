package com.jcertpre.services;

import com.jcertpre.model.Course;
import com.jcertpre.model.User;
import com.jcertpre.model.User.Role;
import com.jcertpre.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IEnrollmentRepository enrollmentRepository;

    @Autowired
    private IConsultationScheduleRepository consultationScheduleRepository;

    @Autowired
    private IAdvisorStudyPlanRepository advisorStudyPlanRepository;

    @Autowired
    private ICourseRepository courseRepository;

    @Autowired
    private IFeedbackRepository feedbackRepository;

    @Autowired
    private ILessonRepository lessonRepository;

    // Đăng ký tài khoản Learner mới
    public User registerLearner(String email, String password, String fullName) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already in use.");
        }
        User newUser = new User(email, password, fullName, Role.LEARNER);
        return userRepository.save(newUser);
    }

    // Đăng ký tài khoản với role tùy chọn
    public User registerUser(String email, String password, String fullName, Role role) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already in use.");
        }
        User newUser = new User(email, password, fullName, role);
        return userRepository.save(newUser);
    }

    // Đăng nhập (basic check)
    public User loginUser(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            return userOpt.get();
        } else {
            throw new RuntimeException("Invalid email or password.");
        }
    }

    // Lấy danh sách tất cả người dùng
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Lấy danh sách người dùng theo vai trò
    public List<User> getUsersByRole(Role role) {
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

    // ✅ Xóa người dùng theo ID (tùy theo role)
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() == Role.INSTRUCTOR) {
            // Lấy tất cả khóa học do giảng viên tạo
            List<Course> courses = courseRepository.findByInstructor(user);

            for (Course course : courses) {
                // Xóa bài giảng của khóa học
                lessonRepository.deleteByCourseId(course.getId());

                // Gỡ học viên khỏi khóa học
                enrollmentRepository.deleteByCourseId(course.getId());
            }

            // Xóa toàn bộ khóa học
            courseRepository.deleteAll(courses);

        } else if (user.getRole() == Role.LEARNER) {
            advisorStudyPlanRepository.deleteByUserId(id);
            consultationScheduleRepository.deleteByUserId(id);
            enrollmentRepository.detachLearnerByUserId(id);
            feedbackRepository.deleteByLearnerId(id);
        }

        // Cuối cùng: xóa người dùng
        userRepository.deleteById(id);
    }

    // Tạo người dùng từ phía Admin
    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email đã tồn tại.");
        }

        // ⚠️ Có thể mã hóa mật khẩu tại đây nếu cần
        return userRepository.save(user);
    }
}
