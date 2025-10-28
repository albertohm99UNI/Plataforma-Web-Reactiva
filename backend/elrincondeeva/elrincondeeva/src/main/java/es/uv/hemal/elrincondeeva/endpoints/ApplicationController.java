package es.uv.hemal.elrincondeeva.endpoints;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import es.uv.hemal.elrincondeeva.domain.Contact;
import es.uv.hemal.elrincondeeva.domain.MyUser;
import es.uv.hemal.elrincondeeva.domain.Review;
import es.uv.hemal.elrincondeeva.domain.Role;
import es.uv.hemal.elrincondeeva.dto.ReviewDTO;
import es.uv.hemal.elrincondeeva.repositories.RoleRepository;
import es.uv.hemal.elrincondeeva.repositories.UserRepository;
import es.uv.hemal.elrincondeeva.services.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("api/v1")

public class ApplicationController {

    private final ReservaService reservaService;

    @Value("${spring.video.path}")
    private String video;
    @Value("${spring.gastronomia.path}")
    private String gastronomia;
    @Autowired
    private StreamService streamService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private UserService userService;
    @Autowired
    private ContactService contactService;

    @Autowired
    private WebClient.Builder webClientBuilder;
    @Value("${media.url}")
    private String mediaUrl;
    
    @Value("${extraccion.url}")
    private String extraccionUrl;
    @Value("${tiempo.url}")
    private String tiempoUrl;

    ApplicationController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }
    @GetMapping(value="video", produces="video/mp4")
    public  Mono<Resource> getVideo() {
        return streamService.getVideo(video);
    }

    @GetMapping(value="gastronomia", produces="video/mp4")
    public  Mono<Resource> getVideoGastronomia() {
        return streamService.getVideo(gastronomia);
    }

    @GetMapping(value="reviews", produces="application/json")
    public  Flux<Review> getReviews() {
        return reviewService.getReviews();
    }
    
    @PostMapping(value="addReview", produces="application/json")
    public Mono<ResponseEntity<Review>> addReview(@RequestBody ReviewDTO reviewDTO) {
        return reviewService.saveReview(reviewDTO)
                .map(savedReview -> ResponseEntity.ok(savedReview))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().build()));
    }

    @GetMapping(value="me", produces="application/json")
    public Mono<ResponseEntity<MyUser>> getUser(@RequestParam String username) {
        return userService.getNombre(username).map(user -> ResponseEntity.ok(user))
        .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().build()));
    }

    @PostMapping("/contact")
public Mono<ResponseEntity<Contact>> sendEmail(@RequestBody Contact c) {
    return contactService.sendEmail(c)
        .map(ResponseEntity::ok)
        .onErrorResume(IllegalArgumentException.class,
            e -> Mono.just(ResponseEntity.badRequest().body(null)));
}


    @GetMapping("/media")
    public Mono<ResponseEntity<String>> getMediaByHashtag(@RequestParam(required = false) String hashtag) {
      
        String url = mediaUrl+"/api/v1/media";
        
      
        if (hashtag != null && !hashtag.isEmpty()) {
            url += "?hashtag=" + hashtag;
        }
    
       
        WebClient webClient = webClientBuilder.baseUrl(url).build();
    
        return webClient.get()
                .retrieve()
                .bodyToMono(String.class) 
                .map(response -> ResponseEntity.ok(response)) 
                .onErrorResume(e -> Mono.just(ResponseEntity.internalServerError().body("Error al obtener los datos")));
    }

    @GetMapping("/entorno")
    public Mono<ResponseEntity<String>> getEntorno() {
      
        String url = extraccionUrl+"/api/v1/extraccion";
       
        WebClient webClient = webClientBuilder.baseUrl(url).build();
    
        return webClient.get()
                .retrieve()
                .bodyToMono(String.class) 
                .map(response -> ResponseEntity.ok(response)) 
                .onErrorResume(e -> Mono.just(ResponseEntity.internalServerError().body("Error al obtener los datos")));
    }
    
    @GetMapping(value="/fiestas",produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<String>> getFiestas() {
      
        String url = extraccionUrl+"/api/v1/extraccionFiestas";
       
        WebClient webClient = webClientBuilder.baseUrl(url).build();
    
        return webClient.get()
                .retrieve()
                .bodyToMono(String.class) 
                .map(response -> ResponseEntity.ok(response)) 
                .onErrorResume(e -> Mono.just(ResponseEntity.internalServerError().body("Error al obtener los datos")));
    }

    

    @GetMapping("/weather")
    public Flux<String> getWeather(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
    
        String url = tiempoUrl+"/api/v1/weather?startDate=" + startDate + "&endDate=" + endDate;
    
        WebClient webClient = webClientBuilder.baseUrl(url).build();
    
        return webClient.get()
                .retrieve()
                .bodyToFlux(String.class); 
    }
    @GetMapping("checkRoles")
    public Flux<Role> checkRoles(@RequestParam String email) {
        return userService.getRoles(email);
            
    }
    
    
    
    @GetMapping("/testCambioRol")
    public void testCambioRol()
    {
        System.out.println("Cambio de roles");
        reservaService.actualizarRolesUsuarios();
    }
    
}
