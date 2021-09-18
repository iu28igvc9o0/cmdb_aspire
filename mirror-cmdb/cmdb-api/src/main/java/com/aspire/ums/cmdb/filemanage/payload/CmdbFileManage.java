package com.aspire.ums.cmdb.filemanage.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbFileManage {

    /*
    *  ID
    * */
    private String id;

    /*
    *  文件名称
    * */
    private String fileName;

    /*
    *  文件别名
    * */
    private String fileNameAlias;

    /*
    *  文件类型
    * */
    private String fileType;

    /*
    *  文件对象
    * */
    private String fileObject;

    /*
    * 文件对象ID
    * */
    private String fileObjectId;

    /*
    *  二进制文件数据
    * */
    private byte[] fileData;

    /*
    *  是否删除
    * */
    private String isDelete;

    public CmdbFileManage(CmdbFileManageRequest requestObj) {
        this.id = requestObj.getId();
        this.fileName = requestObj.getFileName();
        this.fileNameAlias = requestObj.getFileNameAlias();
        this.fileType = requestObj.getFileType();
        this.fileObject = requestObj.getFileObject();
    }
}
