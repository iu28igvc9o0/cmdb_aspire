package com.aspire.ums.cmdb.v2.assessment.service.impl;

import com.aspire.ums.cmdb.assessment.payload.CmdbDeviceType;
import com.aspire.ums.cmdb.assessment.payload.CmdbItDeviceCount;
import com.aspire.ums.cmdb.assessment.payload.CmdbProduceAssessment;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.aspire.ums.cmdb.v2.assessment.DeviceConst;
import com.aspire.ums.cmdb.v2.assessment.mapper.CmdbDeviceTypeMapper;
import com.aspire.ums.cmdb.v2.assessment.mapper.CmdbItDeviceCountMapper;
import com.aspire.ums.cmdb.v2.assessment.mapper.CmdbProduceAssessmentMapper;
import com.aspire.ums.cmdb.v2.assessment.service.ICmdbItDeviceCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* 描述：
* @author
* @date 2019-06-25 16:07:55
*/
@Service
public class CmdbItDeviceCountServiceImpl implements ICmdbItDeviceCountService {

    @Autowired
    private CmdbItDeviceCountMapper mapper;

    @Autowired
    private CmdbProduceAssessmentMapper produceAssessmentMapper;

    @Autowired
    private CmdbDeviceTypeMapper deviceTypeMapper;
    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    public  List<CmdbItDeviceCount> listByEntity(CmdbItDeviceCount deviceCount) {
        return mapper.listByEntity(deviceCount);
    }

    @Override
    public Integer listCount(CmdbItDeviceCount deviceCount) {
        return mapper.listCount();
    }

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回指定ID的数据信息
     */
    public CmdbItDeviceCount get(CmdbItDeviceCount entity) {
        return mapper.get(entity);
    }

