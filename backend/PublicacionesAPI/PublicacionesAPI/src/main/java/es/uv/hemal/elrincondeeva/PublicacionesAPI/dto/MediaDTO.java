package es.uv.hemal.elrincondeeva.PublicacionesAPI.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

public class MediaDTO {
    private String id;
    @JsonAlias("media_url")
    private String mediaUrl;  
    private String timestamp; 
    private String caption; 
    @JsonAlias("media_type")   
    private String mediaType;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getMediaUrl() {
        return mediaUrl;
    }
    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }
    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    public String getCaption() {
        return caption;
    }
    public void setCaption(String caption) {
        this.caption = caption;
    }
    public String getMediaType() {
        return mediaType;
    }
    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }
    
}
