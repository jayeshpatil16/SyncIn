import { Component } from '@angular/core';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {BehaviorSubject, Observable} from 'rxjs';
import {UserProfileResponse} from '../../models/UserProfileResponse';
import {UserService} from '../../services/user.service';

@Component({
  selector: 'app-edit-profile',
    imports: [
        FormsModule,
        ReactiveFormsModule
    ],
  templateUrl: './edit-profile.html',
  styleUrl: './edit-profile.css',
})
export class EditProfile {

  private profileSubject = new BehaviorSubject<UserProfileResponse | null>(null);
  profile$ = this.profileSubject.asObservable();
  profileForm = new FormGroup({
    name: new FormControl('',),
    username: new FormControl('', Validators.required),
    bio: new FormControl(''),
    email: new FormControl('', [Validators.required, Validators.email]),
    avatar_url: new FormControl('')
  });

  isLoading = true;

  isEditProfile: boolean = false;

  constructor(private userService: UserService) {
  }

  ngOnInit() {
    this.getMyProfile();
  }

  getMyProfile() {
    this.userService.getMyProfile().subscribe(res => {
      this.profileSubject.next(res);
      this.profileForm.patchValue(
        {
          name: res.name,
          username: res.username,
          bio: res.bio,
          email: res.email,
          avatar_url: res.avatar_url,
        }
      )
    });
    this.isLoading = false;
  }


  updateProfile()
  {
    this.userService.updateProfile({
      username: this.profileForm.value.username,
      name: this.profileForm.value.name,
      bio: this.profileForm.value.bio,
      email: this.profileForm.value.email,
      avatar_url: this.profileForm.value.avatar_url
    }).subscribe({
      next: response => {
        const current = this.profileSubject.value;
        if(!current) return;
        this.profileSubject.next({
          ...current,
          name: current.name,
          username: response.username,
          bio: response.bio,
          email: response.email,
          avatar_url: response.avatar_url
        })
      }
    })
    alert("Changes saved");
    this.isEditProfile = false;
  }
}
