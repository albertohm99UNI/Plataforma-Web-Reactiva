package es.uv.hemal.elrincondeeva.config;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.BadSqlGrammarException;
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

import es.uv.hemal.elrincondeeva.domain.MyUser;
import es.uv.hemal.elrincondeeva.dto.MyUserDTO;
import es.uv.hemal.elrincondeeva.repositories.ReservaRepository;
import es.uv.hemal.elrincondeeva.repositories.ReviewRepository;
import es.uv.hemal.elrincondeeva.repositories.RoleRepository;
import es.uv.hemal.elrincondeeva.repositories.RoleUserRepository;
import es.uv.hemal.elrincondeeva.repositories.UserRepository;
import es.uv.hemal.elrincondeeva.services.SignupService;
import io.r2dbc.spi.ConnectionFactory;


@Configuration
public class TestDatabaseConfiguration {
    @Autowired
    UserRepository userRepository;
    @Value("${admin.password}")
    private String adminPassword;
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    RoleUserRepository roleUserRepository;
    @Autowired
    SignupService signupService;
    @Autowired
    ReservaRepository reservaRepository;
    @Autowired
    RoleRepository roleRepository;
    @Bean
     ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);
        CompositeDatabasePopulator populator = new CompositeDatabasePopulator();
        populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")) );
        try {
            if(roleRepository.count().block() >0 ) {
                System.out.println("Tablas ya rellenadas"); }
            else
            {
                populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("data.sql")) );
            }
           
        } catch (BadSqlGrammarException e) {
            populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("data.sql")),new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));
        
        }
        initializer.setDatabasePopulator(populator);
        return initializer;
    }


@EventListener(ApplicationReadyEvent.class)
public void initAfterSchemaCreated() {
      MyUserDTO adminUser = new MyUserDTO();
        adminUser.setNombre("admin");
        adminUser.setApellidos("admin");
        adminUser.setEmail("casarural.elrincondeeva@gmail.com");
        adminUser.setPassword(adminPassword);
        adminUser.setTelefono("123456789");
        adminUser.setDni("12345678A");
        adminUser.setFechaNacimiento(LocalDate.of(1950, 1, 1));
        adminUser.setDireccion("Calle Admin 123");

    
    if (signupService.isAdminRegistered(adminUser).block()) {
            System.out.println("Usuario Admin ya registrado");
         
        
    
       
    } else {
       signupService.registerAdmin(adminUser)
            .doOnSuccess(user -> System.out.println("Usuario Admin creado correctamente"))
            .doOnError(error -> System.out.println("Error al crear el usuario Admin: " + error.getMessage()))
            .subscribe();  
    }
}
}