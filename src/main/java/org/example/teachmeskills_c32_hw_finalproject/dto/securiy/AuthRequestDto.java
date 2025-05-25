package org.example.teachmeskills_c32_hw_finalproject.dto.securiy;

import lombok.Data;

@Data
public class AuthRequestDto {
    private String login;
    private String password;
}
