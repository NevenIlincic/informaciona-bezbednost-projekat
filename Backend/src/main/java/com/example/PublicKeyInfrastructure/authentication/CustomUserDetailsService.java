package com.example.PublicKeyInfrastructure.authentication;

import com.example.PublicKeyInfrastructure.repository.AuthenticatedUserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final AuthenticatedUserRepository authenticatedUserRepository;

    public CustomUserDetailsService(AuthenticatedUserRepository authenticatedUserRepository) {
        this.authenticatedUserRepository = authenticatedUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDetails userDetails = authenticatedUserRepository.findByEmail(email)
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        user.getAuthorities().stream().map(role -> new SimpleGrantedAuthority(  role.toString())).collect(Collectors.toList())
                ))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Dodaj logovanje za debagovanje
        System.out.println("User found: " + userDetails.getPassword());
        //System.out.println("Roles: " + userDetails.getAuthorities());
        return userDetails;
    }
}
