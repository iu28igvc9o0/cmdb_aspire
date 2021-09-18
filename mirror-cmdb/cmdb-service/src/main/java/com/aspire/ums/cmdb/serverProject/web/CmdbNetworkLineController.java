package com.aspire.ums.cmdb.serverProject.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.ums.cmdb.common.ResultVo;
import com.aspire.ums.cmdb.serverProject.CmdbNetworkLineAPI;
import com.aspire.ums.cmdb.serverProject.payload.CmdbNetworkLine;
import com.aspire.ums.cmdb.serverProject.payload.CmdbNetworkLineRecord;
import com.aspire.ums.cmdb.serverProject.service.ICmdbNetworkLineRecordService;
import com.aspire.ums.cmdb.serverProject.service.ICmdbNetworkLineService;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: jiangxuwen
 * @Datetime: 2021/01/27 16:32
 */
@RestController
@Slf4j
public class CmdbNetworkLineController implements CmdbNetworkLineAPI {

    @Autowired
    private ICmdbNetworkLineService networkLineService;

    @Autowired
    private ICmdbNetworkLineRecordService networkLineRecordService;

    @Override
    public ResultVo createNetworkLine(CmdbNetworkLine networkLine) {
        return null;
    }

    @Override
    public ResultVo updateNetworkLine(CmdbNetworkLine networkLine) {
        return null;
    }

    @Override
    public ResultVo deleteNetworkLine(String id) {
        return null;
    }

    @Override
    public ResultVo batchDeleteNetworkLine(String ids) {
        return null;
    }

    @Override
    public ResultVo<List<CmdbNetworkLine>> queryCmdbNetworkLine(CmdbNetworkLine networkLine) {
        return null;
    }

    @Override
    public ResultVo createNetworkLineRecord(CmdbNetworkLineRecord record) {
        return null;
    }

    @Override
    public ResultVo updateNetworkLineRecord(CmdbNetworkLineRecord record) {
        return null;
    }

    @Override
    public ResultVo deleteNetworkLineRecord(String id) {
        return null;
    }

    @Override
    public ResultVo batchDeleteNetworkLineRecord(String ids) {
        return null;
    }

    @Override
    public ResultVo<List<CmdbNetworkLineRecord>> queryNetworkLineRecord(CmdbNetworkLineRecord record) {
        return null;
    }
}
