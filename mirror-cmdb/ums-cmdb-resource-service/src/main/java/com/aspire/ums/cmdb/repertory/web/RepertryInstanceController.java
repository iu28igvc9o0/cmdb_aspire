package com.aspire.ums.cmdb.repertory.web;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.aspire.ums.cmdb.maintain.entity.*;
import com.aspire.ums.cmdb.module.entity.FormBean;
import com.aspire.ums.cmdb.module.service.FormService;
import com.github.pagehelper.StringUtil;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSON;
import com.aspire.ums.cmdb.common.Constants;
import com.aspire.ums.cmdb.maintain.service.CircleService;
import com.aspire.ums.cmdb.maintain.service.ColumnFilterService;
import com.aspire.ums.cmdb.maintain.service.FormValueService;
import com.aspire.ums.cmdb.maintain.service.InstanceService;
import com.aspire.ums.cmdb.module.entity.FormOptions;
import com.aspire.ums.cmdb.module.entity.Module;
import com.aspire.ums.cmdb.repertory.service.RepertoryService;
import com.aspire.ums.cmdb.util.BeansUtil;
import com.aspire.ums.cmdb.util.PagedResult;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.github.pagehelper.PageHelper;


@RestController
@RequestMapping("/cmdb/repertryInstance")
public class RepertryInstanceController {

    @Autowired
    private CircleService circleService;

    @Autowired
    private InstanceService instanceService;

    @Autowired
    private FormValueService formValueService;

    @Autowired
    private RepertoryService repertoryService;

    @Autowired
    private FormService formService;

    @Autowired
    private ColumnFilterService columnFilterService;

