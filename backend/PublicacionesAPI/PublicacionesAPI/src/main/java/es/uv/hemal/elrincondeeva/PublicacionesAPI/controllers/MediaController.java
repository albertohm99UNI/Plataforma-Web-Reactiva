package es.uv.hemal.elrincondeeva.PublicacionesAPI.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.uv.hemal.elrincondeeva.PublicacionesAPI.domain.Media;
import es.uv.hemal.elrincondeeva.PublicacionesAPI.services.InstagramTokenService;
import es.uv.hemal.elrincondeeva.PublicacionesAPI.services.MediaService;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("api/v1")
public class MediaController {

    @Autowired
    private MediaService mediaService;

    @Autowired
    private InstagramTokenService instagramTokenRefresher;

    @GetMapping("media")
public Flux<Media> getMediaByHashtag(@RequestParam(required = false) String hashtag) {
    if (hashtag != null && !hashtag.isEmpty()) {
        
        return mediaService.extractMediaByHashtag(hashtag);
    } else {
        
        return mediaService.extractAllMedia();
    }
}


    @GetMapping("media/test")
    public void test() {
         mediaService.fetchAndStorePosts();
    }

    @GetMapping("mediaToken/test")
    public void testToken() {
         instagramTokenRefresher.refreshToken();
    }
}
