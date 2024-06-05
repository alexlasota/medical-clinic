package com.alexlasota.medicalclinic.controller;

import com.alexlasota.medicalclinic.mapper.PatientMapper;
import com.alexlasota.medicalclinic.model.PatientDto;
import com.alexlasota.medicalclinic.service.PatientService;
import com.alexlasota.medicalclinic.model.Patient;
import com.alexlasota.medicalclinic.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;
    private final PatientMapper patientMapper;

    @GetMapping
    public List<PatientDto> getPatients(Pageable pageable) {
        return patientMapper.mapListToDto(patientService.getPatients(pageable));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Patient addPatient(@RequestBody Patient patient) {
        patientService.addPatient(patient);
        return patient;
    }

    @DeleteMapping("/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeUserByEmail(@PathVariable String email) {
        patientService.removeUserByEmail(email);
    }

    @PutMapping("/{email}")
    public PatientDto editPatient(@PathVariable String email, @RequestBody Patient newPatientData) {
        return patientMapper.patientToPatientDto(patientService.editPatient(email, newPatientData));
    }
}