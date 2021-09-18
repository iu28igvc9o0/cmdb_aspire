package com.aspire.ums.cmdb.v2.assessment.service.impl;

import com.aspire.ums.cmdb.assessment.payload.CmdbServiceAssess;
import com.aspire.ums.cmdb.code.payload.CmdbCode;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.deviceStatistic.util.ExportExcelUtil;
import com.aspire.ums.cmdb.ftp.service.FtpService;
import com.aspire.ums.cmdb.module.payload.FullModule;
import com.aspire.ums.cmdb.module.payload.Module;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.v2.assessment.mapper.CmdbServiceAssessMapper;
import com.aspire.ums.cmdb.v2.assessment.service.ICmdbServiceAssessService;
import com.aspire.ums.cmdb.v2.assessment.util.DataHandleUtil;
import com.aspire.ums.cmdb.v2.cache.CacheConst;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 项目名称:
 * 包: com.aspire.ums.cmdb.v2.assessment.service.impl
 * 类名称:
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/7/15 15:29
 * 版本: v1.0
 */
@Slf4j
@Service
public class CmdbServiceAssessServiceImpl implements ICmdbServiceAssessService {
    
    @Autowired
    private CmdbServiceAssessMapper serviceAccessMapper;
    @Autowired
    private FtpService ftpService;

    /**
     * 计算季度设备总量
     * @param deviceType
     * @param quarter
     */
    public void calcTotalDeviceCount(String deviceType, String quarter) {
        // 查询并入库当前季度总设备量
        List<CmdbServiceAssess> deviceCountList = serviceAccessMapper.queryTotalDeviceCount(deviceType, quarter);
        if (deviceCountList == null || deviceCountList.size() == 0) {
            if (StringUtils.isNotEmpty(deviceType)) {
                throw new RuntimeException(quarter + "[" + deviceType + "]无审核通过的数据信息, 无法生成评分信息.");
            } else {
                throw new RuntimeException(quarter + "各设备类型无审核通过的数据信息, 无法生成评分信息.");
            }
        }
        deviceCountList.forEach((countData) -> serviceAccessMapper.saveTotalDeviceCount(countData));
    }

    /**
     * 计算维修事件得分
     * @param deviceType
     * @param quarter
     */
    public void calcRepairEvent(String deviceType, String quarter) {
        // 查询当前已经入库的评分信息
        CmdbServiceAssess queryEntity = new CmdbServiceAssess();
        queryEntity.setDeviceType(deviceType);
        queryEntity.setQuarter(quarter);
        List<CmdbServiceAssess> assessScoreList = serviceAccessMapper.getServiceAccessScore(queryEntity);
        if (assessScoreList == null || assessScoreList.size() == 0) {
            throw new RuntimeException("获取评分信息失败. 请联系技术人员排查.");
        }
        //计算事件占比
        assessScoreList.forEach((assessScore) -> {
            CmdbServiceAssess repairEntity = serviceAccessMapper.statisticsRepairEvent(assessScore);
            if (repairEntity == null) {
                return;
            }
            assessScore.setEventCount(repairEntity.getEventCount());
            assessScore.setTotalEventCount(repairEntity.getTotalEventCount());
            // 维修事件-事件占比
            double eventRate = DataHandleUtil.roundRatio(repairEntity.getEventCount(), repairEntity.getTotalEventCount()) * 100;
            assessScore.setEventRate(String.format("%.2f", eventRate));
            // 维修事件-设备占比
            double eventDeviceRate = DataHandleUtil.roundRatio(repairEntity.getEventCount(), assessScore.getTotalDeviceCount()) * 100;
            assessScore.setEventDeviceRate(String.format("%.2f", eventDeviceRate));
            // 维修事件-指标值
            double eventTargetValue = DataHandleUtil.roundRatio(assessScore.getEventRate(),assessScore.getEventDeviceRate());
            assessScore.setEventTargetValue(String.format("%.2f", eventTargetValue));
            // 维修事件-得分
            if (eventTargetValue == 0.00) {
                assessScore.setEventScore("FALSE");
            } else if (eventTargetValue < 1) {
                assessScore.setEventScore(String.format("%.2f", 40 - 4 * eventTargetValue));
            } else {
                assessScore.setEventScore(String.format("%.2f", 32 + 4 / eventTargetValue));
            }
            // 故障处理平均时长-故障处理总时长
            if (StringUtils.isNotEmpty(repairEntity.getEventProblemHandleLong())) {
                assessScore.setEventProblemHandleLong(String.format("%.2f", Double.parseDouble(repairEntity.getEventProblemHandleLong())));
            }
            // 故障处理平均时长-指标值
            if (StringUtils.isNotEmpty(assessScore.getEventCount()) && Integer.parseInt(assessScore.getEventCount()) == 0) {
                assessScore.setProblemHandleTargetValue(String.valueOf("9999"));
            } else {
                double problemHandleTargetValue = DataHandleUtil.roundRatio(repairEntity.getEventProblemHandleLong(),
                        assessScore.getEventCount());
                assessScore.setProblemHandleTargetValue(String.format("%.2f", problemHandleTargetValue));
            }
            // 保存得分信息
            serviceAccessMapper.saveRepairEventScore(assessScore);
        });
    }

