package com.migu.tsg.microservice.atomicservice.composite.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.migu.tsg.microservice.atomicservice.composite.common.sql.ConditionPiece;
import com.migu.tsg.microservice.atomicservice.composite.dao.po.CompositeResource;


/**
* 数据库表Resources_resource的DAO操作
* Project Name:composite-service
* File Name:CompositeResourceDao.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.dao
* ClassName: CompositeResourceDao <br/>
* date: 2017年9月2日 上午12:02:26 <br/>
* 数据库表Resources_resource的DAO操作
* @author pengguihua
* @version 
* @since JDK 1.6
*/
@Mapper
public interface CompositeResourceDao {

    /**
     * 查找Composite层记录的资源列表 <br/>
     *
     * 作者： pengguihua
     * 
     * @param resource_type
     * @param searchName
     * @return
     */
    List<CompositeResource> queryResourceList(CompositeResource param);
    
    
    /**
    * queryResourceCount: 根据查询条件，获取查询结果数量. <br/>
    *
    * 作者： pengguihua
    * @param param
    * @return
    */
    int queryResourceCount(CompositeResource param);

    /**
     * 根据资源类型和资源名称，查找资源记录.<br/>
     *
     * 作者： pengguihua
     * 
     * @param resType
     * @param resName
     * @return
     */
    CompositeResource queryResourceByName(@Param("namespace") String orgAccount,
            @Param("resType") String resType, @Param("name") String resName);
    
    
    
    /**
    * 根据资源类型和资源名称，查找资源记录.<br/>
    *
    * 作者： pengguihua
    * @param orgAccount
    * @param resType
    * @param resNameList
    * @return
    */
    List<CompositeResource> queryResourcesByNameList(@Param("namespace") String orgAccount,
                                                     @Param("resType") String resType, @Param("nameList") List<String> resNameList);

    List<CompositeResource> queryResourcesByUuidList(@Param("namespace") String orgAccount,
                                                     @Param("resType") String resType, @Param("uuidList") List<String> uuidList);
    /**
     * 根据资源类型和资源uuid，查找资源记录.<br/>
     *
     * 作者： pengguihua
     * 
     * @param orgAccount
     * @param resType
     * @param uuid
     * @return
     */
    CompositeResource queryResourceByUuid(@Param("namespace") String orgAccount,
                                          @Param("resType") String resType, @Param("uuid") String uuid);

    /**
     * 根据资源类型和资源uuid，查找资源记录.<br/>
     *
     * 作者： zhangriyue
     * 
     * @param orgAccount
     * @param resType
     * @param regionId
     * @return 查询对象
     */

    CompositeResource queryResourceByRegionId(@Param("namespace") String orgAccount,
                                              @Param("resType") String resType, @Param("regionId") String regionId);

    /**
     * 根据拼装的条件查询resource列表. 参考WhereBuilder.
     * 
     * @param conds
     * @param orderBy
     * @return
     */
    List<CompositeResource> queryResourcesByConds(@Param("conds") List<ConditionPiece> conds,
                                                  @Param("orderBy") String orderBy);

    /**
     * 根据uuid查询
     * 
     * @param uuid
     * @return
     */
    CompositeResource queryByUuid(@Param("uuid") String uuid);
    
        CompositeResource queryByUuidAndNamespace(
                @Param("uuid") String uuid, @Param("namespace") String namespace);

    /**
     * 插入CompositeResource记录, 支持单条或批量.<br/>
     *
     * 作者： pengguihua
     * 
     * @param resourceArr
     */
    void insertCompositeResource(CompositeResource... resourceArr);

    /**
     * 根据关键字删除Composite资源.<br/>
     *
     * 作者： pengguihua
     * 
     * @param orgAccount
     * @param resType
     * @param uuid
     * @param name
     */
    void removeCompositeResource(@Param("namespace") String orgAccount, @Param("resType") String resType,
                                 @Param("uuid") String uuid);
    
    
    Integer queryInobject(@Param("object") Object object, @Param("uuid") String uuid);

    /**
     * 根据uuidList删除Composite资源.<br/>
     *
     * 作者： longfeng
     * 
     * @param orgAccount
     * @param resType
     * @param uuidList
     */
    void removeCompositeList(@Param("namespace") String orgAccount, @Param("resType") String resType,
                             @Param("uuidList") List<String> uuidList);
    
    /**
     * 根据uuidList删除Composite资源.<br/>
     *
     * 作者： longfeng
     * 
     * @param orgAccount
     * @param resType
     * @param uuidList
     */
    void removeCompositeByName(
            @Param("namespace") String orgAccount, @Param("resTypeList") List<String> resTypeList,
            @Param("name") String name);

         
    
     /**
      * 根据资源类型，资源名称查找资源记录.<br/>
      *
      * 作者： longfeng
      * @param name
      * @param resType
      * @return
      */
     List<CompositeResource> queryResourcesByNameAndType(@Param("name") String name,
                                                         @Param("resType") String resType);
      

      List<CompositeResource> queryResourceByNameTypeList(@Param("namespace") String orgAccount
              , @Param("resTypeList") List<String> resTypeList,
                                                          @Param("name") String name);
      
      List<CompositeResource> queryResourceByNameType(@Param("namespace") String orgAccount
              , @Param("resType") String resType, @Param("name") String name);
			  
	  List<CompositeResource> queryResourceByStartNameType(
              @Param("namespace") String orgAccount, @Param("resType") String resType,
              @Param("name") String name);
      
      List<CompositeResource> queryResourceOnlyByUuid(@Param("uuid") String uuid);
      /**
       * 
       * @Description: 根据uuidlist查询资源
       *
       * @author longfeng
       * @param uuidlist
       * @return
       */
      List<CompositeResource> queryResourceByUuidlist(@Param("uuidlist") List<String> uuidlist);
      
      /**
       * 
       * queryProjectResourceList:查询项目下的绑定资源，不传项目则查询共有资源. <br/>
       * 作者： baiwp
       * @param param
       * @return
       */
      List<CompositeResource> queryProjectResourceList(CompositeResource param);
      
      /**
       * 
       * @Description: 更新资源的regionId
       *
       * @author longfeng
       * @param param
       */
      void updateResourceRegionid(CompositeResource param);
      
      List<CompositeResource> queryResourcesByKnamespaceUuidList(@Param("namespace") String orgAccount,
                                                                 @Param("resType") String resType, @Param("name") String name,
                                                                 @Param("uuidList") List<String> knamespaceUuidList);
      
      /**
       * @Description: 根据KnamespaceUuids模糊查询获取应用资源列表
       * @author weishuai 
       * @param param resType name uuidList
       */
      List<CompositeResource> queryResourcesByKuuidslikename(
              @Param("namespace") String orgAccount,
              @Param("resType") String resType,
              @Param("name") String name,
              @Param("uuidList") List<String> knamespaceUuidList);
      
      /**
       * 
      * queryResourceCountBySpaceName:判断有没有记录引用了spaceName所对应的资源空间. <br/>
      *
      * 作者： zhangqing
      * @param orgAccount
      * @param spaceName
      * @return
       */
      int queryResourceCountBySpaceName(@Param("namespace") String orgAccount,
                                        @Param("space_name") String spaceName);

    /**
     * 根据id修改资源名
     * @param id
     * @param name
     * @return
     */
    int updateResourceNameById(@Param("id") int id, @Param("name") String name);
}
