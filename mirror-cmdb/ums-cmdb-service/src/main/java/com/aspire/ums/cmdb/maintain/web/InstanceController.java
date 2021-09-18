package com.aspire.ums.cmdb.maintain.web;

import com.aspire.ums.cmdb.common.Constants;
import com.aspire.ums.cmdb.maintain.entity.*;
import com.aspire.ums.cmdb.maintain.service.CasoptionsService;
import com.aspire.ums.cmdb.maintain.service.ConfigLogService;
import com.aspire.ums.cmdb.maintain.service.FormValueService;
import com.aspire.ums.cmdb.maintain.service.InstanceService;
import com.aspire.ums.cmdb.module.entity.FormParam;
import com.aspire.ums.cmdb.module.entity.FormRule;
import com.aspire.ums.cmdb.module.service.FormService;
import com.aspire.ums.cmdb.util.BusinessException;
import com.aspire.ums.cmdb.util.FtpUtls;
import com.aspire.ums.cmdb.util.StringTemplateUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.*;


@RestController
@RequestMapping("/cmdb/instance")
public class InstanceController {
    
    private final Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private FormValueService formValueService;
	
   @Autowired
    private InstanceService instanceService;
   
   @Autowired
   private CasoptionsService casoptionsService;
   
   @Autowired
   private ConfigLogService configLogService;
   
   @Autowired
   private FormService formService;
   
   @Value("${ftp.address}")
   private String ftpHost;
   
   @Value("${ftp.port}")
   private String ftpPort;
   
   @Value("${ftp.user}")
   private String ftpUser;
   
   @Value("${ftp.password}")
   private String ftpPassword;
	
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @RequestMapping("/getFormValues")
	public Map<String, Object> getFormValue(@RequestBody Map<String, String> param) {
        long startDate= (new Date()).getTime();
        List<FormValue> list = formValueService.getFormValues(param);
        System.out.println("----获取form数据耗时 -> " + ((new Date()).getTime() - startDate) + "s");
        Map<String, Object> map=new HashMap<String, Object>();  
        
        StringBuilder rule = new StringBuilder("{");
        StringBuilder validateForm = new StringBuilder("{");
        for(FormValue fv : list){
            validateRule(rule, validateForm, fv);
        }
        System.out.println("----验证耗时 -> " + ((new Date()).getTime() - startDate) + "s");
        String s = rule.substring(0, rule.length() - 1);
        rule=new StringBuilder(s);
        
        String ss = validateForm.substring(0, validateForm.length() - 1);
        validateForm=new StringBuilder(ss);
        
        rule.append("}");
        validateForm.append("}");
        startDate= (new Date()).getTime();
        List<FormRule> formRules = formService.getFormRule();
        map.put("formRules", formRules); 
        map.put("validateForm", validateForm.toString()); 
        map.put("rule", rule.toString());
        System.out.println("----规则耗时 -> " + ((new Date()).getTime() - startDate) + "s");
        map.put("dataList", list);  
        map.put("success", true);  
        return map;
	}
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @RequestMapping("/getFormValuesByModuleId")
    public Map<String, Object> getFormValuesByModuleId(@RequestBody Map<String, String> queryMap) {
        Map inMap = new HashMap();
        inMap.put("moduleId", queryMap.get("moduleId"));
        List<FormValue> list = formValueService.getFormValuesByModuleId(inMap);
        Map<String, Object> map=new HashMap<String, Object>();  
        
        StringBuilder rule = new StringBuilder("{");
        StringBuilder validateForm = new StringBuilder("{");
        
//        nativeValidateRule(rule, validateForm);
        
        for(FormValue fv : list){
            
        validateRule(rule, validateForm, fv);

         } 
        String s = rule.substring(0, rule.length() - 1);
        rule=new StringBuilder(s);
        
        String ss = validateForm.substring(0, validateForm.length() - 1);
        validateForm=new StringBuilder(ss);
        
        rule.append("}");
        validateForm.append("}");
        
        List<FormRule> formRules = formService.getFormRule();
        map.put("formRules", formRules); 
        map.put("validateForm", validateForm.toString()); 
        map.put("rule", rule.toString()); 
        map.put("dataList", list);  
        map.put("success", true);  
        return map;
    }

