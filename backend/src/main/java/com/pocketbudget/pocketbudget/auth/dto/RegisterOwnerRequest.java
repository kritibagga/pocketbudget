package com.pocketbudget.pocketbudget.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.*;

public class RegisterOwnerRequest {
    @Email @NotBlank
    public String email;

    @NotBlank @Size(min=6)
    public String password;

    @NotBlank
    public String householdName;

    

}
