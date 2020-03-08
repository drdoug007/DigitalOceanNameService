package one.dastec.chaam;

import lombok.Data;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class IpIfyService {

    public final RestTemplate restTemplate;

    public IpIfyService(@Qualifier("ipifyRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Optional<String> getMyIpAddress(){
        IpIfyRs rs = restTemplate.getForObject("/?format=json", IpIfyRs.class);
        if (rs==null){
            return Optional.empty();
        }
        return Optional.of(rs.getIp());
    }
}

@Data
class IpIfyRs {
    private String ip;
}
