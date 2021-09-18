package com.aspire.ums.cmdb.process.payload;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 描述：
* @author
* @date 2020-04-14 09:08:55
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbProcessHandlerManager {

    /**
     * ID
     */
    private String id;
    /**
     * 处理类型
     */
    private String handlerType;
    /**
     * 处理类
     */
    private String handlerClass;
}