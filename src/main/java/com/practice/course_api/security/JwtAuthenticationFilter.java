package com.practice.course_api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter { // Đảm bảo filter chỉ chạy 1 lần mỗi request

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Lấy header Authorization từ request
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // 2. Kiểm tra xem header có tồn tại và bắt đầu bằng "Bearer " không
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // Không có token, cho đi tiếp (sẽ bị block ở FilterSecurityInterceptor nếu API cần auth)
            return;
        }

        // 3. Cắt chuỗi để lấy đúng phần token (bỏ qua 7 ký tự "Bearer ")
        jwt = authHeader.substring(7);

        // 4. Gọi JwtService để lấy username từ token
        username = jwtService.extractUsername(jwt);

        // 5. Nếu có username và user này chưa được xác thực trong SecurityContext
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Lấy thông tin user từ DB
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // Kiểm tra token có hợp lệ không (chưa hết hạn, đúng username)
            if (jwtService.isTokenValid(jwt, userDetails)) {

                // Tạo object Authentication để báo cho Spring biết user này đã hợp lệ
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // Không cần pass vì dùng token
                        userDetails.getAuthorities() // Nạp danh sách Role vào đây (cái Set mà bạn đã code ở UserDetailsImpl)
                );

                // Cung cấp thêm chi tiết về request (IP, session id...)
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Lưu vào SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 6. Cho phép request đi tiếp tới các filter khác hoặc controller
        filterChain.doFilter(request, response);
    }
}