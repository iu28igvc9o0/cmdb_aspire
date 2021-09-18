package com.aspire.mirror.scada.dao;


import com.aspire.mirror.common.entity.Page;
import com.aspire.mirror.scada.api.dto.model.ScadaCanvasDTO;
import com.aspire.mirror.scada.dao.po.ScadaCanvas;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author JinSu
 */
@Mapper
public interface ScadaCanvasDao {
    /**
     * 新增视图
     */
    int insert(ScadaCanvas scadaCanvas);

    /**
     * 根据主键删除视图
     */
    void deleteByPrimaryKey(@Param(value = "id") String id);

    /**
     * 根据主键更新视图
     */
    int updateByPrimaryKey(ScadaCanvas scadaCanvas);

    /**
     * 根据主键查询视图详情
     */
    ScadaCanvas selectByPrimaryKey(@Param(value = "id") String id);

    /**
     * 根据主键数组查询视图
     *
     * @param canvasIdArrays 主键数组
     * @return List<Canvas> 返回集合对象
     */
    List<ScadaCanvas> selectByPrimaryKeyArrays(String[] canvasIdArrays);

//    /**
//     * 根据监控对象Id获取视图详情
//     *
//     * @param precinctId
//     * @param pictureType
//     * @return
//     */
//    ScadaCanvas getScadaCanvasInfoByPrecinctId(@Param(value = "precinctId") String precinctId,
//                                               @Param(value = "pictureType") Integer pictureType);

    /**
     * 查询所有画布
     *
     * @return
     */
    List<ScadaCanvas> findScadaCanvasList(ScadaCanvas scadaCanvas);

    ScadaCanvas findScadaCanvasByName(@Param(value = "name") String name);

    int pageListCount(Page page);

    List<ScadaCanvas> pageList(Page page);


//    /**
//     * 根据节点名称模糊匹配，找出对应的节点id
//     *
//     */
//    List<Map<String,Object>> findPrecinctIdByPrecinctName(@Param("precinctName") String precinctName);
}