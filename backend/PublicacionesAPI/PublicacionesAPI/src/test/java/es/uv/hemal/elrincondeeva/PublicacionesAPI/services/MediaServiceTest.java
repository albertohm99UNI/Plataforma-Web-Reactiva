package es.uv.hemal.elrincondeeva.PublicacionesAPI.services;

import static org.mockito.Mockito.*;
import es.uv.hemal.elrincondeeva.PublicacionesAPI.domain.Media;
import es.uv.hemal.elrincondeeva.PublicacionesAPI.repositories.MediaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class MediaServiceTest {

    @InjectMocks
    private MediaService mediaService;

    @Mock
    private MediaRepository mediaRepository;

    @Mock
    private InstagramTokenService instagramTokenRefresher;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(mediaService, "userId", "dummyUser");
    }


    @Test
    void testExtractMediaByHashtag() {
        Media media1 = new Media();
        media1.setId(
            "1");
        media1.setHashtag("playa");

        when(mediaRepository.findByHashtag("playa")).thenReturn(Flux.just(media1));

        StepVerifier.create(mediaService.extractMediaByHashtag("playa"))
                .expectNext(media1)
                .verifyComplete();
    }

    @Test
    void testExtractAllMedia() {
        Media media = new Media();
        media.setId("2");

        when(mediaRepository.findAll()).thenReturn(Flux.just(media));

        StepVerifier.create(mediaService.extractAllMedia())
                .expectNext(media)
                .verifyComplete();
    }

}
