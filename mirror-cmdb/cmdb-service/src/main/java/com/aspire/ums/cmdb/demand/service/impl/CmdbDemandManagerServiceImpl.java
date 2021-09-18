package com.aspire.ums.cmdb.demand.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.demand.entity.*;
import com.aspire.ums.cmdb.demand.mapper.CmdbDemandChangedMapper;
import com.aspire.ums.cmdb.demand.mapper.CmdbDemandManagerMapper;
import com.aspire.ums.cmdb.demand.mapper.CmdbDemandResourceTypeMapper;
import com.aspire.ums.cmdb.demand.mapper.CmdbDemandResourceTypeValueMapper;
import com.aspire.ums.cmdb.demand.service.ICmdbDemandManagerService;
import com.aspire.ums.cmdb.util.DateUtils;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.*;

/**
* 描述：
* @author
* @date 2019-05-09 16:28:19
*/
@Slf4j
@Service
public class CmdbDemandManagerServiceImpl implements ICmdbDemandManagerService {

    @Autowired
    private CmdbDemandManagerMapper mapper;
    @Autowired
    private CmdbDemandResourceTypeValueMapper valueMapper;
    @Autowired
    private CmdbDemandResourceTypeMapper typeMapper;
    @Autowired
    private CmdbDemandChangedMapper changedMapper;
    /**
     * 查询单个实例
     * @param id
     * @return
     */
    public CmdbDemandManager getById(String id){
        CmdbDemandManager entity = new CmdbDemandManager();
        entity.setDemandId(id);
        return mapper.get(entity);
    }

    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    public List<CmdbDemandManager> list() {
        return mapper.list();
    }

    /**
     * 列表查询
     * @param queryMap 查询条件
     * @return
     */
    @Override
    public JSONObject queryForMap(Map<String, Object> queryMap) {
        StringBuilder sqlBuilder = new StringBuilder();
        List<CmdbDemandResourceType> typeList = typeMapper.list();
        for (CmdbDemandResourceType type : typeList) {
            if (StringUtils.isEmpty(type.getParentTypeId())) {
                continue;
            }
            sqlBuilder.append("max(CASE when rtv.resource_type_id = ")
                    .append(type.getResourceTypeId())
                    .append(" then rtv.resource_count else '' end) ")
                    .append(type.getResourceCode()).append(",");
            sqlBuilder.append("max(CASE when rtv.resource_type_id = ")
                    .append(type.getResourceTypeId())
                    .append(" then rtv.resource_scene else '' end) ")
                    .append(type.getResourceCode()).append("_scene").append(",");
            sqlBuilder.append("max(CASE when rtv.resource_type_id = ")
                    .append(type.getResourceTypeId())
                    .append(" then rtv.resource_use else '' end) ")
                    .append(type.getResourceCode()).append("_use").append(",");
        }
        Integer pageSize = Integer.parseInt(queryMap.get("pageSize").toString());
        Integer currPage = Integer.parseInt(queryMap.get("currPage").toString());
        queryMap.put("limitMin", Integer.valueOf((currPage-1) * pageSize));
        queryMap.put("limitMax", Integer.valueOf(pageSize));
        queryMap.put("caseWhenSql", sqlBuilder.toString());
        List<LinkedHashMap> list = mapper.queryForMap(queryMap);
        Integer totalCount = mapper.queryForMapCount(queryMap);
        JSONObject returnJson = new JSONObject();
        returnJson.put("total", totalCount);
        returnJson.put("dataList", list);
        return returnJson;
    }
    
    /**
     * 查询导出数据
     * @param queryMap
     * @return
     */
    @Override
    public JSONObject queryExportData(Map<String, Object> queryMap) {
        StringBuilder sqlBuilder = new StringBuilder();
        List<CmdbDemandResourceType> typeList = typeMapper.list();
        for (CmdbDemandResourceType type : typeList) {
            if (StringUtils.isEmpty(type.getParentTypeId())) {
                continue;
            }
            sqlBuilder.append("max(CASE when rtv.resource_type_id = ")
                    .append(type.getResourceTypeId())
                    .append(" then rtv.resource_count else '' end) ")
                    .append(type.getResourceCode()).append(",");
            sqlBuilder.append("max(CASE when rtv.resource_type_id = ")
                    .append(type.getResourceTypeId())
                    .append(" then rtv.resource_scene else '' end) ")
                    .append(type.getResourceCode()).append("_scene").append(",");
            sqlBuilder.append("max(CASE when rtv.resource_type_id = ")
                    .append(type.getResourceTypeId())
                    .append(" then rtv.resource_use else '' end) ")
                    .append(type.getResourceCode()).append("_use").append(",");
        }
        queryMap.put("caseWhenSql", sqlBuilder.toString());
        List<LinkedHashMap> list = mapper.queryExportData(queryMap);
        Integer totalCount = mapper.queryExportDataCount(queryMap);
        JSONObject returnJson = new JSONObject();
        returnJson.put("total", totalCount);
        returnJson.put("dataList", list);
        return returnJson;
    }
    
