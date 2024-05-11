package com.alexlasota.medicalclinic.service;

import com.alexlasota.medicalclinic.exceptions.MedicalClinicException;
import com.alexlasota.medicalclinic.model.Password;
import com.alexlasota.medicalclinic.model.Patient;
import com.alexlasota.medicalclinic.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    public List<Patient> getPatients() {
        return patientRepository.getPatients();
    }

    public Patient getPatientByEmail(String email) {
        return patientRepository.getPatientByEmail(email)
                .orElseThrow(() -> new MedicalClinicException(HttpStatus.NOT_FOUND, "Patient with given email doesnt exist"));
    }

    public void addPatient(Patient patient) {
        if (patientRepository.getPatientByEmail(patient.getEmail()).isPresent()) {
            throw new MedicalClinicException(HttpStatus.BAD_REQUEST, "Patient with email already exists");
        }
        checkIfDataIsNotNull(patient);
        patientRepository.addPatient(patient);
    }

    public void removePatientByEmail(String email) {
        patientRepository.removePatientByEmail(email);
    }

    public Patient editPatient(String email, Patient newPatientData) {
        Patient toEditPatient = patientRepository.getPatientByEmail(email)
                .orElseThrow(() -> new MedicalClinicException(HttpStatus.NOT_FOUND, "Patient with given email doesnt exist"));

        checkIfDataIsNotNull(newPatientData);

        if (!toEditPatient.getIdCardNo().equals(newPatientData.getIdCardNo())) {
            throw new MedicalClinicException(HttpStatus.BAD_REQUEST, "Changing idNumber isnt allowed byczku");
        }
        patientRepository.editPatient(toEditPatient, newPatientData);
        return toEditPatient;
    }

    public Patient updatePassword(String email, Patient newPassword) {
        Patient patient = patientRepository.getPatientByEmail(email)
                .orElseThrow(() -> new MedicalClinicException(HttpStatus.NOT_FOUND, "Patient with given email doesn't exist"));

        if (isValidPassword(newPassword.getPassword())) {
            patientRepository.updatePatientPassword(patient, newPassword.getPassword());
        } else {
            throw new MedicalClinicException(HttpStatus.BAD_REQUEST, "Invalid password format");
        }
        return patient;
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 5 && password.matches(".*\\d.*");
    }
    // \\d - dowolna cyfra 0-9. * ciąg znaków

    private void checkIfDataIsNotNull(Patient patient) {
        if (patient.getEmail() == null) {
            throw new MedicalClinicException(HttpStatus.BAD_REQUEST, "Email is required");
        }
        if (patient.getPassword() == null) {
            throw new MedicalClinicException(HttpStatus.BAD_REQUEST, "Password is required");
        }
        if (patient.getIdCardNo() == null) {
            throw new MedicalClinicException(HttpStatus.BAD_REQUEST, "ID card number is required");
        }
        if (patient.getFirstName() == null) {
            throw new MedicalClinicException(HttpStatus.BAD_REQUEST, "First name is required");
        }
        if (patient.getLastName() == null) {
            throw new MedicalClinicException(HttpStatus.BAD_REQUEST, "Last name is required");
        }
        if (patient.getPhoneNumber() == null) {
            throw new MedicalClinicException(HttpStatus.BAD_REQUEST, "Phone number is required");
        }
        if (patient.getBirthday() == null) {
            throw new MedicalClinicException(HttpStatus.BAD_REQUEST, "Birthday is required");
        }
        if (patientRepository.getPatientByEmail(patient.getEmail()).isPresent()) {
            throw new MedicalClinicException(HttpStatus.BAD_REQUEST, "Patient with given email already exists");
        }
    }
}



