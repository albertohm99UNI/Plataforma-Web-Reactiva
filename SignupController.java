package es.uv.hemal.elrincondeeva.endpoints;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.uv.hemal.elrincondeeva.dto.MyUserDTO;
import es.uv.hemal.elrincondeeva.services.SignupService;
import reactor.core.publisher.Mono;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("api/v1/signup")
public class SignupController {

    @Autowired
    private SignupService signupService;

     @PostMapping("")
    public Mono<ResponseEntity<Map<String, String>>> registro(@RequestBody MyUserDTO entity) {
        System.out.println("Registro de usuario");

        return signupService.registerUser(entity)
            .map(user -> {
               
                Map<String, String> response = new HashMap<>();
                response.put("mensaje", "Usuario registrado exitosamente: " + user.toString());
                return ResponseEntity.ok(response);  
            })
            .onErrorResume(e -> {
               
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Error al registrar usuario: " + e.getMessage());
                return Mono.just(ResponseEntity.badRequest().body(errorResponse));
            });
            
    }
}
