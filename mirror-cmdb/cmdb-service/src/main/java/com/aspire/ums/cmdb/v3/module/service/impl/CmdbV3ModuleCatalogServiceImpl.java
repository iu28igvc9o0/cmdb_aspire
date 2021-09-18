package com.aspire.ums.cmdb.v3.module.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.common.Constants;
import com.aspire.ums.cmdb.module.payload.Module;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.aspire.ums.cmdb.v2.module.service.ModuleService;
import com.aspire.ums.cmdb.v3.module.mapper.CmdbV3ModuleCatalogMapper;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCatalog;
import com.aspire.ums.cmdb.v3.module.service.ICmdbV3ModuleCatalogService;
import com.aspire.ums.cmdb.v3.redis.service.IRedisService;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

/**
* 描述：
* @author
* @date 2020-01-09 14:33:20
*/
@Service
@Transactional(rollbackFor = {Exception.class, RuntimeException.class})
public class CmdbV3ModuleCatalogServiceImpl implements ICmdbV3ModuleCatalogService {

    @Autowired
    private CmdbV3ModuleCatalogMapper mapper;
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private IRedisService redisService;

    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    public List<CmdbV3ModuleCatalog> list() {
        return mapper.list();
    }

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回指定ID的数据信息
     */
    public CmdbV3ModuleCatalog get(CmdbV3ModuleCatalog entity) {
        return mapper.get(entity);
    }

    @Override
    public CmdbV3ModuleCatalog getById(String catalogId) {
        return mapper.getById(catalogId);
    }

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    public JSONObject insert(CmdbV3ModuleCatalog entity) {
        JSONObject returnJson = new JSONObject();
        CmdbV3ModuleCatalog queryCode = new CmdbV3ModuleCatalog();
        queryCode.setParentCatalogId(entity.getParentCatalogId());
        queryCode.setCatalogCode(entity.getCatalogCode());
        CmdbV3ModuleCatalog catalog = mapper.get(queryCode);
        if (catalog != null) {
            returnJson.put("flag", "error");
            returnJson.put("msg", "模型分组编码已经存在.");
            return returnJson;
        }
        CmdbV3ModuleCatalog queryName = new CmdbV3ModuleCatalog();
        queryName.setParentCatalogId(entity.getParentCatalogId());
        queryName.setCatalogName(entity.getCatalogName());
        catalog = mapper.get(queryName);
        if (catalog != null) {
            returnJson.put("flag", "error");
            returnJson.put("msg", "模型分组名称已经存在.");
            return returnJson;
        }
        entity.setId(UUIDUtil.getUUID());
        entity.setIsDelete(0);
        entity.setSortIndex(mapper.selectMaxIndex() + 1);
        mapper.insert(entity);
        this.redisService.syncRefresh(Constants.REDIS_TYPE_CATALOG, entity.getId());
        returnJson.put("flag", "success");
        return returnJson;
    }

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    public JSONObject update(CmdbV3ModuleCatalog entity) {
        JSONObject returnJson = new JSONObject();
        CmdbV3ModuleCatalog queryName = new CmdbV3ModuleCatalog();
        String parentCatalogId = entity.getParentCatalogId();
        if (StringUtils.isEmpty(entity.getParentCatalogId())) {
            CmdbV3ModuleCatalog selfEntity = getById(entity.getId());
            parentCatalogId = selfEntity.getParentCatalogId();
        }
        queryName.setParentCatalogId(parentCatalogId);
        queryName.setCatalogName(entity.getCatalogName());
        CmdbV3ModuleCatalog catalog = mapper.get(queryName);
        if (catalog != null && !catalog.getId().equals(entity.getId())) {
            returnJson.put("flag", "error");
            returnJson.put("msg", "模型分组名称已经存在.");
            return returnJson;
        }
        mapper.update(entity);
        returnJson.put("flag", "success");
        this.redisService.syncRefresh(Constants.REDIS_TYPE_CATALOG, entity.getId());
        return returnJson;
    }

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    public JSONObject delete(CmdbV3ModuleCatalog entity) {
        JSONObject returnJson = new JSONObject();
        try {
            CmdbV3ModuleCatalog queryCatalog = new CmdbV3ModuleCatalog();
            queryCatalog.setParentCatalogId(entity.getId());
            List<CmdbV3ModuleCatalog> catalogList = mapper.listByEntity(queryCatalog);
            if (catalogList != null && catalogList.size() > 0) {
                returnJson.put("msg", "模型分组存在子模型分组, 无法删除.");
                return returnJson;
            }
            Module module = moduleService.getModuleDetailByCatalogId(entity.getId());
            if (module != null) {
                returnJson.put("msg", "模型分组被模型[" + module.getName() + "]使用, 无法删除.");
                return returnJson;
            }
            List<Module> moduleList = moduleService.getChildByCatalogId(entity.getId());
            if (moduleList != null && moduleList.size() > 0) {
                StringBuilder moduleBuilder = new StringBuilder();
                for (Module m : moduleList) {
                    if (StringUtils.isNotEmpty(moduleBuilder)) {
                        moduleBuilder.append("、");
                    }
                    moduleBuilder.append(m.getName());
                }
                returnJson.put("msg", "模型分组被模型[" + moduleBuilder.toString() + "]使用, 无法删除., 无法删除.");
                return returnJson;
            }
            mapper.delete(entity);
            this.redisService.syncRefresh(Constants.REDIS_TYPE_CATALOG, entity.getId());
            returnJson.put("flag", "success");
        } catch (Exception e) {
            returnJson.put("flag", "error");
            returnJson.put("msg", "删除模型分组失败." + e.getMessage());
        }
        return returnJson;
    }

