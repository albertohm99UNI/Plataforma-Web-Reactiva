package es.uv.hemal.elrincondeeva.services;

import es.uv.hemal.elrincondeeva.domain.MyUser;
import es.uv.hemal.elrincondeeva.domain.Role;
import es.uv.hemal.elrincondeeva.domain.UserRole;
import es.uv.hemal.elrincondeeva.dto.MyUserDTO;
import es.uv.hemal.elrincondeeva.repositories.RoleUserRepository;
import es.uv.hemal.elrincondeeva.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SignupServiceTest {

    private UserRepository userRepository;
    private RoleUserRepository roleUserRepository;
    private PasswordEncoder passwordEncoder;

    private SignupService signupService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        roleUserRepository = mock(RoleUserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        signupService = new SignupService(userRepository, roleUserRepository, passwordEncoder);
    }

    private MyUserDTO crearUsuarioDTO(String email) {
        MyUserDTO dto = new MyUserDTO();
        dto.setEmail(email);
        dto.setPassword("pass123");
        dto.setNombre("Juan");
        dto.setApellidos("Pérez");
        dto.setTelefono("123456789");
        dto.setDni("12345678A");
        dto.setFechaNacimiento(LocalDate.of(2000, 1, 1));
        dto.setDireccion("Calle Falsa 123");
        return dto;
    }

    @Test
    void testRegisterUser_emailYaRegistrado_lanzaExcepcion() {
        String email = "usuario@correo.com";
        MyUserDTO dto = crearUsuarioDTO(email);
        MyUser existing = new MyUser();
        existing.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Mono.just(existing));

        Exception ex = assertThrows(Exception.class, () -> signupService.registerUser(dto).block());

        assertTrue(ex.getMessage().contains("ya está en uso"));
    }

    @Test
    void testRegisterUser_nuevoUsuario_guardadoCorrectamente() {
        MyUserDTO dto = crearUsuarioDTO("nuevo@correo.com");

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Mono.empty());
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encodedpass");

        MyUser saved = new MyUser();
        saved.setId(1);
        when(userRepository.save(any(MyUser.class))).thenReturn(Mono.just(saved));

        Role role = new Role();
        role.setId(10);
        when(userRepository.findFirstByName("ROLE_USER")).thenReturn(Mono.just(role));

        when(roleUserRepository.save(any(UserRole.class))).thenReturn(Mono.just(new UserRole()));

        Object result = signupService.registerUser(dto).block();

        assertNotNull(result);
        verify(userRepository).save(any(MyUser.class));
        verify(roleUserRepository).save(any(UserRole.class));
    }

    @Test
    void testRegisterAdmin_nuevoAdmin_guardadoCorrectamente() {
        MyUserDTO dto = crearUsuarioDTO("admin@correo.com");

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Mono.empty());
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encodedpass");

        MyUser saved = new MyUser();
        saved.setId(2);
        when(userRepository.save(any(MyUser.class))).thenReturn(Mono.just(saved));

        Role role = new Role();
        role.setId(20);
        when(userRepository.findFirstByName("ROLE_ADMIN")).thenReturn(Mono.just(role));

        when(roleUserRepository.save(any(UserRole.class))).thenReturn(Mono.just(new UserRole()));

        Object result = signupService.registerAdmin(dto).block();

        assertNotNull(result);
        verify(userRepository).save(any(MyUser.class));
        verify(roleUserRepository).save(any(UserRole.class));
    }

    @Test
    void testRegisterAdmin_emailYaRegistrado_lanzaExcepcion() {
        String email = "admin@correo.com";
        MyUserDTO dto = crearUsuarioDTO(email);
        MyUser existing = new MyUser();
        existing.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Mono.just(existing));

        Exception ex = assertThrows(Exception.class, () -> signupService.registerAdmin(dto).block());

        assertTrue(ex.getMessage().contains("ya está en uso"));
    }
}
