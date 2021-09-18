package com.aspire.ums.cmdb.filemanage.mapper;

import com.aspire.ums.cmdb.filemanage.payload.CmdbFileManage;
import com.aspire.ums.cmdb.filemanage.payload.CmdbFileManageQueryRequest;
import com.aspire.ums.cmdb.filemanage.payload.CmdbFileManageRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/*
*  文件管理接口Mapper层
* */
@Mapper
public interface CmdbFileManageMapper {
    // 新增文件管理对象
    void insert(CmdbFileManage fileManage);

    // 修改文件管理对象
    void update(CmdbFileManage fileManage);

    // 删除文件管理对象
    void delete(CmdbFileManage fileManage);

    // 获取文件管理对象列表
    List<CmdbFileManage> getFileManageList(CmdbFileManageQueryRequest request);

    // 获取文件管理对象列表数量
    Integer getFileManageListCount(CmdbFileManageQueryRequest request);

    // 依据ID获取文件
    CmdbFileManage getOne(String id);
}
