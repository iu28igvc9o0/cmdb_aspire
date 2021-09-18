package com.aspire.ums.cmdb.ipCollect.web;

import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.common.ResultVo;
import com.aspire.ums.cmdb.ipCollect.IIpClashAPI;
import com.aspire.ums.cmdb.ipCollect.payload.entity.*;
import com.aspire.ums.cmdb.ipCollect.service.CmdbIpClashService;
import com.aspire.ums.cmdb.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: fanshenquan
 * @Datetime: 2020/5/27 20:44
 */
@Slf4j
@RestController
public class IpClashController implements IIpClashAPI {
    @Autowired
    private CmdbIpClashService cmdbIpClashService;

    @Override
    public ResultVo<List<CmdbIpClashFindPageResponse>> findPageList(@RequestBody CmdbIpClashFindPageRequest request) {
        log.info("findPageList request is {}", request);
        ResultVo<List<CmdbIpClashFindPageResponse>> resultVo = new ResultVo(true, "查询成功！");
        try {
            request.updatePageNo();
            resultVo.setData(cmdbIpClashService.findData(request));
        } catch (Exception e) {
            log.error("查询失败！", e);
            resultVo.setSuccess(false);
            resultVo.setMsg("查询失败！");
        }
        return resultVo;
    }

    @Override
    public CmdbIpClashResult findPage(@RequestBody CmdbIpClashFindPageRequest request) {
        log.info("findPage request is {}", request);
        CmdbIpClashResult result = new CmdbIpClashResult();
        try {
            request.updatePageNo();
            result.setData(cmdbIpClashService.findData(request));
            result.setTotalSize(cmdbIpClashService.findDataCount(request));
            result.setTopTotal(cmdbIpClashService.findDataTopTotal(request));
        } catch (Exception e) {
            log.error("查询失败！", e);
        }
        return result;
    }

    @Override
    public ResultVo updateHandleStatus(@RequestBody CmdbIpClashUpdateRequest request) {
        log.info("updateHandleStatus request is {}", request);
        ResultVo resultVo = new ResultVo(true, "更新成功！");
        try {
            if (CollectionUtils.isEmpty(request.getMainId())) {
                if (request.getMainId().stream().allMatch(mainId -> StringUtils.isEmpty(mainId))) {
                    throw new RuntimeException("主体ID mainId 不能为空！");
                }
            } else if (StringUtils.isEmpty(request.getHandleStatus())) {
                throw new RuntimeException("处理状态 handleStatus 不能为空！");
            } else if (request.getHandleStatus().equals("1") && StringUtils.isEmpty(request.getNotHandleReason())) {
                throw new RuntimeException("设置为暂不处理时，原因不能留空！");
            } else if ((request.getHandleStatus().equals("0") || request.getHandleStatus().equals("1"))
                    && StringUtils.isEmpty(request.getOperator())) {
                throw new RuntimeException("操作人不能留空！");
            } else if (request.getHandleStatus().equals("3")) {
                throw new RuntimeException("当前处理状态为已处理！");
            }
            cmdbIpClashService.updateHandleStatus(request);
        } catch (Exception e) {
            log.error("更新处理状态失败！", e);
            resultVo.setSuccess(false);
            resultVo.setMsg(String.format("更新失败！%s", e.getMessage()));
        }
        return resultVo;
    }

    @Override
    public ResultVo createIpClash(@RequestBody Result<CmdbIpClashCreateRequest> request) {
        log.info("createIpClash request is {}", request);
        ResultVo resultVo = new ResultVo(true, "success");
        try {
            if (request.getTotalSize() > 0) {
                cmdbIpClashService.ipClashSaveList(request.getData());
            }
        } catch (Exception e) {
            log.error("系统异常！", e);
            resultVo.setSuccess(false);
            resultVo.setMsg("系统异常！");
        }
        return resultVo;
    }

    @Override
    public ResultVo rebuildClashList(@RequestBody(required = false) List<CmdbIpClashRebuildRequest> list) {
        log.info("rebuildClashList request is {}", list);
        ResultVo resultVo = new ResultVo(true, "数据接收成功");
        try {
            if (null != list && !list.isEmpty()) {
                cmdbIpClashService.rebuildClashList(list);
            } else {
                resultVo.setSuccess(false);
                resultVo.setMsg("没有入参");
            }
        } catch (Exception e) {
            log.error("系统异常！", e);
            resultVo.setSuccess(false);
            resultVo.setMsg("系统异常！");
        }
        return resultVo;
    }

}
