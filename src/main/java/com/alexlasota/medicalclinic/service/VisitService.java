package com.alexlasota.medicalclinic.service;

import com.alexlasota.medicalclinic.exceptions.MedicalClinicException;
import com.alexlasota.medicalclinic.mapper.VisitMapper;
import com.alexlasota.medicalclinic.model.*;
import com.alexlasota.medicalclinic.repository.DoctorRepository;
import com.alexlasota.medicalclinic.repository.PatientRepository;
import com.alexlasota.medicalclinic.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VisitService {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final VisitRepository visitRepository;
    private final VisitMapper visitMapper;

    public Visit createVisit(Long doctorId, LocalDateTime visitStartDate, LocalDateTime visitEndDate) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new MedicalClinicException(HttpStatus.BAD_REQUEST, "Doctor not found"));

        if (visitStartDate.isBefore(visitEndDate)) {
            throw new MedicalClinicException(HttpStatus.BAD_REQUEST, "Visit start date cannot be after end date");
        }

        Visit visit = new Visit();
        visit.setDoctor(doctor);
        visit.setVisitStartDate(visitStartDate);
        visit.setVisitEndDate(visitEndDate);

        return visitRepository.save(visit);
    }

    public List<Visit> getVisits() {
        return visitRepository.findAll();
    }

    public VisitDto assignPatientToVisit(Long visitId, String patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new MedicalClinicException(HttpStatus.BAD_REQUEST, "Patient not found"));
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new MedicalClinicException(HttpStatus.BAD_REQUEST, "Visit not found"));

        if (visit.getPatient() == null) {
            visit.setPatient(patient);
        } else throw new MedicalClinicException(HttpStatus.BAD_REQUEST, "Visit not found");

        visitRepository.save(visit);
        return visitMapper.visitToVisitDto(visit);
    }
}