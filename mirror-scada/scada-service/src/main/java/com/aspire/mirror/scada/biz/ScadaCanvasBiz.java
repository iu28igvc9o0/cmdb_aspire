package com.aspire.mirror.scada.biz;


import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.scada.api.dto.model.ScadaCanvasDTO;

import java.util.List;

/**
 * 业务逻辑层接口
 * <p>
 * 项目名称: 微服务运维平台
 * 包:     com.aspire.mirror.scada.biz   
 * 类名称:     CanvasBiz
 * 类描述:     业务逻辑层接口
 * 创建人:     pengfeng
 * 创建时间:     2019-05-24 11:32:23
 */
public interface ScadaCanvasBiz {

	/**
     * 新增视图
     * @param scadaCanvasDTO 动作DTO对象
     * @return String 数据ID
     */
    String insert(ScadaCanvasDTO scadaCanvasDTO);
	
	 /**
     * 根据主键删除视图
     * @param canvasId 主键
     * @return int 删除数据条数
     */
    void deleteByPrimaryKey(String canvasId);
    
    /**
     * 根据主键更新视图
     * @param scadaCanvasDTO 动作DTO对象
     * @return int 数据条数
     */
    int updateByPrimaryKey(ScadaCanvasDTO scadaCanvasDTO);
    
    /**
     * 根据主键视图
     * @param canvasId 主键
     * @return CanvasDTO 返回对象
     */
    ScadaCanvasDTO selectByPrimaryKey(String canvasId);

//    ResMap getScadaCanvasInfoByPrecinctId(String precinctId, Integer pictureType);

    List<ScadaCanvasDTO> findScadaCanvasList( ScadaCanvasDTO scadaCanvasDTO);


    //特殊任务，将文件名和节点id关联
//    String batchFindPrecinctId();


    //特殊任务，视图批量插入数据库
//    String batchSaveScadaCanvas() throws Exception;

    ScadaCanvasDTO findScadaCanvasByName(String name);

    PageResponse<ScadaCanvasDTO> pageList(PageRequest page);
}
