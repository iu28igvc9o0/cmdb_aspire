package com.aspire.ums.cmdb.systemManager.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aspire.ums.cmdb.allocate.entity.Result;
import com.aspire.ums.cmdb.systemManager.entity.BizSystem;
import com.aspire.ums.cmdb.systemManager.entity.BizSystemReq;
import com.aspire.ums.cmdb.systemManager.entity.Concat;
import com.aspire.ums.cmdb.systemManager.entity.OrgManager;
import com.aspire.ums.cmdb.systemManager.mapper.BizSystemMapper;
import com.aspire.ums.cmdb.systemManager.service.BizSystemService;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 项目名称:
 * 包: com.aspire.ums.cmdb.systemManager.service.impl
 * 类名称:
 * 类描述: 业务系统管理
 * 创建人: PJX
 * 创建时间: 2019/5/21 15:26
 * 版本: v1.0
 */
@Slf4j
@Service
@Transactional
public class BizSystemServiceImpl implements BizSystemService {
    
    @Autowired
    private BizSystemMapper bizSystemMapper;
    
    @Override
    public Result<BizSystem> getAllBizSystemData(int pageNum,
                                                 int pageSize,
                                                 String pid,
                                                 String bizName,
                                                 String isdisable) {
        BizSystemReq request = new BizSystemReq();
        request.setPageNum(pageNum);
        request.setPageSize(pageSize);
        request.setStartPageNum();
        request.setPid(pid);
        request.setBizName(bizName == null ? "" : bizName.trim());
        request.setIsdisable(isdisable);
        
        Result<BizSystem> response = new Result<BizSystem>();
        int dataCount = bizSystemMapper.getBizSystemDataCount(request);
        response.setCount(dataCount);
        if (dataCount > 0) {
            List<BizSystem> rsList = new ArrayList<BizSystem>();
            List<BizSystem> rs = bizSystemMapper.getBizSystemData(request);
            for (BizSystem bizSystem : rs) {
                List<Concat> concatList = bizSystemMapper.getConcatById(bizSystem.getId());
                bizSystem.setConcatList(concatList);
                String orgId = bizSystem.getOrgId();
                if (StringUtils.isNotEmpty(orgId)) {
                    OrgManager orgManager =bizSystemMapper.getOrgById(orgId);
                    bizSystem.setOrgManager(orgManager);
                }
                rsList.add(bizSystem);
            }
            response.setData(rs);
        }
        log.info("response is {}",response);
        return response;
    }
    
    @Override
    public String insert(BizSystem bizSystem) {
        log.info("request is {}",bizSystem);
        try {
            String id = UUIDUtil.getUUID();
            bizSystem.setId(id);
            bizSystemMapper.insert(bizSystem);
            List<Concat> concatList = bizSystem.getConcatList();
            if (null != concatList) {
                for (Concat concat : concatList) {
                    concat.setBizId(id);
                    bizSystemMapper.insertConcat(concat);
                }
            }
            return "success";
        } catch (Exception e) {
            log.error("[ERROR] >>> ", e);
            return "error";
        }
    }
    
    @Override
    public BizSystem getBizSystemById(String id) {
        BizSystem bizSystem = null;
        try{
            bizSystem = bizSystemMapper.getBizSystemById(id);
            if (null != bizSystem) {
                String orgId = bizSystem.getOrgId();
                if (StringUtils.isNotEmpty(orgId)) {
                    OrgManager orgManager = bizSystemMapper.getOrgById(bizSystem.getOrgId());
                    if (null != orgManager) {
                        bizSystem.setOrgManager(orgManager);
                        bizSystem.setOrgName(orgManager.getOrgName());
                    }
                }
                List<Concat> concatList = bizSystemMapper.getConcatById(id);
                bizSystem.setConcatList(concatList);
            }
        }catch (Exception e) {
            log.error("[ERROR] >>> ", e);
        }
        return bizSystem;
    }
    
    public List<Concat> getConcatsById(String bizId) {
        List<Concat> concatList = bizSystemMapper.getConcatById(bizId);
        return concatList;
    }
    
    @Override
    public String update(BizSystem bizSystem) {
        log.info("request is {}",bizSystem);
        try{
            int rs = bizSystemMapper.updateByPrimaryKey(bizSystem);
            updateSubBizOrgId(bizSystem.getPid());
            String bizId = bizSystem.getId();
            bizSystemMapper.delConcatById(bizId);
            List<Concat> concatList = bizSystem.getConcatList();
            for (Concat concat : concatList) {
                concat.setBizId(bizId);
                bizSystemMapper.insertConcat(concat);
            }
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
    public List<BizSystem> loadTree() {
        List<BizSystem> rootList = new ArrayList<BizSystem>();
        BizSystem bizSystem = new BizSystem();
        bizSystem.setId("0");
        bizSystem.setBizName("全部业务");
        rootList.add(bizSystem);
        getTree(rootList);
        return rootList;
    }
    
    @Override
    public String delete(String id) {
        log.info("request is {}",id);
        try{
            int rs = bizSystemMapper.delete(id);
            if (rs > 0) {
                return "success";
            }
        }catch (Exception e){
            log.error("[ERROR] >>> ", e);
            return "error";
        }
        return null;
    }
    
    private void getTree(List<BizSystem> reuqest){
        for(BizSystem req : reuqest) {
            String pid = req.getId();
            List<BizSystem> childList = bizSystemMapper.getBizSystemByPid(pid);
            if (childList != null && !childList.isEmpty()) {
                req.setChildren(childList);
                getTree(childList);
            } else {
                continue;
            }
        }
    }
    
    private void updateSubBizOrgId (String pid) {
        List<BizSystem> subBizList = bizSystemMapper.getBizSystemByPid(pid);
        if (null != subBizList) {
            for (BizSystem biz : subBizList) {
                String id = biz.getId();
                bizSystemMapper.updateSubBizOrgId(biz.getOrgId(),id);
                updateSubBizOrgId(id);
            }
        }
    }
}
