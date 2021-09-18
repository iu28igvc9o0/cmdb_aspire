package com.aspire.mirror.alert.server.dao.scanComparision;

import com.aspire.mirror.alert.server.vo.scanComparision.AlertScanComparisionDetailVo;
import com.aspire.mirror.alert.server.vo.scanComparision.AlertScanComparisionReqVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface AlertScanComparisionDao {

    int getScanComparisionCount(AlertScanComparisionReqVo request);

    List<AlertScanComparisionDetailVo> getScanComparisionList(AlertScanComparisionReqVo request);

    void deleteScanComparisionById(List<String> request);

    List<Map<String, Object>> exportScanComparision(AlertScanComparisionReqVo request);

    
    void updateSynStatus(List<Map<String, Object>> request);

    List<Map<String, String>> getAlerts();

    AlertScanComparisionDetailVo getAlertScanComparisionDetailByIpAndPool(@Param("deviceIp") String deviceIp,
                                                                          @Param("idcType") String idcType);
    

    void insertScanComparision(List<AlertScanComparisionDetailVo> request);

    void updateById(@Param("id") String id,
                    @Param("curMoniTime") String curMoniTime);
}
