package es.uv.hemal.extraccion.extraccion.models;

import java.util.ArrayList;
import java.util.List;

public class ScraperResult {
    private List<Recurso> links;

     public ScraperResult() {
        this.links = new ArrayList<>(); 
    }
    public ScraperResult(List<Recurso> links) {
        this.links = links;
  
    }

    public List<Recurso> getLinks() {
        return links;
    }
    public void setLinks(List<Recurso> links) {
        this.links = links;
    }

    public void add(Recurso recurso) {
        links.add(recurso);
    }
 }