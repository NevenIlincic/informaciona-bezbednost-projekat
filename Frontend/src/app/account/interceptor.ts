import { Injectable } from '@angular/core';
import {
    HttpRequest,
    HttpHandler,
    HttpEvent,
    HttpInterceptor,
    HttpResponse,
    HttpErrorResponse,
} from '@angular/common/http';
import { catchError, Observable, switchMap, throwError } from 'rxjs';
import { AuthService, TokenResponse } from './auth.service';
import { Router } from '@angular/router';
import { RefreshTokenDTO } from '../../dto/authentication/RefreshTokenDTO';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable()
export class Interceptor implements HttpInterceptor {
    private isRefreshing = false;


    constructor(private authService: AuthService, private router: Router, private snackBar: MatSnackBar) { }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        const accessToken = localStorage.getItem('accessToken');

        if (req.headers.has('skip')) {
            return next.handle(req); // preskoči sve
        }

        let clonedReq = req;
        if (accessToken) {
            clonedReq = req.clone({
                headers: req.headers.set('Authorization', 'Bearer ' + accessToken),
            });
        }


        return next.handle(clonedReq).pipe( // Kad access token istekne!
            catchError((error: HttpErrorResponse) => {
                if (error.status === 401 && !this.isRefreshing) {
                    this.isRefreshing = true;

                    const refreshToken = localStorage.getItem('refreshToken');
                    const refreshTokenDTO: RefreshTokenDTO = { refreshToken: refreshToken! }
                    if (!refreshToken) {
                        this.logout();
                        return throwError(() => error);
                    }

                    return this.authService.refreshToken(refreshTokenDTO).pipe(
                        switchMap((newTokens: TokenResponse) => {
                            this.isRefreshing = false;
                            localStorage.setItem('accessToken', newTokens.accessToken);
                            localStorage.setItem('refreshToken', newTokens.refreshToken);

                            console.log("NOVI ACCESS TOKEN: " + newTokens.accessToken);
                            // ponavljamo originalni zahtev sa novim tokenom    
                            const newRequest = req.clone({
                                headers: req.headers.set('Authorization', 'Bearer ' + newTokens.accessToken),
                            });
                            return next.handle(newRequest);
                        }),
                        catchError((err: HttpErrorResponse) => {
                            this.isRefreshing = false;
                            const snack = this.snackBar.open('Session has expired! Please login again!', 'OK', {
                                duration: undefined,
                                verticalPosition: 'bottom',
                                panelClass: ["snack-bar-refresh-token-error"]
                            });
                            snack.onAction().subscribe(() => {
                                this.authService.logout();
                                this.router.navigate(['/login']);
                            });



                            return throwError(() => err);
                        })
                    );
                } else {
                    return throwError(() => error);
                }
            })
        );
    }

    private logout() {
        localStorage.clear();
        this.router.navigate(['/login']);
    }
}