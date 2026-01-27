package com.pocketbudget.pocketbudget.jars;

import java.util.Optional;
import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;



public interface JarRepository extends JpaRepository<Jar, Long> {
    List<Jar> findByHouseholdIdAndArchivedFalse(Long householdId);
    Optional<Jar> findByIdAndHouseholdId(Long id, Long householdId);

}
