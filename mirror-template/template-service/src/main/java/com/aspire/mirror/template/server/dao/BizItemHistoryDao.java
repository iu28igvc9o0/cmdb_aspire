package com.aspire.mirror.template.server.dao;


import com.aspire.mirror.common.entity.Page;
import com.aspire.mirror.template.server.dao.po.BizItemHistory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 包名:     com.aspire.webbas.announce.dao
 * 类名:     BizItemHistoryDao
 * 描述:     数据访问对象
 * 作者:     金素
 * 时间:     2018-11-15 16:47:38
 */
@Mapper
public interface BizItemHistoryDao {
    /**
     * 新增
     */
    public void add(BizItemHistory bizItemHistory);

    /**
     * 分页查询
     */
    public List<BizItemHistory> list(Page page);

    /**
     * 详情
     */
    public BizItemHistory get(String id);

    /**
     * 修改
     */
    public void mod(BizItemHistory bizItemHistory);

    /**
     * 删除
     */
    public void del(String id);

}
