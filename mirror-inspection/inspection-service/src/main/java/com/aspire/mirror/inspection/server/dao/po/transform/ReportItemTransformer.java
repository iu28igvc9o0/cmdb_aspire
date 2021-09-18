package com.aspire.mirror.inspection.server.dao.po.transform ;

import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.aspire.mirror.inspection.api.dto.model.ReportItemDTO;
import com.aspire.mirror.inspection.server.dao.po.ReportItem;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * inspection_report_item对象转换类
 *
 * 项目名称:  微服务运维平台
 * 包:       com.aspire.mirror.inspection.server.dao.po.transform
 * 类名称:    ReportItemTransformer.java
 * 类描述:    inspection_report_item对应的PO与DTO的转换类
 * 创建人:    ZhangSheng
 * 创建时间:  2018-07-27 13:48:08
 */
public final class ReportItemTransformer  {

    private ReportItemTransformer(){
    }

   /**
    * 将inspection_report_itemPO实体转换为inspection_report_itemDTO实体
    *
    * @param  reportItem inspection_report_itemPO实体
    * @return ReportItemDTO inspection_report_itemDTO实体
    */
    public static ReportItemDTO fromPo(final ReportItem reportItem) {
        if (null == reportItem) {
            return null;
        }
        
        ReportItemDTO reportItemDTO = new ReportItemDTO();
        reportItemDTO.setReportItemId(reportItem.getReportItemId());
        reportItemDTO.setReportId(reportItem.getReportId());
        reportItemDTO.setItemId(reportItem.getItemId());
        reportItemDTO.setServerType(reportItem.getServerType());
        reportItemDTO.setKey(reportItem.getKey());
        reportItemDTO.setObjectType(reportItem.getObjectType());
        reportItemDTO.setObjectId(reportItem.getObjectId());
        reportItemDTO.setClock(reportItem.getClock());
        reportItemDTO.setValue(reportItem.getValue());
        reportItemDTO.setPreValue(reportItem.getPreValue());
        reportItemDTO.setNs(reportItem.getNs());
        reportItemDTO.setStatus(reportItem.getStatus());
        reportItemDTO.setReportItemExt(reportItem.getReportItemExt());
        reportItemDTO.setReportItemValueList(reportItem.getReportItemValueList());
//        reportItemDTO.setExecStatus(reportItem.getExecStatus());
//        reportItemDTO.setLog(reportItem.getLog());
        reportItemDTO.setRiId(reportItem.getRiId());
        reportItemDTO.setName(reportItem.getName());
        reportItemDTO.setResultName(reportItem.getResultName());
        reportItemDTO.setResultDesc(reportItem.getResultDesc());
        return reportItemDTO;
    }

    /**
     * 将inspection_report_item业务实体对象集合转换为inspection_report_item持久化对象集合
     *
     * @param listReportItem inspection_report_item业务实体对象集合
     * @return List<ReportItemDTO> inspection_report_item持久化对象集合
     */
    public static List<ReportItemDTO> fromPo(final List<ReportItem> listReportItem) {
        if (CollectionUtils.isEmpty(listReportItem)) {
            return Lists.newArrayList();
        }
        List<ReportItemDTO> listReportItemDTO = Lists.newArrayList();

        for (ReportItem reportItem : listReportItem) {
            listReportItemDTO.add(ReportItemTransformer.fromPo(reportItem));
        }
        return listReportItemDTO;
    }

   /**
    * 将inspection_report_itemDTO实体转换为inspection_report_itemPO实体
    *
    * @param  reportItemDTO inspection_report_itemDTO实体类
    * @return ReportItem inspection_report_itemPO实体
    */
    public static ReportItem toPo(final ReportItemDTO reportItemDTO) {
        if (null == reportItemDTO) {
            return null;
        }

        ReportItem reportItem = new ReportItem();
        reportItem.setReportItemId(reportItemDTO.getReportItemId());
        reportItem.setReportId(reportItemDTO.getReportId());
        reportItem.setItemId(reportItemDTO.getItemId());
        reportItem.setServerType(reportItemDTO.getServerType());
        reportItem.setKey(reportItemDTO.getKey());
        reportItem.setObjectType(reportItemDTO.getObjectType());
        reportItem.setObjectId(reportItemDTO.getObjectId());
        reportItem.setClock(reportItemDTO.getClock());
        reportItem.setValue(reportItemDTO.getValue());
        reportItem.setPreValue(reportItemDTO.getPreValue());
        reportItem.setNs(reportItemDTO.getNs());
        reportItem.setStatus(reportItemDTO.getStatus());
//        reportItem.setExecStatus(reportItemDTO.getExecStatus());
//        reportItem.setLog(reportItemDTO.getLog());
        reportItem.setReportItemExt(reportItemDTO.getReportItemExt());
        reportItem.setRiId(reportItemDTO.getRiId());
        reportItem.setName(reportItemDTO.getName());
        return reportItem;
    }

    /**
     * 将inspection_report_item业务实体对象集合转换为inspection_report_item持久化对象集合
     *
     * @param listReportItemDTO inspection_report_item业务实体对象集合
     * @return List<ReportItem> inspection_report_item持久化对象集合
     */
    public static List<ReportItem> toPo(final List<ReportItemDTO> listReportItemDTO) {
        if (CollectionUtils.isEmpty(listReportItemDTO)) {
            return Lists.newArrayList();
        }
        List<ReportItem> listReportItem = Lists.newArrayList();

        for (ReportItemDTO reportItemdTO : listReportItemDTO) {
            listReportItem.add(ReportItemTransformer.toPo(reportItemdTO));
        }
        return listReportItem;
    }
    /**
     * 将inspection_report_item业务实体对象集合转换为Map
     *
     * @param listReportItemDTO inspection_report_item业务实体对象集合
     * @return Map<String, ReportItemDTO> Map[key=String|value=ReportItemDTO]
     */
    public static Map<Long, ReportItemDTO> toDTOMap(final List<ReportItemDTO> listReportItemDTO) {
        if (CollectionUtils.isEmpty(listReportItemDTO)) {
            return Maps.newHashMap();
        }
        Map<Long, ReportItemDTO> reportItemDTOMaps = Maps.newHashMap();

        for (ReportItemDTO reportItemDTO : listReportItemDTO) {
            reportItemDTOMaps.put(reportItemDTO.getReportItemId(), reportItemDTO);
        }
        return reportItemDTOMaps;
    }

    /**
     * 将inspection_report_item业务实体对象集合中的主键进行提取并封装成数组
     *
     * @param listReportItemDTO inspection_report_item业务实体对象集合
     * @return String[] 主键数组
     */
    public static Long[] toDTOPrimaryKeyArrays(final List<ReportItemDTO> listReportItemDTO) {
        if (CollectionUtils.isEmpty(listReportItemDTO)) {
            return null;
        }
        int size = listReportItemDTO.size();
        Long[] reportItemIdArrays = new Long[size];
        for (int i = 0 ;i< size; i++) {
            reportItemIdArrays[i] = listReportItemDTO.get(i).getReportItemId();
        }
        return reportItemIdArrays;
        }
}
