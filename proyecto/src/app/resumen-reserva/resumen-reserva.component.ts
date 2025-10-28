import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ReservaService } from '../services/reserva.service';
import { HttpErrorResponse } from '@angular/common/http';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Reserva } from '../compartido/Reserva';
@Component({
  selector: 'app-resumen-reserva',
  templateUrl: './resumen-reserva.component.html',
  styleUrls: ['./resumen-reserva.component.scss'],
  
})
export class ResumenReservaComponent {
  loading: boolean = false;
  constructor(
    public dialogRef: MatDialogRef<ResumenReservaComponent>,
   
    @Inject(MAT_DIALOG_DATA) public data: Reserva,
    private reservaService: ReservaService, 
    private snackBar: MatSnackBar
  ) {}

  confirmarReserva(): void {
    this.loading = true;
   
    this.reservaService.confirmarReserva(this.data).subscribe({
      next: (response: any) => {
      
        this.loading = false; 
        
        this.snackBar.open('Reserva confirmada con éxito!', 'Cerrar', {
          duration: 3000,
        });

        window.location.reload();
        this.dialogRef.close(); 
      },
      error: (error: HttpErrorResponse) => {
       
        this.loading = false; 

       
        this.snackBar.open('Error al confirmar la reserva. Inténtalo de nuevo.', 'Cerrar', {
          duration: 3000,
        });
      },
    });
  }

  onNoClick(): void {
    
    this.dialogRef.close();
  }
}
