package com.aspire.ums.cmdb.v3.config.service.impl;

import com.aspire.ums.cmdb.common.Constants;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.aspire.ums.cmdb.v3.config.mapper.CmdbConfigMapper;
import com.aspire.ums.cmdb.v3.config.payload.CmdbConfig;
import com.aspire.ums.cmdb.v3.config.payload.CmdbConfigRequest;
import com.aspire.ums.cmdb.v3.config.service.ICmdbConfigService;
import com.aspire.ums.cmdb.v3.redis.service.IRedisService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* 描述：
* @author
* @date 2020-05-06 10:54:58
*/
@Service
public class CmdbConfigServiceImpl implements ICmdbConfigService {

    @Autowired
    private CmdbConfigMapper mapper;
    @Autowired
    private IRedisService redisService;

    @Override
    public Result<CmdbConfig> listByPage(CmdbConfigRequest req) {
        if(req.getPageNo() != null && req.getPageSize() != null) {
            req.setPageNo((req.getPageNo()-1)*req.getPageSize());
        }
        List<CmdbConfig> dataList = mapper.listWithPage(req);
        int total = mapper.listCountWithPage(req);
        return new Result<>(total,dataList);
    }

    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    public List<CmdbConfig> list() {
        return mapper.list();
    }

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回指定ID的数据信息
     */
    public CmdbConfig get(CmdbConfig entity) {
        return mapper.get(entity);
    }

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    public Map<String,Object> insert(CmdbConfig entity) {
        Map<String,Object> mp = new HashMap<>();
        try {
            entity.setId(UUIDUtil.getUUID());
            mapper.insert(entity);
            mp.put("flag",true);
            mp.put("msg","success");
        } catch (Exception e){
            e.printStackTrace();
            mp.put("flag",false);
            mp.put("msg","失败原因:" + e.getLocalizedMessage());
        }
        return mp;
    }

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    public Map<String,Object> update(CmdbConfig entity) {
        Map<String,Object> mp = new HashMap<>();
        try {
            mapper.update(entity);
            mp.put("flag",true);
            mp.put("msg","success");
        } catch (Exception e){
            e.printStackTrace();
            mp.put("flag",false);
            mp.put("msg","失败原因:" + e.getLocalizedMessage());
        }
        return mp;
    }

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    public Map<String,Object> delete(CmdbConfig entity) {
        Map<String,Object> mp = new HashMap<>();
        try {
            mapper.delete(entity);
            mp.put("flag",true);
            mp.put("msg","success");
        } catch (Exception e){
            e.printStackTrace();
            mp.put("flag",false);
            mp.put("msg","失败原因:" + e.getLocalizedMessage());
        }
        return mp;
    }

    @Override
    public CmdbConfig getConfigByCode(String configCode) {
        Object object = redisService.get(String.format(Constants.REDIS_CMDB_CONFIG_PREFIX, configCode));
        if (object == null) {
            CmdbConfig config = new CmdbConfig();
            config.setConfigCode(configCode);
            return get(config);
        }
        return (new ObjectMapper()).convertValue(object, new TypeReference<CmdbConfig>(){});
    }

    @Override
    public CmdbConfig getConfigByCode(String configCode, Object defaultValue) {
        CmdbConfig config = getConfigByCode(configCode);
        if (config == null) {
            if (defaultValue instanceof Exception) {
                throw new RuntimeException((Throwable) defaultValue);
            }
            config = new CmdbConfig();
            config.setConfigCode(configCode);
            config.setConfigValue(String.valueOf(defaultValue));
        }
        return config;
    }
}
