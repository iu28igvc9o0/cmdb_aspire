package com.aspire.ums.cmdb.v2.index.serivce;

import com.aspire.ums.cmdb.index.payload.ItCloudScreenRequest;
import com.aspire.ums.cmdb.index.payload.ScreenCheckScore;

import java.util.List;
import java.util.Map;

/**
 * @ClassName ItCloudScreenCheckScoreService
 * @Description TODO
 * @Author luowenbo
 * @Date 2020/4/9 14:32
 * @Version 1.0
 */
public interface ItCloudScreenCheckScoreService {
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
     *  获取租户扣分项列表
     * */
    Map<String, Object> getCheckScoreList(ItCloudScreenRequest req);

    /*
     *  当均峰值数据全部导入后，通过计算，得出租户下每个业务系统的CPU均峰值
     * */
    List<Map<String,Object>> getCpuMaxByCal(ItCloudScreenRequest req);
}
