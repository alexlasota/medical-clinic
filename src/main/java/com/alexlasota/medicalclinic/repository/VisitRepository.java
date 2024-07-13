package com.alexlasota.medicalclinic.repository;

import com.alexlasota.medicalclinic.model.Visit;
import com.alexlasota.medicalclinic.model.VisitDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

public interface VisitRepository extends JpaRepository<Visit, Long> {

    @Query("select v " +
            "from Visit v " +
            "where :doctorId = v.doctor.id " +
            "and v.visitStartDate <= :visitEndDate " +
            "and v.visitEndDate >= :visitStartDate")
    List<Visit> findAllOverlapping(Long doctorId, LocalDateTime visitStartDate, LocalDateTime visitEndDate);

    @Query("SELECT v " +
            "FROM Visit v " +
            "WHERE v.patient.id = :patientId")
    List<VisitDto> findVisitsByPatientId(@RequestParam("patientId") Long patientId);

    @Query("SELECT v " +
            "FROM Visit v " +
            "WHERE v.doctor.id = :doctorId " +
            "AND v.visitStartDate > CURRENT_TIMESTAMP")
    List<VisitDto> findAvailableVisitsByDoctorId(@RequestParam("doctorId") Long doctorId);

    @Query("SELECT v " +
            "FROM Visit v " +
            "WHERE v.doctor.specialization = :specialization " +
            "AND v.visitStartDate BETWEEN :startDate AND :endDate")
    List<VisitDto> findAvailableVisitsBySpecializationAndDate(@RequestParam("specialization") String specialization, @RequestParam("startDate") LocalDateTime startDate, @RequestParam("endDate") LocalDateTime endDate);
}