package com.aspire.mirror.log.api.dto;

import lombok.Data;

import java.util.Date;

/**
 *
 * @author baiwenping
 * @Title: ConfigCompare
 * @Package com.aspire.mirror.alert.server.dao.po
 * @Description: TODO
 * @date 2020/12/10 17:36
 */
@Data
public class ConfigCompareResp {
    private Integer id;
    private String idcType;
    private String masterIp;
    private String backupIp;
    private String brand;
    private Integer addCount;
    private Integer modifyCount;
    private Integer delCount;
    private String addDatas;
    private String modifyDatas;
    private String delDatas;
    private Date compareTime;
    private String masterConfigFile;
    private String backupConfigFile;
    private Date createTime;
    private String createUser;
}
