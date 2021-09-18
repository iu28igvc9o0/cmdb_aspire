package com.migu.tsg.microservice.atomicservice.composite.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.elasticsearch.api.dto.LldpData;
import com.aspire.mirror.elasticsearch.api.dto.LldpInfo;
import com.aspire.mirror.elasticsearch.api.dto.PeerDeviceInfo;
import com.aspire.ums.cmdb.instance.payload.CmdbInstance;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.instance.InstanceClient;
import com.migu.tsg.microservice.atomicservice.composite.enums.TopoEnum;
import com.mxgraph.io.mxCodec;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.util.mxPoint;
import com.mxgraph.util.mxXmlUtils;
import com.mxgraph.view.mxGraph;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Sets;
import org.springframework.util.StringUtils;

import java.util.*;

//import javax.swing.text.Document;

/**
 * 物理拓扑解析
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.composite.controller
 * 类名称:    LLDPPhysicalTopology.java
 * 类描述:    物理拓扑解析
 * 创建人:    JinSu
 * 创建时间:  2019/9/19 13:52
 * 版本:      v1.0
 */
@Slf4j
public class LLDPPhysicalTopology {
    private static final String TEXT_STYLE = "text;html=1;resizable=0;points=[];align=center;verticalAlign=middle;" +
            "labelBackgroundColor=#ffffff;";

