package es.uv.hemal.elrincondeeva.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.uv.hemal.elrincondeeva.domain.MyUser;
import es.uv.hemal.elrincondeeva.domain.UserRole;
import es.uv.hemal.elrincondeeva.dto.MyUserDTO;
import es.uv.hemal.elrincondeeva.repositories.RoleUserRepository;
import es.uv.hemal.elrincondeeva.repositories.UserRepository;
import reactor.core.publisher.Mono;

@Service
public class SignupService {

    private final UserRepository userRepository;
   private final RoleUserRepository roleUserRepository;
    private final PasswordEncoder passwordEncoder;

 
    public SignupService(UserRepository userRepository, RoleUserRepository roleUserRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleUserRepository = roleUserRepository;
    }


    @Transactional
    public Mono<Object> registerUser(MyUserDTO request) {
        
        return userRepository.findByEmail(request.getEmail())
            .flatMap(existingUser -> Mono.error(new Exception("El email de usuario "+existingUser.getEmail() +" ya está en uso.")))
            .switchIfEmpty(Mono.defer(() -> {
                
                MyUser newUser = new MyUser();
               
                newUser.setPassword(passwordEncoder.encode(request.getPassword()));
                newUser.setNombre(request.getNombre());
                newUser.setApellidos(request.getApellidos());
                newUser.setEmail(request.getEmail());
                newUser.setTelefono(request.getTelefono());
                newUser.setDni(request.getDni());
                newUser.setFechaNacimiento(request.getFechaNacimiento());
                newUser.setDireccion(request.getDireccion());
                newUser.setEstado(true);
                newUser.setFechaRegistro(java.time.LocalDate.now());
                newUser.setFechaModificacion(java.time.LocalDate.now());
    
              
                return userRepository.save(newUser)
                    .flatMap(savedUser -> 
                       
                        userRepository.findFirstByName("ROLE_USER")
                            .flatMap(role -> {
                                UserRole roleAssign = new UserRole();
                                roleAssign.setUserId(savedUser.getId());  
                                roleAssign.setRoleId(role.getId());  
                        
                                return roleUserRepository.save(roleAssign)
                                    .then(Mono.just(savedUser));
                            })
                    );
            }));
    }

    @Transactional
    public Mono<Boolean> isAdminRegistered(MyUserDTO request) {
        return userRepository.findByEmail(request.getEmail())
            .flatMap(existingUser -> Mono.just(true))
            .switchIfEmpty(Mono.just(false));
    }
    @Transactional
    public Mono<Object> registerAdmin(MyUserDTO request) {
        
        return userRepository.findByEmail(request.getEmail())
            .flatMap(existingUser -> Mono.error(new Exception("El email de admin "+existingUser.getEmail() +" ya está en uso.")))
            .switchIfEmpty(Mono.defer(() -> {
                
                MyUser newUser = new MyUser();
               
                newUser.setPassword(passwordEncoder.encode(request.getPassword()));
                newUser.setNombre(request.getNombre());
                newUser.setApellidos(request.getApellidos());
                newUser.setEmail(request.getEmail());
                newUser.setTelefono(request.getTelefono());
                newUser.setDni(request.getDni());
                newUser.setFechaNacimiento(request.getFechaNacimiento());
                newUser.setDireccion(request.getDireccion());
                newUser.setEstado(true);
                newUser.setFechaRegistro(java.time.LocalDate.now());
                newUser.setFechaModificacion(java.time.LocalDate.now());
    
              
                return userRepository.save(newUser)
                    .flatMap(savedUser -> 
                       
                        userRepository.findFirstByName("ROLE_ADMIN")
                            .flatMap(role -> {
                                UserRole roleAssign = new UserRole();
                                roleAssign.setUserId(savedUser.getId());  
                                roleAssign.setRoleId(role.getId());  
                        
                                return roleUserRepository.save(roleAssign)
                                    .then(Mono.just(savedUser));
                            })
                    );
            }));
    }
    
    
    
}
