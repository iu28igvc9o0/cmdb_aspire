package com.aspire.ums.cmdb.networkCard.service.impl;
import com.aspire.ums.cmdb.networkCard.payload.CmdbInstanceNetworkCard;
import com.aspire.ums.cmdb.networkCard.service.ICmdbInstanceNetworkCardService;
import com.aspire.ums.cmdb.networkCard.mapper.CmdbInstanceNetworkCardMapper;
import com.aspire.ums.cmdb.util.UUIDUtil;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
* 描述：
* @author
* @date 2019-09-18 11:26:39
*/
@Service
public class CmdbInstanceNetworkCardServiceImpl implements ICmdbInstanceNetworkCardService {

    @Autowired
    private CmdbInstanceNetworkCardMapper mapper;

    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    public List<CmdbInstanceNetworkCard> list() {
        return mapper.list();
    }

    /**
     * 根据instanceId 获取数据信息
     * @param instanceId 实例Id
     * @return 返回instanceId的数据信息
     */
    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class, SQLException.class})
    public List<CmdbInstanceNetworkCard> listByInstanceId(String instanceId) {
        CmdbInstanceNetworkCard cmdbInstanceNetworkCard = new CmdbInstanceNetworkCard();
        cmdbInstanceNetworkCard.setInstanceId(instanceId);
        return mapper.listByEntity(cmdbInstanceNetworkCard);
    }

    /**
     * 根据名称 获取数据信息
     * @param name 名称
     * @return 返回指定ID的数据信息
     */
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class, SQLException.class})
    public CmdbInstanceNetworkCard get(String name) {
        CmdbInstanceNetworkCard cmdbInstanceNetworkCard = new CmdbInstanceNetworkCard();
        cmdbInstanceNetworkCard.setNetworkCardName(name);
        return mapper.get(cmdbInstanceNetworkCard);
    }

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class, SQLException.class})
    public void insert(CmdbInstanceNetworkCard entity) {
        entity.setId(UUIDUtil.getUUID());
        entity.setIsDelete("0");
        mapper.insert(entity);
    }

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class, SQLException.class})
    public void update(CmdbInstanceNetworkCard entity) {
        mapper.update(entity);
    }

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class, SQLException.class})
    public void delete(CmdbInstanceNetworkCard entity) {
        mapper.delete(entity);
    }

    /**
     * 逻辑删除
     * @param idList id列表
     * @return
     */
    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class, SQLException.class})
    public void deleteByLogic(List idList) {
        mapper.deleteByLogic(idList);
    }

    /**
     * 获取所有导出实例 依据instanceId
     * @param instanceId 网卡ID
     * @return 返回所有实例数据
     */
    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class, SQLException.class})
    public List<Map<String, Object>> exportList(String instanceId) {
        return mapper.exportList(instanceId);
    }
}