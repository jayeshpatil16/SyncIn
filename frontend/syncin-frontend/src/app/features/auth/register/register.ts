import { Component } from '@angular/core';
import {FormsModule} from '@angular/forms';
import {AuthService} from '../../../services/auth.service';
import {provideRouter, Router, RouterLink} from '@angular/router';
import {routes} from '../../../app.routes';

@Component({
  selector: 'app-register',
  imports: [
    FormsModule,
    RouterLink
  ],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
  name!: string;
  username!: string;
  email!: string;
  password!: string;

  submitted = false;

  constructor(private authService: AuthService, private router: Router) {
  }

  register(registrationForm: any)
  {
    this.submitted = true;

    if (registrationForm.invalid) {return}

    this.authService.register({
      name: this.name,
      username: this.username,
      email: this.email,
      password: this.password,
    }).subscribe({
      next: (result) => {
        alert("Register successful! Please login on next step.");
        registrationForm.resetForm();
        this.router.navigate(['/login']);
      },
      error: error => {
        if(error.status === 409) {
          const field = error.error.field;
          if(field === 'email') {
            alert("User with this email already exists!");
          }
          if(field === 'username') {
            alert("User with this username already exists!");
          }
        }
        else
          alert(error.message);
      }
    })
  }
}
