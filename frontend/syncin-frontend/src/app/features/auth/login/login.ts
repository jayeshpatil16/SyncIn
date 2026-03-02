import { Component } from '@angular/core';
import {FormGroup, FormsModule, ReactiveFormsModule} from "@angular/forms";
import {Router, RouterLink} from "@angular/router";
import {AuthService} from '../../../services/auth.service';

@Component({
  selector: 'app-login',
  imports: [
    FormsModule,
    RouterLink,
    ReactiveFormsModule
  ],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
    identifier!: string;
    password!: string;
    submitted = false;

    constructor(private authService: AuthService, private router: Router) {
    }

    login(loginForm: any) {

      this.submitted = true;

      if (loginForm.invalid) {return;}

      this.authService.login({
        identifier: this.identifier,
        password: this.password
      }).subscribe({
        next: (res) => {
          this.authService.setToken(res.name, res.userId, res.token, res.username, res.email, res.avatar_url);
          alert("Login successful!");
          this.identifier = '';
          this.password = '';
          this.router.navigate(['']);
        },
        error: (err) => {
          alert("Failed to login, check your credentials then try again!");
        }
      })
    }
}
