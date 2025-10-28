package es.uv.hemal.elrincondeeva.repositories;


import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import es.uv.hemal.elrincondeeva.domain.Review;
import reactor.core.publisher.Flux;


@Repository
public interface ReviewRepository extends R2dbcRepository<Review, Integer> {
    Flux<Review> findAllByOrderByCreationDateDesc();
}

