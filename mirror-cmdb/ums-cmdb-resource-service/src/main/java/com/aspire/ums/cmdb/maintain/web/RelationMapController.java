package com.aspire.ums.cmdb.maintain.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.aspire.ums.cmdb.common.Constants;
import com.aspire.ums.cmdb.maintain.entity.InstanceRelation;
import com.aspire.ums.cmdb.maintain.entity.ModuleRelation;
import com.aspire.ums.cmdb.maintain.entity.Relation;
import com.aspire.ums.cmdb.maintain.entity.RelationLog;
import com.aspire.ums.cmdb.maintain.service.ConfigLogService;
import com.aspire.ums.cmdb.maintain.service.InstanceRelationService;
import com.aspire.ums.cmdb.maintain.service.InstanceService;
import com.aspire.ums.cmdb.maintain.service.ModuleRelationService;
import com.aspire.ums.cmdb.maintain.service.RelationLogService;
import com.aspire.ums.cmdb.module.entity.Module;
import com.aspire.ums.cmdb.module.mapper.ModuleMapper;
import com.aspire.ums.cmdb.util.BeansUtil;
import com.aspire.ums.cmdb.util.PagedResult;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.github.pagehelper.PageHelper;



@RestController
@RequestMapping("/cmdb/relatiomap")
public class RelationMapController {
    private final Logger logger = Logger.getLogger(getClass());
    @Autowired
	private ModuleRelationService moduleRelationService;
    @Autowired
    private InstanceRelationService instanceRelationService;
    @Autowired
    private ModuleMapper moduleMapper;
    @Autowired
    private InstanceService instanceService;
    @Autowired
    private ConfigLogService configLogService;
    @Autowired
    private RelationLogService relationLogService;
    
    
	//模型关系图
    @SuppressWarnings({ "rawtypes",  "unchecked" })
    @RequestMapping(value="getRelationByCondition")
    public Object getRelationByCondition(ModuleRelation moduleRelation) {
        Module module = moduleMapper.selectByPrimaryKey(moduleRelation.getSourceModuleId());
        List<Map> clist = moduleRelationService.getRetionByCondition(moduleRelation);
        Map<String, Object> map=new HashMap<String, Object>(); 
        if(module!=null){
        Map sm = new HashMap();
        sm.put("sourceModuleName", module.getName()); 
        sm.put("sourceModuleId", module.getId()); 
        sm.put("sourceIconUrl", module.getIconurl()); 
        sm.put("builtin", module.getBuiltin()); 
        map.put("sourceModule", sm);
        }
        if(clist != null && clist.size() > 0){
            map.put("success", true); 
            }else{
            map.put("success", false);
        }
        map.put("dataList", clist);
        return map;
    }
    
    //模型关系新增--模型列表
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @RequestMapping(value="getModuleByCondition")
    public Object getModuleByCondition(ModuleRelation moduleRelation, Integer pageNumber,Integer pageSize) {
        PageHelper.startPage(pageNumber, pageSize);
        PagedResult ps =  BeansUtil.toPagedResult(moduleRelationService.getModuleByCondition(moduleRelation));
        List<Map> clist = (List<Map>) ps.getDataList();
        Map<String, Object> map=new HashMap<String, Object>();  
        if(clist != null && clist.size() > 0){
            map.put("success", true);  
            List<Map> all = new ArrayList();
            List<Map>  clos = new ArrayList();
            int i = 1 ;
            for(Map m: clist){
                clos.add(m);
                if(i % 3 == 0 || i == clist.size()){
                    Map row = new HashMap();
                    row.put("cols", new ArrayList<>(clos));
                    clos.clear();
                    all.add(row);
                    i++;
                    continue;
                }
  
                
                i++;
           }
            map.put("dataList", all);
            map.put("total", ps.getTotal());
            }else{
            map.put("success", false);
        }
 
        return map;
    }
    
    //新增关系类型
    @RequestMapping("/addRelatioType")
    public Object addRelatioType(Relation relation) {
        relation.setId(UUIDUtil.getUUID());
        moduleRelationService.addRelation(relation);
        Map<String, Object> map=new HashMap<String, Object>();  
        map.put("success", true);  
        return map;
    }
    
    //删除关系类型
    @RequestMapping("/delRelatioType")
    public Object delRelatioType(Relation relation) {
        moduleRelationService.delRelation(relation);
        Map<String, Object> map=new HashMap<String, Object>();  
        map.put("success", true);  
        return map;
    }
    
