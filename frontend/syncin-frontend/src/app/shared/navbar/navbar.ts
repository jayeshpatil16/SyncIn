import {Component} from '@angular/core';
import {Router, RouterLink} from '@angular/router';
import {AuthService} from '../../services/auth.service';
import {Observable} from 'rxjs';
import {AsyncPipe, NgOptimizedImage} from '@angular/common';
import {Profile} from '../../models/Profile';
import {LucideAngularModule} from 'lucide-angular';

@Component({
  selector: 'app-navbar',
  imports: [
    RouterLink,
    AsyncPipe,
    LucideAngularModule,
    NgOptimizedImage
  ],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css',
})
export class Navbar {

  constructor(private authService: AuthService, private router: Router) {
    this.checkLogin();
  }

  isLoggedIn$!: Observable<Profile | false>;
  username: string | null = null;
  email: string | null = null;

  ngOnInit(){
    this.isLoggedIn$ = this.authService.isLoggedIn$;
  }

  checkLogin(){
    this.authService.ifUser();
  }

  logout() {
    this.authService.logout();
    // window.location.reload();
    this.router.navigate(['']);
  }
}
