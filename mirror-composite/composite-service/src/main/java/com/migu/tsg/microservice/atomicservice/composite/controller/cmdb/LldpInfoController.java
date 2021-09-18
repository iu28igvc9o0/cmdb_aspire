package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb;

import com.aspire.mirror.composite.service.cmdb.LldpInfoService;
import com.aspire.mirror.composite.service.cmdb.physical.PhysicalTopoResponse;
import com.aspire.mirror.elasticsearch.api.dto.LldpInfo;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.instance.payload.CmdbQueryInstance;
import com.google.common.base.Joiner;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.instance.InstanceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.es.LldpInfoServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.LLDPPhysicalTopology;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * lldp物理设备关联数据控制层
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.composite.controller.cmdb
 * 类名称:    LldpInfoController.java
 * 类描述:    lldp物理设备关联数据
 * 创建人:    JinSu
 * 创建时间:  2019/9/20 14:20
 * 版本:      v1.0
 */
@RestController
@Slf4j
public class LldpInfoController implements LldpInfoService {
    @Autowired
    private InstanceClient instanceClient;

    @Autowired
    private LldpInfoServiceClient lldpInfoServiceClient;

    @Value("${lldp-topo-linesize:12}")
    private Integer maxNum;

    @Override
    public PhysicalTopoResponse getPhysicalTopology(@RequestParam(value = "idcType") String idcType, @RequestParam
            (value = "podName") String podName) {
        PhysicalTopoResponse response = new PhysicalTopoResponse(200, null, null);
        if (StringUtils.isEmpty(idcType)) {
            response.setMsg("资源池参数为空");
            log.warn("LldpInfoController-getPhysicalTopology param idcType is empty");
            return null;
        }
        if (StringUtils.isEmpty(podName)) {
            response.setMsg("POD池为空");
            log.warn("LldpInfoController-getPhysicalTopology param podName is empty");
            return null;
        }
        try {
            CmdbQueryInstance cmdbQueryInstance = new CmdbQueryInstance();
            cmdbQueryInstance.setQueryType("normal");
            cmdbQueryInstance.setPod(podName);
            cmdbQueryInstance.setIdcType(idcType);

            // todo 需要整改参数. 需要循环分页处理
            Result<Map<String, Object>> result = null; //instanceClient.getInstanceList(cmdbQueryInstance);
            if (result != null && !CollectionUtils.isEmpty(result.getData())) {
                List<Map<String, Object>> dataList = result.getData();
                Map<String, String> ipMap = dataList.stream().collect(Collectors.toMap(map -> (String) map.get("ip"),
                        map -> (String) map.get("device_type"), (value1, value2) -> {
                            return value2;
                        }));
                String ids = Joiner.on(",").join(ipMap.keySet());
                List<LldpInfo> list = lldpInfoServiceClient.querylldpInfoByidcAndIp(idcType, ids);
                // 解析拓扑
//            Map<String, String> ipMap = Maps.newHashMap();
                String content = LLDPPhysicalTopology.analysisPhysicalTopology(list, ipMap, instanceClient, maxNum);
                response.setMsg("操作成功");
                response.setData(content);
            }

        } catch (Exception e) {
            response.setCode(500);
            response.setMsg("操作失败");
        }
        return response;
    }

//    String analysisPhysicalTopology(List<LldpInfo> listLldpInfo, Map<String, String> ipMap) {
//        mxGraph graph = new mxGraph();
////       mxGraphModel mxGraphModel = new mxGraphModel();
////       graph.setModel(mxGraphModel);
//        int i = 0;
//        //存放已生成的node
//        Map<String, Integer> hostMap = Maps.newHashMap();
//        Set<String> lineSet = Sets.newHashSet();
//        int maxNum = 8;
//        mxCell root = new mxCell();
//        root.setId(String.valueOf(i++));
//        root.setStyle("pagetype=0");
//        mxCell parent = new mxCell();
//        parent.setId(String.valueOf(i++));
//        parent.setParent(root);
//        root.insert(parent);
//        double cellx, celly, width = 48, height = 48;
//        Map<String, Integer> nodeTypNumMap = new HashMap<>();
//
//        for (LldpInfo lldpInfo : listLldpInfo) {
//            //拼装本设备实体拓扑图形
//            mxCell cell;
//            TopoEnum topoEnum = ipMap.get(lldpInfo.getHost()) == null ? TopoEnum.getTopoEnumByName(lldpInfo
//                    .getDeviceType()) : TopoEnum.getTopoEnumByName(ipMap.get(lldpInfo.getHost()));
//            if (!hostMap.keySet().contains(lldpInfo.getHost())) {
//                if (nodeTypNumMap.get(topoEnum.getName()) == null) {
//                    nodeTypNumMap.put(topoEnum.getName(), 1);
//                } else {
//                    nodeTypNumMap.put(topoEnum.getName(), nodeTypNumMap.get(topoEnum.getName()) + 1);
//                }
//                cellx = topoEnum.getX() + (nodeTypNumMap.get(topoEnum.getName()) - 1) * topoEnum.getXStep();
//                celly = topoEnum.getY();
//
//                if (nodeTypNumMap.get(topoEnum.getName()) > 1 && 0 == (nodeTypNumMap.get(topoEnum.getName()) - 1) %
//                        maxNum) {
//                    cellx = topoEnum.getX();
//                    celly = topoEnum.getY() + ((nodeTypNumMap.get(topoEnum.getName()) - 1) / maxNum) * 100;
//                }
//                cell = new mxCell();
//                cell.setId(String.valueOf(i++));
//                cell.setValue(lldpInfo.getHost());
//                cell.setStyle(topoEnum.getStyle());
////            cell.setParent(parent);
//                cell.setVertex(true);
//                //添加hostSet以便筛除
//                mxGeometry geometry = new mxGeometry(cellx, celly, width, height);
//                cell.setGeometry(geometry);
//                //添加本设备实体图形至画布
//                int index = parent.getChildCount();
//                parent.insert(cell);
//                hostMap.put(lldpInfo.getHost(), index);
//            } else {
//                cell = (mxCell) parent.getChildAt(hostMap.get(lldpInfo.getHost()));
//                cellx = cell.getGeometry().getX();
//                celly = cell.getGeometry().getY();
//            }
//            for (LldpData lldpData : lldpInfo.getLldpData()) {
//                //关联设备cell
//                PeerDeviceInfo peerDeviceInfo = lldpData.getPeerDeviveInfo();
//                mxCell target;
//                double targetx, targety;
//                if (hostMap.get(peerDeviceInfo.getPeerHost()) == null) {
//                    topoEnum = ipMap.get(peerDeviceInfo.getPeerHost()) == null ? TopoEnum.getTopoEnumByName(lldpData
//                            .getPeerDeviveInfo().getPeerDeviceType()) : TopoEnum.getTopoEnumByName(ipMap.get(lldpData
//                            .getPeerDeviveInfo().getPeerHost()));
//                    if (nodeTypNumMap.get(topoEnum.getName()) == null) {
//                        nodeTypNumMap.put(topoEnum.getName(), 1);
//                    } else {
//                        nodeTypNumMap.put(topoEnum.getName(), nodeTypNumMap.get(topoEnum.getName()) + 1);
//                    }
//                    targetx = topoEnum.getX() + (nodeTypNumMap.get(topoEnum.getName()) - 1) * topoEnum.getXStep();
//                    targety = topoEnum.getY();
//                    if (nodeTypNumMap.get(topoEnum.getName()) > 1 && 0 == (nodeTypNumMap.get(topoEnum.getName()) - 1)
//                            % maxNum) {
//                        targetx = topoEnum.getX();
//                        targety = topoEnum.getY() + ((nodeTypNumMap.get(topoEnum.getName()) - 1) / maxNum) * 100;
//                    }
//                    target = new mxCell();
//                    target.setId(String.valueOf(i++));
//                    target.setStyle(topoEnum.getStyle());
//                    target.setValue(peerDeviceInfo.getPeerHost());
//                    //                target.setParent(parent);
//                    target.setVertex(true);
//                    mxGeometry targetGeometry = new mxGeometry(targetx, targety, width, height);
//                    target.setGeometry(targetGeometry);
//                    int index = parent.getChildCount();
//                    parent.insert(target);
//                    hostMap.put(peerDeviceInfo.getPeerHost(), index);
//                } else {
//                    target = (mxCell) parent.getChildAt(hostMap.get(peerDeviceInfo.getPeerHost()));
//                    targetx = target.getGeometry().getX();
//                    targety = target.getGeometry().getY();
//                }
//
//                //添加连接线条cell，当两头中有一头节点不存在，则画线条，两头节点数据之前就存在不划线
//                if (!lineSet.contains(lldpInfo.getHost() + "to" + peerDeviceInfo.getPeerHost()) && !lineSet.contains
//                        (peerDeviceInfo.getPeerHost() + "to" + lldpInfo.getHost())) {
//                    mxCell line = new mxCell();
//                    line.setId(String.valueOf(i++));
//                    line.setStyle("endArrow=none;type=edge;html=1;");
////                line.setParent(parent);
//                    line.setVertex(true);
//                    line.setSource(cell);
//                    line.setTarget(target);
//                    line.setEdge(true);
//                    mxGeometry lineGeometry = new mxGeometry();
//                    lineGeometry.setWidth(50);
//                    lineGeometry.setHeight(50);
//                    lineGeometry.setRelative(true);
//                    mxPoint targetPoint = new mxPoint(targetx, targety);
//                    mxPoint sourcePoint = new mxPoint(cellx, celly);
//                    lineGeometry.setTargetPoint(targetPoint);
//                    lineGeometry.setSourcePoint(sourcePoint);
//                    line.setGeometry(lineGeometry);
//                    lineSet.add(lldpInfo.getHost() + "to" + peerDeviceInfo.getPeerHost());
//                    parent.insert(line);
//                }
//            }
//        }
//        mxCodec codec = new mxCodec();
//        mxGraphModel model = (mxGraphModel) graph.getModel();
//        model.setRoot(root);
//        System.out.println(root.getChildCount());
//        return mxXmlUtils.getXml(codec.encode(model));
//    }
}
