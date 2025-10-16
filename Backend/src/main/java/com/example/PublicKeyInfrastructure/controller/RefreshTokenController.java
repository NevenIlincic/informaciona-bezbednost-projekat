package com.example.PublicKeyInfrastructure.controller;

import com.example.PublicKeyInfrastructure.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/refreshToken")
@CrossOrigin
public class RefreshTokenController {

    @Autowired
    private RefreshTokenService refreshTokenService;

}
