package com.aspire.ums.cmdb.module.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.aspire.ums.cmdb.module.entity.*;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.aspire.ums.cmdb.common.Constants;
import com.aspire.ums.cmdb.module.service.FormService;
import com.aspire.ums.cmdb.util.BusinessException;
/**
 * 
 * <p>Project: ums-cmdb-service</p>
 *
 * @Description: 
 *
 * @author: mingjianyong
 *
 * @Date: 2017-7-7
 */
@RestController
@RequestMapping("/cmdb/form/")
public class FormController {
	
	@Autowired
	private FormService formService;
	
    private static final Logger LOGGER = LoggerFactory.getLogger(FormController.class);

	 
	@RequestMapping("updateForm")
	public Map<String,Object> updateForms(@RequestBody Map<String,Object> forms){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			formService.updateForm(forms);
			map.put("success", true);
		} catch (BusinessException e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("message",e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("message","保存失败！");
		}
		return map;
	}

	@ResponseBody
	@RequestMapping("/getForms")
	public Map<String,Object> getForms(@RequestBody Module module){
		List<FormBean> fbs = null;
		Map<String,Object> re = new HashMap<String,Object>();
		Map<String,Object> casoptions = new HashMap<String,Object>();
		try {
			fbs = formService.getForms(module);
			for(FormBean fb : fbs){
				if(Constants.MODULE_FORM_TYPE_CASCADER.equals(fb.getType())){
					List<OptionBean> obs = formService.getCascaderOptions(fb.getId());
					System.out.println(obs);
					casoptions.put(fb.getId(), obs);
				}
			}
			re.put("formData", fbs);
			re.put("cssops",casoptions);
		} catch (BusinessException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return re;
	}
	@RequestMapping("getFormRule")
	public List<FormRule> getFormRule(){
		return formService.getFormRule();
	}
	/**
	 * 
	 *Description:根据formId获取脚本信息
	 * @return
	 */
	@RequestMapping("getScriptByformId")
	public FormScript getScriptByformId(String formId){
		FormScript result = formService.getScriptByformId(formId);
		System.out.println(result);
		return result;
	}

	/**
	 *
	 *Description:查询字典数据
	 * @return
	 */
	@RequestMapping("getDicts")
	public List<FormBean> getDicts(){
		List<FormBean> dictList= formService.getDicts();
		// System.out.println(result);
		// System.out.println(result);
		return dictList;
	}

    /**
     *
     *Description:查询字典数据
     * @return
     */
    @RequestMapping("getDictValue")
    public FormBean getDictValue(@RequestParam String formId){
        FormBean dict= formService.getDictValue(formId);
        // System.out.println(result);
        // System.out.println(result);
        return dict;
    }
    
//    @RequestMapping(value = "getBusiness/{businessCodes}", method = RequestMethod.GET)
//	public List<Map<String, String>> getBusiness(@PathVariable("businessCodes") String businessCodes) {
//    	LOGGER.info("!!!!!!!!getFirstBusiness!!!!!!!!!!!参数" + businessCodes);
//		List<Map<String, String>> result = new ArrayList<>();
//		if (null != businessCodes) {
//			String[] idArr = businessCodes.split(",");
//			List<String> ids = Arrays.asList(idArr);
//			result = formService.getFirstBusiness(ids);
//		}
//		return result;
//	}
//
//    @RequestMapping(value = "findBusCodeAndName/{id}", method = RequestMethod.GET)
//	public List<Map<String, String>> findBusCodeAndName(@PathVariable("id") String id) {
//		LOGGER.info("!!!!!!!!findBusCodeAndName!!!!!!!!!!!参数" + id);
//		List<Map<String, String>> result = new ArrayList<>();
//		if (null != id) {
//			String[] idArr = id.split(",");
//			List<String> ids = Arrays.asList(idArr);
//			result = formService.findBusCodeAndName(ids);
//		}
//		return result;
//	}
//
//    @RequestMapping(value = "findBusCodeByName/{name}", method = RequestMethod.GET)
//	public Map<String, String> findBusCodeByName(@PathVariable("name") String name) {
//		LOGGER.info("!!!!!!!!findBusCodeByName!!!!!!!!!!!参数" + name);
//		Map<String, String> result = new HashMap<>();
//		if (null != name) {
//			result = formService.findBusCodeByName(name).get(0);
//		}
//		return result;
//	}
}
