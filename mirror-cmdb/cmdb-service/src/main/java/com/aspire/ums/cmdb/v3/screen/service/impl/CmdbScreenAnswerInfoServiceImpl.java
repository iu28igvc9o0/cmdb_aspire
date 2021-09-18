package com.aspire.ums.cmdb.v3.screen.service.impl;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.aspire.ums.cmdb.v3.screen.mapper.CmdbScreenAnswerInfoMapper;
import com.aspire.ums.cmdb.v3.screen.payload.CmdbScreenAnswerInfo;
import com.aspire.ums.cmdb.v3.screen.service.ICmdbScreenAnswerInfoService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* 描述：
* @author
* @date 2020-07-03 10:17:18
*/
@Service
public class CmdbScreenAnswerInfoServiceImpl implements ICmdbScreenAnswerInfoService {

    @Autowired
    private CmdbScreenAnswerInfoMapper mapper;

    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    public List<CmdbScreenAnswerInfo> list() {
        return mapper.list();
    }

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回指定ID的数据信息
     */
    public CmdbScreenAnswerInfo get(CmdbScreenAnswerInfo entity) {
        return mapper.get(entity);
    }

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    public Map<String,Object> insert(CmdbScreenAnswerInfo entity) {
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
    public void update(CmdbScreenAnswerInfo entity) {
        mapper.update(entity);
    }

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    public void delete(CmdbScreenAnswerInfo entity) {
        mapper.delete(entity);
    }
}