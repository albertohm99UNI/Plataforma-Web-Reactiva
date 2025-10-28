package es.uv.hemal.elrincondeeva.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Service;

import es.uv.hemal.elrincondeeva.domain.Review;
import es.uv.hemal.elrincondeeva.dto.ReviewDTO;
import es.uv.hemal.elrincondeeva.repositories.ReviewRepository;
import es.uv.hemal.elrincondeeva.repositories.RoleRepository;
import es.uv.hemal.elrincondeeva.repositories.RoleUserRepository;
import es.uv.hemal.elrincondeeva.repositories.UserRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Service
public class ReviewService {
    @Autowired ReviewRepository reviewRepository;
    @Autowired RoleUserRepository roleUserRepository;
    @Autowired RoleRepository roleRepository;
    @Autowired UserRepository  userRepository;
    public Mono<Review> saveReview(ReviewDTO reviewDTO) {
        System.out.println("ReviewDTO: " + reviewDTO.getRate());
    
        return ReactiveSecurityContextHolder.getContext()
            .flatMap(ctx -> {
                Authentication authentication = ctx.getAuthentication();
                String userId = (String) authentication.getPrincipal();
                System.out.println("Principal: " + userId);
                return Mono.just(userId);
            })
            .flatMap(userId -> 
                userRepository.findByEmail(userId)
                    .switchIfEmpty(Mono.error(new RuntimeException("User not found")))
            )
            .flatMap(user -> {
                Review review = new Review();
                review.setReview(reviewDTO.getReview());
                review.setRate(reviewDTO.getRate());
                review.setRateLimpieza(reviewDTO.getRateLimpieza());
                review.setRateServicios(reviewDTO.getRateServicios());
                review.setRateUbicacion(reviewDTO.getRateUbicacion());
                review.setUserId(user.getId());
                review.setCreationDate(LocalDateTime.now());
                return roleRepository.findByName("ROLE_CLIENTE")
                    .flatMap(role -> {
                        return roleUserRepository.deleteByUserIdAndRoleId(user.getId(), role.getId())
                            .then(reviewRepository.save(review));
                    });
                
            });
    }
     public Flux<Review> getReviews() {
        return reviewRepository.findAllByOrderByCreationDateDesc();
    }
}
