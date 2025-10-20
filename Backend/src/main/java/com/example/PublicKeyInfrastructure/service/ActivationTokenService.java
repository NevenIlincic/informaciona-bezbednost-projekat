package com.example.PublicKeyInfrastructure.service;

import com.example.PublicKeyInfrastructure.model.ActivationToken;
import com.example.PublicKeyInfrastructure.repository.ActivationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivationTokenService {

    @Autowired
    private ActivationTokenRepository activationTokenRepository;

    public ActivationToken getByActivationToken(String token){
        return this.activationTokenRepository.findByToken(token).orElse(null);
    }

    public ActivationToken saveToken(String token){
        ActivationToken activationToken = new ActivationToken();
        activationToken.setToken(token);
        return this.activationTokenRepository.save(activationToken);
    }
}
