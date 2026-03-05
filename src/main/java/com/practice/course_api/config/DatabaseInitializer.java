package com.practice.course_api.config;

import com.practice.course_api.entity.Role;
import com.practice.course_api.entity.User;
import com.practice.course_api.repository.RoleRepository;
import com.practice.course_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j // Dùng để in log ra console
public class DatabaseInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("Bắt đầu kiểm tra và khởi tạo dữ liệu mẫu...");

        // 1. Khởi tạo danh sách Role nếu database đang trống
        if (roleRepository.count() == 0) {
            Role roleUser = new Role(null, "USER", new HashSet<>());
            Role roleAdmin = new Role(null, "ADMIN", new HashSet<>());
            Role roleTrainer = new Role(null, "TRAINER", new HashSet<>());

            roleRepository.saveAll(List.of(roleUser, roleAdmin, roleTrainer));
            log.info("Đã tạo thành công 3 roles: USER, ADMIN, TRAINER.");
        }

        // 2. Khởi tạo một tài khoản ADMIN mặc định để test
        // Kiểm tra xem đã có user nào tên là 'admin' chưa
        if (userRepository.findByUsername("admin").isEmpty()) {

            // Lấy role ADMIN từ DB ra (lúc này chắc chắn đã có vì vừa tạo ở bước 1)
            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy Role ADMIN"));

            User adminUser = new User();
            adminUser.setName("System Administrator");
            adminUser.setUsername("admin"); // Dùng 'admin' làm username đăng nhập
            adminUser.setPasswordHash(passwordEncoder.encode("admin123")); // Phải băm mật khẩu
            adminUser.setIsActive(true);
            adminUser.setRole(adminRole);

            userRepository.save(adminUser);
            log.info("Đã tạo thành công tài khoản ADMIN mặc định. Username: admin / Password: admin123");
        }

        log.info("Quá trình khởi tạo dữ liệu hoàn tất!");
    }
}