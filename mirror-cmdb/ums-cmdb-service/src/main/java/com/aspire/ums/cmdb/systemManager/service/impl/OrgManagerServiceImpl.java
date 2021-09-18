package com.aspire.ums.cmdb.systemManager.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aspire.ums.cmdb.allocate.entity.Result;
import com.aspire.ums.cmdb.systemManager.entity.*;
import com.aspire.ums.cmdb.systemManager.mapper.OrgManagerMapper;
import com.aspire.ums.cmdb.systemManager.service.OrgManagerService;
import com.aspire.ums.cmdb.util.UUIDUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 项目名称:
 * 包: com.aspire.ums.cmdb.systemManager.service.impl
 * 类名称:
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/5/23 15:26
 * 版本: v1.0
 */
@Slf4j
@Service
@Transactional
public class OrgManagerServiceImpl implements OrgManagerService {
    @Autowired
    private OrgManagerMapper orgManagerMapper;
    
    @Override
    public Result<OrgManager> getAllOrgManagerData(int pageNum,
                                                 int pageSize,
                                                 String pid,
                                                 String orgName,
                                                 String orgType) {
        OrgManagerReq request = new OrgManagerReq();
        request.setPageNum(pageNum);
        request.setPageSize(pageSize);
        request.setPid(pid);
        request.setStartPageNum();
        request.setOrgName(orgName == null ? "" : orgName.trim());
        request.setOrgType(orgType);
        
        Result<OrgManager> response = new Result<OrgManager>();
        int dataCount = orgManagerMapper.getOrgManagerDataCount(request);
        response.setCount(dataCount);
        if (dataCount > 0) {
            List<OrgManager> rs = orgManagerMapper.getOrgManagerData(request);
            response.setData(rs);
        }
        log.info("response is {}",response);
        return response;
    }

    @Override
    public String eipInsert(OrgManager bizSystem) {
        log.info("request is {}",bizSystem);
        try {
            orgManagerMapper.insert(bizSystem);
            return "success";
        } catch (Exception e) {
            log.error("[ERROR] >>> ", e);
        }
        return "error";
    }

    @Override
    public String insert(OrgManager bizSystem) {
        log.info("request is {}",bizSystem);
        try {
            String id = UUIDUtil.getUUID();
            bizSystem.setId(id);
            orgManagerMapper.insert(bizSystem);
            return "success";
        }catch (Exception e) {
            log.error("[ERROR] >>> ", e);
            return "error";
        }
    }
    
    @Override
    public OrgManager getOrgManagerById(String id) {
        OrgManager orgManager = null;
        try{
            orgManager = orgManagerMapper.getOrgManagerById(id);
        }catch (Exception e) {
            log.error("[ERROR] >>> ", e);
        }
        return orgManager;
    }
    
    @Override
    public String update(OrgManager orgManager) {
        log.info("request is {}",orgManager);
        try{
            int rs = orgManagerMapper.updateByPrimaryKey(orgManager);
            if (rs > 0) {
                return "success";
            }
        }catch (Exception e){
            log.error("[ERROR] >>> ", e);
            return "error";
        }
        return null;
    }
    
    @Override
    public String delete(String id) {
        log.info("request is {}",id);
        try{
            int rs = orgManagerMapper.delete(id);
            if (rs > 0) {
                return "success";
            }
        }catch (Exception e){
            log.error("[ERROR] >>> ", e);
            return "error";
        }
        return null;
    }
    
    @Override
    public List<OrgManager> getParentOrg(String id) {
        List<OrgManager> orgManagerList = orgManagerMapper.getParentOrg(id);
        return orgManagerList;
    }
    
    @Override
    public List<OrgManager> getOrgManagerByPid (String pid) {
        return orgManagerMapper.getOrgManagerByPid(pid);
    }
    
    @Override
    public List<OrgManager> loadTree() {
        List<OrgManager> rootList = new ArrayList<>();
        OrgManager root = new OrgManager();
        root.setId("0");
        root.setOrgName("根组织");
        rootList.add(root);
        getTree(rootList);
        return rootList;
    }
    
    private void getTree(List<OrgManager> request){
        for(OrgManager req : request) {
            String pid = req.getId();
            List<OrgManager> childList = orgManagerMapper.getOrgManagerByPid(pid);
            if (childList != null && !childList.isEmpty()) {
                req.setChildren(childList);
                getTree(childList);
            } else {
                continue;
            }
        }
    }
}
