package com.aspire.ums.cmdb.v3.screen.service.impl;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.aspire.ums.cmdb.v3.screen.mapper.CmdbScreenProblemInfoMapper;
import com.aspire.ums.cmdb.v3.screen.payload.CmdbScreenProblemInfo;
import com.aspire.ums.cmdb.v3.screen.payload.CmdbScreenProblemInfoRequest;
import com.aspire.ums.cmdb.v3.screen.service.ICmdbScreenProblemInfoService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* 描述：
* @author
* @date 2020-07-03 10:17:19
*/
@Service
public class CmdbScreenProblemInfoServiceImpl implements ICmdbScreenProblemInfoService {

    @Autowired
    private CmdbScreenProblemInfoMapper mapper;

    @Override
    public Result<CmdbScreenProblemInfo> listByPage(CmdbScreenProblemInfoRequest req) {
        if(null != req.getPageNo() && null != req.getPageSize()) {
            req.setPageNo((req.getPageNo()-1)*req.getPageSize());
        }
        List<CmdbScreenProblemInfo> list = mapper.listByEntity(req);
        int total = mapper.listByEntityCount(req);;
        return new Result<>(total,list);
    }

    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    public List<CmdbScreenProblemInfo> list() {
        return mapper.list();
    }

    /**
     * 根据主键ID 获取数据信息
     * @param id 实例ID
     * @return 返回指定ID的数据信息
     */
    public CmdbScreenProblemInfo get(String id) {
        return mapper.get(id);
    }

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    public Map<String,Object> insert(CmdbScreenProblemInfo entity) {
        Map<String,Object> rsMp = new HashMap<>();
        if(null != entity) {
            try {
                entity.setId(UUIDUtil.getUUID());
                mapper.insert(entity);
                rsMp.put("flag",true);
                rsMp.put("msg","success");
            } catch (Exception e) {
                e.printStackTrace();
                rsMp.put("flag",false);
                rsMp.put("msg",e.getMessage());
            }
        }
        return rsMp;
    }

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    public Map<String,Object> update(CmdbScreenProblemInfo entity) {
        Map<String,Object> rsMp = new HashMap<>();
        if(null != entity) {
            // 默认不传状态，表示完成
            if(null == entity.getStatus() || "".equals(entity.getStatus())) {
                entity.setStatus("完成");
            }
            try {
                mapper.update(entity);
                rsMp.put("flag",true);
                rsMp.put("msg","success");
            } catch (Exception e) {
                e.printStackTrace();
                rsMp.put("flag",false);
                rsMp.put("msg",e.getMessage());
            }
        }
        return rsMp;
    }

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    public void delete(CmdbScreenProblemInfo entity) {
        mapper.delete(entity);
    }
}