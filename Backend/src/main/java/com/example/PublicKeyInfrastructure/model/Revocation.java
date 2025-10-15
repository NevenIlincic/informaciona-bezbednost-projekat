package com.example.PublicKeyInfrastructure.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "revocation_list")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Revocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "certificate_id")
    private Certificate certificate;
    @ManyToOne
    @JoinColumn(name = "revoked_by_user_id")
    private AuthenticatedUser user;
    @Column(name = "revocation_date")
    private LocalDateTime revocationDate;
    @Column(name = "revocation_reason")
    private String revocationReason;
}