    @SuppressWarnings("unused")
    private void nativeValidateRule(StringBuilder rule, StringBuilder validateForm) {
//        String ntemplate="\"name\":  [{{duplicateName}}{ \"required\": {{required}}, \"message\": \"请输入名称\", \"trigger\": \"blur\" }  ]";
//        Map<String, String> ndata = new HashMap<String, String>();
//        ndata.put("required", "true");
//        //实例名称重复校验
//        String   duplicateName =  "{ \"validator\": \"\",  \"trigger\": \"blur\"  },";
//        
//        ndata.put("duplicateName", duplicateName);
//        rule.append(StringTemplateUtils.render(ntemplate,ndata)+",");
//        
//        String nformtemplate="\"name\": \"\"";
//        Map<String, String> ndata11 = new HashMap<String, String>();
//        validateForm.append(StringTemplateUtils.render(nformtemplate,ndata11)+",");
    }

    private void validateRule(StringBuilder rule, StringBuilder validateForm, FormValue fv) {
     if("false".equals(fv.getForm().getHidden())){   
        if(Constants.MODULE_FORM_TYPE_MULTISEL.equals(fv.getForm().getType()) ){
            String template="\"key{{formId}}\": [ { \"type\": \"array\", \"required\": {{required}}, \"message\": \"请选择{{formName}}\", \"trigger\": \"change\" }]";
            Map<String, String> data = new HashMap<String, String>();
            data.put("formId", fv.getForm().getId());
            data.put("formName", fv.getForm().getName());
            data.put("required", fv.getForm().getRequired());
            rule.append(StringTemplateUtils.render(template,data)).append(",");
            
            String formtemplate="\"key{{formId}}\": []";
            Map<String, String> data1 = new HashMap<String, String>();
            data1.put("formId", fv.getForm().getId());
            validateForm.append(StringTemplateUtils.render(formtemplate,data1)).append(",");
            
        }else if(Constants.MODULE_FORM_TYPE_LISTSEL.equals(fv.getForm().getType()) || 
                Constants.MODULE_FORM_TYPE_SINGLESEL.equals(fv.getForm().getType())){
            String template="\"key{{formId}}\": [ { \"required\": {{required}}, \"message\": \"请选择{{formName}}\", \"trigger\": \"change\" }]";
            Map<String, String> data = new HashMap<String, String>();
            data.put("formId", fv.getForm().getId());
            data.put("formName", fv.getForm().getName());
            data.put("required", fv.getForm().getRequired());
            rule.append(StringTemplateUtils.render(template,data)).append(",");
            
            String formtemplate="\"key{{formId}}\": \"\"";
            Map<String, String> data1 = new HashMap<String, String>();
            data1.put("formId", fv.getForm().getId());
            validateForm.append(StringTemplateUtils.render(formtemplate,data1)).append(",");
        } else if(Constants.MODULE_FORM_TYPE_RICHTEXT.equals(fv.getForm().getType())){
            String template="\"key{{formId}}\": [ { \"required\": {{required}}, \"message\": \"请填写{{formName}}\", \"trigger\": \"blur\" }]";
            Map<String, String> data = new HashMap<String, String>();
            data.put("formId", fv.getForm().getId());
            data.put("formName", fv.getForm().getName());
            data.put("required", fv.getForm().getRequired());
            rule.append(StringTemplateUtils.render(template,data)).append(",");
            
            String formtemplate="\"key{{formId}}\": \"\"";
            Map<String, String> data1 = new HashMap<String, String>();
            data1.put("formId", fv.getForm().getId());
            data1.put("formValue", fv.getFormValue());
            validateForm.append(StringTemplateUtils.render(formtemplate,data1)).append(",");
        }else if(Constants.MODULE_FORM_TYPE_INT.equals(fv.getForm().getType()) || 
                Constants.MODULE_FORM_TYPE_DOUBLE.equals(fv.getForm().getType()) 
        ){
            String template="\"key{{formId}}\": [ { \"required\": {{required}}, \"message\": \"{{formName}}不能为空\"},{ \"type\": \"number\", \"message\": \"{{formName}}必须为数字值\"}]";
            Map<String, String> data = new HashMap<String, String>();
            data.put("formId", fv.getForm().getId());
            data.put("formName", fv.getForm().getName());
            data.put("required", fv.getForm().getRequired());
            rule.append(StringTemplateUtils.render(template,data)).append(",");
            
            String formtemplate="\"key{{formId}}\": \"\"";
            Map<String, String> data1 = new HashMap<String, String>();
            data1.put("formId", fv.getForm().getId());
            data1.put("formValue", fv.getFormValue());
            validateForm.append(StringTemplateUtils.render(formtemplate,data1)).append(",");
        }else if(   Constants.MODULE_FORM_TYPE_SINGLEROWTEXT.equals(fv.getForm().getType()) ||
                    Constants.MODULE_FORM_TYPE_MULTIROWTEXT.equals(fv.getForm().getType()) 
        ){
                 if("Y_name".equals(fv.getForm().getCode())){ 
                     String ntemplate="\"name\":  [{{duplicateName}}{ \"required\": {{required}}, \"message\": \"请输入名称\", \"trigger\": \"blur\" } {{lengthStr}} ]";
                     
                     String lengthStr = ", { \"min\": {{min}}, \"max\": {{max}}, \"message\": \"长度在 {{min}} 到 {{max}} 个字符\", \"trigger\": \"blur\" }";
                     String min = "";
                     String max = "";
                     for(FormParam  fp:fv.getFormParams()){
                         if("minLength".equals(fp.getKey())){
                             min = fp.getValue();
                         }else if("maxLength".equals(fp.getKey())){
                             max = fp.getValue();
                         }
                     }
                     Map<String, String> data1 = new HashMap<String, String>();
                     data1.put("min", min);
                     data1.put("max", max);
                     lengthStr = StringTemplateUtils.render(lengthStr,data1);
                     if(StringUtils.isBlank(min) || StringUtils.isBlank(max)){
                         lengthStr = "";
                     }
                     
                     Map<String, String> ndata = new HashMap<String, String>();
                     ndata.put("required", "true");
                     //实例名称重复校验
                     String   duplicateName =  "{ \"validator\": \"\",  \"trigger\": \"blur\"  },";
                     
                     ndata.put("duplicateName", duplicateName);
                     rule.append(StringTemplateUtils.render(ntemplate,ndata)).append(",");
                     
                     String nformtemplate="\"name\": \"\"";
                     Map<String, String> ndata11 = new HashMap<String, String>();
                     validateForm.append(StringTemplateUtils.render(nformtemplate,ndata11)).append(",");
                 }else{      
                        String template="\"key{{formId}}\":  [{ \"required\": {{required}}, \"message\": \"请输入{{formName}}\", \"trigger\": \"blur\" } {{lengthStr}} ]";
                        Map<String, String> data = new HashMap<String, String>();
                        data.put("formId", fv.getForm().getId());
                        data.put("formName", fv.getForm().getName());
                        
            
                        String lengthStr = ", { \"min\": {{min}}, \"max\": {{max}}, \"message\": \"长度在 {{min}} 到 {{max}} 个字符\", \"trigger\": \"blur\" }";
                        String min = "";
                        String max = "";
                        for(FormParam  fp:fv.getFormParams()){
                            if("minLength".equals(fp.getKey())){
                                min = fp.getValue();
                            }else if("maxLength".equals(fp.getKey())){
                                max = fp.getValue();
                            }
                        }
                        Map<String, String> data1 = new HashMap<String, String>();
                        data1.put("min", min);
                        data1.put("max", max);
                        lengthStr = StringTemplateUtils.render(lengthStr,data1);
                        if(StringUtils.isBlank(min) || StringUtils.isBlank(max)){
                            lengthStr = "";
                        }
                        
            
                        
                        data.put("lengthStr", lengthStr);
                        data.put("required", fv.getForm().getRequired());
                        rule.append(StringTemplateUtils.render(template,data)).append(",");
                        
                        String formtemplate="\"key{{formId}}\": \"\"";
                        Map<String, String> data11 = new HashMap<String, String>();
                        data11.put("formId", fv.getForm().getId());
                        data11.put("formValue", fv.getFormValue());
                        validateForm.append(StringTemplateUtils.render(formtemplate,data11)).append(",");
                  }       
        }else if(   Constants.MODULE_FORM_TYPE_DATETIME.equals(fv.getForm().getType())     ){
            String template=" \"key{{formId}}\":  [ { \"type\": \"date\", \"required\": {{required}}, \"message\": \"请选择日期\", \"trigger\": \"change\" }]";
            Map<String, String> data = new HashMap<String, String>();
            data.put("formId", fv.getForm().getId());
            data.put("required", fv.getForm().getRequired());
            rule.append(StringTemplateUtils.render(template,data)).append(",");
            
            String formtemplate="\"key{{formId}}\": {}";
            Map<String, String> data1 = new HashMap<String, String>();
            data1.put("formId", fv.getForm().getId());
            validateForm.append(StringTemplateUtils.render(formtemplate,data1)).append(",");
        }else if(   Constants.MODULE_FORM_TYPE_TABLE.equals(fv.getForm().getType())     ){
            String template="\"key{{formId}}\":  [{ \"type\": \"array\", \"required\": {{required}}, \"message\": \"请录入{{formName}}\", \"trigger\": \"change\" }]";
            Map<String, String> data = new HashMap<String, String>();
            data.put("formId", fv.getForm().getId());
            data.put("formName", fv.getForm().getName());
            data.put("required", fv.getForm().getRequired());
            rule.append(StringTemplateUtils.render(template,data)).append(",");
            
            String formtemplate="\"key{{formId}}\": []";
            Map<String, String> data1 = new HashMap<String, String>();
            data1.put("formId", fv.getForm().getId());
            validateForm.append(StringTemplateUtils.render(formtemplate,data1)).append(",");
        }else if( Constants.MODULE_FORM_TYPE_CASCADER.equals(fv.getForm().getType())     ){
            String template="\"key{{formId}}\":  [{ \"type\": \"array\", \"required\": {{required}}, \"message\": \"请录入{{formName}}\", \"trigger\": \"change\" }]";
            Map<String, String> data = new HashMap<String, String>();
            data.put("formId", fv.getForm().getId());
            data.put("formName", fv.getForm().getName());
            data.put("required", fv.getForm().getRequired());
            rule.append(StringTemplateUtils.render(template,data)).append(",");
            
            String formtemplate="\"key{{formId}}\": []";
            Map<String, String> data1 = new HashMap<String, String>();
            data1.put("formId", fv.getForm().getId());
            validateForm.append(StringTemplateUtils.render(formtemplate,data1)).append(",");
            
            Map<String,Object> casoptions = new HashMap<String,Object>();

            List<CasoptionsBean> obs = casoptionsService.getCascaderOptions(fv.getForm().getId());
            casoptions.put(fv.getForm().getId(), obs);
            fv.setCasoptions(casoptions);
   
        }else if(   Constants.MODULE_FORM_TYPE_IMAGE.equals(fv.getForm().getType())   ||    Constants.MODULE_FORM_TYPE_FILE.equals(fv.getForm().getType())){
            String template="\"key{{formId}}\":  [{ \"type\": \"array\", \"required\": {{required}}, \"message\": \"请上传{{formName}}\", \"trigger\": \"change\" }]";
            Map<String, String> data = new HashMap<String, String>();
            data.put("formId", fv.getForm().getId());
            data.put("formName", fv.getForm().getName());
            data.put("required", fv.getForm().getRequired());
            rule.append(StringTemplateUtils.render(template,data)).append(",");
            
            String formtemplate="\"key{{formId}}\": []";
            Map<String, String> data1 = new HashMap<String, String>();
            data1.put("formId", fv.getForm().getId());
            validateForm.append(StringTemplateUtils.render(formtemplate,data1)).append(",");
        }
     }
    }

