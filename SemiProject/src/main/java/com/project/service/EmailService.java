package com.project.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.project.mapper.kdhSemiMapper;

import java.util.Random;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final kdhSemiMapper mapper;

    public EmailService(JavaMailSender mailSender, kdhSemiMapper mapper) {
        this.mailSender = mailSender;
        this.mapper = mapper;
    }

    public String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    public void sendVerificationEmail(String toEmail, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("이메일 인증 코드");
        message.setText("인증번호: " + code + "\n\n이메일 인증을 위해 이 번호를 입력해주세요.");
        message.setFrom("your-email@gmail.com");
        mailSender.send(message);
    }

    public void saveVerificationCode(String email, String code) {
        mapper.saveVerificationCode(email, code);
    }

    public String getVerificationCodeByEmail(String email) {
        return mapper.getVerificationCodeByEmail(email);
    }

    public boolean isEmailVerified(String email) {
        return mapper.isEmailVerified(email) > 0;
    }

    public boolean updateEmailVerifiedStatus(String email) {
        int rowsAffected = mapper.updateEmailVerifiedStatus(email);
        return rowsAffected > 0;
    }
    public boolean checkEmailExists(String email) {
        Integer count = mapper.countByEmail(email);
        return count != null && count > 0;
    }
}