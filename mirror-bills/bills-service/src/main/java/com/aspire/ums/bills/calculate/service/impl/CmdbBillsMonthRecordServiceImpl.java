package com.aspire.ums.bills.calculate.service.impl;

import com.aspire.mirror.common.entity.Result;
import com.aspire.ums.bills.account.payload.CmdbBillsAccountBalance;
import com.aspire.ums.bills.account.payload.CmdbBillsAccountRecord;
import com.aspire.ums.bills.account.service.CmdbBillsAccountService;
import com.aspire.ums.bills.calculate.mapper.CmdbBillsMonthRecordMapper;
import com.aspire.ums.bills.calculate.payload.CmdbBillsMonthBill;
import com.aspire.ums.bills.calculate.payload.CmdbBillsMonthRecord;
import com.aspire.ums.bills.calculate.service.CmdbBillsMonthRecordService;
import com.aspire.ums.bills.client.CmdbFeignClient;
import com.aspire.ums.bills.config.BillLogAnnotation;
import com.aspire.ums.bills.config.RequestAuthContext;
import com.aspire.ums.bills.log.payload.BillsLog;
import com.aspire.ums.bills.log.payload.BillsLogRequest;
import com.aspire.ums.bills.log.service.BillLogService;
import com.aspire.ums.bills.util.BigDecimalCalculateUtils;
import com.aspire.ums.bills.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @projectName: CmdbBillsMonthRecordServiceImpl
 * @description: 月账单实现类
 * @author: luowenbo
 * @create: 2020-08-03 15:38
 **/
@Service
@EnableAsync
@Slf4j
public class CmdbBillsMonthRecordServiceImpl implements CmdbBillsMonthRecordService {

    @Autowired
    private CmdbBillsMonthRecordMapper monthRecordMapper;
    @Autowired
    private CmdbBillsAccountService accountService;
    @Autowired
    private CmdbFeignClient cmdbFeignClient;
    @Autowired
    private BillLogService logService;
    @Value("${cmdb.schema.name}")
    private String cmdbTableName;

    @Override
    public void batchInsert(List<CmdbBillsMonthRecord> recordList) {
        if(!CollectionUtils.isEmpty(recordList)) {
            for(CmdbBillsMonthRecord item : recordList) {
                item.setId(UUIDUtil.getUUID());
                item.setInsertPerson("System");
                item.setInsertTime(new Date());
            }
            monthRecordMapper.batchInsert(recordList);
        }
    }

    @Override
    public void insert(CmdbBillsMonthRecord cmdbBillsMonthRecord) {
        if(null != cmdbBillsMonthRecord) {
            cmdbBillsMonthRecord.setId(UUIDUtil.getUUID());
            cmdbBillsMonthRecord.setInsertPerson("System");
            cmdbBillsMonthRecord.setInsertTime(new Date());
            monthRecordMapper.insert(cmdbBillsMonthRecord);
        }
    }

    @Override
    public void delete(String chargeTime) {
        monthRecordMapper.delete(chargeTime);
    }

    @Override
    public Map<String, Object> update(CmdbBillsMonthRecord record) {
        Map<String, Object> rs = new HashMap<>();
        int count = monthRecordMapper.updateByPrimaryKeySelective(record);
        if(count > 0) {
            rs.put("flag",true);
            rs.put("message","修改成功");
        } else {
            rs.put("flag",false);
            rs.put("message","修改失败");
        }
        return rs;
    }

    @Override
    public List<Map<String,Object>> listBillsWithMonth(String chargeTime) {
        return monthRecordMapper.listBillsWithMonth(chargeTime, cmdbTableName);
    }

    @Override
    public List<Map<String, Object>> listMonthBillsWithDepartment(String type,
                                                                  String department,
                                                                  String chargeTime) {
        return monthRecordMapper.listMonthBillsWithDepartment(type,department,chargeTime,cmdbTableName);
    }

