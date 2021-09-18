package com.aspire.mirror.alert.api.dto.notify;

import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author menglinjie
 */
@Data
public class SmsRecordExportVo implements Serializable {

    private String startTime;

    private String endTime;

    private String receiver;

    private String content;

    private Integer status;

    private static final long serialVersionUID = 1L;

}