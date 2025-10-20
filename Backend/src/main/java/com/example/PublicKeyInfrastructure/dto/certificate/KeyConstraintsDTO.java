package com.example.PublicKeyInfrastructure.dto.certificate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeyConstraintsDTO {
    private boolean isDigitalSignature;
    private boolean isKeyEncipherment;
}
