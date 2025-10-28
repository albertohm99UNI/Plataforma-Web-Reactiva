package hemal.uv.es.tiempo.repositories;



import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import hemal.uv.es.tiempo.domain.WeatherData;
import reactor.core.publisher.Mono;


public interface WeatherDataRepository extends ReactiveMongoRepository<WeatherData, String> {
    @Query("{ 'id' : ?0 }")
    Mono<WeatherData> findByDate(String date);
}