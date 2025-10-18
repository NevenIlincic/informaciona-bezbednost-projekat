import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { NonRevokedCACertificateDTO } from '../../dto/certificate/NonRevokedCACertificateDTO';
import { EECertificateDTO } from '../../dto/certificate/EECertificateDTO';
import { Pcks12DTO } from '../../dto/certificate/Pcks12DTO';

@Injectable({
  providedIn: 'root'
})
export class CertificateService {
  private apiUrl = 'https://localhost:8080/api/certificates'
  constructor(private httpClient: HttpClient) {

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
  // createEECertificate():
}
