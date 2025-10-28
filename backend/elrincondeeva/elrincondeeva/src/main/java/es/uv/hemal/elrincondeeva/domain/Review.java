package es.uv.hemal.elrincondeeva.domain;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("reviews")
public class Review {

    @Id
    private Long id;

    @Column
    @NotNull(message = "El nombre no puede ser nulo")
    private String review;

    @Column()
    @NotNull(message = "La puntuación no puede ser nulo")
    private int rate;

    @Column("rate_servicios")
    @NotNull(message = "La puntuación no puede ser nulo")
    private int rateServicios;
    @Column("rate_limpieza")
    @NotNull(message = "La puntuación no puede ser nulo")
    private int rateLimpieza;
    @Column("rate_ubicacion")
    @NotNull(message = "La puntuación no puede ser nulo")
    private int rateUbicacion;



    public int getRateServicios() {
        return rateServicios;
    }

    public void setRateServicios(int rateServicios) {
        this.rateServicios = rateServicios;
    }

    public int getRateLimpieza() {
        return rateLimpieza;
    }

    public void setRateLimpieza(int rateLimpieza) {
        this.rateLimpieza = rateLimpieza;
    }

    public int getRateUbicacion() {
        return rateUbicacion;
    }

    public void setRateUbicacion(int rateUbicacion) {
        this.rateUbicacion = rateUbicacion;
    }

    @Column("creation_date")
    @NotNull(message = "La fecha de creación no puede ser nulo")
    private LocalDateTime creationDate;

    @Column("user_id")
    @NotNull(message = "El usuario no puede ser nulo")
    private Integer userId;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}
