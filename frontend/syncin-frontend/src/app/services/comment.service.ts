import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {environment} from '../environments/environment';
import {FollowResponse} from '../models/FollowResponse';
import {FollowListResponse} from '../models/FollowListResponse';
import {CommentResponse} from '../models/CommentResponse';

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  private baseUrl: string = environment.apiUrl;

  constructor(private http: HttpClient) {}

  createComment(id: number, data: any) {
    return this.http.post<CommentResponse>(`${this.baseUrl}/api/posts/${id}/comments`, data)
  }

  getComments(id: any) {
    return this.http.delete<CommentResponse[]>(`${this.baseUrl}/api/posts/${id}/comments`);
  }

  getReplies(id: number) {
    return this.http.get<CommentResponse[]>(`${this.baseUrl}/api/posts/${id}/replies`);
  }
}
