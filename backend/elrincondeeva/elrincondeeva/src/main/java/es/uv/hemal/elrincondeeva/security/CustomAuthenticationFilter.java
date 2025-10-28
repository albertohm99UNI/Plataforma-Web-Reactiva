package es.uv.hemal.elrincondeeva.security;


import java.util.stream.Collectors;

import org.springframework.security.authentication.ReactiveAuthenticationManager;

import org.springframework.security.core.Authentication;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;

import es.uv.hemal.elrincondeeva.services.JwtService;
import reactor.core.publisher.Mono;

public class CustomAuthenticationFilter extends AuthenticationWebFilter {

	private JwtService jwtService;

	public CustomAuthenticationFilter(ReactiveAuthenticationManager authenticationManager, JwtService jwtService) {
		super(authenticationManager);
	
		this.jwtService = jwtService;

	}

	@Override
	protected Mono<Void> onAuthenticationSuccess(Authentication authentication, WebFilterExchange webFilterExchange) {

		User user = (User) authentication.getPrincipal();
		String access_token = jwtService.generateAccessToken(user.getUsername(), 
				 user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
		String refresh_token = jwtService.generateRefreshToken(user.getUsername(), 
				   user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));

		for (GrantedAuthority authority : authentication.getAuthorities()) {
			System.out.println(authority.getAuthority());
		}

		webFilterExchange.getExchange().getResponse().getHeaders().add("access_token", access_token);
		webFilterExchange.getExchange().getResponse().getHeaders().add("refresh_token", refresh_token);
		webFilterExchange.getExchange().getResponse().getHeaders().add("username", authentication.getName());
		webFilterExchange.getExchange().getResponse().getHeaders().add("roles", authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()).toString());
		return Mono.empty();
	}
}
