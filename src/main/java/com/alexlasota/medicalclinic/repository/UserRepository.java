package com.alexlasota.medicalclinic.repository;

import com.alexlasota.medicalclinic.model.MedicalUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<MedicalUser, Long> {

    Optional<MedicalUser> findByEmail(String email);

    Optional<MedicalUser> deleteUserByEmail(String email);
}
