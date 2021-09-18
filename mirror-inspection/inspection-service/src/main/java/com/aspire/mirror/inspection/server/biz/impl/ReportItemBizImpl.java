package com.aspire.mirror.inspection.server.biz.impl;

import java.util.List;

import com.aspire.mirror.common.entity.Page;
import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.common.util.PageUtil;
import com.aspire.mirror.inspection.api.dto.constant.TaskSyncOperate;
import com.aspire.mirror.inspection.api.dto.model.SelfHealingItem;
import com.aspire.mirror.inspection.server.clientservice.OpsServiceClient;
import com.aspire.mirror.inspection.server.clientservice.payload.SimpleAgentHostInfo;
import com.aspire.mirror.inspection.server.dao.ReportDao;
import com.aspire.mirror.inspection.server.dao.po.Report;
import com.aspire.mirror.inspection.api.dto.ReportItemExt;
import com.aspire.mirror.inspection.server.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.aspire.mirror.inspection.api.dto.model.ReportItemDTO;
import com.aspire.mirror.inspection.server.biz.ReportItemBiz;
import com.aspire.mirror.inspection.server.dao.ReportItemDao;
import com.aspire.mirror.inspection.server.dao.po.ReportItem;
import com.aspire.mirror.inspection.server.dao.po.transform.ReportItemTransformer;
import org.springframework.transaction.annotation.Transactional;

/**
 * inspection_report_item业务层实现类
 * <p>
 * 项目名称:  微服务运维平台
 * 包:       com.aspire.mirror.inspection.server.biz.impl
 * 类名称:    ReportItemBizImpl.java
 * 类描述:    inspection_report_item业务逻辑层实现类
 * 创建人:    ZhangSheng
 * 创建时间:  2018-07-27 13:48:08
 */
@Service
@Transactional
@Slf4j
public class ReportItemBizImpl implements ReportItemBiz {

    @Value("${selfRepairTopic:ops_auto_repair_alert}")
    private String SELF_HEALING_TOPIC;
    /**
     * 新增数据
     *
     * @param reportItemDTO inspection_report_itemDTO对象
     * @return 自增主键
     */
    public Long insert(final ReportItemDTO reportItemDTO) {
        if (null == reportItemDTO) {
            LOGGER.error("method[insert] param[reportItemDTO] is null");
            throw new RuntimeException("param[reportItemDTO] is null");
        }
        ReportItem reportItem = ReportItemTransformer.toPo(reportItemDTO);
        reportItemDao.insert(reportItem);

        SimpleAgentHostInfo hostInfo = opsServiceClient.queryAgentInfoByProxyIdConcatIP(reportItemDTO.getObjectId());
        String deviceClass = (String) hostInfo.getExtendAttrMap().get("device_class");
        String deviceType = (String) hostInfo.getExtendAttrMap().get("device_type");
        String deviceName = (String) hostInfo.getExtendAttrMap().get("device_name");
        String idcType = hostInfo.getPool();
        String bizSystem = (String) hostInfo.getExtendAttrMap().get("bizSystem");


        ReportItemExt reportItemExt = reportItemDTO.getReportItemExt();
        reportItemExt.setDeviceClass(deviceClass);
        reportItemExt.setDeviceType(deviceType);
        reportItemExt.setDeviceName(deviceName);
        reportItemExt.setIdcType(idcType);
        reportItemExt.setBizSystem(bizSystem);
        reportItemExt.setReportItemId(reportItem.getReportItemId());
        reportItemDao.insertReportItemExt(reportItemExt);

        //插入元素值表
        reportItemDTO.getReportItemValueList().stream().forEach(item -> item.setReportItemId(reportItem.getReportItemId()));
        if (!CollectionUtils.isEmpty(reportItemDTO.getReportItemValueList())) {
            reportItemDao.batchInsertReportItemValue(reportItemDTO.getReportItemValueList());
        }
        // 自动发起报告完毕检查 ----脚本巡检插入报告元素已做过一次状态判断，当前模板存在没有触发器的情况
        Report report = reportDao.selectByPrimaryKey(reportItem.getReportId());
        reportItemCallBackBiz.generateReportAndNotify(report.getReportId(), report.getTaskId());
        // 自愈数据kafka推送
        try {
            SelfHealingItem selfHealingItem = new SelfHealingItem();
            selfHealingItem.setCurMoniValue(reportItemDTO.getValue());
            selfHealingItem.setSourceMark(TaskSyncOperate.XJ_MARK.getKey());
            selfHealingItem.setItemKey(reportItemDTO.getItemId());
            selfHealingItem.setMoniObject(report.getTaskId());
            selfHealingItem.setMoniIndex(null);
            if (hostInfo != null) {
                selfHealingItem.setIdcType(hostInfo.getPool());
                selfHealingItem.setDeviceIp(hostInfo.getAgentIp());
            }
            selfHealingItem.setAlertId(null);

            this.kafkaTemplate.send(SELF_HEALING_TOPIC, JsonUtil.toJacksonJson(selfHealingItem));
        } catch (Exception e) {
            log.error("self healing data to kafka error!", e);
        }
        return reportItem.getReportItemId();
    }

