package es.uv.hemal.elrincondeeva.services;

import es.uv.hemal.elrincondeeva.domain.Reserva;
import es.uv.hemal.elrincondeeva.domain.MyUser;
import es.uv.hemal.elrincondeeva.domain.Role;
import es.uv.hemal.elrincondeeva.domain.UserRole;
import es.uv.hemal.elrincondeeva.dto.ReservaDTO;
import es.uv.hemal.elrincondeeva.repositories.ReservaRepository;
import es.uv.hemal.elrincondeeva.repositories.RoleRepository;
import es.uv.hemal.elrincondeeva.repositories.RoleUserRepository;
import es.uv.hemal.elrincondeeva.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;

public class ReservaServiceTest {
 private ReservaRepository reservaRepository;
    private EmailService emailService;
    private UserRepository userRepository;
    private RoleUserRepository roleUserRepository;
    private RoleRepository roleRepository;

    private ReservaService reservaService;

    @BeforeEach
    public void setUp() {
        reservaRepository = mock(ReservaRepository.class);
        emailService = mock(EmailService.class);
        userRepository = mock(UserRepository.class);
        roleUserRepository = mock(RoleUserRepository.class);
        roleRepository = mock(RoleRepository.class);

        reservaService = new ReservaService(reservaRepository, emailService, userRepository, roleUserRepository, roleRepository);
    }

    @Test
public void testConfirmarReserva_usuarioExiste_reservaConfirmada() {
    ReservaDTO dto = new ReservaDTO(null, null, null, null, null, null, null, 0, 0);
    dto.setStartDate(LocalDate.of(2025, 1, 1));
    dto.setEndDate(LocalDate.of(2025, 1, 3));
    dto.setNumPersonas(2);
    dto.setPrecio(200.0);
    dto.setEmail("cliente@email.com");
    dto.setNombre("Juan");
    dto.setDni("12345678A");
    dto.setTelefono("600000000");
    dto.setEmailUsuario("cliente@email.com");

    MyUser user = new MyUser();
    user.setId(99);

    Reserva reservaGuardada = new Reserva();
    reservaGuardada.setId(123);
    reservaGuardada.setUsuarioId(99);

    when(userRepository.findByEmail("cliente@email.com")).thenReturn(Mono.just(user));
    when(reservaRepository.existeReservaEnFechas(dto.getStartDate(), dto.getEndDate()))
        .thenReturn(Mono.just(false));
    when(reservaRepository.save(any(Reserva.class))).thenReturn(Mono.just(reservaGuardada));
    when(emailService.sendEmail(any(Reserva.class))).thenReturn(Mono.empty());

    Integer id = reservaService.confirmarReserva(dto).block();

    assertNotNull(id);
    assertEquals(123, id);
    verify(reservaRepository, times(1)).save(any(Reserva.class));
    verify(emailService, times(1)).sendEmail(reservaGuardada);
}

   @Test
public void testConfirmarReserva_usuarioNoExiste_lanzaExcepcion() {
    ReservaDTO dto = new ReservaDTO(null, null, null, null, null, null, null, 0, 0);
    dto.setEmailUsuario("noexiste@email.com");

    when(userRepository.findByEmail("noexiste@email.com")).thenReturn(Mono.empty());

    RuntimeException ex = assertThrows(RuntimeException.class, () -> {
        reservaService.confirmarReserva(dto).block();
    });

    assertEquals("ERROR_USER", ex.getMessage());
}

    @Test
    public void testCancelarReserva_enviaEmail() {
        Reserva reserva = new Reserva();
        reserva.setId(1);
        when(reservaRepository.findById(1)).thenReturn(Mono.just(reserva));
        when(emailService.sendEmailCancel(reserva, "Cancelación por motivo")).thenReturn(Mono.empty());

        Reserva result = reservaService.cancelarReserva(1, "Cancelación por motivo").block();

        assertNotNull(result);
        assertEquals(reserva, result);
        verify(emailService, times(1)).sendEmailCancel(reserva, "Cancelación por motivo");
    }
}
