package com.alexlasota.medicalclinic.service;

import com.alexlasota.medicalclinic.exceptions.MedicalClinicException;
import com.alexlasota.medicalclinic.mapper.VisitMapper;
import com.alexlasota.medicalclinic.model.*;
import com.alexlasota.medicalclinic.repository.DoctorRepository;
import com.alexlasota.medicalclinic.repository.PatientRepository;
import com.alexlasota.medicalclinic.repository.VisitRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class VisitServiceTest {

    VisitService visitService;
    VisitRepository visitRepository;
    VisitMapper visitMapper;
    DoctorRepository doctorRepository;
    PatientRepository patientRepository;

    @BeforeEach
    void setup() {
        this.doctorRepository = Mockito.mock(DoctorRepository.class);
        this.patientRepository = Mockito.mock(PatientRepository.class);
        this.visitRepository = Mockito.mock(VisitRepository.class);
        this.visitMapper = Mappers.getMapper(VisitMapper.class);
        this.visitService = new VisitService(doctorRepository, patientRepository, visitRepository, visitMapper);
    }

    @Test
    void getVisits_VisitsExists_VisitsReturned() {
        //given
        List<Visit> visits = new ArrayList<>();
        visits.add(createVisit());
        Page<Visit> visitPage = new PageImpl<>(visits);
        Pageable pageable = PageRequest.of(0, 10);
        when(visitRepository.findAll(pageable)).thenReturn(visitPage);
        //when
        List<Visit> result = visitService.getVisits(pageable);
        //then
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(1, result.get(0).getId());
    }

    @Test
    void createVisit_DoctorExists_DataIsValid_VisitSaved() {
        //given
        Doctor doctor = createDoctor(5L);
        VisitRequestDto requestDto = new VisitRequestDto();
        requestDto.setDoctorId(doctor.getId());
        requestDto.setVisitStartDate(LocalDateTime.of(2025, 1, 1, 10, 15));
        requestDto.setVisitEndDate(LocalDateTime.of(2025, 1, 3, 12, 15));

        Visit visit = new Visit();
        visit.setId(1L);
        visit.setDoctor(doctor);
        visit.setVisitStartDate(requestDto.getVisitStartDate());
        visit.setVisitEndDate(requestDto.getVisitEndDate());

        SimpleDoctorDto simpleDoctorDto = new SimpleDoctorDto(1L, "Alex", "12124121", "Fizio");
        SimpleVisitDto simpleVisitDto = new SimpleVisitDto((LocalDateTime.of(2025, 1, 1, 10, 15)), (LocalDateTime.of(2025, 1, 3, 12, 15)), simpleDoctorDto);

        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));
        when(visitRepository.findById(visit.getId())).thenReturn(Optional.of(visit));
        when(visitRepository.save(any())).thenReturn(visit);

        //when
        SimpleVisitDto dto = visitService.createVisit(requestDto);
        //then
        Assertions.assertEquals(simpleVisitDto.getVisitStartDate(), dto.getVisitStartDate());
    }

    @Test
    void createVisit_InvalidQuarterData_ExceptionThrown() {
        // given
        VisitRequestDto requestDto = new VisitRequestDto();
        requestDto.setDoctorId(1L);
        requestDto.setVisitStartDate(LocalDateTime.of(2025, 1, 1, 10, 12));
        requestDto.setVisitEndDate(LocalDateTime.of(2025, 1, 3, 12, 11));
        // when
        MedicalClinicException exception = Assertions.assertThrows(MedicalClinicException.class,
                () -> visitService.createVisit(requestDto));
        // then
        Assertions.assertEquals("Visit time must be on a quarter-hour mark (e.g., 14:00, 14:15, 14:30, etc.)", exception.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    void createVisit_QuarterValidationCorrect_DoctorDoesntExist_ExceptionThrown() {
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        VisitRequestDto requestDto = new VisitRequestDto();
        requestDto.setDoctorId(1L);
        requestDto.setVisitStartDate(LocalDateTime.of(2025, 1, 1, 10, 15));
        requestDto.setVisitEndDate(LocalDateTime.of(2025, 1, 3, 12, 15));
        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.empty());
        MedicalClinicException exception = Assertions.assertThrows(MedicalClinicException.class,
                () -> visitService.createVisit(requestDto));
        Assertions.assertEquals("Doctor not found", exception.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    void createVisit_QuarterValidationCorrect_DoctorExist_TimesAreOverlapping_ExceptionThrown() {
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        VisitRequestDto requestDto = new VisitRequestDto();
        requestDto.setDoctorId(1L);
        requestDto.setVisitStartDate(LocalDateTime.of(2025, 1, 1, 10, 15));
        requestDto.setVisitEndDate(LocalDateTime.of(2025, 1, 3, 12, 15));
        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));
        when(visitRepository.findAllOverlapping(doctor.getId(), requestDto.getVisitStartDate(), requestDto.getVisitEndDate())).thenReturn(List.of(new Visit()));


        MedicalClinicException exception = Assertions.assertThrows(MedicalClinicException.class,
                () -> visitService.createVisit(requestDto));

        Assertions.assertEquals("Cannot create visit. Visits are overlapping!", exception.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    void assignPatientToVisit_PatientAndVisitExist_PatientAndVisitSaved() {
        //given
        Long visitId = 1L;
        Long patientId = 1L;
        Patient patient = new Patient();
        patient.setId(patientId);
        Visit visit = new Visit();
        visit.setId(visitId);

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(visitRepository.findById(visitId)).thenReturn(Optional.of(visit));
        when(visitRepository.save(visit)).thenReturn(visit);
        //when
        VisitDto result = visitService.assignPatientToVisit(visitId, patientId);
        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, visit.getPatient().getId());
    }

    @Test
    void assignPatientToVisit_PacientDoesntExist_ExceptionThrown(){
        Long visitId = 1L;
        Long patientId = 1L;
        Patient patient = new Patient();
        patient.setId(patientId);
        Visit visit = new Visit();
        visit.setId(visitId);

        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        MedicalClinicException exception = Assertions.assertThrows(MedicalClinicException.class,
                () -> visitService.assignPatientToVisit(visitId,patientId));

        Assertions.assertEquals("Patient not found", exception.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    void assignPatientToVisit_PacientExist_VisitDoesntExist_ExceptionThrown(){
        Long visitId = 1L;
        Long patientId = 1L;
        Patient patient = new Patient();
        patient.setId(patientId);
        Visit visit = new Visit();
        visit.setId(visitId);

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(visitRepository.findById(visitId)).thenReturn(Optional.of(visit));


        MedicalClinicException exception = Assertions.assertThrows(MedicalClinicException.class,
                () -> visitService.assignPatientToVisit(visitId,patientId));

        Assertions.assertEquals("Visit not found", exception.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    void assignPatientToVisit_PacientAndVisitExist_VisitAlreadyHasPatientAssigned_ExceptionThrown(){
        Long visitId = 1L;
        Long patientId = 1L;
        Patient patient = new Patient();
        patient.setId(patientId);
        Visit visit = new Visit();
        visit.setId(visitId);
        visit.setPatient(patient);

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(visitRepository.findById(visitId)).thenReturn(Optional.of(visit));


        MedicalClinicException exception = Assertions.assertThrows(MedicalClinicException.class,
                () -> visitService.assignPatientToVisit(visitId,patientId));

        Assertions.assertEquals("Visit already assigned", exception.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }


    private Visit createVisit() {
        MedicalUser patientUser = new MedicalUser();
        patientUser.setId(1L);
        patientUser.setEmail("patient@gm.com");
        patientUser.setPassword("haslo123");
        patientUser.setFirstName("Ali");
        patientUser.setLastName("Baba");

        Patient patient = new Patient();
        patient.setId(1L);
        patient.setIdCardNo("ABC1");
        patient.setPhoneNumber("82828282");
        patient.setBirthday("1999.03.16");
        patient.setMedicalUser(patientUser);
        patient.setVisits(new ArrayList<>());

        MedicalUser doctorUser = new MedicalUser();
        doctorUser.setId(2L);
        doctorUser.setEmail("al@gmail.com");
        doctorUser.setPassword("1234566");
        doctorUser.setFirstName("Alex");
        doctorUser.setLastName("Las");

        Doctor doctor = new Doctor();
        doctor.setId(2L);
        doctor.setSpecialization("Fizio");
        doctor.setMedicalUser(doctorUser);

        Visit visit = new Visit();
        visit.setId(3L);
        visit.setVisitStartDate(LocalDateTime.of(2024, 1, 1, 10, 15));
        visit.setVisitEndDate(LocalDateTime.of(2024, 1, 1, 12, 15));
        visit.setPatient(patient);
        visit.setDoctor(doctor);

        return visit;
    }

    Doctor createDoctor(Long id) {
        MedicalUser doctorUser = new MedicalUser();
        doctorUser.setId(id);
        doctorUser.setEmail("aB@.com");
        doctorUser.setPassword("123333");
        doctorUser.setFirstName("Alex");
        doctorUser.setLastName("Lasota");

        Doctor doctor = new Doctor();
        doctor.setId(id);
        doctor.setSpecialization("Fizio");
        doctor.setMedicalUser(doctorUser);

        return doctor;
    }
}