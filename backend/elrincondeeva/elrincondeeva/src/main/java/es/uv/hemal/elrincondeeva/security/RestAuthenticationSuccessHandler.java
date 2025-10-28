package es.uv.hemal.elrincondeeva.security;

import org.springframework.http.HttpStatus;

import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

@Component
public class RestAuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {

	private final ObjectMapper mapper;

	public RestAuthenticationSuccessHandler(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
		ServerHttpResponse response = webFilterExchange.getExchange().getResponse();
		response.setStatusCode(HttpStatus.OK);

		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		try {
			return response.writeWith(Mono.just(response.bufferFactory().wrap(mapper.writeValueAsBytes(userDetails))));
		} catch (JsonProcessingException e) {
			return Mono.error(e);
		}
	}
}
