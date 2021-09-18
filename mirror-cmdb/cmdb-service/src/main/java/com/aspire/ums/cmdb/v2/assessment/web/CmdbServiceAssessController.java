package com.aspire.ums.cmdb.v2.assessment.web;

import com.aspire.ums.cmdb.assessment.IServiceAssessAPI;
import com.aspire.ums.cmdb.assessment.payload.CmdbServiceAssess;
import com.aspire.ums.cmdb.v2.assessment.service.ICmdbServiceAssessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 项目名称:
 * 包: com.aspire.ums.cmdb.v2.assessment.web
 * 类名称:
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/7/15 15:35
 * 版本: v1.0
 */
@RefreshScope
@RestController
@Slf4j
public class CmdbServiceAssessController implements IServiceAssessAPI {
    
    @Autowired
    private ICmdbServiceAssessService serviceAccessService;
    
    @Override
    public List<CmdbServiceAssess> queryAllData(@RequestParam(value = "device_type",required = false) String deviceType,
                                                  @RequestParam(value = "quarter") String quarter) {
        return serviceAccessService.queryScoreList(deviceType, quarter);
    }
    
    @Override
    public Map<String, Object> saveScore(@RequestBody List<CmdbServiceAssess> list) {
        return serviceAccessService.saveScore(list);
    }

    /**
     * 获取厂商评分下设备类型评分状态
     * @param quarter 实例数据
     * @return
     */
    @Override
    public List<Map<String, Object>> getScoreDeviceTypeStatus(@RequestParam("quarter") String quarter){
        return serviceAccessService.getScoreDeviceTypeStatus(quarter);
    }


}