    @RequestMapping("/updateFormValues")
    public Map<String, Object> updateFormValues(@RequestBody InstanceModel instanceModel) {
        configLogService.saveInstanceUpdateLog(instanceModel);
        
        formValueService.update(instanceModel);
        
        Map<String, Object> resultMap=new HashMap<String, Object>();  
        resultMap.put("success", true);  
        return resultMap;
    }
    
    @RequestMapping("/addInstance")
    public Map<String, Object> addInstance(@RequestBody InstanceModel instanceModel) {
        Instance instance = instanceService.addInstance(instanceModel.getFormValues(), instanceModel.getName(), instanceModel.getModuleId(), instanceModel.getCircleId());
        
        configLogService.saveInstanceLog(instance.getId() ,Constants.LOG_ACTION_TYPE_ADDINSTANCE_NAME);
        
        Map<String, Object> resultMap=new HashMap<String, Object>();  
        resultMap.put("success", true);  
        return resultMap;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @RequestMapping(value="checkInstancenName")
    public Object checkInstancenName(String name,String moduleId) {
        Map inMap = new HashMap();
        inMap.put("name", name);
        inMap.put("moduleId", moduleId);
        List<Map> clist = instanceService.checkInstanceName(inMap);
        Map<String, Object> map=new HashMap<String, Object>();  
        if(clist != null && clist.size() > 0){
            map.put("success", false);}else{
            map.put("success", true); 
        }
 
        return map;
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes", "unused" })
    @RequestMapping(value="handUpInstance")
    public Object handUpInstance(@RequestBody HandUpInstance handUpInstance) {
        String oldCircleId = handUpInstance.getOldCircleId();
        String circleId = handUpInstance.getCircleId();
        String[] instanceIds = handUpInstance.getInstanceIds();
        instanceService.handUpInstance(circleId, instanceIds);
        
        //写日志
        try{
            for(String instanceId: instanceIds){
                Map inmap = new HashMap();
                inmap.put("instanceId", instanceId);
                List<Map> outList = instanceService.getInstanceInfoById(inmap);
                Map descMap = new HashMap();
                if(outList!=null && outList.size()>0){
                ConfigLog configLog = new ConfigLog();
                configLog.setId(UUIDUtil.getUUID());
                configLog.setName((String) outList.get(0).get("name"));
                configLog.setModuleName((String) outList.get(0).get("moduleName"));
                configLog.setAction(Constants.LOG_ACTION_TYPE_HANDUPINSTANCE_NAME);
                configLog.setCircleId(oldCircleId);
                configLog.setInstanceId(instanceId);
                configLogService.insert(configLog);
                }
            }
        }catch(Exception e){
            logger.error("实例[" + Arrays.toString(instanceIds) + "]移交配置日志写入失败！", e);
            e.printStackTrace();
        }
        
        Map<String, Object> map=new HashMap<String, Object>();  
        map.put("success", true); 
 
        return map;
    }
    
    @RequestMapping("/uploadIcon/{formId}")
    public Map<String,Object> uploadIcon(HttpServletRequest request,@PathVariable("formId") String formId){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("formId", formId);
        try {
            List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
            MultipartFile file = null;
            InputStream is = null;
            //获取配置文件中ftp信息
            String host  = ftpHost;
            Integer port = Integer.valueOf(ftpPort);
            String user = ftpUser;
            String password = ftpPassword;
            for(int i=0;i<files.size();i++){
                file = files.get(i);
                String filename = file.getOriginalFilename();
                if (!file.isEmpty()) {
                    is = file.getInputStream();
                    String result = FtpUtls.uploadImage(host, port, user, password, filename, is);
                    if(null !=result && !"".equals(result)){
                          map.put("url", result);
                        
                    }else{
                        throw new BusinessException("图标文件["+filename+"]上传失败");
                    }
                }
            }
            map.put("success", true);
        } catch (BusinessException e) {
            logger.error("字段[" + formId + "]文件上传失败！", e);
            map.put("message",new String(e.getMessage()));
            map.put("success", false);
        } catch (Exception e) {
            logger.error("字段[" + formId + "]文件上传失败！", e);
            map.put("message","上传出错");
            map.put("success", false);
        }
        return map;
    }
}
