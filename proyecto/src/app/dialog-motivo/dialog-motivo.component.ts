import { Component, Inject, ViewEncapsulation } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Reserva } from '../compartido/Reserva';

@Component({
  selector: 'app-dialog-motivo',
  templateUrl: './dialog-motivo.component.html',
  styleUrls: ['./dialog-motivo.component.scss']
})
export class DialogMotivoComponent {
  motivo: string = '';
  constructor(
    public dialogRef: MatDialogRef<DialogMotivoComponent>,
    @Inject(MAT_DIALOG_DATA) public data: {reservaInfo: Reserva, title: string; placeholder: string; motivo: string }
  ) {}

  onAceptar(): void {
    
    if (this.motivo.trim() !== '') {
      this.dialogRef.close({ motivo: this.motivo, reservaInfo: this.data.reservaInfo });
    } else {
      this.dialogRef.close(null);
    }
  }

  onCancelar(): void {
    this.dialogRef.close(null); 
  }
}
