package com.migu.tsg.microservice.atomicservice.rbac.dao.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author menglinjie
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserClassifyAccount implements Serializable {
    private String userClassifyUuid;

    private String userUuid;

    private static final long serialVersionUID = 1L;

}