    @Override
    public Map<String, Object> listMonthBillsByDepartment(String departmentId, String chargeTime) {
        CmdbBillsAccountBalance accountBalance = accountService.selectByDepartment(departmentId);
        List<Map<String, Object>> data = monthRecordMapper.listMonthBillsByDepartment(departmentId, chargeTime, cmdbTableName);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("balance", accountBalance.getBalance());
        resultMap.put("data", data);
        return resultMap;
    }

    @Override
    public Result updateRealPay(CmdbBillsMonthRecord record) {
        // 更新租户的实缴费用，相当于缴费，得更新账号余额和流水记录
        Result result = new Result();
        if(monthRecordMapper.updateRealPay(record) > 0) {
            result = Result.success();
            dealAccountInfo(record);
        } else {
            result.setResultCode("202");
            result.setResultDes("Update fail.There are no data for " + record.getChargeTime());
        }
        Map<String, Object> departmentInfo = cmdbFeignClient.getDepartmentById(record.getDepartmentId());
        if (departmentInfo == null) {
            throw new RuntimeException("未包含部门信息");
        }
        // 记录日志
        BillsLog billsLog = new BillsLog();
        billsLog.setIp(RequestAuthContext.getRequestIp());
        billsLog.setOperateOBJ("账号缴费");
        billsLog.setOperateType("更新");
        billsLog.setOperateUser(RequestAuthContext.getRequestHeadUserName());
        billsLog.setOperateContent("调整" + departmentInfo.get("orgName") + "资源缴费金额" + record.getRealPay());
        logService.saveBillLog(billsLog);
        return result;
    }

    /**
     * 冲销功能，新增月账单价格
     *
     * @param monthBill 冲销信息
     * @return
     */
    @Override
    public Map<String, Object> insertBillMonth(CmdbBillsMonthBill monthBill) {
        Map<String, Object> rs = new HashMap<>();
        try {
            monthRecordMapper.insertBillMonth(monthBill);
            rs.put("flag",true);
            rs.put("message","新增成功");
        } catch (Exception e) {
            rs.put("flag",false);
            rs.put("message","新增失败" + e.getMessage());
        }
        return rs;
    }

    /**
     * 冲销功能，更改月账单价格
     *
     * @param monthBill 冲销信息
     * @return
     */
    @Override
    @BillLogAnnotation(content = "'调整'+#monthBill.getOrgName() + #monthBill.getChargeTime() +'资源冲销金额' + #monthBill.getNeedPay()", type="更新", obj="冲销月度账单")
    public Map<String, Object> updateBillMonth(CmdbBillsMonthBill monthBill) {
        Map<String, Object> rs = new HashMap<>();
        try {
            monthRecordMapper.updateBillMonth(monthBill);
            rs.put("flag",true);
            rs.put("message","修改成功");
        } catch (Exception e) {
            rs.put("flag",false);
            rs.put("message","修改失败" + e.getMessage());
        }
        return rs;
    }

    @Override
    public void deleteMonthBill(String chargeTime) {
        monthRecordMapper.deleteMonthBill(chargeTime);
    }

    @Override
    public void insertMonthBill(CmdbBillsMonthBill monthBill) {
        monthRecordMapper.insertBillMonth(monthBill);
    }

