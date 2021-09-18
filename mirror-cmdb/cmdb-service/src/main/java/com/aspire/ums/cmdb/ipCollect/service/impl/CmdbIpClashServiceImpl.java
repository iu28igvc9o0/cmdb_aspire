package com.aspire.ums.cmdb.ipCollect.service.impl;

import com.alibaba.fastjson.JSON;
import com.aspire.ums.cmdb.dict.payload.ConfigDict;
import com.aspire.ums.cmdb.dict.service.ConfigDictService;
import com.aspire.ums.cmdb.ipCollect.entity.BpmAutoStartTaskOrderParam;
import com.aspire.ums.cmdb.ipCollect.entity.ExcelTemplateParam;
import com.aspire.ums.cmdb.ipCollect.mapper.CmdbIpClashMapper;
import com.aspire.ums.cmdb.ipCollect.payload.entity.*;
import com.aspire.ums.cmdb.ipCollect.service.CmdbIpClashService;
import com.aspire.ums.cmdb.ipCollect.service.CmdbIpCollectService;
import com.aspire.ums.cmdb.ipCollect.service.CmdbVipCollectService;
import com.aspire.ums.cmdb.util.*;
import com.aspire.ums.cmdb.v3.config.payload.CmdbConfig;
import com.aspire.ums.cmdb.v3.config.service.ICmdbConfigService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @Author: fanshenquan
 * @Datetime: 2020/5/23 15:15
 */
@Slf4j
@Service("CmdbIpClashService")
@Transactional(rollbackFor = Exception.class)
public class CmdbIpClashServiceImpl implements CmdbIpClashService {

    @Autowired
    private CmdbIpClashMapper cmdbIpClashMapper;

    @Autowired
    private CmdbIpCollectService collectService;
    @Autowired
    private CmdbVipCollectService cmdbVipCollectService;
    @Autowired
    private ICmdbConfigService iCmdbConfigService;
    @Autowired
    private RestTemplateUtils restTemplateUtils;
    @Autowired
    private ConfigDictService configDictService;


    // 新cmdb-通用工单自动派单 申请人ID   osa2bpmGeneralAccount=10000128887873
    @Value("${ipclash.sendOrder.cmdb2bpm_general_account:}")
    String cmdb2bpm_general_account ;
    @Value("${ipclash.sendOrder.url:}")
    String cmdb2bpm_url;
    @Value("${bpmJwtTokenUrl:}")
    String bpmJwtTokenUrl;

    @Override
    public List<CmdbIpClashFindPageResponse> findData(CmdbIpClashFindPageRequest request) {
        return cmdbIpClashMapper.findData(request);
    }

    @Override
    public Integer findDataCount(CmdbIpClashFindPageRequest request) {
        return cmdbIpClashMapper.findDataCount(request);
    }

    @Override
    public CmdbIpClashTopTotalResponse findDataTopTotal(CmdbIpClashFindPageRequest request) {
        return cmdbIpClashMapper.findDataTopTotal(request);
    }

    @Override
    public <T extends BaseIpCollectEntity> void ipClashSave(BaseIpCollectEntity oldEntity, Class<T> clazz) {
        if(null == oldEntity || StringUtils.isEmpty(oldEntity.getMac())) {
            return;
        }
        // 1、获取新的存活IP信息,并判断mac是否有修改
        BaseIpCollectEntity newEntity = collectService.findDataByIpList(oldEntity.getIp(), clazz);
        if (null == newEntity || StringUtils.isEmpty(newEntity.getMac())) {
            return;
        }
        if (oldEntity.getMac().equals(newEntity.getMac())) {
            log.info("当前IP'{}'MAC地址没有更新", oldEntity.getIp());
            return;
        }

        // 2、排除虚拟IP和正常IP
        // 排除虚拟IP和正常IP
        if (judgeVirtualIP(oldEntity.getIp()) || judgeNormalIP(oldEntity.getIp())) {
            return;
        }

        // 3、判断IP是否已存在该冲突IP,是则更新关联ID,否则插入
        List<CmdbIpClashMainEntity> clashMainEntities = getEntityByIpAndTypeForMain(oldEntity.getIp(), "0");
        CmdbIpClashMainEntity clashMainEntity = clashMainEntities.isEmpty() ? null : clashMainEntities.get(0);
        String recordId = UUIDUtil.getUUID();
        //主表
        CmdbIpClashMainEntity mainEntity = new CmdbIpClashMainEntity();
        mainEntity.setRecordId(recordId);
        if (clashMainEntity == null) {
            mainEntity.setId(UUIDUtil.getUUID());
            mainEntity.setClashIp(oldEntity.getIp());
            mainEntity.setHandleStatus("0");
            mainEntity.setCollectType("2");
            insertMain(mainEntity);
        } else {
            mainEntity.setId(clashMainEntity.getId());
            updateMain(mainEntity);
            mainEntity.setHandleStatus(clashMainEntity.getHandleStatus());
        }
        //记录表
        CmdbIpClashRecordEntity recordEntity = new CmdbIpClashRecordEntity();
        recordEntity.setId(recordId);
        recordEntity.setOldMac(oldEntity.getMac());
        // recordEntity.setIsNotify("0");
        this.fillClashRecord(mainEntity.getId(), newEntity, recordEntity);
        insertRecord(recordEntity);

        //4、派单 处理状态 0-待处理,1-暂不处理,2-处理中,3-已处理
        if (mainEntity.getHandleStatus().equals("0") || mainEntity.getHandleStatus().equals("3")) {
            // todo 请求派单接口
            // String jobNumber = UUIDUtil.getUUID();
            //
            // recordEntity.setJobNumber(jobNumber);
            // updateRecord(recordEntity);
            //设置为处理中
            // mainEntity.setHandleStatus("2");
            // updateMain(mainEntity);
            sendBpmAndUpdate(Arrays.asList(mainEntity.getId()), Arrays.asList(recordId),
                    Arrays.asList(mainEntity.getClashIp()));
        } else {
            recordEntity.setJobNumber(clashMainEntity.getJobNumber());
            updateRecord(recordEntity);
        }

    }

