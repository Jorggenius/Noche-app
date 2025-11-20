# Usar imagen de Eclipse Temurin compatible con Oracle OpenJDK 21
FROM eclipse-temurin:21-jdk

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar los archivos de Gradle
COPY gradle gradle
COPY gradlew .
COPY gradlew.bat .
COPY build.gradle .
COPY settings.gradle .

# Copiar el código fuente
COPY src src

# Dar permisos de ejecución a gradlew
RUN chmod +x ./gradlew

# Construir la aplicación
RUN ./gradlew build -x test

# Exponer el puerto del backend
EXPOSE 8080

# Ejecutar la aplicación con perfil docker
CMD ["java", "-jar", "-Dspring.profiles.active=docker", "build/libs/CapacitacionInterna-0.0.1-SNAPSHOT.jar"]