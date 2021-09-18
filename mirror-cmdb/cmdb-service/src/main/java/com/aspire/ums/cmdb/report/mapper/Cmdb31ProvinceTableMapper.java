package com.aspire.ums.cmdb.report.mapper;

import com.aspire.ums.cmdb.report.playload.Cmdb31ProvinceTable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* 描述：
* @author
* @date 2020-03-24 10:40:25
*/
@Mapper
public interface Cmdb31ProvinceTableMapper {
     /**
     * 根据归属获取列数据
      * type控制列返回
     * @return 返回所有实例数据
     */
    List<Cmdb31ProvinceTable> listByOwnerAndPage(@Param("resourceOwner") String resourceOwner,
                                                  @Param("showPage") String showPage);

    /**
     * 根据表id获取详情
     * @param id 实例信息
     * @return 返回实例信息的数据信息
     */
    Cmdb31ProvinceTable getById(@Param("id") String id);

    /**
     * 根据归属和表名获取表
     * @param name 实例信息
     * @return 返回实例信息的数据信息
     */
    Cmdb31ProvinceTable getByName(@Param("name") String name);
}