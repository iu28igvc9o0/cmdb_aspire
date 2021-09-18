package com.aspire.ums.cmdb.cmic.service.impl;

import com.aspire.ums.cmdb.cmic.mapper.CmdbModelTabsMapper;
import com.aspire.ums.cmdb.cmic.payload.CmdbModelTabsBase;
import com.aspire.ums.cmdb.cmic.payload.CmdbModelTabsReq;
import com.aspire.ums.cmdb.cmic.payload.CmdbModelTabsResp;
import com.aspire.ums.cmdb.cmic.service.CmdbModelTabsService;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author :  fanshenquan
 * @CreateDate :  2020/5/12 16:16
 */
@Slf4j
@Service("CmdbModelTabsService")
public class CmdbModelTabsServiceImpl implements CmdbModelTabsService {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private CmdbModelTabsMapper cmdbModelTabsMapper;

    @Override
    public String insertTabs(CmdbModelTabsBase entity) {
        StringBuilder result = new StringBuilder("新增完成!");
        String[] modelIdArr = null;
        if (entity.getModelId() != null) {
            modelIdArr = entity.getModelId().split(",");
        }
//        entity.setModelId(null);
        // 关键，如果使用批量功能，需要使用BatchExecutor而不是默认的SimpleExecutor
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
        try {
            CmdbModelTabsMapper dao = sqlSession.getMapper(CmdbModelTabsMapper.class);
            for (String modelId : modelIdArr) {
                // 因为使用的Executor是BatchExecutor, 并不会真的执行
                // 内部调用的是statement.addBatch()

                String modelName = returnModelName(modelId, entity.getTabName());
                if (!StringUtils.isEmpty(modelName)) {
                    result.append(String.format("模型:%s,tab标签:%s 已存在;", modelName, entity.getTabName()));
                } else {
                    CmdbModelTabsBase tabsEntity = new CmdbModelTabsBase();
                    BeanUtils.copyProperties(entity, tabsEntity);
                    tabsEntity.setTabsId(UUIDUtil.getUUID());
                    tabsEntity.setModelId(modelId);
                    dao.insertTabs(tabsEntity);
                }
            }
        } catch (Exception e) {
            log.error("批量插入失败", e);
            result = new StringBuilder("批量插入失败!");
        } finally {
            sqlSession.commit();
            sqlSession.close();
        }
        return result.toString();
    }


    @Override
    public String updateTabsById(CmdbModelTabsBase entity) {
        String result = "更新成功!";
        if (isExistTabs(entity.getModelId(), entity.getTabName())) {
            result = "更新失败!模型对应标签已存在";
        } else {
            cmdbModelTabsMapper.updateTabsById(entity);
        }
        return result;
    }

    @Override
    public List<CmdbModelTabsResp> findPage(CmdbModelTabsReq query) {
        return cmdbModelTabsMapper.selectTabs(query);
    }

    @Override
    public Integer findPageCount(CmdbModelTabsReq query) {
        return cmdbModelTabsMapper.selectTabsCount(query);
    }

    @Override
    public CmdbModelTabsResp findOneDataById(String tabsId) {
        List<CmdbModelTabsResp> entityList = cmdbModelTabsMapper.selectTabs(new CmdbModelTabsReq(tabsId));
        return entityList.isEmpty() ? null : entityList.get(0);
    }

    @Override
    public Boolean deleteByIdBatch(List<String> tabsIdList) {
        int count = cmdbModelTabsMapper.deleteByIdBatch(tabsIdList);
        return count > 0 ? true : false;
    }

    @Override
    public Boolean isExistTabs(String modelId, String tabName) {
        int count = cmdbModelTabsMapper.isExistTabs(modelId, tabName);
        //库的总数加上当前的修改项，超过1说明存在重复修改
        count++;
        return count > 1 ? true : false;
    }

    @Override
    public String returnModelName(String modelId, String tabName) {
        return cmdbModelTabsMapper.returnModelName(modelId, tabName);
    }


}
