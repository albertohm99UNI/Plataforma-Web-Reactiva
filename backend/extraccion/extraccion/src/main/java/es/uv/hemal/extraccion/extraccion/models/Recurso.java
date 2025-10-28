package es.uv.hemal.extraccion.extraccion.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "recursos")
public class Recurso {

    @Id
    private String id;

    private String title;
    private String categoria;
    private String imageString;
    private String url;
    private String description;
    private String localizacion;

    public Recurso() {
    }

    public Recurso(String title, String imageString, String url, String description, String categoria, String localizacion) {
        this.title = title;
        this.imageString = imageString;
        this.url = url;
        this.description = description;
        this.localizacion = localizacion;
        this.categoria = categoria;
        this.id = title + "-" + localizacion;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageString() {
        return imageString;
    }

    public void setImageString(String imageString) {
        this.imageString = imageString;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(String localizacion) {
        this.localizacion = localizacion;
    }
}
