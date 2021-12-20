package com.personalproject.integrated.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LoginDto {
    @NotNull
    private String email;

    @NotNull
    private String password;
}
