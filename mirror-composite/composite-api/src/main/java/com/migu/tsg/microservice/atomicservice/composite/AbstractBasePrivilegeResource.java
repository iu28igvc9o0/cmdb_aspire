package com.migu.tsg.microservice.atomicservice.composite;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractBasePrivilegeResource extends AbstractPrivilegeResource {
    public abstract String getUuid();

    public abstract void setUuid(String uuid);

    @Override
    public String getPrivResUuid() {
        return getUuid();
    }

    @Override
    public void setPrivResUuid(String uuid) {
        setUuid(uuid);
    }
}