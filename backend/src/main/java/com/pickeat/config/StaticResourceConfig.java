package com.pickeat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Configuración para exponer recursos estáticos de uploads.
 */
@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {
    private final Path basePath;

    /**
     * Construye la configuración con la ruta base.
     *
     * @param basePath ruta base de uploads.
     */
    public StaticResourceConfig(@Value("${app.uploads.base-path:uploads}") String basePath) {
        this.basePath = Paths.get(basePath).toAbsolutePath().normalize();
    }

    /**
     * Registra el handler para archivos subidos.
     *
     * @param registry registro de recursos.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String location = basePath.toUri().toString();
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(location + "/");
    }
}
