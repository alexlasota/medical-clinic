package com.alexlasota.medicalclinic.service;

import com.alexlasota.medicalclinic.model.MedicalUser;
import com.alexlasota.medicalclinic.model.Patient;
import com.alexlasota.medicalclinic.repository.PatientRepository;
import com.alexlasota.medicalclinic.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PatientServiceTest {

    PatientService patientService;
    PatientRepository patientRepository;
    UserRepository userRepository;

    @BeforeEach
    void setup() {
        this.patientRepository = Mockito.mock(PatientRepository.class);
        this.userRepository = Mockito.mock(UserRepository.class);
        this.patientService = new PatientService(patientRepository, userRepository);
    }

    // konwencja nazewnicza: nazwaMetodyKtoraTestuje_PrzypadekKtoryTestuje_CoPowinnoSieZadziac
    @Test
    void getPatients_PatientsExist_PatientsReturned() {
        // given -> to sekcja sluzaca do przygotowania danych do przeprowadzenia testu
        // oraz zdefiniowania co maja dla tego Test Caseu zwracac moje mocki
        //given
        List<Patient> patients = new ArrayList<>();
        patients.add(createPatient("al@gmail.com", 1L));
        patients.add(createPatient("ba@gmail.com", 2L));
        Page<Patient> patientPage = new PageImpl<>(patients);
        Pageable pageable = PageRequest.of(0, 10);
        when(patientRepository.findAll(pageable)).thenReturn(patientPage);
        // when -> to sekcja sluzaca przeprowadzeniu testu -> w 99% przypadku bedzie tutaj po prostu
        // wywolanie metody ktora testuje oraz przypisanie jej rezultatu do zmiennej
        //when
        List<Patient> result = patientService.getPatients(pageable);
        // then -> to sekcja sluzaca do weryfikacji czy moj rezultat jest taki jaki sie spodziewalem
        // tutaj wykonujemy asercje
        //then
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(1, result.get(0).getId());
        Assertions.assertEquals("al@gmail.com", result.get(0).getMedicalUser().getEmail());
    }

    @Test
    void addPatients_PatientWithEmailDoesntExist_DataIsValid_PatientSaved() {
        //given
        Patient patient = createPatient("al@gmail.com", 1L);
        when(userRepository.findByEmail(patient.getMedicalUser().getEmail())).thenReturn(Optional.empty());
        //when
        patientService.addPatient(patient);
        //then
        verify(patientRepository).save(patient);
    }

    @Test
    void removeUserByEmail_PatientWithEmailExists_PatientRemoved() {
        //given
        String email = "alex";
        when(userRepository.deleteByEmail(email)).thenReturn(Optional.of(new MedicalUser()));
        //when
        patientService.removeUserByEmail(email);
        //then
        verify(userRepository).deleteByEmail(email);
    }

    @Test
    void editPatient_PatientWithEmailExists_PatientSaved() {
        //given
        String email = "alex";
        Patient newPatient = createPatient("alex", 1L);
        when(patientRepository.findByMedicalUser_email(email)).thenReturn(Optional.of(newPatient));
        //when
        patientService.editPatient(email, newPatient);
        //then
        Assertions.assertEquals(email, newPatient.getMedicalUser().getEmail());
    }


    Patient createPatient(String email, Long id) {
        MedicalUser user = new MedicalUser();
        user.setId(id);
        user.setEmail(email);
        user.setPassword("123");
        user.setFirstName("alex");
        user.setLastName("lasota");
        return new Patient(id, "12345", "213213", "1999-03-16", user, new ArrayList<>());
    }
}