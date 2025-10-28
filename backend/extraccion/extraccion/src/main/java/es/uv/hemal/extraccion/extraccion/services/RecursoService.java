
package es.uv.hemal.extraccion.extraccion.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


import reactor.core.publisher.Flux;
import es.uv.hemal.extraccion.extraccion.models.Recurso;
import es.uv.hemal.extraccion.extraccion.repositories.RecursoRepository;
@Service
public class RecursoService {
    @Autowired
    private ScraperService scraperService;
     @Value("${url.tuejar}")
    private String urlTuejar;
    @Value("${url.chelva}")
    private String urlChelva;
    @Value("${url.aytuejar}")
    private String urlayunta;
    @Autowired
    private RecursoRepository sRepository;
    @Scheduled(cron = "0 0 0 * * ?")
    public void fetchandstoreData() {
      
        scraperService.scrape(urlChelva + "monumentos", "monumentos", "chelva").subscribe(
            null, 
            error -> System.err.println("Error en el scraper para Chelva Monumentos: " + error.getMessage()), 
            () -> System.out.println("Scraping de Chelva Monumentos completado")
        );
        scraperService.scrape(urlChelva + "aldeas", "aldeas", "chelva").subscribe(
            null, 
            error -> System.err.println("Error en el scraper para Chelva Aldeas: " + error.getMessage()), 
            () -> System.out.println("Scraping de Chelva Aldeas completado")
        );
    
        scraperService.scrape(urlTuejar + "patrimonio", "patrimonio", "tuejar").subscribe(
            null, 
            error -> System.err.println("Error en el scraper para Tuejar Patrimonio: " + error.getMessage()), 
            () -> System.out.println("Scraping de Tuejar Patrimonio completado")
        );
    
        scraperService.scrape(urlTuejar + "naturaleza", "naturaleza", "tuejar").subscribe(
            null, 
            error -> System.err.println("Error en el scraper para Tuejar Naturaleza: " + error.getMessage()), 
            () -> System.out.println("Scraping de Tuejar Naturaleza completado")
        );

        scraperService.scrape(urlayunta + "agenda-festividades", "fiestas", "tuejar").subscribe(
            null, 
            error -> System.err.println("Error en el scraper para Tuejar Fiestas: " + error.getMessage()), 
            () -> System.out.println("Scraping de Tuejar Fiestas completado:"+urlayunta + "agenda-festividades")
        );
    
       
        sRepository.findAll().subscribe(System.out::println);
    }
    


    public Flux<Recurso> extractAllResources() {
       return sRepository.findAll();
    }

    public Flux<Recurso> extractFiestas() {
        return sRepository.findByCategoria("fiestas");
     }
    
    
}
