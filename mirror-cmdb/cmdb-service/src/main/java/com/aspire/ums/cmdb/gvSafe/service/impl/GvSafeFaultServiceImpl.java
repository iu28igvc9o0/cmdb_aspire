package com.aspire.ums.cmdb.gvSafe.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aspire.ums.cmdb.gvSafe.service.GvSafeFaultService;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceService;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class GvSafeFaultServiceImpl implements GvSafeFaultService {
	@Autowired
	private ICmdbInstanceService instanceService;

	@Override
	public Map<String, Object> saveGvSafeFault(Map<String, Object> request, String userName) {
		Map<String, Object> result = Maps.newHashMap();
		result.put("flag", true);
		//result.put("msg", "操作成功");
		try {
			String moduleId = request.get("module_id").toString();
			String mfrs = request.get("mfrs").toString();
			List<Map<String, Object>> addList = request.get("add")==null?null:(List<Map<String, Object>>)request.get("add");
			List<Map<String, Object>> delList = request.get("delete")==null?null:(List<Map<String, Object>>)request.get("delete");
			String msg = "";
			if (null != addList && addList.size() > 0) {
				for (Map<String, Object> o : addList) {
					String instanceId = o.get("id") == null ? "" : o.get("id").toString();
					o.put("module_id", moduleId);
					o.put("mfrs", mfrs);
					if (!instanceId.equals("")) {
						msg = instanceService.updateInstance(instanceId, userName, o, "手动更新");
					} else {
						o.put("id", UUIDUtil.getUUID());
						//o.put("module_id", moduleId);
						//o.put("mfrs", mfrs);
						msg = instanceService.addInstance(userName, o, "手动新增");
					}
				}

			}
			if (null != delList && delList.size() > 0) {
				for (Map<String, Object> o : delList) {
					o.put("module_id", moduleId);
				}
				msg = instanceService.deleteInstance(userName, delList, "手动删除");
			}
			result.put("msg", msg);
		} catch (Exception e) {
			result.put("flag", false);
			result.put("msg", e.getMessage());
		}
		return result;
	}
}
