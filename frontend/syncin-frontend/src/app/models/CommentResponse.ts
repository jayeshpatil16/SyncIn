import {Author} from './Author';

export interface CommentResponse {
  commentId: number;
  content: string;
  createdAt: string;
  author: Author;
  upVoteCount: number;
  downVoteCount: number;
}
