package com.pocketbudget.pocketbudget.jars.dto;

import java.math.BigDecimal;

import lombok.Getter;

@Getter
public class JarResponse {

    public Long id;
    public String name;
    public BigDecimal monthlyLimit;
    public String icon;
    public String color;
    public boolean archived;


    public JarResponse(Long id, String name, BigDecimal monthlyLimit, String icon, String color, boolean archived) {
        this.id = id;
        this.name = name;
        this.monthlyLimit = monthlyLimit;
        this.icon = icon;
        this.color = color;
        this.archived = archived;
    }

}
