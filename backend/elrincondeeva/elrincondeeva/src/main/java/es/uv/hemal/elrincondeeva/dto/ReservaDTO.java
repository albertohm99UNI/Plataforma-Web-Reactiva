package es.uv.hemal.elrincondeeva.dto;

import java.time.LocalDate;

public class ReservaDTO {
    private String nombre;
    private String email;
    private String emailUsuario;
    private String dni;
    private String telefono;
    private LocalDate startDate;
    private LocalDate endDate;
    private int numPersonas;
    private double precio;

    
    public ReservaDTO(String nombre, String email, String emailUsuario, String dni, String telefono, LocalDate startDate, LocalDate endDate, int numPersonas, double precio) {
        this.nombre = nombre;
        this.email = email;
        this.emailUsuario = emailUsuario;
        this.dni = dni;
        this.telefono = telefono;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numPersonas = numPersonas;
        this.precio = precio;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getNumPersonas() {
        return numPersonas;
    }

    public void setNumPersonas(int numPersonas) {
        this.numPersonas = numPersonas;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}
