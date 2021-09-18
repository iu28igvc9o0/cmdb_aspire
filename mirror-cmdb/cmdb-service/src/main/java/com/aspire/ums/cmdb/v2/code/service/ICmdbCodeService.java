package com.aspire.ums.cmdb.v2.code.service;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.code.payload.CmdbCode;
import com.aspire.ums.cmdb.code.payload.CmdbCodeGroup;
import com.aspire.ums.cmdb.code.payload.CmdbCodeQuery;
import com.aspire.ums.cmdb.code.payload.CmdbSimpleCode;
import com.aspire.ums.cmdb.common.Result;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
* 描述：
* @author
* @date 2019-05-13 18:39:38
*/
public interface ICmdbCodeService {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    Result<CmdbCode> list(CmdbCodeQuery query);

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbCode get(CmdbCode entity);

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbSimpleCode getByEntity(CmdbCode entity);
    /**
     * 根据主键ID 获取数据信息
     * @param codeId 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbSimpleCode getSimpleCodeById(String codeId);

    /**
     * 根据主键IDs数组 获取数据信息
     * @param ids 根据ids获取实例信息
     * @return 返回实例信息的数据信息
     */
    List<CmdbCode> listByIds(List<String> ids);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    Map<String, String> insert(CmdbCode entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    Map<String, String> update(CmdbCode entity);

    /**
     * 删除实例
     * @param cmdbCode 实例数据
     * @return
     */
    Map<String, String> delete(CmdbCode cmdbCode);

    /**
     * 按照分组展示码表列表
     *
     */
    List<CmdbCodeGroup> queryCodeListFormatGroup(String catalogId);

    /**
     * 根据实体类信息查询列表
     * @param cmdbCode 实体类
     * @return
     */
    List<CmdbCode> listByEntity(CmdbCode cmdbCode);

    /**
     * 根据实体类信息查询列表
     * @param cmdbCode 实体类
     * @return
     */
    List<CmdbSimpleCode> simpleCodeListByEntity(CmdbCode cmdbCode);

    /**
     * 根据模型ID 查询模型下所有的码表字段
     * @param moduleId 模型ID
     * @return
     */
    List<CmdbCode> getCodeListByModuleId(String moduleId);


    /**
     * 根据模型ID 查询模型下所有的码表字段
     * @param filedCode 字段编码
     * @param moduleId 模型ID
     * @return
     */
    CmdbSimpleCode getSimpleCodeByCodeAndModuleId(String moduleId, String filedCode);
    /**
     * 根据模型ID 查询模型下所有的码表字段
     * @param moduleId 模型ID
     * @return
     */
    List<CmdbSimpleCode> getSimpleCodeListByModuleId(String moduleId);

    /**
     * 根据模型ID查询码表列表(包括引用模型中的字段)
     * @param moduleId 模型ID
     * @return
     */
    List<CmdbCode> getSelfCodeListByModuleId(String moduleId);

    /**
     * 改变码表字段大小
     * @param filedCode
     * @param changeLength
     * @return
     */
    String changeCodeLength(String filedCode, Integer changeLength);

    /**
     * 获取码表编码及名称集合
     * @return
     */
    List<Map<String, String>> getDistinctCodeList();

    /**
     * 验证码表填写的值是否正确
     * @param params 请求参数 {"codeId": "value"} 如: {"c12345": "zhangsan"}
     * @return 返回值 {"codeId": {"flag": "success/error", "msg": "xxxx"}}
     */
    Map<String, Map<String, String>> validCodeValue(Map<String, Object> params);

    /**
     * 验证码表填写的值是否正确
     * @param params 请求参数 {"codeId": "value"} 如: {"c12345": "zhangsan"}
     * @return 返回值 {
     *      "codeId":{
     *          "flag": "success/error",
     *          "msg": {
     *              "status": "pass/reject"
     *              "msg": "自动审核通过/自动审核驳回",
     * 	        "   reason": "具体原因"
     * 	        }}}
     */
    Map<String, Map<String, Object>> approveCodeValue(Map<String, Object> params);

    /**
     * 验证码表 编码和模型分组 的唯一性
     * @param filedCode 码表编码
     * @param moduleCatalogId 模型分组ID
     * @return
     */
    JSONObject validateCmdbCodeUnique(String filedCode, String moduleCatalogId);

//    /**
//     * 获取配置项的数据源数据
//     * @param params 入参
//     * @return
//     */
//    List<Map<String, Object>> getCodeDataSource(Map<String, Object> params);

    /**
     * 根据codeId查询父级级联code
     * @param codeIds 码表id
     * @return
     */
    LinkedList<CmdbCode> getCasParentCodes(List<String> codeIds);

    List<Map<String,String>> getCodeIdByNameAndModuleId(Map<String,Object> param);

    /**
     * 获取配置项的数据源数据
     * @param codeId 码表ID
     * @return
     */
    <T> List<Map<String, T>> getRefCodeData(String codeId);
}
