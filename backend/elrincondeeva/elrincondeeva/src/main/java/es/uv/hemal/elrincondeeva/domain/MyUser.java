package es.uv.hemal.elrincondeeva.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

import java.util.List;

@Table("users")



public class MyUser {
    @Id
    private Integer id;

    @Column("nombre")
    @NotNull(message = "El nombre no puede ser nulo")
    @Size(min = 1, message = "El nombre debe tener al menos 1 caracter")
    private String nombre;

   

    @Column("password")
    @NotNull(message = "La contraseña no puede ser nula")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    @Column("apellidos")
    @NotNull(message = "Los apellidos no pueden ser nulos")
    @Size(min = 1, message = "Los apellidos deben tener al menos 1 caracter")
    private String apellidos;

    @Column("email")
    @NotNull(message = "El email no puede ser nulo")
    @Email(message = "El email debe ser válido")
    private String email;

    @Column("telefono")
    @NotNull(message = "El teléfono no puede ser nulo")
    @Pattern(regexp = "^[0-9]{9}$", message = "El teléfono debe tener 9 dígitos")
    private String telefono;

    @Column("dni")
    @NotNull(message = "El DNI no puede ser nulo")
    @Pattern(regexp = "^\\d{8}[A-Za-z]$", message = "El DNI debe tener 8 dígitos seguidos de una letra")
    private String dni;

    @Column("fecha_nacimiento")
    @NotNull(message = "La fecha de nacimiento no puede ser nula")
    private LocalDate fechaNacimiento;

    @Column("direccion")
    @NotNull(message = "La dirección no puede ser nula")
    @Size(min = 10, message = "La dirección debe tener al menos 10 caracteres")
    private String direccion;
	@Column("estado")
    @NotNull(message = "El estado no puede ser nula")
    @Size(min = 10, message = "La dirección debe tener al menos 10 caracteres")
	private Boolean estado;
	@Column("fecha_registro")
	@NotNull(message = "La fecha de registro no puede ser nula")
	private LocalDate fechaRegistro;
	@Column("fecha_modificacion")
	@NotNull(message = "La fecha de modificación no puede ser nula")
	private LocalDate fechaModificacion;
	@Column("fecha_baja")
	private LocalDate fechaBaja;

    @Transient
    private List<Role> roles;

	public MyUser() {
	}
	public MyUser(
			@NotNull(message = "El nombre no puede ser nulo") @Size(min = 1, message = "El nombre debe tener al menos 1 caracter") String nombre,
			@NotNull(message = "El nombre de usuario no puede ser nulo") @Size(min = 4, message = "El nombre de usuario debe tener al menos 4 caracteres") String username,
			@NotNull(message = "La contraseña no puede ser nula") @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres") String password,
			@NotNull(message = "Los apellidos no pueden ser nulos") @Size(min = 1, message = "Los apellidos deben tener al menos 1 caracter") String apellidos,
			@NotNull(message = "El email no puede ser nulo") @Email(message = "El email debe ser válido") String email,
			@NotNull(message = "El teléfono no puede ser nulo") @Pattern(regexp = "^[0-9]{9}$", message = "El teléfono debe tener 9 dígitos") String telefono,
			@NotNull(message = "El DNI no puede ser nulo") @Pattern(regexp = "^\\d{8}[A-Za-z]$", message = "El DNI debe tener 8 dígitos seguidos de una letra") String dni,
			@NotNull(message = "La fecha de nacimiento no puede ser nula") LocalDate fechaNacimiento,
			@NotNull(message = "La dirección no puede ser nula") @Size(min = 10, message = "La dirección debe tener al menos 10 caracteres") String direccion,
			@NotNull(message = "El estado no puede ser nula") @Size(min = 10, message = "La dirección debe tener al menos 10 caracteres") Boolean estado,
			@NotNull(message = "La fecha de registro no puede ser nula") LocalDate fechaRegistro,
			@NotNull(message = "La fecha de modificación no puede ser nula") LocalDate fechaModificacion
	) {
		this.nombre = nombre;
		this.password = password;
		this.apellidos = apellidos;
		this.email = email;
		this.telefono = telefono;
		this.dni = dni;
		this.fechaNacimiento = fechaNacimiento;
		this.direccion = direccion;
		this.estado = estado;
		this.fechaRegistro = fechaRegistro;
		this.fechaModificacion = fechaModificacion;
		this.fechaBaja = fechaBaja;
		this.roles = roles;
		
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getApellidos() {
		return apellidos;
	}
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
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
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public Boolean getEstado() {
		return estado;
	}
	public void setEstado(Boolean estado) {
		this.estado = estado;
	}
	public LocalDate getFechaRegistro() {
		return fechaRegistro;
	}
	public void setFechaRegistro(LocalDate fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	public LocalDate getFechaModificacion() {
		return fechaModificacion;
	}
	public void setFechaModificacion(LocalDate fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	public LocalDate getFechaBaja() {
		return fechaBaja;
	}
	public void setFechaBaja(LocalDate fechaBaja) {
		this.fechaBaja = fechaBaja;
	}
	public List<Role> getRoles() {
		return roles;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	

  
	
}
