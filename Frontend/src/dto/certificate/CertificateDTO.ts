export interface CertificateDTO{
    subjectCommonName: string,
    subjectOrganizationName: string,
    subjectOrganizationalUnit: string,
    subjectCountry: string,
    subjectEmail: string,
    serialNumber: string, 
    validFrom: string,
    validTo: string,
    isDigitalSignature: boolean,
    isKeyEncipherment: boolean,
    isServerAuth: boolean,
    isClientAuth: boolean
}