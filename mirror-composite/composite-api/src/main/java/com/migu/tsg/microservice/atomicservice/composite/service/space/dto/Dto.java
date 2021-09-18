package com.migu.tsg.microservice.atomicservice.composite.service.space.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
//@AllArgsConstructor
@NoArgsConstructor
public class Dto {
    
    private String uuid;

    private String description;

    private String name;

    private String namespace;

    private List<Quota> quotaList;

    private String status;
 
    private List<String> resource_actions;
 
    private Date updated_at;
 
    private Date created_at;
 
    private String created_by;
    
    public Date getCreated_at() {
        if (this.created_at == null) {
            return null;
        }
        return (Date) this.created_at.clone();
    }
    public void setCreated_at(final Date created_at) {
        if (created_at == null) {
            this.created_at = null;
        } else {
            this.created_at = (Date) created_at.clone();
        }
    }
    public Date getUpdated_at() {
        if (this.updated_at == null) {
            return null;
        }
        return (Date) this.updated_at.clone();
    }
    public void setUpdated_at(final Date updated_at) {
        if (updated_at == null) {
            this.updated_at = null;
        } else {
            this.updated_at = (Date) updated_at.clone();
        }
    }
}
