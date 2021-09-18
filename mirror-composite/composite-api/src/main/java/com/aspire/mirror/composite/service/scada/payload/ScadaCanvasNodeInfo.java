package com.aspire.mirror.composite.service.scada.payload;


import java.util.List;

/**
 * 用于存储视图节点信息
 */
public class ScadaCanvasNodeInfo {

    private String id;
    private String label;
    private List<ScadaCanvasNodeInfo> children;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<ScadaCanvasNodeInfo> getChildren() {
        return children;
    }

    public void setChildren(List<ScadaCanvasNodeInfo> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "ScadaCanvasNodeInfo{" +
                "id='" + id + '\'' +
                ", label='" + label + '\'' +
                ", children=" + children +
                '}';
    }
}
