package com.migu.tsg.microservice.atomicservice.composite.controller.scada;

import com.alibaba.fastjson.JSON;
import com.aspire.mirror.alert.api.dto.alert.AlertValueSearchRequest;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.service.scada.IScadaCanvasService;
import com.aspire.mirror.composite.service.scada.payload.*;
import com.aspire.mirror.elasticsearch.api.dto.HistorySearchRequest;
import com.aspire.mirror.elasticsearch.api.dto.LldpInfo;
import com.aspire.mirror.scada.api.dto.PathRequest;
import com.aspire.mirror.scada.api.dto.ScadaCanvasPageRequest;
import com.aspire.mirror.scada.api.dto.ScadaCanvasReq;
import com.aspire.mirror.scada.api.dto.ScadaCanvasRes;
import com.aspire.mirror.scada.api.dto.model.ScadaCanvasDTO;
import com.aspire.ums.cmdb.instance.payload.CmdbQueryInstance;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.alert.AlertsServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.instance.InstanceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.monitor.HistoryServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.es.LldpInfoServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.scada.ScadaCanvasServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.LLDPPhysicalTopology;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.ResAction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil.jacksonBaseParse;

/**
 * 组态视图模块暴露接口服务
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.scada
 * 类名称:    ICanvasService.java
 * 类描述:    组态视图暴露接口服务
 * 创建时间:  2019/6/11 14:40
 * 版本:      v1.0
 *
 * @author JinSu
 */

@Slf4j
@RestController
public class ScadaCanvasController implements IScadaCanvasService {
    /**
     * FTP服务器端口
     */
//    private static String port = "21";
//    /**
//     * 文件最大限制
//     */
//    private static String size = "300000";
//
//    private static String host103 = "10.1.5.103";
//    /**
//     * nginx监听端口
//     */
//    private static String nginxPort103 = "8398";
//    private static String nginxPort62 = "8398";
//    /**
//     * FTP登录账号
//     */
//    private static String user103 = "sudoroot";
//    /**
//     * FTP登录密码
//     */
//    private static String password103 = "spider+999";
//    /**
//     * 文件映射路径   103的存放路径为"/opt/aspire/product/scada/uploadFile/"、
//     * 62的存放路径为"/mnt/file/"、
//     */
//    private static String resourceHandler = "/images/";
//    private static String resourceLocations103 = "/opt/aspire/product/scada/uploadFile/";
//
//    private static String host62 = "10.12.70.62";
//    private static String user62 = "sudoroot";
//    private static String password62 = "Opstest+789";
//    private static String resourceLocations62 = "/mnt/file/";
    @Value("${lldp-topo-linesize:12}")
    private Integer maxNum;

    @Autowired
    private ScadaCanvasServiceClient scadaCanvasService;

//    @Autowired
//    private ConfigDictClient configDictClient;

    @Autowired
    private InstanceClient instanceClient;

    @Autowired
    private LldpInfoServiceClient lldpInfoServiceClient;

    @Autowired
    private HistoryServiceClient historyServiceClient;

    @Autowired
    private AlertsServiceClient alertsServiceClient;



    @Override
    @ResAction(action = "delete", resType = "scada")
    public ResMap delScadaCanvas(@PathVariable("id") String id) {
        com.aspire.mirror.scada.common.entity.ResMap resMap = scadaCanvasService.deleteByPrimaryKey(id);
        return changeResultMap(resMap);
    }

    @Override
    @ResAction(action = "view", resType = "scada")
    public CompScadaCanvas findScadaCanvasInfoById(@RequestParam("id") String id) {
        ScadaCanvasDTO scadaCanvasDTO = scadaCanvasService.findByPrimaryKey(id);
        CompScadaCanvas compScadaCanvas = jacksonBaseParse(CompScadaCanvas.class, scadaCanvasDTO);
        return compScadaCanvas;
    }

//    @Override
//    @ResponseStatus(HttpStatus.OK)
//    @ResAction(action = "view", resType = "scada")
//    public ResMap pictureTransformatting(@RequestParam("picture") MultipartFile picture) {
//        String data = null;
//        if (picture != null) {
//            BASE64Encoder encoder = new BASE64Encoder();
//            // 通过base64来转化图片
//            try {
//                data = encoder.encode(picture.getBytes());
//            } catch (IOException e) {
//            }
//        }
//        Map<String, String> map = new HashMap<>();
//        return ResMap.success(data);
//    }

