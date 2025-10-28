package es.uv.hemal.elrincondeeva.PublicacionesAPI.services;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class InstagramTokenService {

    private final WebClient webClient = WebClient.create("https://graph.instagram.com");

   
    @Value("${instagram.token-file-path}")
    private String tokenFilePath;

        public String getToken() throws IOException {
        
        Path path = Paths.get(System.getProperty("user.dir"), tokenFilePath, "token.txt");

        return Files.readAllLines(path).get(0).trim();
    }

    public void saveToken(String newToken) throws IOException {
        try {
            Path dirPath = Paths.get(System.getProperty("user.dir"), tokenFilePath);
            Path filePath = dirPath.resolve("token.txt");
           
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }
            Files.writeString(filePath, newToken);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   @Scheduled(cron = "0 0 0 30 * ?") 
    public void refreshToken() {  
        try {
            String currentToken = getToken();
            webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/refresh_access_token")
                        .queryParam("grant_type", "ig_refresh_token")
                        .queryParam("access_token", currentToken)
                        .build())
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .map(TokenResponse::getAccess_token)
                .doOnTerminate(() -> System.out.println("Proceso de refresco del token finalizado"))
                .subscribe(newToken -> {
                    if (newToken != null && !newToken.isBlank()) {
                        try {
            
                            saveToken(newToken);
                        } catch (IOException e) {
                            System.err.println("❌ Error al guardar el token en el archivo: " + e.getMessage());
                        }
                    } else {
                        System.err.println("⚠️ No se pudo obtener un nuevo token.");
                    }
                }, error -> {
                    System.err.println("❌ Error al refrescar el token: " + error.getMessage());
                });
        } catch (IOException e) {
            System.err.println("Error al leer el token desde el archivo: " + e.getMessage());
            return;
        }
        
    }

    public static class TokenResponse {
        private String access_token;
        private String token_type;
        private long expires_in;
        private String permissions;
        public TokenResponse() {
        }
    
       @JsonProperty("access_token")
        public String getAccess_token() {
            return access_token;
        }
    
        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }
    
        public String getToken_type() {
            return token_type;
        }
    
        public void setToken_type(String token_type) {
            this.token_type = token_type;
        }
    
        public long getExpires_in() {
            return expires_in;
        }
    
        public void setExpires_in(long expires_in) {
            this.expires_in = expires_in;
        }
    
        public String getPermissions() {
            return permissions;
        }
    
        public void setPermissions(String permissions) {
            this.permissions = permissions;
        }
    }
    
}
