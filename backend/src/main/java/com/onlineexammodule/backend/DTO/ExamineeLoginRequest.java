package com.onlineexammodule.backend.DTO;

public class ExamineeLoginRequest {
    
    private String email;
    private Long examinerId;

    // Getters and setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Long getExaminerId() { return examinerId; }
    public void setExaminerId(Long examinerId) { this.examinerId = examinerId; }
}