    @Override
    @ResAction(action = "view", resType = "scada")
    public ResMap saveScadaCanvas(@RequestBody CompScadaCanvas compScadaCanvas) throws Exception {
        ScadaCanvasReq scadaCanvasReq = jacksonBaseParse(ScadaCanvasReq.class, compScadaCanvas);
        com.aspire.mirror.scada.common.entity.ResMap resMap = null;
        if (StringUtils.isEmpty(compScadaCanvas.getId())) {
            ScadaCanvasDTO result = scadaCanvasService.findScadaCanvasByName(compScadaCanvas.getName());
            if (result != null) {
                return ResMap.error(ResErrorEnum.RESOURCEEXIST, null);
            }
            resMap = scadaCanvasService.creatScadaCanvas(scadaCanvasReq);
        } else {
            resMap = scadaCanvasService.modifyByPrimaryKey(scadaCanvasReq);
        }
        if (!resMap.getCode().equals(ResErrorEnum.SUCCESS.getCode())) {
            //将子层的resultMap进行转换
            return changeResultMap(resMap);

        } else {
            //将子层返回的data转成对象
            compScadaCanvas = changeToCompScadaCanvas(resMap);

        }
        return ResMap.success(compScadaCanvas);
    }

    @Override
    public List<ScadaBindValueResponse> getScadaBindValue(@RequestBody ScadaBindValueRequest bindValueRequest) {
        List<ScadaBindValueResponse> result = Lists.newArrayList();
        if (CollectionUtils.isEmpty(bindValueRequest.getBindObjList())) {
            log.warn("需查询视图绑定值对象为空");
            return result;
        }
        for (ScadaBindVO scadaBindVO : bindValueRequest.getBindObjList()) {
            if (scadaBindVO.getIsBind()) {
                ScadaBindValueResponse valueItem = new ScadaBindValueResponse();
                BeanUtils.copyProperties(scadaBindVO, valueItem);
                if (scadaBindVO.getBandType() == 0) {
                    //告警值
                    List<Map<String, String>> deviceList = scadaBindVO.getDeviceList();
                    Map<String, List<String>> ipMap = Maps.newHashMap();
                    getIpMap(deviceList, ipMap);
                    AlertValueSearchRequest alertValueSearchRequest = new AlertValueSearchRequest();
                    alertValueSearchRequest.setIpMap(ipMap);
                    alertValueSearchRequest.setAlertLevel(scadaBindVO.getAlertLevelList());
                    List<String> itemIdList = scadaBindVO.getItemList().stream().map(map -> map.get("itemId")).collect(Collectors.toList());
                    alertValueSearchRequest.setItemIdList(itemIdList);
                    int value = alertsServiceClient.getAlertValue(alertValueSearchRequest);
                    valueItem.setValue((double) value);
                } else if (scadaBindVO.getBandType() == 1) {
                    //监控值
                    HistorySearchRequest historySearchRequest = new HistorySearchRequest();
                    List<String> itemKeyList = scadaBindVO.getItemList().stream().map(map -> map.get("key")).collect(Collectors.toList());
                    historySearchRequest.setItemList(itemKeyList);
                    List<Map<String, String>> deviceList = scadaBindVO.getDeviceList();
                    Map<String, List<String>> ipMap = Maps.newHashMap();
                    getIpMap(deviceList, ipMap);
                    historySearchRequest.setIpMap(ipMap);
                    historySearchRequest.setCountType(scadaBindVO.getCountType());
                    Map<String, Object> monitorValue = historyServiceClient.getMonitorValue(historySearchRequest);
                    valueItem.setValue((Double) monitorValue.get("value"));
                }
                result.add(valueItem);
            }
        }
        return result;
    }


