package com.aspire.ums.cmdb.collect.web;

import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 项目名称:  咪咕微服务运营平台
 * 包:       com.migu.tsg.msp.atomicservice.region.common
 * 类名称:    com.migu.tsg.msp.atomicservice.region.controller.BaseController.java
 * 类描述:
 * 创建人:    zhu.juwang
 * 创建时间:  2017/08/22 22:43
 * 版本:      v1.0
 */
public class BaseController<T>{

    /**
     * request
     */
    private HttpServletRequest request;

    private HttpServletResponse response;

    private HttpSession session;

    /**
     * request.
     *
     * @return the request
     */
    public HttpServletRequest getRequest() {
        return request;
    }

    /**
     * request.
     *
     * @param request the request to set
     */
    @ModelAttribute public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * response.
     *
     * @return the response
     */
    public HttpServletResponse getResponse() {
        return response;
    }

    /**
     * response.
     *
     * @param response the response to set
     */
    @ModelAttribute public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    /**
     * session.
     *
     * @return the session
     */
    public HttpSession getSession() {
        return session;
    }

    /**
     * session.
     *
     * @param session the session to set
     */
    @ModelAttribute public void setSession(HttpSession session) {
        this.session = session;
    }
}
