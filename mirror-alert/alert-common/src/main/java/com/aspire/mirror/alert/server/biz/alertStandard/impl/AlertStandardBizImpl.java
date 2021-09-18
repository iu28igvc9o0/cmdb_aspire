package com.aspire.mirror.alert.server.biz.alertStandard.impl;

import com.aspire.mirror.alert.server.util.AnnotationUtil;
import com.aspire.mirror.alert.server.annotation.ServiceLogAnnotation;
import com.aspire.mirror.alert.server.biz.alertStandard.AlertStandardBiz;
import com.aspire.mirror.alert.server.constant.Constants;
import com.aspire.mirror.alert.server.dao.alertStandard.AlertStandardDao;
import com.aspire.mirror.alert.server.dao.alertStandard.po.AlertStandard;
import com.aspire.mirror.alert.server.util.ExcelReaderUtil;
import com.aspire.mirror.alert.server.util.ExcelSheetPO;
import com.aspire.mirror.common.entity.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @projectName: AlertStandardBizImpl
 * @description: 实现类
 * @author: luowenbo
 * @create: 2020-06-10 16:10
 **/
@Service
@EnableAsync
public class AlertStandardBizImpl implements AlertStandardBiz {

    @Autowired
    private AlertStandardDao mapper;

    @Override
    @ServiceLogAnnotation(content = "新增规则",
            operateType = Constants.OPERATE_TYPE_INSERT,
            operateTypeDesc = Constants.OPERATE_TYPE_INSERT_DESC,
            operateModel = Constants.OPERATE_MODEL_ALERT_STANDARD,
            operateModelDesc = Constants.OPERATE_MODEL_ALERT_STANDARD_DESC)
    public void insert(AlertStandard as) {
        mapper.insert(as);
        AnnotationUtil.passAnnotationParam(AnnotationUtil.packageParam(as.getInsertPerson(),as.getId().toString()),
                this.getClass(),"insert",AlertStandard.class);
    }

    @Override
    @ServiceLogAnnotation(operateType = Constants.OPERATE_TYPE_UPDATE,
            operateTypeDesc = Constants.OPERATE_TYPE_UPDATE_DESC,
            operateModel = Constants.OPERATE_MODEL_ALERT_STANDARD,
            operateModelDesc = Constants.OPERATE_MODEL_ALERT_STANDARD_DESC)
    public void update(AlertStandard as) {
        Map<String,Object> otherParam = new HashMap<>();
        otherParam.put("content",compareWithOld(as));
        // 更新
        mapper.update(as);
        AnnotationUtil.passAnnotationParam(AnnotationUtil.packageOtherParam(otherParam,as.getInsertPerson(),as.getId().toString()),
                this.getClass(),"update",AlertStandard.class);
    }

    @Override
    @ServiceLogAnnotation(content = "删除规则",
            operateType = Constants.OPERATE_TYPE_DELETE,
            operateTypeDesc = Constants.OPERATE_TYPE_DELETE_DESC,
            operateModel = Constants.OPERATE_MODEL_ALERT_STANDARD,
            operateModelDesc = Constants.OPERATE_MODEL_ALERT_STANDARD_DESC)
    public void deleteByIds(String operator,String[] ids) {
        mapper.deleteByIds(ids);
        AnnotationUtil.passAnnotationParam(AnnotationUtil.packageParam(operator,ids),
                this.getClass(),"deleteByIds",String.class,String[].class);
    }

    @Override
    public PageResponse<AlertStandard> listWithPage(Map<String, Object> mp) {
        if(mp.get("pageNo") != null) {
            mp.put("pageNo",(Integer.parseInt(mp.get("pageNo").toString()) - 1)*Integer.parseInt(mp.get("pageSize").toString()));
        }
        int total = mapper.getAlertStandardCount(mp);
        List<AlertStandard> list =  mapper.getAlertStandardByPage(mp);
        PageResponse<AlertStandard> result = new PageResponse<>();
        result.setCount(total);
        result.setResult(list);
        return result;
    }