    private ResMap changeResultMap(com.aspire.mirror.scada.common.entity.ResMap resMap) {
        ResMap resMap1 = new ResMap();
        BeanUtils.copyProperties(resMap, resMap1);
        return resMap1;
    }

    private void getIpMap(List<Map<String, String>> deviceList, Map<String, List<String>> ipMap) {
        for (int i = 0; i < deviceList.size(); i++) {
            Map<String, String> map = deviceList.get(i);
//            JSONObject jsonObject = deviceArray.getJSONObject(i);
            String idcType = map.get("idcType");
            if (ipMap.get(idcType) == null) {
                List<String> ipList = Lists.newArrayList();
                ipList.add(map.get("ip"));
                ipMap.put(idcType, ipList);
            } else {
                List<String> ipList = ipMap.get(idcType);
                ipList.add(map.get("ip"));
            }
        }
    }
    private CompScadaCanvas changeToCompScadaCanvas(com.aspire.mirror.scada.common.entity.ResMap resMap) throws
            Exception {
        Object object = MapToObj.mapToObject((Map<String, String>) resMap.getData(), ScadaCanvasRes.class);
        ScadaCanvasRes scadaCanvasRes = (ScadaCanvasRes) object;
        return jacksonBaseParse(CompScadaCanvas.class, scadaCanvasRes);
    }
//
//    @Override
//    @ResAction(action = "view", resType = "scada")
//    public ResMap getScadaCanvasInfoByPrecinctId(@RequestParam(value = "precinctId") String precinctId,
//                                                 @RequestParam(value = "pictureType",required = false)Integer
// pictureType) throws Exception {
//        if(pictureType == null){
//            ResMap resMap = findScadaCanvasList(precinctId);
//            return resMap;
//        }else {
//            com.aspire.mirror.template.common.entity.ResMap resMap = scadaCanvasService
// .getScadaCanvasInfoByPrecinctId(precinctId, pictureType);
//            if (resMap.getCode().equals(ResErrorEnum.SUCCESS.getCode())&&resMap.getData()!=null){
//                Object object = MapToObj.mapToObject((Map<String, String>) resMap.getData(), ScadaCanvasDTO.class);
//                ScadaCanvasDTO scadaCanvasDTO = (ScadaCanvasDTO) object;
//                CompScadaCanvas compScadaCanvas = PayloadParseUtil.jacksonBaseParse(CompScadaCanvas.class,
// scadaCanvasDTO);
//                return ResMap.success(compScadaCanvas);
//            }else {
//                return changeResultMap( resMap) ;
//            }
//        }
//
//
//    }


//    @Override
//    @ResAction(action = "view", resType = "scada")
//    public ResMap upload(HttpServletRequest request, @RequestParam(value = "files") MultipartFile file) throws
//            Exception {
//
//        UploadResult uploadResult = new UploadResult();
//        if (file == null) {
//            return ResMap.notice("文件不能为空", null);
//        }
//        InputStream is = null;
//        String result = null;
//        //获取配置文件中的上传图标文件的最大大小
//        int ftpPort = Integer.valueOf(port);
//        Long iconMaxsize = Long.valueOf(size);
//        String fileName = file.getOriginalFilename();
//        System.out.println(file.getSize());
//        if (file.getSize() > iconMaxsize) {
//            return ResMap.notice("上传的文件过大", null);
//        }
//        if (!file.isEmpty()) {
//            is = file.getInputStream();
//            //根据本机IP选择要上传的服务器
//            InetAddress inetAddress = InetAddress.getLocalHost();
//            String ia = inetAddress.getHostAddress().toString();
//            if (host62.equals(ia)) {
//                int nginxPort = Integer.parseInt(nginxPort62);
//                result = FtpUtls.uploadFile(host62, ftpPort, user62, password62, nginxPort,
//                        resourceLocations62, resourceHandler, fileName, is);
//                uploadResult.setUrl("http://" + result);
//            }
////              else if (host103.equals(ia)){
//            else {
////                int nginxPort = Integer.parseInt(nginxPort103);
////                result = FtpUtls.uploadFile(host103, ftpPort, user103, password103,nginxPort,
////                        resourceLocations103, resourceHandler,fileName, is);
//                int nginxPort = Integer.parseInt(nginxPort62);
//                result = FtpUtls.uploadFile(host62, ftpPort, user62, password62, nginxPort,
//                        resourceLocations62, resourceHandler, fileName, is);
//                uploadResult.setUrl("http://" + result);
//            }
//            if (null != result && !"".equals(result)) {
//            } else {
//                return ResMap.notice("文件[\"+fileName+\"]上传失败", null);
//            }
//        }
//
//        uploadResult.setName(fileName);
//
//        return ResMap.success(uploadResult);
//    }

//    @Override
//    @ResAction(action = "view", resType = "scada")
//    public List<AlertAndMeteVO> getAlertCountOrMeteValueByPrecinctId(@RequestBody ComTree comTree) {
//        List<ComTree> comTrees = comTree.getComTrees();
//        if (!CollectionUtils.isEmpty(comTrees)){
//            MeteReq meteReq = new MeteReq();
//            List<MeteReq> meteReqs = new ArrayList<>();
//            QueryAlertsReq req = new QueryAlertsReq();
//            List<QueryAlertsReq> queryAlertsReqList = new ArrayList<>();
//            //根据是否为测点调用不同的方法，如果为测点
//            for (ComTree comTree1 : comTrees){
//                if (StringUtils.isEmpty(comTree1.getPrecinctId())&&StringUtils.isEmpty(comTree1.getDeviceType())){
//                    ComMeteReq comMeteReq = new ComMeteReq();
//                    comMeteReq.setDeviceId(comTree1.getDeviceId());
//                    comMeteReq.setMeteId(comTree1.getId());
//                    comMeteReq.setMeteCode(comTree1.getDeviceModel());
//                    MeteReq meteReq1 = PayloadParseUtil.jacksonBaseParse(MeteReq.class, comMeteReq);
//                    meteReqs.add(meteReq1);
//
//                }
//                //否则
//                else{
//                    ComQueryAlertsReq comQueryAlertsReq = new ComQueryAlertsReq();
//                    comQueryAlertsReq.setPrecinctId(comTree1.getPrecinctId());
//                    comQueryAlertsReq.setPrecinctKind(comTree1.getPrecinctKind());
//                    comQueryAlertsReq.setDeviceKind(comTree1.getDeviceKind());
//                    comQueryAlertsReq.setDeviceType(comTree1.getDeviceType());
//                    comQueryAlertsReq.setIsParent(comTree1.getIsParent());
//                    comQueryAlertsReq.setIsPrecinct(comTree1.getIsPrecinct());
//                    QueryAlertsReq queryAlertsReq = PayloadParseUtil.jacksonBaseParse(QueryAlertsReq.class,
// comQueryAlertsReq);
//                    queryAlertsReqList.add(queryAlertsReq);
//                }
//            }
//            List<AlertAndMeteVO> alertAndMeteVOList = new ArrayList<>();
//            if (!CollectionUtils.isEmpty(meteReqs)){
//                meteReq.setMeteReqs(meteReqs);
//                List<MetePageItemVO> metePageItemVOList = monitorServiceClient.findMeasureVal(meteReq);
//                if (!CollectionUtils.isEmpty(metePageItemVOList)){
//                    for (MetePageItemVO metePageItemVO : metePageItemVOList){
//                        AlertAndMeteVO alertAndMeteVO = new AlertAndMeteVO();
//                        alertAndMeteVO.setId(metePageItemVO.getMeteId());
//                        alertAndMeteVO.setDeviceName(metePageItemVO.getDeviceName());
//                        alertAndMeteVO.setDeviceId(metePageItemVO.getDeviceId());
//                        alertAndMeteVO.setMeteId(metePageItemVO.getMeteId());
//                        alertAndMeteVO.setMeteCode(metePageItemVO.getMeteCode());
//                        alertAndMeteVO.setMeteKind(metePageItemVO.getMeteKind());
//                        alertAndMeteVO.setMeteName(metePageItemVO.getMeteName());
//                        alertAndMeteVO.setUnit(metePageItemVO.getUnit());
//                        alertAndMeteVO.setMeasureTime(metePageItemVO.getMeasureTime());
//                        alertAndMeteVO.setValue(metePageItemVO.getValue());
//                        alertAndMeteVO.setNodeType(1);
//                        alertAndMeteVOList.add(alertAndMeteVO);
//                    }
//                }
//            }
//            if (!CollectionUtils.isEmpty(queryAlertsReqList)){
//                req.setQueryAlertsReqList(queryAlertsReqList);
//                List<QueryAlertRes> queryAlertResList = alertsServiceClient.queryAlertByPrecinctId(req);
//                if (!CollectionUtils.isEmpty(queryAlertResList)){
//                    for (QueryAlertRes queryAlertRes : queryAlertResList){
//                        AlertAndMeteVO alertAndMeteVO = new AlertAndMeteVO();
//                        alertAndMeteVO.setId(queryAlertRes.getPrecinctId());
//                        alertAndMeteVO.setHasAlert(queryAlertRes.isHasAlert());
//                        alertAndMeteVO.setLevelsCount(queryAlertRes.getLevelsCount());
//                        alertAndMeteVO.setNodeType(0);
//                        alertAndMeteVOList.add(alertAndMeteVO);
//                    }
//                }
//            }
//            return alertAndMeteVOList;
//        }else {
//            return null;
//        }
//    }

