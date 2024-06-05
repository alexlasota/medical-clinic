package com.alexlasota.medicalclinic.service;

import com.alexlasota.medicalclinic.exceptions.MedicalClinicException;
import com.alexlasota.medicalclinic.model.MedicalUser;
import com.alexlasota.medicalclinic.model.Patient;
import com.alexlasota.medicalclinic.repository.PatientRepository;
import com.alexlasota.medicalclinic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    public List<Patient> getPatients(Pageable pageable) {
        return patientRepository.findAll(pageable).getContent();
    }

    public MedicalUser getPatientByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new MedicalClinicException(HttpStatus.NOT_FOUND, "Patient with given email doesnt exist"));
    }

    public void addPatient(Patient patient) {
        if (userRepository.findByEmail(patient.getMedicalUser().getEmail()).isPresent()) {
            throw new MedicalClinicException(HttpStatus.BAD_REQUEST, "User with this email already exists");
        }
        checkIfDataIsNotNull(patient);
        patientRepository.save(patient);
    }

    public void removeUserByEmail(String email) {
        userRepository.deleteByEmail(email);
    }

    public Patient editPatient(String email, Patient newPatientData) {
        Patient toEditPatient = patientRepository.findByMedicalUser_email(email)
                .orElseThrow(() -> new MedicalClinicException(HttpStatus.NOT_FOUND, "Patient with given email doesnt exist"));

        checkIfDataIsNotNull(newPatientData);
        checkIsEmailAvailable(newPatientData, newPatientData.getMedicalUser().getEmail());

        if (!toEditPatient.getIdCardNo().equals(newPatientData.getIdCardNo())) {
            throw new MedicalClinicException(HttpStatus.BAD_REQUEST, "Changing idNumber isnt allowed byczku");
        }
        updatePatientData(toEditPatient, newPatientData);
        return patientRepository.save(toEditPatient);
    }

    private void checkIfDataIsNotNull(Patient patient) {
        if (patient.getMedicalUser().getEmail() == null) {
            throw new MedicalClinicException(HttpStatus.BAD_REQUEST, "Email is required");
        }
        if (patient.getMedicalUser().getPassword() == null) {
            throw new MedicalClinicException(HttpStatus.BAD_REQUEST, "Password is required");
        }
        if (patient.getIdCardNo() == null) {
            throw new MedicalClinicException(HttpStatus.BAD_REQUEST, "ID card number is required");
        }
        if (patient.getMedicalUser().getFirstName() == null) {
            throw new MedicalClinicException(HttpStatus.BAD_REQUEST, "First name is required");
        }
        if (patient.getMedicalUser().getLastName() == null) {
            throw new MedicalClinicException(HttpStatus.BAD_REQUEST, "Last name is required");
        }
        if (patient.getPhoneNumber() == null) {
            throw new MedicalClinicException(HttpStatus.BAD_REQUEST, "Phone number is required");
        }
        if (patient.getBirthday() == null) {
            throw new MedicalClinicException(HttpStatus.BAD_REQUEST, "Birthday is required");
        }
    }

    private boolean checkIsEmailAvailable(Patient patient, String email) {
        if (patientRepository.findByMedicalUser_email(email).isPresent()) {
            throw new MedicalClinicException(HttpStatus.BAD_REQUEST, "Patient with given email already exists");
        }
        return false;
    }

    public void updatePatientData(Patient toEditPatient, Patient newPatientData) {
        toEditPatient.getMedicalUser().setPassword(newPatientData.getMedicalUser().getPassword());
        toEditPatient.setBirthday(newPatientData.getBirthday());
        toEditPatient.getMedicalUser().setEmail(newPatientData.getMedicalUser().getEmail());
        toEditPatient.getMedicalUser().setFirstName(newPatientData.getMedicalUser().getFirstName());
        toEditPatient.getMedicalUser().setLastName(newPatientData.getMedicalUser().getLastName());
        toEditPatient.setIdCardNo(newPatientData.getIdCardNo());
        toEditPatient.setPhoneNumber(newPatientData.getPhoneNumber());
    }
}