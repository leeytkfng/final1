package com.example.demo.userservice.service;

import com.example.demo.userservice.dto.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public interface AuthService {
    Map<String, Object> login(LoginRequest loginRequest, HttpServletResponse response);
    String logout(HttpServletRequest request);
    String resolveToken(HttpServletRequest request);
    String refreshAccessToken(String refreshToken);
}