    @Override
    @ResAction(action = "view", resType = "scada")
    public ResMap findScadaCanvasList(@RequestBody ScadaCanvasSearchReq scadaCanvasSearchReq) throws Exception {
        ScadaCanvasReq scadaCanvasReq = new ScadaCanvasReq();
        BeanUtils.copyProperties(scadaCanvasSearchReq, scadaCanvasReq);
        com.aspire.mirror.scada.common.entity.ResMap resMap = scadaCanvasService.findScadaCanvasList(scadaCanvasReq);
        List<CompScadaCanvas> compScadaCanvasList = new ArrayList<>();
        if (resMap.getCode().equals(ResErrorEnum.SUCCESS.getCode()) && resMap.getData() != null) {
            List<Map<String, String>> list = (List<Map<String, String>>) resMap.getData();
            for (Map<String, String> li : list) {
                Object object = MapToObj.mapToObject(li, ScadaCanvasDTO.class);
                ScadaCanvasDTO scadaCanvasDTO = (ScadaCanvasDTO) object;
                CompScadaCanvas compScadaCanvas = jacksonBaseParse(CompScadaCanvas.class, scadaCanvasDTO);
                compScadaCanvasList.add(compScadaCanvas);
            }
            return ResMap.success(compScadaCanvasList);
        } else {
            return changeResultMap(resMap);
        }
    }

