package com.migu.tsg.microservice.atomicservice.composite.controller.faultReport;

import com.aspire.mirror.alert.api.dto.faultReport.FaultReportManageReq;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.payload.faultReport.CompFaultReportManageReq;
import com.aspire.mirror.composite.service.faultReport.ICompFaultReportManageService;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.faultReport.FaultReportManageClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class CompFaultReportManageController implements ICompFaultReportManageService {

    @Autowired
    private FaultReportManageClient faultReportManageClient;
    /**
     * 新增
     *
     * @param manage
     * @return
     */
    @Override
    public void insert(@RequestBody CompFaultReportManageReq manage) {
        faultReportManageClient.insert(PayloadParseUtil.jacksonBaseParse(FaultReportManageReq.class, manage));
    }

    /**
     * 跟新
     *
     * @param manage
     * @return
     */
    @Override
    public void update(@RequestBody CompFaultReportManageReq manage) {
        faultReportManageClient.update(PayloadParseUtil.jacksonBaseParse(FaultReportManageReq.class, manage));
    }

    /**
     * 查询详情
     *
     * @param id
     * @return
     */
    @Override
    public CompFaultReportManageReq selectById(@PathVariable("id") Integer id) {
        FaultReportManageReq faultReportManageReq = faultReportManageClient.selectById(id);
        return PayloadParseUtil.jacksonBaseParse(CompFaultReportManageReq.class, faultReportManageReq);

    }

    /**
     * 导出附件
     *
     * @param ids
     * @return
     */
    @Override
    public Map<String, Object> exportAnnex(@RequestBody List<Integer> ids) {

        return faultReportManageClient.exportAnnex(ids);
    }

    /**
     * 列表查询
     *
     * @param faultReportUser
     * @param reportUser
     * @param faultHappenTimeFrom
     * @param faultHappenTimeTo
     * @param pageSize
     * @param pageNum
     * @return
     */
    @Override
    public PageResponse<CompFaultReportManageReq> selectListByParams(@RequestParam(value = "faultReportUser",required = false) String faultReportUser,
                                                                 @RequestParam(value = "reportUser",required = false) String reportUser,
                                                                 @RequestParam(value = "faultHappenTimeFrom",required = false) String faultHappenTimeFrom,
                                                                 @RequestParam(value = "faultHappenTimeTo",required = false) String faultHappenTimeTo,
                                                                 @RequestParam(value = "pageSize",defaultValue = "20") Integer pageSize,
                                                                 @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        PageResponse<FaultReportManageReq> faultReportManagePageResponse = faultReportManageClient.selectListByParams(faultReportUser, reportUser, faultHappenTimeFrom, faultHappenTimeTo, pageSize, pageNum);
        PageResponse<CompFaultReportManageReq> pageResponse = new PageResponse<>();
        pageResponse.setCount(faultReportManagePageResponse.getCount());
        pageResponse.setResult(PayloadParseUtil.jacksonBaseParse(CompFaultReportManageReq.class, faultReportManagePageResponse.getResult()));
        return pageResponse;
    }

    /**
     * 导出列表
     *
     * @param faultReportUser
     * @param reportUser
     * @param faultHappenTimeFrom
     * @param faultHappenTimeTo
     * @return
     */
    @Override
    public Map<String, Object> export(@RequestParam(value = "faultReportUser",required = false) String faultReportUser,
                                      @RequestParam(value = "reportUser",required = false) String reportUser,
                                      @RequestParam(value = "faultHappenTimeFrom",required = false) String faultHappenTimeFrom,
                                      @RequestParam(value = "faultHappenTimeTo",required = false) String faultHappenTimeTo) {
        return faultReportManageClient.export(faultReportUser, reportUser, faultHappenTimeFrom, faultHappenTimeTo);
    }

    /**
     * 导入
     *
     * @param id
     * @param file
     * @return
     */
    @Override
    public Map<String, Object> importFile(@PathVariable("id") Integer id, @RequestPart("file") MultipartFile file) {
        return faultReportManageClient.importFile(id,file);
    }
}
