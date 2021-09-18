package com.aspire.mirror.alert.api.dto.third;

import java.util.List;

import com.google.common.collect.Lists;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public enum SubjectsEnum {
	CPU_USAGE("CPU_USAGE","cpu占用"),
	MEM_USAGE("MEM_USAGE","内存占用"),
	NET_FLOW_USAGE("NET_FLOW_USAGE","流量占用"),
	PORTCHANGE("PORTCHANGE","端口变化"),
	ROLECHANGE("ROLECHANGE","控制器角色变化"),
	AUTHOREXPIRED("AUTHOREXPIRED","授权过期"),
	DEVICESTATUSCHANGE("DEVICESTATUSCHANGE","设备状态变化"),
	DEVICE_CONF_CONFLICT("DEVICE_CONF_CONFLICT","设备配置冲突"),
	OTHERS("OTHERS","其他");

    @Getter
    private String code;

    /**
     * 样式
     */
    @Getter
    private String name;
   

    public static String getSubjectNameByCode (String itemCode) {
        for (SubjectsEnum topoEnum : SubjectsEnum.values()) {
            if (topoEnum.getCode().equals(itemCode)) {
                return topoEnum.getName();
            }
        }
        return OTHERS.getName();
    }
    
    public static List<String> getSubjectNameByCode () {
    	List<String> list = Lists.newArrayList();
    	for (SubjectsEnum topoEnum : SubjectsEnum.values()) {
    		list.add(topoEnum.getCode());
        }
    	return list;
    }
    
    @Override
    public String toString() {
    	StringBuffer sb = new StringBuffer();
    	sb.append("code:").append(code).append("    desc:").append(name);
        return sb.toString();
    }
}