    /**
     * 获取表格头
     * @return
     */
    @Override
    public List<Map<String, Object>> getTableHeader() {
        List<Map<String, Object>> headerList = new LinkedList<>();
        String[] headerKey = new String[]{"demand_id","department","tenant","tenant_phone","biz_system","is_project","project_time","submit_time","cycle_time",
                "is_host_maintenance","is_disaster","disaster_type","wlan_requirement","commission_time","is_idc_requirement","idc_requirement"};
        String[] labelKey = new String[]{"ID","部门","需求接口人","需求接口人电话","应用系统","是否立项","立项时间","资源需求提出时间","需求满足周期",
                "是否主机代维","是否有容灾高可用","容灾类型","宽带要求","资源预期投产时间","部署资源池要求","资源池要求"};
        List<Map<String, String>> basicList = new LinkedList<>();
        for (int i=0; i < headerKey.length; i ++) {
            Map<String, String> basic = new HashMap<>();
            basic.put("key", headerKey[i]);
            basic.put("label", labelKey[i]);
            basicList.add(basic);
        }
        Map<String, Object> basicMap = new LinkedHashMap<>();
        basicMap.put("key", "basic_");
        basicMap.put("label", "基础信息");
        basicMap.put("data", basicList);
        headerList.add(basicMap);

        List<CmdbDemandResourceType> typeList = typeMapper.getTypeList("");
        for (CmdbDemandResourceType type : typeList) {
            List<CmdbDemandResourceType> childList = typeMapper.getTypeList(type.getResourceTypeId());
            List<Map<String, String>> subHeaderList = new LinkedList<>();
            for (CmdbDemandResourceType child : childList) {
                Map<String, String> typeMap = new LinkedHashMap<>();
                typeMap.put("key", child.getResourceCode());
                typeMap.put("label", child.getResourceType());
                subHeaderList.add(typeMap);
                Map<String, String> sceneMap = new LinkedHashMap<>();
                sceneMap.put("key", child.getResourceCode() + "_scene");
                sceneMap.put("label", child.getResourceType() + "使用场景描述");
                subHeaderList.add(sceneMap);
                Map<String, String> useMap = new LinkedHashMap<>();
                useMap.put("key", child.getResourceCode() + "_use");
                useMap.put("label", child.getResourceType() + "资源用途");
                subHeaderList.add(useMap);
            }
            Map<String, Object> subHeadMap = new LinkedHashMap<>();
            subHeadMap.put("key", type.getResourceCode());
            subHeadMap.put("label", type.getResourceType());
            subHeadMap.put("data", subHeaderList);
            headerList.add(subHeadMap);
        }
        return headerList;
    }
    
    public List<Map<String, Object>> getTypeAndValue() {
        List<Map<String, Object>> headerList = new LinkedList<>();
        // 获取所有的大分类
        List<CmdbDemandResourceType> list = typeMapper.getResourceOwnerList();
        final List<String> ownerList = new LinkedList<>();
        if (list != null && list.size() > 0) {
            list.forEach((owner) -> {
                if (!ownerList.contains(owner.getResourceOwner())) {
                    ownerList.add(owner.getResourceOwner());
                }
            });
        }
        for (String owner : ownerList) {
            // 加载分类下面的所有小分类
            Map<String, Object> ownerMap = new LinkedHashMap<>();
            ownerMap.put("label", owner);
            List<CmdbDemandResourceType> typeList = typeMapper.getTypeListByOwner(owner);
            List<Map<String, Object>> ownerResourceList = new LinkedList<>();
            for (CmdbDemandResourceType type : typeList) {
                Map<String, Object> map = new HashMap<>();
                map.put("label",type.getResourceType());
                map.put("key",type.getResourceCode());
                List<Map<String,String>> childList = typeMapper.getTypeAndValueList(type.getResourceTypeId());
                map.put("data", childList);
                ownerResourceList.add(map);
            }
            ownerMap.put("data", ownerResourceList);
            headerList.add(ownerMap);
        }
        return headerList;
    }

