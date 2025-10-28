package es.uv.hemal.elrincondeeva.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import es.uv.hemal.elrincondeeva.domain.Reserva;
import reactor.core.publisher.Mono;

@Service
public class EmailService {
   @Autowired
    private JavaMailSender emailSender;
     @Value("${spring.email.email}")
    private String email;
    public Mono<Reserva> sendEmail(Reserva reserva) {
        SimpleMailMessage message = new SimpleMailMessage(); 
        message.setSubject("Reserva número: " + reserva.getId()+" confirmada "+reserva.getNombre()+"|"+reserva.getDni());
        message.setText("Reserva confirmada a nombre de: " + reserva.getNombre()+ " con número de reserva: " + reserva.getId() + " para el día " + reserva.getFechaInicio() + " hasta el día " + reserva.getFechaFin() + " para " + reserva.getNumPersonas() + " personas. El precio total es de " + reserva.getPrecioTotal() + "€. Gracias por confiar en nosotros."+"Pongase en contacto con el administrador para realizar el pago de otra forma al 669 511 506 o proceda con la realización del pago mediante transferencia al siguiente número de cuenta:. Ponga en el concepto su nombre de la reserva y el número de la reserva que le hemos facilitado."); 
        message.setTo(reserva.getEmail());
        message.setFrom(email);
        emailSender.send(message);
        return Mono.just(reserva);
    }

    public Mono<Reserva> sendEmailPay(Reserva reserva, String motivo) {
        SimpleMailMessage message = new SimpleMailMessage(); 
        message.setSubject("Reserva número: " + reserva.getId()+" pagada "+reserva.getNombre()+"|"+reserva.getDni());
        message.setText("Reserva pagada. Cantidad recibida: " + reserva.getPrecioTotal() + "€"+" a nombre de: " + reserva.getNombre()+ " con número de reserva: " + reserva.getId() + " para el día " + reserva.getFechaInicio() + " hasta el día " + reserva.getFechaFin() + " para " + reserva.getNumPersonas() + " personas. A continuación le enviamos las instrucciones detalladas para hacer check-in: "+motivo+" Gracias por confiar en nosotros."); 
        message.setTo(reserva.getEmail());
        message.setFrom(email);
        emailSender.send(message);
        return Mono.just(reserva);
    }

    public Mono<Reserva> sendEmailCancel(Reserva reserva,String motivo) {
        SimpleMailMessage message = new SimpleMailMessage(); 
        message.setSubject("Reserva número: " + reserva.getId()+" cancelada "+reserva.getNombre()+"|"+reserva.getDni());
        message.setText("Reserva cancelada."+ "A nombre de: " + reserva.getNombre()+ " con número de reserva: " + reserva.getId() + " para el día " + reserva.getFechaInicio() + " hasta el día " + reserva.getFechaFin() + " para " + reserva.getNumPersonas() + " personas. Lamentamos las molestias ocasionadas, esperamos volver a contar con vosotros. A continuación, le indicamos el motivo de la cancelación de su reserva: "+motivo); 
        message.setTo(reserva.getEmail());
        message.setFrom(email);
        emailSender.send(message);
        return Mono.just(reserva);
    }
}
