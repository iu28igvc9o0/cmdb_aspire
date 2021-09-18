package com.aspire.ums.cmdb.maintenance.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.ftp.service.FtpService;
import com.aspire.ums.cmdb.maintenance.mapper.ProduceInfoConcatMapper;
import com.aspire.ums.cmdb.maintenance.mapper.ProduceInfoMapper;
import com.aspire.ums.cmdb.maintenance.payload.*;
import com.aspire.ums.cmdb.maintenance.service.IProduceInfoService;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.*;

/**
 * @Dscription: 维保厂商Service
 * @Author: PJX
 * @Version: V1.0
 */
@Slf4j
@Service
@Transactional
public class ProduceInfoServiceImpl implements IProduceInfoService {

    @Autowired
    private ProduceInfoMapper produceInfoMapper;

    @Autowired
    private ProduceInfoConcatMapper concatMapper;

    @Override
    public Result<ProduceInfoResq> selectProduceByPage(ProduceInfoRequest produceInfoRequest) {

        Result<ProduceInfoResq> response = new Result<ProduceInfoResq>();

        int dataCount = produceInfoMapper.getProduceInfoCount(produceInfoRequest);
        response.setCount(dataCount);
        if (dataCount > 0) {
            List<ProduceInfoResq> data = produceInfoMapper.getProduceInfoByPage(produceInfoRequest);
            response.setData(data);
        }

        return response;
    }

    @Override
    public ProduceInfoResq getProduceById(String id) {
        return produceInfoMapper.getProduceInfoById(id);
    }

    @Override
    public String selectId(String produce, String type) {
        return produceInfoMapper.selectId(produce,type);
    }