    private final Logger logger = Logger.getLogger(getClass());
    /**
     * 根据机房和设备ip查找设备信息. <br/>
     *
     * @param roomId
     * @param deviceIp
     * @return
     */
    @RequestMapping(value = "/queryDeviceByRoomAndIP/{roomId}/{deviceIp}")
    InstanceBaseColumn queryDeviceByRoomIdAndIP(
            @PathVariable("roomId") String roomId, @PathVariable("deviceIp") String deviceIp) {
        if (logger.isInfoEnabled()) {
            logger.info("RepertryInstanceController[queryDeviceByRoomNameAndIP] param roomId is "+roomId+", deviceIp is " + deviceIp);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("roomId", roomId);
        map.put("ip", deviceIp);
        List<InstanceBaseColumn> clist = instanceService.getInstanceBaseInfoList(map);
        if (CollectionUtils.isEmpty(clist)) {
            return null;
        }
        return clist.get(0);
    }

//    /**
//     * 查询设备IP对应业务系统的map
//     * @param freshTime 刷新时间
//     * @return Map<String, String> 设备IP对业务系统的map
//     */
//    @RequestMapping(value = "/queryDeviceIpTOBizCodeMap/{freshTime}")
//    Map<String, String> queryDeviceIpTOBizCodeMap(@PathVariable("freshTime") String freshTime) {
//        if (logger.isInfoEnabled()) {
//            logger.info("RepertryInstanceController[queryDeviceIpTOBizCodeMap] param freshTime is " + freshTime);
//        }
//
//    }

    /**
     * 获取实例数据
     * @param circleId
     * @param name
     * @param moduleId
     * @param tag
     * @param insertStartTime
     * @param insertEndTime
     * @param updateStartTime
     * @param updateEndTime
     * @param pageNumber
     * @param pageSize
     * @param sort
     * @param order
     * @return
     */
    @RequestMapping("/getInstanceByModule")
    public PagedResult getInstanceByModule(String circleId, String name, String moduleId, String tag,
                                           String insertStartTime, String insertEndTime, String updateStartTime, String updateEndTime,
                                           Integer pageNumber, Integer pageSize, String sort, String order) {
        PageHelper.startPage(pageNumber, pageSize, (sort == null ? "insertTime" : sort) + " " + (order == null ? "desc" : order));
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("circleId", circleId);
        map.put("name", name);
        map.put("moduleId", moduleId);
        map.put("tag", tag);
        map.put("insertStartTime", insertStartTime);
        map.put("insertEndTime", insertEndTime);
        map.put("updateStartTime", updateStartTime);
        map.put("updateEndTime", updateEndTime);

        return BeansUtil.toPagedResult(circleService.getInstanceByModule(map));
    }
    @RequestMapping("/listInstanceBaseInfo/{instanceIds}")
    public Object getInstanceBaseInfoList(@PathVariable("instanceIds") String instanceIds) {
        Map<String, Object> map = new HashMap<String, Object>();
        String[] list = instanceIds.split(",");
        map.put("instanceIds", list);
        List<InstanceBaseColumn> clist = instanceService.getInstanceBaseInfoList(map);
        setRoomNameAndBizSystemName(clist);
        return clist;
    }
    @RequestMapping("/getInstanceBaseInfoListByFreshTime")
    public Object getInstanceBaseInfoListByFreshTime(@RequestParam(name = "freshTime", required = false) String freshTime) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("freshTime", freshTime);
        if (!StringUtils.isEmpty(freshTime)) {
            map.put("includeDelete", 1);
        }
        List<InstanceBaseColumn> clist = instanceService.getInstanceBaseInfoList(map);
        return clist;
    }
    @RequestMapping("/getInstanceBaseInfoList")
    public Object getInstanceBaseInfoList(@RequestBody QueryListInstance queryListInstance) {
        logger.info("method[getInstanceBaseInfoList] begin queryListInstance{} ,now{}"+ System.currentTimeMillis());
//        PageHelper.startPage(queryListInstance.getPageNumber(), queryListInstance.getPageSize(), (queryListInstance.getSort() == null ? "insertTime" : queryListInstance.getSort()) + " " + (queryListInstance.getOrder() == null ? "desc" : queryListInstance.getOrder()));

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", queryListInstance.getName());
        map.put("moduleId", queryListInstance.getModuleId());
        map.put("roomId", queryListInstance.getRoomId());
        map.put("ip", queryListInstance.getIp());
        map.put("pageNumber", queryListInstance.getPageNumber()-1);
        map.put("pageSize", queryListInstance.getPageSize());
        if (queryListInstance.getSort() != null)
        {
            map.put("sort", queryListInstance.getSort());
        } else {
            map.put("sort", "insertTime");
        }
        if (queryListInstance.getOrder() != null)
        {
            map.put("order", queryListInstance.getOrder());
        } else {
            map.put("order", "desc");
        }
        if (!CollectionUtils.isEmpty(queryListInstance.getInstanceList())) {
            map.put("instanceIds", queryListInstance.getInstanceList());
        }
        List<InstanceBaseColumn> clist = instanceService.getInstanceBaseInfoList(map);
        int count = instanceService.getCount(map);
        PagedResult pr = new PagedResult();
        logger.info("method[getInstanceBaseInfoList]  queryListInstance sql exec end ,now{}"+ System.currentTimeMillis());
//        List<InstanceBaseColumn> clist = (List<InstanceBaseColumn>) pr.getDataList();
        //设置业务系统名称和机房名称
        setRoomNameAndBizSystemName(clist);
        logger.info("method[getInstanceBaseInfoList]  response ,now{}"+System.currentTimeMillis());
        pr.setDataList(clist);
        pr.setTotal(count);
        return pr;
    }

    private void setRoomNameAndBizSystemName(List<InstanceBaseColumn> clist) {
        try {
            Module module = new Module();
            //当模型id为0表示公用form
            module.setId("0");
            List<FormBean> listForm = formService.getForms(module);
            List<FormOptions> bizSystemOptionsList = Lists.newArrayList();
            List<FormOptions> roomIdOptionsList = Lists.newArrayList();
            for (FormBean formBean : listForm) {
                if (formBean.getCode().equals("bizSystem")) {
                    bizSystemOptionsList = formBean.getFormOptions();
                }
                if (formBean.getCode().equals("roomId")) {
                    roomIdOptionsList= formBean.getFormOptions();
                }
            }
            for (InstanceBaseColumn instanceBaseColumn : clist) {
                for (FormOptions options : bizSystemOptionsList) {
                    if (options.getValue().equals(instanceBaseColumn.getBizSystem())) {
                        instanceBaseColumn.setBizSystemName(options.getName());
                    }
                }
                for (FormOptions options : roomIdOptionsList) {
                    if (options.getValue().equals(instanceBaseColumn.getRoomId())) {
                        instanceBaseColumn.setRoomName(options.getName());
                    }
                }
            }
        } catch (Exception e) {
            logger.error("获取表单结果失败！", e);
        }
    }

