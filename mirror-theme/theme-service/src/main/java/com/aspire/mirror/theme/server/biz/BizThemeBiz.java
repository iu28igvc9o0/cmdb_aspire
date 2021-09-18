package com.aspire.mirror.theme.server.biz;

import com.aspire.mirror.common.auth.GeneralAuthConstraintsAware;
import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.common.entity.Result;
import com.aspire.mirror.theme.api.dto.model.BizThemeDTO;
import com.aspire.mirror.theme.api.dto.model.ThemeDataDTO;

import java.util.List;
import java.util.Map;

/**
 * 业务主题业务层类
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.theme.server.biz
 * 类名称:    BizThemeBiz.java
 * 类描述:    业务主题业务层类
 * 创建人:    JinSu
 * 创建时间:  2018/10/23 19:27
 * 版本:      v1.0
 */
public interface BizThemeBiz {
    /**
     * 创建主题
     *
     * @param bizThemeDTO 主题对象
     * @return String 主题Id
     */
    String insert(BizThemeDTO bizThemeDTO);

    /**
     * 修改主题
     *
     * @param bizThemeDTO 修改主题
     */
    void updateByPrimaryKey(BizThemeDTO bizThemeDTO);

    /**
     * 删除主题
     *
     * @param themeId 主题ID
     */
    void deleteByPrimaryKey(String themeId);

    /**
     * 分页查询
     *
     * @param page 分页查询对象
     * @return PageResponse<BizThemeDTO> 分页查询返回对象
     */
    PageResponse<BizThemeDTO> pageList(PageRequest page);

    /**
     * 查询主题列表
     *
     * @param param
     * @return ist<BizThemeDTO> 主题列表
     */
    List<BizThemeDTO> select(BizThemeDTO param);

    /**
     * 单条数据查询
     *
     * @param themeId 主题ID
     * @return BizThemeDTO 主题数据
     */
    BizThemeDTO selectByPrimaryKey(String themeId, GeneralAuthConstraintsAware authParam);

    /**
     * 接收主题数据
     *
     * @param themeDataDTO 主题数据对象
     * @return Result 接收结果
     */
    Result createThemeData(ThemeDataDTO themeDataDTO);

    /**
     * 获取主题数据
     *
     * @param themeId
     * @param host
     * @param bizCode
     * @param themeCode
     * @return
     */
    Map<String, Object> getThemeData(String themeId, String host, String bizCode, String themeCode);

    Map<String, Object> thematicHistoryInfo(String indexName, String jkzbCode, Long startTime, Long endTime);

    /**
     * 获取主题日志
     *
     * @param themeId
     * @return
     */
    Map<String, Object> getThemeDataHis(String themeId);

}
