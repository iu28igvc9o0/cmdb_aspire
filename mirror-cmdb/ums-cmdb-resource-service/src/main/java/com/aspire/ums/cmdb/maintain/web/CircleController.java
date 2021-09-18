package com.aspire.ums.cmdb.maintain.web;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.aspire.ums.cmdb.common.Constants;
import com.aspire.ums.cmdb.maintain.entity.Circle;
import com.aspire.ums.cmdb.maintain.entity.ColumnFilter;
import com.aspire.ums.cmdb.maintain.entity.ConfigLog;
import com.aspire.ums.cmdb.maintain.entity.DynamicInstanceColumn;
import com.aspire.ums.cmdb.maintain.entity.FormValue;
import com.aspire.ums.cmdb.maintain.entity.MaintainView;
import com.aspire.ums.cmdb.maintain.service.CircleService;
import com.aspire.ums.cmdb.maintain.service.ColumnFilterService;
import com.aspire.ums.cmdb.maintain.service.ConfigLogService;
import com.aspire.ums.cmdb.maintain.service.FormValueService;
import com.aspire.ums.cmdb.maintain.service.InstanceService;
import com.aspire.ums.cmdb.maintain.service.MaintainViewService;
import com.aspire.ums.cmdb.module.entity.FormOptions;
import com.aspire.ums.cmdb.util.BeansUtil;
import com.aspire.ums.cmdb.util.PagedResult;
import com.aspire.ums.cmdb.util.StringTemplateUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.github.pagehelper.PageHelper;


@RestController
@RequestMapping("/cmdb/circle")
public class CircleController {
    private final Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private CircleService circleService;
	
   @Autowired
    private MaintainViewService maintainViewService;
   
   @Autowired
   private FormValueService formValueService;
   
   @Autowired
   private InstanceService instanceService;
   
   @Autowired
   private ConfigLogService configLogService;
   
   @Autowired
   private ColumnFilterService columnFilterService;

	
    @RequestMapping("/getCircles")
	public PagedResult getCircles(Circle circle, Integer pageNumber,Integer pageSize) {
	    PageHelper.startPage(pageNumber, pageSize);
//        PageHelper.startPage(1, 0);//不执行分页

		return BeansUtil.toPagedResult(circleService.getAll(circle));
	}
    
    @RequestMapping("/getAllCircles")
    public PagedResult getAllCircles(Circle circle, Integer pageNumber,Integer pageSize) {
        PageHelper.startPage(1, 0);//不执行分页

        return BeansUtil.toPagedResult(circleService.getAll(circle));
    }
	
    @RequestMapping("/getCircle")
    public Circle getCircle(String id) {
        Circle circle=circleService.getOne(id);
        return circle;
    }
    
    @RequestMapping("/add")
    public Object save(Circle circle) {
        circle.setId(UUIDUtil.getUUID());
        circleService.insert(circle);
        Map<String, Object> map=new HashMap<String, Object>();  
        map.put("success", true);  

        return map;
    }
    
    @RequestMapping(value="update")
    public Object update(Circle circle) {
        circleService.update(circle);
        Map<String, Object> map=new HashMap<String, Object>();  
        map.put("success", true);  
        return map;
    }
    
    @RequestMapping(value="/delete")
    public Object delete(Circle circle) {
        circle.setIsDelete(1);
        circleService.delete(circle);

        Map<String, Object> map=new HashMap<String, Object>();  
        map.put("success", true);  
        return map;
    }
   
    @RequestMapping(value="goHead")
    public Object goHead(Circle circle) {
        if(circle.getIsTop()==1){
            circle.setIsTop(0);
        }else{
            circle.setIsTop(1);
        }
        circleService.update(circle);
        Map<String, Object> map=new HashMap<String, Object>();  
        map.put("success", true);  
        return map;
    }
    
    @RequestMapping(value="checkName")
    public Object checkName(Circle circle) {
        List<Circle> clist = circleService.getByCondition(circle);
        Map<String, Object> map=new HashMap<String, Object>();  
        if(clist != null && clist.size() > 0){
            map.put("success", false);}else{
            map.put("success", true); 
        }
 
        return map;
    }
    
