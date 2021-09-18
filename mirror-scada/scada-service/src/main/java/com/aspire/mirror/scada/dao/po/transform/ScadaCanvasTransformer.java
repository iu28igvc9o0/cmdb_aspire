package com.aspire.mirror.scada.dao.po.transform;

import com.aspire.mirror.scada.api.dto.model.ScadaCanvasDTO;
import com.aspire.mirror.scada.dao.po.ScadaCanvas;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * 对象转换类
 * <p>
 * 项目名称:  mirror平台
 * 包:     com.aspire.mirror.scada.entity   
 * 类名称:     Canvas
 * 类描述:    对应的PO与DTO的转换类
 * 创建时间:     2019-06-11 11:32:23
 * @author JinSu
 */
public final class ScadaCanvasTransformer {
	private ScadaCanvasTransformer() {
    }
    
    /**
     * 将PO实体转换为动作DTO实体
     * @param scadaCanvas 动作PO实体
     * @return CanvasDTO 动作DTO实体
     */
    public static ScadaCanvasDTO fromPo(final ScadaCanvas scadaCanvas) {
        if (null == scadaCanvas) {
            return null;
        }

        ScadaCanvasDTO scadaCanvasDTO = new ScadaCanvasDTO();
        scadaCanvasDTO.setId(scadaCanvas.getId());
        scadaCanvasDTO.setName(scadaCanvas.getName());
//        scadaCanvasDTO.setXCoordinate(scadaCanvas.getXCoordinate());
//        scadaCanvasDTO.setYCoordinate(scadaCanvas.getYCoordinate());
//        scadaCanvasDTO.setWidth(scadaCanvas.getWidth());
//        scadaCanvasDTO.setHeight(scadaCanvas.getHeight());
//        scadaCanvasDTO.setBackgroundColor(scadaCanvas.getBackgroundColor());
//        scadaCanvasDTO.setBackgroundPictureUrl(scadaCanvas.getBackgroundPictureUrl());
        scadaCanvasDTO.setPictureType(scadaCanvas.getPictureType());
//        scadaCanvasDTO.setPrecinctId(scadaCanvas.getPrecinctId());
        scadaCanvasDTO.setPageType(scadaCanvas.getPageType());
        scadaCanvasDTO.setContent(scadaCanvas.getContent());
//        scadaCanvasDTO.setPrecinctName(scadaCanvas.getPrecinctName());
        scadaCanvasDTO.setIdc(scadaCanvas.getIdc());
        scadaCanvasDTO.setPod(scadaCanvas.getPod());
        scadaCanvasDTO.setBizSystem(scadaCanvas.getBizSystem());
        scadaCanvasDTO.setIsDefault(scadaCanvas.getIsDefault());
        scadaCanvasDTO.setBizSystemList(scadaCanvas.getBizSystemList());
        scadaCanvasDTO.setUpdateTime(scadaCanvas.getUpdateTime());
        scadaCanvasDTO.setCreateTime(scadaCanvas.getCreateTime());
        scadaCanvasDTO.setProjectName(scadaCanvas.getProjectName());
        scadaCanvasDTO.setBindObj(scadaCanvas.getBindObj());
        return scadaCanvasDTO;
    }

    /**
     * 将业务实体对象集合转换为动作持久化对象集合
     * @param listScadaCanvas 业务实体对象集合
     * @return List<CanvasDTO> 持久化对象集合
     */
    public static List<ScadaCanvasDTO> fromPo(final List<ScadaCanvas> listScadaCanvas) {
        if (CollectionUtils.isEmpty(listScadaCanvas)) {
            return Lists.newArrayList();
        }
        List<ScadaCanvasDTO> listScadaCanvasDTO = Lists.newArrayList();

        for (ScadaCanvas scadaCanvas : listScadaCanvas) {
            listScadaCanvasDTO.add(ScadaCanvasTransformer.fromPo(scadaCanvas));
        }
        return listScadaCanvasDTO;
    }

