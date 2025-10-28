#!/bin/bash

# Lista de APIs
apis=("PublicacionesAPI" "extraccion" "elrincondeeva" "tiempo")

# Ruta base de origen y destino
origen_base="./"
destino_base="./despliegue"

# Crear carpetas de destino y copiar el .jar correspondiente
for api in "${apis[@]}"; do
  origen_jar="${origen_base}${api}/${api}/target"
  destino_dir="${destino_base}/${api}"

  # Crear el directorio destino si no existe
  mkdir -p "$destino_dir"

  # Copiar el jar al destino (se asume que hay solo uno)
  cp "$origen_jar"/*.jar "$destino_dir/"
  
  echo "Copiado $api a $destino_dir"
done
