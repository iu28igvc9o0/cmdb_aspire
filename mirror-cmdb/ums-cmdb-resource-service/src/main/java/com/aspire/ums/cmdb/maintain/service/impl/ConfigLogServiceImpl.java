package com.aspire.ums.cmdb.maintain.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.aspire.ums.cmdb.common.Constants;
import com.aspire.ums.cmdb.maintain.entity.ConfigLog;
import com.aspire.ums.cmdb.maintain.entity.FormValue;
import com.aspire.ums.cmdb.maintain.entity.InstanceModel;
import com.aspire.ums.cmdb.maintain.mapper.ConfigLogMapper;
import com.aspire.ums.cmdb.maintain.mapper.FormValueMapper;
import com.aspire.ums.cmdb.maintain.mapper.InstanceMapper;
import com.aspire.ums.cmdb.maintain.service.ConfigLogService;
import com.aspire.ums.cmdb.module.entity.FormOptions;
import com.aspire.ums.cmdb.util.StringTemplateUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;

@Service
@Transactional
public class ConfigLogServiceImpl implements ConfigLogService {
    
    private final Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private ConfigLogMapper configLogMapper;
	
    @Autowired
    private InstanceMapper instanceMapper;
    
    @Autowired
    private FormValueMapper formValueMapper;

    @SuppressWarnings("rawtypes")
    @Override
    public List<Map> getRelationInfoList(Map map) {
        // TODO Auto-generated method stub
        return configLogMapper.getRelationInfoList(map);
    }

