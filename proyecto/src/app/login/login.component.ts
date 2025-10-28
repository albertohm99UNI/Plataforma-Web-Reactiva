// login.component.ts
import { Component, OnInit, Renderer2, ViewEncapsulation } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { catchError, of } from 'rxjs';
@Component({
  selector: 'app-login',

  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  authError: string | null = null;

  constructor(private fb: FormBuilder,private authService:AuthService ,private renderer: Renderer2,private router: Router) {
    
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
    
  }
  ngOnInit(): void {
    const script = this.renderer.createElement('script');
    script.src = 'app/assets/js/login.js';
    console.log(script);
    script.type = 'text/javascript';
    this.renderer.appendChild(document.body, script);
  }
  onSubmit(): void {
    if (this.loginForm.valid) {
      const { username, password } = this.loginForm.value;
      this.closeError();
      this.authService.login(username, password).pipe(
        catchError((error) => {
         
          this.authError = 'El usuario o contraseña no existen en nuestra base de datos.';
          return of(null); 
        })
      ).subscribe({
        next: (response) => {
          if (response) {
            console.log('Login exitoso');
            this.router.navigate(['/inicio']); 
          }
        }
      });
    } else {
      this.authError = 'El usuario o contraseña no cumplen con las especificaciones.';
    }
  }
  
  closeError(): void {
    this.authError = null;
  }
  goToRegister() {
    this.router.navigateByUrl('/register');
  }
}
