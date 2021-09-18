package com.aspire.ums.cmdb.serverProject.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.ums.cmdb.common.ResultVo;
import com.aspire.ums.cmdb.serverProject.CmdbServerCabinatBillAPI;
import com.aspire.ums.cmdb.serverProject.payload.CmdbCabinetBill;
import com.aspire.ums.cmdb.serverProject.payload.CmdbCabinetBillConf;
import com.aspire.ums.cmdb.serverProject.service.ICmdbCabinetBillConfService;
import com.aspire.ums.cmdb.serverProject.service.ICmdbServerCabinetBillService;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: jiangxuwen
 * @Datetime: 2021/01/27 16:32
 */
@RestController
@Slf4j
public class CmdbServerCabinatBillController implements CmdbServerCabinatBillAPI {

    @Autowired
    private ICmdbServerCabinetBillService cabinetBillService;

    @Autowired
    private ICmdbCabinetBillConfService cabinetBillConfService;

    @Override
    public ResultVo createCmdbCabinetBillConfig(CmdbCabinetBillConf billConf) {
        return null;
    }

    @Override
    public ResultVo updateCmdbCabinetBillConfig(CmdbCabinetBillConf cmdbCabinet) {
        return null;
    }

    @Override
    public ResultVo deleteCmdbCabinetBillConfig(String id) {
        return null;
    }

    @Override
    public ResultVo batchDeleteCmdbCabinetBillConfig(String ids) {
        return null;
    }

    @Override
    public ResultVo<List<CmdbCabinetBillConf>> queryCmdbCabinetBillConfig(CmdbCabinetBillConf cmdbCabinet) {
        return null;
    }

    @Override
    public ResultVo createCmdbcabinetBillReport(CmdbCabinetBill bill) {
        return null;
    }

    @Override
    public ResultVo updateCmdbCabinetBillReport(CmdbCabinetBill bill) {
        return null;
    }

    @Override
    public ResultVo deleteCmdbCabinetBillReport(String id) {
        return null;
    }

    @Override
    public ResultVo batchDeleteCmdbCabinetBillReport(String ids) {
        return null;
    }

    @Override
    public ResultVo<List<CmdbCabinetBill>> queryCmdbCabinetBillReport(CmdbCabinetBill bill) {
        return null;
    }
}
