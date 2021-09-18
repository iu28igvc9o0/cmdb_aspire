package com.aspire.mirror.inspection.server.dao.po.transform ;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.util.CollectionUtils;

import com.aspire.mirror.inspection.server.dao.po.Report;
import com.aspire.mirror.inspection.server.dao.po.ReportTask;
import com.aspire.mirror.inspection.api.dto.model.ReportDTO;
import com.aspire.mirror.inspection.api.dto.model.ReportTaskDTO;

import java.util.List;
import java.util.Map;

/**
 * inspection_report对象转换类
 *
 * 项目名称:  微服务运维平台
 * 包:       com.aspire.mirror.inspection.server.dao.po.transform
 * 类名称:    ReportTransformer.java
 * 类描述:    inspection_report对应的PO与DTO的转换类
 * 创建人:    ZhangSheng
 * 创建时间:  2018-07-27 13:48:08
 */
public final class ReportTransformer  {

    private ReportTransformer(){
    }

   /**
    * 将inspection_reportPO实体转换为inspection_reportDTO实体
    *
    * @param  report inspection_reportPO实体
    * @return ReportDTO inspection_reportDTO实体
    */
    public static ReportDTO fromPo(final Report report) {
        if (null == report) {
            return null;
        }
        
        ReportDTO reportDTO = new ReportDTO();
        reportDTO.setReportId(report.getReportId());
        reportDTO.setTaskId(report.getTaskId());
        reportDTO.setName(report.getName());
        reportDTO.setCreateTime(report.getCreateTime());
        reportDTO.setStatus(report.getStatus());
        reportDTO.setFinishTime(report.getFinishTime());
        reportDTO.setTotalTime(report.getTotalTime());
        reportDTO.setBizStatus(report.getBizStatus());
        reportDTO.setReportFilePath(report.getReportFilePath());
        reportDTO.setResult(report.getResult());
        return reportDTO;
    }
    
    /**
     * 将inspection_reportPO实体转换为inspection_reportDTO实体
     *
     * @param  report inspection_reportPO实体
     * @return ReportDTO inspection_reportDTO实体
     */
     public static ReportTaskDTO fromPo(final ReportTask report) {
         if (null == report) {
             return null;
         }
         ReportTaskDTO reportDTO = new ReportTaskDTO();
         reportDTO.setReportId(report.getReportId());
         reportDTO.setTaskId(report.getTaskId());
         reportDTO.setName(report.getName());
         reportDTO.setTaskName(report.getTaskName());
         reportDTO.setTaskType(report.getTaskType());
         reportDTO.setCreateTime(report.getCreateTime());
         reportDTO.setStatus(report.getStatus());
         reportDTO.setFinishTime(report.getFinishTime());
         reportDTO.setReportFilePath(report.getReportFilePath());
         reportDTO.setResult(report.getResult());
         return reportDTO;
     }
    
    /**
     * 将inspection_report业务实体对象集合转换为inspection_report持久化对象集合
     *
     * @param listReport inspection_report业务实体对象集合
     * @return List<ReportDTO> inspection_report持久化对象集合
     */
    public static List<ReportTaskDTO> fromPoTask(final List<ReportTask> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Lists.newArrayList();
        }
        List<ReportTaskDTO> listReportDTO = Lists.newArrayList();

        for (ReportTask report : list) {
            listReportDTO.add(ReportTransformer.fromPo(report));
        }
        return listReportDTO;
    }
    
    /**
     * 将inspection_report业务实体对象集合转换为inspection_report持久化对象集合
     *
     * @param listReport inspection_report业务实体对象集合
     * @return List<ReportDTO> inspection_report持久化对象集合
     */
    public static List<ReportDTO> fromPo(final List<Report> listReport) {
        if (CollectionUtils.isEmpty(listReport)) {
            return Lists.newArrayList();
        }
        List<ReportDTO> listReportDTO = Lists.newArrayList();

        for (Report report : listReport) {
            listReportDTO.add(ReportTransformer.fromPo(report));
        }
        return listReportDTO;
    }

   /**
    * 将inspection_reportDTO实体转换为inspection_reportPO实体
    *
    * @param  reportDTO inspection_reportDTO实体类
    * @return Report inspection_reportPO实体
    */
    public static Report toPo(final ReportDTO reportDTO) {
        if (null == reportDTO) {
            return null;
        }

        Report report = new Report();
        report.setReportId(reportDTO.getReportId());
        report.setTaskId(reportDTO.getTaskId());
        report.setName(reportDTO.getName());
        report.setCreateTime(reportDTO.getCreateTime());
        report.setStatus(reportDTO.getStatus());
        report.setFinishTime(reportDTO.getFinishTime());
        report.setReportFilePath(reportDTO.getReportFilePath());
        report.setTotalTime(reportDTO.getTotalTime());
        report.setBizStatus(reportDTO.getBizStatus());
        report.setResult(reportDTO.getResult());
        return report;
    }

    /**
     * 将inspection_report业务实体对象集合转换为inspection_report持久化对象集合
     *
     * @param listReportDTO inspection_report业务实体对象集合
     * @return List<Report> inspection_report持久化对象集合
     */
    public static List<Report> toPo(final List<ReportDTO> listReportDTO) {
        if (CollectionUtils.isEmpty(listReportDTO)) {
            return Lists.newArrayList();
        }
        List<Report> listReport = Lists.newArrayList();

        for (ReportDTO reportdTO : listReportDTO) {
            listReport.add(ReportTransformer.toPo(reportdTO));
        }
        return listReport;
    }
    /**
     * 将inspection_report业务实体对象集合转换为Map
     *
     * @param listReportDTO inspection_report业务实体对象集合
     * @return Map<String, ReportDTO> Map[key=String|value=ReportDTO]
     */
    public static Map<String, ReportDTO> toDTOMap(final List<ReportDTO> listReportDTO) {
        if (CollectionUtils.isEmpty(listReportDTO)) {
            return Maps.newHashMap();
        }
        Map<String, ReportDTO> reportDTOMaps = Maps.newHashMap();

        for (ReportDTO reportDTO : listReportDTO) {
            reportDTOMaps.put(reportDTO.getReportId(), reportDTO);
        }
        return reportDTOMaps;
    }

    /**
     * 将inspection_report业务实体对象集合中的主键进行提取并封装成数组
     *
     * @param listReportDTO inspection_report业务实体对象集合
     * @return String[] 主键数组
     */
    public static String[] toDTOPrimaryKeyArrays(final List<ReportDTO> listReportDTO) {
        if (CollectionUtils.isEmpty(listReportDTO)) {
            return null;
        }
        int size = listReportDTO.size();
        String[] reportIdArrays = new String[size];
        for (int i = 0 ;i< size; i++) {
            reportIdArrays[i] = listReportDTO.get(i).getReportId();
        }
        return reportIdArrays;
        }
} 
