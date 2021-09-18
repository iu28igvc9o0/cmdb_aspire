package com.aspire.mirror.inspection.server.biz ;

import java.util.List;
import java.util.Map;

import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.inspection.api.dto.model.ReportDTO;
import com.aspire.mirror.inspection.api.dto.model.ReportTaskDTO;

/**
 * inspection_report业务层接口
 *
 * 项目名称: 微服务运维平台
 * 包:      com.aspire.mirror.inspection.server.biz
 * 类名称:   ReportBiz.java
 * 类描述:   inspection_report业务逻辑层接口
 * 创建人:   ZhangSheng
 * 创建时间: 2018-07-27 13:48:08
 */
public interface ReportBiz {

    /**
     * 新增数据
     *
     * @param reportDTO inspection_reportDTO对象
     * @return int 新增数据条数
     */
    int insert(ReportDTO reportDTO);

    /**
     * 根据参数选择性更新数据
     *
     * @param reportDTO inspection_reportDTO对象
     * @return int 数据条数
     */
    int updateByPrimaryKeySelective(ReportDTO reportDTO);

    /**
     * 根据dto实体查询列表
     *
     * @param reportDTO inspection_reportDTO对象
     * @return List<Report>  返回集合
     */
    List<ReportDTO> select(ReportDTO reportDTO);
//=========================================以下手写实现======================================================>
    
    /**
     * 根据主键查询
     *
     * @param reportId 主键
     * @return ReportDTO 返回对象
     */
    ReportDTO selectByPrimaryKey(final String reportId);
    /**
     * 根据任务ID查询巡检报告
     * @param taskId 任务id
     * @return
     */
    List<ReportTaskDTO> selectByPage(final PageRequest pageRequest);
	
	/**
	 * 根据任务id统计报告数量
	 * @param taskId 任务id
	 * @return
	 */
	int selectCount(final PageRequest pageRequest);
	/**
	 * 根据分页对象  分页查询
	 * @param reportPageRequest:巡检查询分页对象
	 * @return
	 */
//	List<ReportDTO> pageList(final PageRequest pageRequest);
	/**
	 * 根据分页对象统计
	 * @param reportPageRequest
	 * @return
	 */
	int pageCount(final PageRequest pageRequest);


    void regenerate(String reportId, Map<String, String> triggerMap);

    int updateReportFilePath(String reportId, String filePath);
} 
