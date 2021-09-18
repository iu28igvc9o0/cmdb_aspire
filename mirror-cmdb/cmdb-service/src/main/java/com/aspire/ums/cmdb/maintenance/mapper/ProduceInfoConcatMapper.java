package com.aspire.ums.cmdb.maintenance.mapper;

import com.aspire.ums.cmdb.dict.payload.ConfigDict;
import com.aspire.ums.cmdb.maintenance.payload.Concat;
import com.aspire.ums.cmdb.maintenance.payload.MaintenProConcatRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName: ProduceInfoConcatMapper
 * @Dscription:
 * @Author:
 * @Version: V1.0
 */
@Mapper
public interface ProduceInfoConcatMapper {


    /**
     * 批量新增
     * @param produceId
     * @param list
     */
    void addBatch(@Param("produceId") String produceId, @Param("list") List<Concat> list);

    /**
     * 新增
     * @param concat
     */
    void add(Concat concat);

    /**
     * 更新
     * @param concat
     */
    void update(Concat concat);

    /**
     * 根据厂商ID删除
     * @param produceId
     */
    void delByProduceId(@Param("produceId") String produceId);

    /**
     * 根据厂商IDs删除
     * @param produceIds
     */
    void batchDelByProduceId(List<String> produceIds);


    /**
     * 根据id删除联系人
     * @param id
     */
    void delConcatById(@Param("id") String id);

    /**
     * 根据厂商ID查询联系人信息
     * @param produceId
     * @return
     */
    List<Concat> queryConcat(@Param("produceId") String produceId,
                             @Param("personType") String type,
                             @Param("name") String name,
                             @Param("phone") String phone,
                             @Param("typeId") String typeId);


    /**
     * 查询联系人是否存在 1表示存在 0表示不存在
     * @param produceId
     * @param name
     * @return
     */
    int selectConcat(@Param("produceId") String produceId,
                     @Param("name") String name,
                     @Param("phone") String phone,
                     @Param("typeId") String id);

    /**
     * 新增关系
     * @param relations
     * @return
     */
    void insertRelationByBatch(List<MaintenProConcatRelation> relations);

    /**
     * 删除关系
     * @param concatId
     * @return
     */
    void delRelByConcatId(String concatId);

    /**
     * 删除关系根据厂商
     * @param produceIds
     * @return
     */
    void batchDelRelByProduceId(List<String> produceIds);

    /*
    *   查询服务供应商
    * */
    List<Concat> queryServiceByProduceId(String produceId);

    /*
     *   查询合同供应商
     * */
    List<Concat> queryContractByProduceId(String produceId);
}