    @Override
    @ServiceLogAnnotation(operateType = Constants.OPERATE_TYPE_UPDATE,
            operateTypeDesc = Constants.OPERATE_TYPE_UPDATE_DESC,
            operateModel = Constants.OPERATE_MODEL_ALERT_STANDARD,
            operateModelDesc = Constants.OPERATE_MODEL_ALERT_STANDARD_DESC)
    public void operatorStatus(String operator,String[] ids) {
        // 启动或者禁用，取非进行操作
        mapper.updateStatus(ids);
        Map<String,Object> otherParam = new HashMap<>();
        otherParam.put("content",judgeOldStatus(ids[0]));
        AnnotationUtil.passAnnotationParam(AnnotationUtil.packageOtherParam(otherParam,operator,ids),
                this.getClass(),"operatorStatus",String.class,String[].class);
    }

    @Override
    @ServiceLogAnnotation(operateType = Constants.OPERATE_TYPE_UPDATE,
            operateTypeDesc = "同步历史告警",
            operateModel = Constants.OPERATE_MODEL_ALERT_STANDARD,
            operateModelDesc = Constants.OPERATE_MODEL_ALERT_STANDARD_DESC,
            content="告警标准名称同步到历史告警")
    public void updateAlertHistory(String operator,String start, String end) {
        // 获取到所有启用的告警标准化数据
        List<AlertStandard> list = mapper.getAlertStandardWithEnable();
        String[] ids = new String[list.size()];
        int index = 0;
        for(AlertStandard item : list) {
            ids[index++] = item.getId().toString();
            String[] levels = item.getAlertLevel().split(",");
            for(String lv : levels) {
                AlertStandard obj = splitLevelAndClone(item,lv);
                mapper.updateAlertHistory(obj,start,end);
            }
        }
        AnnotationUtil.passAnnotationParam(AnnotationUtil.packageParam(operator,ids),
                this.getClass(),"updateAlertHistory",String.class,String.class,String.class);
    }

    @Override
    @ServiceLogAnnotation(operateType = Constants.OPERATE_TYPE_UPDATE,
            operateTypeDesc = "同步历史告警",
            operateModel = Constants.OPERATE_MODEL_ALERT_STANDARD,
            operateModelDesc = Constants.OPERATE_MODEL_ALERT_STANDARD_DESC,
            content="告警标准名称同步到历史告警")
    public void updateAlertHistoryOneRow(String operator,String id,String start, String end) {
        // 获取指定的告警标准化实体
        AlertStandard as = mapper.getAlertStandardById(id);
        String[] levels = as.getAlertLevel().split(",");
        for(String item : levels) {
            AlertStandard obj = splitLevelAndClone(as,item);
            mapper.updateAlertHistory(obj,start,end);
        }
        AnnotationUtil.passAnnotationParam(AnnotationUtil.packageParam(operator,id),
                this.getClass(),"updateAlertHistoryOneRow",String.class,String.class,String.class,String.class);
    }

    private String compareWithOld(AlertStandard nw) {
        AlertStandard old = mapper.getAlertStandardById(nw.getId().toString());
        StringBuffer sb = new StringBuffer();
        sb.append("修改内容,(");
        if(!nw.getDeviceClass().equals(old.getDeviceClass())) {
            sb.append("设备分类：" + old.getDeviceClass() + "->" + nw.getDeviceClass());
            sb.append("、");
        }
        if(!nw.getDeviceType().equals(old.getDeviceType())) {
            sb.append("设备类型：" + old.getDeviceType() + "->" + nw.getDeviceType());
            sb.append("、");
        }
        if(!nw.getMonitorKey().equals(old.getMonitorKey())) {
            sb.append("监控指标Key：" + old.getMonitorKey() + "->" + nw.getMonitorKey());
            sb.append("、");
        }
        if(!nw.getStandardName().equals(old.getStandardName())) {
            sb.append("标准名称：" + old.getStandardName() + "->" + nw.getStandardName());
            sb.append("、");
        }
        if(!nw.getAlertDesc().equals(old.getAlertDesc())) {
            sb.append("告警描述：" + old.getAlertDesc() + "->" + nw.getAlertDesc());
            sb.append("、");
        }
        if(!nw.getStatus().equals(old.getStatus())) {
            sb.append("状态：" + old.getStatus() + "->" + nw.getStatus());
            sb.append("、");
        }
        if(!nw.getAlertLevel().equals(old.getAlertLevel())) {
            sb.append("告警等级：" + alertLevelToChinese(old.getAlertLevel()) + "->" + alertLevelToChinese(nw.getAlertLevel()));
            sb.append("、");
        }
        sb.delete(sb.length()-1,sb.length());
        sb.append(")");
        return sb.toString();
    }

