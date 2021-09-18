package com.aspire.mirror.template.server.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aspire.mirror.template.api.dto.model.ItemExt;
import com.aspire.mirror.template.api.dto.model.MonitorItemPrototype;
import com.aspire.mirror.template.api.dto.model.MonitorItemPrototype.MonitorItemPrototypeQueryModel;


@Mapper
public interface MonitorItemPrototypeDao {
    
    void insertItemPrototype(MonitorItemPrototype prototype);

    void updateItemPrototype(MonitorItemPrototype prototype);

    void deleteItemPrototypeById(@Param("id") Long id);
    
    void batchDeleteItemPrototypeById(List<Long> idList);

    MonitorItemPrototype queryItemPrototypeById(@Param("id") Long id);
    
    List<MonitorItemPrototype> queryItemPrototypeList(MonitorItemPrototypeQueryModel params);
    
    Integer queryTotalItemPrototypeListCount(MonitorItemPrototypeQueryModel params);
    
    ItemExt getItemExtByItemId(@Param("itemId") String itemId);
}
