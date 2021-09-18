package com.migu.tsg.microservice.atomicservice.composite.service.logs.payload;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class LogsSearchRequest {

    @JsonProperty("clusters")
    private String clusters;

    @JsonProperty("start_time")
    private Date start_time;
    
    @JsonProperty("end_time")
    private Date end_time;
    
    @JsonProperty("instances")
    private String instances;
    
    @JsonProperty("nodes")
    private String nodes;
    
    @JsonProperty("pageno")
    private int pageno;
    
    @JsonProperty("paths")
    private String paths;
    
    @JsonProperty("query_string")
    private String query_string;
    
    @JsonProperty("services")
    private String services;
    
    @JsonProperty("size")
    private int size;
    
    public Date getStart_time() {
        if (this.start_time == null) {
            return null;
        }
        return (Date) this.start_time.clone();
    }
    public void setStart_time(final Date start_time) {
        if (start_time == null) {
            this.start_time = null;
        } else {
            this.start_time = (Date) start_time.clone();
        }
    }
    public Date getEnd_time() {
        if (this.end_time == null) {
            return null;
        }
        return (Date) this.end_time.clone();
    }
    public void setEnd_time(final Date end_time) {
        if (end_time == null) {
            this.end_time = null;
        } else {
            this.end_time = (Date) end_time.clone();
        }
    }
 
}