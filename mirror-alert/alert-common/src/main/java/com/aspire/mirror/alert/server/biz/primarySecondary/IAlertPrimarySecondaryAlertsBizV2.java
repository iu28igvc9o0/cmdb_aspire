package com.aspire.mirror.alert.server.biz.primarySecondary;

import com.aspire.mirror.alert.server.util.Criteria;
import com.aspire.mirror.common.entity.PageResponse;

import java.util.List;
import java.util.Map;

/**
 * 描述：
 *
 * @author
 * @date 2019-08-14 19:35:33
 */
public interface IAlertPrimarySecondaryAlertsBizV2 {

    /**
     * 根据条件获取所有实例
     *
     * @param example
     * @return
     */
    List<Map<String, Object>> list(Criteria example);

    /**
     * 分页查询
     *
     * @param example
     * @return
     */
    PageResponse<Map<String, Object>> findPage(Criteria example);
}