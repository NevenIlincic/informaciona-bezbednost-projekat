import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Organization } from '../organization/organization.service';

export interface CreateTemplateDTO {
  name: string;
  cnRegex: string;
  sanRegex: string;
  maxTtlDays: number;
  defaultKeyUsage: string;
  defaultExtendedKeyUsage: string;
  caIssuer: any;
  organizationId: number;
}

export interface CreatedTemplateDTO {
  id: number;
  name: string;
  cnRegex: string;
  sanRegex: string;
  maxTtlDays: number;
  defaultKeyUsage: string;
  defaultExtendedKeyUsage: string;
  caIssuer: any;
  organization: Organization;
}

export interface GetTemplateDTO {
  id: number;
  name: string;
  cnRegex: string;
  sanRegex: string;
  maxTtlDays: number;
  defaultKeyUsage: string;
  defaultExtendedKeyUsage: string;
  organizationName: string;
  caIssuerName: string;
}

@Injectable({
  providedIn: 'root'
})
export class TemplateService {
  private apiUrl = 'https://localhost:8080/api/templates'; // prilagodi ako koristiš http ili drugi port

  constructor(private http: HttpClient) {}

  /** Kreiranje novog šablona */
  createTemplate(template: CreateTemplateDTO): Observable<CreatedTemplateDTO> {
    return this.http.post<CreatedTemplateDTO>(this.apiUrl, template);
  }

  /** Dohvatanje svih šablona */
  getAllTemplates(): Observable<GetTemplateDTO[]> {
    return this.http.get<GetTemplateDTO[]>(this.apiUrl);
  }

  /** Dohvatanje pojedinačnog šablona po ID-u */
  getTemplateById(id: number): Observable<GetTemplateDTO> {
    return this.http.get<GetTemplateDTO>(`${this.apiUrl}/${id}`);
  }
}
