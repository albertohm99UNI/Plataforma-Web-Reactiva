#!/bin/bash

# Define tus nombres de imagen y el tag para Docker Hub
DOCKER_USERNAME="albherre"
IMAGE_TAG="v8"

# Define las rutas de cada API
apis=("publicacionesapi-app" "extraccion-app" "elrincondeeva-app" "tiempo-app")
carpeta=("publicacionesapi" "extraccion" "elrincondeeva" "tiempo")

# Recorre cada API y realiza el tag y push
for i in "${!apis[@]}"; do
  api="${apis[$i]}"
  carpeta="${carpeta[$i]}"

  echo "Etiquetando y subiendo la imagen para: $api"

  # Cambia a la carpeta donde se encuentra la imagen (suponiendo que el Dockerfile está ahí)
  cd ./despliegue/$carpeta || { echo "No se encuentra la carpeta $carpeta"; exit 1; }
  pwd

  # Construye la imagen (si no se ha hecho previamente)
  docker build -t $api:$IMAGE_TAG .

  # Etiqueta la imagen con el repositorio de Docker Hub
  docker tag $api:$IMAGE_TAG $DOCKER_USERNAME/$api:$IMAGE_TAG

  # Realiza el push a Docker Hub
  docker push $DOCKER_USERNAME/$api:$IMAGE_TAG

  echo "Imagen $api subida exitosamente."

  cd ../../
  pwd
done
