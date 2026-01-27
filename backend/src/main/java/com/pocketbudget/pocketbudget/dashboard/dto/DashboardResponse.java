package com.pocketbudget.pocketbudget.dashboard.dto;

import java.math.BigDecimal;
import java.util.List;


public class DashboardResponse {


    public String month;

    public BigDecimal totalIncome;
    public BigDecimal totalExpense;
    public BigDecimal netBalance;

    public List<JarCardResponse> jars;

    public DashboardResponse(String month, BigDecimal totalIncome, BigDecimal totalExpense, BigDecimal netBalance,
            List<JarCardResponse> jars) {
        this.month = month;
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
        this.netBalance = netBalance;
        this.jars = jars;
    }



}
