package es.uv.hemal.elrincondeeva.dto;

import java.time.LocalDateTime;

public class ReviewDTO {
     private String review;
    private int rate;
    private int rateServicios;
    private int rateLimpieza;
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

    private LocalDateTime creationDate;

    // Getters y Setters
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

}
