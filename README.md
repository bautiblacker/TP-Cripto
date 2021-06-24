# TP-Cripto

## Requerimientos
El proyecto requiere tener JDK instalado en la computadora.
## Compilación
Para compilarlo, posicionarse en el directorio raíz y ejecutar `bash compile.sh`
Si se quieren eliminar los archivos de compilación, pararse en el directorio raíz y ejecutar `bash clear-compiled-files.sh`

## Ejecución

### Distribución
`bash run.sh d [imagenSecreta] [k] [directorioDePortadoras]`
La [imagenSecreta] debe ser el nombre de un archivo existente a distribuir. El directorio de portadoras debe contener k imágenes portadoras.
### Recuperación
`bash run.sh r [imagenSecreta] [k] [directorioDePortadoras]`
La [imagenSecreta] debe ser el nombre del archivo donde se guardará la imagen recuperada.
