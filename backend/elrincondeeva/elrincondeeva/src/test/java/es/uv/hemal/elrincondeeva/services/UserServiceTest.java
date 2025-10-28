package es.uv.hemal.elrincondeeva.services;

import es.uv.hemal.elrincondeeva.domain.MyUser;
import es.uv.hemal.elrincondeeva.domain.Role;
import es.uv.hemal.elrincondeeva.domain.UserRole;
import es.uv.hemal.elrincondeeva.repositories.RoleRepository;
import es.uv.hemal.elrincondeeva.repositories.RoleUserRepository;
import es.uv.hemal.elrincondeeva.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private RoleUserRepository roleUserRepository;
    private RoleRepository roleRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        roleUserRepository = mock(RoleUserRepository.class);
        roleRepository = mock(RoleRepository.class);

        userService = new UserService();
        userService.userRepository = userRepository;
        userService.roleUserRepository = roleUserRepository;
        userService.roleRepository = roleRepository;
    }

    @Test
    void testGetNombre_usuarioExistente() {
        String email = "test@correo.com";
        MyUser user = new MyUser();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Mono.just(user));

        MyUser result = userService.getNombre(email).block();

        assertNotNull(result);
        assertEquals(email, result.getEmail());
    }

    @Test
    void testGetNombre_usuarioNoExiste() {
        String email = "noexiste@correo.com";

        when(userRepository.findByEmail(email)).thenReturn(Mono.empty());

        MyUser result = userService.getNombre(email).block();

        assertNull(result);
    }

    @Test
    void testGetRoles_retornaRolesCorrectamente() {
        String email = "usuario@correo.com";
        MyUser user = new MyUser();
        user.setId(1);
        user.setEmail(email);

        UserRole ur1 = new UserRole();
        ur1.setRoleId(100);
        UserRole ur2 = new UserRole();
        ur2.setRoleId(200);

        Role r1 = new Role();
        r1.setId(100);
        r1.setName("ROLE_USER");
        Role r2 = new Role();
        r2.setId(200);
        r2.setName("ROLE_ADMIN");

        when(userRepository.findByEmail(email)).thenReturn(Mono.just(user));
        when(roleUserRepository.findByUserId(1)).thenReturn(Flux.just(ur1, ur2));
        when(roleRepository.findById(100)).thenReturn(Mono.just(r1));
        when(roleRepository.findById(200)).thenReturn(Mono.just(r2));

        List<Role> roles = userService.getRoles(email).collectList().block();

        assertNotNull(roles);
        assertEquals(2, roles.size());
        assertEquals("ROLE_USER", roles.get(0).getName());
        assertEquals("ROLE_ADMIN", roles.get(1).getName());
    }

    @Test
    void testGetRoles_usuarioNoExiste() {
        String email = "invalido@correo.com";

        when(userRepository.findByEmail(email)).thenReturn(Mono.empty());

        List<Role> roles = userService.getRoles(email).collectList().block();

        assertNotNull(roles);
        assertTrue(roles.isEmpty());
    }
}
