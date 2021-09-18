package com.aspire.ums.cmdb.sync.payload;

import java.io.Serializable;

import lombok.Data;

/**
 * 设备资产图片信息.
 *
 * @author jiangxuwen
 * @date 2020/5/13 11:07
 */
@Data
public class CmdbDevicePictureDTO implements Serializable {

    private static final long serialVersionUID = -5677981114390437869L;

    private String id;

    private String deviceId;

    private String pictureType;

    private String pictureOriginalName;

    private String pictureName;

    private String picturePath;

    private String createTime;

    private String updateTime;

    private String createUserId;

    private String updateUserId;

}
