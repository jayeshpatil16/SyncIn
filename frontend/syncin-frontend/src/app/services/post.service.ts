import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {environment} from '../environments/environment';
import {PostResponse} from '../models/PostResponse';
import {DetailedPostResponse} from '../models/DetailedPostResponse';

@Injectable({
  providedIn: 'root'
})
export class PostService {

  private baseUrl: string = environment.apiUrl;

  constructor(private http: HttpClient) {}

  createPost(data: any) {
    return this.http.post<PostResponse>(`${this.baseUrl}/api/posts`, data);
  }

  getPosts(cursor: String | null, limit: number = 10) {
    let params: any = { limit: limit };
    if (cursor) {
      params.cursor = cursor;
    }
    return this.http.get<PostResponse[]>(`${this.baseUrl}/api/feed/`,
      {
        params: params
      });
  }

  getDetailedPost(id: any) {
    return this.http.get<DetailedPostResponse>(`${this.baseUrl}/api/feed/posts/${id}`);
  }

  getMyPosts(cursor: String | null, limit: number = 50) {
    let params: any = { limit };
    if (cursor) {
      params.cursor = cursor;
    }
    return this.http.get<PostResponse[]>(`${this.baseUrl}/api/me/posts`,{
      params: {
        params
      }
    });
  }

  editPost(data: any, id: any) {
    return this.http.put(`${this.baseUrl}/api/posts/${id}`, data);
  }

  deletePost(id: any) {
    return this.http.delete(`${this.baseUrl}/api/posts/${id}`);
  }
}
