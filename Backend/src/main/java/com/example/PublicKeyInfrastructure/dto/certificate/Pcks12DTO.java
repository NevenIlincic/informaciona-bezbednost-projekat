package com.example.PublicKeyInfrastructure.dto.certificate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pcks12DTO {
    private String encodedPcks12;
    private String fileName;
}
