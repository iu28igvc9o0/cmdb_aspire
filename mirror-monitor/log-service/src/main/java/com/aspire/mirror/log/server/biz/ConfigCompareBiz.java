package com.aspire.mirror.log.server.biz;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.log.api.dto.ConfigCompareLogsResp;
import com.aspire.mirror.log.api.dto.ConfigCompareReq;
import com.aspire.mirror.log.api.dto.ConfigCompareResp;
import com.aspire.mirror.log.server.dao.po.ConfigCompare;
import com.aspire.mirror.log.server.dao.po.ConfigCompareLogs;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author baiwenping
 * @Title: ConfigCompareBiz
 * @Package com.aspire.mirror.log.server.biz
 * @Description: TODO
 * @date 2020/12/21 10:33
 */
public interface ConfigCompareBiz {

    /**
     *
     * @param masterIp
     * @param backupIp
     * @param compareTimeFrom
     * @param compareTimeTo
     * @param compareType
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageResponse<ConfigCompare> getCompareList(String masterIp,
                                                   String backupIp,
                                                   String compareTimeFrom,
                                                   String compareTimeTo,
                                                   String compareType,
                                                   Integer pageNum,
                                                   Integer pageSize);

    /**
     * 根据id列表查询
     * @param ids
     * @return
     */
    List<ConfigCompare> getCompareListByIds (List<Integer> ids);

    /**
     *
     * @param id
     * @return
     */
    ConfigCompare getCompareById (Integer id);

    /**
     * 新增
     * @param configCompare
     */
    void insert(ConfigCompare configCompare);

    /**
     * 新增
     * @param configCompare
     */
    void update(ConfigCompare configCompare);

    /**
     * 批量新增
     * @param configCompareList
     */
    void insertBatch(List<ConfigCompare> configCompareList);

    /**
     *
     * @param compareId
     * @return
     */
    List<ConfigCompareLogs> getLogs(Integer compareId);

    /**
     * 新增比对记录
     * @param configCompareLogs
     */
    void insertLogs(ConfigCompareLogs configCompareLogs);

}
