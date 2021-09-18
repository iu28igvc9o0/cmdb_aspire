package com.aspire.mirror.composite.service.scada.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.scada.payload
 * 类名称:    ScadaBindVO.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/10/26 16:33
 * 版本:      v1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScadaBindVO {
   private Boolean valueView;

   private Boolean colorView;

   private Boolean isBind;

   private Integer bandType;

   private List<SillVO> sillList;

   private String countType;

   private String id;

   private List<String> alertLevelList;

   private List<Map<String, String>> deviceList;

   private List<Map<String, String>> itemList;

   private String name;

   private String unit;

   private String conversionType;

   private String conversionVal;
}