    @RequestMapping("/getInstanceByIds/{instance_ids}")
    public List<Instance> getInstanceByIds(@PathVariable("instance_ids") String instanceIds) {
        if (StringUtil.isEmpty(instanceIds)) {
            return null;
        }
        String[] idArrays = instanceIds.split(",");
        List<Instance> response = instanceService.getInstanceByIdArrays(idArrays);
        return response;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping("/getDynamicInstanceColumn")
    public Object getDynamicInstanceColumn(@RequestBody QueryListModel queryListModel) {

        //基础属性列
        Field[] fields = new DynamicInstanceColumn().getClass().getDeclaredFields();
        String[] fieldNames = getFiledName(new DynamicInstanceColumn());
        for (int i = 0; i < fields.length; i++) {
            fieldNames[i] = fields[i].getName();
        }

        //自定义列
        List<Map> columList = new ArrayList<Map>();
        Map inMap = new HashMap();
        inMap.put("moduleId", queryListModel.getModuleId());
        List<FormValue> columns = formValueService.getFormValuesByModuleId(inMap);
        for (FormValue col : columns) {
            if (columnFilter(col)) {
                Map columMap = new HashMap();
                columMap.put("code", "key" + col.getForm().getCode());
                columMap.put("name", col.getForm().getName());

                columMap.put("id", col.getForm().getId());
                columMap.put("formType", col.getForm().getType());
                columMap.put("value", "");
                columList.add(columMap);
            }
        }


        PageHelper.startPage(queryListModel.getPageNumber(), queryListModel.getPageSize(), (queryListModel.getSort() == null ? "insertTime" : queryListModel.getSort()) + " " + (queryListModel.getOrder() == null ? "desc" : queryListModel.getOrder()));
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("circleId", queryListModel.getCircleId());
        map.put("name", queryListModel.getName());
        map.put("moduleId", queryListModel.getModuleId());
        map.put("tagName", queryListModel.getTag());
        map.put("insertStartTime", queryListModel.getInsertStartTime());
        map.put("insertEndTime", queryListModel.getInsertEndTime());
        map.put("updateStartTime", queryListModel.getUpdateStartTime());
        map.put("updateEndTime", queryListModel.getUpdateEndTime());

        //自定义列查询
        if (queryListModel.getColumCondition() != null) {
            List<Map> tmpList = new ArrayList<Map>();

            Iterator iterator = queryListModel.getColumCondition().iterator();
            while (iterator.hasNext()) {
                Map mm = (Map) iterator.next();
                if (StringUtils.isBlank((String) mm.get("value"))) {
                    iterator.remove();
                }
                for (FormValue col : columns) {
                    if (Constants.MODULE_FORM_TYPE_MULTISEL.equals((String) mm.get("formType")) && StringUtils.isNotBlank((String) mm.get("value"))) {

                        String[] newOps = ((String) mm.get("value")).split(",");
                        if (col.getFormOptions() != null && col.getFormId().equals((String) mm.get("id"))) {
                            for (FormOptions nf : col.getFormOptions()) {
                                for (String n : newOps) {
                                    if (nf.getName().equals(n)) {
                                        Map args = new HashMap();
                                        args.putAll(mm);
                                        args.put("value", nf.getValue());
                                        tmpList.add(args);
                                    }
                                }
                            }
                        }
                        iterator.remove();
                    } else if ((Constants.MODULE_FORM_TYPE_LISTSEL.equals((String) mm.get("formType")) || Constants.MODULE_FORM_TYPE_SINGLESEL.equals((String) mm.get("formType"))) && StringUtils.isNotBlank((String) mm.get("value"))) {

                        List<String> newOps = new ArrayList<String>();
                        newOps.add((String) mm.get("value"));
                        if (col.getFormOptions() != null && col.getFormId().equals((String) mm.get("id"))) {
                            for (FormOptions nf : col.getFormOptions()) {
                                for (String n : newOps) {
                                    if (nf.getName().equals(n)) {
                                        mm.put("value", nf.getValue());
                                    }
                                }
                            }
                        }
                    }
                }
            }

            queryListModel.getColumCondition().addAll(tmpList);
        }
        map.put("columCondition", queryListModel.getColumCondition());

        PagedResult pr = BeansUtil.toPagedResult(instanceService.getDynamicInstanceColumn(map));

        List<DynamicInstanceColumn> clist = (List<DynamicInstanceColumn>) pr.getDataList();
        List<Map> resultList = new ArrayList<Map>();

        Map<String, Object> resultMap = new HashMap<String, Object>();

        for (int i = 0; i < clist.size(); i++) {
            DynamicInstanceColumn dy = clist.get(i);
            Map<String, Object> imap = new HashMap<String, Object>();
            for (String fieldName : fieldNames) {
                if (!"formValues".equals(fieldName)) {
                    imap.put(fieldName, getFieldValueByName(fieldName, dy));
                }
            }

            for (FormValue fv : dy.getFormValues()) {
                StringBuilder newStr = new StringBuilder("");
                if (columnFilter(fv)) {
                    if (Constants.MODULE_FORM_TYPE_MULTISEL.equals(fv.getForm().getType())) {

                        List<String> newOps = new ArrayList<String>();
                        newOps = JSON.parseArray(fv.getFormValue(), String.class);
                        if (fv.getFormOptions() != null) {
                            for (FormOptions nf : fv.getFormOptions()) {
                                for (String n : newOps) {
                                    if (nf.getValue().equals(n)) {
                                        newStr.append(nf.getName()).append("  ");
                                    }
                                }
                            }
                        }
                        imap.put("key" + fv.getForm().getCode(), newStr);
                    } else if (Constants.MODULE_FORM_TYPE_LISTSEL.equals(fv.getForm().getType()) || Constants.MODULE_FORM_TYPE_SINGLESEL.equals(fv.getForm().getType())) {


                        List<String> newOps = new ArrayList<String>();
                        newOps.add(fv.getFormValue());
                        if (fv.getFormOptions() != null) {
                            for (FormOptions nf : fv.getFormOptions()) {
                                for (String n : newOps) {
                                    if (nf.getValue().equals(n)) {
                                        newStr.append(nf.getName()).append("  ");
                                    }
                                }
                            }
                        }
                        imap.put("key" + fv.getForm().getCode(), newStr);
                    } else {

                        imap.put("key" + fv.getForm().getCode(), fv.getFormValue());
                    }

                }
            }

            resultList.add(imap);
        }
        try {
            ColumnFilter query = new ColumnFilter();
            query.setMenuType("Repertry");
            query.setModuleId(queryListModel.getModuleId());
            ColumnFilter columnFilter = columnFilterService.getOne(query);
            if (columnFilter == null) {
                //      Map m = JSON.parseObject(columnFilter.getColumnInfo(), Map.class);
                query.setId(UUIDUtil.getUUID());
                columnFilterService.insert(query);
                columnFilter = query;
            }
            resultMap.put("columFilter", columnFilter);
        } catch (Exception e) {
            logger.error("读取字段过滤数据失败！", e);
        }

        resultMap.put("columList", columList);
        resultMap.put("success", true);
        resultMap.put("dataList", resultList);
        resultMap.put("total", pr.getTotal());
        return resultMap;
    }

    @RequestMapping("/getModule")
    public List<Module> getModule() {
        List<Module> modules = repertoryService.selectModule();
        return modules;
    }


    @RequestMapping("/updateColumnFilter")
    public Map<String, Object> updateColumnFilter(@RequestBody ColumnFilter columnFilter) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        columnFilter.setColumnInfo(JSON.toJSON(columnFilter.getColumnMap()).toString());
        try {
            columnFilterService.update(columnFilter);
        } catch (Exception e) {
            resultMap.put("success", false);
            resultMap.put("msg", e.getMessage());
            return resultMap;
        }


        resultMap.put("success", true);
        return resultMap;
    }