    @Override
    public void insert(ConfigLog configLog) {
        configLogMapper.insert(configLog);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void saveInstanceLog(String instanceId , String type) {

            //写日志
            try{
                    Map inmap = new HashMap();
                    inmap.put("instanceId", instanceId);
                    List<Map> outList = instanceMapper.getInstanceLogInfoById(inmap);
                    if(outList!=null && outList.size()>0){
                    ConfigLog configLog = new ConfigLog();
                    configLog.setId(UUIDUtil.getUUID());
                    configLog.setName((String) outList.get(0).get("name"));
                    configLog.setModuleName((String) outList.get(0).get("moduleName"));
                    configLog.setAction(type);
                    configLog.setCircleId((String) outList.get(0).get("circleId"));
                    configLog.setInstanceId(instanceId);
                    configLogMapper.insert(configLog);
                    }
            }catch(Exception e){
                logger.error("实例[" + instanceId + "]"+type+"日志写入失败！", e);
                e.printStackTrace();
            }
        
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void saveInstanceUpdateLog(InstanceModel instanceModel) {
        // TODO Auto-generated method stub
        Map inMap = new HashMap();
        inMap.put("instanceId", instanceModel.getId());
        List<FormValue> list = formValueMapper.getFormValues(inMap);
        
        //写日志
        try{
            Map inmap = new HashMap();
            inmap.put("instanceId", instanceModel.getId());
            List<Map> outList = instanceMapper.getInstanceLogInfoById(inmap);
            StringBuilder result = new StringBuilder("");
            boolean isUpdate = false;
                for(FormValue oldf : list){
                    for(FormValue newf : instanceModel.getFormValues()){
                        if(!oldf.getFormValue().equals(newf.getFormValue()) && oldf.getFormId().equals(newf.getFormId())){
                            if(outList!=null && outList.size()>0){
                                isUpdate = true;

                                StringBuilder oldStr = new StringBuilder("");
                                StringBuilder newStr = new StringBuilder("");
                                
                                if(Constants.MODULE_FORM_TYPE_MULTISEL.equals(oldf.getForm().getType()) ){
                                    String desc = "[{{name}}由'{{oldStr}}'变更为'{{newStr}}']";
                                    List<String> oldOps = new ArrayList<String>();
                                    oldOps = JSON.parseArray(oldf.getFormValue(),String.class);
                                    for(FormOptions of : oldf.getFormOptions()){
                                        for(String o:oldOps){
                                            if(of.getValue().equals(o)){
                                                oldStr.append(of.getName()).append("  ");
                                            }
                                        }
                                    }
                                    
                                    List<String> newOps = new ArrayList<String>();
                                    newOps = JSON.parseArray(newf.getFormValue(),String.class);
                                    for(FormOptions nf : newf.getFormOptions()){
                                        for(String n:newOps){
                                            if(nf.getValue().equals(n)){
                                                newStr.append(nf.getName()).append("  ");
                                            }
                                        }
                                    }
                                    Map<String, String> data = new HashMap<String, String>();
                                    data.put("name", oldf.getForm().getName());
                                    data.put("oldStr", oldStr.toString());
                                    data.put("newStr", newStr.toString());
                                    String change = StringTemplateUtils.render(desc,data);
                                    result.append(change);

                                }else if(Constants.MODULE_FORM_TYPE_LISTSEL.equals(oldf.getForm().getType()) ||   Constants.MODULE_FORM_TYPE_SINGLESEL.equals(oldf.getForm().getType())){
                                    String desc = "[{{name}}由'{{oldStr}}'变更为'{{newStr}}']";
                                    List<String> oldOps = new ArrayList<String>();
                                    oldOps.add(oldf.getFormValue());
                                    for(FormOptions of : oldf.getFormOptions()){
                                        for(String o:oldOps){
                                            if(of.getValue().equals(o)){
                                                oldStr.append(of.getName()).append("  ");
                                            }
                                        }
                                    }
                                    
                                    List<String> newOps = new ArrayList<String>();
                                    newOps.add(newf.getFormValue());
                                    for(FormOptions nf : oldf.getFormOptions()){
                                        for(String n:newOps){
                                            if(nf.getValue().equals(n)){
                                                newStr.append(nf.getName()).append("  ");
                                            }
                                        }
                                    }
                                    Map<String, String> data = new HashMap<String, String>();
                                    data.put("name", oldf.getForm().getName());
                                    data.put("oldStr", oldStr.toString());
                                    data.put("newStr", newStr.toString());
                                    String change = StringTemplateUtils.render(desc,data);
                                    result.append(change);

                                }  else if(Constants.MODULE_FORM_TYPE_RICHTEXT.equals(oldf.getForm().getType()) ||  Constants.MODULE_FORM_TYPE_MULTIROWTEXT.equals(oldf.getForm().getType())
                                        || Constants.MODULE_FORM_TYPE_TABLE.equals(oldf.getForm().getType()) ||  Constants.MODULE_FORM_TYPE_CASCADER.equals(oldf.getForm().getType())
                                        || Constants.MODULE_FORM_TYPE_IMAGE.equals(oldf.getForm().getType())   ||    Constants.MODULE_FORM_TYPE_FILE.equals(oldf.getForm().getType())
                                        ){
                                    String desc = "[{{name}}内容已变更]";
                                    Map<String, String> data = new HashMap<String, String>();
                                    data.put("name", oldf.getForm().getName());
                                    String change = StringTemplateUtils.render(desc,data);
                                    result.append(change);
                                }else if(Constants.MODULE_FORM_TYPE_INT.equals(oldf.getForm().getType()) || Constants.MODULE_FORM_TYPE_DATETIME.equals(oldf.getForm().getType())  ||
                                        Constants.MODULE_FORM_TYPE_DOUBLE.equals(oldf.getForm().getType()) || Constants.MODULE_FORM_TYPE_SINGLEROWTEXT.equals(oldf.getForm().getType())
                                ){
                                    String desc = "[{{name}}由'{{oldStr}}'变更为'{{newStr}}']";
                                    Map<String, String> data = new HashMap<String, String>();
                                    data.put("name", oldf.getForm().getName());
                                    data.put("oldStr", oldf.getFormValue());
                                    data.put("newStr", newf.getFormValue());
                                    String change = StringTemplateUtils.render(desc,data);
                                    result.append(change);
                                }
                              }
                        }
                    }
                  }
              if(isUpdate){
                ConfigLog configLog = new ConfigLog();
                configLog.setId(UUIDUtil.getUUID());
                configLog.setInstanceId(instanceModel.getId());
                configLog.setName(instanceModel.getName());
                configLog.setModuleName((String) outList.get(0).get("moduleName"));
                configLog.setAction(Constants.LOG_ACTION_TYPE_UPDATEINSTANCE_NAME);
                configLog.setDesc(result.toString());
                configLog.setCircleId((String) outList.get(0).get("circleId"));
                configLogMapper.insert(configLog);
              }
        }catch(Exception e){
            logger.error("实例[" + instanceModel.getId() + "]修改配置日志写入失败！", e);
            e.printStackTrace();
        }
    }
	
}
