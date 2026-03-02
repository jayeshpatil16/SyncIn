import { Component } from '@angular/core';
import {
  AbstractControl,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  ValidationErrors,
  ValidatorFn, Validators
} from "@angular/forms";
import {UserService} from '../../services/user.service';

@Component({
  selector: 'app-change-password',
    imports: [
        ReactiveFormsModule
    ],
  templateUrl: './change-password.html',
  styleUrl: './change-password.css',
})
export class ChangePassword {

  passwordMatchValidator: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
    const oldPassword = control.get('oldPassword');
    const reEnterOldPassword = control.get('reEnterOldPassword');

    return oldPassword && reEnterOldPassword && oldPassword.value !== reEnterOldPassword.value
      ? { passwordMismatch: true }
      : null;
  }

  constructor(private userService: UserService) {
  }

  PasswordForm = new FormGroup({
    oldPassword: new FormControl('', Validators.required),
    reEnterOldPassword: new FormControl('', Validators.required),
    newPassword: new FormControl('', Validators.required),
  }, { validators: this.passwordMatchValidator });

  changePassword() {
    this.userService.updatePassword({
      oldPassword: this.PasswordForm.value.oldPassword,
      newPassword: this.PasswordForm.value.newPassword
    }).subscribe({
      next: value => {
        alert("Password changed successfully!");
        this.PasswordForm.reset();
      },
      error: error => {
        alert("Password can't be changed, enter correct old password");
      }
    });
  }
}
