package com.example.PublicKeyInfrastructure.controller;

import com.example.PublicKeyInfrastructure.authentication.TokenResponse;
import com.example.PublicKeyInfrastructure.dto.login.LoginDTO;
import com.example.PublicKeyInfrastructure.dto.login.LogoutDTO;
import com.example.PublicKeyInfrastructure.service.AuthenticationService;
import com.example.PublicKeyInfrastructure.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping(value="/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        TokenResponse tokenResponse = authenticationService.login(loginDTO);
        return new ResponseEntity<>(tokenResponse, HttpStatus.OK);
    }

    @PostMapping(value = "/logout", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> logout(@RequestBody LogoutDTO logoutDTO) {
        authenticationService.logout(logoutDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
