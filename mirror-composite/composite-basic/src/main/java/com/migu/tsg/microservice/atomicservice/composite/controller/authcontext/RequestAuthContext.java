package com.migu.tsg.microservice.atomicservice.composite.controller.authcontext;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

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
	public static final String REQUEST_FIRST_FLAG_KEY 	= "RequestAuthContext_request_first_flag_key";	// 请求是否已解析过user信息
    // 请求参数用户
    @Setter
    private RequestHeadUser user;
    // 资源约束
    private final Map<String, String> rawConstraints        = new HashMap<String, String>();
    private final Map<String, String> flattenConstraints    = new HashMap<String, String>();

    @Setter
    private String resType;         			// 资源类型
    @Setter
    private String rawAction;       			// 资源动作
    @Setter	
    private Boolean loadResFilter;   			// 当前业务动作是否依赖于资源数据权限
    @Setter
    private String 	ctxCacheResFilterConfig;	// 上下文中缓存的用户资源过滤  urlSafeBase64 字符串

    // 请求参数Map
    private final Map<String, String[]> paramMap            = new HashMap<String, String[]>();

    // 用户认证环境上下文
    private static final ThreadLocal<RequestAuthContext> AUTH_CONTEXT = new InheritableThreadLocal<>();

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
     * 功能描述: 当前是否已经存在上下文  
     * <p>
     * @return
     */
    public static boolean isAuthContextExists() {
    	RequestAuthContext currCtx = currentRequestAuthContext();
    	return currCtx != null;
    }

    /**
    * 获取类似    service:create  表示的资源操作串  .<br/>
    *
    * 作者： pengguihua
    * @return
    */
    public String getResAction() {
        return (resType == null ? "" : resType) + ":" + (rawAction == null ? "" : rawAction);
    }

    /**
    * addConstraint: 添加约束. <br/>
    *
    * 作者： pengguihua
    * @param consKeyName
    * @param consVal
    */
    public void addConstraint(String constKey, String consVal) {
        if (StringUtils.isBlank(constKey)) {
            return;
        }

        RequestConstraintEnum item = RequestConstraintEnum.getEnumByName(constKey);
        if (item != null) {
            rawConstraints.put(constKey, consVal);
            flattenConstraints.put("res:" + item.getResType(), consVal);
        }
    }
    
    /**
     * addConstraint: 移除约束. <br/>
     *
     * 作者： pengguihua
     * @param consKeyName
     * @param consVal
     */
    public void removeConstraint(String constKey) {
        if (StringUtils.isBlank(constKey)) {
            return;
        }

        RequestConstraintEnum item = RequestConstraintEnum.getEnumByName(constKey);
        if (item != null) {
            rawConstraints.remove(constKey);
            flattenConstraints.remove("res:" + item.getResType());
        }
    }
    
    /**
    * 获取非扁平化的上下文约束.<br/>
    *
    * 作者： pengguihua
    * @return
    */
    public Map<String, String> getRawConstraints() {
        Map<String, String> cloneMap = new HashMap<String, String>();
        cloneMap.putAll(rawConstraints);
        return cloneMap;
    }
    
    /**
    * 获取扁平化上下文约束.<br/>
    *
    * 作者： pengguihua
    * @return
    */
    public Map<String, String> getFlattenConstraints() {
        Map<String, String> cloneMap = new HashMap<String, String>();
        cloneMap.putAll(flattenConstraints);
        return cloneMap;
    }

    /**
    * 把HTTP请求中参数Map缓存, 供后续直接获取参数.<br/>
    *
    * 作者： pengguihua
    * @param requestMap
    */
    public void cacheRequestParameterMap(Map<String, String[]> requestMap) {
        this.paramMap.clear();
        this.paramMap.putAll(requestMap);
    }

    /**
    * 获取HTTP请求参数单个值.<br/>
    *
    * 作者： pengguihua
    * @param paramName
    * @return
    */
    public String getParameterSingleValue(String paramName) {
        final String[] paramValArr = paramMap.get(paramName);
        if (paramValArr != null && paramValArr.length > 0) {
            return paramValArr[0];
        }
        return null;
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
        private String namespace;               // 根帐号
        private String username;                // 用户名
        private boolean isAdmin     = false;    // 是否管理员帐号
        private boolean isSuperUser = false;    // 是否超级用户

        public boolean selfCheck() {
            return !StringUtils.isBlank(namespace) && !StringUtils.isBlank(username);
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
