package com.example.PublicKeyInfrastructure.service;

import com.example.PublicKeyInfrastructure.authentication.JwtTokenProvider;
import com.example.PublicKeyInfrastructure.authentication.TokenResponse;
import com.example.PublicKeyInfrastructure.dto.login.LoginDTO;
import com.example.PublicKeyInfrastructure.dto.login.LogoutDTO;
import com.example.PublicKeyInfrastructure.model.RefreshToken;
import com.example.PublicKeyInfrastructure.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private RefreshTokenService refreshTokenService;

    public TokenResponse login(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getEmail(),
                        loginDTO.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(loginDTO.getEmail());
        String accessToken = jwtTokenProvider.generateToken(authentication);

        return new TokenResponse(accessToken, refreshToken.getToken());
    }

    public void logout(LogoutDTO logoutDTO) {
        refreshTokenService.revokeRefreshToken(logoutDTO.getEmail());
    }
}