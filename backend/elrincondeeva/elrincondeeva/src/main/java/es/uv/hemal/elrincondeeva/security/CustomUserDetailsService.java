package es.uv.hemal.elrincondeeva.security;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import es.uv.hemal.elrincondeeva.domain.MyUser;
import es.uv.hemal.elrincondeeva.repositories.UserRepository;
//import es.uv.garcosda.domain.MyUser;
//import es.uv.garcosda.repositories.UserRepository;
import reactor.core.publisher.Mono;

@Service

public class CustomUserDetailsService implements ReactiveUserDetailsService {

	
	@Autowired
	UserRepository userRepository;


	@Override
	public Mono<UserDetails> findByUsername(String email) {
		return userRepository.findByEmail(email)
				.switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found"))).flatMap(user -> {
					return userRepository.findRolesByUserId(user.getId()).collectList().map(roles -> {
						user.setRoles(roles);
						return new org.springframework.security.core.userdetails.User(user.getEmail(), 
					              user.getPassword(),
					              getAuthorities(user));
					});
				});
	}

	
	private static Collection<? extends GrantedAuthority> getAuthorities(MyUser user) {
        String[] userRoles = user.getRoles()
                                 .stream()
                                 .map((role) -> role.getName()).toArray(String[]::new);
        
        System.out.println(userRoles);
        Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(userRoles);
        return authorities;
    }

}
