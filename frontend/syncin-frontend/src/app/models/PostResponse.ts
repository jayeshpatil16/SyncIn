import {Author} from './Author';

export interface PostResponse {
  title: string;
  content: string;
  id: number;
  createdAt: string;
  author: Author;
  upVoteCount: number;
  downVoteCount: number;
  commentCount: number;
}
