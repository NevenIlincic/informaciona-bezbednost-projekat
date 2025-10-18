import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { NonRevokedCACertificateDTO } from '../../dto/certificate/NonRevokedCACertificateDTO';
import { EECertificateDTO } from '../../dto/certificate/EECertificateDTO';

@Injectable({
  providedIn: 'root'
})
export class CertificateService {
  private apiUrl = 'https://localhost:8080/api/certificates'
  constructor(private httpClient: HttpClient){

  }

  getNonRevokedCACertificates(): Observable<NonRevokedCACertificateDTO[]>{
    return this.httpClient.get<NonRevokedCACertificateDTO[]>(this.apiUrl+"/intermediate");
  }

  createEECertificate(eeCertificate: EECertificateDTO): Observable<EECertificateDTO>{
    return this.httpClient.post<EECertificateDTO>(this.apiUrl+"/end-entity", eeCertificate);
  }
  // createEECertificate():
}
