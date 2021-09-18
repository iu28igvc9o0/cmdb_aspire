package com.aspire.mirror.common.util;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * 项目名称:  咪咕微服务运营平台
 * 包:       com.migu.tsg.microservice.common.util
 * 类名称:    LogUtil.java
 * 类描述:    统一写入业务日志入口
 * 创建人:    zhu.juwang
 * 创建时间:  2017/12/13 09:46
 * 版本:      v1.0
 */
public class LogUtil {

    /**
     * 日志对象
     */
    private static Logger LOGGER = LogManager.getLogger(LogUtil.class.getName());

    /**
     * 日志信息中必须要包含的RESULT_CODE字符串
     */
    private static final String RESULT_CODE_STRING = "resultCode";

    /**
     * 等于符号字符串
     */
    private static final String EQUART_STRING = "=";

    /**
     * 等于符号字符串
     */
    private static final String MESSAGE_END_WITH = ".";

    /**
     * 输出错误日志信息
     *
     * @param errorCode 错误代码
     * @param message   错误信息描述
     */
    public static void error(String errorCode, String message) {
        error(errorCode, message, null);
    }

    /**
     * 输出错误日志信息
     *
     * @param errorCode 错误代码
     * @param message   错误信息描述
     * @param throwable 异常信息
     */
    public static void error(String errorCode, String message, Throwable throwable) {
        formatOutPutLogInfo(Level.ERROR.toString(), errorCode, message, throwable);
    }

    /**
     * 输出提示日志信息
     *
     * @param message   错误信息描述
     */
    public static void info(String message) {
        formatOutPutLogInfo(Level.INFO.toString(), null, message, null);
    }

    /**
     * 输出提示日志信息
     *
     * @param errorCode 错误代码
     * @param message   错误信息描述
     */
    public static void info(String errorCode, String message) {
        formatOutPutLogInfo(Level.INFO.toString(), errorCode, message, null);
    }

    /**
     * 拼装日志信息
     *
     * @param levelName 日志级别
     * @param errorCode 错误代码
     * @param message   内容描述
     * @param throwable 异常信息
     */
    private static void formatOutPutLogInfo(String levelName, String errorCode, String message, Throwable throwable) {
        //如果启动error级别日志, 则输出error信息
        if (checkEnableOutPutLog(levelName)) {
            StringBuffer systemOut = new StringBuffer();
            //判断错误信息中是否有包含resultCode信息, 如果没有则自动追加resultCode至message中
            StringBuffer messageBuffer = new StringBuffer("");
            if (message != null) {
                messageBuffer.append(message);
                if (!message.endsWith(MESSAGE_END_WITH)) {
                    messageBuffer.append(MESSAGE_END_WITH);
                }
            }
            if (errorCode != null) {
                if (!messageBuffer.toString().contains(RESULT_CODE_STRING)) {
                    messageBuffer.append(RESULT_CODE_STRING);
                    messageBuffer.append(EQUART_STRING);
                    messageBuffer.append(errorCode);
                }
            }
            systemOut.append(messageBuffer);
            if (throwable == null) {
                outPutLog(levelName, messageBuffer);
            } else {
                outPutLog(levelName, messageBuffer, throwable);
            }
        }
    }

    /**
     * 检测指定的日志级别, 是否可以输出
     *
     * @param levelName 日志级别名称. 可以为Level, 或者自定义级别
     * @return true 可以输出,  false 不可以输出
     */
    private static Boolean checkEnableOutPutLog(String levelName) {
        Boolean canOutPut = false;
        if (Level.ERROR.toString().equals(levelName.toUpperCase())) {
            if (LOGGER.isErrorEnabled()) {
                canOutPut = true;
            }
        }
        if (Level.INFO.toString().equals(levelName.toUpperCase())) {
            if (LOGGER.isInfoEnabled()) {
                canOutPut = true;
            }
        }
        return canOutPut;
    }

    /**
     * 输出日志信息
     *
     * @param levelName 日志级别
     * @param systemOut 输出的日志信息
     */
    private static void outPutLog(String levelName, StringBuffer systemOut) {
        if (Level.ERROR.toString().equals(levelName.toUpperCase())) {
            LOGGER.error(systemOut);
        }
        if (Level.INFO.toString().equals(levelName.toUpperCase())) {
            LOGGER.info(systemOut);
        }
    }

    /**
     * 输出日志信息
     *
     * @param levelName 日志级别
     * @param systemOut 输出的日志信息
     * @param throwable 输出的异常信息
     */
    private static void outPutLog(String levelName, StringBuffer systemOut, Throwable throwable) {
        if (Level.ERROR.toString().equals(levelName.toUpperCase())) {
            LOGGER.error(systemOut, throwable);
        }
        if (Level.INFO.toString().equals(levelName.toUpperCase())) {
            LOGGER.info(systemOut, throwable);
        }
    }
}
