package com.alexlasota.medicalclinic.service;

import com.alexlasota.medicalclinic.exceptions.MedicalClinicException;
import com.alexlasota.medicalclinic.model.Doctor;
import com.alexlasota.medicalclinic.model.Facility;
import com.alexlasota.medicalclinic.repository.DoctorRepository;
import com.alexlasota.medicalclinic.repository.FacilityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

public class DoctorServiceTest {

    DoctorService doctorService;
    DoctorRepository doctorRepository;
    FacilityRepository facilityRepository;

    @BeforeEach
    void setup() {
        this.doctorRepository = Mockito.mock(DoctorRepository.class);
        this.facilityRepository = Mockito.mock(FacilityRepository.class);
        this.doctorService = new DoctorService(doctorRepository, facilityRepository);
    }

    @Test
    void addDoctor_DoctorDoesntExist_DoctorSaved() {
        //given
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        when(doctorRepository.save(doctor)).thenReturn(doctor);
        //when
        Doctor addedDoctor = doctorService.addDoctor(doctor);
        //then
        Assertions.assertEquals(1L, addedDoctor.getId());
        Assertions.assertEquals(addedDoctor, doctor);
    }

    @Test
    void getDoctors_DoctorsExist_DoctorsReturned() {
        //given
        List<Doctor> doctorList = new ArrayList<>();
        doctorList.add(new Doctor());
        Page<Doctor> doctorPage = new PageImpl<>(doctorList);
        Pageable pageable = PageRequest.of(0, 10);
        when(doctorRepository.findAll(pageable)).thenReturn(doctorPage);
        //when
        List<Doctor> doctors = doctorService.getDoctors(pageable);
        //then
        Assertions.assertFalse(doctors.isEmpty());
        Assertions.assertEquals(1, doctors.size());
    }

    @Test
    void getDoctorById_DoctorWithIdExists_ReturnDoctor() {
        //given
        Long doctorId = 2L;
        Doctor doctor = new Doctor();
        doctor.setId(doctorId);
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));
        //when
        Doctor doctorById = doctorService.getDoctorById(doctorId);
        //then
        Assertions.assertEquals(2L, doctorById.getId());
    }

    @Test
    void getDoctorById_DoctorDoesntExists_ExceptionThrown() {
        //given
        Long doctorId = 2L;
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.empty());
        //when
        MedicalClinicException medicalClinicException = Assertions.assertThrows(MedicalClinicException.class, () -> doctorService.getDoctorById(doctorId));
        //then
        Assertions.assertEquals("Doctor with this ID doesnt exist", medicalClinicException.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, medicalClinicException.getHttpStatus());
    }

    @Test
    void assignDoctorToFacility_DoctorAndFacilityExist_DoctorSaved() {
        //given
        Long doctorId = 3L;
        Long facilityId = 1L;
        Doctor doctor = new Doctor();
        doctor.setId(doctorId);
        Facility facility = new Facility();
        facility.setId(facilityId);
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));
        when(facilityRepository.findById(facilityId)).thenReturn(Optional.of(facility));
        when(doctorRepository.save(doctor)).thenReturn(doctor);
        //when
        Doctor doctorToFacility = doctorService.assignDoctorToFacility(doctorId, facilityId);
        //then
        Assertions.assertEquals(3L, doctorToFacility.getId());
        Assertions.assertTrue(doctorToFacility.getFacilities().contains(facility));
    }

    @Test
    void assignDoctorToFacility_DoctorDoesntExist_ExceptionThrown() {
        //given
        Long doctorId = 3L;
        Long facilityId = 1L;
        Doctor doctor = new Doctor();
        doctor.setId(doctorId);
        Facility facility = new Facility();
        facility.setId(facilityId);
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.empty());
        //when
        MedicalClinicException medicalClinicException = Assertions.assertThrows(MedicalClinicException.class,
                () -> doctorService.assignDoctorToFacility(doctorId, facilityId));
        //then
        Assertions.assertEquals("Doctor with this ID doesnt exist", medicalClinicException.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, medicalClinicException.getHttpStatus());
    }

    @Test
    void assignDoctorToFacility_DoctorExists_FacilityDoesntExist_ExceptionThrown() {
        //given
        Long doctorId = 3L;
        Long facilityId = 1L;
        Doctor doctor = new Doctor();
        doctor.setId(doctorId);
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));
        when(facilityRepository.findById(facilityId)).thenReturn(Optional.empty());
        //when
        MedicalClinicException medicalClinicException = Assertions.assertThrows(MedicalClinicException.class,
                () -> doctorService.assignDoctorToFacility(doctorId, facilityId));
        //then
        Assertions.assertEquals("Facility with this ID doesnt exist", medicalClinicException.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, medicalClinicException.getHttpStatus());
    }
}