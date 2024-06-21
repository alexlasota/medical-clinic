package com.alexlasota.medicalclinic.service;

import com.alexlasota.medicalclinic.exceptions.MedicalClinicException;
import com.alexlasota.medicalclinic.mapper.FacilityMapper;
import com.alexlasota.medicalclinic.model.Doctor;
import com.alexlasota.medicalclinic.model.Facility;
import com.alexlasota.medicalclinic.model.FacilityDto;
import com.alexlasota.medicalclinic.repository.DoctorRepository;
import com.alexlasota.medicalclinic.repository.FacilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FacilityService {

    private final DoctorRepository doctorRepository;
    private final FacilityRepository facilityRepository;
    private final FacilityMapper facilityMapper;

    //TC1: W przypadku gdy nie istnieje facility o danym id wykona sie metoda save z facilityRepo i zostanie dodane facility
    //TC2: W przypadku gdy istnieje juz facility o danym id poleci wyjatek
    public Facility addFacility(Facility facility) {
        if (facilityRepository.existsById(facility.getId())) {
            throw new MedicalClinicException(HttpStatus.BAD_REQUEST, "Facility with this ID already exists");
        }
        return facilityRepository.save(facility);
    }

    //TC1: W przypadku gdy wykona sie metoda findAll z facilityRepo to zostanie zwrocona lista facilities
    public List<Facility> getFacilities(Pageable pageable) {
        return facilityRepository.findAll(pageable).getContent();
    }

    //TC1: W przypadku gdy istnieje facility o danym id to zostanie zwrocony facility
    //TC2: W przypadky gdy nie istnieje facility o danym id poleci wyjatek
    public Facility getFacilityById(Long id) {
        return facilityRepository.findById(id)
                .orElseThrow(() -> new MedicalClinicException(HttpStatus.BAD_REQUEST, "Facility with this ID doesnt exist"));
    }

    //TC1: W przypadku gdy istnieje doktor o danym id, istnieje facility o danym id przypisujemy doktora do placowki oraz wywolujemy metode save z facility repo
    //TC2: W przypadku gdy nie istnieje doktor o danym id zostanie rzucony wyjatek
    //TC3: W przypadky gdy istnieje doktor o danym id ale nie istnieje facility o danym id zostanie rzucony wujatek
    @Transactional
    public FacilityDto assignFacilityToDoctor(Long facilityId, Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new MedicalClinicException(HttpStatus.BAD_REQUEST, "Doctor with this ID doesnt exist"));
        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new MedicalClinicException(HttpStatus.BAD_REQUEST, "Facility with this ID doesnt exist"));

        facility.getDoctors().add(doctor);
        facilityRepository.save(facility);
        return facilityMapper.facilityToFacilityDto(facility);
    }
}