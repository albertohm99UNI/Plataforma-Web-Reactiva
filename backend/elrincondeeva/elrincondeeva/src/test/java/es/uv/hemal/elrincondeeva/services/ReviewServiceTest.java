package es.uv.hemal.elrincondeeva.services;

import es.uv.hemal.elrincondeeva.domain.Review;
import es.uv.hemal.elrincondeeva.domain.Role;
import es.uv.hemal.elrincondeeva.domain.MyUser;
import es.uv.hemal.elrincondeeva.dto.ReviewDTO;
import es.uv.hemal.elrincondeeva.repositories.ReviewRepository;
import es.uv.hemal.elrincondeeva.repositories.RoleRepository;
import es.uv.hemal.elrincondeeva.repositories.RoleUserRepository;
import es.uv.hemal.elrincondeeva.repositories.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewServiceTest {

    private ReviewRepository reviewRepository;
    private RoleUserRepository roleUserRepository;
    private RoleRepository roleRepository;
    private UserRepository userRepository;

    private ReviewService reviewService;

    @BeforeEach
    void setUp() {
        reviewRepository = mock(ReviewRepository.class);
        roleUserRepository = mock(RoleUserRepository.class);
        roleRepository = mock(RoleRepository.class);
        userRepository = mock(UserRepository.class);

        reviewService = new ReviewService();
        reviewService.reviewRepository = reviewRepository;
        reviewService.roleRepository = roleRepository;
        reviewService.roleUserRepository = roleUserRepository;
        reviewService.userRepository = userRepository;
    }

    @Test
    void testSaveReview_guardadoCorrectamente() {
        // Datos simulados
        ReviewDTO dto = new ReviewDTO();
        dto.setReview("Muy buena experiencia");
        dto.setRate(4);
        dto.setRateLimpieza(5);
        dto.setRateServicios(4);
        dto.setRateUbicacion(3);

        MyUser user = new MyUser();
        user.setId(1);
        user.setEmail("test@correo.com");

        Role role = new Role();
        role.setId(2);
        role.setName("ROLE_CLIENTE");

        Review savedReview = new Review();
        savedReview.setReview(dto.getReview());
        savedReview.setUserId(1);

        // Mocks de seguridad
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn("test@correo.com");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        try (MockedStatic<ReactiveSecurityContextHolder> contextHolder = mockStatic(ReactiveSecurityContextHolder.class)) {
            contextHolder.when(ReactiveSecurityContextHolder::getContext)
                    .thenReturn(Mono.just(securityContext));

            // Mocks de repositorios
            when(userRepository.findByEmail("test@correo.com")).thenReturn(Mono.just(user));
            when(roleRepository.findByName("ROLE_CLIENTE")).thenReturn(Mono.just(role));
            when(roleUserRepository.deleteByUserIdAndRoleId(1, 2)).thenReturn(Mono.empty());
            when(reviewRepository.save(any(Review.class))).thenReturn(Mono.just(savedReview));

            Review result = reviewService.saveReview(dto).block();

            assertNotNull(result);
            assertEquals(dto.getReview(), result.getReview());
            assertEquals(1, result.getUserId());
        }
    }

    @Test
    void testSaveReview_usuarioNoExiste_lanzaExcepcion() {
        ReviewDTO dto = new ReviewDTO();
        dto.setReview("Experiencia mala");
        dto.setRate(1);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn("noexiste@correo.com");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        try (MockedStatic<ReactiveSecurityContextHolder> contextHolder = mockStatic(ReactiveSecurityContextHolder.class)) {
            contextHolder.when(ReactiveSecurityContextHolder::getContext)
                    .thenReturn(Mono.just(securityContext));

            when(userRepository.findByEmail("noexiste@correo.com")).thenReturn(Mono.empty());

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> reviewService.saveReview(dto).block());

            assertEquals("User not found", exception.getMessage());
        }
    }

    @Test
    void testGetReviews_ordenCorrecto() {
        Review r1 = new Review();
        r1.setReview("Reseña 1");

        Review r2 = new Review();
        r2.setReview("Reseña 2");

        when(reviewRepository.findAllByOrderByCreationDateDesc()).thenReturn(Flux.just(r2, r1));

        List<Review> reviews = reviewService.getReviews().collectList().block();

        assertNotNull(reviews);
        assertEquals(2, reviews.size());
        assertEquals("Reseña 2", reviews.get(0).getReview()); // orden descendente esperado
    }
}
