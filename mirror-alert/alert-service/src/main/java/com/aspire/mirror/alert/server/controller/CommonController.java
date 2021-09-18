package com.aspire.mirror.alert.server.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.alert.api.dto.alert.QueryField;
import com.aspire.mirror.alert.api.dto.alert.QueryParams;
import com.aspire.mirror.alert.server.aspect.RequestAuthContext;
import com.aspire.mirror.alert.server.biz.filter.AlertFilterSceneBiz;
import com.aspire.mirror.alert.server.config.properties.OrderOvertimeProperties;
import com.aspire.mirror.alert.server.constant.AlertCommonConstant;
import com.aspire.mirror.alert.server.dao.filter.po.AlertFilterScene;
import com.aspire.mirror.alert.server.constant.AlertConfigConstants;
import com.aspire.mirror.alert.server.vo.model.AlertFieldVo;
import com.aspire.mirror.alert.server.util.Criteria;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.alert.server.v2.controller
 * @Author: baiwenping
 * @CreateTime: 2020-03-03 14:39
 * @Description: ${Description}
 */
@Slf4j
@RestController
public class CommonController {
    @Autowired
    private AlertFilterSceneBiz alertFilterSceneBiz;
    @Autowired
    private OrderOvertimeProperties orderOvertimeProperties;
    /**
     * 转换产品化配置查询条件为数据库查询条件
     * @auther baiwenping
     * @Description
     * @Date 14:39 2020/3/3
     * @Param [queryParams]
     * @return com.aspire.mirror.alert.server.util.Criteria
     **/
    protected Criteria generateCriteria (QueryParams queryParams, List<AlertFieldVo> fieldList) {
        Criteria criteria = new Criteria();
        if (queryParams.getPageNum() != null) {
            if (queryParams.getPageSize() == null) {
                // 默认50
                queryParams.setPageSize(50);
            }
            criteria.setBegin((queryParams.getPageNum() > 0? (queryParams.getPageNum() - 1): 0) * queryParams.getPageSize());
            criteria.setPageSize(queryParams.getPageSize());
        }
        String sortName = queryParams.getSortName();
        if (StringUtils.isNotEmpty(sortName) && sortName.matches("[a-zA-Z0-9\\_\\ ,]+")) {
            criteria.setOrderByClause(sortName);
        }
        Criteria.Condition conditon = criteria.createConditon();

        // 过滤器查询逻辑
        String filterSceneId = queryParams.getFilterSceneId();
        if (StringUtils.isNotBlank(filterSceneId) && StringUtils.isNumeric(filterSceneId)) {
            AlertFilterScene alertFilterScene = alertFilterSceneBiz.selectByPrimaryKey(filterSceneId);
            if (alertFilterScene != null && StringUtils.isNotEmpty(alertFilterScene.getOptionCondition())) {
                String optionCondition = alertFilterScene.getOptionCondition();
                String condition4SQL = getCondition4SQL(optionCondition, fieldList);
                if (StringUtils.isNotEmpty(condition4SQL)) {
                    conditon.andNoValue(condition4SQL);
                }
            }
        }

        List<QueryField> list = queryParams.getList();
        if (!CollectionUtils.isEmpty(list)) {
            for (QueryField queryField:list) {
                String fieldType = queryField.getFieldType();
                String fieldValue = queryField.getFieldValue();
                String fieldName = queryField.getFieldName();
                if (StringUtils.isEmpty(fieldType)
                        || StringUtils.isEmpty(fieldValue)
                        || StringUtils.isEmpty(fieldName)
                        || !fieldName.matches("[a-zA-Z0-9\\_\\ ]+")) {
                    continue;
                }
                boolean flag = false;
                for (AlertFieldVo alertFieldVo : fieldList) {
                    if (alertFieldVo.getFieldCode().equalsIgnoreCase(fieldName)) {
                        flag = true;
                    }
                }
                if (!flag) {
                    continue;
                }
                switch (fieldType) {
                    case AlertConfigConstants.QUERY_TYPE_AND:
                        conditon.andEqualTo(fieldName, fieldValue);
                        break;
                    case AlertConfigConstants.QUERY_TYPE_IN_AND:
                        conditon.andEqualTo(fieldName, fieldValue);
                        break;
                    case AlertConfigConstants.QUERY_TYPE_IN:
                        conditon.andIn(fieldName, Arrays.asList(fieldValue.split(",")));
                        break;
                    case AlertConfigConstants.QUERY_TYPE_LIKE:
                        conditon.andLike(fieldName, fieldValue);
                        break;
                    case AlertConfigConstants.QUERY_TYPE_IN_DATE:
                        setBetween(fieldName, fieldValue, conditon);
                        break;
                    case AlertConfigConstants.QUERY_TYPE_IN_DATETIME:
                        setBetween(fieldName, fieldValue, conditon);
                        break;
                    case AlertConfigConstants.QUERY_TYPE_TREE_IN:
                        conditon.andIn(fieldName, Arrays.asList(fieldValue.split(",")));
                        break;
                    case AlertConfigConstants.QUERY_TYPE_NULL:
                        if (AlertConfigConstants.QUERY_FALSE.equalsIgnoreCase(fieldValue)) {
                            conditon.andIsNotNull(fieldName);
                        } else if (AlertConfigConstants.QUERY_TRUE.equalsIgnoreCase(fieldValue)) {
                            conditon.andIsNull(fieldName);
                        }
                        break;
                    default:
                        if (fieldValue.matches("\\d{4}-\\d{1,2}-\\d{1,2}.*") && fieldValue.indexOf(",") > -1) {
                            setBetween(fieldName, fieldValue, conditon);
                        } else if (fieldValue.indexOf(",") > -1) {
                            conditon.andIn(fieldName, Arrays.asList(fieldValue.split(",")));
                        } else {
                            conditon.andEqualTo(fieldName, fieldValue);
                        }
                        break;
                }
            }
        }
        return criteria;
    }

