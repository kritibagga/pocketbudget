package com.pocketbudget.pocketbudget.auth.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.pocketbudget.pocketbudget.transactions.TransactionType;

public class TransactionResponse {

    public Long id;
    public TransactionType type;
    public BigDecimal amount;
    public LocalDate date;
    public Long jarId;
    public String note;
    public Long createdByUserId;

    
    public TransactionResponse(Long id, TransactionType type, BigDecimal amount, LocalDate date, Long jarId,
            String note, Long createdByUserId) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.date = date;
        this.jarId = jarId;
        this.note = note;
        this.createdByUserId = createdByUserId;
    }



}
