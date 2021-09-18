package com.aspire.ums.cmdb.sync.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aspire.ums.cmdb.sync.entity.CmdbBusinessLine;
import com.aspire.ums.cmdb.sync.mapper.CmdbBusinessLineMapper;
import com.aspire.ums.cmdb.sync.service.ICmdbBusinessLineService;

import lombok.extern.slf4j.Slf4j;

/**
 * 描述：
 * 
 * @author
 * @date 2020-05-20 09:16:01
 */
@Service
@Slf4j
public class CmdbBusinessLineServiceImpl implements ICmdbBusinessLineService {

    @Autowired
    private CmdbBusinessLineMapper mapper;

    /**
     * 获取所有实例
     * 
     * @return 返回所有实例数据
     */
    @Override
    public List<CmdbBusinessLine> list() {
        return mapper.list();
    }

    /**
     * 根据主键ID 获取数据信息
     * 
     * @param entity
     *            实例信息
     * @return 返回指定ID的数据信息
     */
    @Override
    public CmdbBusinessLine get(CmdbBusinessLine entity) {
        return mapper.get(entity);
    }

    /**
     * 新增实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    @Override
    public void insert(CmdbBusinessLine entity) {
        if (StringUtils.isBlank(entity.getDelFlag())) {
            entity.setDelFlag("0");
        }
        if (StringUtils.isBlank(entity.getDisabled())) {
            entity.setDisabled("0");
        }
        CmdbBusinessLine exists = mapper.getByBusinessCode(entity.getBusinessCode());
        if (exists != null) {
            log.info("系统已存在businessCode:[{}]的记录,跳过新增!", entity.getBusinessCode());
            return;
        }
        mapper.insert(entity);
    }

    /**
     * 修改实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    @Override
    public void update(CmdbBusinessLine entity) {
        CmdbBusinessLine exists = mapper.getByBusinessCode(entity.getBusinessCode());
        if (exists == null) {
            log.warn("系统不存在businessCode:[{}]的记录,跳过更新!", entity.getBusinessCode());
            return;
        }
        entity.setId(exists.getId());
        // 修改父级业务线.
        if (!StringUtils.equals(exists.getParentCode(), entity.getParentCode())) {
            if (StringUtils.isBlank(entity.getParentCode())) {
                entity.setParentId(null);
            } else {
                CmdbBusinessLine parent = mapper.getByBusinessCode(entity.getParentCode());
                if (parent != null) {
                    entity.setParentId(parent.getId());
                }
            }
        }
        if (StringUtils.isBlank(entity.getDelFlag())) {
            entity.setDelFlag("0");
        }
        if (StringUtils.isBlank(entity.getDisabled())) {
            entity.setDisabled("0");
        }
        mapper.update(entity);
    }

    /**
     * 删除实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    @Override
    public void delete(CmdbBusinessLine entity) {
        mapper.delete(entity);
    }

    @Override
    public void deleteByBusinessCode(String businessCode) {
        mapper.deleteByBusinessCode(businessCode);
    }
}
