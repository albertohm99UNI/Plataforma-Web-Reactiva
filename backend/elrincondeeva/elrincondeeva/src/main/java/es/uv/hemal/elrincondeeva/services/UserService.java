package es.uv.hemal.elrincondeeva.services;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import es.uv.hemal.elrincondeeva.domain.MyUser;
import es.uv.hemal.elrincondeeva.domain.Role;
import es.uv.hemal.elrincondeeva.repositories.RoleRepository;
import es.uv.hemal.elrincondeeva.repositories.RoleUserRepository;
import es.uv.hemal.elrincondeeva.repositories.UserRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;
    @Autowired
    RoleUserRepository roleUserRepository;
    @Autowired
    RoleRepository roleRepository;

  
	public Mono<MyUser> getNombre(String username) {
        return userRepository.findByEmail(username)
            .doOnNext(user -> System.out.println("Usuario logueado: " + user))  
            .switchIfEmpty(Mono.empty());  
    }

    public Flux<Role> getRoles(String username) {
    return userRepository.findByEmail(username)
        .flatMapMany(user -> roleUserRepository.findByUserId(user.getId())
            .flatMap(roleUser -> roleRepository.findById(roleUser.getRoleId()))
        );
}
	
}