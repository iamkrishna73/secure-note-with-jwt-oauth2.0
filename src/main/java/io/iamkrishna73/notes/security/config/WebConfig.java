package io.iamkrishna73.notes.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
public class WebConfig implements WebMvcConfigurer {
//    @Value("${frontend.url}")
//    private String frontendUrl;
//
//    public void addCorsMapping(CorsRegistry corsRegistry) {
//        corsRegistry.addMapping("app/notes/**")
//                .allowedOrigins(frontendUrl)
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTION")
//                .allowedHeaders("*")
//                .allowCredentials(true)
//                .maxAge(3000);
//
//        // Additional paths can be configured similarly
//        corsRegistry.addMapping("/api/other/**")
//                .allowedOrigins(frontendUrl)
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                .allowedHeaders("*")
//                .allowCredentials(true)
//                .maxAge(3600);
//    }
}
