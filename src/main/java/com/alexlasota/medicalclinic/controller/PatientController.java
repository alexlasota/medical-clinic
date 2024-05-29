package com.alexlasota.medicalclinic.controller;

import com.alexlasota.medicalclinic.mapper.PatientMapper;
import com.alexlasota.medicalclinic.model.Password;
import com.alexlasota.medicalclinic.model.PatientDto;
import com.alexlasota.medicalclinic.service.PatientService;
import com.alexlasota.medicalclinic.model.Patient;
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

    //w sytuacji kiedy w sciezce nasz parametr nazywa sie tak samo jak zmienna
    // to wtedy nie musimy explicit podawac jej nazwy @PathVariable("email")
    @GetMapping("/{email}")
    public PatientDto getPatientByEmail(@PathVariable String email) {
        return patientMapper.patientToPatientDto(patientService.getPatientByEmail(email));
    }
    //W rezultacie, gdy żądanie GET zostanie wysłane do endpointu /
    // z adresem e-mail jako częścią ścieżki (np. /patients/john@example.com),
    // metoda getPatientByEmail zostanie wywołana,
    // a pacjent o podanym adresie e-mail zostanie zwrócony jako odpowiedź na żądanie.

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Patient addPatient(@RequestBody Patient patient) {
        patientService.addPatient(patient);
        return patient;
    }

    @DeleteMapping("/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removePatientByEmail(@PathVariable String email) {
        patientService.removePatientByEmail(email);
    }

    @PutMapping("/{email}")
    public PatientDto editPatient(@PathVariable String email, @RequestBody Patient newPatientData) {
        Patient updatedPatient = patientService.editPatient(email, newPatientData);
        return patientMapper.patientToPatientDto(patientService.editPatient(email, newPatientData));
    }

    @PatchMapping("/{email}")
    public Patient updatePassword(@PathVariable String email, @RequestBody Password newPassword) {
        return patientService.updatePassword(email, newPassword);
    }
}
