import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {environment} from '../environments/environment';
import {LoginResponse} from '../models/LoginResponse';
import {BehaviorSubject} from 'rxjs';
import {Profile} from '../models/Profile';
import {UserService} from './user.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private loggedIn = new BehaviorSubject<Profile | false>(false);

  isLoggedIn$ = this.loggedIn.asObservable();

  private hasToken(): boolean {
    return !!localStorage.getItem('token');
  }

  private baseUrl: string = environment.apiUrl;

  constructor(private http: HttpClient, private userService: UserService) {}

  login(data: any) {
    const response = this.http.post<LoginResponse>(`${this.baseUrl}/auth/login`, data);
    return response;
  }

  register(data: any) {
    return this.http.post<String>(`${this.baseUrl}/auth/register`, data, {
      responseType: 'text' as 'json'
    });
  }

  setToken(name: string, id: number, token: string, username: string, email: string, avatar_url: string) {
    localStorage.setItem('token', token);
    localStorage.setItem('id', id.toString());
    localStorage.setItem('username', username);
    localStorage.setItem('email', email);
    localStorage.setItem('avatar_url', avatar_url);
    localStorage.setItem('name', name);
    this.ifUser();
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }

  ifUser() {
    if(this.hasToken()){
      this.userService.getMyProfile().subscribe(res => {
      this.loggedIn.next({
        id: Number(localStorage.getItem('id')) ?? null,
        name: res.name,
        email: res.email,
        username: res.username,
        avatar_url: res.avatar_url,
        followers_count: res.followers,
        followings_count: res.following,
        bio: res.bio,
      })
      });
    }
    else
      this.loggedIn.next(false);
  }

  getToken() {
    return localStorage.getItem('token');
  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    localStorage.removeItem('email');
    localStorage.removeItem('avatar_url');
    localStorage.removeItem('name');
    this.loggedIn.next(false);
  }
}
