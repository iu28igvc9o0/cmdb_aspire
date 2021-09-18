package com.aspire.ums.cmdb.systemManager.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.config.KafkaConfigConstant;
import com.aspire.ums.cmdb.sync.payload.CmdbOptType;
import com.aspire.ums.cmdb.sync.service.producer.SysOrgProducerServiceImpl;
import com.aspire.ums.cmdb.systemManager.mapper.OrgManagerMapper;
import com.aspire.ums.cmdb.systemManager.payload.OrgManager;
import com.aspire.ums.cmdb.systemManager.payload.OrgManagerReq;
import com.aspire.ums.cmdb.systemManager.service.OrgManagerService;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.aspire.ums.cmdb.v2.instance.mapper.CmdbInstanceMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * 项目名称: 包: com.aspire.ums.cmdb.systemManager.service.impl 类名称: 类描述: 创建人: PJX 创建时间: 2019/5/23 15:26 版本: v1.0
 */
@Slf4j
@Service
@Transactional
public class OrgManagerServiceImpl implements OrgManagerService {

    @Autowired
    private OrgManagerMapper orgManagerMapper;

    @Autowired
    private CmdbInstanceMapper instanceMapper;

    @Autowired(required = false)
    private SysOrgProducerServiceImpl sysOrgProducerService;

    private Map<String, Map<String, Integer>> bizCounts;

    public void setBizCounts() {
        this.bizCounts = new HashMap<>();
        List<Map<String, Object>> counts = instanceMapper.countByBiz();
        counts.forEach(count -> {
            if (StringUtils.isNotEmpty(count.get("department1")) && StringUtils.isNotEmpty(count.get("bizSystem"))) {
                if (null == bizCounts.get(count.get("department1").toString())) {
                    this.bizCounts.put(count.get("department1").toString(), new HashMap<>());
                }
                if (null == bizCounts.get(count.get("department1").toString()).get(count.get("bizSystem").toString())) {
                    this.bizCounts.get(count.get("department1").toString()).put(count.get("bizSystem").toString(),
                            Integer.parseInt(count.get("count").toString()));
                }
            }
        });
    }

    @Override
    public Result<OrgManager> getAllOrgManagerData(int pageNum, int pageSize, String pid, String orgName, String orgType) {
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
        return response;
    }

    @Override
    public String insert(OrgManager bizSystem) {
        log.info("request is {}", bizSystem);
        try {
            String id = bizSystem.getId();
            if (org.apache.commons.lang3.StringUtils.isBlank(id)) {
                id = UUIDUtil.getUUID();
            }
            bizSystem.setId(id);
            orgManagerMapper.insert(bizSystem);

            if (bizSystem.isSyncFlag()) {
                // 发送同步数据到kafka,网管端的组织机构 存在字典表-id
                sysOrgProducerService.saveEventLogAndSendMsg(CmdbOptType.OPT_ADD, KafkaConfigConstant.TOPIC_CONFIG_DICT, null, id,
                        id, KafkaConfigConstant.TABLE_SYS_ORG);
            }
            return "success";
        } catch (Exception e) {
            log.error("[ERROR] >>> " + e.toString());
            return "error";
        }
    }

    @Override
    public String pureInsert(OrgManager bizSystem) {
        log.info("request is {}", bizSystem);
        try {
            orgManagerMapper.insert(bizSystem);
            return "success";
        } catch (Exception e) {
            log.error("[ERROR] >>> " + e.toString());
            return "error";
        }
    }

    @Override
    public OrgManager getOrgManagerById(String id) {
        OrgManager orgManager = null;
        try {
            orgManager = orgManagerMapper.getOrgManagerById(id);
        } catch (Exception e) {
            log.error("[ERROR] >>> " + e.toString());
        }
        return orgManager;
    }

    @Override
    public String update(OrgManager orgManager) {
        log.info("request is {}", orgManager);
        int rs = 0;
        try {
            OrgManager entity = orgManagerMapper.getOrgManagerById(orgManager.getId());
            if (entity == null) {
                return insert(orgManager);
            }
            rs = orgManagerMapper.updateByPrimaryKey(orgManager);

            if (orgManager.isSyncFlag()) {
                // 发送同步数据到kafka,网管端的组织机构 存在字典表-id
                sysOrgProducerService.saveEventLogAndSendMsg(CmdbOptType.OPT_MODIFY, KafkaConfigConstant.TOPIC_CONFIG_DICT, null,
                        orgManager.getId(), orgManager.getId(), KafkaConfigConstant.TABLE_SYS_ORG);
            }

            if (rs > 0) {
                return "success";
            }
        } catch (Exception e) {
            log.error("[ERROR] >>> " + e.toString());
            return "error";
        }
        return null;
    }