    /**
     * 将DTO实体转换为动作PO实体
     * @param scadaCanvasDTO DTO实体类
     * @return Canvas PO实体
     */
    public static ScadaCanvas toPo(final ScadaCanvasDTO scadaCanvasDTO) {
        if (null == scadaCanvasDTO) {
            return null;
        }

        ScadaCanvas scadaCanvas = new ScadaCanvas();
        scadaCanvas.setId(scadaCanvasDTO.getId());
        scadaCanvas.setName(scadaCanvasDTO.getName());
//        scadaCanvas.setXCoordinate(scadaCanvasDTO.getXCoordinate());
//        scadaCanvas.setYCoordinate(scadaCanvasDTO.getYCoordinate());
//        scadaCanvas.setWidth(scadaCanvasDTO.getWidth());
//        scadaCanvas.setHeight(scadaCanvasDTO.getHeight());
//        scadaCanvas.setBackgroundColor(scadaCanvasDTO.getBackgroundColor());
//        scadaCanvas.setBackgroundPictureUrl(scadaCanvasDTO.getBackgroundPictureUrl());
//        scadaCanvas.setPrecinctId(scadaCanvasDTO.getPrecinctId());
        scadaCanvas.setPageType(scadaCanvasDTO.getPageType());
        scadaCanvas.setPictureType(scadaCanvasDTO.getPictureType());
        scadaCanvas.setContent(scadaCanvasDTO.getContent());
//        scadaCanvas.setPrecinctName(scadaCanvasDTO.getPrecinctName());
        scadaCanvas.setIdc(scadaCanvasDTO.getIdc());
        scadaCanvas.setPod(scadaCanvasDTO.getPod());
        scadaCanvas.setBizSystem(scadaCanvasDTO.getBizSystem());
        scadaCanvas.setIsDefault(scadaCanvasDTO.getIsDefault());
        scadaCanvas.setBizSystemList(scadaCanvasDTO.getBizSystemList());
        scadaCanvas.setUpdateTime(scadaCanvasDTO.getUpdateTime());
        scadaCanvas.setCreateTime(scadaCanvasDTO.getCreateTime());
        scadaCanvas.setProjectName(scadaCanvasDTO.getProjectName());
        scadaCanvas.setBindObj(scadaCanvasDTO.getBindObj());
        return scadaCanvas;
    }

    /**
     * 将业务实体对象集合转换为动作持久化对象集合
     * @param listScadaCanvasDTO 业务实体对象集合
     * @return List<Canvas> 持久化对象集合
     */
    public static List<ScadaCanvas> toPo(final List<ScadaCanvasDTO> listScadaCanvasDTO) {
        if (CollectionUtils.isEmpty(listScadaCanvasDTO)) {
            return Lists.newArrayList();
        }
        List<ScadaCanvas> listScadaCanvas = Lists.newArrayList();

        for (ScadaCanvasDTO scadaCanvasDTO : listScadaCanvasDTO) {
            listScadaCanvas.add(ScadaCanvasTransformer.toPo(scadaCanvasDTO));
        }
        return listScadaCanvas;
    }

    /**
     * 将业务实体对象集合转换为Map
     * @param listScadaCanvasDTO 业务实体对象集合
     * @return Map<String,CanvasDTO> Map[key=String|value=CanvasDTO]
     */
    public static Map<String, ScadaCanvasDTO> toDTOMap(final List<ScadaCanvasDTO> listScadaCanvasDTO) {
        if (CollectionUtils.isEmpty(listScadaCanvasDTO)) {
            return Maps.newHashMap();
        }
        Map<String, ScadaCanvasDTO> scadaCanvasDTOMaps = Maps.newHashMap();

        for (ScadaCanvasDTO scadaCanvasDTO : listScadaCanvasDTO) {
            scadaCanvasDTOMaps.put(scadaCanvasDTO.getId(), scadaCanvasDTO);
        }
        return scadaCanvasDTOMaps;
    }

    /**
     * 将业务实体对象集合中的主键进行提取并封装成数组
     * @param listScadaCanvasDTO 业务实体对象集合
     * @return String[] 主键数组
     */
    public static String[] toDTOPrimaryKeyArrays(final List<ScadaCanvasDTO> listScadaCanvasDTO) {
        if (CollectionUtils.isEmpty(listScadaCanvasDTO)) {
            return null;
        }
        int size = listScadaCanvasDTO.size();
        String[] canvasIdArrays = new String[size];
        for (int i = 0; i < size; i++) {
            canvasIdArrays[i] = listScadaCanvasDTO.get(i).getId();
        }
        return canvasIdArrays;
    }

}
