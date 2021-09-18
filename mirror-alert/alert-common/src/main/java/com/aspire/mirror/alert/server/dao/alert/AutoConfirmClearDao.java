package com.aspire.mirror.alert.server.dao.alert;

import com.aspire.mirror.alert.server.vo.alert.AutoConfirmClearVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface AutoConfirmClearDao {

    void insert(AutoConfirmClearVo request);

    AutoConfirmClearVo getAutoConfirmClearId(@Param("deviceIp") String deviceIp,
                                             @Param("idcType") String idcType,
                                             @Param("bizSys") String bizSys,
                                             @Param("alertLevel") String alertLevel,
                                             @Param("source") String source,
                                             @Param("itemId") String itemId,
                                             @Param("autoType") Integer autoType,
                                             @Param("curTime") String curTime);

    List<AutoConfirmClearVo> getAutoConfirmClearDTOs(Map<String,Object> map);


    void deleteRule(@Param("curTime") String curTime);

}
