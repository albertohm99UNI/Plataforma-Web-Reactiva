import {
  Component,
  ChangeDetectorRef,
  OnInit,
  ChangeDetectionStrategy,
  TemplateRef,
  ViewChild,
  LOCALE_ID,
} from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CalendarDateFormatter, CalendarEvent, CalendarMonthViewBeforeRenderEvent, CalendarMonthViewDay, CalendarView, CalendarWeekViewBeforeRenderEvent } from 'angular-calendar';
import {
  addDays,
  addMonths,
  addWeeks,
  addYears,
  endOfDay,
  format,
  isAfter,
  isBefore,
  isEqual,
  startOfDay,
  subDays,
  subMonths,
} from 'date-fns';

import { MyUser } from '../compartido/MyUser';
import { ReservaService } from '../services/reserva.service';
import { MatDialog } from '@angular/material/dialog';
import { ResumenReservaComponent } from '../resumen-reserva/resumen-reserva.component';
import { Reserva } from '../compartido/Reserva';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CustomDateFormatter } from '../reservas/custom-date-formatter.provider';
import { MatPaginator } from '@angular/material/paginator';
import { WeatherService, WeatherWithIcon } from '../services/weather.service';
@Component({
  selector: 'app-reservas',
  templateUrl: './reservas.component.html',
  changeDetection: ChangeDetectionStrategy.Default,
  providers: [
    {
      provide: CalendarDateFormatter,
      useClass: CustomDateFormatter,
    },
  ],
  styleUrls: ['./reservas.component.scss'],
  
})
export class ReservasComponent implements OnInit {
  
  view: CalendarView = CalendarView.Month;
  viewDate: Date = new Date();
  events: CalendarEvent[] = [];
  actualEvent: CalendarEvent[] = [];
 nombre!: string;
 weatherData: WeatherWithIcon[] = [];
aviso!: string;

 selectedDays: Set<Date> = new Set(); 
  teléfono!: string;
  dni!: string;
  apellidos!: string;
  email ='';
  reservedDates: { start: Date; end: Date }[] = [];
  startDate: Date | null = null;
  endDate: Date | null = null;
  @ViewChild('dayEventsTemplate', { static: true })
  dayEventsTemplate!: TemplateRef<any>;
  selectedDay: any;
  today!: Date;
  currentMonthYear: string = '';
  contactForm: FormGroup;
  reservas: Reserva[] = [];
  displayedColumns: string[] = ['id','fecha', 'numPersonas', 'estado']; 
  minDate!: Date;
  maxDate!: Date;


 
  user!: MyUser;
  constructor(private dialog: MatDialog,private cdr: ChangeDetectorRef, private fb: FormBuilder, private reservaService: ReservaService, private snackBar: MatSnackBar, private weatherService: WeatherService) {
    this.contactForm = this.fb.group({
      name: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      emailUsuario: ['', []],
      telefono: ['', [Validators.required, Validators.pattern(/^[0-9]{9}$/)]],
      dni: ['', [Validators.required, Validators.pattern(/^\d{8}[A-Za-z]$/)]],  
      numPersonas: ['', [Validators.required, Validators.min(1), Validators.max(7)]],
      startDate: ['', []],
      endDate: ['', []],
      precio: ['', []],
      

    });
  }
  seleccionado!: boolean;
  ngOnInit(): void {
    if(sessionStorage.getItem('Email') === null){
      this.email='';
    }
    else{
      this.email = sessionStorage.getItem('Email')!;
    }
    if (this.email) {
    this.today = startOfDay(new Date());
    this.initializeEvents();
    this.updateMonthYear();

      this.reservaService.getReservasUser(this.email).subscribe({
        next: (data:Reserva[]) => this.reservas = data,
        error: (err: any) => console.error('Error fetching reservas', err)
      });
    }
    const today = new Date();
    
    this.minDate = new Date(today.setDate(today.getDate() + 7)); 
    this.maxDate = new Date(today.setFullYear(today.getFullYear() + 1));
  }
  initializeEvents(): void {
   
    
    this.reservaService.getReservedDates().subscribe((dates: { start: Date; end: Date }[]) => {
      this.reservedDates = dates;
    
      this.events = [
        ...this.events,
        ...this.reservedDates.map((dateRange) => ({
          start: dateRange.start,
          end: dateRange.end,
          title: `Reservado: ${format(dateRange.start, 'dd/MM/yyyy')} - ${format(
            dateRange.end,
            'dd/MM/yyyy'
          )}`,
          color: { primary: '#d9534f', secondary: '#d9534f' }, 
          allDay: true,
          draggable: false,
          resizable: { beforeStart: false, afterEnd: false },
        })),
      ];
    });
    this.cdr.detectChanges();
  }


  
  dateIsValid(date: Date): boolean {
    return date >= this.minDate && date <= this.maxDate;
  }
 
  
  updateMonthYear() {
    this.currentMonthYear = format(this.viewDate, 'MM yyyy');
    this.cdr.detectChanges();
  }