    /**
     * 新增实例
     * @param entities 实例数据
     * @return
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class, SQLException.class})
    public void insert(List<CmdbItDeviceCount> entities) {
        Date now = new Date();
        List<String> deleteIds = new ArrayList<>();
        List<CmdbItDeviceCount> deviceCounts = new ArrayList<>();
        for (CmdbItDeviceCount deviceCount : entities) {
            validData(deviceCount);
            // 清理数量为空的数据
            if (StringUtils.isEmpty(deviceCount.getCount())) {
                // 有id需要进行删除
                if (StringUtils.isNotEmpty(deviceCount.getId())) {
                    deleteIds.add(deviceCount.getId());
                }
            } else {
                if (StringUtils.isEmpty(deviceCount.getId())) {
                    deviceCount.setId(UUIDUtil.getUUID());
                    deviceCount.setCreateTime(now);
                    deviceCount.setStatus(DeviceConst.DEVICE_STATUS_SAVE);
                } else {
                    deviceCount.setUpdateTime(now);
                }
                deviceCounts.add(deviceCount);
            }
        }
        if (deleteIds.size() > 0) {
            // 先删除要被删除设备信息的相关评分
            produceAssessmentMapper.deleteByBatch(deleteIds);
            mapper.deleteByBatch(deleteIds);
        }
        if (deviceCounts.size() > 0) {
            mapper.insertByBatch(deviceCounts);
        }

//        Date now = new Date();
//        List<CmdbItDeviceCount> sourceCounts = new ArrayList<>();
//        CmdbItDeviceCount count = new CmdbItDeviceCount();
//        List<String> deleteAssCountIds = new ArrayList<>();
//        if (entities.size() > 0) {
//          count.setProvince(entities.get(0).getProvince());
//            sourceCounts = mapper.listByEntity(count);
//        }
//        for (CmdbItDeviceCount deviceCount : entities) {
//            validData(deviceCount);
//            if (StringUtils.isEmpty(deviceCount.getCount()) &&
//                    deviceCount.getProduceAssessment() != null &&
//                    deviceCount.getProduceAssessment().size() > 0 &&
//                   StringUtils.isNotEmpty(deviceCount.getProduceAssessment().get(0).getDeviceCountId())) {
//                deleteAssCountIds.add(deviceCount.getId());
//            }
//            if (StringUtils.isEmpty(deviceCount.getId())) {
//                sourceCounts.forEach(source -> {
//                    if (deviceCount.getProduce().equals(source.getProduce()) && deviceCount.getDeviceTypeId().equals(source.getDeviceTypeId())) {
//                        throw new RuntimeException("插入厂家(" + deviceCount.getProduce() + ")和类型有重复，请查看数据");
//                    }
//                });
//                deviceCount.setId(UUIDUtil.getUUID());
//                deviceCount.setCreateTime(now);
//                deviceCount.setStatus(DeviceConst.DEVICE_STATUS_SAVE);
//            } else {
//                deviceCount.setUpdateTime(now);
//            }
//            if (StringUtils.isNotEmpty(deviceCount.getStatus())) {
//                if (!DeviceConst.APPROVESTATUS.contains(deviceCount.getStatus())) {
//                    throw new RuntimeException(deviceCount.getStatus() + " 为非法状态! 状态（1审批通过，2保存中，0审批驳回，-1录入待审批）");
//                }
//            }
//        }
//        if (deleteAssCountIds.size() > 0) {
//            produceAssessmentMapper.deleteByBatch(deleteAssCountIds);
//        }
//       mapper.insertByBatch(entities);
    }

    private void validData(CmdbItDeviceCount deviceCount) {
        if (StringUtils.isEmpty(deviceCount.getProduce())) {
            throw new RuntimeException("插入厂家不为空，请检查数据");
        }
        if (StringUtils.isEmpty(deviceCount.getProvince())) {
            throw new RuntimeException("插入省份不为空，请检查数据");
        }
        if (StringUtils.isEmpty(deviceCount.getCreateUsername())) {
            throw new RuntimeException("创建用户不能为空，请检查数据");
        }
        if (StringUtils.isEmpty(deviceCount.getCreateUserPhone())) {
            throw new RuntimeException("创建用户电话不能为空，请检查数据");
        }
        if (StringUtils.isEmpty(deviceCount.getDeviceTypeId())) {
            throw new RuntimeException("设备id不能为空，请检查数据");
        }
        if (deviceCount.getStatus() != null) {
            if (!DeviceConst.APPROVESTATUS.contains(deviceCount.getStatus())) {
                throw new RuntimeException(deviceCount.getStatus() + " 为非法状态! 状态（1审批通过，2保存中，0审批驳回，-1录入待审批）");
            }
        }

    }

    /**
     * 修改实例(提交或审核)
     * @param entity 实例数据
     * @return
     */
    public void update(CmdbItDeviceCount entity) {
        if (entity.getStatus() == -1) {
            List<CmdbItDeviceCount> deviceCounts = new ArrayList<>();
            List<CmdbProduceAssessment> produceAssessments = new ArrayList<>();
            CmdbItDeviceCount deviceCount = new CmdbItDeviceCount();
            deviceCount.setProvince(entity.getProvince());
            deviceCount.setQuarter(entity.getQuarter());
            deviceCounts = mapper.listByEntity(deviceCount);
            produceAssessments = produceAssessmentMapper.listByEntity(new CmdbProduceAssessment());
            CmdbDeviceType deviceType = new CmdbDeviceType();
            deviceType.setType("child");
            List<CmdbDeviceType> types = deviceTypeMapper.listByEntity(deviceType);
            for (CmdbItDeviceCount count : deviceCounts) {
                for (CmdbDeviceType type :  types) {
                    if (type.getId().equals(count.getDeviceTypeId()) && !"容量(TB)".equals(type.getName())) {
                        if (StringUtils.isNotEmpty(count.getCount()) ) {
                            int flag = 0;
                            for (CmdbProduceAssessment ass : produceAssessments) {
                                if (ass.getDeviceCountId().equals(count.getId())) {
                                    flag ++;
                                }
                            }
                            if (flag != 6) {
                                throw new RuntimeException("厂家<" + count.getProduce() + ">有未评分设备请查看！");
                            }
                        }
                    }
                }

            }
        }
        entity.setUpdateTime(new Date());
        mapper.update(entity);
    }

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    @Transactional(rollbackFor = {RuntimeException.class, Exception.class, SQLException.class})
    public void delete(CmdbItDeviceCount entity) {
        produceAssessmentMapper.delete(entity.getProduce());
        mapper.delete(entity);
    }

    @Override
    public List<Map<String, Object>> getProvinceStatus(String quarter) {
        return mapper.getProvinceStatus(quarter);
    }

    @Override
    public List<Map<String, Object>> getProduceAndDeviceList(Map<String, String> requestMp) {
        return mapper.getProduceAndDeviceList(requestMp);
    }
}