    /**
     * 根据模型获取需要导出的字段
     * @param modelFromRedisList
     * @return
     */
    protected String getExportField (List<AlertFieldVo> modelFromRedisList) {

        Set<String> set = Sets.newHashSet();
        List<String> fieldCodeList = Lists.newArrayList();
        //获取所有字段
        for (AlertFieldVo alertFieldVo : modelFromRedisList) {
            fieldCodeList.add(alertFieldVo.getFieldCode());
        }
        //获取需要的字段
        for (AlertFieldVo alertFieldVo : modelFromRedisList) {
            String isListShow = alertFieldVo.getIsListShow();
            //判断是否为列表页显示字段
            if (AlertCommonConstant.NUM.ONE.equals(isListShow)) {
                set.add(alertFieldVo.getFieldCode());
                String listShowPattern = alertFieldVo.getListShowPattern();
                if (StringUtils.isNotEmpty(listShowPattern)) {
                    String[] split = listShowPattern.replaceAll("}((?!@\\{).)*@\\{", "||||").split("\\|\\|\\|\\|");
                    for (String fieldTmp: split) {
                        fieldTmp = fieldTmp.replaceAll("^.*@\\{", "").replaceAll("\\}.*$", "");
                        if (fieldCodeList.contains(fieldTmp)) {
                            set.add(fieldTmp);
                        }
                    }
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        for (String fieldCode: set) {
            sb.append("t.").append(fieldCode).append(",");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length()-1);
        } else {
            sb.append("t.*");
        }
        return sb.toString();

    }

    /**
     * 设置between格式
     * @auther baiwenping
     * @Description
     * @Date 16:31 2020/3/13
     * @Param [fieldName, fieldValue, conditon]
     * @return void
     **/
    private void setBetween (String fieldName, String fieldValue, Criteria.Condition conditon) {
        if(fieldValue.contains(",")) {
            String[] split = fieldValue.split(",");
            conditon.andBetween(fieldName, split[0], split[1]);
        }


    }

    /**
     * 设置查询的数据权限
     * @param queryParams
     */
    protected void setDataPermission (QueryParams queryParams) {
        //获取权限数据
        Map<String, List<String>> resFilterMap = RequestAuthContext.getRequestHeadUser().getResFilterConfig();
        if (!CollectionUtils.isEmpty(resFilterMap)) {
            log.info("query data filter permission are : {}", resFilterMap);
            List<QueryField> list = queryParams.getList();
            if (CollectionUtils.isEmpty(list)) {
                list = Lists.newArrayList();
            }
            Map<String, String> filterTransMap = orderOvertimeProperties.getFilterTrans();
            for (Map.Entry<String, List<String>> entry:resFilterMap.entrySet()) {
                String key = entry.getKey();
                if (filterTransMap == null || !filterTransMap.containsKey(key)) {
                    continue;
                }
                String alertKey = filterTransMap.get(key);
                List<String> value = entry.getValue();
                if (CollectionUtils.isEmpty(value)) {
                    continue;
                }
                QueryField queryField = new QueryField();
                queryField.setFieldName(alertKey);
                queryField.setFieldType(AlertConfigConstants.QUERY_TYPE_IN);
                queryField.setFieldValue(Joiner.on(",").join(value));
                list.add(queryField);
            }
            queryParams.setList(list);
        }
    }

    private String getCondition4SQL(String optionCondition, List<AlertFieldVo> fieldList) {
        JSONObject filterOption = JSON.parseObject(optionCondition);
        StringBuffer orSb = new StringBuffer();
        JSONArray orlist = filterOption.getJSONArray("data");
        for (int i = 0; i < orlist.size(); i++) {
            StringBuffer andSb = new StringBuffer();
            JSONObject andJson = orlist.getJSONObject(i);
            JSONArray andlist = andJson.getJSONArray("data");
            for (int j = 0; j < andlist.size(); j++) {
                JSONObject val = andlist.getJSONObject(j);
                String filterItemName = val.getString("filterItemName");
                String operate = val.getString("operate");
                String value = val.getString("value");
                String jdbcType = val.getString("jdbcType");
                boolean flag = false;
                for (AlertFieldVo alertFieldVo : fieldList) {
                    if (alertFieldVo.getFieldCode().equalsIgnoreCase(filterItemName)) {
                        flag = true;
                    }
                }
                if(!flag) {
                    continue;
                }
                if (andSb.length() == 0) {
                    andSb.append("(").append(filterItemName).append(" ").append(operate).append(" ");
                } else {
                    andSb.append(" and ").append(filterItemName).append(" ").append(operate).append(" ");
                }
                if (operate.equalsIgnoreCase("in") || operate.indexOf("not") == 0) {
                    JSONArray valJson = JSON.parseArray(value);
                    for (int k = 0; k < valJson.size(); k++) {
                        String valStr = valJson.getString(k);
                        if (!jdbcType.equalsIgnoreCase("number")) {
                            valStr = "'" + valStr + "'";
                        }
                        if (k == 0) {
                            andSb.append("(").append(valStr);
                        } else {
                            andSb.append(",").append(valStr);
                        }
                    }
                    andSb.append(")");

                } else {
                    if (!jdbcType.equalsIgnoreCase("number")) {
                        if (operate.equalsIgnoreCase("like")) {
                            value = "'%" + value + "%'";
                        } else {
                            value = "'" + value + "'";
                        }
                    }
                    andSb.append(value);
                }
            }
            andSb.append(")");
            if (orSb.length() == 0) {
                orSb.append("( ");
                orSb.append(andSb);
            } else {
                orSb.append(" or ").append(andSb);
            }
        }
        if (orSb.length() > 0) {
            orSb.append(")");
        }

        return orSb.toString();
    }

}
