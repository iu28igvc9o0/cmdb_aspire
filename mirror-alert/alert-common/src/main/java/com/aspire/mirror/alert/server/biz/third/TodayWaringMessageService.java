package com.aspire.mirror.alert.server.biz.third;
import com.aspire.mirror.alert.server.vo.third.TodayWarningMessageVo;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface TodayWaringMessageService {

    List<TodayWarningMessageVo> getSummaryByIdctype(@Param(value = "param") Map<String, Object> param);

    List<TodayWarningMessageVo> getSummaryByDeviceMfrs(@Param(value = "param") Map<String, Object> param);


    List<TodayWarningMessageVo> getSummaryByDeviceTypeAndSoyrceRoomAndDeviceClass(@Param(value = "param") Map<String, Object> param);


}
