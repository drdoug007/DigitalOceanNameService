package one.dastec.chaam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Log4j2
public class DigitalOceanNameService {

    private static final String A_RECORD = "A";
    private final String domainName;
    private final RestTemplate restTemplate;
    private final IpIfyService ipIfyService;
    private final String dnsName;

    DigitalOceanNameService(
            @Value("${digital-ocean-name-service.dns-name}")String dnsName,
            @Value("${digital-ocean-name-service.domain-name}") String domainName,
                            @Qualifier("digitalOceanRestTemplate") RestTemplate restTemplate,
                            IpIfyService ipIfyService){
        this.domainName = domainName;
        this.restTemplate = restTemplate;
        this.ipIfyService = ipIfyService;
        this.dnsName = dnsName;
    }

    public void checkName(){
        log.info("CheckName: "+dnsName+"."+domainName);
        Map<String, String> uriParams = new HashMap<>();
        uriParams.put("domainName", domainName);

        ObjectMapper mapper = new ObjectMapper();
        try {
            ResponseEntity<String> res = restTemplate.exchange("/v2/domains/{domainName}/records", HttpMethod.GET, null,
                    String.class, uriParams);
            if (res.getStatusCode().is2xxSuccessful()){
                DomainRecordList recordList = mapper.readValue(res.getBody(), DomainRecordList.class);

                Optional<DomainRecord> chaamRec = recordList.getDomainRecords().stream().filter(r -> r.getName().equals(dnsName)).findFirst();
                if (chaamRec.isPresent()){
                    updateRecord(uriParams, chaamRec);
                } else {
                    createNewRecord(uriParams);
                }
            } else {
                log.error("getDomain Response Code: "+res.getStatusCode().name());
            }

        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
    }

    private void updateRecord(Map<String, String> uriParams, Optional<DomainRecord> chaamRec) {
        DomainRecord record = chaamRec.get();
        Optional<String> myIp = ipIfyService.getMyIpAddress();
        if (myIp.isPresent()){
            if (!myIp.get().equals(record.getData())){
                Map<String, String> putUriParams = new HashMap<>(uriParams);
                putUriParams.put("id", record.getId());
                record.setData(myIp.get());
                restTemplate.put("/v2/domains/{domainName}/records/{id}", record, putUriParams);
                log.info("Updating IP Address: "+myIp.get());
            } else {
                log.debug(record.getData());
            }
        }


    }

    private void createNewRecord(Map<String, String> uriParams) {
        Optional<String> myIp = ipIfyService.getMyIpAddress();
        if (myIp.isPresent()) {
            DomainRecord domainRecord = new DomainRecord();
            domainRecord.setType(A_RECORD);
            domainRecord.setName(dnsName);
            domainRecord.setData(myIp.get());
            domainRecord.setTtl(30);
            HttpEntity<DomainRecord> httpEntity = new HttpEntity<>(domainRecord, null);
            ResponseEntity<DomainRecord> responseEntity = restTemplate.exchange("/v2/domains/{domainName}/records", HttpMethod.POST, httpEntity,
                    DomainRecord.class, uriParams);
            log.info(responseEntity.getStatusCode());
            log.info(domainRecord);
        }
    }
}
