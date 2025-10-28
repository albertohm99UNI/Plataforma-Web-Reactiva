package es.uv.hemal.elrincondeeva.domain;


import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;



@Table("reservas")
public class Reserva {

    @Id
    private Integer id;

    @Column("nombre")
    private String nombre;
    @Column("email")
    private String email;
    @Column("dni")
    private String dni;
    @Column("telefono")
    private String telefono;

    @Column("fecha_inicio")
    private LocalDate fechaInicio;

    @Column("fecha_fin")
    private LocalDate fechaFin;

    @Column("num_personas")
    private int numPersonas;

    @Column("precio_total")
    private Double precioTotal;

    @Column("estado")
    private EstadoReserva estado;

    @Column("usuario_id")
    private Integer usuarioId;

    // Constructor, Getters y Setters

    public Reserva() {}


    public Reserva(Integer id, String nombre, String email, String dni, String telefono, LocalDate fechaInicio,
            LocalDate fechaFin, int numPersonas, Double precioTotal, EstadoReserva estado, Integer usuarioId) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.dni = dni;
        this.telefono = telefono;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.numPersonas = numPersonas;
        this.precioTotal = precioTotal;
        this.estado = estado;
        this.usuarioId = usuarioId;
    }


    public String getNombre() {
        return nombre;
    }


    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public String getDni() {
        return dni;
    }


    public void setDni(String dni) {
        this.dni = dni;
    }


    public String getTelefono() {
        return telefono;
    }


    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public int getNumPersonas() {
        return numPersonas;
    }

    public void setNumPersonas(int numPersonas) {
        this.numPersonas = numPersonas;
    }

    public Double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(Double precioTotal) {
        this.precioTotal = precioTotal;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public void setEstado(EstadoReserva estado) {
        this.estado = estado;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    
    public enum EstadoReserva {
        CONFIRMADA,
        PAGADA,
        CANCELADA,
        FINALIZADA
    }
}
