package com.aspire.ums.cmdb.cmic.service.impl;

import com.aspire.ums.cmdb.cmic.mapper.CmdbModuleEventHandlerClassMapper;
import com.aspire.ums.cmdb.cmic.payload.CmdbModuleEventHandlerClass;
import com.aspire.ums.cmdb.cmic.service.ICmdbModuleEventHandlerClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* 描述：
* @author
* @date 2020-05-18 18:28:11
*/
@Service
public class CmdbModuleEventHandlerClassServiceImpl implements ICmdbModuleEventHandlerClassService {

    @Autowired
    private CmdbModuleEventHandlerClassMapper mapper;

    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    public List<CmdbModuleEventHandlerClass> list() {
        return mapper.list();
    }

    @Override
    public List<CmdbModuleEventHandlerClass> listByEventType(String eventClass) {
        CmdbModuleEventHandlerClass handlerClass = new CmdbModuleEventHandlerClass();
        handlerClass.setEventClass(eventClass);
        return mapper.listByEntity(handlerClass);
    }

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回指定ID的数据信息
     */
    public CmdbModuleEventHandlerClass get(CmdbModuleEventHandlerClass entity) {
        return mapper.get(entity);
    }

    @Override
    public CmdbModuleEventHandlerClass getById(String id) {
        CmdbModuleEventHandlerClass handlerClass = new CmdbModuleEventHandlerClass();
        handlerClass.setId(id);
        return get(handlerClass);
    }

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    public void insert(CmdbModuleEventHandlerClass entity) {
        mapper.insert(entity);
    }

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    public void update(CmdbModuleEventHandlerClass entity) {
        mapper.update(entity);
    }

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    public void delete(CmdbModuleEventHandlerClass entity) {
        mapper.delete(entity);
    }
}