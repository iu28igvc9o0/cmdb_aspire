package com.aspire.mirror.alert.server.biz.primarySecondary.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.aspire.mirror.alert.server.util.AlertFilterCommonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.aspire.mirror.alert.server.biz.operateLog.IAlertOperateLogBiz;
import com.aspire.mirror.alert.server.biz.primarySecondary.IAlertPrimarySecondaryBiz;
import com.aspire.mirror.alert.server.constant.Constants;
import com.aspire.mirror.alert.server.dao.primarySecondary.AlertPrimarySecondaryMapper;
import com.aspire.mirror.alert.server.dao.primarySecondary.po.AlertPrimarySecondary;
import com.aspire.mirror.alert.server.vo.primarySecondary.AlertPrimarySecondaryVo;
import com.aspire.mirror.common.entity.PageResponse;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 描述：
 *
 * @author
 * @date 2019-08-14 19:35:33
 */
@Service
public class AlertPrimarySecondaryBizImpl implements IAlertPrimarySecondaryBiz {

    @Autowired
    private AlertPrimarySecondaryMapper mapper;

    @Autowired
    private IAlertOperateLogBiz alertOperateLogBiz;

    /**
     * 获取所有实例
     *
     * @return 返回所有实例数据
     */
    public List<AlertPrimarySecondary> list() {
        return mapper.list();
    }

    /**
     * 根据主键ID 获取数据信息
     *
     * @param id 实例信息
     * @return 返回指定ID的数据信息
     */
    public AlertPrimarySecondary get(String id) {
        return mapper.get(id);
    }

    /**
     * 校验名称是否可用
     * @param name
     * @return
     */
    public boolean checkName (String name) {
        List<AlertPrimarySecondary> alertPrimarySecondarys = mapper.checkName(name);
        if (CollectionUtils.isEmpty(alertPrimarySecondarys)) {
            return true;
        }
        return false;
    }

    /**
     * 新增实例
     *
     * @param entity 实例数据
     * @return
     */
    public void insert(AlertPrimarySecondary entity) {
        mapper.insert(entity);
        alertOperateLogBiz.insertOperateLog("新增规则", entity.getCreater(), Constants.OPERATE_TYPE_INSERT, Constants.OPERATE_TYPE_INSERT_DESC, Constants.OPERATE_MODEL_ALERT_PRIMARY_SECONDARY, Constants.OPERATE_MODEL_ALERT_PRIMARY_SECONDARY_DESC, entity.getId().toString());
    }

    /**
     * 修改实例
     *
     * @param entity 实例数据
     * @return
     */
    public void update(AlertPrimarySecondary entity) {

        AlertPrimarySecondary alertPrimarySecondary = mapper.get(entity.getId().toString());
        String content = getOperateContent(entity, alertPrimarySecondary);
        mapper.update(entity);
        alertOperateLogBiz.insertOperateLog(content, entity.getEditer(), Constants.OPERATE_TYPE_UPDATE, Constants.OPERATE_TYPE_UPDATE_DESC, Constants.OPERATE_MODEL_ALERT_PRIMARY_SECONDARY, Constants.OPERATE_MODEL_ALERT_PRIMARY_SECONDARY_DESC, entity.getId().toString());
    }

    /**
     * 获取修改内容存为操作日志
     * @param newPrimarySecondary
     * @param oldPrimarySecondary
     * @return
     */
    private String getOperateContent(AlertPrimarySecondary newPrimarySecondary, AlertPrimarySecondary oldPrimarySecondary) {
        StringBuffer sb = new StringBuffer("修改内容,由");
        sb.append(transformContenttoStr(oldPrimarySecondary)).append("改为").append(transformContenttoStr(newPrimarySecondary));
        return sb.toString();
    }

    /**
     * 规则内容转换为string
     * @param primarySecondary
     * @return
     */
    private String transformContenttoStr (AlertPrimarySecondary primarySecondary) {
        StringBuffer sb = new StringBuffer("");
        if (null == primarySecondary) {
            return sb.toString();
        }
        sb.append("主要告警(");
        if ("1".equals(primarySecondary.getDeviceRelationType())) {
            sb.append("设备关联类型").append("相同设备;设备范围:")
                    .append(AlertFilterCommonUtil.getOptionStr(primarySecondary.getPrimaryOptionCondition()));
        } else {
            sb.append("设备关联类型").append("不同设备;资源池:")
                    .append(primarySecondary.getPrimaryIdc())
                    .append(";设备IP:").append(primarySecondary.getPrimaryIp());
        }
        sb.append(";告警内容:").append(primarySecondary.getPrimaryMoniIndex())
                .append(";告警等级:").append(primarySecondary.getPrimaryAlertLevel())
                .append("),次要告警(");
        sb.append(AlertFilterCommonUtil.getOptionStr(primarySecondary.getSecondaryOptionCondition())).append(")");
        return sb.toString();
    }

