package com.pocketbudget.pocketbudget.transactions;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.pocketbudget.pocketbudget.household.Household;
import com.pocketbudget.pocketbudget.jars.Jar;
import com.pocketbudget.pocketbudget.user.User;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Transaction {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    //household scoping
    @ManyToOne(optional = false)
    @JoinColumn(name="household_id")
    private Household household;

    @ManyToOne(optional = false)
    @JoinColumn(name="jar_id")
    private Jar jar;

    @ManyToOne(optional = false)
    @JoinColumn(name="created_by_user_id")
    private User createdBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(nullable=false, precision=12, scale=2)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDate date;

    @Column(length=300)
    private String note;













}
