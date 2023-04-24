package com.example.coursemanagement.controller;

import com.example.coursemanagement.entity.User;
import com.example.coursemanagement.model.request.LoginRequest;
import com.example.coursemanagement.security.ICurrentUser;
import com.example.coursemanagement.service.AuthService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final ICurrentUser iCurrentUser;

    @GetMapping("admin/login")
    public String getLoginPage() {
        User user = iCurrentUser.getUser();
        if(user != null) {
            return "redirect:/admin/courses";
        }
        return "admin/login/login";
    }

    @PostMapping("api/admin/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpSession session) {
        return ResponseEntity.ok(authService.login(request, session));
    }
}
