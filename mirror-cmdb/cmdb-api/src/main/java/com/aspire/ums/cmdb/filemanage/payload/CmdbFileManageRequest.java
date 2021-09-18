package com.aspire.ums.cmdb.filemanage.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CmdbFileManageRequest {
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
     *  二进制文件数据
     * */
    private MultipartFile fileData;
}
