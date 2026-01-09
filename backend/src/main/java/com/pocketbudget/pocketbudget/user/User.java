package com.pocketbudget.pocketbudget.user;

import com.pocketbudget.pocketbudget.household.Household;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true)
    private String email;

    @Column(nullable = false, unique = true)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;


    @ManyToOne(optional = false)
    @JoinColumn(name="household_id")
    private Household household;


}
