package com.alexlasota.medicalclinic.repository;

import com.alexlasota.medicalclinic.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface VisitRepository extends JpaRepository<Visit, Long> {

    @Query("select v " +
            "from Visit v " +
            "where :doctorId = v.doctor.id " +
            "and v.visitStartDate <= :visitEndDate " +
            "and v.visitEndDate >= :visitStartDate")
    List<Visit> findAllOverlapping(Long doctorId, LocalDateTime visitStartDate, LocalDateTime visitEndDate);
}