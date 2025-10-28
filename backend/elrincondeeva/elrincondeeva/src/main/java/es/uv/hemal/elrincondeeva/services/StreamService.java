package es.uv.hemal.elrincondeeva.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;
@Service
public class StreamService {
@Autowired
private ResourceLoader resourceLoader;

public Mono<Resource> getVideo(String video) {
    return Mono.fromSupplier(() -> resourceLoader.getResource("classpath:" + video));
}
public Mono<Resource> getVideoGastronomia(String video) {
    return Mono.fromSupplier(() -> resourceLoader.getResource("classpath:" + video));
}
}