    private String alertLevelToChinese(String levels){
        return levels
                .replace("，",",")
                .replace("2","低")
                .replace("3","中")
                .replace("4","高")
                .replace("5","重大");
    }

    private String alertLevelFromChinese(String levels){
        return levels
                .replace("，",",")
                .replace("低","2")
                .replace("中","3")
                .replace("高","4")
                .replace("重大","5");
    }

    private String judgeOldStatus(String id){
        AlertStandard old = mapper.getAlertStandardById(id);
        StringBuffer sb = new StringBuffer();
        sb.append("修改状态,");
        if("0".equals(old.getStatus())) {
            sb.append("由禁用改为启用");
        } else {
            sb.append("由启用改为禁用");
        }
        return sb.toString();
    }

    private AlertStandard splitLevelAndClone(AlertStandard as,String level){
        AlertStandard nwAs = new AlertStandard();
        nwAs.setDeviceClass(as.getDeviceClass());
        nwAs.setDeviceType(as.getDeviceType());
        nwAs.setMonitorKey(as.getMonitorKey());
        nwAs.setStandardName(as.getStandardName());
        nwAs.setAlertLevel(level);
        return nwAs;
    }

    @Override
    @ServiceLogAnnotation(content = "文件导入新增",
            operateType = Constants.OPERATE_TYPE_INSERT,
            operateTypeDesc = "批量新增",
            operateModel = Constants.OPERATE_MODEL_ALERT_STANDARD,
            operateModelDesc = Constants.OPERATE_MODEL_ALERT_STANDARD_DESC)
    public void uploadAS(MultipartFile file,String operator) {
        List<AlertStandard> asList = new ArrayList<>();
        try {
            List<ExcelSheetPO> excelList = ExcelReaderUtil.readExcel(file,null,7);
            for (ExcelSheetPO po : excelList) {
                List<List<Object>> dataList = po.getDataList();
                for(List<Object> item : dataList) {
                    AlertStandard as = new AlertStandard();
                    as.setDeviceClass(item.get(0).toString());
                    as.setDeviceType(item.get(1).toString());
                    as.setMonitorKey(item.get(2).toString());
                    as.setStandardName(item.get(3).toString());
                    as.setAlertDesc(item.get(4).toString());
                    String status = "启用".equals(item.get(5).toString()) ? "1" : "0";
                    as.setStatus(status);
                    as.setAlertLevel(alertLevelFromChinese(item.get(6).toString()));
                    as.setInsertPerson(operator);
                    asList.add(as);
                }
            }
            // 批量新增
            mapper.batchInsert(asList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] ids = new String[asList.size()];
        int index = 0;
        for(AlertStandard item : asList) {
            ids[index++] = item.getId().toString();
        }
        AnnotationUtil.passAnnotationParam(AnnotationUtil.packageParam(operator,ids),
                this.getClass(),"uploadAS",MultipartFile.class,String.class);
    }

    @Override
    public List<Map<String, Object>> exportAll(Map<String, Object> mp) {
        return mapper.exportAll(mp);
    }
}
