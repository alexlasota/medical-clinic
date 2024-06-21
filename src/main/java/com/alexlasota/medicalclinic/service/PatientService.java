package com.alexlasota.medicalclinic.service;

import com.alexlasota.medicalclinic.exceptions.MedicalClinicException;
import com.alexlasota.medicalclinic.model.Patient;
import com.alexlasota.medicalclinic.repository.PatientRepository;
import com.alexlasota.medicalclinic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    //TC1: W przypadku gdy zostanie wywolana metoda findAll z patientRepo to zostanie zwrocona lista pacjentow
    public List<Patient> getPatients(Pageable pageable) {
        return patientRepository.findAll(pageable).getContent();
    }

    // TC1: W przypadku gdy nie istnieje pacjent o danym emailu oraz metoda checkIfDataIsNotNull zwraca prawidłowe dane
    // wykona sie metoda save z patientRepo i pacjent zostanie zapisany
    // TC2: W przypadku gdy istnieje pacjent o danym emailu to poleci wyjątek
    // TC3: W przypadku gdy nie istniej pacjent o danym mailu ale metoda checkIfDataIsNotNull
    // otrzyma niekompletne informacje poleci wyjatek
    @Transactional
    public Patient addPatient(Patient patient) {
        if (userRepository.findByEmail(patient.getMedicalUser().getEmail()).isPresent()) {
            throw new MedicalClinicException(HttpStatus.BAD_REQUEST, "User with this email already exists");
        }
        checkIfDataIsNotNull(patient);
        patientRepository.save(patient);
        return patient;
    }

    //TC1: W przypadku gdy wykona sie metoda deleteByMail z userRepo zostanie usuniety user o danym mailu
    public void removeUserByEmail(String email) {
        userRepository.deleteByEmail(email);
    }

    // TC1: W przypadku gdy istnieje pacjent o danym mailu, metoda checkIfDataIsnotNull/checkIsEmailAvailable zwraca prawidlowe dane
    // pacjent nie zmieni getIdCard zostanie zaktualizaowane info pacjenta oraz zostanie wywolana metoda save z patientRepo
    // TC 2: W przypadku gdy nie istnieje pacjent o danym mailu powinien poleciec wyjatek
    // TC 3: W przypadku gdy istnieje pacjent o danym mailu ale metoda checkIfDataIsNotNull zwraca nieprawidlowe dane poleci wyjatek
    // TC 4: W przypadku gdy istnieje pacjent o danym mailu ale metoda checkIsEmailAvaikabke zwraca nieprawidlowe dane poleci wyjatek
    // TC 5: W przypadku gdy istnieje pacjent o danym mailu, metoda checkIfDataIsnotNull/checkIsEmailAvailable zwraca prawidlowe dane
    // ale pacjent chce zmienic IdCardNo poleci wyjatek
    @Transactional
    public Patient editPatient(String email, Patient newPatientData) {
        Patient toEditPatient = patientRepository.findByMedicalUser_email(email)
                .orElseThrow(() -> new MedicalClinicException(HttpStatus.NOT_FOUND, "Patient with given email doesnt exist"));

        checkIfDataIsNotNull(newPatientData);
        checkIsEmailAvailable(toEditPatient, newPatientData.getMedicalUser().getEmail());

        if (!toEditPatient.getIdCardNo().equals(newPatientData.getIdCardNo())) {
            throw new MedicalClinicException(HttpStatus.BAD_REQUEST, "Changing idNumber isnt allowed byczku");
        }
        updatePatientData(toEditPatient, newPatientData);
        return patientRepository.save(toEditPatient);
    }


    public void checkIfDataIsNotNull(Patient patient) {
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

    private void checkIsEmailAvailable(Patient currentPatient, String email) {
        if (currentPatient != null && currentPatient.getMedicalUser() != null
                && email.equals(currentPatient.getMedicalUser().getEmail())) {
            return;
        }
        if (patientRepository.findByMedicalUser_email(email).isPresent()) {
            throw new MedicalClinicException(HttpStatus.BAD_REQUEST, "Patient with given email already exists");
        }
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