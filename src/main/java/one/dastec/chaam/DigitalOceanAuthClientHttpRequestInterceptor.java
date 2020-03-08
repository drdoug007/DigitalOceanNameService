package one.dastec.chaam;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Log4j2
@Component
public class DigitalOceanAuthClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {

    private final String apiKey;

    DigitalOceanAuthClientHttpRequestInterceptor(@Value("${digital-ocean-name-service.api-key}") String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        request.getHeaders().add("Authorization", "bearer "+apiKey);
        request.getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        request.getHeaders().add("Accept", MediaType.APPLICATION_JSON_VALUE);
        log.debug(request.getHeaders());
        log.debug(request.getURI().toString());
        return execution.execute(request, body);
    }
}
