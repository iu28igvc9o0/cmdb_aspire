package com.aspire.mirror.composite.service.scada.payload;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用于封装“文件上传”接口的返回内容
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadResult {

    private String name; //上传的文件名称

    private String url; //文件的存储路径

    //private String msg; //该上传请求是否成功的信息描述
}
