package com.example.PublicKeyInfrastructure.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "templates")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Template {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;
    @ManyToOne
    @JoinColumn(name = "ca_issuer_id")
    private Certificate caIssuer;
    @Column(name = "cn_regex")
    private String cnRegex;
    @Column(name = "san_regex")
    private String sanRegex;
    @Column(name = "max_ttl_days")
    private Integer maxTtlDays;
    @Column(name = "default_key_usage")
    private String defaultKeyUsage;
    @Column(name = "default_extended_key_usage")
    private String defaultExtendedKeyUsage;

}
