package com.aspire.ums.cmdb.gvSafe.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.ums.cmdb.gvSafe.IGvSafeFaultAPI;
import com.aspire.ums.cmdb.gvSafe.service.GvSafeFaultService;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class GvSafeFaultController implements IGvSafeFaultAPI {

    @Autowired
    private GvSafeFaultService gvSafeFaultService;

   
	@Override
	public Map<String,Object> saveGvSafeFault(@RequestBody Map<String, Object> request, String userName) {
		log.info("saveGvSafeFault-begin");
		Map<String,Object> result = Maps.newHashMap();
		if(null==request || request.size()==0) {
			result.put("flag", false);
			result.put("msg", "参数不能为空");
			return result;
		}
		Object moduleId = request.get("module_id");
		Object mfrs = request.get("mfrs");
		if(null==moduleId || null==mfrs || moduleId.toString().equals("")
				 || mfrs.toString().equals("")) {
			result.put("flag", false);
			result.put("msg", "厂商和模型ID不能为空");
			return result;
		}
		return gvSafeFaultService.saveGvSafeFault(request, userName);
	}

   

}
