package es.uv.hemal.elrincondeeva.security;


import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.uv.hemal.elrincondeeva.services.JwtService;
import reactor.core.publisher.Mono;

import java.util.Collection;

import java.util.Map;

@Component
public class CustomAuthorizationFilter implements WebFilter {

	private final JwtService jwtService;

	public CustomAuthorizationFilter(JwtService jwtService) {
		this.jwtService = jwtService;
	}

	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
	    String path = exchange.getRequest().getPath().value();

	    if ("/api/v1/login".equals(path)) {
	        return chain.filter(exchange);
	    } else {
	        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
	        if (authHeader != null && authHeader.startsWith("Bearer ")) {
	            try {
	                String token = jwtService.getTokenFromHeader(authHeader);
	                String username = jwtService.getUsernameFromToken(token);
	                
	                Collection<SimpleGrantedAuthority> authorities = jwtService.getAuthoritiesFromToken(token);

	                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null, authorities);
	                SecurityContextHolder.getContext().setAuthentication(auth);
	                if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
	                    for (GrantedAuthority authority : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
	                        System.out.println("- " + authority.getAuthority());
	                    }

	                    return chain.filter(exchange);
	                } else {

	                    return handleException(exchange, new Exception("Forbidden"));
	                }
	          

	            } catch (Exception exception) {
	                return handleException(exchange, exception);
	            }
	        } else {
	            return chain.filter(exchange);
	        }
	    }
	}


	private Mono<Void> handleException(ServerWebExchange exchange, Throwable throwable) {
		exchange.getResponse().getHeaders().set("error", throwable.getMessage());
		exchange.getResponse().setRawStatusCode(403);
		Map<String, String> error = Map.of("error_msg", throwable.getMessage());
		exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
		try {
			return exchange.getResponse().writeWith(Mono
					.just(exchange.getResponse().bufferFactory().wrap(new ObjectMapper().writeValueAsBytes(error))));
		} catch (Exception e) {
			return Mono.error(e);
		}
	}
}