    @Async
    void dealAccountInfo(CmdbBillsMonthRecord record){
        try {
            // 当前录入缴费金额
            Long money = record.getRealPay();
            // 获取历史账单
            List<Map<String, Object>> recordList= monthRecordMapper.listMonthBillsByDepartment(record.getDepartmentId(), record.getChargeTime(), cmdbTableName);
            // 获取账号余额
            CmdbBillsAccountBalance account = accountService.selectByDepartment(record.getDepartmentId());
            // 余额
            Double balance = account.getBalance();
            if (money > 0) {
                Long allRealPay = 0L;
                Long allNeedPay = 0L;
                for (Map<String, Object> r : recordList) {
                    // 实缴
                    Long realPay = new Double(Double.parseDouble(r.get("real_pay").toString())).longValue();
                    // 应缴
                    Long needPay = new Double(Double.parseDouble(r.get("need_pay").toString())).longValue();
                    Long subMoney = needPay - realPay;
                    if (subMoney > 0) {
                        // 更新的月账单
                        CmdbBillsMonthRecord updateRecord = new CmdbBillsMonthRecord();
                        updateRecord.setChargeTime(r.get("chargeTime").toString());
                        updateRecord.setDepartmentId(record.getDepartmentId());
                        // 开始记录冲销金额
                        CmdbBillsAccountRecord useRecord = new CmdbBillsAccountRecord();
                        useRecord.setAccountId(account.getId());
                        useRecord.setDealMonth(r.get("chargeTime").toString());
                        useRecord.setDealType(1);
                        useRecord.setPayMethod("system");

                        if (money - subMoney > 0) {
                            useRecord.setAmount(Double.parseDouble(String.valueOf(subMoney)));
                            updateRecord.setRealPay(realPay + subMoney);
                            money = money - subMoney;
                        } else {
                            useRecord.setAmount(Double.parseDouble(String.valueOf(money)));
                            updateRecord.setRealPay(realPay + money);
                        }
                        accountService.insertAccountRecord(useRecord);
                        // 更新的月账单
                        monthRecordMapper.updateRealPay(updateRecord);
                        allNeedPay += Long.parseLong(r.get("need_pay").toString());
                        allRealPay += updateRecord.getRealPay();
                    }
                }
                // 更新余额
                if (allNeedPay == 0 && allRealPay == 0) {
                    account.setBalance(BigDecimalCalculateUtils.doubleAddWithBigDecimal(String.valueOf(balance), String.valueOf(money)));
                } else {
                    account.setBalance(BigDecimalCalculateUtils.doubleSubWithBigDecimal(String.valueOf(allRealPay), String.valueOf(allNeedPay)));
                }
                accountService.updateAccountBalance(account);
            }

        } catch (Exception e) {
            log.error("生成{}缴费记录失败,error: {}", record.getChargeTime(),e.getMessage());
            throw new RuntimeException("生成缴费记录失败, error:" + e.getMessage());
        }


//        // 应缴
//        Long needPay = record.getNeedPay() == null ? 0 : record.getNeedPay();
//        // 实缴
//        Long realPay = record.getRealPay() == null ? 0 : record.getRealPay();
//
//        // 余额 + 实缴 - 应缴 = 现余额
//        balance = BigDecimalCalculateUtils.doubleAddWithBigDecimal(String.valueOf(balance), String.valueOf(realPay));
//        balance = BigDecimalCalculateUtils.doubleSubWithBigDecimal(String.valueOf(balance), String.valueOf(needPay));

//        if (null != realPayUnique.getRealPay()) {
//            List<CmdbBillsAccountRecord> records = accountService.getAccountRecord(account.getId(), record.getChargeTime(), "1");
//            for (CmdbBillsAccountRecord item : records) {
//                // 交易金额
//                Double amount = item.getAmount();
//                // 删除老旧的交易记录,账号余额回减
//                accountService.deleteAccountRecordById(item.getId());
//                balance = BigDecimalCalculateUtils.doubleSubWithBigDecimal(String.valueOf(balance), String.valueOf(amount));
//            }
//        }
//        balance = BigDecimalCalculateUtils.doubleAddWithBigDecimal(String.valueOf(balance),String.valueOf(record.getRealPay()));

        // 添加新帐单的流水记录
//        CmdbBillsAccountRecord newRecord = new CmdbBillsAccountRecord();
//        newRecord.setAccountId(account.getId());
//        newRecord.setAmount(Double.parseDouble(String.valueOf(record.getRealPay())));
//        newRecord.setDealMonth(record.getChargeTime());
//        newRecord.setDealType(1);
//        newRecord.setPayMethod("system");
//        accountService.insertAccountRecord(newRecord);
    }
}
