import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {environment} from '../environments/environment';
import {UserProfileResponse} from '../models/UserProfileResponse';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private baseUrl: string = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getMyProfile() {
    return this.http.get<UserProfileResponse>(`${this.baseUrl}/api/profile`);
  }

  getUserProfile(id: any) {
    return this.http.get<UserProfileResponse>(`${this.baseUrl}/api/user/${id}`);
  }

  changePassword(data: any) {
    return this.http.put(`${this.baseUrl}/api/profile/password`, data);
  }

  updateProfile(data: any) {
    return this.http.put<UserProfileResponse>(`${this.baseUrl}/api/profile`, data);
  }

  updatePassword(data: any) {
    return this.http.put<string>(`${this.baseUrl}/api/profile/password`, data, {
      responseType: 'text' as 'json'
    });
  }

  deleteProfile() {
    return this.http.delete(`${this.baseUrl}/api/profile`);
  }
}