  onDayClicked({ day }: { day: any }): void {
    this.seleccionado = false;
    this.selectedDay = this.selectedDay === day.date ? null : day.date;
    this.aviso = '';
    // Si no se ha seleccionado un inicio, lo asignamos
    if (!this.startDate) {
      this.aviso = 'Selecciona la fecha final para tu reserva';
      this.startDate = day.date;
      this.selectedDays.add(this.startDate!);  // Marcamos el día como seleccionado
    } else if (!this.endDate) {
      // Si el final está vacío, validamos y asignamos la fecha final
      if (day.date >= this.startDate) {
        this.endDate = day.date;
        this.addEvent();
        this.selectedDays.add(this.endDate!);  // Marcamos también el fin como seleccionado
        this.fetchWeather();
        this.aviso = 'Haz Click sobre el circulo marrón para continuar con la reserva';
      } else {
        this.snackBar.open('La fecha final debe ser posterior a la fecha de inicio.', 'Cerrar', {
          duration: 3000,
        });
      
      }
    } else {
      // Si ya se tiene un rango, reiniciamos la selección
      this.removeEvent(this.startDate, this.endDate);
      this.startDate = day.date;
      this.endDate = null;
      this.aviso = 'Selecciona la fecha final de tu reserva';
      this.selectedDays.clear();  // Limpiamos la selección
      this.selectedDays.add(this.startDate!);  // Volvemos a seleccionar el nuevo inicio
    }
  
    this.cdr.detectChanges();
  }

  addEvent(): void {
    const today = startOfDay(new Date());
    const initOfRange = addWeeks(today, 1);
    const endOfRange = addWeeks(addYears(today, 1), 1); 


    if (
      this.startDate &&
      this.endDate &&
      this.startDate <= this.endDate &&
      this.startDate > today &&
      this.startDate >= initOfRange &&
      this.endDate <= endOfRange
    ) {
      

      const isOverlapping = this.reservedDates.some(
        (reserved) =>
          // Caso 1: Empieza dentro del rango y termina dentro del rango
          (isAfter(this.startDate!, reserved.start) && isBefore(this.startDate!, reserved.end)) ||
          (isAfter(this.endDate!, reserved.start) && isBefore(this.endDate!, reserved.end)) ||
          //Caso 2: Empieza antes y termina dentro del rango
          (isBefore(this.startDate!, reserved.start) && isAfter(this.endDate!, reserved.start) && isBefore(this.endDate!, reserved.end))||
          (isBefore(this.startDate!, reserved.start) && isAfter(this.endDate!, reserved.start) && isEqual(this.endDate!, reserved.end))||
          // Caso 3: Empieza antes y termina después (engloba la reserva existente)
          (isBefore(this.startDate!, reserved.start) && isAfter(this.endDate!, reserved.end)) ||
        
          // Caso 4: Empieza en la misma fecha de inicio de una reserva existente y termina dentro del rango
          (isEqual(this.startDate!, reserved.start) && isBefore(this.endDate!, reserved.end)) ||
          // Caso 5: Empieza en la misma fecha de inicio de una reserva existente y termina fuera del rango
          (isEqual(this.startDate!, reserved.start) && isAfter(this.endDate!, reserved.end)) ||
          // Caso 6: Empieza dentro del rango y termina exactamente en la fecha de fin de una reserva existente
          (isAfter(this.startDate!, reserved.start) && isEqual(this.endDate!, reserved.end)) ||
      
          // Caso 7: Mismo rango exacto que una reserva existente
          (isEqual(this.startDate!, reserved.start) && isEqual(this.endDate!, reserved.end))
          
      );
      if (!isOverlapping) {
        this.events = [
          ...this.events,
          {
            start: this.startDate,
            end: this.endDate,

            title:
              'Rango seleccionado: ' +
              format(this.startDate, 'dd/MM/yyyy') +
              '-' +
              format(this.endDate, 'dd/MM/yyyy') +
              ' , haz click para reservar',
            color: { primary: '#995d03', secondary: '#995d03' },

            allDay: true,
            resizable: {
              beforeStart: true,
              afterEnd: true,
            },
            draggable: false,
          },
        ];
      }
      else
      {
        this.snackBar.open('El rango seleccionado se superpone con una reserva existente.', 'Cerrar', {
          duration: 3000,
        });
        this.cdr.detectChanges();
        
      }
      console.log(this.events);
      this.cdr.detectChanges();
      this.viewDate = new Date(this.viewDate);
    } else if (this.startDate! <= today) {
    
      this.snackBar.open('La fecha de inicio debe ser una fecha futura.', 'Cerrar', {
        duration: 3000,
      });
    }
  }

