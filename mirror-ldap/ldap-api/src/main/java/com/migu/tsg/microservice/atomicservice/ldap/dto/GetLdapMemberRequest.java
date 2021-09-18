package com.migu.tsg.microservice.atomicservice.ldap.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class GetLdapMemberRequest {
    private String namespace;
    private List<String> uuids;
    private List<String> usernames;
    private List<String> names;
    private List<String> projects;
    private List<String> orderBy;
    private int pageSize = 20;
    private int currentPage = 1;
}
