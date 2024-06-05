package com.alexlasota.medicalclinic.service;

import com.alexlasota.medicalclinic.exceptions.MedicalClinicException;
import com.alexlasota.medicalclinic.model.MedicalUser;
import com.alexlasota.medicalclinic.model.Password;
import com.alexlasota.medicalclinic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public MedicalUser updatePassword(String email, Password newPassword) {
        MedicalUser medicalUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new MedicalClinicException(HttpStatus.NOT_FOUND, "User with given email doesn't exist"));

        if (!isValidPassword(newPassword.getPassword())) {
            throw new MedicalClinicException(HttpStatus.BAD_REQUEST, "Invalid password format");
        }
        medicalUser.setPassword(newPassword.getPassword());
        return userRepository.save(medicalUser);
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 5 && password.matches(".*\\d.*");
    }
    // \\d - dowolna cyfra 0-9. * ciąg znaków
}