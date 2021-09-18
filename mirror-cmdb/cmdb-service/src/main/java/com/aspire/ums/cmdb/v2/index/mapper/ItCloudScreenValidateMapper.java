package com.aspire.ums.cmdb.v2.index.mapper;

import com.aspire.ums.cmdb.index.payload.ItCloudScreenValidateRequest;
import com.aspire.ums.cmdb.index.payload.ScreenValidate;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @ClassName ItCloudScreenValidateMapper
 * @Description 数据验证数据层
 * @Author luowenbo
 * @Date 2020/5/6 14:18
 * @Version 1.0
 */
@Mapper
public interface ItCloudScreenValidateMapper {
    /*
    *  新增
    * */
    void insert(ScreenValidate obj);

    /*
    *  查询，按照新增时间列出前5条
    * */
    List<ScreenValidate> listAll();

    /*
    *  验证数据是否已经导入
    * */
    int countListData(ItCloudScreenValidateRequest req);

    /*
     *  验证数据是否完整(均值和均峰值的CPU、内存要同时存在)
     * */
    List<Map<String,Object>> validateDataIsComplete(ItCloudScreenValidateRequest req);

    /*
     *  验证数据是否准确(每天的均峰值都要比均值大)
     * */
    List<Map<String,Object>> validateDataIsCorrect(ItCloudScreenValidateRequest req);
}
