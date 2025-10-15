package com.example.PublicKeyInfrastructure.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "certificate_extensions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificateExtension {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer  id;

    @ManyToOne
    @JoinColumn(name = "certificate_id")
    private Certificate certificate;
    @Column(name = "oid")
    private String oid;
    @Column(name = "name")
    private String name;
    @Column(name = "value")
    private String value;
    @Column(name = "is_critical")
    private boolean isCritical;
}
