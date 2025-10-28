package es.uv.hemal.elrincondeeva.repositories;


import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.uv.hemal.elrincondeeva.domain.MyUser;
import es.uv.hemal.elrincondeeva.domain.Role;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends R2dbcRepository<MyUser, Integer> {

	Mono<MyUser> findByEmail(String username);
	
	@Query("SELECT r.* FROM roles r INNER JOIN user_role ur ON r.id = ur.role_id WHERE ur.user_id = :userId")
	Flux<Role> findRolesByUserId(Integer userId);

	@Query("SELECT r.* FROM roles r WHERE r.name = :name")
	Mono<Role> findFirstByName(@Param("name") String name);
	
	@Query("SELECT id FROM users WHERE username = :username")
	Mono<Integer> findByUsername(@Param("username") String username);

}
