package com.pocketbudget.pocketbudget.jars;

import java.math.BigDecimal;

import com.pocketbudget.pocketbudget.household.Household;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="jars")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Jar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name="household_id")
    private Household household;

    @Column(nullable=false)
    private String name;

    @Column(nullable=false, precision = 12, scale=2)
    private BigDecimal monthlyLimit;

    @Column(nullable=false)
    private String icon;

    @Column(nullable = false)
    private String color;

    @Column(nullable=false)
    private boolean archived;



}
