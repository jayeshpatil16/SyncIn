import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {environment} from '../environments/environment';
import {FollowResponse} from '../models/FollowResponse';
import {FollowListResponse} from '../models/FollowListResponse';

@Injectable({
  providedIn: 'root'
})
export class FollowService {

  private baseUrl: string = environment.apiUrl;

  constructor(private http: HttpClient) {}

  followUser(id: number) {
    return this.http.post<FollowResponse>(`${this.baseUrl}/api/user/${id}/follow`, null);
  }

  unFollowUser(id: any) {
    console.log(id);
    return this.http.delete<FollowResponse>(`${this.baseUrl}/api/user/${id}/unfollow`);
  }

  getFollowers(id: number) {
    return this.http.get<FollowListResponse[]>(`${this.baseUrl}/api/user/${id}/followers`);
  }

  getFollowing(id: any) {
    return this.http.get<FollowListResponse[]>(`${this.baseUrl}/api/user/${id}/following`);
  }
}
