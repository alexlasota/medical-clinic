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

import java.util.List;

@Service
@RequiredArgsConstructor
public class FacilityService {

    private final DoctorRepository doctorRepository;
    private final FacilityRepository facilityRepository;
    private final FacilityMapper facilityMapper;

    public Facility addFacility(Facility facility) {
        if (facilityRepository.existsById(facility.getId())) {
            throw new MedicalClinicException(HttpStatus.BAD_REQUEST, "Facility with this ID already exists");
        }
        return facilityRepository.save(facility);
    }

    public List<Facility> getFacilities(Pageable pageable) {
        return facilityRepository.findAll(pageable).getContent();
    }

    public Facility getFacilityById(Long id) {
        return facilityRepository.findById(id)
                .orElseThrow(() -> new MedicalClinicException(HttpStatus.BAD_REQUEST, "Facility with this ID doesnt exist"));
    }

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