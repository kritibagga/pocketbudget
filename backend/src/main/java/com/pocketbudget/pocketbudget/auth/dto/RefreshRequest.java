package com.pocketbudget.pocketbudget.auth.dto;

import jakarta.validation.constraints.NotBlank;

public class RefreshRequest {
    @NotBlank
    public String refreshToken;

}
