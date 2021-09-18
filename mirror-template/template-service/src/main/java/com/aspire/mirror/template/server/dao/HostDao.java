package com.aspire.mirror.template.server.dao;

import com.aspire.mirror.template.api.dto.vo.MirrorHostVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 自监控设备Dao层
 * <p>
 * 项目名称:  mirror平台
 * 类名称:    HostDao
 * 类描述:    自监控设备Dao层
 * 创建人:    JinSu
 * 创建时间:  2020/11/4 10:20
 * 版本:      v1.0
 */
@Mapper
public interface HostDao {
    void batchInsertHost(List<MirrorHostVO> hostList);

    MirrorHostVO selectByIpAndPool(@Param("ip") String ip, @Param("pool") String pool);

    List<MirrorHostVO> selectByTemplateId(@Param("templateId") String templateId);

    List<MirrorHostVO> selectByProxyIdentity(@Param("proxyIdentity") String proxyIdentity);
}
