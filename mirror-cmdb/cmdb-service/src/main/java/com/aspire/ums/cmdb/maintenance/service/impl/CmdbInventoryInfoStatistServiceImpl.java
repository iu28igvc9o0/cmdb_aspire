package com.aspire.ums.cmdb.maintenance.service.impl;

import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.maintenance.mapper.CmdbInventoryInfoStatistMapper;
import com.aspire.ums.cmdb.maintenance.payload.CmdbComponentInfo;
import com.aspire.ums.cmdb.maintenance.payload.CmdbInventoryInfoStatistRequest;
import com.aspire.ums.cmdb.maintenance.service.ICmdbInventoryInfoStatistService;
import com.aspire.ums.cmdb.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scala.Int;

import java.sql.SQLException;
import java.util.*;

/**
 * @ClassName CmdbInventoryInfoStatistServiceImpl
 * @Description TODO
 * @Author luowenbo
 * @Date 2020/2/16 15:49
 * @Version 1.0
 */
@Service
public class CmdbInventoryInfoStatistServiceImpl implements ICmdbInventoryInfoStatistService {

    @Autowired
    private CmdbInventoryInfoStatistMapper cmdbInventoryInfoStatistMapper;

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class, SQLException.class})
    public List<Map<String, Object>> firstLayer() {
        List<Map<String, Object>> tmpRs = cmdbInventoryInfoStatistMapper.firstLayer();
        List<Map<String, Object>> result = new ArrayList<>();
        // 设备区域为多个资源池，特别处理维保关联未成功数量
        List<Map<String, Object>> itemList = cmdbInventoryInfoStatistMapper.getFailCountByDeviceArea();
        Map<String,Integer> failCount = new HashMap<>();
        if(!itemList.isEmpty()) {
            for(Map<String, Object> item : itemList) {
                // 多个资源池，进行分割，逐个统计数量
                String idcType = item.get("idcType").toString();
                Integer idcCount = Integer.parseInt(item.get("count").toString());
                String[] idcArray = idcType.split(",");
                for(String str : idcArray) {
                    if(failCount.containsKey(str)) {
                        failCount.put(str,failCount.get(str)+idcCount);
                    } else {
                        failCount.put(str,idcCount);
                    }
                }
            }
        }
        // 再与之前统计的维保未关联数量进行合计
        for(Map<String, Object> r: tmpRs) {
            String idcS = r.get("idcType").toString();
            if(null != failCount && failCount.containsKey(idcS)) {
                Integer count = Integer.parseInt(r.get("failCount").toString()) + failCount.get(idcS);
                r.put("failCount",count.toString());
            }
            result.add(r);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class, SQLException.class})
    public Result<Map<String, Object>> secondLayer(CmdbInventoryInfoStatistRequest req) {
        List<Map<String, Object>> result = cmdbInventoryInfoStatistMapper.secondLayer(req);
        Collections.sort(result, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                String o1Success = o1.get("successCount").toString();
                String o2Success = o2.get("successCount").toString();
                String o1Fail = o1.get("failCount").toString();
                String o2Fail = o2.get("failCount").toString();
                String o1Name = o1.get("projectName").toString();
                String o2Name = o2.get("projectName").toString();
                // 维保关联成功的数量，先排序
                if(!o1Success.equals(o2Success)) {
                    return Integer.parseInt(o2Success) - Integer.parseInt(o1Success);
                }
                // 当维保关联成功数量一致时，按照维保未关联数量排序
                if(!o1Fail.equals(o2Fail)) {
                    return Integer.parseInt(o2Fail) - Integer.parseInt(o1Fail);
                }
                // 最后按照维保项目名称排序
                return o2Name.compareTo(o1Name);
            }
        });
        List<Map<String, Object>> r = null;
        if (StringUtils.isNotEmpty(req.getPageNo()) && StringUtils.isNotEmpty(req.getPageSize())) {
            int fromIndex = (req.getPageNo() - 1)* req.getPageSize();
            int endIndex = req.getPageNo()* req.getPageSize()>result.size()?result.size():req.getPageNo()* req.getPageSize();
            r = result.subList(fromIndex,endIndex);
        } else {
            r = result;
        }
        return new Result<>(result.size(), r);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class, SQLException.class})
    public Result<Map<String, Object>> thirdLayer(CmdbInventoryInfoStatistRequest req) {
        if (StringUtils.isNotEmpty(req.getPageNo()) && StringUtils.isNotEmpty(req.getPageSize())) {
            req.setPageNo((req.getPageNo() - 1)* req.getPageSize());
        }
        List<Map<String, Object>> list = null;
        Integer totalCount = null;
        // 维保未关联数据处理
        if("maintenance".equals(req.getSearchType())) {
            list = cmdbInventoryInfoStatistMapper.maintenThirdLayer(req);
            totalCount = cmdbInventoryInfoStatistMapper.getMaintenThirdLayerCount(req);
        } else {
            // 资产未关联数据处理
            list = cmdbInventoryInfoStatistMapper.instanceThirdLayer(req);
            totalCount = cmdbInventoryInfoStatistMapper.getInstanceThirdLayerCount(req);
        }
        return new Result<>(totalCount, list);
    }
}
