import {Author} from './Author';
import {CommentResponse} from './CommentResponse';
import {List} from 'postcss/lib/list';

export interface DetailedPostResponse {
  postId: number;
  title: string;
  content: string;
  createdAt: string;
  author: Author;
  upVoteCount: number;
  downVoteCount: number;
  commentCount: number;
  voteType: string;
  comments: CommentResponse[];
}
