package com.aspire.ums.cmdb.resource.web;

import com.aspire.ums.cmdb.resource.entity.ResourceEstimate;
import com.aspire.ums.cmdb.resource.entity.ResourceEstimateRequest;
import com.aspire.ums.cmdb.resource.entity.ResourceEstimateResponse;
import com.aspire.ums.cmdb.resource.service.ResourceDemandCollectService;
import com.aspire.ums.cmdb.resource.service.ResourceEstimateService;
import com.aspire.ums.cmdb.util.StringUtils;
import com.gs.collections.impl.bimap.mutable.HashBiMap;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping(value = "/resource/resourceEstimate")
@Api("资源预估")
public class ResourceEstimateController {
    private final Logger logger = Logger.getLogger(getClass());
    private static final String KEY_SPLIT_PAGE = "page";
    private static final String KEY_SPLIT_ROWS = "rows";

    @Autowired
    private ResourceEstimateService resourceEstimateService;
    @Autowired
    private ResourceDemandCollectService resourceDemandCollectService;

    @RequestMapping(value = "/getResourcePoolAll_config", method = RequestMethod.GET)
    @ApiOperation(value = "获取资源池配置", notes = "获取资源池配置")
    public Object[] getResourcePoolAll_config() {
        Object[] obj = null;
        try {
            List<String> resourcePoolList = resourceEstimateService.getResourcePoolAll_config();
            if (!CollectionUtils.isEmpty(resourcePoolList)) {
                List<Map<String,String>> list = new ArrayList<>();
                for (String text : resourcePoolList) {
                    Map<String, String> innerMap = new HashBiMap<>();
                    innerMap.put("text", text);
                    list.add(innerMap);
                }
                obj = new Object[]{ Boolean.TRUE, list };
            }
        } catch (Exception e) {
            logger.error("获取资源池出错!", e);
            obj = new Object[] { Boolean.FALSE, "获取资源池失败!"};
        }
        return obj;
    }

    @RequestMapping(value = "/gridData", method = RequestMethod.POST)
    @ApiOperation(value = "获取资源池数据", notes = "获取资源池数据")
    public ResourceEstimateResponse gridData(@RequestParam Map<String, Object> params) {
        Integer page = Integer.parseInt(params.get(KEY_SPLIT_PAGE).toString().trim());
        Integer rows = Integer.parseInt(params.get(KEY_SPLIT_ROWS).toString().trim());
        Integer startIndex = (page - 1) * rows;
        return resourceEstimateService.queryDataGrid(params, startIndex, rows);
    }

    @RequestMapping(value = "/collectGridData", method = RequestMethod.POST)
    @ApiOperation(value = "获取资源收集数据", notes = "获取资源收集数据")
    public ResourceEstimateResponse collectGridData(@RequestParam Map<String, Object> params) {
        Integer page =  Integer.parseInt(params.get(KEY_SPLIT_PAGE).toString().trim());
        Integer rows = Integer.parseInt(params.get(KEY_SPLIT_ROWS).toString().trim());
        Integer startIndex = (page - 1) * rows;
        return resourceEstimateService.queryCollectDataGrid(params, startIndex, rows);
    }

    @RequestMapping(value = "/getCollectByCollectIds", method = RequestMethod.POST)
    @ApiOperation(value = "通过收集ID获取资源收集数据", notes = "通过收集ID获取资源收集数据")
    public Object[] getCollectByCollectIds(@RequestBody ResourceEstimateRequest param) {
        Object[] obj = null;
        ResourceEstimate estimate = null;
        try {
            estimate = resourceEstimateService.getCollectByCollectIds(param.getCollect_id());
            obj = new Object[] { Boolean.TRUE, "成功!" , estimate};
        } catch (Exception e) {
            logger.error("getCollectByCollectIds error", e);
            obj = new Object[] { Boolean.FALSE, "失败" };
        }
        return obj;
    }

    @RequestMapping(value = "/addOrEditEstimate", method = RequestMethod.POST)
    @ApiOperation(value = "添加资源预估数据", notes = "添加资源预估数据")
    public Object[] addOrEditEstimate(@RequestBody ResourceEstimate estimate) {
        Object[] obj = null;
        String headStr = "新增";
        String userId = "";
        try {
            if (StringUtils.isNotEmpty(estimate.getId())) { // Id 已存在，更新
                headStr = "更新";
                estimate.setUpdate_id(userId);
                estimate.setUpdate_time(new Date());
                resourceEstimateService.updateEstimate(estimate);
            } else { // Id 不存在，新增
                estimate.setCreate_id(userId);
                estimate.setCreate_time(new Date());
                int flag = resourceEstimateService.isClosedByPoolName(estimate.getResource_pool());
                if (flag < 1) {
                    resourceEstimateService.addEstimate(estimate);
                } else {
                    obj = new Object[] { Boolean.FALSE, "该资源池还未关闭，请对其进行编辑" };
                    return obj;
                }
            }
        } catch (Exception e) {
            logger.error("[资源建设管理-资源预估]"+headStr+"资源预估失败", e);
            obj = new Object[] { Boolean.FALSE, headStr+"资源预估失败" };
            return obj;
        }
        obj = new Object[] { Boolean.TRUE, headStr+"成功!" };
        return obj;
    }

    @RequestMapping(value = "/collectGridDataAll", method = RequestMethod.POST)
    @ApiOperation(value = "查询所有资源收集数据", notes = "查询所有资源收集数据")
    public ResourceEstimateResponse collectGridDataAll(@RequestParam(value = "estimateId") String estimateId,
                                                       @RequestParam(value = KEY_SPLIT_PAGE) Integer page,
                                                       @RequestParam(value = KEY_SPLIT_ROWS) Integer rows) {
        Map<String, Object> params = new HashMap<>();
        page =  page != null ? page : 1;
        rows = rows != null ? rows : 10;
        Integer startIndex = (page - 1) * rows;
        params.put("estimateId", estimateId);
        params.put("startIndex", startIndex);
        params.put("pageSize", rows);
        logger.error("collectGridDataAll: startIndex: " + startIndex + ", pageSize: " + rows);
        return resourceDemandCollectService.getCollectAll(params);
    }

    @RequestMapping("/closeEstimate")
    @ResponseBody
    @ApiOperation(value = "关闭资源预估", notes = "关闭资源预估")
    public Object[] closeEstimate(@RequestBody ResourceEstimateRequest param, HttpServletRequest request){
        String id = param.getEstimate_id();
        Object[] obj = null;
        try {
            resourceEstimateService.closeEstimate(id, "admin");
        } catch (Exception e) {
            obj = new Object[] { Boolean.FALSE, "关闭资源预估失败" };
        }
        obj = new Object[] { Boolean.TRUE, "关闭资源预估成功!"};
        return obj;
    }
}
