package com.aspire.ums.bills.config;

import com.alibaba.druid.support.http.StatViewServlet;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: DruidStatServlet
 * Author:   zhu.juwang
 * Date:     2020/7/13 15:55
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@WebServlet(urlPatterns = "/druid/*", initParams = {
                @WebInitParam(name = "loginUsername", value = "admin"),// 用户名
                @WebInitParam(name = "loginPassword", value = "123456"),// 密码
                @WebInitParam(name = "resetEnable", value = "false")// 禁用HTML页面上的“Reset All”功能
        })
public class DruidStatServlet extends StatViewServlet {
    private static final long serialVersionUID = -2688872071445249539L;
}
