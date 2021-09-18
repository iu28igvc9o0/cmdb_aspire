package com.aspire.ums.cmdb.v2.assessment.util;

import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * 项目名称:
 * 包: com.aspire.ums.cmdb.v2.assessment.util
 * 类名称:
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/7/18 12:38
 * 版本: v1.0
 */
public class DataHandleUtil {
    
    public static float div(float v1,float v2,int scale){
        if(scale<0){
            throw new IllegalArgumentException("没有指精确位数");
        }
        BigDecimal b1 = new BigDecimal(v1);
        
        BigDecimal b2 = new BigDecimal(v2);
        
        return b1.divide(b2,scale,BigDecimal.ROUND_HALF_UP).floatValue();
        
    }
    
    /**
     * 计算百分比，精确到小数点两位
     * @param v1
     * @param v2
     * @return
     */
    public static double roundRatio(Object v1, Object v2){
        if(v1 == null || v2 == null || Float.parseFloat(v1.toString()) == 0 || Float.parseFloat(v2.toString()) == 0.0f){
            return 0.0f;
        }
        BigDecimal b1 = new BigDecimal(Double.parseDouble(v1.toString()));
        BigDecimal b2 = new BigDecimal(Double.parseDouble(v2.toString()));
        return b1.divide(b2, 4, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
