package com.aspire.mirror.indexadapt.adapt.migubizmonitordb.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.aspire.mirror.indexadapt.adapt.BaseRawIndexData;

/**
 * 日志监控告警基本配置对象
 */
public class BusinessTrigger extends BaseRawIndexData implements Serializable {

    private static final long serialVersionUID = 5701626162097559591L;

    /**
     * 主键.
     */
    private String id;

    /**
     * 名称.
     */
    private String triggerName;

    /**
     * 监控项key.
     */
    private String keyName;

    /**
     * 告警描述.
     */
    private String triggerDesc;

    /**
     * 日志索引.
     */
    private String triggerIndex;

    /**
     * 业务模块code.
     */
    private String code;

    private String codeDesc;

    /**
     * 业务系统code.
     */
    private String sysCode;

    private String sysCodeDesc;

    /**
     * 搜搜内容.
     */
    private String triggerContent;

    /**
     * 告警类型(0:事件数告警 1:字段统计告警).
     */
    private String triggerType;

    private String triggerTypeDesc;

    /**
     * 告警级别(information:提示 warning:警告 average:一般 high:严重 disaster:致命).
     */
    private String triggerLevel;

    /**
     * 触发时间范围单位(0:分钟 1:小时).
     */
    private String triggerTimeUnit;

    private String triggerTimeUnitDesc;

    /**
     * 触发时间范围值.
     */
    private String triggerTimeValue;

    /**
     * 聚合类型( 0:总计 1:最大值 2:最小值 3:平均值 4:占比).
     */
    private String triggerValueType;

    private String triggerValueTypeDesc;

    /**
     * 触发比较(0：大于 1：小于,2：在区间内,3:在区间外).
     */
    private String triggerCompare;

    private String triggerCompareDesc;

    /**
     * 触发阀值.
     */
    private String triggerThreShold;
    /**
     * 区间上限
     */
    private String triggerSectionMax;
    /**
     * 区间下限
     */
    private String triggerSectionMin;
    /**
     * 基线时间
     */
    private String triggerBaseline;

    /**
     * 检测频率单位(0: 分钟 1:小时).
     */
    private String triggerCheckUnit;

    private String triggerCheckUnitDesc;

    /**
     * 检测频率值.
     */
    private String triggerCheckValue;

    /**
     * 备注.
     */
    private String triggerRemark;

    /**
     * 创建人编号.
     */
    private String createBy;

    /**
     * 创建人姓名.
     */
    private String createByName;

    /**
     * 创建时间.
     */
    private Date createTime;

    /**
     * 修改时间.
     */
    private Date updateTime;

    /**
     * 状态(0:正常 1:删除).
     */
    private String status;

    /**
     * zabbix监控项编号
     */
    private String zabbixItemId;

    /**
     * zabbix告警编号
     */
    private String zabbixTriggerId;

    /**
     * 过滤字段子集集合
     private List<BusinessTriggerFilter> list;
     */

    /**
     * 字段统计子集
     * private BusinessTriggerField businessTriggerField;
     */

    private Date lastLogTime;

    private String lastLogTimeStr;

    private String lastLogValue;

    /**
     * 告警状态
     */
    private String triggerStatus;

    /**
     * 0值是否监控
     */
    private String isZero;

    /**
     * 业务指标对应的上报主机
     */
    private String hostIp;

    /**
     * 业务指标对应状态
     */
    private String zabbixStatus;

    /**
     * 业务指标关联触发器数量
     */
    private String triggerCountStr;

    private String order;

    private String sort;

    private List<String> sysCodes = new ArrayList<String>();

    private Boolean isOpr;

    private String ztsj;// 指标数据

    private String ztsjName;

    private String upTypeDesc; // 主题数据接入方式

    private String calculationType; // 计算时间点1:数据到达时，2:周期性

    private String cycleValue; // calculationType为2时，周期性取值

    private String cycleType; // calculationType为2时，周期性取值类型，0:分1:小时:2:天

    private String monitorStatics; // 指标项计算

    private String zbxJson;

    private String zbxJsonStr;

    private String dataStatus; // 是否新数据0：新，1：旧,

    private String jkzbCode; // 主题编码

    private Date logStartTime;

    private String logStartTimeStr;

    private String jkzbName;

    private String dimTypes;

    private String dimIds;

    private String template_id;//模板ID

    private String item_id;//监控子系统创建好监控项返回的监控项ID

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public String getDimIds() {
        return dimIds;
    }

    public void setDimIds(String dimIds) {
        this.dimIds = dimIds;
    }

    public String getDimTypes() {
        return dimTypes;
    }

    public void setDimTypes(String dimTypes) {
        this.dimTypes = dimTypes;
    }

    public String getJkzbName() {
        return jkzbName;
    }

    public void setJkzbName(String jkzbName) {
        this.jkzbName = jkzbName;
    }

    public String getZtsjName() {
        return ztsjName;
    }

    public void setZtsjName(String ztsjName) {
        this.ztsjName = ztsjName;
    }

    public String getUpTypeDesc() {
        return upTypeDesc;
    }

    public void setUpTypeDesc(String upTypeDesc) {
        this.upTypeDesc = upTypeDesc;
    }