    /**
     * 获取在线视图
     *
     * @param scadaCanvasSearchReq
     * @return
     * @throws Exception
     */
    @Override
    public ResMap findOnlineScada(@RequestBody ScadaCanvasSearchReq scadaCanvasSearchReq) throws Exception {
        if (scadaCanvasSearchReq == null) {
            log.warn("ScadaCanvasController[findOnlineScada] param is null");
            return null;
        }
//        if (scadaCanvasSearchReq.getPictureType() != 1) {
//            log.warn("ScadaCanvasController[findOnlineScada] param pictureType is not logical topology");
//            return null;
//        }
//        List<String> idcList = Lists.newArrayList();
//        List<String> podList = Lists.newArrayList();
//        if (!StringUtils.isEmpty(scadaCanvasSearchReq.getIdc())) {
//            idcList.add(scadaCanvasSearchReq.getIdc());
//        } else {
//            List<ConfigDict> dictList = configDictClient.getDictsByType("idcType", null, null, null);
//            idcList = dictList.stream().map(configDict -> configDict.getValue()).collect(Collectors.toList());
//        }
//        if (!StringUtils.isEmpty(scadaCanvasSearchReq.getPod())) {
//            podList.add(scadaCanvasSearchReq.getPod());
//        } else {
//            List<ConfigDict> dictList = configDictClient.getDictsByType("pod_name", null, null, null);
//            podList = dictList.stream().map(configDict -> configDict.getValue()).collect(Collectors.toList());
//        }
        CmdbQueryInstance cmdbQueryInstance = new CmdbQueryInstance();
        if (!StringUtils.isEmpty(scadaCanvasSearchReq.getIdc())) {
            cmdbQueryInstance.setIdcType(scadaCanvasSearchReq.getIdc());
        }
        if (!StringUtils.isEmpty(scadaCanvasSearchReq.getPod())) {
            cmdbQueryInstance.setPod(scadaCanvasSearchReq.getPod());
        }
//        ResMap resMap = new ResMap(ResErrorEnum.SUCCESS.getCode(), "操作成功");
        try {
            List<Map<String, Object>> result = instanceClient.getNetworkAndSafetyDeivce(cmdbQueryInstance);
            List<CompScadaCanvas> compScadaCanvasList = Lists.newArrayList();
            for (Map<String, Object> map : result) {
                String ipString = (String) map.get("ipString");
                String[] ipAndDeviceTypeArray = ipString.split(",");
                Map<String, String> ipMap = Maps.newHashMap();
                for (String ipAndDeviceType : ipAndDeviceTypeArray) {
                    String[] temp = ipAndDeviceType.split("_");
                    if (temp.length >= 2) {
                        ipMap.put(temp[0], temp[1]);
                    }
                }
                if (ipMap.size() > 0 && !StringUtils.isEmpty(map.get("idc")) && !StringUtils.isEmpty(map.get("pod"))) {
                    String ids = Joiner.on(",").join(ipMap.keySet());
                    String idc = (String) map.get("idc");
                    String pod = (String) map.get("pod");
                    List<LldpInfo> list = lldpInfoServiceClient.querylldpInfoByidcAndIp(idc, ids);
                    if (!CollectionUtils.isEmpty(list)) {
                        String content = LLDPPhysicalTopology.analysisPhysicalTopology(list, ipMap, instanceClient, maxNum);
                        CompScadaCanvas compScadaCanvas = new CompScadaCanvas();
                        compScadaCanvas.setName(idc + "-" + pod + "-实时视图");
                        compScadaCanvas.setIdc(idc);
                        compScadaCanvas.setPod(pod);
                        compScadaCanvas.setContent(content);
                        compScadaCanvas.setPictureType(1);
                        compScadaCanvasList.add(compScadaCanvas);
                    }
                }
            }
            return ResMap.success(compScadaCanvasList);
        } catch (Exception e) {
            log.error("查询在线视图错误", e);
            return ResMap.error();
        }
    }

