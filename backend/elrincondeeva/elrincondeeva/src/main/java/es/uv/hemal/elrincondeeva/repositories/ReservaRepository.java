package es.uv.hemal.elrincondeeva.repositories;


import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.cglib.core.Local;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import org.springframework.stereotype.Repository;

import es.uv.hemal.elrincondeeva.domain.Reserva;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



@Repository
public interface ReservaRepository extends R2dbcRepository<Reserva, Integer> {

   @Query("SELECT id FROM reservas")
    Flux<Integer> findAllId();

    Flux<Reserva> findByEmail(String email);
    @SuppressWarnings("null")
    Mono<Reserva> findById(Integer id);

    Flux<Reserva> findAllByOrderByFechaInicioDesc();
    Flux<Reserva> findByFechaInicioGreaterThanEqual(LocalDate fechaInicio);
    Flux<Reserva> findByEstadoAndFechaInicioLessThanEqual(Reserva.EstadoReserva estado, LocalDate fecha);

    @Query("""
    SELECT CASE
        WHEN COUNT(r.*) > 0 THEN TRUE
        ELSE FALSE
    END
    FROM reservas r
    WHERE (:startDate BETWEEN r.fecha_inicio AND r.fecha_fin)
       OR (:endDate BETWEEN r.fecha_inicio AND r.fecha_fin)
       OR (r.fecha_inicio BETWEEN :startDate AND :endDate)
""")
Mono<Boolean> existeReservaEnFechas(LocalDate startDate, LocalDate endDate);


}
