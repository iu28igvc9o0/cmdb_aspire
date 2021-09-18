package com.aspire.mirror.ops.dao;

import com.aspire.mirror.ops.api.domain.OpsFile;
import com.aspire.mirror.ops.api.domain.OpsFileQueryModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文件管理DAO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.dao
 * 类名称:    OpsFileDao.java
 * 类描述:    文件管理DAO
 * 创建人:    JinSu
 * 创建时间:  2020/6/9 21:14
 * 版本:      v1.0
 */
@Mapper
public interface OpsFileDao {
    OpsFile selectByNameAndVersion(@Param("fileName") String fileName, @Param("fileVersion") String fileVersion);

    void insert(OpsFile opsFile);

    void updateByFileId(OpsFile opsFile);

    void delete(@Param("fileId") Long fileId);

    OpsFile getFileById(@Param("fileId") Long fileId);

    Integer queryListTotalSize(OpsFileQueryModel opsFileQueryModel);

    List<OpsFile> queryList(OpsFileQueryModel opsFileQueryModel);
}
