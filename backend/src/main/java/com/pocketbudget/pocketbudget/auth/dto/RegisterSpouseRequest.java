package com.pocketbudget.pocketbudget.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterSpouseRequest {
    @Email @NotBlank public String email;
    @NotBlank @Size(min=6) public String password;
    @NotBlank public String inviteCode;
}
