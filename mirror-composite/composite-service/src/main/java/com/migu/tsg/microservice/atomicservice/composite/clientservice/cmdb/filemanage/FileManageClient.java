package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.filemanage;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.filemanage.payload.CmdbFileManage;
import com.aspire.ums.cmdb.filemanage.payload.CmdbFileManageQueryRequest;
import com.aspire.ums.cmdb.filemanage.payload.CmdbFileManageRequest;

@FeignClient(value = "CMDB")
public interface FileManageClient {
    /**
     *  保存文件管理对象
     * @return
     */
    @PostMapping(value = "/cmdb/file/manage/save" )
    JSONObject insert(CmdbFileManage fileManage);

    /**
     *  修改文件管理对象
     * @return
     */
    @PostMapping(value = "/cmdb/file/manage/update" )
    JSONObject update(@RequestBody CmdbFileManageRequest fileManage);

    /**
     *  删除文件管理对象
     * @return
     */
    @PostMapping(value = "/cmdb/file/manage/delete" )
    JSONObject delete(@RequestBody CmdbFileManageRequest fileManage);

    /**
     *  获取文件管理对象列表
     * @return
     */
    @PostMapping(value = "/cmdb/file/manage/list" )
    Result<CmdbFileManage> getFileManageList(@RequestBody CmdbFileManageQueryRequest request);

    /**
     *  下载文件,依据id
     * @return
     */
    @GetMapping(value = "/cmdb/file/manage/download/{id}" )
    CmdbFileManage getOneFile(@PathVariable("id") String id);

    /**
     *  根据文件类型，列出文件对象列表
     * @return
     */
    @PostMapping(value = "/cmdb/file/manage/listFileObj" )
    List<Map<String,String>> getFileObjectList(@RequestBody String fileType);
}
