package com.aspire.ums.cmdb.repertory.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.collect.entity.CollectChangeLogEntity;
import com.aspire.ums.cmdb.collect.service.CollectChangeLogService;
import com.aspire.ums.cmdb.common.Constants;
import com.aspire.ums.cmdb.dict.entity.ConfigDict;
import com.aspire.ums.cmdb.dict.entity.Dict;
import com.aspire.ums.cmdb.dict.service.ConfigDictService;
import com.aspire.ums.cmdb.maintain.entity.*;
import com.aspire.ums.cmdb.maintain.service.CircleService;
import com.aspire.ums.cmdb.maintain.service.ColumnFilterService;
import com.aspire.ums.cmdb.maintain.service.FormValueService;
import com.aspire.ums.cmdb.maintain.service.InstanceService;
import com.aspire.ums.cmdb.module.entity.FormBean;
import com.aspire.ums.cmdb.module.entity.FormOptions;
import com.aspire.ums.cmdb.module.entity.Module;
import com.aspire.ums.cmdb.module.service.FormService;
import com.aspire.ums.cmdb.repertory.entity.BatchUpdateData;
import com.aspire.ums.cmdb.repertory.service.RepertoryService;
import com.aspire.ums.cmdb.util.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.StringUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;


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

    @Autowired
    private CollectChangeLogService changeLogService;

    @Autowired
    private ConfigDictService configDictService;

    private final Logger logger = Logger.getLogger(getClass());

    @RequestMapping(value = "/getAuthDeviceData")
    Map<String, Object> getAuthDeviceData(@RequestBody Map<String, String> request) {
        String area = request.get("area");
        String bizSystem = request.get("bizSystem");
        String deviceType = request.get("deviceType");
        String[] areaArray =  area.split(",");
        List<String> dictIds = new ArrayList<>();
        for (String areaItem : areaArray) {
            String[] subAreaItem = areaItem.split("_");
            for (String dictId : subAreaItem) {
                dictIds.add(dictId);
            }
        }
        Map<String, Object> resultMap = Maps.newHashMap();
        List<Dict> configDict = configDictService.getDictByIds(dictIds);
        Map<String, Dict> dictMap = transferDictToMap(configDict);
        for (String areaItem : areaArray) {
            String[] subAreaItem = areaItem.split("_");
            Map<String, Object> param = Maps.newHashMap();
            for (String dictId : subAreaItem) {
                if (dictMap.containsKey(dictId)) {
                    List<String> paramItem = (List<String>) param.get(dictMap.get(dictId).getColName());
                    if (paramItem != null) {
                        paramItem.add(dictMap.get(dictId).getDictNote());
                    } else {
                        paramItem = Lists.newArrayList();
                        paramItem.add(dictMap.get(dictId).getDictNote());
                        param.put(dictMap.get(dictId).getColName(), paramItem);
                    }
                }
            }
            if (StringUtils.isNotEmpty(bizSystem)) {
                List<Dict> dictList = configDictService.getDictByIds(Arrays.asList(bizSystem.split(",")));
                if (!CollectionUtils.isEmpty(dictList)) {
                    List<String> bizSystemList = Lists.newArrayList();
                    for (Dict dict : dictList) {
                        bizSystemList.add(dict.getDictNote());
                    }
                    param.put("bizSystem", bizSystemList);
                }
            }
            if (StringUtils.isNotEmpty(deviceType)) {
                List<Dict> dictList = configDictService.getDictByIds(Arrays.asList(deviceType.split(",")));
                if (!CollectionUtils.isEmpty(dictList)) {
                    List<String> deviceTypeList = Lists.newArrayList();
                    for (Dict dict : dictList) {
                      deviceTypeList.add(dict.getDictNote());
                    }
                    param.put("deviceType", deviceTypeList);
                }
            }
            List<Map> instances = instanceService.getInstanceByAuthParam(param);
            resultMap.put(subAreaItem[subAreaItem.length-1], instances);
        }
        return resultMap;
    }

    private Map<String,Dict> transferDictToMap(List<Dict> configDict) {
        Map<String, Dict> map = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(configDict)) {
            for (Dict dict : configDict) {
                map.put(dict.getDictId().toString(), dict);
            }
        }
        return map;
    }

    /**
     * 根据机房和设备ip查找设备信息. <br/>
     *
     * @param deviceIp
     * @return
     */
    @RequestMapping(value = "/queryDeviceByRoomAndIP")
    InstanceBaseColumn queryDeviceByRoomIdAndIP(
            @RequestParam(value = "idc", required = false) String idc, @RequestParam("deviceIp") String deviceIp) {
        if (logger.isInfoEnabled()) {
            logger.info("RepertryInstanceController[queryDeviceByRoomNameAndIP] param roomId is " + idc + ", deviceIp is " + deviceIp);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("idc", idc);
        map.put("ip", deviceIp);
        List<InstanceBaseColumn> clist = instanceService.getInstanceBaseInfoByIp(map);
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
        long time1 = System.currentTimeMillis();
        //基础属性列
        Field[] fields = new DynamicInstanceColumn().getClass().getDeclaredFields();
        String[] fieldNames = getFiledName(new DynamicInstanceColumn());
        for (int i = 0; i < fields.length; i++) {
            fieldNames[i] = fields[i].getName();
        }
        long time2 = System.currentTimeMillis();
        logger.info("------1.------转换列消耗时间：" + (time2-time1) + "ms.整体消耗时间：" + (time2-time1) + "ms");
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
        long time3 = System.currentTimeMillis();
        logger.info("------2.------自定义列消耗时间：" + (time3-time2) + "ms.整体消耗时间：" + (time3-time1) + "ms");

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
        long time4 = System.currentTimeMillis();
        logger.info("------3.------拼装查询条件消耗时间：" + (time4-time3) + "ms.整体消耗时间：" + (time4-time1) + "ms");

        PagedResult pr = BeansUtil.toPagedResult(instanceService.getDynamicInstanceColumn(map));
        long time5 = System.currentTimeMillis();
        logger.info("------4.------查询实例消耗时间：" + (time5-time4) + "ms.整体消耗时间：" + (time5-time1) + "ms");

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
        long time6 = System.currentTimeMillis();
        logger.info("------5.------组装实例列数据消耗时间：" + (time6-time5) + "ms.整体消耗时间：" + (time6-time1) + "ms");

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
        long time7 = System.currentTimeMillis();
        logger.info("------6.------查询筛选列消耗时间：" + (time7-time6) + "ms.整体消耗时间：" + (time7-time1) + "ms");

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

    @RequestMapping("/getOrInsertColumnFilter/{menuType}/{moduleId}")
    public JSONObject getOrInsertColumnFilter(@PathVariable("menuType") String menuType, @PathVariable("moduleId") String moduleId) {
        ColumnFilter query = new ColumnFilter();
        query.setMenuType(menuType);
        query.setModuleId(moduleId);
        ColumnFilter columnFilter = columnFilterService.getOne(query);
        if (columnFilter == null) {
            query.setId(UUIDUtil.getUUID());
            query.setInsertTime(new Date());
            columnFilterService.insert(query);
            columnFilter = query;
        }
        return (JSONObject) JSON.toJSON(columnFilter);
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

    /**
     * 根据instanceId批量更新数据
     */
    @RequestMapping("/updateByBatch")
    private Map<String, Object> updateByBatch(@RequestBody BatchUpdateData updateData) {
        logger.info("method[updateByBatch] begin requestbody: " + updateData.toString());
        Map<String, Object> resultMap = new HashMap<>();
        resultMap = validData(updateData);
        if (resultMap.size() > 0) {
            return resultMap;
        }
        String moduleId = updateData.getModuleId();
        List<String> instanceNames = updateData.getInstanceNames();
        Map<String, FormValue> formValues = updateData.getFormValues();
        Map<String, Object> queryInstanceMap = new HashMap<>();
        queryInstanceMap.put("instanceNames", instanceNames);
        queryInstanceMap.put("moduleId", moduleId);
        // 根据名称和模型字段查询所有相关的instanceId
        List<String> queryInstances = instanceService.getInstanceByNameAndModuleId(queryInstanceMap);
        logger.info("query instances by name and moduleId ------ result: " + queryInstances);
        String batchId = "B" + new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
        for (String instanceId : queryInstances) {
            Map<String, String> formValueMap = new HashMap<>();
            List<Map<String, String>> maps =  formValueService.getFormValueMapByInstanceId(instanceId);
            for (Map<String, String>  map: maps) {
                formValueMap.put(map.get("formCode"), map.get("formValue"));
            }
            for (String key : formValues.keySet()) {
                if (formValueMap.containsKey(key)) {
                    if (!formValueMap.get(key).equals(formValues.get(key).getFormValue())) {
                        insertChangeLog(batchId, formValues, instanceId, key, formValueMap);
                        FormValue formValue = new FormValue();
//                        Map<String, String> updateMap = new HashMap<>();
                        formValue.setFormCode(key);
                        formValue.setFormValue(formValues.get(key).getFormValue());
                        formValue.setInstanceId(instanceId);
//                        updateMap.put("formCode", key);
//                        updateMap.put("formValue", formValues.get(key).getFormValue());
//                        updateMap.put("instanceId", instanceId);
                        formValueService.update(formValue);
                    }
                } else {
                    insertChangeLog(batchId, formValues, instanceId, key, formValueMap);
                    FormValue formValue = new FormValue();
                    formValue.setId(UUIDUtil.getUUID());
                    formValue.setInstanceId(instanceId);
                    formValue.setFormCode(key);
                    formValue.setFormValue(formValues.get(key).getFormValue());
                    formValue.setFormId(formValues.get(key).getFormId());
                    formValueService.insertSingle(formValue);
                }
            }
        }
        resultMap.put("success", true);
        resultMap.put("msg", "更新成功");
        return resultMap;
    }

    private Map<String, Object> validData(BatchUpdateData updateData) {
        Map<String, Object> resultMap = new HashMap<>();
        String moduleId = updateData.getModuleId();
        List<String> instanceNames = updateData.getInstanceNames();
        Map<String, FormValue> formValues = updateData.getFormValues();
        if (null == moduleId) {
            resultMap.put("success", false);
            resultMap.put("msg", "更新失败，未选择模型");
            return resultMap;
        }
        if (instanceNames.size() == 0) {
            resultMap.put("success", false);
            resultMap.put("msg", "更新失败，未选择需要更新的实例名称");
            return resultMap;
        }
        if (formValues.size() == 0) {
            resultMap.put("success", false);
            resultMap.put("msg", "更新失败，没有需要更新的数据");
            return resultMap;
        }
        if (formValues.containsKey("ip")) {
            resultMap.put("success", false);
            resultMap.put("msg", "更新失败, 设备IP不可更改");
            return resultMap;
        }
        return resultMap;
    }

    private void insertChangeLog (String batchId, Map<String, FormValue> formValues, String instanceId, String key,  Map<String, String> formValueMap) {
        CollectChangeLogEntity changeLogEntity = new CollectChangeLogEntity();
        changeLogEntity.setId(UUIDUtil.getUUID());
        changeLogEntity.setBatchId(batchId);
        changeLogEntity.setFormId(formValues.get(key).getFormId());
        changeLogEntity.setInstanceId(instanceId);
        changeLogEntity.setOldValue(formValueMap.get(key));
        changeLogEntity.setCurrValue(formValues.get(key).getFormValue());
        changeLogEntity.setOperaterType("批量更新");
        changeLogEntity.setOperator("系统管理员");
        changeLogEntity.setOperatorTime(new Date());
        changeLogService.insertVO(changeLogEntity);
    }
    /**
     * 下载批量更新模版
     */
    @RequestMapping("/exportExcel")
    private void exportExcel(@RequestParam Map<String, Object> params, HttpServletRequest request,
                               HttpServletResponse response) {
        try {
            if (params != null) {
                /* 第一步 */
                String[] headerList = {"实例名称"};
                String[] instance_name = {};
                List<Object> instance_nameList = new ArrayList<Object>();
                instance_nameList.add(instance_name);
                instance_nameList.add("instance_name");

                List<List<Object>> result = new ArrayList<>();
                result.add(instance_nameList);
                OutputStream os = response.getOutputStream();// 取得输出流
                try {
                    String title = params.get("title").toString();
                    String fileName = title + ".xls";
                    response.setHeader("Content-Disposition",
                            "attachment;filename=".concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
                    response.setHeader("Connection", "close");
                    response.setHeader("Content-Type", "application/vnd.ms-excel");

                    /*
                     * POICascadeUtils POICascadeUtils = new POICascade(); Workbook wb = new
                     * HSSFWorkbook(); poiCascadeTest2.utilss(wb, result,headerList); wb.write(os);
                     */
                    POICascadeUtils2 poiCascade = new POICascadeUtils2();
                    Workbook wb = new HSSFWorkbook();
                    poiCascade.repertryInstanceUtilss(wb, result, headerList, title);
                    wb.write(os);

                } catch (Exception e) {
                    logger.error("导出excel失败", e);
                } finally {
                    os.flush();
                    os.close();
                }
            } else {
                throw new NullPointerException("导出excel失败，excel文件不能为空");
            }
        }catch (Exception e) {
            logger.error("导出excel模板失败", e);
        }
    }

    @RequestMapping("/getExcelData")
    public Map<String, Object> getExcelData(@RequestParam String moduleId, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> resultMap = new HashMap<>();
        String message = null;
        Boolean result = Boolean.FALSE;
        InputStream is = null;
        String extName = "";
        String newFileName = "";
        Map<Integer, String> excelData = new HashMap<>();
        List<String> existInstances = new ArrayList<>();
        List<String> unExistInstances =  new ArrayList<>();
        ExcelReaderUtils excelReader = new ExcelReaderUtils();
        // 服务器目录
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("myFile");
        try {
            is = file.getInputStream();
            validUploadExcel(file);
            excelData =  excelReader.readExcelContent(is);
            if (excelData.size() <= 0) {
                result = Boolean.FALSE;
                throw new Exception("上传文件内容为空");
            }
            for (Integer key : excelData.keySet()) {
                if (!existInstances.contains(excelData.get(key))) {
                    Map<String, Object> queryMap = new HashMap<>();
                    queryMap.put("moduleId", moduleId);
                    List<String> instanceNames = new ArrayList<>();
                    instanceNames.add(excelData.get(key));
                    queryMap.put("instanceNames", instanceNames);
                    if(instanceService.getInstanceByNameAndModuleId(queryMap).size() > 0) {
                        if (!existInstances.contains(excelData.get(key).trim())){
                            existInstances.add(excelData.get(key).trim());
                        }
                    } else {
                        if (!unExistInstances.contains(excelData.get(key).trim())){
                            unExistInstances.add(excelData.get(key).trim());
                        }
                    }
                }
            }
            result = Boolean.TRUE;
        } catch (IOException e) {
            result = Boolean.FALSE;
            logger.error("解析excel失败", e);
        } catch (Exception e) {
            logger.error("解析excel失败", e);
            if (e.getMessage().contains("列数")) {
                message = "抱歉，已超出文件的列数限制，请重新填写！";
            } else if (e.getMessage().contains("行数")) {
                message = "抱歉，已超出文件的行数限制，请分批导入！";
            }else {
                message = e.getMessage();
            }
            result = Boolean.FALSE;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        resultMap.put("result", result);
        resultMap.put("message", message);
        resultMap.put("existInstance", existInstances);
        resultMap.put("unExistInstance", unExistInstances);
        return resultMap;
    }

    private void validUploadExcel(MultipartFile file) throws Exception {
        InputStream is = file.getInputStream();
        POIFSFileSystem fs;
        Workbook wb;
        Sheet sheet;
        Row row;
        fs = new POIFSFileSystem(is);
        if (file.getOriginalFilename().toUpperCase().endsWith(".XLSX")) {
            wb = new HSSFWorkbook(fs);// Excel 2007
        } else if (file.getOriginalFilename().toUpperCase().endsWith(".XLS")) {
            wb = new HSSFWorkbook(fs);// Excel 2003
        } else {
            throw new Exception("请录入正确的文件格式！");
        }
        sheet = wb.getSheetAt(0);
        row = sheet.getRow(0);
        int colNum = row.getPhysicalNumberOfCells();
        if (colNum > 1) {
            throw new Exception("列数");
        }
        int lastRowNum = sheet.getLastRowNum();
        if (lastRowNum > 1000) {
            throw new Exception("行数");
        }
    }


    private boolean columnFilter(FormValue fv) {
        return !Constants.MODULE_FORM_TYPE_TABLE.equals(fv.getForm().getType()) && !Constants.MODULE_FORM_TYPE_RICHTEXT.equals(fv.getForm().getType())
                && !Constants.MODULE_FORM_TYPE_IMAGE.equals(fv.getForm().getType()) && !Constants.MODULE_FORM_TYPE_FILE.equals(fv.getForm().getType())
                && !Constants.MODULE_FORM_TYPE_MULTIROWTEXT.equals(fv.getForm().getType()) && !Constants.MODULE_FORM_TYPE_CASCADER.equals(fv.getForm().getType())
                && !Constants.MODULE_FORM_TYPE_GROUPLINE.equals(fv.getForm().getType()) && !"Y_name".equals(fv.getForm().getCode());
    }
}
