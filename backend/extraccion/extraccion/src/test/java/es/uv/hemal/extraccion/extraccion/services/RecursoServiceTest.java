package es.uv.hemal.extraccion.extraccion.services;

import es.uv.hemal.extraccion.extraccion.models.Recurso;
import es.uv.hemal.extraccion.extraccion.repositories.RecursoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class RecursoServiceTest {

    @InjectMocks
    private RecursoService recursoService;

    @Mock
    private ScraperService scraperService;

    @Mock
    private RecursoRepository recursoRepository;

    private final String urlChelva = "https://chelva.test/";
    private final String urlTuejar = "https://tuejar.test/";
    private final String urlAytuejar = "https://aytuejar.test/";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(recursoService, "urlChelva", urlChelva);
        ReflectionTestUtils.setField(recursoService, "urlTuejar", urlTuejar);
        ReflectionTestUtils.setField(recursoService, "urlayunta", urlAytuejar);
    }

    @Test
    void testFetchAndStoreData() {
        when(scraperService.scrape(anyString(), anyString(), anyString()))
                .thenReturn(Mono.empty());

        when(recursoRepository.findAll()).thenReturn(Flux.empty());

        recursoService.fetchandstoreData();

        verify(scraperService).scrape(urlChelva + "monumentos", "monumentos", "chelva");
        verify(scraperService).scrape(urlChelva + "aldeas", "aldeas", "chelva");
        verify(scraperService).scrape(urlTuejar + "patrimonio", "patrimonio", "tuejar");
        verify(scraperService).scrape(urlTuejar + "naturaleza", "naturaleza", "tuejar");
        verify(scraperService).scrape(urlAytuejar + "agenda-festividades", "fiestas", "tuejar");

        verify(recursoRepository).findAll();
    }

    @Test
    void testExtractAllResources() {
        Recurso recurso = new Recurso();
        when(recursoRepository.findAll()).thenReturn(Flux.just(recurso));

        recursoService.extractAllResources().collectList().block();

        verify(recursoRepository).findAll();
    }

    @Test
    void testExtractFiestas() {
        Recurso recurso = new Recurso();
        when(recursoRepository.findByCategoria("fiestas")).thenReturn(Flux.just(recurso));

        recursoService.extractFiestas().collectList().block();

        verify(recursoRepository).findByCategoria("fiestas");
    }
}
