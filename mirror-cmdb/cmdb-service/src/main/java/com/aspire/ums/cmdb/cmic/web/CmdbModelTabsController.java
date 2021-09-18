package com.aspire.ums.cmdb.cmic.web;

import com.aspire.ums.cmdb.cmic.IModelTabsAPI;
import com.aspire.ums.cmdb.cmic.payload.CmdbModelTabsBase;
import com.aspire.ums.cmdb.cmic.payload.CmdbModelTabsReq;
import com.aspire.ums.cmdb.cmic.payload.CmdbModelTabsResp;
import com.aspire.ums.cmdb.cmic.service.CmdbModelTabsService;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.common.ResultVo;
import com.aspire.ums.cmdb.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * @Author :  fanshenquan
 * @CreateDate :  2020/5/12 17:34
 */
@RestController
@Slf4j
public class CmdbModelTabsController implements IModelTabsAPI {

    @Autowired
    private CmdbModelTabsService cmdbModelTabsService;

    @Override
    public Result<CmdbModelTabsResp> getModelTabsList(@RequestBody CmdbModelTabsReq cmdbModelTabsReq) {
        log.info("cmdbModelTabsReq is {}", cmdbModelTabsReq);
        cmdbModelTabsReq.updatePageNo();
        Result result = new Result();
        try {
            result.setData(cmdbModelTabsService.findPage(cmdbModelTabsReq));
            result.setTotalSize(cmdbModelTabsService.findPageCount(cmdbModelTabsReq));
        } catch (Exception e) {
            log.error("getModelTabsList查询失败", e);
        }
        return result;
    }

    @Override
    public ResultVo<CmdbModelTabsResp> getModelTabsById(@RequestParam(value = "tabsId") String tabsId) {
        log.info("tabsId is {}", tabsId);
        ResultVo<CmdbModelTabsResp> resultVo = new ResultVo(true, "操作成功");
        try {
            CmdbModelTabsResp resp = cmdbModelTabsService.findOneDataById(tabsId);
            if (resp == null) {
                resultVo.setSuccess(false);
                resultVo.setMsg(String.format("根据id %s 查询无结果", tabsId));
            } else {
                resultVo.setData(resp);
            }
        } catch (Exception e) {
            log.error("getModelTabsById根据tabsId获取详情失败", e);
            resultVo.setSuccess(false);
            resultVo.setMsg(String.format("根据id %s 获取详情失败", tabsId));
        }
        return resultVo;
    }

    @Override
    public ResultVo saveModelTabs(@RequestBody CmdbModelTabsBase cmdbModelTabsBase) {
        log.info("cmdbModelTabsBase is {}", cmdbModelTabsBase);
        ResultVo resultVo = new ResultVo();
        try {
            if (StringUtils.isEmpty(cmdbModelTabsBase.getTabsId())) {
                resultVo.setMsg(cmdbModelTabsService.insertTabs(cmdbModelTabsBase));
                resultVo.setSuccess(resultVo.getMsg().contains("完成") ? true : false);
            } else {
                resultVo.setMsg(cmdbModelTabsService.updateTabsById(cmdbModelTabsBase));
                resultVo.setSuccess(resultVo.getMsg().contains("成功") ? true : false);
            }
        } catch (Exception e) {
            log.error("saveModelTabs保存或更新数据失败", e);
            resultVo.setSuccess(false);
            resultVo.setMsg("保存或更新数据失败");
        }
        return resultVo;
    }

    @Override
    public ResultVo deleteModelTabsById(@RequestParam(value = "idList") String[] idList) {
        log.info("idList is {}", Arrays.toString(idList));
        try {
            // List<String> tabsIdList = Arrays.asList(idList.split(","));
            List<String> tabsIdList = Arrays.asList(idList);
            cmdbModelTabsService.deleteByIdBatch(tabsIdList);
            return ResultVo.success();
        } catch (Exception e) {
            log.error("deleteModelTabsById保存或更新数据失败", e);
        }
        return ResultVo.fail();
    }
}
