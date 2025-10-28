export class Reserva {
  id: number;
    nombre: string;
    email: string;
    emailUsuario: string;
    dni: string;
    telefono: string;
    startDate: Date;
    endDate: Date;
    numPersonas: number;
    precio: number;
    estado: string;

  
    constructor(
      id: number,
      nombre: string,
      email: string,
      emailUsuario: string,
      dni: string,
      telefono: string,
      startDate: Date,
      endDate: Date,
      numPersonas: number,
      precio: number,
      estado: string,
     
    ) {
      this.id = id;
      this.nombre = nombre;
      this.email = email;
      this.emailUsuario = emailUsuario;
      this.dni = dni;
      this.telefono = telefono;
      this.startDate = startDate;
      this.endDate = endDate;
      this.numPersonas = numPersonas;
      this.precio = precio;
      this.estado = estado;
      
    }
  }