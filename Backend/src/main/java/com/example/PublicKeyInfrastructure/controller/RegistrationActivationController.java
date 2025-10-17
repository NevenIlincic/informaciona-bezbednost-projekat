package com.example.PublicKeyInfrastructure.controller;

import com.example.PublicKeyInfrastructure.model.AuthenticatedUser;
import com.example.PublicKeyInfrastructure.service.AuthenticatedUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/")
@CrossOrigin
public class RegistrationActivationController {

    @Autowired
    private AuthenticatedUserService authenticatedUserService;

    @GetMapping(value = "activate")
    public RedirectView registrationActivation(@RequestParam("token") String token) {
        AuthenticatedUser activatedUser = authenticatedUserService.activateAccount(token);
        if (activatedUser == null){
            return new RedirectView("https://localhost:4200/activation-failure"); // Stranica za isteknuti token!!
        }else{
            return new RedirectView("https://localhost:4200/activation-success"); // Stranica za uspesnu aktivaciju!
        }
    }
}
