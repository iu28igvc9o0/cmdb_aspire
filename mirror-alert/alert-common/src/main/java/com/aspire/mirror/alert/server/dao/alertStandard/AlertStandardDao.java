package com.aspire.mirror.alert.server.dao.alertStandard;

import com.aspire.mirror.alert.server.dao.alertStandard.po.AlertStandard;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @projectName: AlertStandardDao
 * @description: 接口
 * @author: luowenbo
 * @create: 2020-06-10 14:32
 **/
@Mapper
public interface AlertStandardDao {
    /*
     * 批量新增
     * */
    void batchInsert(List<AlertStandard> asList);

    /*
    * 新增
    * */
    void insert(AlertStandard as);

    /*
     * 修改
     * */
    void update(AlertStandard as);

    /*
    * 批量修改状态
    * */
    void updateStatus(@Param("ids") String[] ids);

    /*
     * 删除
     * */
    void deleteByIds(@Param("ids") String[] ids);

    /**
     * 查询分页数量
     * @param
     */
    int getAlertStandardCount(Map<String,Object> mp);


    /**
     * 查询分页数据
     * @param
     */
    List<AlertStandard> getAlertStandardByPage(Map<String,Object> mp);

    /**
     * 查询数据依据Id
     * @param
     */
    AlertStandard getAlertStandardById(@Param("id") String id);

    /*
    *  查询状态为启用的数据
    * */
    List<AlertStandard> getAlertStandardWithEnable();

    /*
    *  同步历史数据
    * */
    void updateAlertHistory(@Param("as") AlertStandard as,
                            @Param("startTime") String startTime,
                            @Param("endTime") String endTime);

    List<Map<String,Object>> exportAll(Map<String,Object> mp);
}
