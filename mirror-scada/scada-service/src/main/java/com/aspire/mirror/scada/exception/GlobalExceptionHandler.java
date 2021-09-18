package com.aspire.mirror.scada.exception;

import com.aspire.mirror.scada.common.entity.ResMap;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * @author JinSu
 */
@CrossOrigin
@RestControllerAdvice
public class GlobalExceptionHandler
{

    private static Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler
    public ResMap processException(Exception e, HttpServletRequest request, HttpServletResponse response)
    {

        if (e instanceof MethodArgumentNotValidException)
        {
            MethodArgumentNotValidException exs = (MethodArgumentNotValidException) e;
            List<ObjectError> allErrors = exs.getBindingResult().getAllErrors();
            if (CollectionUtils.isNotEmpty(allErrors))
            {
                return ResMap.notice(allErrors.get(0).getDefaultMessage(), null);
            }
        }
        if (e instanceof ApiException)
        {
            LOGGER.warn(e.getMessage());
            return ResMap.notice(e.getMessage(), null);
        }
        LOGGER.error("异常url:" + request.getRequestURI(), e);
        return ResMap.error();
    }

}
