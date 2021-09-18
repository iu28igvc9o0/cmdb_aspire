package com.aspire.mirror.composite.service.desk.bpm.payload;

import lombok.Data;

import java.io.Serializable;

/**
 * @author menglinjie
 */
@Data
public class ProcInstVo implements Serializable {
    private String procInstId;

    private String procDefKey;

    private static final long serialVersionUID = 1L;

}