package one.dastec.chaam;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class DomainRecordList {

    @JsonProperty("domain_records")
    private List<DomainRecord> domainRecords;

    private Object links;

    private Map<String, Object> meta;

    public DomainRecordList(){
        domainRecords = new ArrayList<>();
    }
}