    @Override
    public int selectConcat(String produceId, String name, String phone, String typeId) {
        return concatMapper.selectConcat(produceId, name, phone, typeId);
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class, SQLException.class})
    public void addConcat(Concat concat) {
        validConcat(concat);
        concat.setId(UUIDUtil.getUUID());
        concat.setCreateTime(new Date());
        insertRelations(concat);
        concatMapper.add(concat);
    }

    @Override
    public void deleteConcat(String id) {
        concatMapper.delConcatById(id);
        concatMapper.delRelByConcatId(id);
    }


    private void validConcat(Concat concat) {
        if (StringUtils.isEmpty(concat.getName())) {
            throw new RuntimeException("联系人不能为空");
        }
        if (StringUtils.isEmpty(concat.getPhone())) {
            throw new RuntimeException("电话不能为空");
        }
        if (StringUtils.isEmpty(concat.getEmail())) {
            throw new RuntimeException("邮箱不能为空");
        }
        if (concat.getPersonTypes() == null || concat.getPersonTypes().size() == 0) {
            throw new RuntimeException("人员类型不能为空");
        }
        for (ConfigDict type : concat.getPersonTypes()) {
            List<Concat> concats= concatMapper.queryConcat(concat.getProduceId(),
                    null,
                    concat.getName(),
                    concat.getPhone(),
                    null)  ;
            if (concats != null && concats.size() > 0) {
                String resultId = concats.get(0).getId();
                if (StringUtils.isNotEmpty(concat.getId()) && !resultId.equals(concat.getId())) {
                    throw new RuntimeException("当前厂商已存在同名同电话的联系人");
                } else  if(StringUtils.isEmpty(concat.getId())) {
                    throw new RuntimeException("当前厂商已存在同名同电话的联系人");
                }
            }
        }


    }

    @Override
    public JSONObject queryExportData(Map<String, Object> sendRequest) {
        List<LinkedHashMap> list = produceInfoMapper.queryExportData(sendRequest);
        int dataCount = (null == list ? 0 : list.size());
        JSONObject returnJson = new JSONObject();
        returnJson.put("total", dataCount);
        returnJson.put("dataList", list);
        return returnJson;
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class, SQLException.class})
    public void updateConcat(Concat concat) {
        validConcat(concat);
        concatMapper.delRelByConcatId(concat.getId());
        insertRelations(concat);
        concatMapper.update(concat);
    }

    private void insertRelations(Concat concat) {
        List<ConfigDict> personTypes = concat.getPersonTypes();
        List<MaintenProConcatRelation> relations = new ArrayList<>();
        if (personTypes != null && personTypes.size() > 0) {
            for (ConfigDict type : personTypes)  {
                MaintenProConcatRelation relation = new MaintenProConcatRelation();
                relation.setId(UUIDUtil.getUUID());
                relation.setConcatId(concat.getId());
                relation.setTypeId(type.getId());
                relations.add(relation);
            }
            concatMapper.insertRelationByBatch(relations);
        }
    }

    @Override
    public List<ProduceInfoResq> queryProduceInfoList() {
        return produceInfoMapper.queryProduceInfoList();
    }


    @Override
    public List<Concat> queryConcat(String produceId, String personType) {
        return concatMapper.queryConcat(produceId, personType, null, null, null);
    }

    @Override
    public Map<String, Object> insert(ProduceInfoResq produceInfoResq) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            if (null != produceInfoResq) {
                String produceId = produceInfoResq.getId();
                if (StringUtils.isEmpty(produceId)) {
                    produceId = UUIDUtil.getUUID();
                }
                String produce = produceInfoResq.getProduce();
                String type = produceInfoResq.getType();
//                int a = produceInfoMapper.selectProduce(produceId, produce, type);
                String  existId = produceInfoMapper.selectId(produce, type);
                // <1 表示不存在
                if (StringUtils.isEmpty(existId) || existId.equals(produceInfoResq.getId())) {
                    produceInfoMapper.add(produceInfoResq);
                    if (StringUtils.isEmpty(existId)) {
                        result.put("success", true);
                        result.put("message", "新增厂商成功");
                        result.put("id", produceId);
                    } else {
                        result.put("success", true);
                        result.put("message", "更新厂商成功");
                        result.put("id", produceId);
                    }
                } else {
                    if (StringUtils.isEmpty(existId) && !existId.equals(produceInfoResq.getId())) {
                        result.put("success", false);
                        result.put("message", "新增厂商失败，厂商已存在");
                    } else if (StringUtils.isNotEmpty(existId) && !existId.equals(produceInfoResq.getId())) {
                        result.put("success", false);
                        result.put("message", "更新厂商失败，厂商已存在");
                    }
                }

            }
        } catch (Exception e) {
            log.error("ProduceInfoServiceImpl insert() error {}" , e);
            result.put("success", false);
            result.put("message", "新增厂商失败" + e.getMessage());
        }
        return result;
    }

    @Override
    public Map<String, Object> update(ProduceInfoResq produceInfoResq) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            if (null != produceInfoResq) {
                int a = produceInfoMapper.selectProduce(produceInfoResq.getId(), produceInfoResq.getProduce(), produceInfoResq.getType());
                if (a < 1) {
                    produceInfoMapper.update(produceInfoResq);
                    result.put("success", true);
                    result.put("message", "修改厂商成功");
                } else {
                    result.put("success", false);
                    result.put("message", "修改厂商失败，厂商已存在");
                }
            }
        } catch (Exception e) {
            log.error("ProduceInfoServiceImpl update() error {}" , e);
            result.put("success", false);
            result.put("message", "修改厂商失败");
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class, SQLException.class})
    public void deleteProduce(String produceId) {
            String[] aa = produceId.split(",");
            List<String> ids = Lists.newArrayList();
            for (String id : aa) {
                ids.add(id);
            }

            // 先根据厂商删除联系人关系
        concatMapper.batchDelRelByProduceId(ids);
        // 再根据厂商删除联系人
        concatMapper.batchDelByProduceId(ids);
        // 再更改厂商状态为删除
        produceInfoMapper.delete(ids);

    }



}
