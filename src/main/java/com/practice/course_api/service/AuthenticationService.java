package com.practice.course_api.service;

import com.practice.course_api.dto.AuthResponse;
import com.practice.course_api.dto.LoginRequest;
import com.practice.course_api.dto.RegisterRequest;
import com.practice.course_api.entity.Role;
import com.practice.course_api.entity.User;
import com.practice.course_api.repository.RoleRepository;
import com.practice.course_api.repository.UserRepository;
import com.practice.course_api.security.JwtService;
import com.practice.course_api.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Tìm role USER mặc định (bạn nhớ insert data mẫu các role vào bảng tbl_role nhé)
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Lỗi: Không tìm thấy Role mặc định."));

        // Tạo Entity User
        User user = new User();
        user.setName(request.getName());
        user.setUsername(request.getName());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword())); // Phải băm mật khẩu
        user.setIsActive(true);
        user.setRole(userRole);

        userRepository.save(user);

        // Chuyển sang UserDetailsImpl để JWTService có thể tạo token
        UserDetailsImpl userDetails = UserDetailsImpl.build(user);

        // Trả về Token ngay sau khi đăng ký thành công
        String jwtToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        // AuthenticationManager sẽ tự động gọi UserDetailsService và so sánh mật khẩu đã hash
        // Nếu sai mật khẩu, hàm này ném ra exception BadCredentialsException ngay lập tức
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // Nếu qua được bước trên nghĩa là đăng nhập thành công
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy user"));

        UserDetailsImpl userDetails = UserDetailsImpl.build(user);

        String jwtToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }
}