    @Override
    public List<CmdbV3ModuleCatalog> getFirstLevel() {
        CmdbV3ModuleCatalog queryParams = new CmdbV3ModuleCatalog();
        queryParams.setParentCatalogId("0");
        return mapper.listByEntity(queryParams);
    }

    @Override
    public JSONObject getAllLevelTree(String catalogId) {
        JSONObject rootJson = new JSONObject();
        if (StringUtils.isEmpty(catalogId)) {
            rootJson.put("id", "0");
            rootJson.put("catalogCode", Constants.CMDB_MODULE_CATALOG_PREFIX);
            rootJson.put("catalogName", "全部");
            rootJson.put("hasModule", "1");
            rootJson.put("child", new JSONArray());
            catalogId = "0";
        } else {
            CmdbV3ModuleCatalog queryParams = new CmdbV3ModuleCatalog();
            queryParams.setId(catalogId);
            CmdbV3ModuleCatalog catalog = get(queryParams);
            if (catalog == null) {
                return rootJson;
            }
            List<Module> moduleList = moduleService.getModuleTree(catalog.getId(), null);
            Integer hasModule = (moduleList == null || moduleList.size() == 0) ? 0 : 1;
            rootJson.put("id", catalog.getId());
            rootJson.put("catalogCode", catalog.getCatalogCode());
            rootJson.put("catalogName", catalog.getCatalogName());
            rootJson.put("hasModule", hasModule);
            rootJson.put("child", new JSONArray());
        }
        this.loopLevel(rootJson, catalogId);
        return rootJson;
    }

