package com.pocketbudget.pocketbudget.transactions;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction ,Long>{

    List<Transaction> findByHouseholdIdAndDateBetweenOrderByDateDesc(Long householdId, LocalDate start, LocalDate end);
    Optional<Transaction> findByIdAndHouseholdId(Long id, Long householdId);

}
