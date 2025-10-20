export interface CertificateTabDTO{
    id: number,
    subjectCommonName: string,
    subjectOrganizationName: string,
    subjectOrganizationalUnit: string,
    subjectCountry: string,
    subjectEmail: string,
    serialNumber: string,
    validFrom: string,
    validTo: string,
    digitalSignature: string,
    keyEncipherment: string,
    revoked: boolean,
    revocationDate: string,
    revocationReason: string,
    serverAuth: string,
    clientAuth: string,
    type: string
}