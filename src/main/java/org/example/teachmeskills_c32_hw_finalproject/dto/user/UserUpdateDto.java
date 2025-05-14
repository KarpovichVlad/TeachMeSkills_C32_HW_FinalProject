package org.example.teachmeskills_c32_hw_finalproject.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
public class UserUpdateDto {
    private String firstname;
    private String secondName;
    private Integer age;
    private String sex;
    private String telephoneNumber;
}

