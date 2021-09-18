package com.aspire.mirror.alert.server.dao.common;/**
 * @title: AlertProxyIdcDao
 * @projectName mirror-alert
 * @description: TODO
 * @author baiwp
 * @date 2019/5/2120:00
 */

import com.aspire.mirror.alert.server.dao.common.po.AlertProxyIdc;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @title: AlertProxyIdcDao
 * @projectName mirror-alert
 * @description: TODO
 * @author baiwp
 * @date 2019/5/2120:00
 */
@Mapper
public interface AlertProxyIdcDao {
    public List<AlertProxyIdc> selectAllProxyIdc();
}