    @Override
    public void updateSort(String catalogId, String sortType) {
        CmdbV3ModuleCatalog sortEntity = null;
        // 升序
        if (sortType.toLowerCase(Locale.ENGLISH).equals("up")) {
            sortEntity = mapper.getSortUpEntity(catalogId);
        }
        // 降序
        if (sortType.toLowerCase(Locale.ENGLISH).equals("down")) {
            sortEntity = mapper.getSortDownEntity(catalogId);
        }
        CmdbV3ModuleCatalog selfEntity = new CmdbV3ModuleCatalog();
        selfEntity.setId(catalogId);
        CmdbV3ModuleCatalog entity = mapper.get(selfEntity);
        if (sortEntity != null && entity != null) {
            Integer sortIndex = sortEntity.getSortIndex();
            Integer selfSortIndex = entity.getSortIndex();
            sortEntity.setSortIndex(selfSortIndex);
            mapper.update(sortEntity);
//            this.redisService.syncRefresh(Constants.REDIS_TYPE_CATALOG, sortEntity.getId());
            entity.setSortIndex(sortIndex);
            mapper.update(entity);
            CmdbV3ModuleCatalog topCatalog = getTopCatalog(catalogId);
            this.redisService.syncRefresh(Constants.REDIS_TYPE_CATALOG, topCatalog.getId());
        }
    }

    @Override
    public JSONObject validCatalog(String parentCatalogId, String catalogCode, String catalogName) {
        JSONObject returnJson = new JSONObject();
        CmdbV3ModuleCatalog queryCode = new CmdbV3ModuleCatalog();
        queryCode.setParentCatalogId(parentCatalogId);
        queryCode.setCatalogCode(catalogCode);
        CmdbV3ModuleCatalog catalog = mapper.get(queryCode);
        if (catalog != null) {
            returnJson.put("flag", "error");
            returnJson.put("msg", "模型分组编码已经存在.");
            return returnJson;
        }
        CmdbV3ModuleCatalog queryName = new CmdbV3ModuleCatalog();
        queryName.setParentCatalogId(parentCatalogId);
        queryName.setCatalogCode(catalogName);
        catalog = mapper.get(queryName);
        if (catalog != null) {
            returnJson.put("flag", "error");
            returnJson.put("msg", "模型分组名称已经存在.");
            return returnJson;
        }
        returnJson.put("flag", "success");
        return returnJson;
    }

    /**
     * 循环每个父节点 加载子数据
     * @param parentCatalogId 父节点ID
     * @return
     */
    private void loopLevel(JSONObject parentJson, String parentCatalogId) {
        CmdbV3ModuleCatalog queryParams = new CmdbV3ModuleCatalog();
        queryParams.setParentCatalogId(parentCatalogId);
        List<CmdbV3ModuleCatalog> rootList = mapper.listByEntity(queryParams);
        if (rootList != null && rootList.size() > 0) {
            JSONArray childArray = new JSONArray();
            for (CmdbV3ModuleCatalog catalog : rootList) {
                JSONObject child = new JSONObject();
                child.put("id", catalog.getId());
                child.put("catalogCode", catalog.getCatalogCode());
                child.put("catalogName", catalog.getCatalogName());
                // 查询是否有模型数据
                Module m = new Module();
                m.setCatalogId(catalog.getId());
                List<Module> moduleList = moduleService.getModuleSelective(m);
                Integer hasModule = (moduleList == null || moduleList.size() == 0) ? 0 : 1;
                child.put("hasModule", hasModule);
                this.loopLevel(child, catalog.getId());
                childArray.add(child);
            }
            parentJson.put("child", childArray);
        } else {
            parentJson.put("child", new JSONArray());
        }
    }

    /**
     * 根据catalogId获取顶级catalogId
     * @param catalogId 模型分组ID
     * @return
     */
    public CmdbV3ModuleCatalog getTopCatalog(String catalogId) {
        CmdbV3ModuleCatalog catalog = getById(catalogId);
        if (catalog == null) {
            return null;
        }
        if (catalog.getParentCatalogId() != null && ("0").equals(catalog.getParentCatalogId())) {
            return catalog;
        }
        return getTopCatalog(catalog.getParentCatalogId());
    }

    @Override
    public CmdbV3ModuleCatalog getByModuleId(String moduleId) {
        return mapper.getByModuleId(moduleId);
    }

}