    @Override
    public PageResponse<CompScadaCanvas> pageList(@RequestBody CompScadaCanvasPageRequest pageRequest) {
        if (pageRequest == null) {
            log.warn("ScadaCanvasController[pageList] param is null");
            return null;
        }
        PageResponse<CompScadaCanvas> response = new PageResponse<>();
        ScadaCanvasPageRequest scadaCanvasPageRequest = new ScadaCanvasPageRequest();
        BeanUtils.copyProperties(pageRequest, scadaCanvasPageRequest);
        PageResponse<ScadaCanvasDTO> pageResponse = scadaCanvasService.pageList(scadaCanvasPageRequest);
        response.setCount(pageResponse.getCount());
        if (!CollectionUtils.isEmpty(pageResponse.getResult())) {
            List<CompScadaCanvas> compScadaCanvasList = jacksonBaseParse(CompScadaCanvas.class, pageResponse
                    .getResult());
            response.setResult(compScadaCanvasList);
        }
        return response;
    }

	@Override
	public ResMap getPath(@RequestBody ComPathRequest pathRequest) throws Exception {
		if (StringUtils.isEmpty(pathRequest.getContent()) || StringUtils.isEmpty(pathRequest.getDestIp()) 
   			 ||StringUtils.isEmpty(pathRequest.getSourceIp())) {
			log.warn("getPath param error:{}",JSON.toJSONString(pathRequest));
			  return new ResMap(5,"请求异常");
        }
		return jacksonBaseParse(ResMap.class, scadaCanvasService.getPath(jacksonBaseParse(PathRequest.class,pathRequest)));
	}


//    @Override
//    public List<ScadaCanvasNodeInfo> getScadaCanvasNodeInfo() throws Exception{
//
//        List<ScadaCanvasNodeInfo> resultList = new ArrayList<ScadaCanvasNodeInfo>();
//
//        //调取配置管理(彭峰)接口，获取一级菜单
//        Map<String, Object> firstMap = deviceService.getDicsByColNames("scada_kind");
//
//        //对一级菜单数据进行封装处理
//        List<Object> firstList = (List<Object>) firstMap.get("scada_kind");
//        for(Object firstBean:firstList){
//            String[] firsSplit = firstBean.toString().replace("{", "").replace("}", "").split(", ");
//            ScadaCanvasNodeInfo fistNode = new ScadaCanvasNodeInfo();
//            fistNode.setId((firsSplit[1].split("="))[1]);
//            fistNode.setLabel((firsSplit[3].split("="))[1]);
//
//            //调取配置管理(彭峰)接口，获取二级菜单
//            JSONArray resultsubList = deviceService.getSubListByUpDict((firsSplit[0].split("="))[1]);
//
//            //对二级菜单数据进行封装处理
//            ObjectMapper objectMapper = new ObjectMapper();
//            JsonNode jsonNode = objectMapper.readTree(resultsubList.toString());
//            if(jsonNode.isArray()){
//                List<ScadaCanvasNodeInfo> subList = new ArrayList<ScadaCanvasNodeInfo>();
//                Iterator<JsonNode> it = jsonNode.iterator();
//                while (it.hasNext()){
//                    JsonNode node = it.next();
//                    ScadaCanvasNodeInfo subNode = new ScadaCanvasNodeInfo();
//                    subNode.setId(node.get("dictCode").asText());
//                    subNode.setLabel(node.get("dictNote").asText());
//                    subList.add(subNode);
//                }
//                fistNode.setChildren(subList);
//            }
//
//
//            resultList.add(fistNode);
//        }
//        return resultList;
//    }


//    @Override
//    public List<Map<String, Object>> getExcelData(@RequestParam(value = "files") MultipartFile file) throws Exception {
//
//        //生成返回结果封装对象
//        ArrayList<Map<String, Object>> resultList = new ArrayList<>();
//
//
//        //获取结果原始数据，已经排序
//        //以下参考我的网盘项目myspringboot里的FileUploadController类
//        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
//        for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
//
//            //《《《==================================================================================》》》
//            //《《《===================== 解析excel原始数据开始=========================================》》》
//
//            //生成每页的原始数据封装对象，并对结果集合进行排序，方便进行二次封装。
//            TreeMap<String, String> DataMap = new TreeMap<>(new Comparator<String>() {
//                @Override
//                public int compare(String o1, String o2) {
//                    return o1.compareTo(o2);
//                }
//            });
//
//
//            XSSFSheet sheet = workbook.getSheetAt(sheetNum);
//            //忽略样板的前两个sheet
//            if (sheet.getSheetName().equals("填写说明") || sheet.getSheetName().equals("配电影响业务梳理表--示例")) {
//                continue;
//            }
//
//
//            for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
//                CellRangeAddress mergedRegion = sheet.getMergedRegion(i);
//                int firstRow = mergedRegion.getFirstRow();
//                //忽略表头
//                if (firstRow <= 1) {
//                    continue;
//                }
//
//                int firstColumn = mergedRegion.getFirstColumn();
//
//                //按照要求，只获取第0，第1，第4列的内容，其它的略过
//                if (firstColumn != 0 & firstColumn != 1 & firstColumn != 4) {
//                    continue;
//                }
//
//                //生成每个单元格内容的唯一id
//                //生成规则：sheet编号+row编号+column编号
//                //数字格式化为4位，是为了防止字符串里的数字在compareTo对比大小时，出现一位数字大于两位数字的情况
//                String key = "s" + String.format("%04d", sheetNum) + "r" + String.format("%04d", firstRow) + "c" +
//                        String.format("%04d", firstColumn);
//                DataMap.put(key, sheet.getRow(firstRow).getCell(firstColumn).getStringCellValue());
//            }
//
//            //《《《==================================================================================》》》
//            //《《《===================== 解析excel原始数据结束=========================================》》》
//
//
//            //《《《==================================================================================》》》
//            //《《《====对原始数据进行二次封装处理开始(ps：这个返回结果封装对象的结构，是按照前端同事的需求而设计的)》》》
//
//            //生成每页的返回结果封装对象，对原始数据经行二次封装。
//            ArrayList<Map<String, String>> sheetList = new ArrayList<>();
//
//            //遍历每行原始数据，进行层级封装
//            int counter = 0;
//            LinkedHashMap<String, String> rowMap = new LinkedHashMap<>();
//            for (String key : DataMap.keySet()) {
//                if (counter == 3) {
//                    //把每行数据作为一个map，存入sheetList.
//                    sheetList.add(rowMap);
//
//                    //清空每行的记录，重新开始
//                    counter = 0;
//                    rowMap = new LinkedHashMap<>();
//                }
//                counter = counter + 1;
//                rowMap.put("num" + counter, DataMap.get(key)); //rowMap里面的key生成模式，按照前端耿双同学的要求修改的
//            }
//
//
//            //把sheetList封装进sheetMap
//            //sheetMap的结构为：
//            //key：sheet.getSheetName()，每个sheet的名称
//            //value：sheetList，里面是多个rowMap
//            LinkedHashMap<String, Object> sheetMap = new LinkedHashMap<>();
//            sheetMap.put(sheet.getSheetName(), sheetList);
//
//
//            //至此，每一个sheet封装完毕，放入最终返回对象resultList中
//            resultList.add(sheetMap);
//
//
//            //《《《==================================================================================》》》
//            //《《《============== 对原始数据进行二次封装处理结束=========================================》》》
//
//        }
//
//
//        return resultList;
//    }