    @RequestMapping("/getAllRelatioType")
    public PagedResult getAllRelatioType() {

//      PageHelper.startPage(pageNumber, pageSize ,"cc.isTop DESC, cc.updateTime DESC ");
        PageHelper.startPage(1, 0);//不执行分页

        return BeansUtil.toPagedResult(moduleRelationService.getAllRelation());
    }
    
    
    @RequestMapping("/addModuleRelation")
    public Object addModuleRelation(String restriction, String relationId, String[] moduleIds, String targetModuleId, String sourceModuleId) {
        Map<String, Object> map=new HashMap<String, Object>(); 
        List<ModuleRelation> inList = new ArrayList<ModuleRelation>();
        for(String moduleId:moduleIds){
            ModuleRelation moduleRelation = new ModuleRelation();
            moduleRelation.setRestriction(restriction);
            moduleRelation.setId(UUIDUtil.getUUID());
            moduleRelation.setBuiltin("false");
            moduleRelation.setRelationId(relationId);
            if(StringUtils.isNotBlank(targetModuleId)){
                moduleRelation.setTargetModuleId(targetModuleId);
                moduleRelation.setSourceModuleId(moduleId);
            }
            if(StringUtils.isNotBlank(sourceModuleId)){
                moduleRelation.setSourceModuleId(sourceModuleId);
                moduleRelation.setTargetModuleId(moduleId);
            }
            inList.add(moduleRelation);

        }
        moduleRelationService.insert(inList);
        map.put("success", true);  
        map.put("dataList", moduleIds);  
        return map;
    }
    
