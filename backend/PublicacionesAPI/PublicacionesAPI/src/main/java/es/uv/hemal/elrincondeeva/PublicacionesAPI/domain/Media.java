package es.uv.hemal.elrincondeeva.PublicacionesAPI.domain;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "media")
public class Media {

    @Id
    private String id;
    
    private String mediaurl;  
    private String timestamp; 
    private String caption;    
    private String mediatype;
    @Indexed
    private String hashtag;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
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
    
    public String getMediaurl() {
        return mediaurl;
    }
    public void setMediaurl(String mediaurl) {
        this.mediaurl = mediaurl;
    }
    public String getMediatype() {
        return mediatype;
    }
    public void setMediatype(String mediatype) {
        this.mediatype = mediatype;
    }
    public String getHashtag() {
        return hashtag;
    }
    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    } 

   
}