    static public String analysisPhysicalTopology(List<LldpInfo> listLldpInfo, Map<String, String> ipMap, InstanceClient
            instanceClient, Integer maxNum) {
        List<String> unknownDevice = new ArrayList<>();
        mxGraph graph = new mxGraph();
//       mxGraphModel mxGraphModel = new mxGraphModel();
//       graph.setModel(mxGraphModel);
        int i = 0;
        //存放已生成的node
        Map<String, Integer> hostMap = Maps.newHashMap();
        Set<String> lineSet = Sets.newHashSet();
        mxCell root = new mxCell();
        root.setId(String.valueOf(i++));
        root.setStyle("pagetype=0");
        mxCell parent = new mxCell();
        parent.setId(String.valueOf(i++));
        parent.setParent(root);
        root.insert(parent);
        double cellx = 0, celly = 0, width = 48, height = 48, stepY = 100;
        Map<String, Pair<Double, Double>> nodeTypNumMap = Maps.newHashMap();

        Map<String, Set<String>> topoItemNumMap = Maps.newLinkedHashMap();
        for (TopoEnum topoEnum : TopoEnum.getTopoEnumList()) {
            topoItemNumMap.put(topoEnum.getName(), Sets.newHashSet());
        }
        //入节点数据前计算每种类型的y轴位置
        for (LldpInfo lldpInfo : listLldpInfo) {
            TopoEnum topoEnum = getTopoEnum(ipMap, instanceClient, lldpInfo, unknownDevice);
            if (topoEnum == null) {
                continue;
            }
            // 设置
            topoItemNumMap.get(topoEnum.getName()).add(lldpInfo.getHost());
            for (LldpData lldpData : lldpInfo.getLldpData()) {
                //关联设备cell
                PeerDeviceInfo peerDeviceInfo = lldpData.getPeerDeviveInfo();
                if (hostMap.get(peerDeviceInfo.getPeerHost()) == null) {
                    topoEnum = getTopoEnum(ipMap, instanceClient, lldpInfo, peerDeviceInfo, unknownDevice);
                    if (topoEnum == null) {
                        continue;
                    }
                    topoItemNumMap.get(topoEnum.getName()).add(lldpData.getPeerDeviveInfo().getPeerHost());
                }
            }
        }
        for (String name : topoItemNumMap.keySet()) {
            Double y = Double.valueOf(100);
            if (TopoEnum.getUpperTopoEnumByName(name) != null) {
                TopoEnum upperTopoEnpum = TopoEnum.getUpperTopoEnumByName(name);
                if (nodeTypNumMap.get(upperTopoEnpum.getName()) != null) {
                    Integer cellStepY = topoItemNumMap.get(upperTopoEnpum.getName()).size() / maxNum * 100;
                    y = nodeTypNumMap.get(upperTopoEnpum.getName()).getValue() + (topoItemNumMap.get
                            (upperTopoEnpum.getName()).size() % maxNum == 0 ? cellStepY : (cellStepY + 100));
                }
            }
            if (y >= TopoEnum.getTopoEnumByName(name).getY()) {
                nodeTypNumMap.put(name, new MutablePair<>(TopoEnum.getTopoEnumByName(name).getX(), y));
            } else {
                nodeTypNumMap.put(name, new MutablePair<>(TopoEnum.getTopoEnumByName(name).getX(), TopoEnum
                        .getTopoEnumByName(name).getY()));
            }

        }
        for (LldpInfo lldpInfo : listLldpInfo) {
            //拼装本设备实体拓扑图形
            mxCell cell;
            TopoEnum topoEnum = getTopoEnum(ipMap, instanceClient, lldpInfo, unknownDevice);
            if (topoEnum == null) continue;

            if (!hostMap.containsKey(lldpInfo.getHost())) {
//                if (nodeTypNumMap.get(topoEnum.getName()) != null) {
//                    cellx = topoEnum.getX();
//                    celly = topoEnum.getY();
//                }
//                else {
                Pair<Double, Double> pair = nodeTypNumMap.get(topoEnum.getName());
                if (pair.getKey() != topoEnum.getX() && (pair.getKey() - topoEnum.getX()) % (topoEnum.getXStep()
                        * maxNum) == 0) {
                    cellx = topoEnum.getX();
                    celly = pair.getValue() + stepY;
                } else {
                    cellx = pair.getKey();
                    celly = pair.getValue();
                }
//                }
                nodeTypNumMap.put(topoEnum.getName(), new MutablePair(cellx + topoEnum.getXStep(), celly));
                cell = new mxCell();
                cell.setId(String.valueOf(i++));
                cell.setValue(lldpInfo.getHost());
                cell.setStyle(topoEnum.getStyle());
//            cell.setParent(parent);
                cell.setVertex(true);
                //添加hostSet以便筛除
                mxGeometry geometry = new mxGeometry(cellx, celly, width, height);
                cell.setGeometry(geometry);
                //添加本设备实体图形至画布
                int index = parent.getChildCount();
                parent.insert(cell);
                hostMap.put(lldpInfo.getHost(), index);
            } else {
                cell = (mxCell) parent.getChildAt(hostMap.get(lldpInfo.getHost()));
                cellx = cell.getGeometry().getX();
                celly = cell.getGeometry().getY();
            }
            for (LldpData lldpData : lldpInfo.getLldpData()) {
                //关联设备cell
                PeerDeviceInfo peerDeviceInfo = lldpData.getPeerDeviveInfo();
                mxCell target;
                double targetx = 0, targety = 0;
                if (hostMap.get(peerDeviceInfo.getPeerHost()) == null) {
                    topoEnum = getTopoEnum(ipMap, instanceClient, lldpInfo, peerDeviceInfo, unknownDevice);
                    if (topoEnum == null) continue;
                    Pair<Double, Double> pair = nodeTypNumMap.get(topoEnum.getName());
                    if (pair.getKey() != topoEnum.getX() && (pair.getKey() - topoEnum.getX()) % (topoEnum
                            .getXStep() * maxNum) == 0) {
                        targetx = topoEnum.getX();
                        targety = pair.getValue() + stepY;
                    } else {
                        targetx = pair.getKey();
                        targety = pair.getValue();
                    }
//                    }
                    nodeTypNumMap.put(topoEnum.getName(), new MutablePair(targetx + topoEnum.getXStep(), targety));
                    target = new mxCell();
                    target.setId(String.valueOf(i++));
                    target.setStyle(topoEnum.getStyle());
                    target.setValue(peerDeviceInfo.getPeerHost());
                    //                target.setParent(parent);
                    target.setVertex(true);
                    mxGeometry targetGeometry = new mxGeometry(targetx, targety, width, height);
                    target.setGeometry(targetGeometry);
                    int index = parent.getChildCount();
                    parent.insert(target);
                    hostMap.put(peerDeviceInfo.getPeerHost(), index);
                } else {
                    target = (mxCell) parent.getChildAt(hostMap.get(peerDeviceInfo.getPeerHost()));
                    targetx = target.getGeometry().getX();
                    targety = target.getGeometry().getY();
                }

                //添加连接线条cell，当两头中有一头节点不存在，则画线条，两头节点数据之前就存在不划线
                if (!lineSet.contains(lldpInfo.getHost() + "to" + peerDeviceInfo.getPeerHost()) && !lineSet.contains
                        (peerDeviceInfo.getPeerHost() + "to" + lldpInfo.getHost())) {
                    mxCell line = new mxCell();
                    line.setId(String.valueOf(i++));
                    List<String> styleList = Lists.newArrayList();

//                    Map<String, String> dataMap = Maps.newHashMap();
//                    LineCellInfo source = new LineCellInfo(lldpInfo.getHost(), lldpData.getPortDesc(), lldpInfo
//                            .getDeviceBrand(), lldpInfo.getDeviceModel());
                    JSONObject source = new JSONObject();
                    source.put("ip", lldpInfo.getHost());
                    source.put("hostname", lldpInfo.getHostname());
                    source.put("portDesc", lldpData.getPortDesc());
                    source.put("deviceBrand", lldpInfo.getDeviceBrand());
                    source.put("deviceModel", lldpInfo.getDeviceModel());

                    JSONObject targetObj = new JSONObject();
                    targetObj.put("ip", peerDeviceInfo.getPeerHost());
                    targetObj.put("hostname", peerDeviceInfo.getPeerHostname());
                    targetObj.put("portDesc", peerDeviceInfo.getPeerPortDesc());
                    targetObj.put("deviceBrand", peerDeviceInfo.getPeerDeviceBrand());
                    targetObj.put("deviceModel", peerDeviceInfo.getPeerDeviceModel());

//                    dataMap.put()
//                    dataMap.put("source", JSON.toJSONString(source));
//                    dataMap.put("target", JSON.toJSONString(targetObj));
                    styleList.add("endArrow=none;type=edge;html=1");
                    styleList.add("source=" + JSON.toJSONString(source));
                    styleList.add("target=" + JSON.toJSONString(targetObj));
                    styleList.add("online=" + true);
                    line.setStyle(Joiner.on(";").join(styleList));
//                line.setParent(parent);
                    line.setVertex(true);
                    line.setSource(cell);
                    line.setTarget(target);
                    line.setEdge(true);
                    mxGeometry lineGeometry = new mxGeometry();
                    lineGeometry.setWidth(50);
                    lineGeometry.setHeight(50);
                    lineGeometry.setRelative(true);
                    mxPoint targetPoint = new mxPoint(targetx, targety);
                    mxPoint sourcePoint = new mxPoint(cellx, celly);
                    lineGeometry.setTargetPoint(targetPoint);
                    lineGeometry.setSourcePoint(sourcePoint);
                    line.setGeometry(lineGeometry);
                    lineSet.add(lldpInfo.getHost() + "to" + peerDeviceInfo.getPeerHost());
                    parent.insert(line);

                    //添加target的端口
//                    mxCell targetText = new mxCell();
//                    targetText.setStyle(TEXT_STYLE);
//                    targetText.setValue(peerDeviceInfo.getPeerPortDesc());
//                    targetText.setVertex(true);
//                    targetText.setConnectable(false);
//                    mxGeometry targetTextGeometry = new mxGeometry();
//                    targetTextGeometry.setX(0.7365);
//                    targetTextGeometry.setRelative(true);
//                    mxPoint targetTextPoint = new mxPoint();
//                    List<mxPoint> mxPointList = Lists.newArrayList();
//                    mxPointList.add(targetTextPoint);
//                    targetTextGeometry.setPoints(mxPointList);
//                    targetText.setGeometry(targetTextGeometry);
//                    line.insert(targetText);
//
//                    //添加source的端口
//                    mxCell sourceText = new mxCell();
//                    sourceText.setStyle(TEXT_STYLE);
//                    sourceText.setValue(lldpData.getPortDesc());
//                    sourceText.setVertex(true);
//                    sourceText.setConnectable(false);
//                    mxGeometry sourceTextGeometry = new mxGeometry();
//                    sourceTextGeometry.setX(-0.7365);
//                    sourceTextGeometry.setRelative(true);
//                    mxPoint sourceTextPoint = new mxPoint();
//                    List<mxPoint> mxPointList1 = Lists.newArrayList();
//                    mxPointList1.add(sourceTextPoint);
//                    sourceTextGeometry.setPoints(mxPointList1);
//                    sourceText.setGeometry(sourceTextGeometry);
//                    line.insert(sourceText);
                }
            }
        }
        mxCodec codec = new mxCodec();
        mxGraphModel model = (mxGraphModel) graph.getModel();
        model.setRoot(root);
        return "\"" + StringEscapeUtils.escapeJava(mxXmlUtils.getXml(codec.encode(model))) + "\"";
    }

