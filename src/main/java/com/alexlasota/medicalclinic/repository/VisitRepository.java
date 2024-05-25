package com.alexlasota.medicalclinic.repository;

import com.alexlasota.medicalclinic.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitRepository extends JpaRepository<Visit, Long> {
}
