package com.aspire.ums.cmdb.maintenance.service;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.maintenance.payload.Concat;
import com.aspire.ums.cmdb.maintenance.payload.ProduceInfoRequest;
import com.aspire.ums.cmdb.maintenance.payload.ProduceInfoResq;

import java.util.List;
import java.util.Map;

public interface IProduceInfoService {

    /**
     * 新增厂商信息
     * @param produceInfoResq
     * @return
     */
    Map<String, Object> insert(ProduceInfoResq produceInfoResq);

    /**
     * 更新厂商信息
     * @param produceInfoResq
     * @return
     */
    Map<String, Object> update(ProduceInfoResq produceInfoResq);

    /**
     * 查询厂商信息接口
     * @return
     */
    List<ProduceInfoResq> queryProduceInfoList();
    /**
     * 逻辑删除厂商信息
     * @param produceId
     * @return
     */
    void deleteProduce(String produceId);

    /**
     * 分页查询
     * @param produceInfoRequest
     * @return
     */
    Result<ProduceInfoResq> selectProduceByPage(ProduceInfoRequest produceInfoRequest);

    /**
     *  根据id查询厂商详情
     * @return 模型列表
     */
    ProduceInfoResq getProduceById(String id);

//    /**
//     * 根据厂商ID查询联系人
//     * @param produceId
//     * @return
//     */
//    List<Concat> getConcatsByProduceId(String produceId);

    /**
     * 查询厂商ID
     * @param produce
     * @param type
     * @return
     */
    String selectId(String produce, String type);

    /**
     * 查询导出数据
     * @param sendRequest
     * @return
     */
    JSONObject queryExportData(Map<String,Object> sendRequest);


    /**
     * 更新联系人
     * @param concat
     */
    void updateConcat(Concat concat);


//    /**
//     * 查询厂商联系人信息接口
//     * @return
//     */
//    List<Concat> queryConcatByProduceId(String produceId);

    /**
     * 查询联系人是否存在
     * @param produceId
     * @param name
     * @return
     */
    int selectConcat(String produceId, String name, String phone, String typeId);

    /**
     * 根据厂商ID和类型查询联系人信息
     * @param produceId
     * @return
     */
    List<Concat> queryConcat(String produceId, String personType);

    /**
     * 新增联系人
     * @param concat
     */
    void addConcat(Concat concat);
    /**
     * 删除联系人
     * @param id
     */
    void deleteConcat(String id);
}
