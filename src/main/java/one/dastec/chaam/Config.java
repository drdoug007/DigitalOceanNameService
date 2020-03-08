package one.dastec.chaam;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@EnableScheduling
@Configuration
public class Config {

    @Bean("digitalOceanRestTemplate")
    public RestTemplate digitalOceanRestTemplate(
            @Value("${digital-ocean-name-service.digital.ocean.url}") String rootUri,
            DigitalOceanAuthClientHttpRequestInterceptor authClientHttpRequestInterceptor){
        return new RestTemplateBuilder()
                .rootUri(rootUri)
                .additionalInterceptors(authClientHttpRequestInterceptor)
                .build();
    }

    @Bean("ipifyRestTemplate")
    public RestTemplate ipifyRestTemplate(
            @Value("${digital-ocean-name-service.ipify.url}") String rootUri){
        return new RestTemplateBuilder()
                .rootUri(rootUri)
                .build();
    }
}
