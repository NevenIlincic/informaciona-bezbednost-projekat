import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthenticatedUserService {
  private apiUrl = 'https://localhost:8080/api/users';

  constructor(private http: HttpClient) {}

  registerUser(userData: any): Observable<void> {
    return this.http.post<void>(this.apiUrl, userData);
  }

  getUserByEmail(email: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${email}`);
  }
}