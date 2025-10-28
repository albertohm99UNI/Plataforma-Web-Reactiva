package es.uv.hemal.extraccion.extraccion.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.uv.hemal.extraccion.extraccion.models.Recurso;

import es.uv.hemal.extraccion.extraccion.services.RecursoService;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("api/v1")
public class ExtraccionController {


    @Autowired
    private RecursoService imgService;

   

    @GetMapping("extraccion")
    public Flux<Recurso> getResources( ) {
        return imgService.extractAllResources();
     
     
    }

    @GetMapping("extraccionFiestas")
    public Flux<Recurso> getFiestas( ) {
        return imgService.extractFiestas();
     
     
    }

    @GetMapping("extraccion/test")
    public void test() {
            imgService.fetchandstoreData();
    }
}
