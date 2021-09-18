package com.aspire.ums.cmdb.v2.assessment.mapper;

import com.aspire.ums.cmdb.assessment.payload.CmdbItDeviceCount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* 描述：
* @author
* @date 2019-06-26 10:48:12
*/
@Mapper
public interface CmdbItDeviceCountMapper {
    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbItDeviceCount> list(@Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize);

    /**
     * 获取信息数量
     * @return 返回数量
     */
    Integer listCount();

    /**
     * 获取所有实例
     * @param entity 实例信息
     * @return 返回所有实例数据
     */
    List<CmdbItDeviceCount> listByEntity(@Param("count") CmdbItDeviceCount entity);

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbItDeviceCount get(CmdbItDeviceCount entity);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(CmdbItDeviceCount entity);

    /**
     * 新增实例
     * @param entities 实例数据
     * @return
     */
    void insertByBatch(List<CmdbItDeviceCount> entities);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(CmdbItDeviceCount entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(CmdbItDeviceCount entity);
    /**
     * 删除实例
     * @param ids 实例数据
     * @return
     */
    void deleteByBatch(List<String> ids);
    /**
     * 获取分支机构状态
     * @param quarter 实例数据
     * @return
     */
    List<Map<String, Object>> getProvinceStatus(String quarter);

    /**
     * 加载机构下的厂商和设备类型列表
     * @param requestMp 包括province和quarter
     * @return
     */
    List<Map<String, Object>> getProduceAndDeviceList(Map<String,String> requestMp);
}