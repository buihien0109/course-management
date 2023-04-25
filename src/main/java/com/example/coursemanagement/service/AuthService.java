package com.example.coursemanagement.service;

import com.example.coursemanagement.model.request.LoginRequest;
import jakarta.servlet.http.HttpSession;

public interface AuthService {
    String login(LoginRequest request, HttpSession session);
}
