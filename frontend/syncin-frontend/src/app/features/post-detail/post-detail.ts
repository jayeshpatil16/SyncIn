import { Component } from '@angular/core';
import {AsyncPipe, NgOptimizedImage} from '@angular/common';
import {BehaviorSubject} from 'rxjs';
import {PostService} from '../../services/post.service';
import {DetailedPostResponse} from '../../models/DetailedPostResponse';
import {CommentResponse} from '../../models/CommentResponse';
import {ActivatedRoute, Router} from '@angular/router';
import {CommentService} from '../../services/comment.service';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {VoteService} from '../../services/vote.service';

@Component({
  selector: 'app-post-detail',
  imports: [AsyncPipe, NgOptimizedImage, FormsModule, ReactiveFormsModule],
  templateUrl: './post-detail.html',
  styleUrl: './post-detail.css',
})
export class PostDetail {
  private postSubject = new BehaviorSubject<DetailedPostResponse | null>(null);
  detailedPost$ = this.postSubject.asObservable();
  constructor(private route: ActivatedRoute, private postService: PostService, private commentService: CommentService, private voteService: VoteService, private router: Router) {}
  id!: number;
  commentContent!: string;

  voteType: string | null = null;

  commentOnPost: boolean = false;
  replyOnPost: number | null = null;

  showReplies: number | null = null;

  private commentSubject = new BehaviorSubject<CommentResponse[]>([]);
  commentReplies$ = this.commentSubject.asObservable();


  ngOnInit() {
    this.id = Number(this.route.snapshot.paramMap.get('postId'));
    this.getDetailedPost(Number(this.id));
  }

  hasLoggedIn(): boolean {
    if(localStorage.getItem('token') == null) {
      alert("please register or login first!");
      return true;
    }
    return false;
  }

  getReplies(id: number) {
    this.commentService.getReplies(id).subscribe((replies) => {
      this.commentSubject.next(replies);
    })

    this.showReplies = id;
  }

  reply(id: number){
    this.commentService.createComment(this.id, {
      content: this.commentContent,
      parentCommentId: id
    }).subscribe((replies) => {
      const current = this.commentSubject.value;
      this.commentSubject.next([replies]);
    })

    this.replyOnPost = null;
    this.showReplies = id;
  }

  showReplyOnPost(id : number)
  {
    if(this.replyOnPost === id)
      this.replyOnPost = null;
    else this.replyOnPost = id;
  }

  hideCommentReplies()
  {
    this.showReplies = null;
  }

  showCommentBox()
  {
    if(this.hasLoggedIn())
      return;
    this.commentOnPost = !this.commentOnPost;
  }

  getDetailedPost(id: number | null) {
    this.postService.getDetailedPost(id).subscribe({
      next: post => {
        this.voteType = post.voteType;
        this.postSubject.next(post);
      }
    });
  }

  goToPost(id: any) {
    this.router.navigate(['/profile', id]);
  }

  addComment() {
    if (!this.commentContent.trim()) return;

    this.commentService.createComment(this.id, {
      content: this.commentContent
    })
      .subscribe(comment => {

        const current = this.postSubject.value;
        if (!current) return;

        this.postSubject.next({
          ...current,
          commentCount: current.commentCount + 1,
          comments: [comment, ...current.comments]
        });

        this.commentContent = '';
      });

    this.commentOnPost = false;
  }

  voteComment(id: number, type: string) {
    // this.voteService.voteComment(id, {
    //   voteType: type
    // }).subscribe(vote => {
    //   const current = this.postSubject.value;
    //   if(!current) return;
    //   console.log(vote.voteType);
    //   let {upVoteCount, downVoteCount, voteType} = current;
    //
    //   downVoteCount = vote.downVoteCount;
    //   upVoteCount = vote.upVoteCount;
    //   voteType = vote.voteType;
    //
    //   this.postSubject.next({
    //     ...current,
    //     upVoteCount,
    //     downVoteCount,
    //     voteType
    //   })
    // })
  }

  votePost(id: number, type: string) {

    if(type === this.voteType)
      this.voteType = null;
    else
      this.voteType = type;

    this.voteService.votePost(id, {
      voteType: type
    }).subscribe(vote => {
      const current = this.postSubject.value;
      if(!current) return;
      console.log(vote.voteType);
      let {upVoteCount, downVoteCount, voteType} = current;

      downVoteCount = vote.downVoteCount;
      upVoteCount = vote.upVoteCount;
      voteType = vote.voteType;

      this.postSubject.next({
        ...current,
        upVoteCount,
        downVoteCount,
        voteType
      })
    })
  }

}