    private static TopoEnum getTopoEnum(Map<String, String> ipMap, InstanceClient instanceClient, LldpInfo lldpInfo,
                                        PeerDeviceInfo peerDeviceInfo, List<String> unknownDevice) {
        TopoEnum topoEnum;
        topoEnum = !StringUtils.isEmpty(ipMap.get(peerDeviceInfo.getPeerHost())) ? TopoEnum
                .getTopoEnumByName
                        (ipMap.get(peerDeviceInfo.getPeerHost())) : !StringUtils.isEmpty(peerDeviceInfo
                .getPeerDeviceType()) ? TopoEnum.getTopoEnumByName(peerDeviceInfo
                .getPeerDeviceType()) : null;
        if (topoEnum == null) {
            if (unknownDevice.contains(lldpInfo.getPoolname()+peerDeviceInfo.getPeerHost())) {
                topoEnum = TopoEnum.ORTER;
                return topoEnum;
            }
            Map<String, Object> queryParams = new HashMap<>();
            queryParams.put("ip", peerDeviceInfo.getPeerHost());
            queryParams.put("idcType", lldpInfo.getPoolname());
            queryParams.put("token", "inner_antomate");
            Map<String, Object> instanceDetail = instanceClient.queryInstanceDetail(queryParams);
            if (!MapUtils.isEmpty(instanceDetail)) {
                if ("服务器".equals(instanceDetail.get("device_class"))) {
                    return null;
                } else {
                    log.warn("@@@@@@发现非CMDB安全和网络设备@@@@@@, 设备ip:{}, 设备类型：{}", instanceDetail.get("ip"),
                            instanceDetail.get("device_type"));
                }
            }
            topoEnum = TopoEnum.ORTER;
            unknownDevice.add(lldpInfo.getPoolname()+peerDeviceInfo.getPeerHost());
        }
        return topoEnum;
    }
    private static TopoEnum getTopoEnum(Map<String, String> ipMap, InstanceClient instanceClient, LldpInfo lldpInfo, List<String> unknownDevice) {
        // cmdb中存在lldp节点数据时，设备类型以CMDB安全设备和网络设备为准，否则以lldp数据的设备类型
        TopoEnum topoEnum = !StringUtils.isEmpty(ipMap.get(lldpInfo.getHost())) ? TopoEnum.getTopoEnumByName
                (ipMap.get(lldpInfo.getHost())) : !StringUtils.isEmpty(lldpInfo
                .getDeviceType()) ? TopoEnum.getTopoEnumByName(lldpInfo
                .getDeviceType()) : null;
        // 如果lldp也没有设备类型，则根据资源池和ip查找cmdb数据，如果是服务器直接忽略节点，其他情况直接生成位置设备节点
        if (topoEnum == null) {
            if (unknownDevice.contains(lldpInfo.getPoolname()+lldpInfo.getHost())) {
                topoEnum = TopoEnum.ORTER;
                return topoEnum;
            }
            Map<String, Object> queryParams = new HashMap<>();
            queryParams.put("ip", lldpInfo.getHost());
            queryParams.put("idcType", lldpInfo.getPoolname());
            queryParams.put("token", "inner_antomate");
            Map<String, Object> instanceDetail = instanceClient.queryInstanceDetail(queryParams);
            if (!MapUtils.isEmpty(instanceDetail)) {
                if ("服务器".equals(instanceDetail.get("device_class"))) {
                    log.warn("@@@@@发现服务器设备，直接不生成节点！");
                    return null;
                } else {
                    log.warn("@@@@@@发现非CMDB安全和网络设备@@@@@@, 设备ip:{}, 设备类型：{}", instanceDetail.get("ip"),
                            instanceDetail.get("device_type"));
                }
            }
            unknownDevice.add(lldpInfo.getPoolname()+lldpInfo.getHost());
            topoEnum = TopoEnum.ORTER;
        }
        return topoEnum;
    }

