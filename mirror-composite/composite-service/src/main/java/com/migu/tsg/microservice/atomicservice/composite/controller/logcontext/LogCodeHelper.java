package com.migu.tsg.microservice.atomicservice.composite.controller.logcontext;

import static com.migu.tsg.microservice.atomicservice.composite.controller.logcontext.LogCodeConstants.LOG_CODE_LENGTH;

import org.apache.commons.lang.StringUtils;

/**
* 10位日志合成编码帮助类, 外部包使用此类执行logCode追加和获取的操作
* Project Name:composite-service
* File Name:LogCodeHelper.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.controller.logcontext
* ClassName: LogCodeHelper <br/>
* date: 2017年12月8日 下午3:19:22 <br/>
* 
* @author pengguihua
* @version 
* @since JDK 1.6
*/
public class LogCodeHelper {
    
    /**
    * 追加10位日志合成码的最后一位错误码.<br/>
    *
    * 作者： pengguihua
    * @param lastCode
    * @return 返回追加之后的logCode值
    public static String appendLastLogCode(LastLogCodeEnum lastCode) {
        String currLogCode = getCurrentLogCode();
        if (StringUtils.isBlank(currLogCode) || currLogCode.length() < LOG_CODE_LENGTH) {
            return appendLogCode(String.valueOf(lastCode.getErrorCode()));
        }
        String subLogCode = currLogCode.substring(0, LOG_CODE_LENGTH - 1);
        return replaceCurrLogCode(subLogCode + lastCode);
    }
     */
    
    
    /**
    * 当10位最后1位错误码不存在时，填充指定的错误码.<br/>
    *
    * 作者： pengguihua
    * @param lastCode
    * @return
    */
    public static String appendLastLogCodeIfNotExist(LastLogCodeEnum lastCodeEnum) {
        String currLogCode = getCurrentLogCode();
        if (StringUtils.isBlank(currLogCode) || currLogCode.length() < LOG_CODE_LENGTH) {
            return appendLogCode(String.valueOf(lastCodeEnum.getErrorCode()));
        }
        return currLogCode;
    }
    
    /**
    * 追加日志logCode <br/>
    *
    * 作者： pengguihua
    * @param logCode2Apppend
    * @return 返回追加之后的logCode值
    */
    public static String appendLogCode(String logCode2Append) {
        LogCodeContext logCtx = LogCodeContext.currentLogCodeContext();
        return logCtx.appendLogCode(logCode2Append);
    }
    
    /**
    * 使用指定的logCode替换当前已经存在的logCode <br/>
    *
    * 作者： pengguihua
    * @param logCode2Replace
    * @return 返回替换之后的logCode值
    */
    public static String replaceCurrLogCode(String logCode2Replace) {
        LogCodeContext logCtx = LogCodeContext.currentLogCodeContext();
        return logCtx.replaceLogCode(logCode2Replace);
    }
    
    /**
    * 清空当前上下文中logCode .<br/>
    *
    * 作者： pengguihua
    */
    public static String clearCurrLogCode() {
        LogCodeContext logCtx = LogCodeContext.currentLogCodeContext();
        return logCtx.replaceLogCode(StringUtils.EMPTY);
    }
    
    /**
    * 当前上下文不存在logCode时，追加指定的logCode.<br/>
    *
    * 作者： pengguihua
    * @param logCode2Append
    * @return
    */
    public static String appendLogCodeIfNotExist(String logCode2Append) {
        String currLogCode = getCurrentLogCode();
        if (StringUtils.isBlank(logCode2Append)) {
            return currLogCode;
        }
        if (StringUtils.isNotBlank(currLogCode)) {
            return currLogCode;
        }
        return replaceCurrLogCode(logCode2Append);
    }
    
    /**
    * 获取日志logCode <br/>
    *
    * 作者： pengguihua
    * @return
    */
    public static String getCurrentLogCode() {
        return LogCodeContext.currentLogCodeContext().getLogCode();
    }
}
