package com.alexlasota.medicalclinic.controller;

import com.alexlasota.medicalclinic.model.Password;
import com.alexlasota.medicalclinic.model.MedicalUser;
import com.alexlasota.medicalclinic.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PatchMapping("/{email}/password")
    public MedicalUser updatePassword(@PathVariable String email, @RequestBody Password newPassword) {
        return userService.updatePassword(email, newPassword);
    }
}