package com.aspire.ums.cmdb.v2.assessment.mapper;

import com.aspire.ums.cmdb.assessment.payload.CmdbServiceAssess;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 项目名称:
 * 包: com.aspire.ums.cmdb.v2.assessment.mapper
 * 类名称:
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/7/15 09:23
 * 版本: v1.0
 */
@Mapper
public interface CmdbServiceAssessMapper {

    /**
     * 获取所有得分列表
     * @param deviceType
     * @param quarter
     * @return
     */
    List<CmdbServiceAssess> queryScoreList(@Param("device_type") String deviceType,
                                         @Param("quarter") String quarter);

    /**
     * 查询设备总数
     * @param deviceType 设备类型
     * @param quarter 评估周期
     * @return
     */
    List<CmdbServiceAssess> queryTotalDeviceCount(@Param("deviceType") String deviceType, @Param("quarter") String quarter);

    /**
     * 保存设备总数量
     * @param assessEntity 得分实体
     */
    void saveTotalDeviceCount(CmdbServiceAssess assessEntity);

    /**
     * 删除得分信息
     * @param deviceType 设备类型
     * @param quarter 评估周期
     */
    void delete(@Param("deviceType") String deviceType, @Param("quarter") String quarter);

    /**
     * 保存最终得分
     * @param entity
     */
    void saveScore(CmdbServiceAssess entity);

    /**
     * 获取设备类型及评分状态
     * @param quarter 评分周期
     * @return
     */
    List<Map<String, Object>> getScoreDeviceTypeStatus(@Param("quarter") String quarter);

    /**
     * 根据传入的查询条件查询评分信息
     * @param queryEntity 查询entity
     * @return
     */
    List<CmdbServiceAssess> getServiceAccessScore(CmdbServiceAssess queryEntity);
    /**
     * 统计维修事件
     * @param queryEntity 查询entity
     * @return
     */
    CmdbServiceAssess statisticsRepairEvent(CmdbServiceAssess queryEntity);

    /**
     * 保存维修事件得分信息
     * @param assessScore
     */
    void saveRepairEventScore(CmdbServiceAssess assessScore);

    /**
     * 统计故障事件信息
     * @param queryEntity
     * @return
     */
    CmdbServiceAssess statisticsProblemEvent(CmdbServiceAssess queryEntity);

    /**
     * 保存故障事件得分信息
     * @param assessScore
     */
    void saveProblemEventScore(CmdbServiceAssess assessScore);

    /**
     * 统计设备问题信息
     * @param queryEntity
     * @return
     */
    CmdbServiceAssess statisticsEquipmentProblem(CmdbServiceAssess queryEntity);

    /**
     * 保存故障事件得分信息
     * @param assessScore
     */
    void saveEquipmentProblemScore(CmdbServiceAssess assessScore);

    /**
     *  获取故障处理平均时长 最小指标值
     * @param assessScore
     * @return
     */
    String statisticsMinProblemHandleTargetValue(CmdbServiceAssess assessScore);

    /**
     * 保存故障处理平均时长得分
     * @param assessScore
     */
    void saveProblemHandleScore(CmdbServiceAssess assessScore);

    /**
     *  获取故障恢复平均时长 最小指标值
     * @param assessScore
     * @return
     */
    String statisticsMinRepairTargetValue(CmdbServiceAssess assessScore);

    /**
     *  服务评估得分
     * @param assessScore
     * @return
     */
    String statisticsDeviceEvaluate(CmdbServiceAssess assessScore);

    /**
     * 保存故障处理平均时长得分
     * @param assessScore
     */
    void saveDeviceEvaluateScore(CmdbServiceAssess assessScore);
}
