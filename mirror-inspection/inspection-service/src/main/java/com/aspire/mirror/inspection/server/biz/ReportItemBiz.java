package com.aspire.mirror.inspection.server.biz ;

import java.util.List;

import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.inspection.api.dto.model.ReportItemDTO;

/**
 * inspection_report_item业务层接口
 *
 * 项目名称: 微服务运维平台
 * 包:      com.aspire.mirror.inspection.server.biz
 * 类名称:   ReportItemBiz.java
 * 类描述:   inspection_report_item业务逻辑层接口
 * 创建人:   ZhangSheng
 * 创建时间: 2018-07-27 13:48:08
 */
public interface ReportItemBiz {

    /**
     * 新增数据
     *
     * @param reportItemDTO inspection_report_itemDTO对象
     * @return int 新增数据条数
     */
    Long insert(ReportItemDTO reportItemDTO);

//    /**
//     * 批量新增inspection_report_item数据
//     *
//     * @param listReportItemDTO inspection_report_itemDTO集合对象
//     * @return int 新增数据条数
//     */
//    int insertByBatch(List<ReportItemDTO> listReportItemDTO);
    /**
     * 根据主键删除数据
     *
     * @param reportItemId 主键
     * @return int 删除数据条数
     */
//    int deleteByPrimaryKey(String reportItemId);

    /**
     * 根据主键数组批量删除数据
     *
     * @param reportItemIdArrays 主键数组
     * @return int 删除数据条数
     */
//    int deleteByPrimaryKeyArrays(String[] reportItemIdArrays);

    /**
     * 根据条件删除数据
     *
     * @param reportItemDTO  inspection_report_itemDTO对象
     * @return int 删除数据条数
     */
//    int delete(ReportItemDTO reportItemDTO);

    /**
     * 根据参数选择性更新数据
     *
     * @param reportItemDTO inspection_report_itemDTO对象
     * @return int 数据条数
     */
    int updateByPrimaryKeySelective(ReportItemDTO reportItemDTO);

    /** 批量更新数据
     *
			 * @param reportItemDTOList inspection_report_itemDTO对象
     * @return int 数据条数
     */
	int updateBatchByPrimaryKeySelective(List<ReportItemDTO> reportItemDTOList);

    /**
     * 根据主键更新数据
     *
     * @param reportItemDTO inspection_report_itemDTO对象
     * @return int 数据条数
     */
//    int updateByPrimaryKey(ReportItemDTO reportItemDTO);

    /**
     * 根据主键查询
     *
     * @param reportItemId 主键
     * @return ReportItemDTO 返回对象
     */
//    ReportItemDTO selectByPrimaryKey(String reportItemId);

    /**
     * 根据主键数组查询
     *
     * @param reportItemIdArrays 主键数组
     * @return List<ReportItemDTO> 返回集合对象
     */
//    List<ReportItemDTO> selectByPrimaryKeyArrays(String[] reportItemIdArrays);
    /**
     * 根据dto实体查询列表
     *
     * @param reportItemDTO inspection_report_itemDTO对象
     * @return List<ReportItem>  返回集合
     */
//    List<ReportItemDTO> select(ReportItemDTO reportItemDTO);

    /**
     * 根据DTO实体查询条数
     *
     * @param reportItemDTO inspection_report_itemDTO对象
     * @return int 数据条数
     */
//    int selectCount(ReportItemDTO reportItemDTO);
    /**
     * 根据reportId查询ReportItem
     * @param reportId
     * @return
     */
	List<ReportItemDTO> selectByReportId(String reportId);
	/**
	 * 根据reportId统计
	 * @param reportId
	 * @return
	 */
	int selectCountByReportId(String reportId);

    Integer getItemNumByReportId(String reportId);

    PageResponse<ReportItemDTO> pageList(PageRequest page);

    int insertBatch(List<ReportItemDTO> reportItemDTOList);
} 
