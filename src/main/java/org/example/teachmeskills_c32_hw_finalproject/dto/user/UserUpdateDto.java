package org.example.teachmeskills_c32_hw_finalproject.dto.user;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateDto {
    @NotBlank
    private String firstname;

    @NotBlank
    private String secondName;

    @Min(value = 0)
    @Max(value = 120)
    private Integer age;
    private String sex;

    @Size(max = 12)
    private String telephoneNumber;
}

