package es.uv.hemal.elrincondeeva.dto;

import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class MyUserDTO {

    @NotNull(message = "El nombre no puede ser nulo")
    @Size(min = 1, message = "El nombre debe tener al menos 1 caracter")
    private String nombre;

    @NotNull(message = "Los apellidos no pueden ser nulos")
    @Size(min = 1, message = "Los apellidos deben tener al menos 1 caracter")
    private String apellidos;

    @NotNull(message = "El nombre de usuario no puede ser nulo")
    @Size(min = 1, message = "El nombre de usuario debe tener al menos 1 caracter")
    private String username;

    @NotNull(message = "La contraseña no puede ser nula")
    @Size(min = 1, message = "La contraseña debe tener al menos 1 caracter")
    private String password;

    @NotNull(message = "El email no puede ser nulo")
    private String email;

    @NotNull(message = "El teléfono no puede ser nulo")
    @Size(min = 9, max = 9, message = "El teléfono debe tener 9 dígitos")
    private String telefono;

    @NotNull(message = "El DNI no puede ser nulo")
    @Size(min = 9, max = 9, message = "El DNI debe tener 8 números y una letra")
    private String dni;

    @NotNull(message = "La fecha de nacimiento no puede ser nula")
    private LocalDate fechaNacimiento;

    @NotNull(message = "La dirección no puede ser nula")
    @Size(min = 10, message = "La dirección debe tener al menos 10 caracteres")
    private String direccion;

    // Getters y Setters

    public String getUsername() {
        return username;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setAddress(String direccion) {
        this.direccion = direccion;
    }
}