  removeEvent(startDate: Date, endDate: Date): void {
    this.events = this.events.filter(
      (event) =>
        !(
          event.start.getTime() === startDate.getTime() &&
          event.end?.getTime() === endDate.getTime()
        )
    );
  }

  changeMonth(offset: number) {
    this.viewDate =
      offset > 0
        ? addMonths(this.viewDate, offset)
        : subMonths(this.viewDate, -offset);
    this.updateMonthYear();
  }

  onEventClicked(event: CalendarEvent): void {
    if (
      event.start.getTime() === this.startDate?.getTime() &&
      event.end?.getTime() === this.endDate?.getTime()
    )
    {
      this.seleccionado = true;
      this.reservaService.obtenerDatosUsuario().subscribe({
        next: (data: MyUser|null) => {
          if (data) {
            this.nombre = data.nombre ;
            this.apellidos = data.apellidos;
            this.email = data.email;
            this.teléfono = data.telefono;
            this.dni = data.dni;
            
            this.contactForm.patchValue({
              name: `${this.nombre} ${this.apellidos}`,
              email: this.email,
              telefono: this.teléfono,
              dni: this.dni,

            });
          } else {
            console.error('Data is null or undefined');
          }
          
        },
        error: (error: any) => {
          console.error('Error al obtener los datos del usuario:', error);
        }
      });
 
    }else {
      this.seleccionado = false;
    }
  }

  onSubmit(form: FormGroup) {
    if (form.valid) {
      const emailValue = form.get('email')?.value;
      const nombreValue = form.get('name')?.value;
      const dni = form.get('dni')?.value;
      const telefono = form.get('telefono')?.value;
      const numPersonas = form.get('numPersonas')?.value;
      const startDateValue = this.startDate;
      const endDateValue = this.endDate;

      if (!startDateValue || !endDateValue) {
        console.error('Fecha de inicio o fin no válida');
        return;
      }
    
      const diffTime = Math.abs(endDateValue.getTime() - startDateValue.getTime());
      const diffDays = Math.ceil(diffTime / (1000 * 3600 * 24));
      let precioTotal: number = 0;
      const precioPorDia = 350;
      const precioFinDeSemana = 560;
      const precioPorNocheExtra = 210;


      if (diffDays <= 1) {
  
        precioTotal = precioPorDia;
      } else if (this.esFinDeSemana(startDateValue, endDateValue)) {
    
        precioTotal = precioFinDeSemana;
      } else if (diffDays >= 2) {
     
        precioTotal = diffDays * precioPorNocheExtra;
      }

     
  
      const dialogRef = this.dialog.open(ResumenReservaComponent, {
        data: {
          nombre: nombreValue,
          email: emailValue,
          emailUsuario: sessionStorage.getItem('Email'),
          dni: dni,
          telefono: telefono,
          numPersonas: numPersonas,
          startDate: startDateValue,
          endDate: endDateValue,
          precio: precioTotal
        }
      });

      
    } else {
      this.snackBar.open('Formulario de reserva incorrecto. Rellene todos los datos necesarios', 'Cerrar', {
        duration: 3000,
      });
    }
  }


  esFinDeSemana(startDate: Date, endDate: Date): boolean {
    const startDay = startDate.getDay();
    const endDay = endDate.getDay(); 

    
    return (
      (startDay === 5 && endDay === 6) ||
      (startDay === 6 && endDay === 0) ||
      (startDay === 5 && endDay === 0)  
    );
  }

  fetchWeather(): void {
    if (!this.startDate || !this.endDate) {
      alert('Por favor, ingresa ambas fechas.');
      return;
    }

    this.weatherService
    .getWeather(this.startDate, this.endDate)
    .subscribe((data) => {
    
      this.weatherData = data.map(item => {
        
        item.date = new Date(item.date); 
        return item;
      });
    });
  }
}