    /**
     * 删除实例
     *
     * @param ids 实例数据
     * @return
     */
    public void delete(String operater, String... ids) {
        mapper.delete(ids);
        alertOperateLogBiz.insertOperateLog("删除规则", operater, Constants.OPERATE_TYPE_DELETE, Constants.OPERATE_TYPE_DELETE_DESC, Constants.OPERATE_MODEL_ALERT_PRIMARY_SECONDARY, Constants.OPERATE_MODEL_ALERT_PRIMARY_SECONDARY_DESC, ids);
    }

    /**
     * 启动/停用屏蔽规则
     *
     * @param status
     * @param ids
     */
    public void status(String status, String operater, String... ids) {
        mapper.status(status, ids);
        String content = "修改状态，由停用修改为启用";
        if (Constants.ISOLATE_STATUS_STOP.equals(status)) {
            content = "修改状态，由启用修改为停用";
        }
        alertOperateLogBiz.insertOperateLog(content, operater, Constants.OPERATE_TYPE_UPDATE, Constants.OPERATE_TYPE_UPDATE_DESC, Constants.OPERATE_MODEL_ALERT_PRIMARY_SECONDARY, Constants.OPERATE_MODEL_ALERT_PRIMARY_SECONDARY_DESC, ids);
    }

    public PageResponse<AlertPrimarySecondary> findPage(AlertPrimarySecondaryVo alertPrimarySecondaryVo) {
        List<AlertPrimarySecondary> pageWithResult = null;
        if (alertPrimarySecondaryVo.getPageSize() < 0) {
            pageWithResult = mapper.list();
        } else {

            pageWithResult = mapper.findPageWithResult(alertPrimarySecondaryVo);
        }
        Integer pageWithCount = mapper.findPageWithCount(alertPrimarySecondaryVo);
        PageResponse<AlertPrimarySecondary> page = new PageResponse<>();
        page.setCount(pageWithCount);
        page.setResult(pageWithResult);
        return page;
    }
    
    @Override
    public PageResponse<Map<String,Object>> listByConfigId(int alertType,String primaryId
    		,boolean pageFlag,Integer pageNo,Integer pageSize) {
    	Map<String,Object> params = Maps.newHashMap();
    	Integer begin = null;
		if(null!=pageNo && null!=pageSize) {
			if(pageNo==0) {
				pageNo = 1;
			}
			 begin = (pageNo - 1) * pageSize;
		}
		
    	params.put("alertType", alertType);
    	params.put("primaryId", primaryId);
    	params.put("begin", begin);
    	params.put("pageSize", pageSize);
    	
    	 List<Map<String,Object>> pageWithResult = Lists.newArrayList();
    	 int pageWithCount = 0;
          if (!pageFlag) {
              pageWithResult =  mapper.findprimayRalate(params);
          } else {
        	  pageWithCount = mapper.findprimayRalateCount(params);
        	  if(pageWithCount>0) {
        		  pageWithResult = mapper.findprimayRalate(params);
        	  }
              
          }
          PageResponse<Map<String,Object>> page = new PageResponse<>();
          page.setCount(pageWithCount);
          page.setResult(pageWithResult);
          return page;
	}

    @Override
    public void copyDerive(String id) {
        AlertPrimarySecondary entity = mapper.get(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String name = entity.getName();
        if (org.apache.commons.lang.StringUtils.isNotBlank(name) && name.contains("_")) {
            name = name.split("_")[0];
        }
        entity.setName(name + "_" + sdf.format(new Date()));
        entity.setStatus("0");
        mapper.insert(entity);
        alertOperateLogBiz.insertOperateLog("新增规则", entity.getCreater(), Constants.OPERATE_TYPE_INSERT, Constants.OPERATE_TYPE_INSERT_DESC, Constants.OPERATE_MODEL_ALERT_PRIMARY_SECONDARY, Constants.OPERATE_MODEL_ALERT_PRIMARY_SECONDARY_DESC, entity.getId().toString());
    }
}