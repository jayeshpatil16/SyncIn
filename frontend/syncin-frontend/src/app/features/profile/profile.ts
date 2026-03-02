import {Component, HostListener} from '@angular/core';
import {PostService} from '../../services/post.service';
import {UserService} from '../../services/user.service';
import {ActivatedRoute, RouterLink} from '@angular/router';
import {UserProfileResponse} from '../../models/UserProfileResponse';
import {BehaviorSubject, Observable} from 'rxjs';
import {AsyncPipe, NgOptimizedImage} from '@angular/common';
import {PostResponse} from '../../models/PostResponse';
import {FollowService} from '../../services/follow.service';
import {FormsModule} from '@angular/forms';
import dayjs from 'dayjs';

@Component({
  selector: 'app-profile',
  imports: [
    AsyncPipe,
    NgOptimizedImage,
    RouterLink,
    FormsModule
  ],
  templateUrl: './profile.html',
  styleUrl: './profile.css',
})
export class Profile {

  private postSub = new BehaviorSubject<PostResponse[]>([]);
  posts$ = this.postSub.asObservable();
  cursor: string | null = null;
  private loading = false;

  username: string | null = null;

  title: string | null = null;
  content: string | null = null;

  isCreatePostOpen: boolean = false;

  private profileSubject = new BehaviorSubject<UserProfileResponse | null>(null);
  profile$ = this.profileSubject.asObservable();
  id!: number;

  constructor(private userService: UserService, private route: ActivatedRoute, private postService: PostService, private followService: FollowService) {
    this.username = localStorage.getItem('username');
  }

  postedAt(date: string)
  {
    if (!date) return 'Just now';
    const d = dayjs(date);
    return d.isValid() ? d.fromNow() : 'Just now';
  }

  ngOnInit() {
    this.id = Number(this.route.snapshot.paramMap.get('profileId'));
    if(this.id == 0)
    {
      this.getMyProfile();
    }
    else{
      this.getUserProfile(this.id);
    }

    this.loadPosts();
  }

  setCreatePost() {
    this.isCreatePostOpen = !this.isCreatePostOpen;
  }

  loadPosts() {
    if(this.loading) return;
    this.loading = true;
    this.postService.getMyPosts(this.cursor, 50).subscribe((posts: PostResponse[]) => {

      const currentPost = this.postSub.value;

      this.postSub.next([...currentPost, ...posts]);

      if(posts.length > 0)
      {
        this.cursor = posts[posts.length - 1].createdAt;
      }
      this.loading = false;
    });
  }

  followUser(id: number)
  {
    this.followService.followUser(id).subscribe({
      next: response => {
        const current = this.profileSubject.value;
        if(!current) return;
        this.profileSubject.next({
          ...current,
          followers: response.followerCount,
          following: response.followingCount,
          followedByMe: true
        })
        console.log(response)},
      error: error => {

      }
    });
    // window.location.reload();
  }

  unFollowUser(id: number)
  {
    this.followService.unFollowUser(id).subscribe({
      next: response => {
        const current = this.profileSubject.value;
        if(!current) return;
        this.profileSubject.next({
          ...current,
          followers: response.followerCount,
          following: response.followingCount,
          followedByMe: false
        })
        console.log(response)},
      error: error => {

      }
    });
    console.log(id);
    // window.location.reload();
  }


  getUserProfile(id: number) {
    this.userService.getUserProfile(id).subscribe(res => {
      this.profileSubject.next(res);
    });
  }

  getMyProfile() {
    this.userService.getMyProfile().subscribe(res => {
      this.profileSubject.next(res);
    });
  }



  createPost()
  {
    if(this.title == null || this.content == null)
      return;

    this.postService.createPost({
      title: this.title,
      content: this.content,
    }).subscribe(res => {
      const content = this.postSub.value;

      this.postSub.next([...content, res]);
    })

    this.isCreatePostOpen = false;
  }



  @HostListener('window:scroll', [])
  onScroll() {
    if(window.innerHeight + window.scrollY >= document.body.offsetHeight - 200) {
      this.loadPosts();
    }
  }
}
