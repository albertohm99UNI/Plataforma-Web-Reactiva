package es.uv.hemal.elrincondeeva.repositories;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import es.uv.hemal.elrincondeeva.domain.Role;
import reactor.core.publisher.Mono;

@Repository
public interface  RoleRepository extends R2dbcRepository<Role, Integer> {
    Mono<Role> findByName(String name);
    Mono<String> findNameById(Integer id);
}
