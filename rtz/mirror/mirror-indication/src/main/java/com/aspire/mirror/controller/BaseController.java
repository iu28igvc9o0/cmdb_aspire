package com.aspire.mirror.controller;

import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created with IntelliJ IDEA.
 * User: jw.zhu
 * Date: 2018/10/31
 * 软探针异常指标监控系统
 * com.aspire.mirror.controller.BaseController
 */
public class BaseController<T> {

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
    @ModelAttribute
    public void setRequest(HttpServletRequest request) {
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
    @ModelAttribute
    public void setResponse(HttpServletResponse response) {
        this.response = response;
        //设置所有请求都返回
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
        response.setHeader("Access-Control-Allow-Credentials","true");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type,X-Token");
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
