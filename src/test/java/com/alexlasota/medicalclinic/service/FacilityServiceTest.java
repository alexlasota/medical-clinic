package com.alexlasota.medicalclinic.service;

import com.alexlasota.medicalclinic.exceptions.MedicalClinicException;
import com.alexlasota.medicalclinic.mapper.FacilityMapper;
import com.alexlasota.medicalclinic.model.Doctor;
import com.alexlasota.medicalclinic.model.Facility;
import com.alexlasota.medicalclinic.model.FacilityDto;
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

public class FacilityServiceTest {

    DoctorRepository doctorRepository;
    FacilityRepository facilityRepository;
    FacilityMapper facilityMapper;
    FacilityService facilityService;

    @BeforeEach
    void setup() {
        this.doctorRepository = Mockito.mock(DoctorRepository.class);
        this.facilityRepository = Mockito.mock(FacilityRepository.class);
        this.facilityMapper = Mockito.mock(FacilityMapper.class);
        this.facilityService = new FacilityService(doctorRepository, facilityRepository, facilityMapper);
    }

    @Test
    void addFacility_FacilityWithIdDoesntExist_FacilitySaved() {
        //given
        Facility facility = new Facility();
        facility.setId(1L);
        when(facilityRepository.existsById(facility.getId())).thenReturn(false);
        when(facilityRepository.save(facility)).thenReturn(facility);
        //when
        Facility facility1 = facilityService.addFacility(facility);
        //then
        Assertions.assertNotNull(facility1);
        Assertions.assertEquals(1L, facility1.getId());
    }

    @Test
    void addFacility_FacilityExists_ExceptionThrown() {
        //given
        Facility facility = new Facility();
        facility.setId(1L);
        when(facilityRepository.existsById(facility.getId())).thenReturn(true);
        //when
        MedicalClinicException medicalClinicException = Assertions.assertThrows(MedicalClinicException.class,
                () -> facilityService.addFacility(facility));
        //then
        Assertions.assertEquals("Facility with this ID already exists", medicalClinicException.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, medicalClinicException.getHttpStatus());
    }

    @Test
    void getFacilities_FacilitiesExist_FacilitiesReturned() {
        //given
        List<Facility> facilities = new ArrayList<>();
        facilities.add(new Facility());
        Page<Facility> facilityPage = new PageImpl<>(facilities);
        Pageable pageable = PageRequest.of(0, 10);
        when(facilityRepository.findAll(pageable)).thenReturn(facilityPage);
        //when
        List<Facility> facilityList = facilityService.getFacilities(pageable);
        //then
        Assertions.assertEquals(1, facilityList.size());
    }

    @Test
    void getFacilityById_FacilityWithIdExists_FacilityReturned() {
        //given
        Long id = 4L;
        Facility facility = new Facility();
        facility.setId(id);
        when(facilityRepository.findById(id)).thenReturn(Optional.of(facility));
        //when
        Facility facilityById = facilityService.getFacilityById(id);
        //then
        Assertions.assertEquals(4L, facilityById.getId());
    }

    @Test
    void getFacilityById_FacilityDoesntExist_ExceptionThrown() {
        //given
        Long id = 4L;
        Facility facility = new Facility();
        facility.setId(id);
        when(facilityRepository.findById(id)).thenReturn(Optional.empty());
        //when
        MedicalClinicException medicalClinicException = Assertions.assertThrows(MedicalClinicException.class,
                () -> facilityService.getFacilityById(id));
        //then
        Assertions.assertEquals("Facility with this ID doesnt exist", medicalClinicException.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, medicalClinicException.getHttpStatus());
    }

    @Test
    void assignFacilityToDoctor_DoctorAndFacilityExist_FacilitySaved() {
        //given
        Long facilityId = 1L;
        Long doctorId = 1L;
        Doctor doctor = new Doctor();
        doctor.setId(doctorId);
        Facility facility = new Facility();
        facility.setId(facilityId);
        facility.setDoctors(new ArrayList<>());
        List<Long> doctorIds = new ArrayList<>();
        FacilityDto facilityDto = new FacilityDto(facility.getId(), "Matka polka", "Lodz", "1234", "Zgierska", "12", doctorIds);
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));
        when(facilityRepository.findById(facilityId)).thenReturn(Optional.of(facility));
        when(facilityRepository.save(facility)).thenReturn(facility);
        when(facilityMapper.facilityToFacilityDto(facility)).thenReturn(facilityDto);
        //when
        FacilityDto dto = facilityService.assignFacilityToDoctor(facilityId, doctorId);
        //then
        Assertions.assertTrue(facility.getDoctors().contains(doctor));
    }

    @Test
    void assignFacilityToDoctor_DoctorDoesntExist_ExceptionThrown() {
        Long facilityId = 1L;
        Long doctorId = 1L;
        Doctor doctor = new Doctor();
        doctor.setId(doctorId);
        Facility facility = new Facility();
        facility.setId(facilityId);
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.empty());
        //when
        MedicalClinicException medicalClinicException = Assertions.assertThrows(MedicalClinicException.class,
                () -> facilityService.assignFacilityToDoctor(facilityId, doctorId));
        //then
        Assertions.assertEquals("Doctor with this ID doesnt exist", medicalClinicException.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, medicalClinicException.getHttpStatus());
    }

    @Test
    void assignFacilityToDoctor_DoctorExists_FacilityDoesntExist_ExceptionThrown() {
        Long facilityId = 1L;
        Long doctorId = 1L;
        Doctor doctor = new Doctor();
        doctor.setId(doctorId);
        Facility facility = new Facility();
        facility.setId(facilityId);
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));
        when(facilityRepository.findById(facilityId)).thenReturn(Optional.empty());
        //when
        MedicalClinicException medicalClinicException = Assertions.assertThrows(MedicalClinicException.class,
                () -> facilityService.assignFacilityToDoctor(facilityId, doctorId));
        //then
        Assertions.assertEquals("Facility with this ID doesnt exist", medicalClinicException.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, medicalClinicException.getHttpStatus());
    }
}