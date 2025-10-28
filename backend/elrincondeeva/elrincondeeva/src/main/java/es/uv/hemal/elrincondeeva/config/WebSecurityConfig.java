package es.uv.hemal.elrincondeeva.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import es.uv.hemal.elrincondeeva.security.*;
import es.uv.hemal.elrincondeeva.services.JwtService;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class WebSecurityConfig {
@Value("${app.frontend.url}")
	private  String url;
    private final CustomUserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;
    private final JwtService jwtService;

    public WebSecurityConfig(CustomUserDetailsService userDetailsService,
                             AuthenticationManager authenticationManager,
                             SecurityContextRepository securityContextRepository,
                             JwtService jwtService) {
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
        this.jwtService = jwtService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager() {
        var authManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
        authManager.setPasswordEncoder(passwordEncoder());
        return authManager;
    }

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        var authenticationFilter = new CustomAuthenticationFilter(reactiveAuthenticationManager(), jwtService);
        var authorizationFilter = new CustomAuthorizationFilter(jwtService);

        return http
            .csrf(csrf -> csrf.disable()) // ⚠️ Habilitar si usas cookies/session en lugar de JWT
            .authenticationManager(authenticationManager)
            .securityContextRepository(securityContextRepository)

            .headers(headers -> headers
                .contentSecurityPolicy(csp -> csp
                    .policyDirectives("default-src 'self'; script-src 'self'; style-src 'self'; font-src 'self'; img-src 'self' data:"))
                .contentTypeOptions(config -> {}) // X-Content-Type-Options: nosniff
        
            )

            .authorizeExchange(exchanges -> exchanges
                .pathMatchers("/api/v1/login/**").permitAll()
                .pathMatchers("/api/v1/signup").permitAll()
                .pathMatchers("/api/v1/video", "/api/v1/contact", "/api/v1/media", "/api/v1/entorno",
                              "/api/v1/fiestas", "/api/v1/gastronomia", "/api/v1/reviews").permitAll()
                .pathMatchers("/v3/api-docs**", "/swagger-ui/**").permitAll()
                .pathMatchers("/api/v1/addReview").hasAuthority("ROLE_CLIENTE")
                .anyExchange().authenticated()
            )

            .addFilterAt(authenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
            .addFilterBefore(authorizationFilter, SecurityWebFiltersOrder.AUTHORIZATION)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Collections.singletonList("*"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); 
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "*")); 
        config.setExposedHeaders(Arrays.asList("Authorization", "Content-Type", "Access-Control-Allow-Origin","access_token", "refresh_token","username","roles")); 

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
