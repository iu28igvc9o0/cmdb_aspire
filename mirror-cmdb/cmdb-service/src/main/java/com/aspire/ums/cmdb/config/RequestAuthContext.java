package com.aspire.ums.cmdb.config;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
* 请求中鉴权相关信息上下文
* Project Name:composite-service
* File Name:RequestAuthContext.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.controller.model
* ClassName: RequestAuthInfo <br/>
* date: 2017年8月23日 下午2:22:01 <br/>
* 解析请求中和鉴权相关的信息，拼装成相关对象: 如用户信息, 资源约束等
* @author pengguihua
* @version
* @since JDK 1.6
*/
@Getter
public class RequestAuthContext {
    // 请求参数用户
    @Setter
    private RequestHeadUser user;

    // 用户认证环境上下文
    private static final ThreadLocal<RequestAuthContext> AUTH_CONTEXT = new InheritableThreadLocal<RequestAuthContext>();

    /**
    * 获取当前auth上下文.<br/>
    *
    * 作者： pengguihua
    * @return
    */
    public static RequestAuthContext currentRequestAuthContext() {
        return AUTH_CONTEXT.get();
    }

    /**
    * 设置auth上下文.<br/>
    *
    * 作者： pengguihua
    * @param authCtx
    */
    public static void setRequestAuthContext(RequestAuthContext authCtx) {
        AUTH_CONTEXT.set(authCtx);
    }
    
    /** 
     * 功能描述: 获取http请求头中的用户名  
     * <p>
     * @return
     */
    public static String getRequestHeadUserName() {
    	RequestHeadUser headUser = getRequestHeadUser();
		return headUser == null ? null : headUser.getUsername();
	}
    
    /** 
     * 功能描述: 获取http请求头中的用户信息  
     * <p>
     * @return
     */
    public static RequestHeadUser getRequestHeadUser() {
    	RequestAuthContext userAuth = RequestAuthContext.currentRequestAuthContext();
		if (userAuth == null || userAuth.getUser() == null) {
			return null;
		}
		return userAuth.getUser();
    }

    /**
    *  RequestHeadUser类
    * Project Name:composite-service
    * File Name:RequestAuthContext.java
    * Package Name:com.migu.tsg.microservice.atomicservice.composite.controller.authContext
    * ClassName: RequestHeadUser <br/>
    * date: 2017年9月1日 下午7:01:20 <br/>
    *  详细描述这个类的功能等
    * @author yangshilei
    * @version RequestAuthContext
    * @since JDK 1.6
    */
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class RequestHeadUser {
		private String						namespace;					// 根帐号
		private String						username;					// 用户名
		private boolean						isAdmin			 = false;	// 是否管理员帐号
		private boolean						isSuperUser		 = false;	// 是否超级用户
		private Map<String, List<String>>	resFilterConfig;			// 资源数据权限配置

        public boolean selfCheck() {
//            if (StringUtils.isBlank(namespace) || StringUtils.isBlank(username)) {
//                return false;
//            }
            return true;
        }
        
        public void setNamespace(String namespace) {
            this.namespace = namespace;
        }
        
        @Override
        public String toString() {
            return "Current User info as orgAccount=" + namespace + "|username=" + username + "|isAdmin=" + isAdmin;
        }
    }
}
