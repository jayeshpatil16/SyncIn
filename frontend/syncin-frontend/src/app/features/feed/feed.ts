import {Component, HostListener} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {PostService} from '../../services/post.service'
import {PostResponse} from '../../models/PostResponse';
import {Router, RouterLink} from '@angular/router';
import {AsyncPipe, NgForOf, NgOptimizedImage} from '@angular/common';
import {BehaviorSubject} from 'rxjs';
import dayjs from 'dayjs';
import relativeTime from 'dayjs/plugin/relativeTime';

dayjs.extend(relativeTime);

@Component({
  selector: 'app-feed',
  imports: [
    RouterLink,
    AsyncPipe,
    NgOptimizedImage
  ],
  templateUrl: './feed.html',
  styleUrl: './feed.css',
})
export class Feed {
  private postSub = new BehaviorSubject<PostResponse[]>([]);
  posts$ = this.postSub.asObservable();
  cursor: string | null = null;
  private loading = false;

  constructor(private feedService: PostService, private router: Router) {
  }

  ngOnInit() {
    this.loadFeed();
  }

  postedAt(date: string)
  {
    if (!date) return 'Just now';
    const d = dayjs(date);
    return d.isValid() ? d.fromNow() : 'Just now';
  }

  loadFeed() {
    if(this.loading) return;
    this.loading = true;
    this.feedService.getPosts(this.cursor, 10).subscribe((posts: PostResponse[]) => {

        const currentPost = this.postSub.value;

        if(currentPost.length != 0) {
          const uniquePosts = posts.filter(
            newPost => currentPost.some(p => p.id === newPost.id)
          );
          this.postSub.next([...currentPost, ...uniquePosts]);
        }
        else
        {
          this.postSub.next([...currentPost, ...posts]);
        }



        if(posts.length > 0)
        {
          this.cursor = posts[posts.length - 1].createdAt;
        }
        console.log("Hours after posting: " + (Number(posts[0].createdAt) - Date.now()));
        this.loading = false;
      });
  }

  goToPost(id: any) {
    this.router.navigate(['/profile', id]);
  }

  @HostListener('window:scroll', [])
  onScroll() {
    if(window.innerHeight + window.scrollY >= document.body.offsetHeight - 200) {
      console.log(this.cursor);
      this.loadFeed();
    }
  }
}
