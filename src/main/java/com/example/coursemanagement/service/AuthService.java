package com.example.coursemanagement.service;

import com.example.coursemanagement.exception.BadRequestException;
import com.example.coursemanagement.model.request.LoginRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;

    public String login(LoginRequest request, HttpSession session) {
        // Tạo đối tượng xác thực
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        );

        try {
            // Tiến hành xác thực
            Authentication authentication = authenticationManager.authenticate(token);

            // Lưu dữ liệu vào context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Tạo ra session
            session.setAttribute("MY_SESSION", authentication.getName());

            return "Đăng nhập thành công";
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}