    @Override
    public InsertDemandEntity getDemandInfoId(String demandId) {
        CmdbDemandManager queryEntity = new CmdbDemandManager();
        queryEntity.setDemandId(demandId);
        InsertDemandEntity returnEntity = new InsertDemandEntity();
        returnEntity.setDemandManager(mapper.get(queryEntity));
        returnEntity.setResourceTypeValueList(valueMapper.listByDemandId(demandId));
        returnEntity.setChangedList(changedMapper.listByDemandId(demandId));
        return returnEntity;
    }
    
    
    
    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回指定ID的数据信息
     */
    public CmdbDemandManager get(CmdbDemandManager entity) {
        return mapper.get(entity);
    }

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class, SQLException.class})
    public void insert(InsertDemandEntity entity) {
        CmdbDemandManager demandManager = entity.getDemandManager();
        String demandId = UUIDUtil.getUUID();
        demandManager.setDemandId(demandId);
        List<CmdbDemandResourceType> typeList = typeMapper.getTypeList("");
        List<CmdbDemandResourceTypeValue> valueList = entity.getResourceTypeValueList();
        for (CmdbDemandResourceType type : typeList) {
            List<CmdbDemandResourceType> childList = typeMapper.getTypeList(type.getResourceTypeId());
            if (childList != null && childList.size() > 0) {
                for (CmdbDemandResourceType child : childList) {
                    CmdbDemandResourceTypeValue typeValue = null;
                    if (valueList != null && valueList.size() > 0) {
                        for (CmdbDemandResourceTypeValue value : valueList) {
                            if (child.getResourceTypeId().equals(value.getResourceTypeId())) {
                                typeValue = value;
                                break;
                            }
                        }
                    }
                    if (typeValue == null) {
                        typeValue = new CmdbDemandResourceTypeValue();
                    }
                    typeValue.setDemandId(demandId);
                    typeValue.setResourceTypeId(child.getResourceTypeId());
                    valueMapper.insert(typeValue);
                }
            }
        }
        mapper.insert(demandManager);
    }

    @Transactional(rollbackFor = {Exception.class, RuntimeException.class, SQLException.class})
    public void insertExcelData(CmdbDemandManager demandManager, List<CmdbDemandResourceTypeValue> valueList) {
        String demandId = UUIDUtil.getUUID();
        demandManager.setDemandId(demandId);

        for (CmdbDemandResourceTypeValue value : valueList) {
            value.setDemandId(demandId);
            try {
                valueMapper.insert(value);
            } catch (Exception e) {
                log.info(demandManager + " --> " + value);
            }
        }
        mapper.insert(demandManager);
    }

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    public void update(InsertDemandEntity entity) {
        try {
    
            CmdbDemandManager demandManager = entity.getDemandManager();
            String demandId = demandManager.getDemandId();
            CmdbDemandManager old = mapper.get(demandManager);
            mapper.update(demandManager);
            List<CmdbDemandResourceTypeValue> oldValueList = valueMapper.listByDemandId(demandId);
            List<CmdbDemandResourceTypeValue> valueList = entity.getResourceTypeValueList();
            if (valueList != null && valueList.size() > 0) {
                for (CmdbDemandResourceTypeValue value : valueList) {
                    valueMapper.update(value);
                }
            }
            String currentDate = DateUtils.getCurrentDate("yyyy-MM-dd HH:mm:ss");
            String updateUser = entity.getUserName() == null ? "" : entity.getUserName();
            List<CmdbDemandChanged> changedList = new ArrayList<CmdbDemandChanged>();
            List<CmdbDemandChanged> changedBase = compareBase(old,demandManager);
            changedList.addAll(changedBase);
            List<CmdbDemandChanged> changedType = compareTypeValue(oldValueList,valueList);
            changedList.addAll(changedType);
            log.info("需求收集变更list --> {}",changedList);
            if (null != changedList && !changedList.isEmpty()){
                for (CmdbDemandChanged c : changedList) {
                    c.setDemandId(demandId);
                    c.setUpdateTime(currentDate);
                    c.setUpdateUser(updateUser);
                    changedMapper.insertChanged(c);
                }
            }
        } catch (Exception e) {
            log.error("[ERROR] >>> " + e.toString());
        }
    }

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    public void delete(CmdbDemandManager entity) {
        mapper.delete(entity);
    }
    
    private List<CmdbDemandChanged> compareBase(CmdbDemandManager oldData,CmdbDemandManager newData) {
        List<CmdbDemandChanged> result = new ArrayList<>();
        if (null != oldData && null != newData) {
            if (!(oldData.getDepartment() == null ? "" : oldData.getDepartment()).equals(newData.getDepartment() == null ? "" : newData.getDepartment())) {
                CmdbDemandChanged changed = new CmdbDemandChanged();
                changed.setName("部门");
                changed.setOldVal(oldData.getDepartment());
                changed.setNewVal(newData.getDepartment());
                result.add(changed);
            }
            if (!(oldData.getTenant() == null ? "" : oldData.getTenant()).equals(newData.getTenant() == null ? "" : newData.getTenant())) {
                CmdbDemandChanged changed = new CmdbDemandChanged();
                changed.setName("需求接口人");
                changed.setOldVal(oldData.getTenant());
                changed.setNewVal(newData.getTenant());
                result.add(changed);
            }
            if (!(oldData.getTenantPhone() == null ? "" : oldData.getTenantPhone()).equals(newData.getTenantPhone() == null ? "" : newData.getTenantPhone())) {
                CmdbDemandChanged changed = new CmdbDemandChanged();
                changed.setName("需求接口人电话");
                changed.setOldVal(oldData.getTenantPhone());
                changed.setNewVal(newData.getTenantPhone());
                result.add(changed);
            }
            if (!(oldData.getBizSystem() == null ? "" : oldData.getBizSystem()).equals(newData.getBizSystem() == null ? "" : newData.getBizSystem())) {
                CmdbDemandChanged changed = new CmdbDemandChanged();
                changed.setName("应用系统");
                changed.setOldVal(oldData.getBizSystem());
                changed.setNewVal(newData.getBizSystem());
                result.add(changed);
            }
            if (!(oldData.getIsProject() == null ? "" : oldData.getIsProject()).equals(newData.getIsProject() == null ? "" : newData.getIsProject())) {
                CmdbDemandChanged changed = new CmdbDemandChanged();
                changed.setName("是否立项");
                changed.setOldVal(oldData.getIsProject());
                changed.setNewVal(newData.getIsProject());
                result.add(changed);
            }
            if (!(oldData.getProjectTime() == null ? "" : oldData.getProjectTime()).equals(newData.getProjectTime() == null ? "" : newData.getProjectTime())) {
                CmdbDemandChanged changed = new CmdbDemandChanged();
                changed.setName("立项时间");
                changed.setOldVal(oldData.getProjectTime());
                changed.setNewVal(newData.getProjectTime());
                result.add(changed);
            }
            if (!(oldData.getSubmitTime() == null ? "" : oldData.getSubmitTime()).equals(newData.getSubmitTime() == null ? "" : newData.getSubmitTime())) {
                CmdbDemandChanged changed = new CmdbDemandChanged();
                changed.setName("资源需求提出时间");
                changed.setOldVal(oldData.getSubmitTime());
                changed.setNewVal(newData.getSubmitTime());
                result.add(changed);
            }
            if (!(oldData.getIsHostMaintenance() == null ? "" : oldData.getIsHostMaintenance()).equals(newData.getIsHostMaintenance() == null ? "" : newData.getIsHostMaintenance())) {
                CmdbDemandChanged changed = new CmdbDemandChanged();
                changed.setName("是否需主机代维");
                changed.setOldVal(oldData.getIsHostMaintenance());
                changed.setNewVal(newData.getIsHostMaintenance());
                result.add(changed);
            }
            if (!(oldData.getIsDisaster() == null ? "" : oldData.getIsDisaster()).equals(newData.getIsDisaster() == null ? "" : newData.getIsDisaster())) {
                CmdbDemandChanged changed = new CmdbDemandChanged();
                changed.setName("有无容灾高可用");
                changed.setOldVal(oldData.getIsDisaster());
                changed.setNewVal(newData.getIsDisaster());
                result.add(changed);
            }
            if (!(oldData.getDisasterType() == null ? "" : oldData.getDisasterType()).equals(newData.getDisasterType() == null ? "" : newData.getDisasterType())) {
                CmdbDemandChanged changed = new CmdbDemandChanged();
                changed.setName("容灾高可用类型");
                changed.setOldVal(oldData.getDisasterType());
                changed.setNewVal(newData.getDisasterType());
                result.add(changed);
            }
            if (!(oldData.getWlanRequirement() == null ? "" : oldData.getWlanRequirement()).equals(newData.getWlanRequirement() == null ? "" : newData.getWlanRequirement())) {
                CmdbDemandChanged changed = new CmdbDemandChanged();
                changed.setName("带宽要求");
                changed.setOldVal(oldData.getWlanRequirement());
                changed.setNewVal(newData.getWlanRequirement());
                result.add(changed);
            }
            if (!(oldData.getCommissionTime() == null ? "" : oldData.getCommissionTime()).equals(newData.getCommissionTime() == null ? "" : newData.getCommissionTime())) {
                CmdbDemandChanged changed = new CmdbDemandChanged();
                changed.setName("资源预期投产时间");
                changed.setOldVal(oldData.getCommissionTime());
                changed.setNewVal(newData.getCommissionTime());
                result.add(changed);
            }
            if (!(oldData.getIsIdcRequirement() == null ? "" : oldData.getIsIdcRequirement()).equals(newData.getIsIdcRequirement() == null ? "" : newData.getIsIdcRequirement())) {
                CmdbDemandChanged changed = new CmdbDemandChanged();
                changed.setName("部署资源池要求");
                changed.setOldVal(oldData.getIsIdcRequirement());
                changed.setNewVal(newData.getIsIdcRequirement());
                result.add(changed);
            }
            if (!(oldData.getIdcRequirement() == null ? "" : oldData.getIdcRequirement()).equals(newData.getIdcRequirement() == null ? "" : newData.getIdcRequirement())) {
                CmdbDemandChanged changed = new CmdbDemandChanged();
                changed.setName("资源池要求");
                changed.setOldVal(oldData.getIdcRequirement());
                changed.setNewVal(newData.getIdcRequirement());
                result.add(changed);
            }
            if (!(oldData.getCycleTime() == null ? "" : oldData.getCycleTime()).equals(newData.getCycleTime() == null ? "" : newData.getCycleTime())) {
                CmdbDemandChanged changed = new CmdbDemandChanged();
                changed.setName("需求满足周期");
                changed.setOldVal(oldData.getCycleTime());
                changed.setNewVal(newData.getCycleTime());
                result.add(changed);
            }
            
        }
        return result;
    }
    
    private List<CmdbDemandChanged> compareTypeValue(List<CmdbDemandResourceTypeValue> oldData,List<CmdbDemandResourceTypeValue> newData) {
        List<CmdbDemandChanged> result = new ArrayList<>();
        if (null != oldData && null != newData) {
            for (CmdbDemandResourceTypeValue oldVal : oldData) {
                for (CmdbDemandResourceTypeValue newVal : newData) {
                    if (oldVal.getResourceTypeId().equals(newVal.getResourceTypeId())) {
                        if (!(oldVal.getResourceCount() == null ? "" : oldVal.getResourceCount()).equals(newVal.getResourceCount() == null ? "" : newVal.getResourceCount())) {
                            CmdbDemandChanged changed = new CmdbDemandChanged();
                            CmdbDemandResourceType typeVo = new CmdbDemandResourceType();
                            typeVo.setResourceTypeId(oldVal.getResourceTypeId());
                            CmdbDemandResourceType typeEntity = typeMapper.get(typeVo);
                            changed.setName(typeEntity.getResourceType());
                            changed.setOldVal(oldVal.getResourceCount());
                            changed.setNewVal(newVal.getResourceCount());
                            result.add(changed);
                        }
                        if (!(oldVal.getResourceScene() == null ? "" : oldVal.getResourceScene()).equals(newVal.getResourceScene() == null ? "" : newVal.getResourceScene())) {
                            CmdbDemandChanged changed = new CmdbDemandChanged();
                            CmdbDemandResourceType typeVo = new CmdbDemandResourceType();
                            typeVo.setResourceTypeId(oldVal.getResourceTypeId());
                            CmdbDemandResourceType typeEntity = typeMapper.get(typeVo);
                            changed.setName(typeEntity.getResourceType()+"使用场景描述");
                            changed.setOldVal(oldVal.getResourceScene());
                            changed.setNewVal(newVal.getResourceScene());
                            result.add(changed);
                        }
                        if (!(oldVal.getResourceUse() == null ? "" : oldVal.getResourceUse()).equals(newVal.getResourceUse() == null ? "" : newVal.getResourceUse())) {
                            CmdbDemandChanged changed = new CmdbDemandChanged();
                            CmdbDemandResourceType typeVo = new CmdbDemandResourceType();
                            typeVo.setResourceTypeId(oldVal.getResourceTypeId());
                            CmdbDemandResourceType typeEntity = typeMapper.get(typeVo);
                            changed.setName(typeEntity.getResourceType()+"资源用途");
                            changed.setOldVal(oldVal.getResourceUse());
                            changed.setNewVal(newVal.getResourceUse());
                            result.add(changed);
                        }
                    }
                }
            }
        }
        return result;
    }
}