    public String getZbxJsonStr() {
        return zbxJsonStr;
    }

    public void setZbxJsonStr(String zbxJsonStr) {
        this.zbxJsonStr = zbxJsonStr;
    }

    public Date getLogStartTime() {
        return logStartTime;
    }

    public void setLogStartTime(Date logStartTime) {
        this.logStartTime = logStartTime;
    }

    public String getLogStartTimeStr() {
        return logStartTimeStr;
    }

    public void setLogStartTimeStr(String logStartTimeStr) {
        this.logStartTimeStr = logStartTimeStr;
    }

    public String getJkzbCode() {
        return jkzbCode;
    }

    public void setJkzbCode(String jkzbCode) {
        this.jkzbCode = jkzbCode;
    }

    public String getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(String dataStatus) {
        this.dataStatus = dataStatus;
    }

    public String getZbxJson() {
        return zbxJson;
    }

    public void setZbxJson(String zbxJson) {
        this.zbxJson = zbxJson;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTriggerName() {
        return this.triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public String getKeyName() {
        return this.keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getTriggerDesc() {
        return this.triggerDesc;
    }

    public void setTriggerDesc(String triggerDesc) {
        this.triggerDesc = triggerDesc;
    }

    public String getTriggerIndex() {
        return this.triggerIndex;
    }

    public void setTriggerIndex(String triggerIndex) {
        this.triggerIndex = triggerIndex;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTriggerContent() {
        return this.triggerContent;
    }

    public void setTriggerContent(String triggerContent) {
        this.triggerContent = triggerContent;
    }

    public String getTriggerType() {
        return this.triggerType;
    }

    public void setTriggerType(String triggerType) {
        this.triggerType = triggerType;
    }

    public String getTriggerLevel() {
        return this.triggerLevel;
    }

    public void setTriggerLevel(String triggerLevel) {
        this.triggerLevel = triggerLevel;
    }

    public String getTriggerTimeUnit() {
        return this.triggerTimeUnit;
    }

    public void setTriggerTimeUnit(String triggerTimeUnit) {
        this.triggerTimeUnit = triggerTimeUnit;
    }

    public String getTriggerTimeValue() {
        return this.triggerTimeValue;
    }

    public void setTriggerTimeValue(String triggerTimeValue) {
        this.triggerTimeValue = triggerTimeValue;
    }

    public String getTriggerValueType() {
        return this.triggerValueType;
    }

    public void setTriggerValueType(String triggerValueType) {
        this.triggerValueType = triggerValueType;
    }

    public String getTriggerCompare() {
        return this.triggerCompare;
    }

    public void setTriggerCompare(String triggerCompare) {
        this.triggerCompare = triggerCompare;
    }

    public String getTriggerThreShold() {
        return this.triggerThreShold;
    }

    public void setTriggerThreShold(String triggerThreShold) {
        this.triggerThreShold = triggerThreShold;
    }

    public String getTriggerCheckUnit() {
        return this.triggerCheckUnit;
    }

    public void setTriggerCheckUnit(String triggerCheckUnit) {
        this.triggerCheckUnit = triggerCheckUnit;
    }

    public String getTriggerCheckValue() {
        return this.triggerCheckValue;
    }

    public void setTriggerCheckValue(String triggerCheckValue) {
        this.triggerCheckValue = triggerCheckValue;
    }

    public String getTriggerRemark() {
        return this.triggerRemark;
    }

    public void setTriggerRemark(String triggerRemark) {
        this.triggerRemark = triggerRemark;
    }

    public String getCreateBy() {
        return this.createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateByName() {
        return this.createByName;
    }

    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }

//    public List<BusinessTriggerFilter> getList() {
//        return list;
//    }
//
//    public void setList(List<BusinessTriggerFilter> list) {
//        this.list = list;
//    }

//    public BusinessTriggerField getBusinessTriggerField() {
//        return businessTriggerField;
//    }
//
//    public void setBusinessTriggerField(BusinessTriggerField businessTriggerField) {
//        this.businessTriggerField = businessTriggerField;
//    }

    public Date getLastLogTime() {
        return lastLogTime;
    }

    public void setLastLogTime(Date lastLogTime) {
        this.lastLogTime = lastLogTime;
    }

    public String getLastLogValue() {
        return lastLogValue;
    }

    public void setLastLogValue(String lastLogValue) {
        this.lastLogValue = lastLogValue;
    }

    public String getCodeDesc() {
        return codeDesc;
        // if(code == null){
        // return "业务模块";
        // }
        // return getCode("cmdb.sys.module", code);
    }

    public void setCodeDesc(String codeDesc) {
        this.codeDesc = codeDesc;
    }

    public String getSysCodeDesc() {
        return sysCodeDesc;
        // if(sysCode == null){
        // return "业务系统";
        // }
        // return getCode("flow.sys", sysCode);
    }

    public void setSysCodeDesc(String sysCodeDesc) {
        this.sysCodeDesc = sysCodeDesc;
    }

    public String getTriggerCheckUnitDesc() {
        return triggerCheckUnitDesc;
        // if(triggerCheckUnit == null){
        // return "检测频率";
        // }
        // return getCode("ums.businessTrigger.triggerCheckUnit", triggerCheckUnit);
    }

    public void setTriggerCheckUnitDesc(String triggerCheckUnitDesc) {
        this.triggerCheckUnitDesc = triggerCheckUnitDesc;
    }

    public String getTriggerTimeUnitDesc() {
        return triggerTimeUnitDesc;
        // if(triggerTimeUnit == null){
        // return "时间";
        // }
        // return getCode("ums.businessTrigger.triggerTimeUnit", triggerTimeUnit);
    }

    public void setTriggerTimeUnitDesc(String triggerTimeUnitDesc) {
        this.triggerTimeUnitDesc = triggerTimeUnitDesc;
    }

    public String getTriggerValueTypeDesc() {
        return triggerValueTypeDesc;
        // if(triggerValueType == null){
        // return "聚合类型";
        // }
        // return getCode("ums.businessTrigger.triggerValueType", triggerValueType);
    }

    public void setTriggerValueTypeDesc(String triggerValueTypeDesc) {
        this.triggerValueTypeDesc = triggerValueTypeDesc;
    }

    public String getTriggerCompareDesc() {
        return triggerCompareDesc;
        // if(triggerCompare == null){
        // return "触发比较";
        // }
        // return getCode("ums.businessTrigger.triggerCompare", triggerCompare);
    }

    public void setTriggerCompareDesc(String triggerCompareDesc) {
        this.triggerCompareDesc = triggerCompareDesc;
    }

    public String getTriggerTypeDesc() {
        return triggerTypeDesc;
        // if(triggerType == null){
        // return "告警类型";
        // }
        // return getCode("ums.businessTrigger.triggerType", triggerType);
    }

    public void setTriggerTypeDesc(String triggerTypeDesc) {
        this.triggerTypeDesc = triggerTypeDesc;
    }

    public String getZabbixItemId() {
        return zabbixItemId;
    }

    public void setZabbixItemId(String zabbixItemId) {
        this.zabbixItemId = zabbixItemId;
    }

    public String getZabbixTriggerId() {
        return zabbixTriggerId;
    }

    public void setZabbixTriggerId(String zabbixTriggerId) {
        this.zabbixTriggerId = zabbixTriggerId;
    }

    public String getTriggerSectionMax() {
        return triggerSectionMax;
    }

    public void setTriggerSectionMax(String triggerSectionMax) {
        this.triggerSectionMax = triggerSectionMax;
    }

    public String getTriggerSectionMin() {
        return triggerSectionMin;
    }

    public void setTriggerSectionMin(String triggerSectionMin) {
        this.triggerSectionMin = triggerSectionMin;
    }

    public String getTriggerBaseline() {
        return triggerBaseline;
    }

    public void setTriggerBaseline(String triggerBaseline) {
        this.triggerBaseline = triggerBaseline;
    }

    public String getTriggerStatus() {
        if (null == triggerStatus) {
            return "0";
        }
        return triggerStatus;
    }

    public void setTriggerStatus(String triggerStatus) {
        this.triggerStatus = triggerStatus;
    }

    public String getIsZero() {
        return isZero;
    }

    public void setIsZero(String isZero) {
        this.isZero = isZero;
    }

    public String getHostIp() {
        return hostIp;
    }

    public void setHostIp(String hostIp) {
        this.hostIp = hostIp;
    }

    public String getZabbixStatus() {
        return zabbixStatus;
    }

    public void setZabbixStatus(String zabbixStatus) {
        this.zabbixStatus = zabbixStatus;
    }

    public String getTriggerCountStr() {
        return triggerCountStr;
    }

    public void setTriggerCountStr(String triggerCountStr) {
        this.triggerCountStr = triggerCountStr;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getLastLogTimeStr() {
        return lastLogTimeStr;
    }

    public void setLastLogTimeStr(String lastLogTimeStr) {
        this.lastLogTimeStr = lastLogTimeStr;
    }

    public List<String> getSysCodes() {
        return sysCodes;
    }

    public void setSysCodes(List<String> sysCodes) {
        this.sysCodes = sysCodes;
    }

    public Boolean getIsOpr() {
        return isOpr;
    }

    public void setIsOpr(Boolean isOpr) {
        this.isOpr = isOpr;
    }

    public String getZtsj() {
        return ztsj;
    }

    public void setZtsj(String ztsj) {
        this.ztsj = ztsj;
    }

    public String getCalculationType() {
        return calculationType;
    }

    public void setCalculationType(String calculationType) {
        this.calculationType = calculationType;
    }

    public String getCycleValue() {
        return cycleValue;
    }

    public void setCycleValue(String cycleValue) {
        this.cycleValue = cycleValue;
    }

    public String getCycleType() {
        return cycleType;
    }

    public void setCycleType(String cycleType) {
        this.cycleType = cycleType;
    }

    public String getMonitorStatics() {
        return monitorStatics;
    }

    public void setMonitorStatics(String monitorStatics) {
        this.monitorStatics = monitorStatics;
    }

    @Override
    public Integer getIndexSeq() {
        return id.hashCode();
    }
}
