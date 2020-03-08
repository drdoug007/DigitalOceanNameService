package one.dastec.chaam;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Scheduled;

@Log4j2
@SpringBootApplication
public class DigitalOceanNameServiceApplication {

    private final DigitalOceanNameService nameService;

    public DigitalOceanNameServiceApplication(DigitalOceanNameService nameService) {
        this.nameService = nameService;
    }

    public static void main(String[] args) {
        SpringApplication.run(DigitalOceanNameServiceApplication.class, args);
    }

    @Scheduled(fixedRate = 10*60*1000)
    void execute(){
        nameService.checkName();
    }


}


