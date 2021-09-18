package com.aspire.ums.cmdb.sync.service.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aspire.ums.cmdb.dict.payload.Dict;
import com.aspire.ums.cmdb.dict.service.ConfigDictService;

/**
 * 字典表同步.
 *
 * @author jiangxuwen
 * @date 2020/5/13 20:44
 */
@Service("configDictProducerService")
public class ConfigDictProducerServiceImpl extends AbstractKafkaProducerService<Dict> {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ConfigDictService configDictService;

    @Override
    public Dict getObjectData(String moduleId, String objectId) {
        return configDictService.getDictById(objectId);
    }
}
