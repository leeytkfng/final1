package com.example.demo.userservice.service;

public interface VerificationService {
    void sendVerificationCode(String email);
    boolean verifyCode(String email, String code);
}
