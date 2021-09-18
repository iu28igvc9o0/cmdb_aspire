package com.aspire.mirror.alert.server.util;

import com.fasterxml.jackson.databind.JsonNode;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.axis.utils.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * prometheus告警工具类
 * <p>
 * 项目名称：mirror平台
 * 包：com.aspire.mirror.alert.server.util
 * 类名称：PrometheusUtil.java
 * 类描述：prometheus告警工具类
 * 创建人：zhujiahao
 * 创建时间：2019/1/11 14:00
 * 版本：v1.0
 */
public final class PrometheusUtil {

    public static Date getTime(String time) {
        String s1 = (time.split("[.]"))[0];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date newtime = null;
        try {
            newtime = sdf.parse(s1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newtime;
    }

    public static String getAlertId(String ip, String type) {
        if (ip == null || type == null) {
            return "null";
        }

        ip = ip.replace(".", "");

		/*这个是把utc时间截取为纯数字串
		time = time.split("[+]")[0];
		time = time.replace("-","");
		time = time.replace("T","");
		time = time.replace(":","");
		time = time.replace(".","");
		*/

        return ip + type;
    }

    public static String getValue(JsonNode jsonNode) {
        String value = jsonNode.asText();
        try {
            value = value.split("[|]")[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            //“监控值”获取失败
            //说明p监控模板里没有在description标签里用管道符后标出“监控值”
            return "null";
        }

        value = value.trim();
        return value;
    }

    public static String isNull(JsonNode jsonNode) {
        if (jsonNode == null) {
            return "null";
        }
        return jsonNode.asText();
    }

    public static String getString(JsonNode jsonNode) {
        if (jsonNode == null) {
            return null;
        }
        return jsonNode.asText();
    }

    /**
     * 汉字转为拼音
     *
     * @param chinese
     * @return
     */
    public static String toPinyin(String chinese) {
        if (StringUtils.isEmpty(chinese)) {
            return chinese;
        }
        String pinyinStr = "";
        char[] newChar = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < newChar.length; i++) {
            if (newChar[i] > 128) {
                try {
                    pinyinStr += PinyinHelper.toHanyuPinyinStringArray(newChar[i], defaultFormat)[0];
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    return chinese;
                }
            } else {
                pinyinStr += newChar[i];
            }
        }
        return pinyinStr;
    }

    public static String getLevel(String prometheusLevel) {
        if (StringUtils.isEmpty(prometheusLevel)) {
            return prometheusLevel;
        }
        String level = null;
        switch (prometheusLevel) {
            case "0":
                level = "2";
                break;
            case "1":
                level = "2";
                break;
            case "2":
                level = "2";//低
                break;
            case "3":
                level = "3";//中
                break;
            case "4":
                level = "4";//高
                break;
            case "5":
                level = "5";//重大
                break;
            default:
                level = "2";
        }
        return level;
    }
}