    @Override
    public void ipClashSaveList(List<CmdbIpClashCreateRequest> requestList) {
        List<CmdbIpClashCreateRequest> addCreateRequests = Lists.newArrayList();
        List<CmdbIpClashCreateRequest> eidtCreateRequests = Lists.newArrayList();

        List<String> ipList = getIpByTypeForMain("1");
        requestList.stream().forEach(item -> {
            if (ipList.stream().anyMatch(e -> e.equals(item.getIp()))) {
                eidtCreateRequests.add(item);
            } else {
                addCreateRequests.add(item);
            }
        });

        // 主表不存在该冲突IP
        if (!CollectionUtils.isEmpty(addCreateRequests)) {
            saveClashIpNotExist(addCreateRequests);
        }
        // 主表已存在该冲突IP
        if (!CollectionUtils.isEmpty(eidtCreateRequests)) {
            saveClashIpExist(eidtCreateRequests);
        }

    }


    @Override
    public void insertMain(CmdbIpClashMainEntity entity) {
        cmdbIpClashMapper.insertMain(entity);
    }

    @Override
    public void batchInsertMain(List<CmdbIpClashMainEntity> entities) {
        cmdbIpClashMapper.batchInsertMain(entities);
    }

    @Override
    public void updateHandleStatus(CmdbIpClashUpdateRequest request) {
        cmdbIpClashMapper.updateHandleStatus(request);
    }

    @Override
    public List<String> getAllIpForMain() {
        return cmdbIpClashMapper.getAllIpForMain();
    }

    @Override
    public Boolean isExistByIpForMain(String ip) {
        int count = cmdbIpClashMapper.countByIpForMain(ip);
        return count > 0 ? true : false;
    }

    @Override
    public List<CmdbIpClashMainEntity> getEntityByIpForMain(String ip) {
        return cmdbIpClashMapper.getEntityByIpForMain(ip);
    }

    @Override
    public List<CmdbIpClashMainEntity> getEntityByIpAndTypeForMain(String ip, String collectType) {
        return cmdbIpClashMapper.getEntityByIpAndTypeForMain(ip, collectType);
    }

    @Override
    public List<String> getIpByTypeForMain(String colletType) {
        return cmdbIpClashMapper.getIpByTypeForMain(colletType);
    }


    @Override
    public void updateMain(CmdbIpClashMainEntity entity) {
        cmdbIpClashMapper.updateMain(entity);
    }

    @Override
    public void insertRecord(CmdbIpClashRecordEntity entity) {
        cmdbIpClashMapper.insertRecord(entity);
    }

    @Override
    public void batchInsertRecord(List<CmdbIpClashRecordEntity> entities) {
        cmdbIpClashMapper.batchInsertRecord(entities);
    }

    @Override
    public void updateRecord(CmdbIpClashRecordEntity entity) {
        cmdbIpClashMapper.updateRecord(entity);
    }