    //模型关系图
    @RequestMapping(value="deleteModuleRelation")
    public Object deleteModuleRelation(ModuleRelation moduleRelation) {
        moduleRelationService.delete(moduleRelation.getId());
        Map<String, Object> map=new HashMap<String, Object>();  
        map.put("success", true); 
        return map;
    }  
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @RequestMapping(value="checkRelationName")
    public Object checkName(String name,String builtin) {
        Map inMap = new HashMap();
        inMap.put("name", name);
        inMap.put("builtin", builtin);
        List<Map> clist = moduleRelationService.checkRelationName(inMap);
        Map<String, Object> map=new HashMap<String, Object>();  
        if(clist != null && clist.size() > 0){
            map.put("success", false);
            map.put("msg", "名称已存在");
        }else{
            map.put("success", true); 
            map.put("msg", "名称不存在");

        }
 
        return map;
    }
    
    
    //实例关系图
    @SuppressWarnings({ "rawtypes",  "unchecked" })
    @RequestMapping(value="getInstanceRelationByCondition")
    public Object getInstanceRelationByCondition(InstanceRelation instanceRelation) {
        List<Map> clist = instanceRelationService.getRetionByCondition(instanceRelation);
        Map<String, Object> map=new HashMap<String, Object>(); 
        
        //实例信息
        Map in = new HashMap();
        in.put("instanceId", instanceRelation.getSourceInstanceId());
        List<Map> inList = instanceService.getInstanceInfoById(in);
        if(inList!=null && inList.size()==1){
            Map sm = new HashMap();
            sm.put("sourceInstanceName", inList.get(0).get("name"));
            sm.put("sourceInstanceId", inList.get(0).get("id")); 
            sm.put("sourceModuleId", inList.get(0).get("moduleId")); 
            sm.put("sourceIconUrl", inList.get(0).get("iconUrl")); 
            map.put("sourceInstance", sm);
        }
 
        if(clist != null && clist.size() > 0){
            map.put("success", true); 
            }else{
            map.put("success", false);
        }
        map.put("dataList", clist);
        return map;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked"})
    @RequestMapping(value="getInstanceRetionByModule")
    public Object getInstanceRetionByModule(String queryInstanceName, String instanceId, String instanceModuleId, String selectModuleId, String checkboxModules, String checkRadioModule
            ,Integer pageNumber,Integer pageSize, String isSelect, String isInit) {
        Map<String, Object> inmap=new HashMap<String, Object>(); 
        inmap.put("instanceId", instanceId);
        inmap.put("instanceModuleId", instanceModuleId);
        inmap.put("selectModuleId", selectModuleId);
        inmap.put("queryInstanceName", queryInstanceName);
//        inmap.put("isSelect", isSelect);
        List<Map> clist = new ArrayList<Map>();
        PagedResult pagedResult = new PagedResult();
        long total = 0;
        
        List<Map> allList = new ArrayList<Map>();
        List<Map> typeList = instanceRelationService.getInstanceRetionType(inmap);
        for(Map m1:typeList){
            if(Constants.MODOULE_RELATION_DIRECT_UP.equals(m1.get("direction")) && StringUtils.isNotBlank((String) m1.get("id"))){
                PageHelper.startPage(pageNumber, pageSize );
                pagedResult = BeansUtil.toPagedResult(instanceRelationService.getInstanceUpRetionByModule(inmap));
                clist = (List<Map>) pagedResult.getDataList();
                total = pagedResult.getTotal();
                
                inmap.remove("queryInstanceName");
                allList = instanceRelationService.getInstanceUpRetionByModule(inmap);
            }else if(Constants.MODOULE_RELATION_DIRECT_DOWN.equals(m1.get("direction")) && StringUtils.isNotBlank((String) m1.get("id"))){
                PageHelper.startPage(pageNumber, pageSize );
                pagedResult = BeansUtil.toPagedResult(instanceRelationService.getInstanceDownRetionByModule(inmap));
                clist = (List<Map>) pagedResult.getDataList();
                total = pagedResult.getTotal();
                
                inmap.remove("queryInstanceName");
                allList = instanceRelationService.getInstanceDownRetionByModule(inmap);
            }

        }
        
        List<String> selectmoduleList =  new ArrayList<String>();
        Map<String, Object> resultMap=new HashMap<String, Object>(); 
        //是否建立关系
        String restriction="";
        //上下级关系
        String direction = "";
        for(Map m: allList){
            //一对多,正反向
            if(StringUtils.isNotBlank((String) m.get("restriction"))){
                restriction = (String) m.get("restriction");
                direction = (String) m.get("direction");
                resultMap.put("restriction", restriction);
                resultMap.put("direction", direction);
                resultMap.put("moduleRelationId", (String) m.get("moduleRelationId"));
                break;
            }
        }
        
        boolean isShowCheckbox = false;
        //单选多选
        if( Constants.MODOULE_RELATION_DIRECT_UP.equals(direction)
                && ( Constants.MODULE_RELATION_MANYTOONE.equals(restriction) || Constants.MODULE_RELATION_MANYTOMANY.equals(restriction))  
                ||  (Constants.MODOULE_RELATION_DIRECT_DOWN.equals(direction) &&  (Constants.MODULE_RELATION_ONETOMANY.equals(restriction) || Constants.MODULE_RELATION_MANYTOMANY.equals(restriction))   )
           ){
            isShowCheckbox = true;
        }else{
            isShowCheckbox = false;
        }
        
        List<Map> tmpList = new ArrayList<Map>(); 
        for(Map sm: allList){
            if(StringUtils.isNotBlank(isInit) && "true".equals(isInit)){
                //取出 实例中已建立关系的
                if(StringUtils.isNotBlank((String) sm.get("sourceInstanceId")) && StringUtils.isNotBlank((String) sm.get("restriction"))){
                    selectmoduleList.add((String) sm.get("id"));
                }
            }else{
                if(isShowCheckbox && checkboxModules!=null){
                    for(String cb:checkboxModules.split(",")){
                        if(cb !=null && cb.equals((String) sm.get("id"))){
                            selectmoduleList.add((String) sm.get("id"));
                            if(StringUtils.isBlank(queryInstanceName) || (StringUtils.isNotBlank(queryInstanceName) && ((String) sm.get("name")).indexOf(queryInstanceName)>0)){
                            tmpList.add(sm);
                            }
                        }
                    }
                }else{
                    if(checkRadioModule!=null && checkRadioModule.equals((String) sm.get("id"))){
                        selectmoduleList.add((String) sm.get("id"));
                        if(StringUtils.isBlank(queryInstanceName) || (StringUtils.isNotBlank(queryInstanceName) && ((String) sm.get("name")).indexOf(queryInstanceName)>0)){
                        tmpList.add(sm);
                        }
                    }
                }
            }
        }
        //前端已选分页
        if(StringUtils.isNotBlank(isSelect) && "true".equals(isSelect)){
            total = tmpList.size();
            int fromIndex = pageSize * (pageNumber - 1);
            int toIndex = fromIndex + pageSize;
            if (toIndex >= tmpList.size()) {
                toIndex = tmpList.size();
            }
            clist = tmpList.subList(fromIndex, toIndex);
        }
        
        //单选多选
        if(isShowCheckbox){
            resultMap.put("isShowCheckbox", true); 
            resultMap.put("checkboxModules", selectmoduleList); 
        }else{
            resultMap.put("checkRadioModule", selectmoduleList.size()>0?selectmoduleList.get(0):"");   
            resultMap.put("isShowCheckbox", false);
        }
        
        List<String> moduleList =  new ArrayList<String>();
        moduleList.addAll(selectmoduleList);

 
        
        resultMap.put("success", true); 
        resultMap.put("dataList", clist);
        resultMap.put("total", total);

        return resultMap;
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @RequestMapping("/addInstanceRelation")
    public Object addInstanceRelation(String restriction, String moduleRelationId, String[] checkboxModules, String checkRadioModule, 
            String instanceId, String isShowCheckbox, String circleId, String direction, String selectModuleId) {
        Map<String, Object> map=new HashMap<String, Object>(); 
        List<String> idS = new ArrayList<String>();
        List<InstanceRelation> inList = new ArrayList<InstanceRelation>();
        //历史记录
        List<InstanceRelation> historyList = new ArrayList<InstanceRelation>();

        
        if("true".equals(isShowCheckbox)){
            if(checkboxModules!=null){
                for(String id:checkboxModules){
                    idS.add(id);
                    InstanceRelation ir = new InstanceRelation();
                    ir.setId(UUIDUtil.getUUID());
                    ir.setCircleId(circleId);
                    ir.setModuleRelationId(moduleRelationId);
                        if("UP".equals(direction)){
                        ir.setSourceInstanceId(id);
                        ir.setTargerInstanceId(instanceId);
                        }else if("DOWN".equals(direction)){
                        ir.setSourceInstanceId(instanceId);
                        ir.setTargerInstanceId(id);   
                        }
                    inList.add(ir);
                }
            }
        }else if("false".equals(isShowCheckbox)){
            if(StringUtils.isNotBlank(checkRadioModule)){
                idS.add(checkRadioModule);
                InstanceRelation ir = new InstanceRelation();
                ir.setId(UUIDUtil.getUUID());
                ir.setCircleId(circleId);
                ir.setModuleRelationId(moduleRelationId);
                    if("UP".equals(direction)){
                    ir.setSourceInstanceId(checkRadioModule);
                    ir.setTargerInstanceId(instanceId);
                    }else if("DOWN".equals(direction)){
                    ir.setSourceInstanceId(instanceId);
                    ir.setTargerInstanceId(checkRadioModule);   
                    }
                inList.add(ir);
            }
        }
        
        //删除list
        List<String> removeList = new ArrayList<String>();
        //删除list
        List<String> addList = new ArrayList<String>();
        Map inMap = new HashMap();
        if("UP".equals(direction)){
            inMap.put("targerInstanceId", instanceId);
            inMap.put("moduleRelationId", moduleRelationId);
            
            try{
                historyList = instanceRelationService.getRelationHistory(inMap);
                List<String> history = new ArrayList<String>();
                for(InstanceRelation i: historyList){
                    if(!idS.contains(i.getSourceInstanceId())){
                        removeList.add(i.getId());
                    }
                    history.add(i.getSourceInstanceId());
                }
                for(InstanceRelation j : inList){
                    if(!history.contains(j.getSourceInstanceId())){
                        addList.add(j.getId());
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
                
            }            
        }else if("DOWN".equals(direction)){
            inMap.put("sourceInstanceId", instanceId);
            inMap.put("moduleRelationId", moduleRelationId);
            
            try{
                historyList = instanceRelationService.getRelationHistory(inMap);
                List<String> history = new ArrayList<String>();
                for(InstanceRelation i: historyList){
                    if(!idS.contains(i.getTargerInstanceId())){
                        removeList.add(i.getId());
                    }
                    history.add(i.getTargerInstanceId());
                }
                for(InstanceRelation j : inList){
                    if(!history.contains(j.getTargerInstanceId())){
                        addList.add(j.getId());
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }  
        }
        
        instanceRelationService.addInstanceRelation(inList, inMap);
        

        //写日志
        try{
            
            for(String ii: addList){
                Map inmap = new HashMap();
                inmap.put("sourceInstanceId", instanceId);
                inmap.put("moduleRelationId", ii);
                List<Map> outList = configLogService.getRelationInfoList(inmap);
                if(outList!=null && outList.size()>0){

                    RelationLog relationLog = new RelationLog();
                    relationLog.setAction(Constants.LOG_ACTION_TYPE_ADDINSTANCE_RELATION_NAME);
                    relationLog.setCircleId(circleId);
                    relationLog.setId(UUIDUtil.getUUID());
                    relationLog.setInstanceId(instanceId);

                    
                    if(!instanceId.equals((String) outList.get(0).get("sourceInstanceId"))){
                        relationLog.setName((String) outList.get(0).get("targetInstanceName"));
                        relationLog.setTargetName((String) outList.get(0).get("sourceInstanceName"));
                        }else{
                            relationLog.setName((String) outList.get(0).get("sourceInstanceName"));
                            relationLog.setTargetName((String) outList.get(0).get("targetInstanceName")); 
                   }
                    relationLog.setRelationName((String) outList.get(0).get("relationName"));
                    
                    relationLogService.insert(relationLog);
                    
                }
            }
            
            for(String jj: removeList){
                Map inmap = new HashMap();
                inmap.put("sourceInstanceId", instanceId);
                inmap.put("moduleRelationId", jj);
                List<Map> outList = configLogService.getRelationInfoList(inmap);
                if(outList!=null && outList.size()>0){
                    
                    RelationLog relationLog = new RelationLog();
                    relationLog.setAction(Constants.LOG_ACTION_TYPE_DELINSTANCE_RELATION_NAME);
                    relationLog.setCircleId(circleId);
                    relationLog.setId(UUIDUtil.getUUID());
                    relationLog.setInstanceId(instanceId);
                    
                    if(!instanceId.equals((String) outList.get(0).get("sourceInstanceId"))){
                        relationLog.setName((String) outList.get(0).get("targetInstanceName"));
                        relationLog.setTargetName((String) outList.get(0).get("sourceInstanceName"));
                        }else{
                            relationLog.setName((String) outList.get(0).get("sourceInstanceName"));
                            relationLog.setTargetName((String) outList.get(0).get("targetInstanceName")); 
                    }
                    relationLog.setRelationName((String) outList.get(0).get("relationName"));
                    relationLogService.insert(relationLog);

                }
            }
        }catch(Exception e){
            logger.error("实例编辑关系日志写入失败！addList:[" + addList.toString() + "]romoveList:["+ removeList.toString() + "]", e);
            e.printStackTrace();
        }
        
        map.put("success", true);  
        map.put("dataList", idS);  
        return map;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @RequestMapping(value="checkInstanceRelation")
    public Object checkInstanceRelation(String instanceIds,String targetModuleId,String targetInstanceId,String restriction,String direction) {
        Map<String, Object> map=new HashMap<String, Object>();  
        if(( ("UP".equals(direction) && ("ManyToMany".equals(restriction) || "OneToMany".equals(restriction))) ||
                ("DOWN".equals(direction) && ("ManyToOne".equals(restriction) || "ManyToMany".equals(restriction)) ))){
            map.put("success", true); 
            return map;
        }
        Map inMap = new HashMap();
        inMap.put("instanceIds", instanceIds.split(","));
        inMap.put("targetModuleId", targetModuleId);
        inMap.put("targetInstanceId", targetInstanceId);
        List<Map> clist = instanceRelationService.checkInstanceRelation(inMap);

        if(clist != null && clist.size() > 0 ){
            String erroMsg = "";
            for(Map m:clist){
                erroMsg+=(m.get("sname") + "已经和" + m.get("tname") + "建立联系; \n ");
            }
            map.put("success", false);
            map.put("erroMsg", erroMsg);
       }else{
            map.put("success", true); 
        }
 
        return map;
    }
    
    //模型关系图
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @RequestMapping(value="deleteInstanceRelation")
    public Object deleteInstanceRelation(String  id, String  sourceInstanceId, String  circleId) {
        Map inmap = new HashMap();
        inmap.put("sourceInstanceId", sourceInstanceId);
        inmap.put("moduleRelationId", id);
        List<Map> outList = configLogService.getRelationInfoList(inmap);
        
        instanceRelationService.delete(id);
        
        //写日志
        try{
                if(outList!=null && outList.size()>0){
                RelationLog relationLog = new RelationLog();
                relationLog.setAction(Constants.LOG_ACTION_TYPE_DELINSTANCE_RELATION_NAME);
                relationLog.setCircleId(circleId);
                relationLog.setId(UUIDUtil.getUUID());
                relationLog.setInstanceId(sourceInstanceId);
                if(!sourceInstanceId.equals((String) outList.get(0).get("sourceInstanceId"))){
                relationLog.setName((String) outList.get(0).get("targetInstanceName"));
                relationLog.setTargetName((String) outList.get(0).get("sourceInstanceName"));
                }else{
                    relationLog.setName((String) outList.get(0).get("sourceInstanceName"));
                    relationLog.setTargetName((String) outList.get(0).get("targetInstanceName")); 
                }
                relationLog.setRelationName((String) outList.get(0).get("relationName"));
                relationLogService.insert(relationLog);
                }
        }catch(Exception e){
            logger.error("实例删除关系日志写入失败！id:[" + sourceInstanceId + "]", e);
            e.printStackTrace();
        }
        
        Map<String, Object> map=new HashMap<String, Object>();  
        map.put("success", true); 
        return map;
    }  
    
}
