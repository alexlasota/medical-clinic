package com.alexlasota.medicalclinic.service;

import com.alexlasota.medicalclinic.exceptions.MedicalClinicException;
import com.alexlasota.medicalclinic.model.MedicalUser;
import com.alexlasota.medicalclinic.model.Patient;
import com.alexlasota.medicalclinic.repository.PatientRepository;
import com.alexlasota.medicalclinic.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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
    void addPatients_PatientWithEmailExists_ExceptionThrown() {
        Patient patient = createPatient("al@gmail.com", 1L);
        when(userRepository.findByEmail(patient.getMedicalUser().getEmail())).thenReturn(Optional.of(patient.getMedicalUser()));

        MedicalClinicException exception = Assertions.assertThrows(MedicalClinicException.class,
                () -> patientService.addPatient(patient));

        Assertions.assertEquals("User with this email already exists", exception.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    void addPatients_PatientDoesntExist_DataIncorrect_ExceptionThrown() {
        Patient patient = createPatient(null, 1L);
        when(userRepository.findByEmail(patient.getMedicalUser().getEmail())).thenReturn(Optional.empty());

        MedicalClinicException exception = Assertions.assertThrows(MedicalClinicException.class,
                () -> patientService.addPatient(patient));

        Assertions.assertEquals("Email is required", exception.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    void removeUserByEmail_PatientWithEmailExists_PatientRemoved() {
        //given
        String email = "alex";
        when(patientRepository.deleteByMedicalUser_email(email)).thenReturn(Optional.of(new Patient()));
        //when
        patientService.removeUserByEmail(email);
        //then
        verify(patientRepository).deleteByMedicalUser_email(email);
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

    @Test
    void editPatient_PatientDoesntExist_ExceptionThrown() {
        String email = "alex";
        Patient newPatient = createPatient("alex", 1L);
        when(patientRepository.findByMedicalUser_email(email)).thenReturn(Optional.empty());

        MedicalClinicException exception = Assertions.assertThrows(MedicalClinicException.class,
                () -> patientService.editPatient(email, newPatient));

        Assertions.assertEquals("Patient with given email doesnt exist", exception.getMessage());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void editPatient_PatientExists_CheckIfDataIsNotNullIncorrect_ExceptionThrown() {
        String email = "alex";
        Patient newPatient = createPatient(null, 1L);
        when(patientRepository.findByMedicalUser_email(email)).thenReturn(Optional.of(newPatient));

        MedicalClinicException exception = Assertions.assertThrows(MedicalClinicException.class,
                () -> patientService.editPatient(email, newPatient));

        Assertions.assertEquals("Email is required", exception.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    void editPatient_PatientExists_CheckIsEmailAvailableIncorrect_ExceptionThrown() {
        String email = "alex";
        Patient existingPatient = createPatient(email, 1L);
        Patient newPatientData = createPatient("newmail.com", 1L);

        when(patientRepository.findByMedicalUser_email(email)).thenReturn(Optional.of(existingPatient));
        when(patientRepository.findByMedicalUser_email(newPatientData.getMedicalUser().getEmail())).thenReturn(Optional.of(newPatientData));

        // When & Then
        MedicalClinicException exception = Assertions.assertThrows(MedicalClinicException.class,
                () -> patientService.editPatient(email, newPatientData));

        // Weryfikacja
        Assertions.assertEquals("Patient with given email already exists", exception.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }


    @Test
    void editPatient_ChangingIdCardNoNotAllowed_ExceptionThrown() {
        String email = "alex@example.com";
        Patient newPatient = createPatient(email, 1L);
        newPatient.setIdCardNo("ABC123");
        Patient newPatientData = createPatient(email, 1L);
        newPatientData.setIdCardNo("NEW456");

        when(patientRepository.findByMedicalUser_email(email)).thenReturn(Optional.of(newPatient));

        MedicalClinicException exception = Assertions.assertThrows(MedicalClinicException.class,
                () -> patientService.editPatient(email, newPatientData));

        Assertions.assertEquals("Changing idNumber isnt allowed byczku", exception.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @ParameterizedTest
    @MethodSource("provideUsersToCheckData")
    void checkIfDataIsNotNull(Patient patient, String message) {
        MedicalClinicException medicalClinicException = Assertions
                .assertThrows(MedicalClinicException.class, () -> patientService.checkIfDataIsNotNull(patient));
        Assertions.assertEquals(message, medicalClinicException.getMessage());
    }

    private static Stream<Arguments> provideUsersToCheckData() {
        return Stream.of(
                Arguments.of(new Patient(null, null, null, null, new MedicalUser(null, null, null, null, null), null), "Email is required"),
                Arguments.of(new Patient(null, null, null, null, new MedicalUser(null, "al@gmail.com", null, null, null), null), "Password is required"),
                Arguments.of(new Patient(null, null, null, null, new MedicalUser(null, "al@gmail.com", "haslo123", null, null), null), "ID card number is required"),
                Arguments.of(new Patient(null, "132411", null, null, new MedicalUser(null, "al@gmail.com", "haslo123", null, null), null), "First name is required"),
                Arguments.of(new Patient(null, "132411", null, null, new MedicalUser(null, "al@gmail.com", "haslo123", "Alex", null), null), "Last name is required"),
                Arguments.of(new Patient(null, "132411", null, null, new MedicalUser(null, "al@gmail.com", "haslo123", "Alex", "Lasota"), null), "Phone number is required"),
                Arguments.of(new Patient(null, "132411", "12142515", null, new MedicalUser(null, "al@gmail.com", "haslo123", "Alex", "Lasota"), null), "Birthday is required")
        );
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