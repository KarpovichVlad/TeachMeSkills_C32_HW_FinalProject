package org.example.teachmeskills_c32_hw_finalproject.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String firstname;
    private String secondName;
    private Integer age;
    private String email;
    private String sex;
    private String telephoneNumber;

}
