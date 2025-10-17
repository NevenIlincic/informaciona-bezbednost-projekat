import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import {JwtHelperService} from '@auth0/angular-jwt';

export interface TokenResponse {
  accessToken: string;
  refreshToken: string;
}

export interface LoginDTO {
  email: string;
  password: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'https://localhost:8080/api/auth'; // ako koristiš HTTPS backend
  
  user$ = new BehaviorSubject<string>(this.getRole());
  userState = this.user$.asObservable();

  constructor(private http: HttpClient) {
    this.user$.next(this.getRole());
  }



  login(loginData: LoginDTO): Observable<TokenResponse> {
    return this.http.post<TokenResponse>(`${this.apiUrl}/login`, loginData).pipe(
      tap((response: TokenResponse) => {
        localStorage.setItem('accessToken', response.accessToken);
        localStorage.setItem('refreshToken', response.refreshToken);
        this.user$.next(this.setRole(response.accessToken));
      }),
      catchError(err => {
        console.error('Login error:', err);
        return throwError(() => err);
      })
    );
  }

  logout(): void {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    this.user$.next("");
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('accessToken');
  }

  
  getRole(): any {
    if (this.isLoggedIn()) {
      const accessToken: any = localStorage.getItem('accessToken'); // Ako je prijavljen, token se vec nalazi u localStorage
      const helper = new JwtHelperService();
      return helper.decodeToken(accessToken).role; /// Preuzimanje uloge iz tokena
    }
    return null;
  }

  setRole(accessToken: string): string {
    const helper = new JwtHelperService();
    return helper.decodeToken(accessToken).role;
  }
}
