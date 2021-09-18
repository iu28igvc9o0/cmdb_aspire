package com.aspire.ums.cmdb.serverProject.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.ums.cmdb.common.ResultVo;
import com.aspire.ums.cmdb.serverProject.CmdbNetworkLineBillAPI;
import com.aspire.ums.cmdb.serverProject.payload.CmdbNetworkLineBill;
import com.aspire.ums.cmdb.serverProject.service.ICmdbNetworkLineBillService;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: jiangxuwen
 * @Datetime: 2021/01/27 16:32
 */
@RestController
@Slf4j
public class CmdbNetworkLineBillController implements CmdbNetworkLineBillAPI {

    @Autowired
    private ICmdbNetworkLineBillService networkLineBillService;

    @Override
    public ResultVo createNetworkLineBill(@RequestBody CmdbNetworkLineBill bill) {
        ResultVo vo = new ResultVo();
        try {
            networkLineBillService.insert(bill);
            vo.setSuccess(true);
        } catch (Exception e) {
            log.error("", e);
            vo.setSuccess(false);
            vo.setMsg("新增失败!");
        }

        return vo;
    }

    @Override
    public ResultVo updateNetworkLineBill(@RequestBody CmdbNetworkLineBill bill) {
        ResultVo vo = new ResultVo();
        try {
            networkLineBillService.update(bill);
            vo.setSuccess(true);
        } catch (Exception e) {
            log.error("", e);
            vo.setSuccess(false);
            vo.setMsg("修改失败!");
        }

        return vo;
    }

    @Override
    public ResultVo deleteNetworkLineBill(String id) {
        ResultVo vo = new ResultVo();
        try {
            CmdbNetworkLineBill entity = new CmdbNetworkLineBill();
            entity.setId(id);
            networkLineBillService.delete(entity);
            vo.setSuccess(true);
        } catch (Exception e) {
            log.error("", e);
            vo.setSuccess(false);
            vo.setMsg("删除失败!");
        }

        return vo;
    }

    @Override
    public ResultVo batchDeleteNetworkLineBill(String ids) {
        ResultVo vo = new ResultVo();
        try {
            for (String id : ids.split(",")) {
                CmdbNetworkLineBill entity = new CmdbNetworkLineBill();
                entity.setId(id);
                networkLineBillService.delete(entity);
            }
            vo.setSuccess(true);
        } catch (Exception e) {
            log.error("", e);
            vo.setSuccess(false);
            vo.setMsg("删除失败!");
        }

        return vo;
    }

    @Override
    public ResultVo<List<CmdbNetworkLineBill>> queryNetworkLineBill(@RequestBody CmdbNetworkLineBill bill) {
        return null;
    }
}
