package com.alexlasota.medicalclinic.controller;

import com.alexlasota.medicalclinic.service.PatientService;
import com.alexlasota.medicalclinic.model.Patient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @GetMapping //@GetMapping obsługuje żądania typu GET,
    public List<Patient> getPatients() {
        return patientService.getPatients();
    }

    //w sytuacji kiedy w sciezce nasz parametr nazywa sie tak samo jak zmienna
    // to wtedy nie musimy explicit podawac jej nazwy @PathVariable("email")
    @GetMapping("/{email}")
    public Patient getPatientByEmail(@PathVariable String email) {
        return patientService.getPatientByEmail(email);
        //W rezultacie, gdy żądanie GET zostanie wysłane do endpointu /
        // z adresem e-mail jako częścią ścieżki (np. /patients/john@example.com),
        // metoda getPatientByEmail zostanie wywołana,
        // a pacjent o podanym adresie e-mail zostanie zwrócony jako odpowiedź na żądanie.
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addPatient(@RequestBody Patient patient) { //// pacjent do dodania ma zostac wyslany w body
        patientService.addPatient(patient);
    }

    @DeleteMapping("/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT) //NO_CONTENT zeby uzytkownik nie byl zdezorientowany czemu nie ma zadnej wiadomosci w body
    public void removePatientByEmail(@PathVariable String email) {
        patientService.removePatientByEmail(email);
    }

    @PutMapping("/{email}")
    public Patient editPatient(@PathVariable String email, @RequestBody Patient newPatientData) {
        return patientService.editPatient(email, newPatientData);
    }
    @PatchMapping("/{email}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Optional<Patient> updatePassword(@PathVariable String email, @RequestBody Patient newPassword){
        return patientService.updatePassword(email,newPassword);
    }

}
