package com.pocketbudget.pocketbudget.dashboard.dto;

import java.math.BigDecimal;

public class JarCardResponse {

    public Long jarId;
    public String name;
    public String icon;
    public String color;

    public BigDecimal limit;
    public BigDecimal spent;
    public BigDecimal remaining;

    public JarStatus status;

    public JarCardResponse(Long jarId, String name, String icon, String color, BigDecimal limit, BigDecimal spent,
            BigDecimal remaining, JarStatus status) {
        this.jarId = jarId;
        this.name = name;
        this.icon = icon;
        this.color = color;
        this.limit = limit;
        this.spent = spent;
        this.remaining = remaining;
        this.status = status;
    }

}
