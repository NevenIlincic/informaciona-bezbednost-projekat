package com.example.PublicKeyInfrastructure.controller;

import com.example.PublicKeyInfrastructure.authentication.TokenResponse;
import com.example.PublicKeyInfrastructure.dto.login.LoginDTO;
import com.example.PublicKeyInfrastructure.service.AuthenticationService;
import com.example.PublicKeyInfrastructure.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        TokenResponse tokenResponse = authenticationService.login(loginDTO);
        return new ResponseEntity<>(tokenResponse, HttpStatus.OK);
    }
}
