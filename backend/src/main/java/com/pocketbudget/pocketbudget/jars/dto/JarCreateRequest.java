package com.pocketbudget.pocketbudget.jars.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class JarCreateRequest {
    @NotBlank public String name;
    @NotNull public BigDecimal monthlyLimit;
    @NotBlank public String icon;
    @NotBlank public String color;

}
