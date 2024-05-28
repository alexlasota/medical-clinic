package com.alexlasota.medicalclinic.service;

import com.alexlasota.medicalclinic.exceptions.MedicalClinicException;
import com.alexlasota.medicalclinic.model.Password;
import com.alexlasota.medicalclinic.model.Patient;
import com.alexlasota.medicalclinic.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    public List<Patient> getPatients() {
        return patientRepository.findAll();
    }

    public Patient getPatientByEmail(String email) {
        return patientRepository.findById(email)
                .orElseThrow(() -> new MedicalClinicException(HttpStatus.NOT_FOUND, "Patient with given email doesnt exist"));
    }

    public void addPatient(Patient patient) {
        if (patientRepository.findById(patient.getEmail()).isPresent()) {
            throw new MedicalClinicException(HttpStatus.BAD_REQUEST, "Patient with email already exists");
        }
        checkIfDataIsNotNull(patient);
        patientRepository.save(patient);
    }

    public void removePatientByEmail(String email) {
        patientRepository.deleteById(email);
    }

    public Patient editPatient(String email, Patient newPatientData) {
        Patient toEditPatient = patientRepository.findById(email)
                .orElseThrow(() -> new MedicalClinicException(HttpStatus.NOT_FOUND, "Patient with given email doesnt exist"));

        checkIfDataIsNotNull(newPatientData);
        checkIsEmailAvailable(newPatientData, newPatientData.getEmail());

        if (!toEditPatient.getIdCardNo().equals(newPatientData.getIdCardNo())) {
            throw new MedicalClinicException(HttpStatus.BAD_REQUEST, "Changing idNumber isnt allowed byczku");
        }
        updatePatientData(toEditPatient, newPatientData);
        return patientRepository.save(toEditPatient);
    }

    public Patient updatePassword(String email, Password newPassword) {
        Patient patient = patientRepository.findById(email)
                .orElseThrow(() -> new MedicalClinicException(HttpStatus.NOT_FOUND, "Patient with given email doesn't exist"));

        if (!isValidPassword(newPassword.getPassword())) {
            throw new MedicalClinicException(HttpStatus.BAD_REQUEST, "Invalid password format");
        }
        patient.setPassword(newPassword.getPassword());
        return patientRepository.save(patient);
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
    }

    private void checkIsEmailAvailable(Patient patient, String email) {
        if (!patient.getEmail().equals(email) && patientRepository.findById(patient.getEmail()).isPresent()) {
            throw new MedicalClinicException(HttpStatus.BAD_REQUEST, "Patient with given email already exists");
        }
    }

    public void updatePatientData(Patient toEditPatient, Patient newPatientData) {
        toEditPatient.setPassword(newPatientData.getPassword());
        toEditPatient.setBirthday(newPatientData.getBirthday());
        toEditPatient.setEmail(newPatientData.getEmail());
        toEditPatient.setFirstName(newPatientData.getFirstName());
        toEditPatient.setLastName(newPatientData.getLastName());
        toEditPatient.setIdCardNo(newPatientData.getIdCardNo());
        toEditPatient.setPhoneNumber(newPatientData.getPhoneNumber());
    }
}