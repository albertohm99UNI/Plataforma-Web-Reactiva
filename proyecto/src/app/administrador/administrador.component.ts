import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatDialog } from '@angular/material/dialog';
import { DialogMotivoComponent } from '../dialog-motivo/dialog-motivo.component';
import { ReservaService } from '../services/reserva.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { HttpErrorResponse } from '@angular/common/http';
import { Reserva } from '../compartido/Reserva';


@Component({
  selector: 'app-administrador',
  templateUrl: './administrador.component.html',
  styleUrls: ['./administrador.component.scss']
})
export class AdministradorComponent implements OnInit {
  displayedColumns: string[] = ['nombre', 'numeroPersonas', 'estado', 'fechas', 'acciones'];
  dataSource = new MatTableDataSource<Reserva>(); 
  finalizadas = new MatTableDataSource<Reserva>(); // Para reservas finalizadas
  reservasOk: Reserva[] = []; // Para almacenar las reservas filtradas
  reservasFinalizadas: Reserva[] = []; // Para almacenar las reservas finalizadas
  filtros = {
  nombre: '',
  dni: '',
  estado:''
};

  constructor(
    private reservaService: ReservaService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
  this.dataSource.filterPredicate = (reserva: Reserva, filtroStr: string): boolean => {
    const f = JSON.parse(filtroStr);

    const coincideNombre = !f.nombre || reserva.nombre?.toLowerCase().includes(f.nombre.toLowerCase());
    const coincideDni = !f.dni || reserva.dni?.toLowerCase().includes(f.dni.toLowerCase());

        const coincideEstado = !f.estado || reserva.estado === f.estado;
    return coincideNombre && coincideDni && coincideEstado;
  };

  this.cargarReservas();
}

applyFilter(): void {
  const filtroStr = JSON.stringify(this.filtros);
  this.dataSource.filter = filtroStr;
}

  
  cargarReservas(): void {
    


this.reservaService.obtenerReservas().subscribe(reservas => {
  this.reservasOk = reservas.filter(reserva => {
    return reserva.estado !== 'FINALIZADA' ;
  });

  this.dataSource.data = this.reservasOk;
  this.reservasFinalizadas = reservas.filter(reserva => reserva.estado === 'FINALIZADA');
  this.finalizadas.data = this.reservasFinalizadas;
});

  }

  aprobarPagoReserva(reserva: Reserva): void {
    
    const dialogRef = this.dialog.open(DialogMotivoComponent, {
      width: '300px',
      data: { reservaInfo: reserva,title: 'Aprobar pago de la reserva', placeholder: 'Mensaje de confirmación', motivo: '' }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        if (result.motivo && result.motivo.trim() !== '') { 
          console.log(result.reservaInfo);
          this.reservaService.pagarReserva(result.reservaInfo, result.motivo).subscribe({
            next: (response: Reserva) => {
              this.snackBar.open('Reserva confirmada con éxito!', 'Cerrar', {
                duration: 3000,
              });
              this.cargarReservas();
            },
            error: (error: HttpErrorResponse) => {
              this.snackBar.open('Error al confirmar la reserva. Inténtalo de nuevo.', 'Cerrar', {
                duration: 3000,
              });
            },
          });
        } else {
          this.snackBar.open('El motivo no puede estar vacío.', 'Cerrar', {
            duration: 3000,
            verticalPosition: 'top',
            horizontalPosition: 'center',
          });
        }
      } else {
        this.snackBar.open('No se proporcionó motivo. Operación cancelada.', 'Cerrar', {
          duration: 3000,
          verticalPosition: 'top',
          horizontalPosition: 'center',
        });
      }
    });
  }

  cancelarReserva(reserva: Reserva): void {
    const dialogRef = this.dialog.open(DialogMotivoComponent, {
      width: '300px',
      data: { reservaInfo: reserva,title: 'Denegar Reserva', placeholder: 'Motivo de rechazo', motivo: '' }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        if (result.motivo && result.motivo.trim() !== '') { 
        this.reservaService.cancelarReserva(result.reservaInfo, result.motivo).subscribe({
          next: (response: Reserva) => {
          
           
           
            this.snackBar.open('Reserva cancelada con éxito!', 'Cerrar', {
              duration: 3000,
            });
            this.cargarReservas();
    
            
          },
          error: (error: HttpErrorResponse) => {

           
            this.snackBar.open('Error al cancelar la reserva. Inténtalo de nuevo.', 'Cerrar', {
              duration: 3000,
            });
          },
        });
        
      
    } else {
        this.snackBar.open('El motivo no puede estar vacío.', 'Cerrar', {
          duration: 3000,
          verticalPosition: 'top',
          horizontalPosition: 'center',
        });
      }
    } else {
      this.snackBar.open('No se proporcionó motivo. Operación cancelada.', 'Cerrar', {
        duration: 3000,
        verticalPosition: 'top',
        horizontalPosition: 'center',
      });
    }
    });
  }
}
