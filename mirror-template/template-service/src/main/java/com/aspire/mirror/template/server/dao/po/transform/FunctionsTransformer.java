package com.aspire.mirror.template.server.dao.po.transform;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.util.CollectionUtils;

import com.aspire.mirror.template.server.dao.po.Functions;
import com.aspire.mirror.template.api.dto.model.FunctionsDTO;

import java.util.List;
import java.util.Map;

/**
 * monitor_functions对象转换类
 *
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.template.server.dao.po.transform
 * 类名称:    FunctionsTransformer.java
 * 类描述:    monitor_functions对应的PO与DTO的转换类
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 12:14:48
 */
public final class FunctionsTransformer  {

    private FunctionsTransformer(){
    }

   /**
    * 将monitor_functionsPO实体转换为monitor_functionsDTO实体
    *
    * @param  functions monitor_functionsPO实体
    * @return FunctionsDTO monitor_functionsDTO实体
    */
    public static FunctionsDTO fromPo(final Functions functions) {
        if (null == functions) {
            return null;
        }
        
        FunctionsDTO functionsDTO = new FunctionsDTO();
        functionsDTO.setFunctionId(functions.getFunctionId());
        functionsDTO.setItemId(functions.getItemId());
        functionsDTO.setTriggerId(functions.getTriggerId());
        functionsDTO.setFunction(functions.getFunction());
        functionsDTO.setParameter(functions.getParameter());

        return functionsDTO;
    }

    /**
     * 将monitor_functions业务实体对象集合转换为monitor_functions持久化对象集合
     *
     * @param listFunctions monitor_functions业务实体对象集合
     * @return List<FunctionsDTO> monitor_functions持久化对象集合
     */
    public static List<FunctionsDTO> fromPo(final List<Functions> listFunctions) {
        if (CollectionUtils.isEmpty(listFunctions)) {
            return Lists.newArrayList();
        }
        List<FunctionsDTO> listFunctionsDTO = Lists.newArrayList();

        for (Functions functions : listFunctions) {
            listFunctionsDTO.add(FunctionsTransformer.fromPo(functions));
        }
        return listFunctionsDTO;
    }

   /**
    * 将monitor_functionsDTO实体转换为monitor_functionsPO实体
    *
    * @param  functionsDTO monitor_functionsDTO实体类
    * @return Functions monitor_functionsPO实体
    */
    public static Functions toPo(final FunctionsDTO functionsDTO) {
        if (null == functionsDTO) {
            return null;
        }

        Functions functions = new Functions();
        functions.setFunctionId(functionsDTO.getFunctionId());
        functions.setItemId(functionsDTO.getItemId());
        functions.setTriggerId(functionsDTO.getTriggerId());
        functions.setFunction(functionsDTO.getFunction());
        functions.setParameter(functionsDTO.getParameter());

        return functions;
    }

    /**
     * 将monitor_functions业务实体对象集合转换为monitor_functions持久化对象集合
     *
     * @param listFunctionsDTO monitor_functions业务实体对象集合
     * @return List<Functions> monitor_functions持久化对象集合
     */
    public static List<Functions> toPo(final List<FunctionsDTO> listFunctionsDTO) {
        if (CollectionUtils.isEmpty(listFunctionsDTO)) {
            return Lists.newArrayList();
        }
        List<Functions> listFunctions = Lists.newArrayList();

        for (FunctionsDTO functionsdTO : listFunctionsDTO) {
            listFunctions.add(FunctionsTransformer.toPo(functionsdTO));
        }
        return listFunctions;
    }
    /**
     * 将monitor_functions业务实体对象集合转换为Map
     *
     * @param listFunctionsDTO monitor_functions业务实体对象集合
     * @return Map<String, FunctionsDTO> Map[key=String|value=FunctionsDTO]
     */
    public static Map<String, FunctionsDTO> toDTOMap(final List<FunctionsDTO> listFunctionsDTO) {
        if (CollectionUtils.isEmpty(listFunctionsDTO)) {
            return Maps.newHashMap();
        }
        Map<String, FunctionsDTO> functionsDTOMaps = Maps.newHashMap();

        for (FunctionsDTO functionsDTO : listFunctionsDTO) {
            functionsDTOMaps.put(functionsDTO.getFunctionId(), functionsDTO);
        }
        return functionsDTOMaps;
    }

    /**
     * 将monitor_functions业务实体对象集合中的主键进行提取并封装成数组
     *
     * @param listFunctionsDTO monitor_functions业务实体对象集合
     * @return String[] 主键数组
     */
    public static String[] toDTOPrimaryKeyArrays(final List<FunctionsDTO> listFunctionsDTO) {
        if (CollectionUtils.isEmpty(listFunctionsDTO)) {
            return null;
        }
        int size = listFunctionsDTO.size();
        String[] functionIdArrays = new String[size];
        for (int i = 0 ;i< size; i++) {
            functionIdArrays[i] = listFunctionsDTO.get(i).getFunctionId();
        }
        return functionIdArrays;
        }
} 
