package es.uv.hemal.elrincondeeva.PublicacionesAPI.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


import es.uv.hemal.elrincondeeva.PublicacionesAPI.domain.Media;
import es.uv.hemal.elrincondeeva.PublicacionesAPI.dto.DataDTO;
import es.uv.hemal.elrincondeeva.PublicacionesAPI.dto.MediaDTO;
import es.uv.hemal.elrincondeeva.PublicacionesAPI.repositories.MediaRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MediaService {

    @Autowired
    private MediaRepository mediaRepository;
    @Autowired
    private InstagramTokenService instagramTokenRefresher;
    
    private String accessToken = "";
    @Value("${instagram.user-id}")
    private  String userId = "YOUR_USER_ID";  

    @Scheduled(cron = "0 0 0 * * ?")
public void fetchAndStorePosts() {
    WebClient webClient = WebClient.create("https://graph.instagram.com");
    try {
        accessToken = instagramTokenRefresher.getToken();
    } catch (IOException e) {
        System.out.println("Error al leer el token de acceso: " + e.getMessage());
    } 
     webClient.get()
            .uri(uriBuilder -> uriBuilder
                    .path("/"+userId+"/media")
                    .queryParam("fields", "id,caption,media_type,media_url,timestamp")
                    .queryParam("access_token", accessToken)
                    .build())
            .retrieve()
            
            .bodyToMono(DataDTO.class) 
            .doOnNext(response -> {
            
                System.out.println("Respuesta cruda: " + response);
            })
            .flatMapMany(dataDTO -> Flux.fromIterable(dataDTO.getData())) 
            .concatMap(this::processAndStorePost)
            .doOnTerminate(() -> System.out.println("Proceso de actualización de posts finalizado"))
            .doOnError(e -> System.err.println("Error al actualizar posts: " + e.getMessage()))
            .subscribe();
}

public void fetchAndStorePostsById(Long id) {
    WebClient webClient = WebClient.create("https://graph.instagram.com");
    try {
        accessToken = instagramTokenRefresher.getToken();
    } catch (IOException e) {
        System.out.println("Error al leer el token de acceso: " + e.getMessage());
    } 
    webClient.get()
            .uri(uriBuilder -> uriBuilder
                    .path("/" + userId + "/media")
                    .queryParam("fields", "id,caption,media_type,media_url,timestamp")
                    .queryParam("access_token", accessToken)
                    .build())
            .retrieve()
            .bodyToMono(DataDTO.class)
            .doOnNext(response -> System.out.println("Respuesta cruda: " + response))
            .flatMapMany(dataDTO -> Flux.fromIterable(dataDTO.getData()))
            .filter(post -> post.getId().equals(id)) 
            .flatMap(this::updateMediaUrlById) 
            .doOnTerminate(() -> System.out.println("Proceso de actualización de posts finalizado"))
            .doOnError(e -> System.err.println("Error al actualizar posts: " + e.getMessage()))
            .subscribe();
}

private Mono<Media> updateMediaUrlById(MediaDTO postDTO) {
    return mediaRepository.findById(postDTO.getId()) 
            .flatMap(media -> {
                media.setMediaurl(postDTO.getMediaUrl()); 
                media.setTimestamp(postDTO.getTimestamp());
                System.out.println("Actualizando URL de la media: " + postDTO.getMediaUrl());
                return mediaRepository.save(media); 
            })
            .switchIfEmpty(Mono.error(new RuntimeException("Post no encontrado con id: " + postDTO.getId()))); 
}

private Mono<Void> processAndStorePost(MediaDTO post) {
    return mediaRepository.existsById(post.getId())
        .flatMap(exists -> {
            if (!exists) {
                List<String> hashtags = extractHashtags(post.getCaption());
                return savePostWithHashtags(post, hashtags);
            } else {
                return mediaRepository.findById(post.getId())
                    .flatMap(media -> {
                        media.setMediaurl(post.getMediaUrl()); 
                        media.setTimestamp(post.getTimestamp());
                        
                        return mediaRepository.save(media);
                    })
                    .then(); 
            }
        });
}


    private List<String> extractHashtags(String caption) {
        if (caption == null || caption.isEmpty()) {
            return Collections.emptyList();
        }
        Pattern pattern = Pattern.compile("#(\\w+)");
        Matcher matcher = pattern.matcher(caption);
        List<String> hashtags = new ArrayList<>();
        while (matcher.find()) {
            hashtags.add(matcher.group(1));
        }
        return hashtags;
    }

    private Mono<Void> savePostWithHashtags(MediaDTO post, List<String> hashtags) {
        if (hashtags.isEmpty()) {
            return Mono.empty(); 
        }
    
        return Flux.fromIterable(hashtags)
                .flatMap(hashtag -> {
                    Media media = new Media();
                    System.out.println(post.getMediaType()+" "+post.getMediaUrl());
                    media.setId(post.getId()); 
                    media.setMediaurl(post.getMediaUrl());
                    media.setTimestamp(post.getTimestamp());
                    media.setCaption(post.getCaption());
                    media.setMediatype(post.getMediaType());
                    media.setHashtag(hashtag);
    
                    return mediaRepository.save(media);
                })
                .then(); 
    }

    public Flux<Media> extractMediaByHashtag(String hashtag) {
        
        return mediaRepository.findByHashtag(hashtag);
    }

    public Flux<Media> extractAllMedia() {
       return mediaRepository.findAll();
    }
    
  

   
}
