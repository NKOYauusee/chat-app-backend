package com.example.web_back.entity.po;


import jakarta.validation.Valid;
import lombok.Data;

@Data
public class VerifiedUser {
    String code;
    @Valid
    User user;
}
