package com.aspire.ums.cmdb.filemanage.service.impl;

import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.filemanage.mapper.CmdbFileManageMapper;
import com.aspire.ums.cmdb.filemanage.payload.CmdbFileManage;
import com.aspire.ums.cmdb.filemanage.payload.CmdbFileManageQueryRequest;
import com.aspire.ums.cmdb.filemanage.payload.CmdbFileManageRequest;
import com.aspire.ums.cmdb.filemanage.service.ICmdbFileManageService;
import com.aspire.ums.cmdb.schema.service.SchemaService;
import com.aspire.ums.cmdb.util.SpringUtils;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
public class CmdbFileManageServiceImpl implements ICmdbFileManageService {

    @Autowired
    private CmdbFileManageMapper fileManageMapper;

    private SchemaService schemaService;

    private SchemaService getSchemaService() {
        if (schemaService == null) {
            schemaService = SpringUtils.getBean(SchemaService.class);
        }
        return schemaService;
    }

    @Override
    @Transactional(rollbackFor = {SQLException.class, RuntimeException.class, Exception.class})
    public void insert(CmdbFileManage fileManage) {
        if(fileManage != null) {
            fileManage.setId(UUIDUtil.getUUID());
        }
        fileManageMapper.insert(fileManage);
    }

    @Override
    @Transactional(rollbackFor = {SQLException.class, RuntimeException.class, Exception.class})
    public void update(CmdbFileManageRequest fileManage) {
        CmdbFileManage cmdbFileManage = new CmdbFileManage(fileManage);
        fileManageMapper.update(cmdbFileManage);
    }

    @Override
    @Transactional(rollbackFor = {SQLException.class, RuntimeException.class, Exception.class})
    public void delete(CmdbFileManageRequest fileManage) {
        CmdbFileManage cmdbFileManage = new CmdbFileManage(fileManage);
        fileManageMapper.delete(cmdbFileManage);
    }

    @Override
    @Transactional(rollbackFor = {SQLException.class, RuntimeException.class, Exception.class})
    public  Result<CmdbFileManage> getFileManageList(CmdbFileManageQueryRequest request) {
        if (StringUtils.isNotEmpty(request.getPageNo()) && StringUtils.isNotEmpty(request.getPageSize())) {
            request.setPageNo((request.getPageNo() - 1)* request.getPageSize());
        }
        List<CmdbFileManage> list = fileManageMapper.getFileManageList(request);
        Integer totalCount = fileManageMapper.getFileManageListCount(request);
        return new Result<>(totalCount, list);
    }

    @Override
    @Transactional(rollbackFor = {SQLException.class, RuntimeException.class, Exception.class})
    public CmdbFileManage getOne(String id) {
        CmdbFileManage result =  fileManageMapper.getOne(id);
        return result;
    }

    @Override
    public List<Map<String, Object>> getFileObjectList(String fileType) {
        String sql = null;
        switch (fileType) {
            case "maintenType":
                sql = "select id as id, project_name as name from cmdb_maintenance_project where is_delete = '0'";
        }
        List<Map<String, Object>> result = getSchemaService().executeSql(sql);
        return result;
    }
}
