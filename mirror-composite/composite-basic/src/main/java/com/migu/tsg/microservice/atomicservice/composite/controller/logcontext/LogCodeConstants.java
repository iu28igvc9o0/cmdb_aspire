package com.migu.tsg.microservice.atomicservice.composite.controller.logcontext;

/**
* logCode相关常量定义类
* Project Name:composite-service
* File Name:LogCodeConstants.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.controller.logcontext
* ClassName: LogCodeConstants <br/>
* date: 2017年12月8日 下午4:41:01 <br/>
* 
* @author pengguihua
* @version 
* @since JDK 1.6
*/
public class LogCodeConstants {
    public static final int LOG_CODE_LENGTH = 10;       // logCode长度定义
    
    public static final String PROJECT_LOG_CODE = "10502";  
    
    public static final String FRAMEWORK_LOG_CODE = PROJECT_LOG_CODE + "9999" 
                                                  + LastLogCodeEnum.FRAMEWORK_ERROR.getErrorCode();
    
    public static final String APP_GENERAL_LOG_CODE = PROJECT_LOG_CODE + "9999" 
                                                    + LastLogCodeEnum.GENERAL_ERROR.getErrorCode();
    
    public static final String RESULT_CODE_PLACE_HOLDER = "resultCode=${{}}";       // resultCode占位符
    
    public static final String RESULT_CODE_CONTACT      = "---";                    // 分隔符
    
    public static final String BAD_REQUEST_EXCEPTION = RESULT_CODE_PLACE_HOLDER 
            + RESULT_CODE_CONTACT + "The requested URL {} was not found on this server.";
    
    public static final String HYSTIRX_RUNTIME_EXCEPTION = RESULT_CODE_PLACE_HOLDER 
            + RESULT_CODE_CONTACT + "Catch the HystrixRuntimeException for the URI: {}";
    
    public static final String HYSTIRX_BAD_REQUEST_EXCEPTION = RESULT_CODE_PLACE_HOLDER 
            + RESULT_CODE_CONTACT + "Catch the HystrixBadRequestException for the URI: {}";
    
    public static final String HYSTIRX_TIMEOUT_EXCEPTION = RESULT_CODE_PLACE_HOLDER 
            + RESULT_CODE_CONTACT + "Catch the HystrixTimeoutException for the URI: {}";
    
    public static final String COMPOSITE_BUSINESS_EXCEPTION = RESULT_CODE_PLACE_HOLDER 
            + RESULT_CODE_CONTACT + "Catch the Composite business exception for the URI: {}";
    
    public static final String COMPOSITE_GENERAL_EXCEPTION = RESULT_CODE_PLACE_HOLDER 
            + RESULT_CODE_CONTACT + "Catch the Composite general exception for the URI: {}";
    
    public static final String SERVLET_REQUEST_BINDING_EXCEPTION = RESULT_CODE_PLACE_HOLDER 
            + RESULT_CODE_CONTACT + "Catch the ServletRequestBindingException exception for the URI: {}";
    
    public static final String HTTP_MESSAGE_CONVERSION_EXCEPTION = RESULT_CODE_PLACE_HOLDER 
            + RESULT_CODE_CONTACT + "Catch the HttpMessageConversionException exception for the URI: {}";
    
    public static final String FRAMEWORK_VALIDATION_EXCEPTION = RESULT_CODE_PLACE_HOLDER 
            + RESULT_CODE_CONTACT + "Catch the validation exception for the URI: {}, "
            + " the validation error tips: {}";
    
    public static final String SPRING_DAO_EXCEPTION = RESULT_CODE_PLACE_HOLDER 
            + RESULT_CODE_CONTACT + "Catch the spring DAO exception for the URI: {}";
    
    public static final String GENERAL_SPRING_EXCEPTION = RESULT_CODE_PLACE_HOLDER 
            + RESULT_CODE_CONTACT + "Catch the general spring framework exception for the URI: {}";
    
    public static final String GENERAL_SERVLET_EXCEPTION = RESULT_CODE_PLACE_HOLDER 
            + RESULT_CODE_CONTACT + "Catch the servlet exception for the URI: {}";
    
    public static final String GENERAL_LANG_EXCEPTION = RESULT_CODE_PLACE_HOLDER 
            + RESULT_CODE_CONTACT + "Catch the general exception for the URI: {}";
}
