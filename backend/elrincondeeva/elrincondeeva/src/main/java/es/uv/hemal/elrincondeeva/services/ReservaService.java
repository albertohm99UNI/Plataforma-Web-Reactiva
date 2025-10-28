package es.uv.hemal.elrincondeeva.services;


import java.time.LocalDate;
import java.util.Optional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import es.uv.hemal.elrincondeeva.domain.Reserva;
import es.uv.hemal.elrincondeeva.domain.UserRole;
import es.uv.hemal.elrincondeeva.dto.ReservaDTO;
import es.uv.hemal.elrincondeeva.repositories.ReservaRepository;
import es.uv.hemal.elrincondeeva.repositories.RoleRepository;
import es.uv.hemal.elrincondeeva.repositories.RoleUserRepository;
import es.uv.hemal.elrincondeeva.repositories.UserRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

@Service
public class ReservaService {

    
   private final ReservaRepository reservaRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final RoleUserRepository roleUserRepository;
    private final RoleRepository roleRepository;
    public ReservaService(ReservaRepository reservaRepository, EmailService emailService, UserRepository userRepository, RoleUserRepository roleUserRepository, RoleRepository roleRepository) {
        this.reservaRepository = reservaRepository;
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.roleUserRepository = roleUserRepository; 
        this.roleRepository = roleRepository;
    }

public Mono<Integer> confirmarReserva(ReservaDTO reservaDTO) {
    Reserva nuevaReserva = new Reserva();
    
    nuevaReserva.setFechaInicio(reservaDTO.getStartDate());
    nuevaReserva.setFechaFin(reservaDTO.getEndDate());
   
    nuevaReserva.setNumPersonas(reservaDTO.getNumPersonas());
    nuevaReserva.setPrecioTotal(reservaDTO.getPrecio());
    nuevaReserva.setEstado(Reserva.EstadoReserva.CONFIRMADA);
    nuevaReserva.setEmail(reservaDTO.getEmail());
    nuevaReserva.setNombre(reservaDTO.getNombre());
    nuevaReserva.setDni(reservaDTO.getDni());
    nuevaReserva.setTelefono(reservaDTO.getTelefono());

    return userRepository.findByEmail(reservaDTO.getEmailUsuario())
        .switchIfEmpty(Mono.error(new RuntimeException("ERROR_USER")))
        .flatMap(user -> {
            nuevaReserva.setUsuarioId(user.getId());
            return reservaRepository.existeReservaEnFechas(
                    nuevaReserva.getFechaInicio(),
                    nuevaReserva.getFechaFin()
                  
                ).flatMap(existe -> {
                    if (!existe) {
                        return reservaRepository.save(nuevaReserva)
                            .doOnSuccess(savedReserva -> {
                                emailService.sendEmail(savedReserva)
                                    .doOnSuccess(_ -> System.out.println("Email enviado"))
                                    .doOnError(error -> new RuntimeException("ERROR_EMAIL"))
                                    .subscribe();
                            })
                        .map(Reserva::getId);
                    }
                    else {
                        return Mono.error(new RuntimeException("EXISTE_RESERVA"));
                    }
                });
        });
}
    public Mono<Reserva> confirmarPagoReserva(Integer idReserva, String motivo) {
       
        return reservaRepository.findById(idReserva)
            .doOnSuccess(reservaPagada -> {
            
                
                emailService.sendEmailPay(reservaPagada, motivo)
                    .doOnSuccess(_ -> System.out.println("Email enviado"))
                    .doOnError(error -> System.out.println("Error al enviar email: " + error.getMessage()))
                    .subscribe();
            });
            
           
    }

    public Mono<Reserva> cancelarReserva(Integer idReserva, String motivo) {
       
        return reservaRepository.findById(idReserva)
            .doOnSuccess(reservaPagada -> {
            
                
                emailService.sendEmailCancel(reservaPagada, motivo)
                    .doOnSuccess(_ -> System.out.println("Email enviado"))
                    .doOnError(error -> System.out.println("Error al enviar email: " + error.getMessage()))
                    .subscribe();
            })
            
       ;
    }
    

    public Flux<Integer> getReservas() {
        return  reservaRepository.findAllId();
    }

    public Flux<Reserva> getAllDataReservas() {
        return  reservaRepository.findAllByOrderByFechaInicioDesc();
    }

    public Flux<Tuple2<LocalDate, LocalDate>> getAllDataReservasFromToday() {
        return reservaRepository.findByFechaInicioGreaterThanEqual(LocalDate.now())
        .map(reserva -> Tuples.of(reserva.getFechaInicio(), reserva.getFechaFin()));
    }

    

    public Flux<Reserva> getReservasByUser(String email) {
        return  reservaRepository.findByEmail(email);
    }
    public Mono<Reserva> setEstado(Integer reservaId, String estado, String motivo) {
        return reservaRepository.findById(reservaId)
                .flatMap(reserva -> {
                    reserva.setEstado(Reserva.EstadoReserva.valueOf(estado));
                    if (estado.equals("PAGADA")) {
                        return confirmarPagoReserva(reservaId, motivo)
                                .then(reservaRepository.save(reserva)); 
                    } else if (estado.equals("CANCELADA")) {
                        return cancelarReserva(reservaId, motivo)
                                .then(reservaRepository.save(reserva)); 
                    } else {
                        return reservaRepository.save(reserva); 
                    }
                });
    }
    
    @Scheduled(cron = "0 0 2 * * ?") 
public void actualizarRolesUsuarios() {
    LocalDate hoy = LocalDate.now();

    reservaRepository.findByEstadoAndFechaInicioLessThanEqual(Reserva.EstadoReserva.PAGADA, hoy)
    .flatMap(reserva -> 
        roleUserRepository.findByUserId(reserva.getUsuarioId()) // Obtener los roles del usuario
            .collectList() // Convertimos los roles a una lista
            .flatMap(roles -> 
                roleRepository.findByName("ROLE_CLIENTE") // Buscamos el rol CLIENTE de manera reactiva
                    .flatMap(roleCliente -> {
                        // Verificamos si la lista de roles ya contiene ROLE_CLIENTE
                        boolean tieneClienteRole = roles.stream()
                            .anyMatch(role -> role.getRoleId().equals(roleCliente.getId()));
                        System.out.println(tieneClienteRole);
                        if (tieneClienteRole) {
                            return Mono.empty(); // No hacer nada si ya tiene el rol CLIENTE
                        } else {
                            // Si no tiene el rol, lo asignamos
                            UserRole roleAssign = new UserRole();
                            roleAssign.setUserId(reserva.getUsuarioId());
                            roleAssign.setRoleId(roleCliente.getId());
                            System.out.println("Asignando rol CLIENTE al usuario " + roleAssign.getUserId()+ " con id " + roleAssign.getRoleId());
                            // Actualizamos el estado de la reserva a FINALIZADA
                            reserva.setEstado(Reserva.EstadoReserva.FINALIZADA);
                            return reservaRepository.save(reserva) 
                                .then(roleUserRepository.save(roleAssign)); 
                        }
                    })
            )
    )
    .subscribe(
        _ -> System.out.println("Roles actualizados correctamente."),
        error -> System.err.println("Error al actualizar roles: " + error.getMessage())
    );


        
        
}

}