package com.aspire.mirror.composite.service.desk.bpm.payload;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author menglinjie
 */
@Data
public class DeskLogs implements Serializable {
    private String ID;

    private String LogTime;

    private String SubUser;

    private String App;

    private String Sip;

    private String AppModule;

    private String OpType;

    private Date CreateTime;

    private String OpText;

    private String AppType;

    private static final long serialVersionUID = 1L;

}