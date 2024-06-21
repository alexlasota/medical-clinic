package com.alexlasota.medicalclinic.controller;

import com.alexlasota.medicalclinic.mapper.DoctorMapper;
import com.alexlasota.medicalclinic.model.Doctor;
import com.alexlasota.medicalclinic.model.DoctorDto;
import com.alexlasota.medicalclinic.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;

    @GetMapping
    public List<DoctorDto> getDoctors(Pageable pageable) {
        return doctorMapper.mapListToDto(doctorService.getDoctors(pageable));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Doctor addDoctor(@RequestBody Doctor doctor) {
        return doctorService.addDoctor(doctor);
    }

    @GetMapping("/{id}")
    public DoctorDto getDoctorById(@PathVariable Long id) {
        return doctorMapper.doctorToDoctorDto(doctorService.getDoctorById(id));
    }

    @PatchMapping("/{doctorId}/facilities/{facilityId}")
    public DoctorDto assignDoctorToFacility(@PathVariable Long doctorId, @PathVariable Long facilityId) {
        Doctor doctor = doctorService.assignDoctorToFacility(doctorId, facilityId);
        return doctorMapper.doctorToDoctorDto(doctor);
    }
}
