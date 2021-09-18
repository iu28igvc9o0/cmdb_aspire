package com.aspire.mirror.alert.server.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

public class AlertFilterCommonUtil {

    /**
     * 转换告警过滤场景为可用的sql段
     * @param optionCondition
     * @return
     */
    // 获取场景配置内容
    public static String getCondition(String optionCondition) {
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
                if (andSb.length() == 0) {
                    andSb.append("(").append(filterItemName).append(" ").append(operate).append(" ");
                } else {
                    andSb.append(" and ").append(filterItemName).append(" ").append(operate).append(" ");
                }
                if(operate.equalsIgnoreCase("in") || operate.indexOf("not")==0) {
                    JSONArray valJson = JSON.parseArray(value);
                    for (int k = 0; k < valJson.size(); k++) {
                        String valStr = valJson.getString(k);
                        if (!jdbcType.equalsIgnoreCase("number")) {
                            valStr = "'" + valStr + "'";
                        }
                        if(k==0) {
                            andSb.append("(").append(valStr);
                        }else {
                            andSb.append(",").append(valStr);
                        }
                    }
                    andSb.append(")");

                }else {
                    if (!jdbcType.equalsIgnoreCase("number")) {
                        if(operate.equalsIgnoreCase("like")) {
                            value = "'%" + value + "%'";
                        }else {
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
        if(orSb.length()>0) {
            orSb.append(")");
        }
        return orSb.toString();
    }

    /**
     * 把告警过滤条件转换为可读性日志
     * @param optionCondition
     * @return
     */
    public static String getOptionStr (String optionCondition) {
        StringBuffer sb = new StringBuffer();
        if (StringUtils.isEmpty(optionCondition)) {
            return sb.toString();
        }
        JSONObject jsonObject = JSONObject.parseObject(optionCondition);
        JSONArray objects = jsonObject.getJSONArray("data");
        //条件为空的忽略
        if (objects == null || objects.isEmpty()) {
            return sb.toString();
        }
        sb.append("(");
        //逐条解析条件
        for (int i = 0; i < objects.size(); i++) {
            JSONObject andJson = objects.getJSONObject(i);
            JSONArray andlist = andJson.getJSONArray("data");
            if (andlist == null || andlist.isEmpty()) {
                continue;
            }
            if (sb.length() > 2 && i > 0) {
                sb.append(",或者");
            }
            // 判断条件,单条规则所有条件都符合才能屏蔽
            boolean DeriveFlag = true;
            boolean updateFlag = false;
            for (int j = 0; j < andlist.size(); j++) {
                JSONObject val = andlist.getJSONObject(j);
                String filterItemName = val.getString("filterItemName");
                String operate = val.getString("operate").toLowerCase();
                String value = val.getString("value");
                sb.append(filterItemName);
                switch (operate) {
                    case "like":
                        sb.append("模糊匹配");
                        break;
                    case "=":
                        sb.append("等于");
                        break;
                    case "!=":
                        sb.append("不等于");
                        break;
                    case ">":
                        sb.append("大于");
                        break;
                    case ">=":
                        sb.append("大于等于");
                        break;
                    case "<":
                        sb.append("小于");
                        break;
                    case "<=":
                        sb.append("小于等于");
                        break;
                    case "in":
                        sb.append("包含");
                        break;
                    case "not in":
                        sb.append("不包含");
                        break;
                    default:
                        sb.append("未知");
                        break;
                }
                sb.append(value).append("、");
            }
        }
        sb.append(")");
        return sb.toString();
    }

}
