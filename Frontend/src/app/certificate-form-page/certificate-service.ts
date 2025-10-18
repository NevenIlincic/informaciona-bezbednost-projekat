import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { NonRevokedCACertificateDTO } from '../../dto/certificate/NonRevokedCACertificateDTO';

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
  // createEECertificate():
}
