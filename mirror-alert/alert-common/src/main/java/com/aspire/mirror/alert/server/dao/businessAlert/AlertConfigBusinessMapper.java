package com.aspire.mirror.alert.server.dao.businessAlert;

import com.aspire.mirror.alert.server.dao.businessAlert.po.AlertConfigBusiness;
import com.aspire.mirror.alert.server.vo.businessAlert.AlertConfigBusinessVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 描述：
 *
 * @author
 * @date 2019-08-14 19:35:33
 */
@Mapper
public interface AlertConfigBusinessMapper {
    /**
     * 获取所有实例
     *
     * @return 返回所有实例数据
     */
    List<AlertConfigBusiness> list();

    /**
     * 获取所有生效的屏蔽规则实例
     *
     * @return 返回所有实例数据
     */
    List<AlertConfigBusiness> listEffective();
    /**
     * 获取所有需要自动解除的实例
     *
     * @return 返回所有实例数据
     */
    List<String> listAutoRelease();

    /**
     * 获取所有实例
     *
     * @param entity 实例信息
     * @return 返回所有实例数据
     */
    List<AlertConfigBusiness> listByEntity(AlertConfigBusiness entity);

    /**
     * 根据主键ID 获取数据信息
     *
     * @param id 实例信息
     * @return 返回实例信息的数据信息
     */
    AlertConfigBusiness get(@Param("id") String id);

    /**
     * 新增实例
     *
     * @param entity 实例数据
     * @return
     */
    void insert(AlertConfigBusiness entity);

    /**
     * 修改实例
     *
     * @param entity 实例数据
     * @return
     */
    void update(AlertConfigBusiness entity);

    /**
     * 删除实例
     *
     * @param ids 实例数据
     * @return
     */
    void delete(@Param("ids") String... ids);

    /**
     * 启动/停用屏蔽规则
     *
     * @param status
     * @param ids
     */
    void status(@Param("status") String status, @Param("ids") String... ids);

    /**
     * 分页查询
     *
     * @param entity
     * @return
     */
    List<AlertConfigBusiness> findPageWithResult(AlertConfigBusinessVo entity);

    /**
     * 分页查询计数
     *
     * @param entity
     * @return
     */
    Integer findPageWithCount(AlertConfigBusinessVo entity);
}