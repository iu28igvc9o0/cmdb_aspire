package com.migu.tsg.microservice.atomicservice.composite.controller.logcontext;

import org.apache.commons.lang.StringUtils;

import lombok.Getter;
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
class LogCodeContext {
    @Setter
    private String logCode;         // 日志合成编码
    
    private LogCodeContext(String logCode) {
        this.logCode = logCode;
    }

    // logCode上下文
    private static final ThreadLocal<LogCodeContext> LOGCODE_CONTEXT = new ThreadLocal<LogCodeContext>() {
        @Override
        protected LogCodeContext initialValue() {
            return new LogCodeContext(StringUtils.EMPTY);
        };
    };

    /**
    * 获取当前logCode上下文.<br/>
    *
    * 作者： pengguihua
    * @return
    */
    public static LogCodeContext currentLogCodeContext() {
        return LOGCODE_CONTEXT.get();
    }
    
    /**
    * 追加logCode <br/>
    *
    * 作者： pengguihua
    * @param logCode2Append
    */
    public String appendLogCode(String logCode2Append) {
        if (logCode2Append != null) {
            logCode = logCode == null ? logCode2Append : logCode + logCode2Append;
        }
        return logCode;
    }
    
    /**
    * 使用指定的logCode替换当前上下文logCode <br/>
    *
    * 作者： pengguihua
    * @param logCode2Replace
    */
    public String replaceLogCode(String logCode2Replace) {
        if (logCode2Replace != null) {
            logCode = logCode2Replace;
        }
        return logCode;
    }
    
    /**
    * 获取logCode <br/>
    *
    * 作者： pengguihua
    * @return
    */
    public String getLogCode() {
        return this.logCode;
    }
}
