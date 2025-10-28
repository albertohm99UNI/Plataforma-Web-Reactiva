package es.uv.hemal.extraccion.extraccion.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import es.uv.hemal.extraccion.extraccion.models.Recurso;
import reactor.core.publisher.Flux;

public interface RecursoRepository extends ReactiveMongoRepository<Recurso, String> {
    Flux<Recurso> findByTitle(String title);
    Flux<Recurso> findByCategoria(String categoria);
}
