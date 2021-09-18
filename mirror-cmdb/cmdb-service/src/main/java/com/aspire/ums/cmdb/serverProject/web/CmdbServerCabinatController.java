package com.aspire.ums.cmdb.serverProject.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.ums.cmdb.common.ResultVo;
import com.aspire.ums.cmdb.serverProject.CmdbServerCabinatAPI;
import com.aspire.ums.cmdb.serverProject.payload.CmdbCabinet;
import com.aspire.ums.cmdb.serverProject.payload.CmdbCabinetRecord;
import com.aspire.ums.cmdb.serverProject.service.ICmdbCabinetRecordService;
import com.aspire.ums.cmdb.serverProject.service.ICmdbServerCabinetService;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: jiangxuwen
 * @Datetime: 2021/01/27 16:32
 */
@RestController
@Slf4j
public class CmdbServerCabinatController implements CmdbServerCabinatAPI {

    @Autowired
    private ICmdbServerCabinetService cabinetService;

    @Autowired
    private ICmdbCabinetRecordService cabinetRecordService;

    @Override
    public ResultVo createCmdbCabinet(CmdbCabinet cmdbCabinet) {
        return null;
    }

    @Override
    public ResultVo updateCmdbCabinet(CmdbCabinet cmdbCabinet) {
        return null;
    }

    @Override
    public ResultVo deleteCmdbCabinet(String id) {
        return null;
    }

    @Override
    public ResultVo batchDeleteCmdbCabinet(String ids) {
        return null;
    }

    @Override
    public ResultVo<List<CmdbCabinet>> queryCmdbCabinet(CmdbCabinet cmdbCabinet) {
        return null;
    }

    @Override
    public ResultVo createCmdbcabinetRecord(CmdbCabinetRecord cmdbCabinet) {
        return null;
    }

    @Override
    public ResultVo updateCmdbCabinetRecord(CmdbCabinetRecord cmdbCabinet) {
        return null;
    }

    @Override
    public ResultVo deleteCmdbCabinetRecord(String id) {
        return null;
    }

    @Override
    public ResultVo batchDeleteCmdbCabinetRecord(String ids) {
        return null;
    }

    @Override
    public ResultVo<List<CmdbCabinetRecord>> queryCmdbCabinetRecord(CmdbCabinetRecord cmdbCabinet) {
        return null;
    }
}