    /**
     * 填充冲突IP记录类.
     *
     * @param mainId       主表ID
     * @param entity       存活IP类
     * @param recordEntity 冲突IP记录类
     * @return
     */
    private void fillClashRecord(String mainId, BaseIpCollectEntity entity, CmdbIpClashRecordEntity recordEntity) {
        if (recordEntity == null) {
            recordEntity = new CmdbIpClashRecordEntity();
        }
        recordEntity.setId(StringUtils.isEmpty(recordEntity.getId()) ? UUIDUtil.getUUID() : recordEntity.getId());
        recordEntity.setMainId(mainId);
        recordEntity.setIp(entity.getIp());
        recordEntity.setNowMac(entity.getMac());
        recordEntity.setGateway(entity.getSrcip());
        recordEntity.setResource(entity.getResource());
        recordEntity.setCheckTime(entity.getTime());
    }

    private void fillClashMainAndRecord(String mainId, CmdbIpClashCreateRequest request,
                                        CmdbIpClashMainEntity mainEntity, CmdbIpClashRecordEntity recordEntity) {
        String recordId = UUIDUtil.getUUID();

        if (mainEntity != null) {
            mainEntity.setClashIp(request.getIp());
            mainEntity.setRecordId(recordId);
            mainEntity.setHandleStatus("0");
            mainEntity.setCollectType("1");
        }

        recordEntity.setId(recordId);
        recordEntity.setMainId(mainId);
        recordEntity.setIp(request.getIp());
        recordEntity.setOldMac(request.getOldMac());
        recordEntity.setNowMac(request.getOldMac() + "," +request.getNowMac());
        recordEntity.setGateway(request.getGateway());
        recordEntity.setResource(request.getResource());
        recordEntity.setCheckTime(request.getCheckTime());
    }

    /**
     * 判断是否为虚拟IP
     *
     * @param ip
     * @return
     */
    private Boolean judgeVirtualIP(String ip) {
        boolean flag = false;
        List<String> vipList = cmdbVipCollectService.findAllVip();
        if (vipList.stream().anyMatch(e -> e.equals(ip))) {
            log.info("当前IP'{}'属于虚拟IP", ip);
            flag = true;
        }
        return flag;
    }

    /**
     * 判断是否为正常IP
     *
     * @param ip
     * @return
     */
    private Boolean judgeNormalIP(String ip) {
        // boolean flag = true;

        return false;
    }

    /**
     * 主表不存在该冲突IP
     *
     * @param requestList
     */
    private void saveClashIpNotExist(List<CmdbIpClashCreateRequest> requestList) {
        List<String> ipBpmList = Lists.newArrayList();
        List<CmdbIpClashMainEntity> mainEntities = Lists.newArrayList();
        List<CmdbIpClashRecordEntity> recordEntities = Lists.newArrayList();
        requestList.stream().forEach(item -> {
            CmdbIpClashMainEntity mainEntity = new CmdbIpClashMainEntity();
            CmdbIpClashRecordEntity recordEntity = new CmdbIpClashRecordEntity();
            mainEntity.setId(UUIDUtil.getUUID());

            // 封装实体类
            fillClashMainAndRecord(mainEntity.getId(), item, mainEntity, recordEntity);
            mainEntities.add(mainEntity);
            recordEntities.add(recordEntity);
            ipBpmList.add(item.getIp());
        });
        // 保存冲突IP
        if (!CollectionUtils.isEmpty(mainEntities)) {
            batchInsertMain(mainEntities);
        }
        if (!CollectionUtils.isEmpty(recordEntities)) {
            batchInsertRecord(recordEntities);
        }
        // todo bpm发起工单
        List<String> mainIdList = mainEntities.stream().map(e -> e.getId()).collect(Collectors.toList());
        List<String> recordIdList = recordEntities.stream().map(e -> e.getId()).collect(Collectors.toList());
        sendBpmAndUpdate(mainIdList, recordIdList, ipBpmList);
    }

    /**
     * 主表已存在该冲突IP
     *
     * @param requestList
     */
    private void saveClashIpExist(List<CmdbIpClashCreateRequest> requestList) {
        List<String> mainIdList = Lists.newArrayList();//派单主体ID
        List<String> ipBpmList = Lists.newArrayList();//派单IP
        List<CmdbIpClashRecordEntity> bpmRecordEntities = Lists.newArrayList();//需要派单集合
        List<CmdbIpClashRecordEntity> insertRecordEntities = Lists.newArrayList();//所有需要保存集合
        requestList.stream().forEach(item -> {
            // 获取主表所有主体
            List<CmdbIpClashMainEntity> clashMainEntities = getEntityByIpAndTypeForMain(null, "1");
            clashMainEntities.stream().forEach(mainEntity -> {
                // 判断ip是否相同
                if (item.getIp().equals(mainEntity.getClashIp())) {
                    //0-待处理，3-已处理，可以派单
                    if (mainEntity.getHandleStatus().equals("0") || mainEntity.getHandleStatus().equals("3")) {
                        //需要派单获取工单号
                        mainIdList.add(mainEntity.getId());
                        ipBpmList.add(item.getIp());
                        CmdbIpClashRecordEntity recordEntity = new CmdbIpClashRecordEntity();
                        fillClashMainAndRecord(mainEntity.getId(), item, null, recordEntity);
                        bpmRecordEntities.add(recordEntity);
                    } else {//不需要派单
                        CmdbIpClashRecordEntity recordEntity = new CmdbIpClashRecordEntity();
                        fillClashMainAndRecord(mainEntity.getId(), item, null, recordEntity);
                        recordEntity.setJobNumber(mainEntity.getJobNumber());
                        insertRecordEntities.add(recordEntity);
                    }
                    return;
                }
            });//end clashMainEntities
        });//end requestList

        //插入数据
        insertRecordEntities.addAll(bpmRecordEntities);
        if (!CollectionUtils.isEmpty(insertRecordEntities)) {
            batchInsertRecord(insertRecordEntities);
        }
        // bpm派单，并更新数据
        List<String> recordIdList = bpmRecordEntities.stream().map(e -> e.getId()).collect(Collectors.toList());
        // todo 更新冲突IP为处理中状态
        sendBpmAndUpdate(mainIdList, recordIdList, ipBpmList);
    }

