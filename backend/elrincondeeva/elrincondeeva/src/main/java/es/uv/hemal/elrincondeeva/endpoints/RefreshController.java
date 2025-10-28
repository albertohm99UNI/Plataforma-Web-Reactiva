package es.uv.hemal.elrincondeeva.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;


import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import es.uv.hemal.elrincondeeva.security.CustomUserDetailsService;
import es.uv.hemal.elrincondeeva.services.JwtService;
import es.uv.hemal.elrincondeeva.services.UserService;

@RestController
@RequestMapping("api/v1/login")
public class RefreshController {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    
    @Autowired
    private JwtService jwtService;

    @GetMapping("/refresh")
    public Mono<Void> refreshToken(ServerHttpRequest request, ServerHttpResponse response) {
        String authHeader = request.getHeaders().getFirst("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String token = this.jwtService.getTokenFromHeader(authHeader);
                String username = this.jwtService.getUsernameFromToken(token);
                System.out.println("Token a refrescar: " + token);
                Mono<UserDetails> user = this.customUserDetailsService.findByUsername(username);
                return user.flatMap(userDetails -> {
                    String access_token = jwtService.generateAccessToken(userDetails.getUsername(),
                            userDetails.getAuthorities().stream().map(Object::toString).collect(Collectors.toList()));
                    System.out.println("Nuevo access token: " + access_token);
                    response.getHeaders().add("access_token", access_token);
                    response.getHeaders().add("refresh_token", token);
                    response.getHeaders().add("roles", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()).toString());
                    Map<String, String> tokens = new HashMap<>();
                    tokens.put("access_token", access_token);
                    tokens.put("refresh_token", token);
                   
                     tokens.put("roles",userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()).toString() );
                    response.setStatusCode(HttpStatus.OK);
                    response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                    String responseBody = "{\"access_token\": \"" + access_token + "\", \"refresh_token\": \"" + token + "\", \"roles\": \"" + userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()).toString() + "\"}";
                    return response.writeWith(Mono.just(response.bufferFactory().wrap(responseBody.getBytes())));
                });
            } catch (Exception exception) {
                response.getHeaders().add("error", exception.getMessage());
          
                response.setStatusCode(HttpStatus.FORBIDDEN);
                Map<String, String> error = new HashMap<>();
                error.put("error_msg", exception.getMessage());
                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                String responseBody = "{\"error_msg\": \"" + exception.getMessage() + "\"}";
                return response.writeWith(Mono.just(response.bufferFactory().wrap(responseBody.getBytes())));
            }
        } else {
            response.setStatusCode(HttpStatus.BAD_REQUEST);
            return response.setComplete();
        }
    }
}
