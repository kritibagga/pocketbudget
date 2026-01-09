package com.pocketbudget.pocketbudget.household;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name="households")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Household {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String name;

    @Column(unique=true)
    private String inviteCode;

}
