package ru.itis.flavorful_book.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.itis.flavorful_book.util.converter.StringToUnitConverter;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final StringToUnitConverter stringToUnitConverter;
    private final String uploadDir;

    public WebMvcConfig(StringToUnitConverter stringToUnitConverter,
                        @Value("${app.upload.dir}") String uploadDir) {
        this.stringToUnitConverter = stringToUnitConverter;
        this.uploadDir = uploadDir;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(stringToUnitConverter);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // отдаём загруженные картинки по URL /uploads/**
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadDir + "/");
    }
}
