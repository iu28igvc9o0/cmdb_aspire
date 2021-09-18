package com.aspire.ums.cmdb.filemanage.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CmdbFileManageQueryRequest {
    // 参数——文件类型
    private String fileType;
    // 参数——文件名称
    private String fileName;
    // 参数——文件对象
    private String fileObject;
    // 参数——页码
    private Integer pageNo;
    // 参数——页面大小
    private Integer pageSize;
}
