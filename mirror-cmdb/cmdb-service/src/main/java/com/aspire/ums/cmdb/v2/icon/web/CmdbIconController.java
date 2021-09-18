package com.aspire.ums.cmdb.v2.icon.web;


import com.aspire.ums.cmdb.icon.ICmdbIconAPI;
import com.aspire.ums.cmdb.icon.payload.CmdbIcon;
import com.aspire.ums.cmdb.util.BeansUtil;
import com.aspire.ums.cmdb.util.BusinessException;
import com.aspire.ums.cmdb.util.PagedResult;
import com.aspire.ums.cmdb.v2.icon.mapper.CmdbIconMapper;
import com.aspire.ums.cmdb.v2.icon.service.CmdbIconSerivce;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * <p>Project: ums-cmdb-service</p>
 *
 * @Description: 获取模型图标信息
 *
 * @author: mingjianyong
 *
 * @Date: 2017-6-28
 */
@RestController
public class CmdbIconController implements ICmdbIconAPI {
    @Autowired
    private CmdbIconMapper iconMapper;
    @Autowired
    private CmdbIconSerivce iconService;

    /**
     * 获取icon图片
     */
    public PagedResult getIcon(@RequestBody(required = false) CmdbIcon icon,
                               @RequestParam(value = "pageNumber",required = true) Integer pageNumber,
                               @RequestParam(value = "pageSize",required = true) Integer pageSize){
        System.out.println(pageSize);
        PageHelper.startPage(pageNumber, pageSize ,"id");
        List<Map> list = iconMapper.selectIconAll(icon);
        System.out.println(list.size());

        return BeansUtil.toPagedResult(list);
    }
    /**
     * 上传图片
     */
    public Map<String,Object> uploadIcon(HttpServletRequest request){
        Map<String,Object> map = new HashMap<String,Object>();
        try {
            iconService.uploadIcon(request);
            map.put("success", true);
        } catch (BusinessException e) {
            map.put("message",new String(e.getMessage()));
            map.put("success", false);
        } catch (Exception e) {
            map.put("message",e.getMessage());
            map.put("success", false);
            e.printStackTrace();
        }
        return map;
    }
}