    /**
     * bpm派单、更新工单号和处理状态
     *
     * @param mainIdList
     * @param recordIdList
     * @param ipBpmList
     */
    private void sendBpmAndUpdate(List<String> mainIdList, List<String> recordIdList, List<String> ipBpmList) {
        if (!CollectionUtils.isEmpty(mainIdList)) {
            // todo 更新冲突IP为处理中状态
            // String jobNumber = UUIDUtil.getUUID();

            /*
            // 更新工单号
            Map<String, Object> recordMap = Maps.newHashMap();
            recordMap.put("jobNumber", jobNumber);
            recordMap.put("recordId", recordIdList);
            cmdbIpClashMapper.updateJobNumberByIdsForRecord(recordMap);
            // 更新处理状态
            Map<String, Object> mainMap = Maps.newHashMap();
            mainMap.put("handleStatus", "2");
            mainMap.put("mainId", mainIdList);
            cmdbIpClashMapper.updateStatusByIdsForMain(mainMap);
            */
        }
    }

    @Override
    public void saveIpClashList4Now(Map<String, List<CmdbIpCollectResponse>> ipClashList4Now) {
        if (null == ipClashList4Now) {
            return;
        }
        List<String> vipList = cmdbVipCollectService.findAllVip();
        // 获取当前系统中已经存在冲突IP列表
        List<String> clashIpTimeKeyList = cmdbIpClashMapper.getClashIpTimeKeyList();
        List<CmdbIpClashMainEntity> mainEntityList = new ArrayList<>();
        List<CmdbIpClashRecordEntity> recordEntityList = new ArrayList<>();
        Map<String,Map<String,String>> param = new HashMap<>();
        for (Map.Entry<String, List<CmdbIpCollectResponse>> map : ipClashList4Now.entrySet()) {
            // 据ip检测时间戳进行筛选，已经录入的冲突IP不需要重新录入列表
            if (clashIpTimeKeyList.contains(map.getKey())) {
                continue;
            }
            List<CmdbIpCollectResponse> valueList = map.getValue();
            // 判断mac地址是否有变化
            CmdbIpCollectResponse ipCollect0 = valueList.get(0);
            String gateway = ipCollect0.getGateway();
            String resource = ipCollect0.getResource();
            String mac0 = ipCollect0.getMac().replaceAll("[\\pP\\p{Punct}]", "");
            StringBuilder sb = new StringBuilder(ipCollect0.getMac());
            Date time = ipCollect0.getTime();
            for (int i=1;i < valueList.size();i++) {
                CmdbIpCollectResponse ipCollect = valueList.get(i);
                String ip = ipCollect.getIp();
                String mac = ipCollect.getMac().replaceAll("[\\pP\\p{Punct}]", "");
                if (!mac0.equalsIgnoreCase(mac)) { // mac地址有变化
                    if (vipList.stream().anyMatch(e -> e.equals(ip))) { // 是虚拟IP
                        log.info("当前IP'{}'属于虚拟IP", ip);
                        continue;
                    }
                    sb.append(",").append(ipCollect.getMac());
                    if ("ip_address_pool".equals(ipCollect.getSource())) {
                        gateway = ipCollect.getGateway();
                        resource = ipCollect.getResource();
                        time = ipCollect.getTime();
                    }
                }
            }
            String allMac = sb.toString();
            if (allMac.contains(",")) {
                //主表
                String recordId = UUIDUtil.getUUID(); // 记录表详情ID
                CmdbIpClashMainEntity mainEntity = new CmdbIpClashMainEntity();
                mainEntity.setId(UUIDUtil.getUUID());
                mainEntity.setRecordId(recordId);
                mainEntity.setClashIp(ipCollect0.getIp());
                mainEntity.setHandleStatus("0");
                mainEntity.setCollectType("2");
                mainEntityList.add(mainEntity);
                //记录表
                CmdbIpClashRecordEntity recordEntity = new CmdbIpClashRecordEntity();
                recordEntity.setId(recordId);
                recordEntity.setOldMac(valueList.get(0).getMac());
                recordEntity.setMainId(mainEntity.getId());
                recordEntity.setIp(ipCollect0.getIp());
                recordEntity.setNowMac(allMac);
                recordEntity.setGateway(gateway);
                recordEntity.setResource(resource);
                recordEntity.setCheckTime(time);
                recordEntity.setStatisticFlag("0");
                recordEntity.setTimeKey(map.getKey());
                recordEntityList.add(recordEntity);

                // 构建二次校验的参数
                String tmp = gateway + "-" + resource;
                Map<String, String> clashMap = param.get(tmp);
                if (null == clashMap) {
                    clashMap = new HashMap<>();
                    clashMap.put("ip_list",ipCollect0.getIp());
                    clashMap.put("device_ip",gateway);
                    clashMap.put("resource_pool",resource);
                } else {
                    String ipList = clashMap.get("ip_list");
                    ipList = ipList + "," + ipCollect0.getIp();
                    clashMap.put("ip_list",ipList);
                }
                param.put(tmp,clashMap);
            }
        }
        if (!mainEntityList.isEmpty()) {
            batchInsertMain(mainEntityList);
            batchInsertRecord(recordEntityList);
            // 获取自动化版本ID
            CmdbConfig cmdbConfig = iCmdbConfigService.getConfigByCode("automatevid");
            if(null != cmdbConfig) {
                String vid = cmdbConfig.getConfigValue();
                if (StringUtils.isEmpty(vid)) {
                    vid = "f0d6db1a1ae61e32f6c70205e7557898";
                }
                buildAutomateRequest(param,vid);
            }
        } else {
            log.info("此次冲突IP比对无新增数据");
        }
    }

