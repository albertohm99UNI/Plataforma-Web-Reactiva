package es.uv.hemal.elrincondeeva.security;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import es.uv.hemal.elrincondeeva.services.JwtService;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

	@Autowired
	private JwtService tp;

	@Override
	public Mono<Authentication> authenticate(Authentication authentication) {
		String token = authentication.getCredentials().toString();
		String username;
		try {
			username = tp.getUsernameFromToken(token);
		} 
		catch (Exception e) {
			username = null;
		}
		if (username != null && !tp.isTokenExpired(token)) {
			Collection<SimpleGrantedAuthority> authorities = tp.getAuthoritiesFromToken(token);
			UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, username, authorities);
			SecurityContextHolder.getContext().setAuthentication(auth);
			return Mono.just(auth);
		} 
		else {
			return Mono.empty();
		}
	}
}

