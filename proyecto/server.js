const express = require('express');
const helmet = require('helmet');

const path = require('path');

const app = express();

app.use(
  helmet.contentSecurityPolicy({
    directives: {
      defaultSrc: ["'self'"],
      scriptSrc: ["'self'",  "https://cdn.jsdelivr.net","https://cdnjs.cloudflare.com/ajax/libs/gsap/1.20.3/TweenMax.min.js", "https://cdnjs.cloudflare.com", "https://ajax.googleapis.com", "http://localhost:*","http://elrincondeeva.local"],
      scriptSrcAttr: ["'self'", "'unsafe-inline'", "https://cdn.jsdelivr.net", "http://localhost:*","http://elrincondeeva.local", "https://cdnjs.cloudflare.com", "https://ajax.googleapis.com"],
      styleSrc: ["'self'", "'unsafe-inline'", "https://fonts.googleapis.com", "https://cdn.jsdelivr.net", "http://localhost:*","http://elrincondeeva.local"],
      frameSrc: ["'self'",  "http://localhost:*","http://elrincondeeva.local", "https://www.youtube.com", "https://www.youtube-nocookie.com", "https://www.facebook.com", "https://www.instagram.com", "https://www.tiktok.com","www.google.com", "https://www.google.es", "https://www.google.com", "https://www.tuejar.es", "https://tuejarturismo.es", "https://www.turismochelva.es"],
      fontSrc: ["'self'", "https://fonts.gstatic.com"],
      imgSrc: ["'self'", "data:","https://*.tile.openstreetmap.org" ,"http://localhost:*","https://scontent.cdninstagram.com","https://www.tuejar.es","https://tuejarturismo.es","https://www.turismochelva.es"],

      mediaSrc: ["'self'",  "http://localhost:*","http://elrincondeeva.local"],
      connectSrc: ["'self'", "http://localhost:8080","http://elrincondeeva.local", "https://cdn.jsdelivr.net", "http://localhost:*", "https://cdnjs.cloudflare.com", "https://ajax.googleapis.com","https://overpass-api.de"],
      objectSrc: ["'none'"],
      baseUri: ["'self'"],
      frameAncestors: ["'none'"],
      formAction: ["'self'"],
        workerSrc: ["'self'"],
      childSrc: ["'self'"]

    }
  })
);



app.use(helmet.noSniff());
app.disable('x-powered-by');
// Ruta absoluta al dist
const distPath = path.join( 'C://Users//alber//OneDrive - Universitat de Valencia//MASTER//TFM//desarrollo//tfm-hemal//proyecto//dist//', 'elrincondeeeva');
app.use(express.static(distPath));
console.log("Buscando:", path.join(distPath, 'index.html'));
// Fallback para Angular (para rutas internas)
app.get('/', (req, res) => {
 res.sendFile('index.html', { root: distPath });

});

const PORT = process.env.PORT || 8089;
app.listen(PORT, '0.0.0.0', () => {
  console.log(`Servidor corriendo en http://localhost:${PORT}`);
});