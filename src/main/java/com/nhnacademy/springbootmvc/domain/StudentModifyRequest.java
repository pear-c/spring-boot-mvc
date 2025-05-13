package com.nhnacademy.springbootmvc.domain;

import jakarta.validation.constraints.*;
import lombok.Value;

@Value
public class StudentModifyRequest {
    String id;
    String password;

    @NotBlank
    String name;

    @Email
    String email;

    @Min(0)
    @Max(100)
    int score;

    @NotBlank
    @Size(max = 200)
    String comment;
}
