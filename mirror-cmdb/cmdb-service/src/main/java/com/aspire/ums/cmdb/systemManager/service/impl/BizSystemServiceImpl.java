package com.aspire.ums.cmdb.systemManager.service.impl;

import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.systemManager.mapper.BizSystemMapper;
import com.aspire.ums.cmdb.systemManager.payload.BizSystem;
import com.aspire.ums.cmdb.systemManager.payload.BizSystemReq;
import com.aspire.ums.cmdb.systemManager.payload.Concat;
import com.aspire.ums.cmdb.systemManager.payload.OrgManager;
import com.aspire.ums.cmdb.systemManager.service.BizSystemService;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
                                                 String department1,
                                                 String department2,
                                                 String bizName,
                                                 String isdisable) {
        BizSystemReq request = new BizSystemReq();
        request.setPageNum(pageNum);
        request.setPageSize(pageSize);
        request.setStartPageNum();
        request.setPid(pid);
        request.setBizName(bizName == null ? "" : bizName.trim());
        request.setIsdisable(isdisable);
        if (StringUtils.isNotEmpty(department1) && StringUtils.isNotEmpty(department2)) {
            request.setDepartmentId(department2);
        } else if (StringUtils.isNotEmpty(department1) && StringUtils.isEmpty(department2)) {
            request.setDepartmentId(department1);
        }

        Result<BizSystem> response = new Result<BizSystem>();
        int dataCount = bizSystemMapper.getBizSystemDataCount(request);
        response.setCount(dataCount);
        if (dataCount > 0) {
            List<BizSystem> rs = bizSystemMapper.getBizSystemData(request);
            List<BizSystem> rsList = new ArrayList<BizSystem>();
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
            log.error("[ERROR] >>> " + e.toString());
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
            log.error("[ERROR] >>> " + e.toString());
        }
        return bizSystem;
    }
    
    public List<Concat> getConcatsById(String bizId) {
        List<Concat> concatList = bizSystemMapper.getConcatById(bizId);
        return concatList;
    }

    @Override
    public List<Map<String, String>> getBizSystemByIds(String ids) {
        List<Map<String, String>> bizSystemList = new LinkedList<>();
        String[] bizIds = ids.split(",");
        for(String id : bizIds) {
            BizSystem bizSystem = bizSystemMapper.getBizSystemById(id);
            if (bizSystem != null) {
                Map<String, String> bizMap = new HashMap<>();
                bizMap.put("uuid", bizSystem.getId());
                bizMap.put("code", bizSystem.getBizName());
                bizMap.put("name", bizSystem.getBizName());
                bizSystemList.add(bizMap);
            }
        }
        return bizSystemList;
    }

    @Override
    public Map<String, String> getDepartmentInfoByBizSystem(String bizSystem) {
        return bizSystemMapper.getDepartmentInfoByBizSystem(bizSystem);
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
            log.error("[ERROR] >>> " + e.toString());
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
            log.error("[ERROR] >>> " + e.toString());
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
