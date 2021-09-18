package com.aspire.ums.cmdb.v2.index.mapper;

import com.aspire.ums.cmdb.index.payload.ItCloudScreenRequest;
import com.aspire.ums.cmdb.index.payload.ScreenCheckScore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @ClassName ItCloudScreenCheckScoreMapper
 * @Description 租户扣除分数接口——原子层
 * @Author luowenbo
 * @Date 2020/4/9 13:51
 * @Version 1.0
 */
@Mapper
public interface ItCloudScreenCheckScoreMapper {

    /*
    *  新增实体
    * */
    void insert(ScreenCheckScore scs);

    /*
     *  批量新增实体
     * */
    void batchInsert(List<ScreenCheckScore> list);

    /*
     *  更新实体
     * */
    void delete(ItCloudScreenRequest req);

    /*
    *  获取单个实体，根据业务系统和设备类型
    * */
    ScreenCheckScore getOne(ItCloudScreenRequest req);

    /*
    *  获取某个租户的扣分项，以物理机和虚拟机合并的CPU均峰值为指标
    * */
    List<Map<String,Object>> getCheckScoreByDepDetail(ItCloudScreenRequest req);

    /*
    *  当均峰值数据全部导入后，通过计算，得出租户下每个业务系统的CPU均峰值
    * */
    List<Map<String,Object>> getCpuMaxByCal(ItCloudScreenRequest req);

    /*
    *  获取部门的考核起始时间
    * */
    Map<String,Object> getAssessStartMonth(@Param("department1") String department1,
                                           @Param("department2") String department2);
}
