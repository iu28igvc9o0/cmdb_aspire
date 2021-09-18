package com.migu.tsg.microservice.atomicservice.composite.enums;

import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public enum TopoEnum {

    FIRE_WALL(1, "防火墙","shape=image;needInputCode=true;warning=true;type=a;labelPosition=left;verticalLabelPosition=middle;whiteSpace=wrap;aspect=fixed;align=right;labelBackgroundColor=#ffffff;image=stencils/editor/firewall/a_firewall.svg;dashed=1;strokeWidth=1;fontSize=12;", 100, 100, 150),
    ROUTER(2, "路由器","shape=image;needInputCode=true;warning=true;type=core;labelPosition=left;verticalLabelPosition=middle;whiteSpace=wrap;aspect=fixed;align=right;labelBackgroundColor=#ffffff;image=stencils/editor/router/core_router.svg;dashed=1;strokeWidth=1;fontSize=12;", 100, 200, 150),
    F5(3, "负载均衡设备","shape=image;needInputCode=true;warning=true;type=normal;labelPosition=left;verticalLabelPosition=middle;whiteSpace=wrap;aspect=fixed;align=right;labelBackgroundColor=#ffffff;image=stencils/editor/loadBalance/normal_f5.svg", 100, 300,150),
    SWITCH(4, "交换机","shape=image;needInputCode=true;warning=true;type=access;labelPosition=left;verticalLabelPosition=middle;whiteSpace=wrap;aspect=fixed;align=right;labelBackgroundColor=#ffffff;image=stencils/editor/switch/access_switch.svg", 100, 400,150),
    ORTER(5, "其他","shape=image;needInputCode=true;warning=true;type=gyzl;labelPosition=left;verticalLabelPosition=middle;whiteSpace=wrap;aspect=fixed;align=right;labelBackgroundColor=#ffffff;image=stencils/editor/switch/unknow_device.svg", 100, 500, 150),;

    @Getter
    private Integer lineIndex;

    @Getter
    private String name;

    /**
     * 样式
     */
    @Getter
    private String style;
    /**
     * x开始位置
     */
    @Getter
    private double x;

    /**
     * y开始位置
     */
    @Getter
    private double y;

    /**
     * x增量
     */
    @Getter
    private double xStep;

    public static TopoEnum getTopoEnumByName (String itemName) {
        for (TopoEnum topoEnum : TopoEnum.values()) {
            if (topoEnum.getName().equals(itemName)) {
                return topoEnum;
            }
        }
        log.warn("@@@@@发现未定义设备类型@@@@，" + itemName);
        return ORTER;
    }

    public static List<TopoEnum> getTopoEnumList () {
        return Arrays.asList(TopoEnum.values());
    }
    public static TopoEnum getUpperTopoEnumByName (String name) {
        TopoEnum topoEnum = getTopoEnumByName(name);
        return getUpperTopoEnumByIndex(topoEnum.getLineIndex());
    }
    public static TopoEnum getUpperTopoEnumByIndex (Integer index) {
        for (TopoEnum topoEnum : TopoEnum.values()) {
            if (topoEnum.getLineIndex().equals(index-1)) {
                return topoEnum;
            }
        }
        return null;
    }
}
