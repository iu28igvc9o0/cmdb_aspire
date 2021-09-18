package com.aspire.ums.cmdb.v2.assessment.service;


import com.aspire.ums.cmdb.assessment.payload.CmdbItDeviceCount;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
* 描述：
* @author
* @date 2019-06-25 16:07:55
*/
public interface ICmdbItDeviceCountService {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
     List<CmdbItDeviceCount> listByEntity(CmdbItDeviceCount deviceCount);

    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    Integer listCount(CmdbItDeviceCount deviceCount);

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbItDeviceCount get(CmdbItDeviceCount entity);

    /**
     * 新增实例
     * @param entities 实例数据
     * @return
     */
    void insert(List<CmdbItDeviceCount> entities);

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