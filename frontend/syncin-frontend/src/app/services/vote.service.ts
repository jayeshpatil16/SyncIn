import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {environment} from '../environments/environment';
import {VoteResponse} from '../models/VoteResponse';

@Injectable({
  providedIn: 'root'
})
export class VoteService {

  private baseUrl: string = environment.apiUrl;

  constructor(private http: HttpClient) {}

  votePost(id: number, type: any) {
    return this.http.post<VoteResponse>(`${this.baseUrl}/api/posts/${id}/vote`, type);
  }

  voteComment(id: any, type: any) {
    return this.http.post<VoteResponse>(`${this.baseUrl}/api/comments/${id}/vote`, type);
  }
}
