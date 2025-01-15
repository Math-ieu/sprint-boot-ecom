package fr.smartshop.productservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig  implements WebMvcConfigurer{

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
         // Ajouter un handler pour le répertoire uploads
         registry.addResourceHandler("/uploads/**")  // URL accessible via le navigateur
         .addResourceLocations("file:uploads/");  // Chemin vers le répertoire uploads sur le système de fichiers
    }
    
}