    /**
     * 计算故障事件得分
     * @param deviceType
     * @param quarter
     */
    public void calcProblemEvent(String deviceType, String quarter) {
        CmdbServiceAssess queryEntity = new CmdbServiceAssess();
        queryEntity.setDeviceType(deviceType);
        queryEntity.setQuarter(quarter);
        List<CmdbServiceAssess> assessScoreList = serviceAccessMapper.getServiceAccessScore(queryEntity);
        if (assessScoreList == null || assessScoreList.size() == 0) {
            throw new RuntimeException("获取评分信息失败. 请联系技术人员排查.");
        }
        // 故障事件
        assessScoreList.forEach((assessScore) -> {
            CmdbServiceAssess problemEntity = serviceAccessMapper.statisticsProblemEvent(assessScore);
            // 如果查询不到 不处理
            if (problemEntity == null) {
                return;
            }
            assessScore.setProblemCount(problemEntity.getProblemCount());
            String problemScore = "0";
            if (StringUtils.isNotEmpty(problemEntity.getProblemScore())) {
                problemScore = problemEntity.getProblemScore();
            }
            assessScore.setProblemScore(problemScore);
            // 业务恢复时间
            if (StringUtils.isNotEmpty(problemEntity.getBizRepairLong())) {
                assessScore.setBizRepairLong(String.format("%.2f", Double.parseDouble(problemEntity.getBizRepairLong())));
            }
            // 业务恢复指标值
            if (StringUtils.isNotEmpty(problemEntity.getProblemCount()) && Integer.parseInt(problemEntity.getProblemCount()) == 0) {
                assessScore.setBizRepairTargetValue(String.valueOf("9999"));
            } else {
                // “总时长”除以“故障次数”得出“指标值”
                double repairTargetValue = DataHandleUtil.roundRatio(problemEntity.getBizRepairLong(), assessScore.getProblemCount());
                assessScore.setBizRepairTargetValue(String.format("%.2f", repairTargetValue));
            }
            //保存得分
            serviceAccessMapper.saveProblemEventScore(assessScore);
        });
    }

    /**
     * 计算设备问题得分
     * @param deviceType
     * @param quarter
     */
    public void calcEquipmentProblem(String deviceType, String quarter) {
        CmdbServiceAssess queryEntity = new CmdbServiceAssess();
        queryEntity.setDeviceType(deviceType);
        queryEntity.setQuarter(quarter);
        List<CmdbServiceAssess> assessScoreList = serviceAccessMapper.getServiceAccessScore(queryEntity);
        if (assessScoreList == null || assessScoreList.size() == 0) {
            throw new RuntimeException("获取评分信息失败. 请联系技术人员排查.");
        }
        // 设备问题
        assessScoreList.forEach((assessScore) -> {
            CmdbServiceAssess equipmentEntity = serviceAccessMapper.statisticsEquipmentProblem(assessScore);
            if (equipmentEntity == null) {
                return ;
            }
            assessScore.setEquipmentProblemCount(equipmentEntity.getEquipmentProblemCount());
            assessScore.setTotalEquipmentProblemCount(equipmentEntity.getTotalEquipmentProblemCount());
            // 设备问题-问题占比
            double equipmentProblemRate = DataHandleUtil.roundRatio(equipmentEntity.getEquipmentProblemCount(),
                    equipmentEntity.getTotalEquipmentProblemCount()) * 100;
            assessScore.setEquipmentProblemRate(String.format("%.2f", equipmentProblemRate));
            // 设备问题-设备占比
            double equipmentProblemDeviceRate = DataHandleUtil.roundRatio(equipmentEntity.getEquipmentProblemCount(),
                    assessScore.getTotalDeviceCount()) * 100;
            assessScore.setEquipmentProblemDeviceRate(String.format("%.2f", equipmentProblemDeviceRate));
            // 设备问题-指标值
            double equipmentProblemTargetValue = DataHandleUtil.roundRatio(assessScore.getEquipmentProblemRate(),
                    assessScore.getEquipmentProblemDeviceRate());
            assessScore.setEquipmentProblemTargetValue(String.format("%.2f", equipmentProblemTargetValue));
            // 设备问题-得分 最高10分 最低-10分
            String equipmentProblemScore = "0.0";
            if (equipmentProblemTargetValue > 10) {
                equipmentProblemScore = "-10.0";
            } else if (equipmentProblemTargetValue != 0.0) {
                equipmentProblemScore = "-" + String.format("%.2f", equipmentProblemTargetValue);
            }
            assessScore.setEquipmentProblemScore(equipmentProblemScore);
            // 保存设备问题得分
            serviceAccessMapper.saveEquipmentProblemScore(assessScore);
        });
    }

