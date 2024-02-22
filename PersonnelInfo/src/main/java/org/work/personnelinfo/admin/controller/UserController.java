package org.work.personnelinfo.admin.controller;


import jakarta.persistence.PrePersist;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.work.personnelinfo.admin.Service.AuthenticationService;
import org.work.personnelinfo.admin.dto.UserDTO;
import org.work.personnelinfo.personel.dto.PersonelDTO;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO entity){
        String token = authenticationService.login(entity);
        if (token != null) {
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
}