    @Override
    public int insertBatch(List<ReportItemDTO> reportItemDTOList) {
        for (ReportItemDTO reportItemDTO : reportItemDTOList) {
            this.insert(reportItemDTO);
        }
        return reportItemDTOList.size();
    }
    /**
     * 根据参数选择性更新数据
     *
     * @param reportItemDTO inspection_report_itemDTO对象
     * @return int 数据条数
     */
    public int updateByPrimaryKeySelective(final ReportItemDTO reportItemDTO) {
        if (null == reportItemDTO) {
            LOGGER.error("method[updateByPrimaryKey] param[reportItemDTO] is null");
            throw new RuntimeException("param[reportItemDTO] is null");
        }
        ReportItem reportItem = ReportItemTransformer.toPo(reportItemDTO);
        return reportItemDao.updateByPrimaryKeySelective(reportItem);
    }

    /**
     * 根据参数选择性更新数据
     *
     * @param reportItemDTOList inspection_report_itemDTO列表对象
     * @return int 数据条数
     */
    public int updateBatchByPrimaryKeySelective(final List<ReportItemDTO> reportItemDTOList) {
        if (CollectionUtils.isEmpty(reportItemDTOList)) {
            LOGGER.error("method[updateByPrimaryKey] param[reportItemDTOList] is null");
            throw new RuntimeException("param[reportItemDTOList] is null");
        }
        List<ReportItem> reportItemList = ReportItemTransformer.toPo(reportItemDTOList);
        return reportItemDao.updateBatchByPrimaryKeySelective(reportItemList);
    }

    @Override
    public int selectCountByReportId(final String reportId) {
        int count = reportItemDao.selectCountByReportId(reportId);
        return count;
    }

    @Override
    public Integer getItemNumByReportId(String reportId) {
        return reportItemDao.getItemNumByReportId(reportId);
    }

    @Override
    public PageResponse<ReportItemDTO> pageList(PageRequest pageRequest) {
        Page page = PageUtil.convert(pageRequest);
        int count = reportItemDao.pageListCount(page);
        PageResponse<ReportItemDTO> pageResponse = new PageResponse<>();
        pageResponse.setCount(count);
        if (count > 0) {
            List<ReportItem> listReportItem = reportItemDao.pageList(page);
            pageResponse.setResult(ReportItemTransformer.fromPo(listReportItem));
        }
        return pageResponse;
    }

    @Override
    public List<ReportItemDTO> selectByReportId(final String reportId) {
        List<ReportItem> items = reportItemDao.selectByReportId(reportId);
        List<ReportItemDTO> lItemDTOs = null;
        if (items != null) {
            lItemDTOs = ReportItemTransformer.fromPo(items);
        }
        return lItemDTOs;
    }

    /**
     * slf4j
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportItemBizImpl.class);

    /**
     * 数据访问层接口
     */
    @Autowired
    private ReportItemDao reportItemDao;


    @Autowired
    private ReportDao reportDao;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private OpsServiceClient opsServiceClient;

    @Autowired
    private ReportItemCallBackBizImpl reportItemCallBackBiz;

} 
