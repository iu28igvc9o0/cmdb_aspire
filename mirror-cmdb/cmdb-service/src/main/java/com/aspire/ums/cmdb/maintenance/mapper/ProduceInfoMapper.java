package com.aspire.ums.cmdb.maintenance.mapper;

import com.aspire.ums.cmdb.maintenance.payload.ProduceInfoRequest;
import com.aspire.ums.cmdb.maintenance.payload.ProduceInfoResq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface ProduceInfoMapper {

    int getProduceInfoCount(@RequestBody ProduceInfoRequest request);

    List<ProduceInfoResq> getProduceInfoByPage(@RequestBody ProduceInfoRequest request);

    int selectProduce(@Param("id") String id, @Param("produce") String produce, @Param("type") String type);

    String selectId(@Param("produce") String produce, @Param("type") String type);

    void add(@RequestBody ProduceInfoResq requester);

    void update(@RequestBody ProduceInfoResq requester);

    void delete(List<String> ids);

    List<LinkedHashMap> queryExportData(Map<String, Object> queryMap);

    List<ProduceInfoResq> queryProduceInfoList();

    /**
     * 根据供应商ID, 获取产商信息
     * @param id
     * @return
     */
    ProduceInfoResq getProduceInfoById(@Param("id") String id);

    /**
     * 根据供应商ID, 获取合同供应商产商信息
     */
    ProduceInfoResq getContractProduceInfoById(String id);

    /**
     * 根据供应商ID, 获取服务供应商产商信息
     */
    ProduceInfoResq getServiceProduceInfoById(String id);
}
