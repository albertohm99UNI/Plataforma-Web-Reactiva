package es.uv.hemal.elrincondeeva.endpoints;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.uv.hemal.elrincondeeva.domain.Reserva;
import es.uv.hemal.elrincondeeva.dto.ReservaDTO;
import es.uv.hemal.elrincondeeva.services.ReservaService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/v1")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @PostMapping("/reserva")
public Mono<ResponseEntity<Integer>> confirmarReserva(@RequestBody ReservaDTO reservaDTO) {
    return reservaService.confirmarReserva(reservaDTO)
        .map(reserva -> new ResponseEntity<>(reserva, HttpStatus.CREATED))
        .onErrorResume(e -> {
           if(e.getMessage().contains("ERROR_USER")) {
                return Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND));
            } else if(e.getMessage().contains("ERROR_RESERVA")) {
                return Mono.just(new ResponseEntity<>(HttpStatus.CONFLICT));
            } else {
                System.err.println("Error confirmacion de reserva: " + e.getMessage());
                return Mono.just(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
            }

        });
}

    @GetMapping("/reservas")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public Flux<Reserva> getReservas() {
    return reservaService.getAllDataReservas()
            .onErrorResume(e -> {
               
                System.err.println("Error fetching reservas: " + e.getMessage());
                return Flux.empty();
            });
}

@GetMapping("/reservasProximas")

public Flux<Tuple2<LocalDate, LocalDate>> getReservasFromDate() {
    return reservaService.getAllDataReservasFromToday()
            .onErrorResume(e -> {
               
                System.err.println("Error fetching reservas: " + e.getMessage());
                return Flux.empty();
            });
}
@PutMapping("/{id}/estado")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public Mono<ResponseEntity<Reserva>> actualizarEstado(@PathVariable Integer id, @RequestParam String estado, @RequestBody String motivo) {
    return reservaService.setEstado(id, estado, motivo)
    .map(result -> ResponseEntity.ok().body(result))
    .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))  
    .onErrorResume(e -> {
        return Mono.just(ResponseEntity.internalServerError().build()); 
    });
}


@GetMapping("/reservasUser")
public Flux<Reserva> getReservasUsuario(@RequestParam String email) {
    return reservaService.getReservasByUser(email);
}
    
    
    
    
}

