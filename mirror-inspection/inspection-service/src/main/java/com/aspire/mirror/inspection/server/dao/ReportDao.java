package com.aspire.mirror.inspection.server.dao ;

import com.aspire.mirror.common.entity.Page;
import com.aspire.mirror.inspection.api.dto.model.InspectionCountQueryModel;
import com.aspire.mirror.inspection.api.dto.model.InspectionCountResp;
import com.aspire.mirror.inspection.api.dto.model.OpsTimeConsumeStatisticBase;
import com.aspire.mirror.inspection.server.dao.po.Report;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * inspection_report数据访问层接口
 *
 * 项目名称:  微服务运维平台
 * 包:       com.aspire.mirror.inspection.server.dao
 * 类名称:    ReportDao.java
 * 类描述:    inspection_report数据访问层接口
 * 创建人:    ZhangSheng
 * 创建时间:  2018-07-27 13:48:08
 */
@Mapper
public interface ReportDao {

    /**
     * 新增数据
     *
     * @param report inspection_reportPO对象
     * @return int 新增数据条数
     */
    int insert(Report report);

    /**
     * 批量新增inspection_report数据
     *
     * @param listReport inspection_reportPO集合对象
     * @return int 新增数据条数
     */
    int insertByBatch(List<Report> listReport);
    /**
     * 根据主键删除数据
     *
     * @param reportId 主键
     * @return int 删除数据条数
     */
    int deleteByPrimaryKey(@Param(value="reportId")String reportId);

    /**
     * 根据主键数组批量删除数据
     *
     * @param reportIdArrays 主键数组
     * @return int 删除数据条数
     */
    int deleteByPrimaryKeyArrays(String[] reportIdArrays);

    /**
     * 根据条件删除数据
     *
     * @param report  inspection_reportPO对象
     * @return int 删除数据条数
     */
    int delete(Report report);

    /**
     * 根据参数选择性更新数据
     *
     * @param report inspection_reportPO对象
     * @return int 数据条数
     */
    int updateByPrimaryKeySelective(Report report);

    /**
     * 根据主键更新数据
     *
     * @param report inspection_reportPO对象
     * @return int 数据条数
     */
    int updateByPrimaryKey(Report report);



    /**
     * 根据主键数组查询
     *
     * @param reportIdArrays 主键数组
     * @return List<Report> 返回集合对象
     */
    List<Report> selectByPrimaryKeyArrays(String[] reportIdArrays);
    /**
     * 根据po实体查询列表
     *
     * @param report inspection_reportPO对象
     * @return List<Report>  返回集合
     */
    List<Report> select(Report report);
//----------------------------以下手写实现---------------------------------->
    /**
     * 根据主键查询
     *
     * @param reportId 主键
     * @return Report 返回对象
     */
    Report selectByPrimaryKey(@Param(value="reportId")String reportId);
    /**
     * 根据分页对象查询巡检报告
     * @param taskId
     * @return
     */
	List<Report> selectByPage(final Page page);
	/**
	 * 根据分页对象查询分页总记录数
	 * @param page
	 * @return
	 */
	int selectCount(final Page page);
	
	/**
	 * 根据某个巡检报告查询巡检报告分页信息
	 * @param page
	 * @return
	 */
	int selectByTaskIdCount(final Page page);


    /**
     *
     * @param reportId
     * @param filePath
     * @return
     */
    int updateReportFilePath(@Param(value="reportId") String reportId, @Param(value="filePath") String filePath);

    InspectionCountResp selectInspectionCount(InspectionCountQueryModel inspectionCountQueryModel);

    OpsTimeConsumeStatisticBase getInspectionTimeStatistic(Map<String,Object> param);
} 
