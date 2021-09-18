package com.aspire.mirror.zabbixintegrate.util;

import com.aspire.mirror.zabbixintegrate.daoAlert.po.KpiConfigKey;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author baiwenping
 * @Title: CommonUtils
 * @Package com.aspire.mirror.zabbixintegrate.util
 * @Description: TODO
 * @date 2021/1/26 16:53
 */
public class CommonUtils {

    /**
     * 把指标项组成数据库所需的格式
     * @auther baiwenping
     * @Description
     * @Date 17:46 2020/4/17
     * @Param [kpiConfigKeyList]
     * @return java.lang.String
     **/
    public static List<String> generateKeyListDb (List<KpiConfigKey> kpiConfigKeyList) {
        List<String> list = new ArrayList<>();
        if (kpiConfigKeyList.isEmpty()) {
            list.add("1=1");
            return list;
        }
        StringBuilder sb1 = new StringBuilder("");
        String field = kpiConfigKeyList.get(0).getKpiKeyField();
        for (KpiConfigKey kpiConfigKey: kpiConfigKeyList) {
            String kpiKeyModel = kpiConfigKey.getKpiKeyModel();
            if (StringUtils.isEmpty(kpiKeyModel) || "1".equals(kpiKeyModel)) {
                sb1.append("'").append(kpiConfigKey.getKpiKey()).append("',");
            } else if ("2".equals(kpiKeyModel)) {
                list.add(kpiConfigKey.getKpiKeyField() + " like '" + kpiConfigKey.getKpiKey() + "%' ");
            }
        }
        if (sb1.length() > 0) {
            list.add(field + " in (" + sb1.substring(0, sb1.length() -1) + ")");
        }
        return list;
    }

    /**
     * 把指标项组成数据库所需的格式
     * @auther baiwenping
     * @Description
     * @Date 17:46 2020/4/17
     * @Param [kpiConfigKeyList]
     * @return java.lang.String
     **/
    public static String  generateKeysDb (List<KpiConfigKey> kpiConfigKeyList) {

        if (kpiConfigKeyList.isEmpty()) {
            return "1=1";
        }
        StringBuilder sb = new StringBuilder("(");
        StringBuilder sb1 = new StringBuilder("");
        String field = null;
        for (KpiConfigKey kpiConfigKey: kpiConfigKeyList) {
            String kpiKeyModel = kpiConfigKey.getKpiKeyModel();
            if (StringUtils.isEmpty(kpiKeyModel) || "1".equals(kpiKeyModel)) {
                field = kpiConfigKey.getKpiKeyField();
                sb1.append("'").append(kpiConfigKey.getKpiKey()).append("',");
            } else if ("2".equals(kpiKeyModel)) {
                sb.append(kpiConfigKey.getKpiKeyField()).append(" like '").append(kpiConfigKey.getKpiKey())
                        .append("%' or ");
            }
        }
        String returnStr = null;
        if (sb1.length() > 0) {
            sb.append(field).append(" in (").append(sb1.substring(0, sb1.length() -1)).append("))");
            returnStr = sb.toString();
        } else if (sb.length() > 1) {
            returnStr = sb.substring(0, sb.length() -3) + ")";
        } else {
            returnStr = "1=1";
        }
        return returnStr;
    }
}
