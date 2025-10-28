package es.uv.hemal.elrincondeeva.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import es.uv.hemal.elrincondeeva.domain.Contact;
import jakarta.mail.internet.MimeMessage;
import reactor.core.publisher.Mono;

@Service
public class ContactService {


    private final JavaMailSender mailSender;

    @Value("${spring.email.email}")           
    private String to;

    public ContactService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public Mono<Contact> sendEmail(Contact contact) {

        return Mono.fromCallable(() -> {

            String reply = Optional.ofNullable(contact.getEmail())
                                   .orElse("")
                                   .trim();

            if (reply.contains("\r") || reply.contains("\n") ||
                !org.apache.commons.validator.routines
                    .EmailValidator.getInstance().isValid(reply)) {

                throw new IllegalArgumentException("E-mail inválido");
            }

            MimeMessage mime = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mime, "UTF-8");

            helper.setFrom("noreply@tudominio.com");          
            helper.setTo(to);                                
            helper.setReplyTo(reply);                        
            helper.setSubject("Consulta de: " + contact.getName());
            helper.setText(contact.getMessage(), false);     

            mailSender.send(mime);
            return contact;
        });
    }
}

