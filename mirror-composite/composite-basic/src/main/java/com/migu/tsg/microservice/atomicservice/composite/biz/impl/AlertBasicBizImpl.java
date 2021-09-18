package com.migu.tsg.microservice.atomicservice.composite.biz.impl;

import com.alibaba.fastjson.JSON;
import com.aspire.mirror.common.entity.PageResponse;
import com.migu.tsg.microservice.atomicservice.composite.biz.AlertBiz;
import com.migu.tsg.microservice.atomicservice.composite.vo.alert.AlertFieldDetailVo;
import com.migu.tsg.microservice.atomicservice.composite.vo.alert.QueryParamsVo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author baiwenping
 * @Title: AlertBasicBizImpl
 * @Package com.migu.tsg.microservice.atomicservice.composite.biz.impl
 * @Description: TODO
 * @date 2021/3/16 11:25
 */
@Component
public class AlertBasicBizImpl implements AlertBiz {
    /**
     * 判断是否基础组件，基础服务不调用外部接口
     */
    @Override
    public boolean isBasic() {
        return true;
    }

    /**
     * 查询告警模型
     *
     * @param tableName
     * @param sort
     * @return
     */
    @Override
    public List<AlertFieldDetailVo> getModelFromRedis(String tableName, String sort) {
        String modelStr = "[{\"fieldCode\":\"order_type\",\"fieldName\":\"工单类型\"},{\"fieldCode\":\"department1\",\"fieldName\":\"一级部门\",\"ciCode\":\"department1_orgName_name\"},{\"fieldCode\":\"create_time\",\"fieldName\":\"告警上报时间\"},{\"fieldCode\":\"pod_name\",\"fieldName\":\"Pod池\",\"ciCode\":\"pod_name_pod_name_name\"},{\"fieldCode\":\"device_id\",\"fieldName\":\"设备ID\",\"fieldType\":\"2\",\"isLock\":\"0\",\"dataType\":\"varchar\",\"dataLength\":\"64\",\"dataTip\":\"设备ID\",\"ciCode\":\"id\"},{\"fieldCode\":\"ext_id\",\"fieldName\":\"扩展id\",\"fieldType\":\"1\",\"isLock\":\"1\",\"dataType\":\"varchar\",\"dataLength\":\"128\",\"dataTip\":\"\",\"ciCode\":\"\"},{\"fieldCode\":\"alert_id\",\"fieldName\":\"告警id\",\"fieldType\":\"1\",\"isLock\":\"1\",\"dataType\":\"varchar\",\"dataLength\":\"64\",\"dataTip\":\"\",\"ciCode\":null},{\"fieldCode\":\"source\",\"fieldName\":\"告警来源\",\"fieldType\":\"1\",\"isLock\":\"1\",\"dataType\":\"varchar\",\"dataLength\":\"100\",\"dataTip\":\"告警来源\",\"ciCode\":null},{\"fieldCode\":\"r_alert_id\",\"fieldName\":\"原告警id\",\"fieldType\":\"1\",\"isLock\":\"1\",\"dataType\":\"varchar\",\"dataLength\":\"300\",\"dataTip\":\"\",\"ciCode\":null},{\"fieldCode\":\"cur_moni_value\",\"fieldName\":\"当前监控值\",\"fieldType\":\"1\",\"isLock\":\"1\",\"dataType\":\"varchar\",\"dataLength\":\"1024\",\"dataTip\":\"\",\"ciCode\":null},{\"fieldCode\":\"operate_status\",\"fieldName\":\"告警操作状态\",\"fieldType\":\"1\",\"isLock\":\"1\",\"dataType\":\"tinyint\",\"dataLength\":\"1\",\"ciCode\":null},{\"fieldCode\":\"idc_cabinet\",\"fieldName\":\"机柜名称\",\"fieldType\":\"2\",\"isLock\":\"0\",\"dataType\":\"varchar\",\"dataLength\":\"200\",\"dataTip\":\"\",\"ciCode\":\"idc_cabinet\"},{\"fieldCode\":\"item_id\",\"fieldName\":\"监控项ID\",\"fieldType\":\"1\",\"isLock\":\"1\",\"dataType\":\"varchar\",\"dataLength\":\"64\",\"dataTip\":\"监控项ID\",\"ciCode\":null},{\"fieldCode\":\"remark\",\"fieldName\":\"备注\",\"fieldType\":\"1\",\"isLock\":\"1\",\"dataType\":\"varchar\",\"dataLength\":\"512\",\"dataTip\":\"\",\"ciCode\":null},{\"fieldCode\":\"department2\",\"fieldName\":\"二级部门\",\"fieldType\":\"2\",\"isLock\":\"0\",\"dataType\":\"varchar\",\"dataLength\":\"100\",\"dataTip\":\"\",\"ciCode\":\"department2_orgName_name\"},{\"fieldCode\":\"item_key\",\"fieldName\":\"监控项key\",\"fieldType\":\"1\",\"isLock\":\"1\",\"dataType\":\"varchar\",\"dataLength\":\"128\",\"dataTip\":\"\",\"ciCode\":null},{\"fieldCode\":\"alert_level\",\"fieldName\":\"告警级别\",\"fieldType\":\"1\",\"isLock\":\"1\",\"dataType\":\"varchar\",\"dataLength\":\"20\",\"ciCode\":null},{\"fieldCode\":\"device_ip\",\"fieldName\":\"设备IP\",\"fieldType\":\"1\",\"isLock\":\"1\",\"dataType\":\"varchar\",\"dataLength\":\"100\",\"dataTip\":\"\",\"ciCode\":\"\"},{\"fieldCode\":\"idc_type\",\"fieldName\":\"资源池\",\"fieldType\":\"1\",\"isLock\":\"1\",\"dataType\":\"varchar\",\"dataLength\":\"128\",\"dataTip\":\"资源池\",\"ciCode\":\"idcType_idc_name_name\"},{\"fieldCode\":\"biz_sys\",\"fieldName\":\"所属业务系统\",\"fieldType\":\"1\",\"isLock\":\"1\",\"dataType\":\"varchar\",\"dataLength\":\"128\",\"dataTip\":\"\",\"ciCode\":\"bizSystem_bizSystem_name\"},{\"fieldCode\":\"device_class\",\"fieldName\":\"设备分类\",\"fieldType\":\"2\",\"isLock\":\"0\",\"dataType\":\"varchar\",\"dataLength\":\"128\",\"dataTip\":\"设备分类\",\"ciCode\":\"device_class_dict_note_name\"},{\"fieldCode\":\"key_comment\",\"fieldName\":\"告警标题\",\"fieldType\":\"1\",\"isLock\":\"1\",\"dataType\":\"varchar\",\"dataLength\":\"512\",\"dataTip\":\"\",\"ciCode\":null},{\"fieldCode\":\"moni_index\",\"fieldName\":\"告警内容\",\"fieldType\":\"1\",\"isLock\":\"1\",\"dataType\":\"varchar\",\"dataLength\":\"1024\",\"dataTip\":\"\",\"ciCode\":null},{\"fieldCode\":\"alert_start_time\",\"fieldName\":\"告警开始时间\",\"fieldType\":\"1\",\"isLock\":\"1\",\"dataType\":\"datetime\",\"dataLength\":\"\",\"dataTip\":\"告警开始时间\",\"ciCode\":null},{\"fieldCode\":\"cur_moni_time\",\"fieldName\":\"当前监控时间\",\"fieldType\":\"1\",\"isLock\":\"1\",\"dataType\":\"datetime\",\"dataLength\":\"\",\"dataTip\":\"\",\"ciCode\":null},{\"fieldCode\":\"order_id\",\"fieldName\":\"工单Id\",\"fieldType\":\"1\",\"isLock\":\"1\",\"dataType\":\"varchar\",\"dataLength\":\"100\",\"dataTip\":\"\",\"ciCode\":null},{\"fieldCode\":\"source_room\",\"fieldName\":\"机房位置\",\"fieldType\":\"2\",\"isLock\":\"0\",\"dataType\":\"varchar\",\"dataLength\":\"100\",\"dataTip\":\"机房位置\",\"ciCode\":\"roomId_room_name_name\"},{\"fieldCode\":\"device_mfrs\",\"fieldName\":\"设备厂商\",\"fieldType\":\"2\",\"isLock\":\"0\",\"dataType\":\"varchar\",\"dataLength\":\"128\",\"dataTip\":\"\",\"ciCode\":\"device_mfrs_value_name\"},{\"fieldCode\":\"device_model\",\"fieldName\":\"设备型号\",\"fieldType\":\"2\",\"isLock\":\"0\",\"dataType\":\"varchar\",\"dataLength\":\"128\",\"dataTip\":\"\",\"ciCode\":\"device_model\"},{\"fieldCode\":\"host_name\",\"fieldName\":\"主机名\",\"fieldType\":\"2\",\"isLock\":\"0\",\"dataType\":\"varchar\",\"dataLength\":\"128\",\"dataTip\":\"\",\"ciCode\":\"HOST_NAME\"},{\"fieldCode\":\"moni_object\",\"fieldName\":\"监控对象\",\"fieldType\":\"1\",\"isLock\":\"1\",\"dataType\":\"varchar\",\"dataLength\":\"512\",\"dataTip\":\"监控对象\",\"ciCode\":null},{\"fieldCode\":\"object_type\",\"fieldName\":\"告警类型\",\"fieldType\":\"1\",\"isLock\":\"1\",\"dataType\":\"varchar\",\"dataLength\":\"50\",\"dataTip\":\"\",\"ciCode\":null},{\"fieldCode\":\"alert_count\",\"fieldName\":\"告警收敛数量\",\"fieldType\":\"1\",\"isLock\":\"1\",\"dataType\":\"int\",\"dataLength\":\"11\",\"dataTip\":\"\",\"ciCode\":null},{\"fieldCode\":\"notify_status\",\"fieldName\":\"通知状态\",\"fieldType\":\"1\",\"isLock\":\"1\",\"dataType\":\"varchar\",\"dataLength\":\"2\",\"dataTip\":\"通知状态,0-未通知，1-已通知\",\"ciCode\":\"\"},{\"fieldCode\":\"order_status\",\"fieldName\":\"工单状态\",\"fieldType\":\"1\",\"isLock\":\"1\",\"dataType\":\"varchar\",\"dataLength\":\"50\",\"ciCode\":null},{\"fieldCode\":\"device_type\",\"fieldName\":\"设备类型\",\"fieldType\":\"2\",\"isLock\":\"0\",\"dataType\":\"varchar\",\"dataLength\":\"255\",\"dataTip\":\"设备类型\",\"ciCode\":\"device_type_dict_note_name\"}]";
        List<AlertFieldDetailVo> list = JSON.parseArray(modelStr, AlertFieldDetailVo.class);
        return list;
    }

    /**
     * 统计条件下告警对应的设备数量
     *
     * @param queryParams
     * @return
     */
    @Override
    public PageResponse<Map<String, Object>> queryDeviceAlertList(QueryParamsVo queryParams) {
        return new PageResponse<>();
    }

}