    public static void main(String[] args) {
        LldpInfo lldpInfo = new LldpInfo();
        lldpInfo.setHost("1.1.1.41");
        lldpInfo.setHostname("ASP-H3C-S9000-41");
        lldpInfo.setDeviceType("交换机");
        lldpInfo.setDeviceBrand("H3C");
        lldpInfo.setIdccode("xxg");
        lldpInfo.setPoolname("信息港");
        List<LldpData> lldpDataList = Lists.newArrayList();
        LldpData lldpData = new LldpData();
        lldpData.setPortIndex("1");
        lldpData.setPortDesc("G1/0/1");
        PeerDeviceInfo peerDeviceInfo = new PeerDeviceInfo();
        peerDeviceInfo.setPeerDeviceBrand("H3C");
        peerDeviceInfo.setPeerPortIndex("10");
        peerDeviceInfo.setPeerDeviceModel("S9000");
        peerDeviceInfo.setPeerHost("1.1.1.1");
        peerDeviceInfo.setPeerPortDesc("G1/0/10");
        peerDeviceInfo.setPeerDeviceType("交换机");
        lldpData.setPeerDeviveInfo(peerDeviceInfo);
        lldpDataList.add(lldpData);
        lldpInfo.setLldpData(lldpDataList);


        LldpInfo lldpInfo2 = new LldpInfo();
        lldpInfo2.setHost("1.1.1.1");
        lldpInfo2.setHostname("ASP-H3C-S9000-41");
        lldpInfo2.setDeviceType("交换机");
        lldpInfo2.setDeviceBrand("H3C");
        lldpInfo2.setIdccode("xxg");
        lldpInfo2.setPoolname("信息港");
        List<LldpData> lldpDataList2 = Lists.newArrayList();
        LldpData lldpData2 = new LldpData();
        lldpData2.setPortIndex("1");
        lldpData2.setPortDesc("G1/0/1");
        PeerDeviceInfo peerDeviceInfo2 = new PeerDeviceInfo();
        peerDeviceInfo2.setPeerDeviceBrand("H3C");
        peerDeviceInfo2.setPeerPortIndex("10");
        peerDeviceInfo2.setPeerDeviceModel("S9000");
        peerDeviceInfo2.setPeerHost("1.1.1.41");
        peerDeviceInfo2.setPeerPortDesc("G1/0/10");
        peerDeviceInfo2.setPeerDeviceType("交换机");
        lldpData2.setPeerDeviveInfo(peerDeviceInfo2);

        LldpData lldpData3 = new LldpData();
        lldpData3.setPortIndex("1");
        lldpData3.setPortDesc("G1/0/1");
        PeerDeviceInfo peerDeviceInfo3 = new PeerDeviceInfo();
        peerDeviceInfo3.setPeerDeviceBrand("H3C");
        peerDeviceInfo3.setPeerPortIndex("10");
        peerDeviceInfo3.setPeerDeviceModel("S9000");
        peerDeviceInfo3.setPeerHost("1.1.1.44");
        peerDeviceInfo3.setPeerPortDesc("G1/0/10");
        peerDeviceInfo3.setPeerDeviceType("交换机");
        lldpData3.setPeerDeviveInfo(peerDeviceInfo3);

        lldpDataList2.add(lldpData2);
        lldpDataList2.add(lldpData3);
        lldpInfo2.setLldpData(lldpDataList2);

        List<LldpInfo> lldpInfoList = Lists.newArrayList();
        lldpInfoList.add(lldpInfo);
        lldpInfoList.add(lldpInfo2);

        String aa = StringEscapeUtils.escapeJava("\"<mxGraphModel><root><mxCell id=\"0\" " +
                "style=\"pagetype=0\"/><mxCell " +
                "id=\"1\" parent=\"0\"/><mxCell id=\"2\" parent=\"1\" style=\"shape=image;needInputCode=true;" +
                "warning=true;type=access;labelPosition=left;verticalLabelPosition=middle;whiteSpace=wrap;" +
                "aspect=fixed;align=right;labelBackgroundColor=#ffffff;image=stencils/editor/switch/access_switch" +
                ".svg\" value=\"10.1.1.120\" vertex=\"1\"><mxGeometry as=\"geometry\" height=\"48.0\" width=\"48.0\" " +
                "x=\"100.0\" y=\"500.0\"/></mxCell><mxCell id=\"3\" parent=\"1\" style=\"shape=image;" +
                "needInputCode=true;warning=true;type=gyzl;labelPosition=left;verticalLabelPosition=middle;" +
                "whiteSpace=wrap;aspect=fixed;align=right;labelBackgroundColor=#ffffff;" +
                "image=stencils/editor/simulationPeiDian/gyzl_S.svg\" value=\"10.1.1.150\" vertex=\"1\"><mxGeometry " +
                "as=\"geometry\" height=\"48.0\" width=\"48.0\" x=\"100.0\" y=\"700.0\"/></mxCell><mxCell edge=\"1\" " +
                "id=\"4\" parent=\"1\" source=\"2\" style=\"endArrow=none;type=edge;html=1;\" target=\"3\" " +
                "vertex=\"1\"><mxGeometry as=\"geometry\" height=\"50.0\" relative=\"1\" width=\"50.0\"><mxPoint " +
                "as=\"sourcePoint\" x=\"100.0\" y=\"500.0\"/><mxPoint as=\"targetPoint\" x=\"100.0\" " +
                "y=\"700.0\"/></mxGeometry></mxCell><mxCell id=\"5\" parent=\"1\" style=\"shape=image;" +
                "needInputCode=true;warning=true;type=gyzl;labelPosition=left;verticalLabelPosition=middle;" +
                "whiteSpace=wrap;aspect=fixed;align=right;labelBackgroundColor=#ffffff;" +
                "image=stencils/editor/simulationPeiDian/gyzl_S.svg\" value=\"10.1.1.100\" vertex=\"1\"><mxGeometry " +
                "as=\"geometry\" height=\"48.0\" width=\"48.0\" x=\"200.0\" y=\"700.0\"/></mxCell><mxCell edge=\"1\" " +
                "id=\"6\" parent=\"1\" source=\"2\" style=\"endArrow=none;type=edge;html=1;\" target=\"5\" " +
                "vertex=\"1\"><mxGeometry as=\"geometry\" height=\"50.0\" relative=\"1\" width=\"50.0\"><mxPoint " +
                "as=\"sourcePoint\" x=\"100.0\" y=\"500.0\"/><mxPoint as=\"targetPoint\" x=\"200.0\" " +
                "y=\"700.0\"/></mxGeometry></mxCell><mxCell id=\"7\" parent=\"1\" style=\"shape=image;" +
                "needInputCode=true;warning=true;type=access;labelPosition=left;verticalLabelPosition=middle;" +
                "whiteSpace=wrap;aspect=fixed;align=right;labelBackgroundColor=#ffffff;" +
                "image=stencils/editor/switch/access_switch.svg\" value=\"10.1.1.38\" vertex=\"1\"><mxGeometry " +
                "as=\"geometry\" height=\"48.0\" width=\"48.0\" x=\"200.0\" y=\"500.0\"/></mxCell><mxCell edge=\"1\" " +
                "id=\"8\" parent=\"1\" source=\"2\" style=\"endArrow=none;type=edge;html=1;\" target=\"7\" " +
                "vertex=\"1\"><mxGeometry as=\"geometry\" height=\"50.0\" relative=\"1\" width=\"50.0\"><mxPoint " +
                "as=\"sourcePoint\" x=\"100.0\" y=\"500.0\"/><mxPoint as=\"targetPoint\" x=\"200.0\" " +
                "y=\"500.0\"/></mxGeometry></mxCell><mxCell id=\"9\" parent=\"1\" style=\"shape=image;" +
                "needInputCode=true;warning=true;type=gyzl;labelPosition=left;verticalLabelPosition=middle;" +
                "whiteSpace=wrap;aspect=fixed;align=right;labelBackgroundColor=#ffffff;" +
                "image=stencils/editor/simulationPeiDian/gyzl_S.svg\" value=\"10.1.1.101\" vertex=\"1\"><mxGeometry " +
                "as=\"geometry\" height=\"48.0\" width=\"48.0\" x=\"300.0\" y=\"700.0\"/></mxCell><mxCell edge=\"1\" " +
                "id=\"10\" parent=\"1\" source=\"2\" style=\"endArrow=none;type=edge;html=1;\" target=\"9\" " +
                "vertex=\"1\"><mxGeometry as=\"geometry\" height=\"50.0\" relative=\"1\" width=\"50.0\"><mxPoint " +
                "as=\"sourcePoint\" x=\"100.0\" y=\"500.0\"/><mxPoint as=\"targetPoint\" x=\"300.0\" " +
                "y=\"700.0\"/></mxGeometry></mxCell><mxCell id=\"11\" parent=\"1\" style=\"shape=image;" +
                "needInputCode=true;warning=true;type=gyzl;labelPosition=left;verticalLabelPosition=middle;" +
                "whiteSpace=wrap;aspect=fixed;align=right;labelBackgroundColor=#ffffff;" +
                "image=stencils/editor/simulationPeiDian/gyzl_S.svg\" value=\"10.1.1.2\" vertex=\"1\"><mxGeometry " +
                "as=\"geometry\" height=\"48.0\" width=\"48.0\" x=\"400.0\" y=\"700.0\"/></mxCell><mxCell edge=\"1\" " +
                "id=\"12\" parent=\"1\" source=\"2\" style=\"endArrow=none;type=edge;html=1;\" target=\"11\" " +
                "vertex=\"1\"><mxGeometry as=\"geometry\" height=\"50.0\" relative=\"1\" width=\"50.0\"><mxPoint " +
                "as=\"sourcePoint\" x=\"100.0\" y=\"500.0\"/><mxPoint as=\"targetPoint\" x=\"400.0\" " +
                "y=\"700.0\"/></mxGeometry></mxCell><mxCell id=\"13\" parent=\"1\" style=\"shape=image;" +
                "needInputCode=true;warning=true;type=access;labelPosition=left;verticalLabelPosition=middle;" +
                "whiteSpace=wrap;aspect=fixed;align=right;labelBackgroundColor=#ffffff;" +
                "image=stencils/editor/switch/access_switch.svg\" value=\"10.1.1.56\" vertex=\"1\"><mxGeometry " +
                "as=\"geometry\" height=\"48.0\" width=\"48.0\" x=\"300.0\" y=\"500.0\"/></mxCell><mxCell edge=\"1\" " +
                "id=\"14\" parent=\"1\" source=\"7\" style=\"endArrow=none;type=edge;html=1;\" target=\"13\" " +
                "vertex=\"1\"><mxGeometry as=\"geometry\" height=\"50.0\" relative=\"1\" width=\"50.0\"><mxPoint " +
                "as=\"sourcePoint\" x=\"200.0\" y=\"500.0\"/><mxPoint as=\"targetPoint\" x=\"300.0\" " +
                "y=\"500.0\"/></mxGeometry></mxCell><mxCell id=\"15\" parent=\"1\" style=\"shape=image;" +
                "needInputCode=true;warning=true;type=access;labelPosition=left;verticalLabelPosition=middle;" +
                "whiteSpace=wrap;aspect=fixed;align=right;labelBackgroundColor=#ffffff;" +
                "image=stencils/editor/switch/access_switch.svg\" value=\"10.1.1.57\" vertex=\"1\"><mxGeometry " +
                "as=\"geometry\" height=\"48.0\" width=\"48.0\" x=\"400.0\" y=\"500.0\"/></mxCell><mxCell edge=\"1\" " +
                "id=\"16\" parent=\"1\" source=\"7\" style=\"endArrow=none;type=edge;html=1;\" target=\"15\" " +
                "vertex=\"1\"><mxGeometry as=\"geometry\" height=\"50.0\" relative=\"1\" width=\"50.0\"><mxPoint " +
                "as=\"sourcePoint\" x=\"200.0\" y=\"500.0\"/><mxPoint as=\"targetPoint\" x=\"400.0\" " +
                "y=\"500.0\"/></mxGeometry></mxCell><mxCell id=\"17\" parent=\"1\" style=\"shape=image;" +
                "needInputCode=true;warning=true;type=access;labelPosition=left;verticalLabelPosition=middle;" +
                "whiteSpace=wrap;aspect=fixed;align=right;labelBackgroundColor=#ffffff;" +
                "image=stencils/editor/switch/access_switch.svg\" value=\"10.1.1.45\" vertex=\"1\"><mxGeometry " +
                "as=\"geometry\" height=\"48.0\" width=\"48.0\" x=\"500.0\" y=\"500.0\"/></mxCell><mxCell edge=\"1\" " +
                "id=\"18\" parent=\"1\" source=\"7\" style=\"endArrow=none;type=edge;html=1;\" target=\"17\" " +
                "vertex=\"1\"><mxGeometry as=\"geometry\" height=\"50.0\" relative=\"1\" width=\"50.0\"><mxPoint " +
                "as=\"sourcePoint\" x=\"200.0\" y=\"500.0\"/><mxPoint as=\"targetPoint\" x=\"500.0\" " +
                "y=\"500.0\"/></mxGeometry></mxCell><mxCell id=\"19\" parent=\"1\" style=\"shape=image;" +
                "needInputCode=true;warning=true;type=access;labelPosition=left;verticalLabelPosition=middle;" +
                "whiteSpace=wrap;aspect=fixed;align=right;labelBackgroundColor=#ffffff;" +
                "image=stencils/editor/switch/access_switch.svg\" value=\"10.1.1.53\" vertex=\"1\"><mxGeometry " +
                "as=\"geometry\" height=\"48.0\" width=\"48.0\" x=\"600.0\" y=\"500.0\"/></mxCell><mxCell edge=\"1\" " +
                "id=\"20\" parent=\"1\" source=\"7\" style=\"endArrow=none;type=edge;html=1;\" target=\"19\" " +
                "vertex=\"1\"><mxGeometry as=\"geometry\" height=\"50.0\" relative=\"1\" width=\"50.0\"><mxPoint " +
                "as=\"sourcePoint\" x=\"200.0\" y=\"500.0\"/><mxPoint as=\"targetPoint\" x=\"600.0\" " +
                "y=\"500.0\"/></mxGeometry></mxCell><mxCell id=\"21\" parent=\"1\" style=\"shape=image;" +
                "needInputCode=true;warning=true;type=access;labelPosition=left;verticalLabelPosition=middle;" +
                "whiteSpace=wrap;aspect=fixed;align=right;labelBackgroundColor=#ffffff;" +
                "image=stencils/editor/switch/access_switch.svg\" value=\"10.1.1.54\" vertex=\"1\"><mxGeometry " +
                "as=\"geometry\" height=\"48.0\" width=\"48.0\" x=\"700.0\" y=\"500.0\"/></mxCell><mxCell edge=\"1\" " +
                "id=\"22\" parent=\"1\" source=\"7\" style=\"endArrow=none;type=edge;html=1;\" target=\"21\" " +
                "vertex=\"1\"><mxGeometry as=\"geometry\" height=\"50.0\" relative=\"1\" width=\"50.0\"><mxPoint " +
                "as=\"sourcePoint\" x=\"200.0\" y=\"500.0\"/><mxPoint as=\"targetPoint\" x=\"700.0\" " +
                "y=\"500.0\"/></mxGeometry></mxCell><mxCell id=\"23\" parent=\"1\" style=\"shape=image;" +
                "needInputCode=true;warning=true;type=gyzl;labelPosition=left;verticalLabelPosition=middle;" +
                "whiteSpace=wrap;aspect=fixed;align=right;labelBackgroundColor=#ffffff;" +
                "image=stencils/editor/simulationPeiDian/gyzl_S.svg\" value=\"10.1.1.62\" vertex=\"1\"><mxGeometry " +
                "as=\"geometry\" height=\"48.0\" width=\"48.0\" x=\"500.0\" y=\"700.0\"/></mxCell><mxCell edge=\"1\" " +
                "id=\"24\" parent=\"1\" source=\"7\" style=\"endArrow=none;type=edge;html=1;\" target=\"23\" " +
                "vertex=\"1\"><mxGeometry as=\"geometry\" height=\"50.0\" relative=\"1\" width=\"50.0\"><mxPoint " +
                "as=\"sourcePoint\" x=\"200.0\" y=\"500.0\"/><mxPoint as=\"targetPoint\" x=\"500.0\" " +
                "y=\"700.0\"/></mxGeometry></mxCell><mxCell id=\"25\" parent=\"1\" style=\"shape=image;" +
                "needInputCode=true;warning=true;type=access;labelPosition=left;verticalLabelPosition=middle;" +
                "whiteSpace=wrap;aspect=fixed;align=right;labelBackgroundColor=#ffffff;" +
                "image=stencils/editor/switch/access_switch.svg\" value=\"10.1.1.52\" vertex=\"1\"><mxGeometry " +
                "as=\"geometry\" height=\"48.0\" width=\"48.0\" x=\"800.0\" y=\"500.0\"/></mxCell><mxCell edge=\"1\" " +
                "id=\"26\" parent=\"1\" source=\"7\" style=\"endArrow=none;type=edge;html=1;\" target=\"25\" " +
                "vertex=\"1\"><mxGeometry as=\"geometry\" height=\"50.0\" relative=\"1\" width=\"50.0\"><mxPoint " +
                "as=\"sourcePoint\" x=\"200.0\" y=\"500.0\"/><mxPoint as=\"targetPoint\" x=\"800.0\" " +
                "y=\"500.0\"/></mxGeometry></mxCell><mxCell id=\"27\" parent=\"1\" style=\"shape=image;" +
                "needInputCode=true;warning=true;type=gyzl;labelPosition=left;verticalLabelPosition=middle;" +
                "whiteSpace=wrap;aspect=fixed;align=right;labelBackgroundColor=#ffffff;" +
                "image=stencils/editor/simulationPeiDian/gyzl_S.svg\" value=\"10.1.1.50\" vertex=\"1\"><mxGeometry " +
                "as=\"geometry\" height=\"48.0\" width=\"48.0\" x=\"600.0\" y=\"700.0\"/></mxCell><mxCell edge=\"1\" " +
                "id=\"28\" parent=\"1\" source=\"7\" style=\"endArrow=none;type=edge;html=1;\" target=\"27\" " +
                "vertex=\"1\"><mxGeometry as=\"geometry\" height=\"50.0\" relative=\"1\" width=\"50.0\"><mxPoint " +
                "as=\"sourcePoint\" x=\"200.0\" y=\"500.0\"/><mxPoint as=\"targetPoint\" x=\"600.0\" " +
                "y=\"700.0\"/></mxGeometry></mxCell><mxCell id=\"29\" parent=\"1\" style=\"shape=image;" +
                "needInputCode=true;warning=true;type=access;labelPosition=left;verticalLabelPosition=middle;" +
                "whiteSpace=wrap;aspect=fixed;align=right;labelBackgroundColor=#ffffff;" +
                "image=stencils/editor/switch/access_switch.svg\" value=\"10.1.1.49\" vertex=\"1\"><mxGeometry " +
                "as=\"geometry\" height=\"48.0\" width=\"48.0\" x=\"900.0\" y=\"500.0\"/></mxCell><mxCell edge=\"1\" " +
                "id=\"30\" parent=\"1\" source=\"7\" style=\"endArrow=none;type=edge;html=1;\" target=\"29\" " +
                "vertex=\"1\"><mxGeometry as=\"geometry\" height=\"50.0\" relative=\"1\" width=\"50.0\"><mxPoint " +
                "as=\"sourcePoint\" x=\"200.0\" y=\"500.0\"/><mxPoint as=\"targetPoint\" x=\"900.0\" " +
                "y=\"500.0\"/></mxGeometry></mxCell><mxCell id=\"31\" parent=\"1\" style=\"shape=image;" +
                "needInputCode=true;warning=true;type=access;labelPosition=left;verticalLabelPosition=middle;" +
                "whiteSpace=wrap;aspect=fixed;align=right;labelBackgroundColor=#ffffff;" +
                "image=stencils/editor/switch/access_switch.svg\" value=\"10.1.1.58\" vertex=\"1\"><mxGeometry " +
                "as=\"geometry\" height=\"48.0\" width=\"48.0\" x=\"100.0\" y=\"600.0\"/></mxCell><mxCell edge=\"1\" " +
                "id=\"32\" parent=\"1\" source=\"7\" style=\"endArrow=none;type=edge;html=1;\" target=\"31\" " +
                "vertex=\"1\"><mxGeometry as=\"geometry\" height=\"50.0\" relative=\"1\" width=\"50.0\"><mxPoint " +
                "as=\"sourcePoint\" x=\"200.0\" y=\"500.0\"/><mxPoint as=\"targetPoint\" x=\"100.0\" " +
                "y=\"600.0\"/></mxGeometry></mxCell><mxCell id=\"33\" parent=\"1\" style=\"shape=image;" +
                "needInputCode=true;warning=true;type=access;labelPosition=left;verticalLabelPosition=middle;" +
                "whiteSpace=wrap;aspect=fixed;align=right;labelBackgroundColor=#ffffff;" +
                "image=stencils/editor/switch/access_switch.svg\" value=\"10.1.1.43\" vertex=\"1\"><mxGeometry " +
                "as=\"geometry\" height=\"48.0\" width=\"48.0\" x=\"200.0\" y=\"600.0\"/></mxCell><mxCell edge=\"1\" " +
                "id=\"34\" parent=\"1\" source=\"7\" style=\"endArrow=none;type=edge;html=1;\" target=\"33\" " +
                "vertex=\"1\"><mxGeometry as=\"geometry\" height=\"50.0\" relative=\"1\" width=\"50.0\"><mxPoint " +
                "as=\"sourcePoint\" x=\"200.0\" y=\"500.0\"/><mxPoint as=\"targetPoint\" x=\"200.0\" " +
                "y=\"600.0\"/></mxGeometry></mxCell><mxCell id=\"35\" parent=\"1\" style=\"shape=image;" +
                "needInputCode=true;warning=true;type=access;labelPosition=left;verticalLabelPosition=middle;" +
                "whiteSpace=wrap;aspect=fixed;align=right;labelBackgroundColor=#ffffff;" +
                "image=stencils/editor/switch/access_switch.svg\" value=\"10.1.1.44\" vertex=\"1\"><mxGeometry " +
                "as=\"geometry\" height=\"48.0\" width=\"48.0\" x=\"300.0\" y=\"600.0\"/></mxCell><mxCell edge=\"1\" " +
                "id=\"36\" parent=\"1\" source=\"7\" style=\"endArrow=none;type=edge;html=1;\" target=\"35\" " +
                "vertex=\"1\"><mxGeometry as=\"geometry\" height=\"50.0\" relative=\"1\" width=\"50.0\"><mxPoint " +
                "as=\"sourcePoint\" x=\"200.0\" y=\"500.0\"/><mxPoint as=\"targetPoint\" x=\"300.0\" " +
                "y=\"600.0\"/></mxGeometry></mxCell><mxCell id=\"37\" parent=\"1\" style=\"shape=image;" +
                "needInputCode=true;warning=true;type=access;labelPosition=left;verticalLabelPosition=middle;" +
                "whiteSpace=wrap;aspect=fixed;align=right;labelBackgroundColor=#ffffff;" +
                "image=stencils/editor/switch/access_switch.svg\" value=\"10.1.1.51\" vertex=\"1\"><mxGeometry " +
                "as=\"geometry\" height=\"48.0\" width=\"48.0\" x=\"400.0\" y=\"600.0\"/></mxCell><mxCell edge=\"1\" " +
                "id=\"38\" parent=\"1\" source=\"7\" style=\"endArrow=none;type=edge;html=1;\" target=\"37\" " +
                "vertex=\"1\"><mxGeometry as=\"geometry\" height=\"50.0\" relative=\"1\" width=\"50.0\"><mxPoint " +
                "as=\"sourcePoint\" x=\"200.0\" y=\"500.0\"/><mxPoint as=\"targetPoint\" x=\"400.0\" " +
                "y=\"600.0\"/></mxGeometry></mxCell><mxCell id=\"39\" parent=\"1\" style=\"shape=image;" +
                "needInputCode=true;warning=true;type=gyzl;labelPosition=left;verticalLabelPosition=middle;" +
                "whiteSpace=wrap;aspect=fixed;align=right;labelBackgroundColor=#ffffff;" +
                "image=stencils/editor/simulationPeiDian/gyzl_S.svg\" value=\"10.1.1.48\" vertex=\"1\"><mxGeometry " +
                "as=\"geometry\" height=\"48.0\" width=\"48.0\" x=\"700.0\" y=\"700.0\"/></mxCell><mxCell edge=\"1\" " +
                "id=\"40\" parent=\"1\" source=\"7\" style=\"endArrow=none;type=edge;html=1;\" target=\"39\" " +
                "vertex=\"1\"><mxGeometry as=\"geometry\" height=\"50.0\" relative=\"1\" width=\"50.0\"><mxPoint " +
                "as=\"sourcePoint\" x=\"200.0\" y=\"500.0\"/><mxPoint as=\"targetPoint\" x=\"700.0\" " +
                "y=\"700.0\"/></mxGeometry></mxCell><mxCell id=\"41\" parent=\"1\" style=\"shape=image;" +
                "needInputCode=true;warning=true;type=access;labelPosition=left;verticalLabelPosition=middle;" +
                "whiteSpace=wrap;aspect=fixed;align=right;labelBackgroundColor=#ffffff;" +
                "image=stencils/editor/switch/access_switch.svg\" value=\"10.1.1.41\" vertex=\"1\"><mxGeometry " +
                "as=\"geometry\" height=\"48.0\" width=\"48.0\" x=\"500.0\" y=\"600.0\"/></mxCell><mxCell edge=\"1\" " +
                "id=\"42\" parent=\"1\" source=\"7\" style=\"endArrow=none;type=edge;html=1;\" target=\"41\" " +
                "vertex=\"1\"><mxGeometry as=\"geometry\" height=\"50.0\" relative=\"1\" width=\"50.0\"><mxPoint " +
                "as=\"sourcePoint\" x=\"200.0\" y=\"500.0\"/><mxPoint as=\"targetPoint\" x=\"500.0\" " +
                "y=\"600.0\"/></mxGeometry></mxCell><mxCell id=\"43\" parent=\"1\" style=\"shape=image;" +
                "needInputCode=true;warning=true;type=access;labelPosition=left;verticalLabelPosition=middle;" +
                "whiteSpace=wrap;aspect=fixed;align=right;labelBackgroundColor=#ffffff;" +
                "image=stencils/editor/switch/access_switch.svg\" value=\"10.1.1.47\" vertex=\"1\"><mxGeometry " +
                "as=\"geometry\" height=\"48.0\" width=\"48.0\" x=\"600.0\" y=\"600.0\"/></mxCell><mxCell edge=\"1\" " +
                "id=\"44\" parent=\"1\" source=\"7\" style=\"endArrow=none;type=edge;html=1;\" target=\"43\" " +
                "vertex=\"1\"><mxGeometry as=\"geometry\" height=\"50.0\" relative=\"1\" width=\"50.0\"><mxPoint " +
                "as=\"sourcePoint\" x=\"200.0\" y=\"500.0\"/><mxPoint as=\"targetPoint\" x=\"600.0\" " +
                "y=\"600.0\"/></mxGeometry></mxCell><mxCell id=\"45\" parent=\"1\" style=\"shape=image;" +
                "needInputCode=true;warning=true;type=access;labelPosition=left;verticalLabelPosition=middle;" +
                "whiteSpace=wrap;aspect=fixed;align=right;labelBackgroundColor=#ffffff;" +
                "image=stencils/editor/switch/access_switch.svg\" value=\"10.1.1.39\" vertex=\"1\"><mxGeometry " +
                "as=\"geometry\" height=\"48.0\" width=\"48.0\" x=\"700.0\" y=\"600.0\"/></mxCell><mxCell edge=\"1\" " +
                "id=\"46\" parent=\"1\" source=\"7\" style=\"endArrow=none;type=edge;html=1;\" target=\"45\" " +
                "vertex=\"1\"><mxGeometry as=\"geometry\" height=\"50.0\" relative=\"1\" width=\"50.0\"><mxPoint " +
                "as=\"sourcePoint\" x=\"200.0\" y=\"500.0\"/><mxPoint as=\"targetPoint\" x=\"700.0\" " +
                "y=\"600.0\"/></mxGeometry></mxCell><mxCell id=\"47\" parent=\"1\" style=\"shape=image;" +
                "needInputCode=true;warning=true;type=access;labelPosition=left;verticalLabelPosition=middle;" +
                "whiteSpace=wrap;aspect=fixed;align=right;labelBackgroundColor=#ffffff;" +
                "image=stencils/editor/switch/access_switch.svg\" value=\"10.1.1.46\" vertex=\"1\"><mxGeometry " +
                "as=\"geometry\" height=\"48.0\" width=\"48.0\" x=\"800.0\" y=\"600.0\"/></mxCell><mxCell edge=\"1\" " +
                "id=\"48\" parent=\"1\" source=\"7\" style=\"endArrow=none;type=edge;html=1;\" target=\"47\" " +
                "vertex=\"1\"><mxGeometry as=\"geometry\" height=\"50.0\" relative=\"1\" width=\"50.0\"><mxPoint " +
                "as=\"sourcePoint\" x=\"200.0\" y=\"500.0\"/><mxPoint as=\"targetPoint\" x=\"800.0\" " +
                "y=\"600.0\"/></mxGeometry></mxCell><mxCell id=\"49\" parent=\"1\" style=\"shape=image;" +
                "needInputCode=true;warning=true;type=access;labelPosition=left;verticalLabelPosition=middle;" +
                "whiteSpace=wrap;aspect=fixed;align=right;labelBackgroundColor=#ffffff;" +
                "image=stencils/editor/switch/access_switch.svg\" value=\"10.1.1.40\" vertex=\"1\"><mxGeometry " +
                "as=\"geometry\" height=\"48.0\" width=\"48.0\" x=\"900.0\" y=\"600.0\"/></mxCell><mxCell edge=\"1\" " +
                "id=\"50\" parent=\"1\" source=\"7\" style=\"endArrow=none;type=edge;html=1;\" target=\"49\" " +
                "vertex=\"1\"><mxGeometry as=\"geometry\" height=\"50.0\" relative=\"1\" width=\"50.0\"><mxPoint " +
                "as=\"sourcePoint\" x=\"200.0\" y=\"500.0\"/><mxPoint as=\"targetPoint\" x=\"900.0\" " +
                "y=\"600.0\"/></mxGeometry></mxCell></root></mxGraphModel>\"");
        System.out.println(aa);
//        String xml = analysisPhysicalTopology(lldpInfoList);
//        System.out.println(xml);
    }

//    @Data
//    @AllArgsConstructor
//    @NoArgsConstructor
//    public class LineCellInfo {
//        private String ip;
//        private String portDesc;
//        private String deviceBrand;
//        private String deviceModel;
//    }
}
