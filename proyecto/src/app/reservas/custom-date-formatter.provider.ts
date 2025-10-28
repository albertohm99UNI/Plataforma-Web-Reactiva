import { CalendarDateFormatter, DateFormatterParams } from 'angular-calendar';
import { formatDate } from '@angular/common';
import { Injectable } from '@angular/core';

@Injectable()
export class CustomDateFormatter extends CalendarDateFormatter {
  // Sobrescribe los métodos definidos en la clase padre

  public override monthViewColumnHeader({ date }: DateFormatterParams): string {
    // Usamos 'E' para obtener el día completo en español y luego obtenemos la primera letra
    const dayName = formatDate(date, 'E', 'es'); // 'EEEE' devuelve el día completo
    return dayName.charAt(0).toUpperCase(); // Devuelve solo la primera letra en mayúscula
  }

  public override monthViewTitle({ date }: DateFormatterParams): string {
    return formatDate(date, 'MMMM y', 'es');
  }

 
}
