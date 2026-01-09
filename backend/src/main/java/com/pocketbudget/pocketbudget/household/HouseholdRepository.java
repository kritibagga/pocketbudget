package com.pocketbudget.pocketbudget.household;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HouseholdRepository extends JpaRepository<Household, Long> {
    Optional<Household> findByInviteCode(String inviteCode);

}
