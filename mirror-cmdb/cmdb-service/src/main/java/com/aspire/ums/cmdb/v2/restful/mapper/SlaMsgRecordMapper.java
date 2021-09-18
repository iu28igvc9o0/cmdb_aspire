package com.aspire.ums.cmdb.v2.restful.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: AlertRestfulMapper
 * Author:   zhu.juwang
 * Date:     2019/12/10 16:40
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Mapper
public interface SlaMsgRecordMapper {
    /**
     * 根据资源池统计设备数量
     * @param params 请求参数
     */
    List<Map<String, Object>> queryMsgRecord(Map<String,Object> params);
    
    void insert(Map<String,Object> Object);
    
    List<Map<String,Object>> listNoWorkingDays();

}
