package com.migu.tsg.microservice.atomicservice.rbac.biz;

import com.migu.tsg.microservice.atomicservice.rbac.dao.UserClassifyAccountDao;
import com.migu.tsg.microservice.atomicservice.rbac.dao.UserClassifyDao;
import com.migu.tsg.microservice.atomicservice.rbac.dao.UserClassifyPageConfigDao;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.UserClassify;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.UserClassifyAccount;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.UserClassifyPageConfig;
import com.migu.tsg.microservice.atomicservice.rbac.dto.UserClassifyReq;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.UserDTO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author menglinjie
 */
@Service
@Transactional
public class UserClassifyBiz {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(UserClassifyBiz.class);

    @Resource
    private UserClassifyDao userClassifyDao;

    @Resource
    private UserClassifyAccountDao userClassifyAccountDao;

    @Resource
    private UserClassifyPageConfigDao userClassifyPageConfigDao;

    @Resource
    private UserBiz userBiz;

    @Transactional(rollbackFor=Exception.class)
    public Map<String, Object> saveUserClassify(UserClassifyReq req) {
        Date date = new Date();
        //新增
        if (StringUtils.isBlank(req.getUuid())){
            UserClassify userClassify = new UserClassify();
            BeanUtils.copyProperties(req, userClassify);
            userClassify.setUuid(UUID.randomUUID().toString());
            userClassify.setLastUpdateTime(date);
            //保存人员分类
            userClassifyDao.insert(userClassify);
            if (req.getUserIds() != null){
                saveUserClassifyAccountList(req.getUserIds(),userClassify.getUuid());
            }
            //保存人员分类与页面关系
            saveUserClassifyPageConfig(req,date,userClassify.getUuid());

            LOGGER.debug("新增成功："+userClassify.getUuid());
        }else {
            //编辑
            UserClassify userClassify = userClassifyDao.selectByPrimaryKey(req.getUuid());
            userClassify.setUserClassifyName(req.getUserClassifyName());
            userClassify.setDescription(req.getDescription());
            userClassify.setLastUpdateTime(date);
            //保存人员分类
            userClassifyDao.updateByPrimaryKey(userClassify);
            //根据人员类别删除旧的关联用户
            userClassifyAccountDao.deleteByUserClassifyId(req.getUuid());
            if (req.getUserIds() != null){
                saveUserClassifyAccountList(req.getUserIds(),userClassify.getUuid());

            }
            //后续有可能一个人员分类绑定多个首页，所以此处先根据人员分类id删除旧的绑定，然后插入薪的绑定
            userClassifyPageConfigDao.deleteByUserClassifyId(req.getUuid());
            //保存人员分类与页面关系
            saveUserClassifyPageConfig(req,date, userClassify.getUuid());

            LOGGER.debug("编辑成功："+userClassify.getUuid());
        }
        Map<String,Object> map = new HashMap<>();
        map.put("state","success");
        return map;
    }

    private void saveUserClassifyPageConfig(UserClassifyReq req, Date date, String userClassifyId) {
        UserClassifyPageConfig userClassifyPageConfig = new UserClassifyPageConfig();
        userClassifyPageConfig.setUuid(UUID.randomUUID().toString());
        userClassifyPageConfig.setUserClassifyUuid(userClassifyId);
        userClassifyPageConfig.setSystemId(req.getSystemId());
        userClassifyPageConfig.setPageCode(req.getPageCode());
        userClassifyPageConfig.setPageAlias(req.getPageAlias());
        userClassifyPageConfig.setPageCustomConfig(req.getPageCustomConfig());
        userClassifyPageConfig.setNamespace(req.getNamespace());
        userClassifyPageConfig.setCreator(req.getCreator());
        userClassifyPageConfig.setLastUpdateTime(date);
        userClassifyPageConfigDao.insert(userClassifyPageConfig);
    }

    private void saveUserClassifyAccountList(String[] userIds, String userClassifyId) {
        List<UserClassifyAccount> userClassifyAccountList = new ArrayList<>();
        List<String> userIdList = Arrays.asList(userIds);
        for (String userId : userIdList){
            UserClassifyAccount userClassifyAccount = new UserClassifyAccount();
            userClassifyAccount.setUserClassifyUuid(userClassifyId);
            userClassifyAccount.setUserUuid(userId);
            userClassifyAccountList.add(userClassifyAccount);

        }
        //保存人员分类与用户关系
        userClassifyAccountDao.saveList(userClassifyAccountList);
    }

    public Map<String, Object> findListBySystemId(String systemId) {
        List<Map> list = userClassifyDao.findListBySystemId(systemId);
        Map<String,Object> map = new HashMap<>();
        map.put("state","success");
        map.put("data",list);
        return map;
    }

    public List<UserClassifyReq> findListByLdapId(String ldapId) {
        //根据账号查询用户
        UserDTO userDTO = userBiz.findByLdapId(ldapId);
        if (userDTO == null){
            throw new RuntimeException("账号不存在");
        }
        //担心数据量过大，以下单表查询
        String userId = userDTO.getUuid();
        List<UserClassifyAccount> userClassifyAccountList = userClassifyAccountDao.findByUserId(userId);
        List<String> userClassifyIdList = userClassifyAccountList.stream().map(p->p.getUserClassifyUuid()).collect(Collectors.toList());
       if (!CollectionUtils.isEmpty(userClassifyIdList)){
           List<UserClassifyReq> pageList = userClassifyDao.findListById(userClassifyIdList);
//           Map<String,List<UserClassifyReq>> stringListMap =pageList.stream().collect(Collectors.groupingBy(UserClassifyReq::getSystemId));
           return pageList;
       }
        LOGGER.debug("该用户未分配首页");
        return null;
    }

    @Transactional(rollbackFor=Exception.class)
    public Map<String, Object> delete(UserClassifyReq req) {
        userClassifyDao.deleteByPrimaryKey(req.getUuid());
        userClassifyAccountDao.deleteByUserClassifyId(req.getUuid());
        userClassifyPageConfigDao.deleteByUserClassifyId(req.getUuid());
        Map<String,Object> map = new HashMap<>();
        map.put("state","success");
        return map;
    }
}
