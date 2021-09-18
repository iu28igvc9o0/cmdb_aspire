package com.migu.tsg.microservice.atomicservice.composite.controller.rbac;

import com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc.UserClassifyServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.IUserClassifyService;
import com.migu.tsg.microservice.atomicservice.rbac.dto.UserClassifyReq;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author menglinjie
 */
@RestController
public class UserClassifyController implements IUserClassifyService {
    @Resource
    UserClassifyServiceClient userClassifyServiceClient;

    @Override
    public Object saveUserClassify(UserClassifyReq req) {
        return userClassifyServiceClient.saveUserClassify(req);
    }

    @Override
    public Object delete(UserClassifyReq req) {
        return userClassifyServiceClient.delete(req);
    }

    @Override
    public Object findListBySystemId(String systemId) {
        return userClassifyServiceClient.findListBySystemId(systemId);
    }

    @Override
    public Object findListByLdapId(String ldapId) {
        return userClassifyServiceClient.findListByLdapId(ldapId);
    }
}