    @Override
    public int getClashMain4Now() {
        return cmdbIpClashMapper.getClashMain4Now();
    }

    @Override
    public void rebuildClashList(List<CmdbIpClashRebuildRequest> list) {
        try {
            String paramJson = JsonUtil.toJacksonJson(list);
            Map<String,String> insertMap = new HashMap<>();
            insertMap.put("id",UUIDUtil.getUUID());
            insertMap.put("requestContent",paramJson);
            insertMap.put("type","1");
            cmdbIpClashMapper.insertRebuildClash(insertMap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<CmdbIpClashMainEntity> mainEntityList = new ArrayList<>();
        List<CmdbIpClashRecordEntity> recordEntityList = new ArrayList<>();
        if (list.size() == 1) {
            return;
        }
        CmdbIpClashRebuildRequest clash0 = list.get(0);
        StringBuilder sb = new StringBuilder(clash0.getMac());
        String mac0 = clash0.getMac().replaceAll("[\\pP\\p{Punct}]", "");
        for (int i=1;i < list.size();i++) {
            CmdbIpClashRebuildRequest clash = list.get(i);
            String mac = clash.getMac().replaceAll("[\\pP\\p{Punct}]", "");
            if (!mac0.equalsIgnoreCase(mac)) { // mac地址有变化
                sb.append(",").append(clash.getMac());
            }
        }
        String allMac = sb.toString();
        if (allMac.contains(",")) {
            //主表
            String recordId = UUIDUtil.getUUID();
            CmdbIpClashMainEntity mainEntity = new CmdbIpClashMainEntity();
            mainEntity.setId(UUIDUtil.getUUID());
            mainEntity.setRecordId(recordId);
            mainEntity.setClashIp(clash0.getIp());
            mainEntity.setHandleStatus("0");
            mainEntity.setCollectType("2");
            mainEntityList.add(mainEntity);
            //记录表
            CmdbIpClashRecordEntity recordEntity = new CmdbIpClashRecordEntity();
            String format = DateUtils.format(clash0.getTime(), "yyyy-MM-dd HH");
            String timeKey = clash0.getIp() + "-" + clash0.getResource() + "-" + format;
            recordEntity.setId(recordId);
            recordEntity.setOldMac(clash0.getMac());
            recordEntity.setMainId(mainEntity.getId());
            recordEntity.setIp(clash0.getIp());
            recordEntity.setNowMac(allMac);
            recordEntity.setGateway(clash0.getGateway());
            recordEntity.setResource(clash0.getResource());
            recordEntity.setCheckTime(clash0.getTime());
            recordEntity.setTimeKey(timeKey);
            recordEntity.setStatisticFlag("1");
            recordEntityList.add(recordEntity);
        }
        if (!mainEntityList.isEmpty()) {
            batchInsertMain(mainEntityList);
            batchInsertRecord(recordEntityList);
        }
    }

    /**
     * 构建冲突IP复核比对请求参数
     * @param param 需要比对的初始冲突IP
     */
    private void buildAutomateRequest(Map<String,Map<String,String>> param,String vid) {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        try {
            // 构建执行IP
            Map<String,String> idcMap = new HashMap<>();
            idcMap.put("资源池","10.153.1.76");
            idcMap.put("融合通信资源池","10.1.220.66");
            idcMap.put("资源池MM独立区","192.168.42.132");
            idcMap.put("萝岗数据中心","10.3.0.2");
            idcMap.put("呼和浩特资源池","10.2.0.12");
            for (Map.Entry<String, Map<String, String>> mapEntry : param.entrySet()) {
                String key = mapEntry.getKey();
                Map<String, String> value = mapEntry.getValue();
                String[] split = key.split("-");
                String idcIp = idcMap.get(split[1]);
                executorService.execute(() -> sendAutomateRequest(value,idcIp,vid));
            }
        } catch (Exception e) {
            log.error("冲突IP二次校验接口发起失败",e);
        } finally {
            executorService.shutdown();
        }
    }

    private void sendAutomateRequest(Map<String,String> value,String idcIp,String vid) {
        Map<String, String> headers = RestTemplateUtils.buildAutomateHeaders();
        String paramJson = RestTemplateUtils.buildAutomateBody(value,idcIp,vid);
        String retJson = restTemplateUtils.postJson("http://172.16.108.104/tools/execution", paramJson, headers);
        String convertJson = UnicodeConvertUtils.unicodeToCn(retJson);
        Map<String,String> insertMap = new HashMap<>();
        insertMap.put("id",UUIDUtil.getUUID());
        insertMap.put("requestContent",paramJson);
        insertMap.put("reponseContent",retJson);
        insertMap.put("convertContent",convertJson);
        insertMap.put("type","0");
        cmdbIpClashMapper.insertRebuildClash(insertMap);
    }

    @Override
    public void buidThatDayPendingIpAndSendOrder() {
        String taskName ="[每日巡检][状态为待处理的IP]";
        log.info("================{},开始查询状态为待处理的IP数据列表==========", taskName);
        List<Map<String,Object>> resultList =null;
        try {
            CmdbIpClashFindPageRequest cmdbIpClashFindPageRequest =createParams();
            resultList=cmdbIpClashMapper.findPendingIpList(cmdbIpClashFindPageRequest);
            try {
                log.info("pending Ip autoSendOrder list.size==={}", resultList.size());
                log.info("================{},派发工单任务开始========================", taskName);
                handleAutoOrder(resultList);
                log.info("================{},派发工单任务完成========================", taskName);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("================派发工单任务异常========================"+e);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("数据来源为系统比对,状态为待处理的IP列表 查询失败"+e);
        }
    }
    //构建查询条件  1.数据来源：checkType  0-系统比对  2.状态：handleStatus  0-待处理  3.当天时间
    private CmdbIpClashFindPageRequest createParams() throws java.text.ParseException {
        CmdbIpClashFindPageRequest cmdbIpClashFindPageRequest = new CmdbIpClashFindPageRequest();
        //当天  2020-12-07
        SimpleDateFormat todayFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date d = new Date();
        String today = todayFormat.format(d);
        //today="2020-12-02";
        //组装查询条件to cmdbIpClashFindPageRequest
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        cmdbIpClashFindPageRequest.setHandleStatus("0");
        cmdbIpClashFindPageRequest.setCheckType("0");
        Date startTime = sdf.parse(today+" 00:00:00");
        cmdbIpClashFindPageRequest.setStartTime(startTime);
        Date endTime = sdf.parse(today+" 23:59:59");
        cmdbIpClashFindPageRequest.setEndTime(endTime);
        return cmdbIpClashFindPageRequest;
    }

    private void handleAutoOrder(List<Map<String,Object>> list) throws Exception {
        if (CollectionUtils.isEmpty(list)) {
            log.info("状态为待处理的IP列表 无数据，派单失败");
            return;
        }
        String result= doSendAutoOrder(list);
        log.info("autoStartTaskOrder response==={}", result);
        Map<String, Object> resultMap = parseJSON2Map(JSONObject.fromObject(result));
        if (MapUtils.isEmpty(resultMap)) {
            return;
        }
        //更新 冲突ip列表  的工单号
        updateOrderSendStatus(list, resultMap);
    }

    /**
     * 执行发送派发工单到BPM.
     * @param
     * @return
     */
    protected String doSendAutoOrder(List<Map<String,Object>> resultList) throws Exception {
        BpmAutoStartTaskOrderParam param = buildBpmAutoTaskParam();
        Object[] objects = getAttachmentContent(resultList);
        String fileName = (String) objects[0];
        byte[] content = (byte[]) objects[1];
        String json = JSON.toJSONString(param);
        return httpPostSend(json, content, fileName);
    }

    private String httpPostSend(String json, byte[] content, String fileName)  {
        CloseableHttpResponse resp = null;
        String respondBody = null;
        try {
            final CloseableHttpClient httpClient = HttpClients.createDefault();
            final HttpPost httpPost = new HttpPost(cmdb2bpm_url);
            Map<String, String> adminHeader = getBpmUserToken("dw_jzwgxt");
            httpPost.addHeader("Authorization",adminHeader.get("Authorization") );
            //附件参数需要用到的请求参数实体构造器
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addBinaryBody("file", content, ContentType.MULTIPART_FORM_DATA, fileName);// 文件流
            ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
            StringBody jsonBody = new StringBody(json,contentType);
            builder.addPart("param", jsonBody);
            StringBody  fileNameBody = new StringBody(fileName,contentType);
            builder.addPart("xqfjmc", fileNameBody);
            log.info("================派发工单任务参数：{},需求附件名称{}========================", json,fileName);
            HttpEntity httpEntity = builder.build();
            httpPost.setEntity(httpEntity);
            resp = httpClient.execute(httpPost);
            respondBody = EntityUtils.toString(resp.getEntity());
        } catch (IOException | ParseException e) {
            //日志信息及异常处理
            log.error("执行HTTP响应时抛出异常，需要关注", e);
        }finally {
            if (resp != null) {
                try {
                    //关闭请求
                    resp.close();
                } catch (IOException e) {
                    log.error("关闭HTTP响应时抛出异常，需要关注", e);
                }
            }
        }
        return respondBody;
    }

    /**
     * 获取要发送到BPM的附件内容.
     *
     * @param resultList
     *            结果列表
     * @return
     */
    protected Object[] getAttachmentContent(List<Map<String,Object>> resultList) throws Exception {
        ExcelTemplateParam excelTemplateParam = buildExcelTemplateParam();
        Object[] obj = new Object[2];
        OutputStream out = null;
        String[] headerArray = excelTemplateParam.getHeaders();
        String[] keyArray = excelTemplateParam.getKeys();
        String date = DateUtils.format(new Date(), DateUtils.DATE_PLAIN_FORMAT);
        String title = excelTemplateParam.getSheetName() + "_" + date;
        obj[0] = title + ".xls";
        String fileName = excelTemplateParam.getFilePath() + obj[0];
        ExportExcelUtils eeu = new ExportExcelUtils();
        Workbook book = new SXSSFWorkbook(128);
        try {
            FileUtils.forceMkdir(new File(fileName).getParentFile());
            eeu.exportExcel(book, 0, title, headerArray, resultList, keyArray);
            out = new FileOutputStream(fileName);
            book.write(out);
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
        byte[] content = FileUtils.readFileToByteArray(new File(fileName));
        obj[1] = content;
        return obj;
    }

    public ExcelTemplateParam buildExcelTemplateParam() {
        String[] headerArray = {"\t\t\t检测时间\t\t\t", "\t\t\t\t冲突IP\t\t\t\t", "绑定MAC地址", "累计切换次数",
                "\t系统推测\t", "网关设备IP", "所属资源池", "处理状态", "原因说明",
                "操作人", "\t\t\t操作时间\t\t\t", "是否已通知", "\t\t\t工单号\t\t\t", "\t\t来源\t\t"};
        String[] keyArray = {"check_time", "clash_ip", "now_mac", "total",
                "system_infer", "gateway", "resource", "handle_status", "not_handle_reason",
                "operator", "update_time", "is_notify", "job_number", "collect_type"};
        String sheetName = "状态为待处理的冲突IP列表";
        String filePath = "/opt/aspire/attachment/bmpx/";
        return new ExcelTemplateParam(keyArray, headerArray, sheetName, filePath);
    }

    public BpmAutoStartTaskOrderParam buildBpmAutoTaskParam() {
        // 新发现Ip 派发通用任务工单到网络组
        String date = DateUtils.format(new Date(), DateUtils.DATE_PLAIN_FORMAT);
        //String sqrId = cmdb2bpm_general_account;
        //String id1 = configDictMapper.getIdByNoteAndCol("未存活", "survival_status");
        //String id2 = configDictMapper.getIdByNoteAndCol("已存活", "survival_status");
        /*
        String sqrId = "10000007184871";
        String businessLevel1Id = "6390449926644736";
        String businessLevel2Id = "7665430513984512";
        String ydjkrRoleID = "8503924842767360";
        String roleID = "10000004784952";
        */
        ConfigDict sqr = configDictService.getDictByColNameAndDictCode("bpm_networrk_sqrId", "冲突IP稽核_bpm派单-申请人ID");
        ConfigDict business1 = configDictService.getDictByColNameAndDictCode("bpm_networrk_businessLevel1Id", "冲突IP稽核_bpm派单-独立业务线");
        ConfigDict business2 = configDictService.getDictByColNameAndDictCode("bpm_networrk_businessLevel2Id", "冲突IP稽核_bpm派单-独立业务子模块");
        ConfigDict ydjkrRole = configDictService.getDictByColNameAndDictCode("bpm_networrk_ydjkrRoleID", "冲突IP稽核_bpm派单-移动接口人角色");
        ConfigDict role = configDictService.getDictByColNameAndDictCode("bpm_networrk_roleID", "冲突IP稽核_bpm派单-实施人员角色");
        String sqrId = sqr != null ? sqr.getValue() : "10000007184871";// 陈俊良
        String businessLevel1Id = business1 != null ? business1.getValue() : "6390449926644736";// 61 资源池_网络
        String businessLevel2Id = business2 != null ? business2.getValue() : "7665430513984512";// 01云资源池
        String ydjkrRoleID = ydjkrRole != null ? ydjkrRole.getValue() : "8503924842767360";//陈俊良角色
        String roleID = role != null ? role.getValue() : "10000004784952";//网络组角色
        String xqms = "请核对冲突IP列表并及时处理相关IP，如果不属于冲突IP，请登录IP地址管理系统-》冲突IP报表，把IP改为暂不处理状态，则该IP不再派发工单；\n";
        String sfxysjlsp = "2";
        String sfxybmldsp = "2";
        String gdbt = "【冲突IP稽核】[状态为待处理的冲突IP列表]工单-" + date;
        return new BpmAutoStartTaskOrderParam(sqrId, gdbt, businessLevel1Id, businessLevel2Id, ydjkrRoleID, roleID, xqms, sfxysjlsp,
                sfxybmldsp);
    }

    private void updateOrderSendStatus(List<Map<String,Object>> resultList, Map<String, Object> resultMap) {
        String resultCode = (String) resultMap.get("code");
        if ("0".equals(resultCode)) {
            String orderNo = (String) resultMap.get("globalFlowNo");
            Map<String, Object> objectMap = Maps.newHashMap();
            objectMap.put("orderNo", orderNo);
            int count = 200;// 一次提交200条
            int start, stop;
            int loop = (int) Math.ceil(resultList.size() / (double) count);
            List<String> idList = Lists.newArrayList();
            for (int i = 0; i < loop; i++) {
                idList.clear();
                start = i * count;
                stop = Math.min(i * count + count - 1, resultList.size() - 1);
                log.info("range:{}-{}", start, stop);
                for (int j = start; j <= stop; j++) {
                    idList.add(resultList.get(j).get("record_id").toString());
                }
                objectMap.put("idList", idList);
                cmdbIpClashMapper.updateIpClashOrderStatus(objectMap);
            }
        }

    }

    /**
     * 将json对象转换为HashMap
     *
     * @param json
     * @return
     */
    public static Map<String, Object> parseJSON2Map(JSONObject json) {
        Map<String, Object> map = new HashMap<String, Object>();
        // 最外层解析
        for (Object k : json.keySet()) {
            Object v = json.get(k);
            // 如果内层还是json数组的话，继续解析
            if (v instanceof JSONArray) {
                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                Iterator<JSONObject> it = ((JSONArray) v).iterator();
                while (it.hasNext()) {
                    JSONObject json2 = it.next();
                    list.add(parseJSON2Map(json2));
                }
                map.put(k.toString(), list);
            } else if (v instanceof JSONObject) {
                // 如果内层是json对象的话，继续解析
                map.put(k.toString(), parseJSON2Map((JSONObject) v));
            } else {
                // 如果内层是普通对象的话，直接放入map中
                map.put(k.toString(), v);
            }
        }
        return map;
    }

    public  Map<String, String> getBpmUserToken(String userLoginName) {
        try {
            String name = Base64Util.getBase64(userLoginName);
            String tokenUrl = bpmJwtTokenUrl + "?name=" + name;
            String resultToken = HttpUtil.post(tokenUrl, "");
            com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(resultToken);
            String jwtToken = jsonObject.getString("token");
            Map<String, String> header = new HashMap<>();
            header.put("Authorization", "Bearer " + jwtToken);
            return header;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
