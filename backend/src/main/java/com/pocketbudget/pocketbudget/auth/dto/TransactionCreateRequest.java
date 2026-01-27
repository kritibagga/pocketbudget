package com.pocketbudget.pocketbudget.auth.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.pocketbudget.pocketbudget.transactions.TransactionType;

import jakarta.validation.constraints.NotNull;

public class TransactionCreateRequest {
    @NotNull public TransactionType type;
    @NotNull public BigDecimal amount;
    @NotNull public LocalDate date;

    public Long jarId;//required for Expense, optional for income

    public String note;
}