    /**
     * 计算故障处理平均时长得分、业务恢复处理平均时长得分
     * @param deviceType
     * @param quarter
     */
    public void calcHandlerAndRepairLong(String deviceType, String quarter) {
        CmdbServiceAssess queryEntity = new CmdbServiceAssess();
        queryEntity.setDeviceType(deviceType);
        queryEntity.setQuarter(quarter);
        List<CmdbServiceAssess> assessScoreList = serviceAccessMapper.getServiceAccessScore(queryEntity);
        if (assessScoreList == null || assessScoreList.size() == 0) {
            throw new RuntimeException("获取评分信息失败. 请联系技术人员排查.");
        }
        // 计算故障处理平均时长得分、业务恢复处理平均时长得分
        assessScoreList.forEach((assessScore) -> {
            if ("9999".equals(assessScore.getProblemHandleTargetValue())) {
                assessScore.setProblemHandleScore("FALSE");
            } else {
                //查询指标指最小的记录
                String minTargetValue = serviceAccessMapper.statisticsMinProblemHandleTargetValue(assessScore);
                // 6+同设备类型设备产生最小指标值/本类型指标值*4
                double score = DataHandleUtil.roundRatio(minTargetValue, assessScore.getProblemHandleTargetValue()) * 4;
                double problemHandleScore = 6.00 + score;
                assessScore.setProblemHandleScore(String.format("%.2f", problemHandleScore));
            }
            if ("9999".equals(assessScore.getBizRepairTargetValue())) {
                assessScore.setBizRepairScore("FALSE");
            } else {
                //查询指标指最小的记录
                String minTargetValue = serviceAccessMapper.statisticsMinRepairTargetValue(assessScore);
                // 8+同设备类型设备产生最小指标值/本类型指标值*2
                double score = DataHandleUtil.roundRatio(minTargetValue, assessScore.getBizRepairTargetValue()) * 2;
                double repairScore = 8.00 + score;
                assessScore.setBizRepairScore(String.format("%.2f", repairScore));
            }
            // 保存评分
            serviceAccessMapper.saveProblemHandleScore(assessScore);
        });
    }

    /**
     * 计算各厂商评估得分
     * @param deviceType
     * @param quarter
     */
    public void calcEvaluateScore(String deviceType, String quarter) {
        CmdbServiceAssess queryEntity = new CmdbServiceAssess();
        queryEntity.setDeviceType(deviceType);
        queryEntity.setQuarter(quarter);
        List<CmdbServiceAssess> assessScoreList = serviceAccessMapper.getServiceAccessScore(queryEntity);
        if (assessScoreList == null || assessScoreList.size() == 0) {
            throw new RuntimeException("获取评分信息失败. 请联系技术人员排查.");
        }
        // 计算评估得分 收集并统计各省份及资源池相同厂商的“服务评估”得分平均值
        assessScoreList.forEach((assessScore) -> {
            String evaluateScore = serviceAccessMapper.statisticsDeviceEvaluate(assessScore);
            if (StringUtils.isNotEmpty(evaluateScore)) {
                assessScore.setServiceScore(String.format("%.2f", Double.parseDouble(evaluateScore)));
            }
            // 保存评分
            serviceAccessMapper.saveDeviceEvaluateScore(assessScore);
        });
    }

