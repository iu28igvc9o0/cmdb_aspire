package com.aspire.ums.cmdb.v3.condication.service.impl;

import com.aspire.ums.cmdb.v3.condication.mapper.CmdbV3AccessUserMapper;
import com.aspire.ums.cmdb.v3.condication.payload.CmdbV3AccessUser;
import com.aspire.ums.cmdb.v3.condication.service.ICmdbV3AccessUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;

/**
* 描述：
* @author
* @date 2020-03-11 15:11:42
*/
@Service
@Slf4j
public class CmdbV3AccessUserServiceImpl implements ICmdbV3AccessUserService {

    @Autowired
    private CmdbV3AccessUserMapper mapper;
    @Value("${cmdb.access.inner}")
    private String innerUserId;

    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    public List<CmdbV3AccessUser> list() {
        return mapper.list();
    }

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回指定ID的数据信息
     */
    public CmdbV3AccessUser get(CmdbV3AccessUser entity) {
        return mapper.get(entity);
    }

    @Override
    public CmdbV3AccessUser getUserByToken(String token) {
        // 获取User用户信息
        CmdbV3AccessUser queryUser = new CmdbV3AccessUser();
        if (!innerUserId.equals(token)) {
            String[] userInfo;
            try {
                Base64.Decoder decoder = Base64.getDecoder();
                token = new String(decoder.decode(token));
                log.info("Decoder token -> {}", token);
                userInfo = token.split("\\+");
                if (userInfo.length != 2) {
                    log.error("Invalid authentication token -> {}", token);
                    throw new RuntimeException("Authentication failed.");
                }
            } catch (Exception e) {
                log.error("Parse token error.", e);
                throw new RuntimeException("Authentication failed.");
            }
            queryUser.setUserName(userInfo[0]);
            queryUser.setPassword(userInfo[1]);
            return mapper.get(queryUser);
        }
        return mapper.getById(token);
    }

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    public void insert(CmdbV3AccessUser entity) {
        mapper.insert(entity);
    }

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    public void update(CmdbV3AccessUser entity) {
        mapper.update(entity);
    }

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    public void delete(CmdbV3AccessUser entity) {
        mapper.delete(entity);
    }
}