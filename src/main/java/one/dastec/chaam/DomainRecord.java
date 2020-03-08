package one.dastec.chaam;

import lombok.Data;

@Data
public class DomainRecord {
    private String id;
    private String type;
    private String name;
    private String data;
    private Integer priority;
    private Integer port;
    private Integer ttl;
    private Integer weight;
    private Integer flags;
    private String tag;
}