    @RequestMapping(value="checkViewName")
    public Object checkViewName(MaintainView maintainView) {
        List<MaintainView> clist = maintainViewService.getByCondition(maintainView);
        Map<String, Object> map=new HashMap<String, Object>();  
        if(clist != null && clist.size() > 0){
            map.put("success", false);
        }else{
            map.put("success", true); 
        }
 
        return map;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @RequestMapping("/getModuleTree")
    public List<Map> getModuleTree() {
        Map<String,Object>  in = new HashMap<String,Object>();
        List<Map> buildmodule = maintainViewService.getModule(in);
        
        for(Map m:buildmodule){
            in.put("parentId", m.get("id"));
            List<Map> itemmodule = maintainViewService.getModule(in);
            m.put("item", itemmodule);
        }
        return buildmodule;
    }
   
    
    @RequestMapping("/addMaintainView")
    public Object addMaintainView(MaintainView maintainView, String[] moduleIds) {
        maintainViewService.insert(maintainView, moduleIds);
        Map<String, Object> map=new HashMap<String, Object>();  
        map.put("success", true);  
        return map;
    }
    
    @RequestMapping(value="/deleteMaintainView")
    public Object deleteMaintainView(MaintainView maintainView) {
        maintainViewService.delete(maintainView.getId());
        Map<String, Object> map=new HashMap<String, Object>();  
        map.put("success", true);  
        return map;
    }
    
    @RequestMapping("/editMaintainView")
    public Object editMaintainView(MaintainView maintainView, String[] moduleIds) {
        maintainViewService.update(maintainView, moduleIds);

        Map<String, Object> map=new HashMap<String, Object>();  
        map.put("success", true);  
        return map;
    }
   
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @RequestMapping(value="/getMaintainView")
    public Object getMaintainView(MaintainView maintainView) {
        Map idsMap = new HashMap();
        idsMap.put("viewId", maintainView.getId());
        List<Map> modules = maintainViewService.getModuleIds(idsMap);
        
        Map formMap = new  HashMap();
        List<String> moduleIds = new ArrayList<String>();
        for(Map m: modules){
            if( m.get("id")!=null){
            moduleIds.add((String) m.get("id"));
            }
            formMap.put("name", (String) m.get("viewName"));
        }
        formMap.put("moduleIds", moduleIds);
        
        Map<String, Object> map=new HashMap<String, Object>();  
        map.put("ruleForm", formMap);
        map.put("success", true);  
        return map;
    }
    
    @SuppressWarnings("rawtypes")
    @RequestMapping("/getMaintainViewMenu")
    public Map getMaintainViewMenu(MaintainView maintainView) {
        List<MaintainView> clist = maintainViewService.getByCondition(maintainView);
        Map<String, Object> map=new HashMap<String, Object>();  
        map.put("dataList", clist); 
        map.put("success", true); 
        return map;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @RequestMapping("/getInstanceByView")
    public Object getInstanceByView(String circleId, String defaultView, String viewId, String name, String moduleId, String tag, 
            String insertStartTime, String insertEndTime, String updateStartTime, String updateEndTime, 
            Integer pageNumber,Integer pageSize, String sort, String order) {

            PageHelper.startPage(pageNumber, pageSize ,(sort==null?"insertTime":sort)+" "+(order==null?"desc":order));
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("circleId", circleId); 
            map.put("defaultView", defaultView); 
            map.put("viewId", viewId); 
            map.put("name", name);
            map.put("moduleId", moduleId);
            map.put("tagName", tag);
            map.put("insertStartTime", insertStartTime);
            map.put("insertEndTime", insertEndTime);
            map.put("updateStartTime", updateStartTime);
            map.put("updateEndTime", updateEndTime);
            
            PagedResult pr = BeansUtil.toPagedResult(instanceService.getDynamicViewInstanceColumn(map));
            
            List<DynamicInstanceColumn> clist =  (List<DynamicInstanceColumn>) pr.getDataList();
            List<Map> resultList =  new ArrayList<Map>();
            
            Map<String, Object> resultMap=new HashMap<String, Object>(); 
            List<Map> columList = new ArrayList<Map>(); 
            
            Map idsMap = new HashMap();
            idsMap.put("viewId", viewId);
            List<Map> modules = maintainViewService.getModuleIds(idsMap);
            if(modules != null && modules.size() == 1){
                    //基础属性列
                    Field[] fields=new DynamicInstanceColumn().getClass().getDeclaredFields();
                    String[] fieldNames=getFiledName(new DynamicInstanceColumn());  
                    for(int i=0;i<fields.length;i++){  
                        fieldNames[i]=fields[i].getName();  
                    } 
                    
                    //自定义列
                    Map inMap = new HashMap();
                    inMap.put("moduleId", modules.get(0).get("id"));
                    List<FormValue> columns = formValueService.getFormValuesByModuleId(inMap);
                    for(FormValue col: columns){
                        if(columnFilter(col)){
                                Map columMap = new HashMap();
                                columMap.put("code", "key"+col.getForm().getCode());
                                columMap.put("name", col.getForm().getName());
                                columList.add(columMap);
                        }
                    }
                    
                    for(int i = 0 ; i < clist.size() ; i++ ){
                        DynamicInstanceColumn dy = clist.get(i);
                        Map<String, Object> imap = new HashMap<String, Object>(); 
                        for(String fieldName : fieldNames){
                            if(!("formValues").equals(fieldName)){
                            imap.put(fieldName, getFieldValueByName(fieldName,dy));
                            }
                        }
                        //自定义列值
                        for(FormValue fv: dy.getFormValues()){
                            StringBuilder newStr = new StringBuilder("");
                            if(columnFilter(fv)){
                                if(Constants.MODULE_FORM_TYPE_MULTISEL.equals(fv.getForm().getType()) ){
                                    
                                    List<String> newOps = new ArrayList<String>();
                                    newOps = JSON.parseArray(fv.getFormValue(),String.class);
                                    if(fv.getFormOptions() != null){
                                        for(FormOptions nf : fv.getFormOptions()){
                                            for(String n:newOps){
                                                if(nf.getValue().equals(n)){
                                                    newStr.append(nf.getName()).append("  ");
                                                }
                                            }
                                        }
                                    }
                                    imap.put("key"+fv.getForm().getCode(), newStr);
                                }else if(Constants.MODULE_FORM_TYPE_LISTSEL.equals(fv.getForm().getType()) ||   Constants.MODULE_FORM_TYPE_SINGLESEL.equals(fv.getForm().getType())){

                                    
                                    List<String> newOps = new ArrayList<String>();
                                    newOps.add(fv.getFormValue());
                                    if(fv.getFormOptions() != null){
                                        for(FormOptions nf : fv.getFormOptions()){
                                            for(String n:newOps){
                                                if(nf.getValue().equals(n)){
                                                    newStr.append(nf.getName()).append("  ");
                                                }
                                            }
                                        }
                                    }
                                    imap.put("key"+fv.getForm().getCode(), newStr);
                                }else{
                                
                                         imap.put("key"+fv.getForm().getCode(), fv.getFormValue());
                                }
                                         
                                      }
                        }
        
                        resultList.add(imap);
                    }
                    resultMap.put("dataList", resultList);  
                    
                    if(modules.get(0).get("id")!=null){
                            try{
                                ColumnFilter  query = new ColumnFilter();
                                query.setMenuType("Maintain");
                                query.setModuleId((String) modules.get(0).get("id"));
                                ColumnFilter  columnFilter = columnFilterService.getOne(query);
                                if(columnFilter==null){
            //                    Map m = JSON.parseObject(columnFilter.getColumnInfo(), Map.class);
                                    query.setId(UUIDUtil.getUUID());
                                    columnFilterService.insert(query);
                                    columnFilter = query;
                                }
                                resultMap.put("columFilter", columnFilter);
                                resultMap.put("isFilter", "true");
                            }catch(Exception e){
                                logger.error("读取字段过滤数据失败！", e);
                            }    
                    }
            }else{
                    resultMap.put("dataList", clist);  
            }
            

            
            resultMap.put("columList", columList);
            resultMap.put("success", true);  
            resultMap.put("total", pr.getTotal());  
        
            return resultMap;
 
    }
    
    @RequestMapping(value="/deleteInstance")
    public Object deleteInstance(String ids) {
 
            String[] idList = ids.split(",");
            for(String id: idList){
                
                circleService.deleteInstance(id);
                
                configLogService.saveInstanceLog(id ,Constants.LOG_ACTION_TYPE_DELINSTANCE_NAME);
            }

        Map<String, Object> map=new HashMap<String, Object>();  
        map.put("success", true);  
        return map;
    }
    
    @SuppressWarnings("rawtypes")
    @RequestMapping("/updateTagName")
    public Object updateTagName(String name,String moduleName,String circleId, String instanceId, String[] tags) {
        List<Map> old= maintainViewService.selectInstanceTag(instanceId);

        
        maintainViewService.updateTagName(instanceId, tags);
        
        //写日志
        try{
            String oldTags = "";
            if(old != null && old.size()>0 && old.get(0)!=null){
                oldTags =  (String) old.get(0).get("name");
            }
            
            String newTags = "";
            if(tags!=null){
                for(String t:tags){
                    newTags += t;
                    newTags += " ";
                }
            } 
            
            String desc = "[标签由'{{oldStr}}'变更为'{{newStr}}']";
            Map<String, String> data = new HashMap<String, String>();
            data.put("oldStr", oldTags.replace(",", " "));
            data.put("newStr", newTags);
            String change = StringTemplateUtils.render(desc,data);

                ConfigLog configLog = new ConfigLog();
                configLog.setId(UUIDUtil.getUUID());
                configLog.setName(name);
                configLog.setModuleName(moduleName);
                configLog.setAction(Constants.LOG_ACTION_TYPE_UPDATEINSTANCE_NAME);
                configLog.setCircleId(circleId);
                configLog.setInstanceId(instanceId);
                configLog.setDesc(change);
                configLogService.insert(configLog);
        }catch(Exception e){
            logger.error("实例[" + instanceId + "]修改配置日志写入失败！", e);
            e.printStackTrace();
        }

        Map<String, Object> map=new HashMap<String, Object>();  
        map.put("success", true);  
        return map;
    }
    
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @RequestMapping("/getHistoryByActionType")
    public Object getHistoryByActionType(String name,String circleId, String actionType,  Integer pageNumber,Integer pageSize, String sort, String order) {

            PageHelper.startPage(pageNumber, pageSize);
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("circleId", circleId);
            map.put("name", name);
            map.put("actionType", actionType);
            
            PagedResult pr = BeansUtil.toPagedResult(circleService.getHistoryByActionType(map));
            
            List<Map> clist =  (List<Map>) pr.getDataList();
            
            Map<String, Object> resultMap=new HashMap<String, Object>(); 
                
            resultMap.put("dataList", clist);    
            resultMap.put("success", true);  
            resultMap.put("total", pr.getTotal());  
        
            return resultMap;
  
    }
    
    @SuppressWarnings({ "rawtypes" })
    @RequestMapping("/getHistoryByInstanceId")
    public Object getHistoryByInstanceId(String instanceId ,String actionType) {

            Map<String, Object> map=new HashMap<String, Object>();
            map.put("instanceId", instanceId);
            map.put("actionType", actionType);
            List<Map> clist =  circleService.getHistoryByActionType(map);
            
            Map<String, Object> resultMap=new HashMap<String, Object>(); 
                
            resultMap.put("dataList", clist);    
            resultMap.put("success", true);  
        
            return resultMap;
  
    }
    
    
    private boolean columnFilter(FormValue fv){  
        return !Constants.MODULE_FORM_TYPE_TABLE.equals(fv.getForm().getType()) && !Constants.MODULE_FORM_TYPE_RICHTEXT.equals(fv.getForm().getType())
                && !Constants.MODULE_FORM_TYPE_IMAGE.equals(fv.getForm().getType())  && !Constants.MODULE_FORM_TYPE_FILE.equals(fv.getForm().getType())
                && !Constants.MODULE_FORM_TYPE_MULTIROWTEXT.equals(fv.getForm().getType())  && !Constants.MODULE_FORM_TYPE_CASCADER.equals(fv.getForm().getType()) 
                && !Constants.MODULE_FORM_TYPE_GROUPLINE.equals(fv.getForm().getType()) &&  !"Y_name".equals(fv.getForm().getCode());  
       }  
    
    /** 
     * 获取属性名数组 
     * */  
    private String[] getFiledName(Object o){  
     Field[] fields=o.getClass().getDeclaredFields();  
         String[] fieldNames=new String[fields.length];  
     for(int i=0;i<fields.length;i++){  
         fieldNames[i]=fields[i].getName();  
     }  
     return fieldNames;  
    }  
    
    /** 
     * 根据属性名获取属性值 
     * */  
   private Object getFieldValueByName(String fieldName, Object o) {  
       try {    
           String firstLetter = fieldName.substring(0, 1).toUpperCase();    
           String getter = "get" + firstLetter + fieldName.substring(1);    
           Method method = o.getClass().getMethod(getter, new Class[] {});    
           Object value = method.invoke(o, new Object[] {});    
           return value;    
       } catch (Exception e) {    
           return null;    
       }    
   }
}
