import { Routes } from '@angular/router';
import {AuthGuard} from './Guards/AuthGuard';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./features/feed/feed').then(m => m.Feed)
  },
  {
    path: 'post/:postId',
    loadComponent: () => import('./features/post-detail/post-detail').then(m => m.PostDetail)
  },
  {
    path: 'editProfile',
    loadComponent: () => import('./features/edit-profile/edit-profile').then(m => m.EditProfile),
    canActivate: [AuthGuard],
  },
  {
    path: 'changePassword',
    loadComponent: () => import('./features/change-password/change-password').then(m => m.ChangePassword),
    canActivate: [AuthGuard],
  },
  {
    path: 'profile/:profileId',
    loadComponent: () => import('./features/profile/profile').then(m => m.Profile),
    canActivate: [AuthGuard],
  },
  {
    path: 'login',
    loadComponent: () => import('./features/auth/login/login').then(m => m.Login)
  },
  {
    path: 'register',
    loadComponent: () => import('./features/auth/register/register').then(m => m.Register)
  },
  {
    path: '**',
    redirectTo: ''
  }
];
