package upc.edu.pe.BrightMind.notification_service.util;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    @LoadBalanced  // Habilita el balanceo de carga para Eureka
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
