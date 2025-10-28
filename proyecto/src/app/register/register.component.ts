import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { Router } from '@angular/router';
import { RegistroService } from '../services/registro.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { HttpErrorResponse } from '@angular/common/http';
@Component({
  selector: 'app-register',


  templateUrl: './register.component.html',
  styleUrl: './register.component.scss',
})
export class RegisterComponent implements OnInit {
  registerForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private registerService: RegistroService, 
    private router: Router ,
     private snackBar: MatSnackBar
  ) {
    this.registerForm = this.fb.group({
      password: ['', [Validators.required, Validators.minLength(6)]],
      nombre: ['', [Validators.required, Validators.minLength(2)]],
      apellidos: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],  
      telefono: ['', [Validators.required, Validators.pattern(/^[0-9]{9}$/)]],
      dni: ['', [Validators.required, Validators.pattern(/^\d{8}[A-Za-z]$/)]],  
      fechaNacimiento: ['', [Validators.required]],  
      direccion: ['', [Validators.required, Validators.minLength(10)]]  
    });
  }

  ngOnInit(): void {}

  onSubmit(): void {
   
  
    if (this.registerForm.valid) {
      const formData = this.registerForm.value;
  
      this.registerService.registerUser(formData).subscribe(
        response => {
          this.snackBar.open('Usuario registrado correctamente', 'Cerrar', {
            duration: 3000,
          });
          this.router.navigate(['/login']);
        },
        error => {
         
          this.snackBar.open(`${error.message}`, 'Cerrar', {
            duration: 3000,
          });
        }
      );
    } else {
      this.snackBar.open('Formulario invalido rellene correctamente los datos.', 'Cerrar', {
        duration: 3000,
      });
    }
  }
  
  
}

