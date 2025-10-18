export interface EECertificateDTO{
    issuerCertificateId: number,
    passwordForCertificate: string,
    subjectCommonName: string,
    subjectOrganizationName: string,
    subjectOrganizationalUnit: string,
    subjectCountry: string,
    subjectEmail: string,
    validFrom: string,
    validTo: string,
    isDigitalSignature: boolean,
    isKeyEncipherment: boolean
}