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
 * 类名称:    ScadaBindValueRequest.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/10/26 16:30
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScadaBindValueRequest {
   private List<ScadaBindVO> bindObjList;
}
