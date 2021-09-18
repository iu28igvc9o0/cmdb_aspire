package com.aspire.cmdb.agent.util;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ConcatTypeConvert
 * Author:   hangfang
 * Date:     2019/11/25
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class ConcatTypeConvert {
    /**
     * 转接属性转换
     * @param type
     * @throws RuntimeException
     */
    public static String convertType(String type) throws RuntimeException {
        String convertType;
       switch (type) {
           case "上联": convertType = "下联"; break;
           case "下联": convertType = "上联";break;
           case "父": convertType = "子";break;
           case "子": convertType = "父";break;
           case "包含": convertType = "属于";break;
           case "属于": convertType = "包含";break;
           case "基于": convertType = "被基于";break;
           case "被基于": convertType = "基于";break;
           case "指派于": convertType = "被指派于";break;
           default: convertType = "连接";break;
       }
       return convertType;
    }
}
