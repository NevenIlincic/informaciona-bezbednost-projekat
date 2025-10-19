import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { NonRevokedCACertificateDTO } from '../../dto/certificate/NonRevokedCACertificateDTO';
import { EECertificateDTO } from '../../dto/certificate/EECertificateDTO';
import { Pcks12DTO } from '../../dto/certificate/Pcks12DTO';
import { RegularUserCertificateDTO } from '../../dto/certificate/RegularUserCertificateDTO';
import { RevocationDTO } from '../../dto/certificate/RevocationDTO';
import { CertificateTabDTO } from '../../dto/certificate/CertificateTabDTO';
import { DownloadCertificateDTO } from '../../dto/certificate/DownloadCertificateDTO';

@Injectable({
  providedIn: 'root'
})
export class CertificateService {
  private apiUrl = 'https://localhost:8080/api/certificates'
  constructor(private httpClient: HttpClient) {

  }

  downloadCertificate(pcks12DTO: Pcks12DTO){
    const binary = atob(pcks12DTO.encodedPcks12);
        const bytes = new Uint8Array(binary.length);
        for (let i = 0; i < binary.length; i++) {
          bytes[i] = binary.charCodeAt(i);
        }

        // Kreiramo Blob i URL za download
        const blob = new Blob([bytes], { type: 'application/x-pkcs12' });
        const url = window.URL.createObjectURL(blob);

        // Automatski download
        const a = document.createElement('a');
        a.href = url;
        a.download = pcks12DTO.fileName;
        a.click();
        window.URL.revokeObjectURL(url);
  }

  getNonRevokedCACertificates(): Observable<NonRevokedCACertificateDTO[]> {
    return this.httpClient.get<NonRevokedCACertificateDTO[]>(this.apiUrl + "/intermediate");
  }

  createEECertificateRegularUser(eeCertificate: EECertificateDTO): Observable<Pcks12DTO> {
    const params = new HttpParams().set('isAdminCreating', 'false'); // string
    return this.httpClient.post<Pcks12DTO>(
      this.apiUrl + "/end-entity",
      eeCertificate,
      { params: params }
    );
  }
  getRegularUserCertificates(userEmail: string): Observable<RegularUserCertificateDTO[]>{
    return this.httpClient.get<RegularUserCertificateDTO[]>(this.apiUrl + `/end-entity/user/${userEmail}`);
  }

  revokeCertificate(revocationDTO: RevocationDTO): Observable<null>{
    return this.httpClient.delete<null>(this.apiUrl+"/revoke", {body: revocationDTO});
  }

  getAllCertificatesAdmin(): Observable<CertificateTabDTO[]>{
    return this.httpClient.get<CertificateTabDTO[]>(this.apiUrl+"/admin");
  }

  requestCertificateDownload(downloadCertificateDTO: DownloadCertificateDTO): Observable<Pcks12DTO>{
    return this.httpClient.post<Pcks12DTO>(this.apiUrl+"/download", downloadCertificateDTO);
  }
}