    /**
     * 获取属性名数组
     */
    private String[] getFiledName(Object o) {
        Field[] fields = o.getClass().getDeclaredFields();
        String[] fieldNames = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            fieldNames[i] = fields[i].getName();
        }
        return fieldNames;
    }

    /**
     * 根据属性名获取属性值
     */
    private Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[]{});
            Object value = method.invoke(o, new Object[]{});
            return value;
        } catch (Exception e) {
            return null;
        }
    }

    private boolean columnFilter(FormValue fv) {
        return !Constants.MODULE_FORM_TYPE_TABLE.equals(fv.getForm().getType()) && !Constants.MODULE_FORM_TYPE_RICHTEXT.equals(fv.getForm().getType())
                && !Constants.MODULE_FORM_TYPE_IMAGE.equals(fv.getForm().getType()) && !Constants.MODULE_FORM_TYPE_FILE.equals(fv.getForm().getType())
                && !Constants.MODULE_FORM_TYPE_MULTIROWTEXT.equals(fv.getForm().getType()) && !Constants.MODULE_FORM_TYPE_CASCADER.equals(fv.getForm().getType())
                && !Constants.MODULE_FORM_TYPE_GROUPLINE.equals(fv.getForm().getType()) && !"Y_name".equals(fv.getForm().getCode());
    }
}
