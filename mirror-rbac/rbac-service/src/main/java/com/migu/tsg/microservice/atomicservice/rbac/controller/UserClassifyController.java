package com.migu.tsg.microservice.atomicservice.rbac.controller;

import com.migu.tsg.microservice.atomicservice.rbac.biz.UserClassifyBiz;
import com.migu.tsg.microservice.atomicservice.rbac.dto.UserClassifyReq;
import com.migu.tsg.microservice.atomicservice.rbac.service.UserClassifyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author menglinjie
 */
@RestController
public class UserClassifyController implements UserClassifyService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserClassifyController.class);

    @Resource
    private UserClassifyBiz userClassifyBiz;


    @Override
    public Map<String, Object> saveUserClassify(@RequestBody UserClassifyReq req) {
        return userClassifyBiz.saveUserClassify(req);
    }

    @Override
    public Map<String, Object> delete(@RequestBody UserClassifyReq req) {
        return userClassifyBiz.delete(req);
    }

    @Override
    public Map<String, Object> findListBySystemId(String systemId) {
        return userClassifyBiz.findListBySystemId(systemId);
    }

    @Override
    public List<UserClassifyReq> findListByLdapId(String ldapId) {
        return userClassifyBiz.findListByLdapId(ldapId);
    }
}
