package es.uv.hemal.elrincondeeva.PublicacionesAPI.repositories;

import es.uv.hemal.elrincondeeva.PublicacionesAPI.domain.Media;
import reactor.core.publisher.Flux;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MediaRepository extends ReactiveMongoRepository<Media, String> {
     Flux<Media> findByHashtag(String hashtag);
}