    @Override
    public void delete(String deviceType, String quarter) {
        try {
            // 清理以前数据
            serviceAccessMapper.delete(deviceType, quarter);
        } catch (Exception e) {
            log.error("删除评分数据失败.",e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CmdbServiceAssess> queryScoreList(String deviceType, String quarter) {
        return serviceAccessMapper.queryScoreList(deviceType, quarter);
    }

    /**
     * 保存评分信息
     * @param assessesList 评分对象
     * @return
     */
    @Override
    @Transactional
    public Map<String, Object> saveScore(List<CmdbServiceAssess> assessesList) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (assessesList != null && assessesList.size() > 0) {
                assessesList.forEach((assess -> {
                    /*// 计算总得分
                    Integer falseNum = 0;
                    float score = 0f;
                    // 维修事件得分
                    if (assess.getEventScore().equals("FALSE")) {
                        falseNum ++;
                    } else {
                        score += Float.parseFloat(assess.getEventScore());
                    }
                    // 故障事件得分
                    score += Float.parseFloat(assess.getProblemScore());
                    // 设备问题得分
                    score += Float.parseFloat(assess.getEquipmentProblemScore());
                    // 故障处理时长得分
                    if (assess.getProblemHandleScore().equals("FALSE")) {
                        falseNum ++;
                    } else {
                        score += Float.parseFloat(assess.getProblemHandleScore());
                    }
                    // 故障恢复得分
                    if (assess.getBizRepairScore().equals("FALSE")) {
                        falseNum ++;
                    } else {
                        score += Float.parseFloat(assess.getBizRepairScore());
                    }
                    // 服务评估得分
                    if (StringUtils.isNotEmpty(assess.getServiceScore())) {
                        score += Float.parseFloat(assess.getServiceScore());
                    }
                    // 领导满意分
                    if (StringUtils.isNotEmpty(assess.getSatisfactionScore())) {
                        score += Float.parseFloat(assess.getSatisfactionScore());
                        // 最终得分 falseNum大于1 系数0.4 等于1 系数0.9
                        if (falseNum > 1) {
                            score = (float)(score/0.4);
                        } else {
                            score = (float)(score/0.9);
                        }
                        assess.setScore(String.format("%.2f", score));
                    }*/
                    serviceAccessMapper.saveScore(assess);
                }));
            }
            result.put("flag", "success");
            result.put("msg", "保存成功");
        } catch (Exception e) {
            log.error("insert error {}",e);
            result.put("flag", "error");
            result.put("msg", "保存失败");
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> getScoreDeviceTypeStatus(String quarter) {
        return serviceAccessMapper.getScoreDeviceTypeStatus(quarter);
    }

    @Override
    public Map<String, Object> exportEvaluate(String deviceType, String quarter) {
        Map<String, Object> returnMap = new HashMap<>();
        try {
            // 支持多表头、合并、多sheet页的excel导出
//            device_type
//                    produce
//            quarter
//                    event_count
//            total_event_count
//                    event_rate
//            event_device_rate
//                    event_target_value
//            event_score
//                    problem_count
//            problem_score
//                    equipment_problem_count
//            total_equipment_problem_count
//                    equipment_problem_rate
//            equipment_problem_device_rate
//                    equipment_problem_target_value
//            equipment_problem_score
//                    event_problem_handle_long
//            problem_handle_target_value
//                    problem_handle_score
//            biz_repair_long
//                    biz_repair_target_value
//            biz_repair_score
//                    service_score
//            satisfaction_score
//                    score
//            total_device_count
//            headerList = getInstanceHeader(moduleId);
//            Result<Map<String, Object>> mapResult = getInstanceList(queryInstance);
//            String fileName = "xxx季度xxx类型评分.xlsx"; //
//            final List<String> header = new LinkedList<>();
//            final List<String> keys = new LinkedList<>();
//            if (headerList != null && headerList.size() > 0) {
//                headerList.stream().forEach((code) -> {
//                    header.add(code.getFiledName());
//                    keys.add(code.getFiledCode());
//                });
//                ExportExcelUtil eeu = new ExportExcelUtil();
//                Workbook book = new SXSSFWorkbook(128);
//                eeu.exportExcel(book, 0, moduleName, header.toArray(new String[header.size()]),
//                        mapResult.getData(), keys.toArray(new String[keys.size()]));
//                ByteArrayOutputStream ops = null;
//                ByteArrayInputStream in = null;
//                try {
//                    ops = new ByteArrayOutputStream();
//                    book.write(ops);
//                    byte[] b = ops.toByteArray();
//                    in = new ByteArrayInputStream(b);
//                    returnMap = ftpService.uploadtoFTP(fileName, in);
//                    ops.flush();
//                } catch (Exception e) {
//                    log.error("导出excel失败，失败原因：", e);
//                    returnMap.put("code","error");
//                    returnMap.put("message", e.getMessage());
//                }finally {
//                    IOUtils.closeQuietly(book);
//                    IOUtils.closeQuietly(ops);
//                    IOUtils.closeQuietly(in);
//                    return returnMap;
//                }
//            }
        } catch (Exception e) {
            log.error("导出Excel数据失败!", e);
            returnMap.put("code", "error");
            returnMap.put("message", e.getMessage());
        }
        return returnMap;
    }


}
