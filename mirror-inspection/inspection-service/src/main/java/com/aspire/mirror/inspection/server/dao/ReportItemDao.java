package com.aspire.mirror.inspection.server.dao ;

import java.util.List;

import com.aspire.mirror.common.entity.Page;
import com.aspire.mirror.inspection.api.dto.ReportItemExt;
import com.aspire.mirror.inspection.api.dto.ReportItemValue;
import com.aspire.mirror.inspection.server.dao.po.ReportResultStatistic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aspire.mirror.inspection.server.dao.po.ReportItem;

/**
 * inspection_report_item数据访问层接口
 *
 * 项目名称:  微服务运维平台
 * 包:       com.aspire.mirror.inspection.server.dao
 * 类名称:    ReportItemDao.java
 * 类描述:    inspection_report_item数据访问层接口
 * 创建人:    ZhangSheng
 * 创建时间:  2018-07-27 13:48:08
 */
@Mapper
public interface ReportItemDao {

    /**
     * 新增数据
     *
     * @param reportItem inspection_report_itemPO对象
     * @return int 新增数据条数
     */
    int insert(ReportItem reportItem);

    /**
     * 批量新增inspection_report_item数据
     *
     * @param listReportItem inspection_report_itemPO集合对象
     * @return int 新增数据条数
     */
    int insertByBatch(List<ReportItem> listReportItem);
    /**
     * 根据主键删除数据
     *
     * @param reportItemId 主键
     * @return int 删除数据条数
     */
    int deleteByPrimaryKey(@Param(value="reportItemId")String reportItemId);

    /**
     * 根据主键数组批量删除数据
     *
     * @param reportItemIdArrays 主键数组
     * @return int 删除数据条数
     */
    int deleteByPrimaryKeyArrays(String[] reportItemIdArrays);

    /**
     * 根据条件删除数据
     *
     * @param reportItem  inspection_report_itemPO对象
     * @return int 删除数据条数
     */
    int delete(ReportItem reportItem);

    /**
     * 根据参数选择性更新数据
     *
     * @param reportItem inspection_report_itemPO对象
     * @return int 数据条数
     */
    int updateByPrimaryKeySelective(ReportItem reportItem);

    /**
     * 根据主键更新数据
     *
     * @param reportItem inspection_report_itemPO对象
     * @return int 数据条数
     */
    int updateByPrimaryKey(ReportItem reportItem);
    
    /**
    * 根据 report_id, item_id, device_id三个组合唯一索引, 更新该监控项的告警状态. <br/>
    *
    * 作者： pengguihua
    * @param reportItem
    * @return
    */  
    int updateStatusByUniqueKeys(ReportItem reportItem);

    /**
     * 根据主键查询
     *
     * @param reportItemId 主键
     * @return ReportItem 返回对象
     */
    ReportItem selectByPrimaryKey(@Param(value="reportItemId")String reportItemId);

    /**
     * 根据主键数组查询
     *
     * @param reportItemIdArrays 主键数组
     * @return List<ReportItem> 返回集合对象
     */
    List<ReportItem> selectByPrimaryKeyArrays(String[] reportItemIdArrays);
    /**
     * 根据po实体查询列表
     *
     * @param reportItem inspection_report_itemPO对象
     * @return List<ReportItem>  返回集合
     */
    List<ReportItem> select(ReportItem reportItem);

    /**
     * 根据po实体查询条数
     *
     * @param reportItem inspection_report_itemPO对象
     * @return int 数据条数
     */
    int selectCount(ReportItem reportItem);
    /**
     * 根据reportId统计
     * @param reportId
     * @return
     */
	int selectCountByReportId(@Param(value="reportId") String reportId);
	/**
	 * 根据reportId查询ReportItem结果集
	 * @param reportId
	 * @return
	 */
	List<ReportItem> selectByReportId(@Param(value="reportId") String reportId);

    int updateBatchByPrimaryKeySelective(List<ReportItem> reportItemList);

    Integer getItemNumByReportId(String reportId);

    int pageListCount(Page page);

    List<ReportItem> pageList(Page page);

    int selectNoResultCount(@Param(value="reportId") String reportId);

    int selectNoFinishItemCount(@Param(value="reportId") String reportId, @Param(value="taskId") String taskId);

    int selectCountByStatus(@Param(value="status") String status, @Param(value="reportId") String reportId);

    int insertReportItemExt(ReportItemExt reportItemExt);

    int updateExecStatusByUniqueKeys(ReportItem updateParam);

    int batchInsertReportItemValue(List<ReportItemValue> reportItemValueList);

    int updateReportItemValueStatus(ReportItemValue reportItemValue);

    void updateReportItemExtByReportItemId(ReportItemExt reportItemExt);

    ReportResultStatistic selectNumStatistic(@Param(value="reportId") String reportId);
}
