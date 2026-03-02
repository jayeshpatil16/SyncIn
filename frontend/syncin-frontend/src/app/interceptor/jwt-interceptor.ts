import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import {environment} from '../environments/environment';
import {Router} from '@angular/router';
import {inject} from '@angular/core';
import {catchError, throwError} from 'rxjs';

const API_URL = environment.apiUrl;

export const jwtInterceptor: HttpInterceptorFn = (req, next) => {

  const router = inject(Router);
  const token = localStorage.getItem('token');

  if (token && req.url.startsWith(`${API_URL}/api/feed/posts`)) {
    const cloned = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    return next(cloned);
  }

  if(req.url.startsWith(`${API_URL}/auth/`) || req.url.startsWith(`${API_URL}/api/feed/`) ) {
    return next(req);
  }

  if(!token) {
    router.navigate(['/login']);
    return next(req);
  }


  const cloned = req.clone({
    setHeaders: {
      Authorization: `Bearer ${token}`
    }
  });
  return next(cloned).pipe(
    catchError((err: HttpErrorResponse) => {
      if(err.status === 401)
      {
        localStorage.clear();
        router.navigate(['/login']);
      }
      return throwError(() => err);
    })
  );
};
