package com.aspire.mirror.inspection.api.dto.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.aspire.mirror.inspection.api.dto.ReportItemValue;
import org.springframework.beans.BeanUtils;

import com.aspire.mirror.inspection.api.dto.ReportItemDetailResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(Include.NON_NULL)
public class ReportItemDataWrap extends ReportItemDetailResponse {
    private static final long serialVersionUID = 8860380736519462283L;

    // 指标相关的设备信息
    @JsonProperty
    private String name;

    @JsonProperty("device_type")
    private String deviceType;

    @JsonProperty
    private String ip;

    // 指标相关的触发器名、监控项名、监控项单位
    @JsonProperty
    private String expression;
    @JsonProperty("item_name")
    private String itemName;
    @JsonProperty
    private String unit;

    @JsonProperty("device_class")
    private String deviceClass;

    @JsonProperty("biz_system")
    private String bizSystem;

    @JsonProperty("idc_type")
    private String idcType;

    @JsonProperty("result_name")
    private String resultName;

    @JsonProperty("result_desc")
    private String resultDesc;

    @JsonProperty("biz_group")
    private String bizGroup;

    @JsonProperty("item_group")
    private String itemGroup;
    //    @JsonProperty("device_name")
//	private String deviceName;
    public static List<ReportItemDataWrap> parse(List<ReportItemDTO> itemList) {
        if (itemList == null) {
            return null;
        }
        List<ReportItemDataWrap> resultList = new ArrayList<>();
        for (ReportItemDTO item : itemList) {
            if (!CollectionUtils.isEmpty(item.getReportItemValueList())) {
                for (ReportItemValue reportItemValue : item.getReportItemValueList()) {
                    ReportItemDataWrap itemData = new ReportItemDataWrap();
                    BeanUtils.copyProperties(item, itemData);
                    itemData.setValue(reportItemValue.getResultValue());
                    itemData.setResultName(reportItemValue.getResultName());
                    // 特殊处理，要求存在描述则用描述代替巡检值展示
                    if (!StringUtils.isEmpty(reportItemValue.getResultDesc())) {
                        itemData.setValue(reportItemValue.getResultDesc());
                    }
                    itemData.setStatus(reportItemValue.getResultStatus());
                    extResultParse(item, itemData);
                    resultList.add(itemData);
                }
            } else {
                ReportItemDataWrap itemData = new ReportItemDataWrap();
                BeanUtils.copyProperties(item, itemData);
                extResultParse(item, itemData);
                resultList.add(itemData);
            }
        }
        return resultList;
    }

    private static void extResultParse(ReportItemDTO item, ReportItemDataWrap itemData) {
        if (item.getReportItemExt() != null) {
            itemData.setDeviceClass(item.getReportItemExt().getDeviceClass());
            itemData.setDeviceType(item.getReportItemExt().getDeviceType());
            itemData.setDeviceName(item.getReportItemExt().getDeviceName());
            itemData.setIdcType(item.getReportItemExt().getIdcType());
            itemData.setBizSystem(item.getReportItemExt().getBizSystem());
            itemData.setName(item.getReportItemExt().getItemName());
            itemData.setItemName(item.getReportItemExt().getItemName());
            itemData.setExpression(item.getReportItemExt().getExpression());
            itemData.setItemGroup(item.getReportItemExt().getItemGroup());
            itemData.setBizGroup(item.getReportItemExt().getBizGroup());
            if (item.getObjectId().indexOf(":") == -1) {
                itemData.setIp(item.getObjectId());
            } else {
                itemData.setIp(item.getObjectId().substring(item.getObjectId().indexOf(":") + 1, item.getObjectId().length()));
            }
        }
    }
}
