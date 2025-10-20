export interface IntermediateCertificateDTO {
    issuerCertificateId: number,
    subjectCommonName: string,
    subjectOrganizationName: string,
    subjectOrganizationalUnit: string,
    subjectCountry: string,
    subjectEmail: string,
    validFrom: string,
    validTo: string,
    isDigitalSignature: boolean,
    isKeyEncipherment: boolean,
    isServerAuth: boolean,
    isClientAuth: boolean,
    isAdminCreating: boolean
}