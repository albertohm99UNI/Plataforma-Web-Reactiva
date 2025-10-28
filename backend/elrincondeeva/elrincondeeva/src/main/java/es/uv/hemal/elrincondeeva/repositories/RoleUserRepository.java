package es.uv.hemal.elrincondeeva.repositories;


import org.springframework.data.r2dbc.repository.R2dbcRepository;

import org.springframework.stereotype.Repository;

import es.uv.hemal.elrincondeeva.domain.UserRole;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Repository
public interface RoleUserRepository extends R2dbcRepository<UserRole, Integer> {
    Flux<UserRole> findByUserId(Integer userId);
    Flux<UserRole> findByRoleId(Integer roleId);
    Mono<Void> deleteByUserIdAndRoleId(Integer userId, Integer roleId);
}
