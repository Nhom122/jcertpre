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

    // ===================== 1. Đăng ký người dùng =====================

    public User registerLearner(String email, String password, String fullName) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email đã tồn tại.");
        }
        User newUser = new User(email, password, fullName, Role.LEARNER);
        return userRepository.save(newUser);
    }

    public User registerUser(String email, String password, String fullName, Role role) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email đã tồn tại.");
        }
        User newUser = new User(email, password, fullName, role);
        return userRepository.save(newUser);
    }

    // ===================== 2. Đăng nhập =====================

    public User loginUser(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            return userOpt.get();
        } else {
            throw new RuntimeException("Email hoặc mật khẩu không đúng.");
        }
    }

    // ===================== 3. Truy vấn người dùng =====================

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getUsersByRole(Role role) {
        return userRepository.findByRole(role);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + id));
    }

    // ===================== 4. Cập nhật thông tin người dùng =====================

    public User updateUser(Long id, User updatedUser) {
        User existingUser = getUserById(id);
        existingUser.setFullName(updatedUser.getFullName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPassword(updatedUser.getPassword());
        existingUser.setRole(updatedUser.getRole());
        return userRepository.save(existingUser);
    }

    // ===================== 5. Xóa người dùng (hủy liên kết đúng cách) =====================

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng."));

        if (user.getRole() == Role.INSTRUCTOR) {
            // Xử lý cho giảng viên: xóa bài giảng, khóa học, gỡ học viên
            List<Course> courses = courseRepository.findByInstructor(user);
            for (Course course : courses) {
                lessonRepository.deleteByCourseId(course.getId());
                enrollmentRepository.deleteByCourseId(course.getId());
            }
            courseRepository.deleteAll(courses);

        } else if (user.getRole() == Role.LEARNER) {
            // Xử lý cho học viên: hủy liên kết nhưng giữ lại dữ liệu
            advisorStudyPlanRepository.deleteByUserId(id);
            consultationScheduleRepository.deleteByUserId(id);
            enrollmentRepository.detachLearnerByUserId(id);
            feedbackRepository.detachLearnerByUserId(id); // ⚠️ Gỡ liên kết chứ không xóa feedback
        }

        userRepository.deleteById(id);
    }

    // ===================== 6. Tạo user từ admin =====================

    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email đã tồn tại.");
        }
        // Có thể thêm mã hóa mật khẩu tại đây nếu cần
        return userRepository.save(user);
    }
}
