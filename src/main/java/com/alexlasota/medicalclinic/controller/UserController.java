package com.alexlasota.medicalclinic.controller;

import com.alexlasota.medicalclinic.model.Password;
import com.alexlasota.medicalclinic.model.MedicalUser;
import com.alexlasota.medicalclinic.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Update user password", description = "Updates the password for a user identified by their email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MedicalUser.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content)
    })
    @PatchMapping("/{email}/password")
    public MedicalUser updatePassword(@PathVariable String email, @RequestBody Password newPassword) {
        return userService.updatePassword(email, newPassword);
    }
}