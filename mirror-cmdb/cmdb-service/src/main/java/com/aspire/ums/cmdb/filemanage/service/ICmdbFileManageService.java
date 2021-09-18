package com.aspire.ums.cmdb.filemanage.service;

import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.filemanage.payload.CmdbFileManage;
import com.aspire.ums.cmdb.filemanage.payload.CmdbFileManageQueryRequest;
import com.aspire.ums.cmdb.filemanage.payload.CmdbFileManageRequest;

import java.util.List;
import java.util.Map;

public interface ICmdbFileManageService {
    // 新增文件管理对象
    void insert(CmdbFileManage fileManage);

    // 修改文件管理对象
    void update(CmdbFileManageRequest fileManage);

    // 删除文件管理对象
    void delete(CmdbFileManageRequest fileManage);

    // 获取文件管理对象列表
    Result<CmdbFileManage> getFileManageList(CmdbFileManageQueryRequest request);

    // 下载单个文件
    CmdbFileManage getOne(String id);

    //根据文件类型，列出文件对象
    List<Map<String,Object>> getFileObjectList(String fileType);
}
