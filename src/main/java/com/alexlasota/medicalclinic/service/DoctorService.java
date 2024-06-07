package com.alexlasota.medicalclinic.service;

import com.alexlasota.medicalclinic.exceptions.MedicalClinicException;
import com.alexlasota.medicalclinic.model.Doctor;
import com.alexlasota.medicalclinic.model.Facility;
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
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final FacilityRepository facilityRepository;

    // TC1: W przypadku gdy zostanie wywolana metoda save z doctorRepo to zostanie zwrocony doktor
    @Transactional
    public Doctor addDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    // TC1: W przypadku gdy zostanie wywolana metoda findall z doctorRepo to zostanie zwrocona lista doktorow
    @Transactional
    public List<Doctor> getDoctors(Pageable pageable) {
        return doctorRepository.findAll(pageable).getContent();
    }

    // TC1: W przypadku gdy istnieje doktor o danym id zostanie zwrocony doktor
    // TC2: W przypadku gdy nie istnieje doktor o danym id powinien poleciec wyjatek
    @Transactional
    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new MedicalClinicException(HttpStatus.BAD_REQUEST, "Doctor with this ID doesnt exist"));
    }

    // TC1: W przypadku gdy istnieje doktor o danym id oraz istnieje facility o danym id
    //facility zostanie przypisane do doktora oraz wykona sie metoda save z doctorRepo dodajac doctora
    // TC2: W przypadku gdy nie istnieje doktor o danym id powinien poleciec wyjatek
    // TC3: W przypadku gdy istnieje doktor o danym id ale nie istnieje facility o danym id powinien poleciec wyjatek
    @Transactional
    public Doctor assignDoctorToFacility(Long doctorId, Long facilityId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new MedicalClinicException(HttpStatus.BAD_REQUEST, "Doctor with this ID doesnt exist"));
        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new MedicalClinicException(HttpStatus.BAD_REQUEST, "Facility with this ID doesnt exist"));

        doctor.getFacilities().add(facility);
        return doctorRepository.save(doctor);
    }
}