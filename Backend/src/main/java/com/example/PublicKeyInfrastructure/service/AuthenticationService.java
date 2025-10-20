package com.example.PublicKeyInfrastructure.service;

import com.example.PublicKeyInfrastructure.authentication.JwtTokenProvider;
import com.example.PublicKeyInfrastructure.authentication.TokenResponse;
import com.example.PublicKeyInfrastructure.dto.login.LoginDTO;
import com.example.PublicKeyInfrastructure.dto.login.LogoutDTO;
import com.example.PublicKeyInfrastructure.dto.token.RefreshTokenDTO;
import com.example.PublicKeyInfrastructure.model.RefreshToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private RefreshTokenService refreshTokenService;

    private Authentication returnAuthentication(String email, String password){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        password
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    public TokenResponse login(LoginDTO loginDTO) {
        Authentication authentication = returnAuthentication(loginDTO.getEmail(), loginDTO.getPassword());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(loginDTO.getEmail());
        String accessToken = jwtTokenProvider.generateToken(authentication);

        return new TokenResponse(accessToken, refreshToken.getToken());
    }

    public void logout(LogoutDTO logoutDTO) {
        refreshTokenService.revokeRefreshToken(logoutDTO.getEmail());
    }

    public TokenResponse createNewAccessToken(RefreshTokenDTO refreshTokenDTO) throws IllegalArgumentException {
        System.out.println("TOKEN: " + refreshTokenDTO.getRefreshToken());
        RefreshToken foundToken = refreshTokenService.findRefreshTokenByToken(refreshTokenDTO.getRefreshToken());
        if (foundToken.getExpiryDate().isBefore(LocalDateTime.now()) || !foundToken.isValid()){
            throw new IllegalArgumentException("Refresh token expired or is invalid!");
        }
        String accessToken = jwtTokenProvider.generateTokenFromUser(foundToken.getUser());
        return new TokenResponse(accessToken, foundToken.getToken());
    }
}