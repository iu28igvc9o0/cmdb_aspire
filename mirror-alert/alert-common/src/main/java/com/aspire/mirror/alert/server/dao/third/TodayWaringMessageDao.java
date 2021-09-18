package com.aspire.mirror.alert.server.dao.third;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

@Mapper
public interface TodayWaringMessageDao {
    /**
     * @param //统计出信息港资源池和3050机房待确认告警和已确认告警的信息
     * @return
     */

    List<Map<String, Object>> getSummaryByIdctype(@Param(value = "param") Map<String, Object> param);

    /**
     *
     *
     * @param //统计出信息港资源池和3050机房根据设备品牌进行告警信息的统计
     * @return
     */

    List<Map<String, Object>> getSummaryByDeviceMfrs(@Param(value = "param") Map<String, Object> param);

    /**
     * @param //统计出信息港资源池和3050机房根据设备名称和设备类型进行告警信息的统计
     * @return
     */
    List<Map<String, Object>> getSummaryByDeviceTypeAndSoyrceRoomAndDeviceClass(@Param(value = "param") Map<String, Object> param);


}
