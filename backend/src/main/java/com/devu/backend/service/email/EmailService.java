package com.devu.backend.service.email;

public interface EmailService {
    String sendValidationMail(String to) throws Exception;
}
