package es.uv.hemal.elrincondeeva.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import  es.uv.hemal.elrincondeeva.services.JwtService;
import reactor.core.publisher.Mono;

@Component
public class SecurityContextRepository implements ServerSecurityContextRepository {

	@Autowired
	private AuthenticationManager am;
	@Autowired
	private JwtService tp;

	@Override
	public Mono<Void> save(ServerWebExchange swe, SecurityContext sc) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Mono<SecurityContext> load(ServerWebExchange swe) {
		ServerHttpRequest request = swe.getRequest();
		String header = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
		String authToken = null;
		if (header != null && header.startsWith(this.tp.getTokenHeaderPrefix())) {
			authToken = this.tp.getTokenFromHeader(header);
		}	
		if (authToken != null) {
			Authentication auth = new UsernamePasswordAuthenticationToken(authToken, authToken);
			return this.am.authenticate(auth)
					      .map((authentication) -> new SecurityContextImpl(authentication));
		} 
		else {
			return Mono.empty();
		}
	}

}