    //特殊任务
    //视图批量插入数据库
//    @Override
//    public String batchSaveScadaCanvas() throws Exception{
//
//        File path = new File("d:/667788/");
//        File listFile = new File("c:/Users/zhujiahao/Desktop/卓望工作任务/动环相关工作/组态模块/批量导入视图/已处理的视图文件和节点id对应.txt");
//        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(listFile),
// "UTF-8"));
//        String s = null;
//        while((s = bufferedReader.readLine()) != null){
//
//            String[] array = s.split("=");
//
//            //获取每个文件的内容
//            String content = null;
//            File file = new File(path, array[0]);
//            Long filelength = file.length();
//            byte[] filecontent = new byte[filelength.intValue()];
//            try {
//                FileInputStream in = new FileInputStream(file);
//                in.read(filecontent);
//                in.close();
//                content = new String(filecontent,"UTF-8");
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            //获取试图名称
//            String scadaName = array[0].replace(".xml","").replace("附件1：","");
//
//            //获取节点id
//            String precinctId = "";
//            String precinctName = "";
//            if(array.length >= 2){
//                precinctId = array[1];
//                precinctName = array[2];
//            }
//
//            CompScadaCanvas canvas = new CompScadaCanvas();
//            canvas.setContent(content);
//            canvas.setName(scadaName);
//            canvas.setPrecinctId(precinctId);
//            canvas.setPictureType(1000);
//            canvas.setPageType(0);
//            ResMap resMap = this.saveScadaCanvas(canvas);
//
//            System.out.println("文件名："+scadaName+"******节点id："+precinctId+"******节点名称："+precinctName);
//        }
//
//
//        return null;
//    }
}