    @Override
    public String delete(String id) {
        log.info("request is {}", id);
        try {
            int rs = orgManagerMapper.delete(id);
            // 发送同步数据到kafka,网管端的组织机构 存在字典表-id
            sysOrgProducerService.saveEventLogAndSendMsg(CmdbOptType.OPT_DEL, KafkaConfigConstant.TOPIC_CONFIG_DICT, null, id, id,
                    KafkaConfigConstant.TABLE_SYS_ORG);
            if (rs > 0) {
                return "success";
            }
        } catch (Exception e) {
            log.error("[ERROR] >>> " + e.toString());
            return "error";
        }
        return null;
    }

    @Override
    public String delete(OrgManager orgManager) {
        String id = orgManager.getId();
        log.info("request is {}", id);
        try {
            int rs = orgManagerMapper.delete(id);
            if (rs > 0) {
                return "success";
            }
        } catch (Exception e) {
            log.error("[ERROR] >>> " + e.toString());
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
    public List<OrgManager> getOrgManagerByPid(String pid) {
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

    @Override
    public List<Map> loadTreeDepBiz() {
        // 一级部门
        List<OrgManager> org1List = orgManagerMapper.getOrgManagerByPid("0");
        List<Map> treeList = new ArrayList<>();
        for (OrgManager org : org1List) {
            Map<String, Object> map = new HashMap();
            map.put("uuid", org.getId());
            map.put("name", org.getOrgName());
            map.put("type", "department1");
            map.put("icon", "el-icon-menu");
            map.put("parentName", org.getOrgName());
            map.put("parentUUID", "0");
            //一级部门下的二级部门数据
            List<OrgManager> org2List = orgManagerMapper.getOrgManagerByPid(org.getId());
            List<Map> org1SubList = new ArrayList<>();
            for (OrgManager org2 : org2List) {
                Map<String, Object> o2map = new HashMap();
                o2map.put("uuid", org2.getId());
                o2map.put("name", org2.getOrgName());
                o2map.put("type", "department2");
                o2map.put("icon", "el-icon-printer");
                o2map.put("leaf", true);
                o2map.put("parentName", org.getOrgName());
                o2map.put("parentUUID", org.getId());
                //二级部门下业务系统
                List<Map> dept2BizList = orgManagerMapper.getBizSystemByDepartment2(org2.getId());
                o2map.put("subList", dept2BizList);
                org1SubList.add(o2map);
            }
            //一级部门下的业务系统数据
            List<Map> dept1BizList = orgManagerMapper.getBizSystemByDepartment1(org.getId());
            org1SubList.addAll(dept1BizList);
            map.put("subList", org1SubList);
            treeList.add(map);
        }
        return treeList;
    }

    public List<Map> loadTreeDepBizOld() {
        try {
//            this.setBizCounts();
            List<Map> treeList = new LinkedList<>();
            List<OrgManager> childList = orgManagerMapper.getOrgManagerByPid("0");
            // 一级部门
            if (childList != null && !childList.isEmpty()) {
                for (OrgManager org : childList) {
                    Map map = new HashMap();
                    map.put("uuid", org.getId());
                    map.put("name", org.getOrgName());
                    map.put("type", "department1");
                    map.put("icon", "el-icon-menu");
                    map.put("parentName", org.getOrgName());
                    map.put("parentUUID", "0");
                    treeList.add(map);
                }
                getTree2(treeList);
            }
//            for (Map list : treeList) {
//                for (Map sub : (List<Map>) list.get("subList")) {
//                    if ("department2".equals(sub.get("type")) && (int) sub.get("subSize") > 1) {
//                        Collections.sort((List<Map>) list.get("subList"), new Comparator<Map>() {
//
//                            @Override
//                            public int compare(Map o1, Map o2) {
//                                Integer size1 = (Integer) o1.get("subSize");// name1是从你list里面拿出来的一个
//                                Integer size2 = (Integer) o2.get("subSize"); // name1是从你list里面拿出来的第二个name
//                                return size2.compareTo(size1);
//                            }
//                        });
//                    }
//                }
                // list.put("subSize",subSize);
//            }
//            for (Map list : treeList) {
//                Integer subSize = 0;
//                for (Map sub : (List<Map>) list.get("subList")) {
//                    if ("bizSystem".equals(sub.get("type"))) {
//                        subSize++;
//                    }
//                    if ("department2".equals(sub.get("type"))) {
//                        subSize += Integer.parseInt(sub.get("subSize").toString());
//                    }
//                }
//                list.put("subSize", subSize);
//            }
            return treeList;
        } catch (Exception e) {
            log.error("读取组织树信息报错.");
            e.printStackTrace();
            throw new RuntimeException("读取组织树信息失败.error:" + e.getMessage());
        }
    }

    @Override
    public List<OrgManager> getAllOrg() {
        return orgManagerMapper.getAllOrg();
    }

    @Override
    public List<OrgManager> getAllEipOrg() {
        return orgManagerMapper.getAllEipOrg();
    }

    private void getTree(List<OrgManager> request) {
        for (OrgManager req : request) {
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

    private void getTree2(List<Map> request) {
        for (Map<String, Object> req : request) {
            String pid = String.valueOf(req.get("uuid"));
            String type = String.valueOf(req.get("type"));

            List<Map> subList = new LinkedList<>();
            List<OrgManager> childList = new ArrayList<>();
            // 先加载部门
            if ("department1".equals(type)) {
                childList = orgManagerMapper.getOrgManagerByPid(pid);
            }
            if (childList != null && !childList.isEmpty() ) {
                for (OrgManager org : childList) {
                    Map map = new HashMap();
                    map.put("uuid", org.getId());
                    map.put("name", org.getOrgName());
                    map.put("type", "department2");
                    map.put("icon", "el-icon-printer");
                    map.put("leaf", true);
                    map.put("parentName", req.get("parentName"));
                    map.put("parentUUID", req.get("uuid"));
                    subList.add(map);

                }
                if (!"bizSystem".equals(type)) {
                    List<Map> bizList;
                    String orgName = String.valueOf(req.get("uuid"));
                    if ("department1".equals(type)) {
                        bizList = orgManagerMapper.getBizSystemByDepartment1(orgName);
                    } else {
                        bizList = orgManagerMapper.getBizSystemByDepartment2(orgName);
                    }
                    if (null != bizList && !bizList.isEmpty()) {
                        bizList.forEach(biz -> {
                            biz.put("leaf", true);
                            biz.put("icon", "el-icon-share");
//                            if (null != this.bizCounts.get(req.get("uuid").toString())) {
//                                if (null != this.bizCounts.get(req.get("uuid").toString()).get(biz.get("uuid").toString())) {
//                                    biz.put("instanceSize",
//                                            this.bizCounts.get(req.get("uuid").toString()).get(biz.get("uuid").toString()));
//                                }
//                            }
                        });
                    }
                    subList.addAll(bizList);
                }
                req.put("subList", subList);
                req.put("subSize", subList.size());
                getTree2(subList);
            } else {
                if (!"bizSystem".equals(type)) {
                    List<Map> bizList;
                    String orgName = String.valueOf(req.get("uuid"));
                    if ("department1".equals(type)) {
                        bizList = orgManagerMapper.getBizSystemByDepartment1(orgName);
                    } else {
                        bizList = orgManagerMapper.getBizSystemByDepartment2(orgName);
                    }
                    if (null != bizList && !bizList.isEmpty()) {
                        bizList.forEach(biz -> {
                            biz.put("leaf", true);
                            biz.put("icon", "el-icon-share");
//                            if (req != null && req.containsKey("parentUUID") && req.get("parentUUID") != null &&
//                                    null != this.bizCounts.get(req.get("parentUUID").toString())) {
//                                if (null != this.bizCounts.get(req.get("parentUUID").toString()).get(biz.get("uuid").toString())) {
//                                    biz.put("instanceSize",
//                                            this.bizCounts.get(req.get("parentUUID").toString()).get(biz.get("uuid").toString()));
//                                }
//                            }
                        });
                    }
                    subList.addAll(bizList);
                }
                req.put("subList", subList);
                req.put("subSize", subList.size());
//                getTree2(subList);
            }
            // 加载业务系统
            // if ("department1".equals(type)) {
            // List<Map> bizList = orgManagerMapper.getBizSystemByOrgId(pid);
            // if (null != bizList && !bizList.isEmpty()) {
            // bizList.forEach(biz -> {
            // biz.put("leaf", true);
            // biz.put("icon", "el-icon-share");
            // });
            // }
            // subList.addAll(bizList);
            // }
            // req.put("subList", subList);

        }
    }

    @Override
    public List<OrgManager> getOrgManagerData(String pid, String orgName, String orgType) {
        OrgManagerReq request = new OrgManagerReq();
        request.setPid(pid);
        request.setOrgName(orgName == null ? "" : orgName.trim());
        request.setOrgType(orgType);

        return orgManagerMapper.getOrgManagerData(request);
    }

    @Override
    public OrgManager getWithDepInfo(String dep1, String dep2) {
        OrgManager orgManager = null;
        if(StringUtils.isEmpty(dep2)) {
            OrgManagerReq orgManagerReq = new OrgManagerReq();
            orgManagerReq.setOrgName(dep1);
            List<OrgManager> orgManagerData = orgManagerMapper.getOrgManagerData(orgManagerReq);
            return orgManagerData.isEmpty() ? null : orgManagerData.get(0);
        }
        return orgManagerMapper.getWithDepInfo(dep1, dep2);
    }
}
