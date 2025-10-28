package es.uv.hemal.extraccion.extraccion.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uv.hemal.extraccion.extraccion.models.Recurso;
import es.uv.hemal.extraccion.extraccion.repositories.RecursoRepository;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ScraperService {

    @Autowired
    private RecursoRepository recursoRepository;

    @Autowired
    private ReactiveMongoTemplate mongoTemplate;

    public Mono<Long> scrape(String baseUrl, String tipo, String localizacion) {
        System.out.println("Raspando: " + baseUrl + " para tipo: " + tipo + ", localización: " + localizacion);
        
        return ensureCollectionExists("recursos")
                .then(Mono.fromCallable(() -> Jsoup.connect(baseUrl).get()))
                .flatMapMany(document -> processByTypeAndLocation(document, tipo, localizacion))
                .flatMap(recurso -> recursoRepository.save(recurso)
                        .doOnSuccess(saved -> System.out.println("Guardado en MongoDB: " + saved))
                        .doOnError(error -> System.err.println("Error al guardar: " + error.getMessage())))
                .count() 
                .doOnNext(count -> System.out.println("Total de recursos procesados: " + count))
                .doOnError(error -> System.err.println("Error en el proceso de scraping: " + error.getMessage()));
    }
    

    private Mono<Void> ensureCollectionExists(String collectionName) {
        return mongoTemplate.collectionExists(collectionName)
                .flatMap(exists -> exists
                        ? Mono.empty()
                        : mongoTemplate.createCollection(collectionName).then())
                .doOnNext(unused -> System.out.println("Colección asegurada: " + collectionName));
    }

    private Flux<Recurso> processByTypeAndLocation(Document document, String tipo, String localizacion) {
        if (("monumentos".equals(tipo)|| "aldeas".equals(tipo)) && "chelva".equals(localizacion)) {
            return processChelvaMonumentosAldeas(document, tipo, localizacion);
        } else if (("patrimonio".equals(tipo) || "naturaleza".equals(tipo)) && "tuejar".equals(localizacion)) {
            return processTuejarPatrimonioNaturaleza(document, tipo, localizacion);
        }else if (("fiestas".equals(tipo)) && "tuejar".equals(localizacion)) {
            return processTuejarFiestas(document, tipo, localizacion);
        }
        System.out.println("Sin coincidencia para tipo: " + tipo + ", localización: " + localizacion);
        return Flux.empty();
    }

    private Flux<Recurso> processChelvaMonumentosAldeas(Document document, String tipo, String localizacion) {
        return Flux.fromIterable(document.select("div.qodef-tours-destination-item-holder a"))
                .map(imgLink -> {
                    String pageUrl = imgLink.attr("href");
                    String imageUrl = imgLink.select("div.qodef-tours-destination-item-image img").attr("src");
                    String title = imgLink.select("h3.qodef-tours-destination-item-title").text();

                    if (pageUrl.isEmpty()) {
                        System.err.println("URL de la página vacía: " + imgLink);
                        return null;
                    }
                    return new Recurso(title, imageUrl, pageUrl, "", tipo, localizacion);
                })
                .filter(recurso -> recurso != null)
                .doOnNext(recurso -> System.out.println("Procesado recurso: " + recurso));
    }

    private Flux<Recurso> processTuejarPatrimonioNaturaleza(Document document, String tipo, String localizacion) {
        return Flux.fromIterable(document.select("a[href] img[src]"))
                .filter(imgLink -> imgLink.absUrl("src").contains(".jpg"))
                .flatMap(imgLink -> {
                    String imageUrl = imgLink.absUrl("src");
                    String imageAlt = imgLink.attr("alt");
                    String linkUrl = imgLink.parent().absUrl("href");

                    if (imageUrl.isEmpty() || imageAlt.isEmpty() || linkUrl.isEmpty()) {
                        System.err.println("Datos faltantes: img=" + imageUrl + ", alt=" + imageAlt + ", link=" + linkUrl);
                        return Mono.empty();
                    }

                    return fetchLinkedPage(linkUrl)
                            .map(description -> new Recurso(imageAlt, imageUrl, linkUrl, description, tipo, localizacion))
                            .defaultIfEmpty(new Recurso(imageAlt, imageUrl, linkUrl, "", tipo, localizacion));
                })
                .doOnNext(recurso -> System.out.println("Procesado recurso: " + recurso));
    }
    private Flux<Recurso> processTuejarFiestas(Document document, String tipo, String localizacion) {
        return Flux.fromIterable(document.select(".image-style-imagen-120-x-120"))
              
                .flatMap(imgLink -> {
                    String imageUrl = imgLink.parent().absUrl("href");
                    String url = imgLink.parent().absUrl("href");
                    String linkUrl = imgLink.parent().absUrl("href");
                  
                    
                    String[] parts = url.split("/");
                        
                    String imageAlt  = parts[parts.length - 1];
                     
                    

                    

                    if (imageUrl.isEmpty() ||  linkUrl.isEmpty()) {
                        System.err.println("Datos faltantes: img=" + imageUrl + ", alt=" + imageAlt + ", link=" + linkUrl);
                        return Mono.empty();
                    }

                    return fetchLinkedPage(linkUrl)
                            .map(description -> new Recurso(imageAlt, imageUrl, linkUrl, description, tipo, localizacion))
                            .defaultIfEmpty(new Recurso(imageAlt, imageUrl, linkUrl, "", tipo, localizacion));
                })
                .doOnNext(recurso -> System.out.println("Procesado recurso: " + recurso));
    }
   

    private Mono<String> fetchLinkedPage(String linkUrl) {
        return Mono.fromCallable(() -> Jsoup.connect(linkUrl).get())
                .map(linkedPage -> {
                    Elements titleElements = linkedPage.selectXpath("//*[@id='cs-content']/div[1]/div/div/div[1]/div[2]");
                    return titleElements.isEmpty() ? "" : titleElements.get(0).text();
                })
                .doOnError(e -> System.err.println("Error accediendo a: " + linkUrl))
                .onErrorResume(e -> Mono.empty());
    }
}
