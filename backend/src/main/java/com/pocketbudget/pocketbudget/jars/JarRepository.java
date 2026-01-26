package com.pocketbudget.pocketbudget.jars;

import java.util.Optional;
import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;

import com.pocketbudget.pocketbudget.jars.dto.JarResponse;

public interface JarRepository extends JpaRepository<Jar, Long> {
    List<JarResponse> findByHouseholdIdAndArchivedFalse(Long householdId);
    Optional<Jar> findByIdAndHouseholdId(Long id, Long householdId);

}
