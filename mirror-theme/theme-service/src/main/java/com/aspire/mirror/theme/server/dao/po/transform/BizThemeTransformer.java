package com.aspire.mirror.theme.server.dao.po.transform;

import com.aspire.mirror.theme.api.dto.model.BizThemeDTO;
import com.aspire.mirror.theme.server.dao.po.BizTheme;
import com.google.common.collect.Lists;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 业务主题转换
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.theme.server.dao.po.transform
 * 类名称:    BizThemeTransformer.java
 * 类描述:    业务主题转换
 * 创建人:    JinSu
 * 创建时间:  2018/10/23 19:47
 * 版本:      v1.0
 */
@NoArgsConstructor
public class BizThemeTransformer {
    /**
     * 将主题PO实体转换为动作DTO实体
     *
     * @param  bizTheme 告警PO实体
     * @return ActionsDTO 动作DTO实体
     */
    public static BizThemeDTO fromPo(final BizTheme bizTheme) {
        if (null == bizTheme) {
            return null;
        }
        BizThemeDTO bizThemeDTO = new BizThemeDTO();
        bizThemeDTO.setThemeId(bizTheme.getThemeId());
        bizThemeDTO.setCreateTime(bizTheme.getCreateTime());
        bizThemeDTO.setIndexName(bizTheme.getIndexName());
        bizThemeDTO.setInterval(bizTheme.getInterval());
        bizThemeDTO.setLastFailTime(bizTheme.getLastFailTime());
        bizThemeDTO.setLastUpTime(bizTheme.getLastUpTime());
        bizThemeDTO.setLastUpValue(bizTheme.getLastUpValue());
        bizThemeDTO.setObjectId(bizTheme.getObjectId());
        bizThemeDTO.setObjectType(bizTheme.getObjectType());
        bizThemeDTO.setStatus(bizTheme.getStatus());
        bizThemeDTO.setThemeCode(bizTheme.getThemeCode());
        bizThemeDTO.setThemeName(bizTheme.getThemeName());
        bizThemeDTO.setUpStatus(bizTheme.getUpStatus());
        bizThemeDTO.setUpSwitch(bizTheme.getUpSwitch());
        bizThemeDTO.setUpType(bizTheme.getUpType());
        bizThemeDTO.setValueType(bizTheme.getValueType());
        bizThemeDTO.setLogReg(bizTheme.getLogReg());
        bizThemeDTO.setLogContent(bizTheme.getLogContent());
        bizThemeDTO.setUpdateTime(bizTheme.getUpdateTime());
        bizThemeDTO.setDeleteFlag(bizTheme.getDeleteFlag());
        return bizThemeDTO;
    }

    /**
     * 将主题业务实体对象集合转换为动作持久化对象集合
     *
     * @param themeList 主题业务实体对象集合
     * @return List<ActionsDTO> 主题持久化对象集合
     */
    public static List<BizThemeDTO> fromPo(final List<BizTheme> themeList) {
        if (CollectionUtils.isEmpty(themeList)) {
            return Lists.newArrayList();
        }
        List<BizThemeDTO> listBizThemeDTO = Lists.newArrayList();

        for (BizTheme bizTheme : themeList) {
            listBizThemeDTO.add(BizThemeTransformer.fromPo(bizTheme));
        }
        return listBizThemeDTO;
    }

    /**
     * 将主题DTO实体转换为动作PO实体
     *
     * @param  bizThemeDTO 主题DTO实体类
     * @return Alerts 主题PO实体
     */
    public static BizTheme toPo(final BizThemeDTO bizThemeDTO) {
        if (null == bizThemeDTO) {
            return null;
        }

        BizTheme bizTheme = new BizTheme();
        bizTheme.setThemeId(bizThemeDTO.getThemeId());
        bizTheme.setCreateTime(bizThemeDTO.getCreateTime());
        bizTheme.setIndexName(bizThemeDTO.getIndexName());
        bizTheme.setInterval(bizThemeDTO.getInterval());
        bizTheme.setLastFailTime(bizThemeDTO.getLastFailTime());
        bizTheme.setLastUpTime(bizThemeDTO.getLastUpTime());
        bizTheme.setLastUpValue(bizThemeDTO.getLastUpValue());
        bizTheme.setObjectId(bizThemeDTO.getObjectId());
        bizTheme.setObjectType(bizThemeDTO.getObjectType());
        bizTheme.setStatus(bizThemeDTO.getStatus());
        bizTheme.setThemeCode(bizThemeDTO.getThemeCode());
        bizTheme.setThemeName(bizThemeDTO.getThemeName());
        bizTheme.setUpStatus(bizThemeDTO.getUpStatus());
        bizTheme.setUpSwitch(bizThemeDTO.getUpSwitch());
        bizTheme.setUpType(bizThemeDTO.getUpType());
        bizTheme.setValueType(bizThemeDTO.getValueType());
        bizTheme.setLogReg(bizThemeDTO.getLogReg());
        bizTheme.setLogContent(bizThemeDTO.getLogContent());
        bizTheme.setUpdateTime(bizThemeDTO.getUpdateTime());
        bizTheme.setDeleteFlag(bizThemeDTO.getDeleteFlag());
        bizTheme.setAuthPrecinctList(bizThemeDTO.getAuthPrecinctList());
        bizTheme.setAuthBizSystemIdList(bizThemeDTO.getAuthBizSystemIdList());
        bizTheme.setAuthDeviceIdList(bizThemeDTO.getAuthDeviceIdList());
        bizTheme.setAuthDeviceTypeList(bizThemeDTO.getAuthDeviceTypeList());
        return bizTheme;
    }

    /**
     * 将主题业务实体对象集合转换为动作持久化对象集合
     *
     * @param listBizThemeDTO 主题业务实体对象集合
     * @return List<Alerts> 主题持久化对象集合
     */
    public static List<BizTheme> toPo(final List<BizThemeDTO> listBizThemeDTO) {
        if (CollectionUtils.isEmpty(listBizThemeDTO)) {
            return Lists.newArrayList();
        }
        List<BizTheme> listBizTheme = Lists.newArrayList();

        for (BizThemeDTO bizThemeDTO : listBizThemeDTO) {
            listBizTheme.add(BizThemeTransformer.toPo(bizThemeDTO));
        }
        return listBizTheme;
    }
}
