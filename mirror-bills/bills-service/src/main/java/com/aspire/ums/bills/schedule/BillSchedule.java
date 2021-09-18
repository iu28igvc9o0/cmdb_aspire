package com.aspire.ums.bills.schedule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.common.entity.Result;
import com.aspire.ums.bills.account.payload.CmdbBillsAccountBalance;
import com.aspire.ums.bills.account.payload.CmdbBillsAccountRecord;
import com.aspire.ums.bills.account.service.CmdbBillsAccountService;
import com.aspire.ums.bills.calculate.payload.CmdbBillsDayRecord;
import com.aspire.ums.bills.calculate.payload.CmdbBillsMonthBill;
import com.aspire.ums.bills.calculate.payload.CmdbBillsMonthRecord;
import com.aspire.ums.bills.calculate.service.CmdbBillsDayRecordService;
import com.aspire.ums.bills.calculate.service.CmdbBillsMonthRecordService;
import com.aspire.ums.bills.calculate.service.CmdbFeginService;
import com.aspire.ums.bills.calculate.util.PricingStrategyUtil;
import com.aspire.ums.bills.config.BillLogAnnotation;
import com.aspire.ums.bills.discount.service.CmdbBillsDiscountService;
import com.aspire.ums.bills.price.payload.CmdbBillsPriceResponse;
import com.aspire.ums.bills.price.service.CmdbBillsPriceService;
import com.aspire.ums.bills.util.BigDecimalCalculateUtils;
import com.aspire.ums.bills.util.DateUtils;
import com.aspire.ums.bills.util.StringUtils;
import com.aspire.ums.bills.util.UUIDUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Before;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @projectName: BillSchedule
 * @description: 账单生成定时器
 * @author: luowenbo
 * @create: 2020-08-03 13:53
 **/
@Component
@EnableScheduling
@Slf4j
@EnableAspectJAutoProxy(proxyTargetClass=true, exposeProxy=true)
@RequestMapping("/v1/cmdb/bill")
public class BillSchedule {
    @Autowired
    private CmdbBillsDayRecordService dayRecordService;
    @Autowired
    private CmdbBillsMonthRecordService monthRecordService;
    @Autowired
    private CmdbBillsPriceService priceManageService;
    @Autowired
    private CmdbBillsDiscountService discountManageService;
    @Autowired
    private CmdbBillsAccountService accountService;
    @Autowired
    private CmdbFeginService cmdbFeginService;

    @Scheduled(cron = "0 0 0 1/1 * ?")
    public void triggerDayBillGenerator() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        String chargeTime = simpleDateFormat.format(calendar.getTime());
        log.info("{} 日账单#############开始生成.",chargeTime);
        ((BillSchedule) AopContext.currentProxy()).dailyBillGeneration(chargeTime);
        log.info("{} 日账单#############生成结束.",chargeTime);
    }

    @Scheduled(cron = "0 0 0 1/1 * ? ")
    public void triggerMonthBillGenerator() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        // 当月1号，计算上月月账单
        if ( calendar.get(Calendar.DAY_OF_MONTH) == 1) {
            calendar.add(Calendar.MONTH, -1);
        }
        String chargeTime = simpleDateFormat.format(calendar.getTime());
        log.info("{} 月账单#############开始生成.",chargeTime);
        ((BillSchedule) AopContext.currentProxy()).monthBillGeneration(chargeTime);
        log.info("{} 月账单#############生成结束.",chargeTime);
    }

    /**
    * 功能：日账单生成
    * 需求：按天计费，不足一天按一天计费。计价=配额*每日单价(单价/当前月天数)*折扣  每日按照23:58分时间点计算。
    * */
    @BillLogAnnotation(content = "'生成'+#chargeTime+'资源日账单'", type="新增", obj="日账单")
    public void dailyBillGeneration(String chargeTime) {
        // 获取所有租户的设备分配情况
        List<Map<String, Object>> quotas = cmdbFeginService.getNeedChargeBusinessQuotaInfo();
        // 判断租户是否为内部租户的标识
        String innerDepartmentFlag = cmdbFeginService.getInnerDepartmentFlag();
        if(!CollectionUtils.isEmpty(quotas)) {
            for(Map<String, Object> item : quotas) {
                String department1 = item.get("department1") == null ? "" : item.get("department1").toString();
                String department2 = item.get("department2") == null ? "" : item.get("department2").toString();
                // 如果是内部租户缴费部门为二级部门，外部租户，缴费部门为一级部门
                String department = (innerDepartmentFlag.equals(department1) && !"".equals(department2)) ? department2 : department1;
                String biz = item.get("bizSystem").toString();
                String idc = item.get("idcType").toString();
                String pod = item.get("pod").toString();
                List<CmdbBillsDayRecord> addList = new ArrayList<>();
                // 获取所有的设备类型，遍历计算费用且存入日账单
                List<Map<String, Object>> billsDeviceTypes = cmdbFeginService.getBillsDeviceTypes(null);
                for(Map<String, Object> dtItem : billsDeviceTypes) {
                    String dtId = dtItem.get("id").toString();
                    String key = dtItem.get("name").toString();
                    // 1.获取分配额
                    String quota = item.get(key) == null ? "0" : item.get(key).toString();
                    // 2.获取单价
                    CmdbBillsPriceResponse priceCondi = priceManageService.getPriceByCondition(idc, dtId);
                    String price = String.valueOf(null == priceCondi ? "0" : priceCondi.getPrice());
                    // 3.获取折扣
                    List<String> ids = new ArrayList<>();
                    if(!StringUtils.isEmpty(department)) {
                        ids.add(department1);
                        ids.add(department2);
                    }
                    if(!StringUtils.isEmpty(biz)) {
                        ids.add(biz);
                    }
                    if(!StringUtils.isEmpty(idc)) {
                        ids.add(idc);
                    }
                    Map<String, Object> som = discountManageService.queryDiscount(ids);
                    String discount = String.valueOf(null == som.get("discount") ? "1" : som.get("discount"));
                    // 4.计算日账单费用
                    String totalMoney = PricingStrategyUtil.calDailyExpense(quota,price,discount);
                    String discountBefore = PricingStrategyUtil.calDailyExpenseWithoutDiscount(quota,price);
                    // 5.保存日账单
                    CmdbBillsDayRecord dayRecord = new CmdbBillsDayRecord();
                    dayRecord.setBusinessSystemId(biz);
                    dayRecord.setDepartmentId(department);
                    dayRecord.setDeviceTypeId(dtId);
                    dayRecord.setIdcId(idc);
                    dayRecord.setPodId(pod);
                    dayRecord.setQuota(quota);
                    dayRecord.setPrice(Double.parseDouble(PricingStrategyUtil.getDailyPrice(price)));
                    dayRecord.setDiscount(Double.parseDouble(discount));
                    dayRecord.setTotalMoney(Double.parseDouble(totalMoney));
                    dayRecord.setDiscountBefore(Double.parseDouble(discountBefore));
                    dayRecord.setChargeTime(DateUtils.getDateFromString(chargeTime,"yyyy-MM-dd"));
                    addList.add(dayRecord);
                }
                dayRecordService.batchInsert(addList);
            }
        }
    }

    /**
    * 功能：月账单生成
    * 需求：每月第一天0点后进行上个月自动生成账单，并提供手工生成账单能力。
    * */
    @BillLogAnnotation(content = "'生成'+#chargeTime+'资源月账单'", type="新增", obj="月账单")
    public void monthBillGeneration(String chargeTime){
        // 1.依据部门、业务系统、资源池、POD池、设备类型去日账单中统计
        List<CmdbBillsDayRecord> dayRecords = dayRecordService.getDayBillsRecordWithMonth(chargeTime);
        // 特殊处理，当生成11月份账单是，同时生成一份12月的账单，且与11月相同
        String[] strSplit = chargeTime.split("-");
        int year = Integer.parseInt(strSplit[0]);
        int month = Integer.parseInt(strSplit[1]);
        // 2.保存月账单
        List<CmdbBillsMonthRecord> monthRecords = new ArrayList();
        Map<String, Long> monthBillMap = new LinkedHashMap<>();
        for (CmdbBillsDayRecord day : dayRecords) {
            Long pay = Long.valueOf((long)Math.ceil(day.getTotalMoney()));
            CmdbBillsMonthRecord monthRecord = new CmdbBillsMonthRecord();
            monthRecord.setDepartmentId(day.getDepartmentId());
            monthRecord.setBusinessSystemId(day.getBusinessSystemId());
            monthRecord.setIdcId(day.getIdcId());
            monthRecord.setPodId(day.getPodId());
            monthRecord.setDeviceTypeId(day.getDeviceTypeId());
            monthRecord.setNeedPay(pay);
            monthRecord.setDiscountBeforePay(Long.valueOf((long)Math.ceil(day.getDiscountBefore())));
            monthRecord.setChargeTime(chargeTime);
            monthRecords.add(monthRecord);
            Long needPay = 0L;
            if (monthBillMap.containsKey(day.getDepartmentId())) {
                needPay = monthBillMap.get(day.getDepartmentId());
            }
            if (month == 11) {
                CmdbBillsMonthRecord dec = new CmdbBillsMonthRecord();
                BeanUtils.copyProperties(monthRecord,dec);
                dec.setChargeTime(year + "-12");
                monthRecords.add(dec);
            }
            monthBillMap.put(day.getDepartmentId(), needPay + pay);
        }
        if (month == 11) {
            monthRecordService.delete(year + "-12");
            // 生成租户总账单数据
            monthRecordService.deleteMonthBill(year + "-12");
        }
        // 一个月只会存在一张账单
        monthRecordService.delete(chargeTime);
        // 新增月账单 monthRecordService.batchInsert(monthRecords); 批量新增，存在数据包大于默认数据库最大值
        List<CmdbBillsMonthRecord> batchRecord;
        int size = 1000;
        int page = monthRecords.size() % size == 0 ? (monthRecords.size() / size) : (monthRecords.size() / size + 1);
        for (int p = 0; p < page; p++) {
            batchRecord = new ArrayList<>();
            for (int i = p * size, i1 = 0; i < monthRecords.size() && i1 < size; i ++, i1++ ) {
                batchRecord.add(monthRecords.get(i));
            }
            monthRecordService.batchInsert(batchRecord);
        }
        //
        calculateAccountBalance(monthRecords);
        // 删除租户总账单数据
        monthRecordService.deleteMonthBill(chargeTime);
        for(Map.Entry entry : monthBillMap.entrySet()) {
            String departmentId = entry.getKey().toString();
            Long needPay = (Long) entry.getValue();
            CmdbBillsAccountBalance accountBalance = accountService.selectByDepartment(departmentId);
            CmdbBillsMonthBill monthBill = new CmdbBillsMonthBill();
            if (accountBalance != null) {
                monthBill.setAccountId(accountBalance.getId());
            }
            monthBill.setDepartmentId(departmentId);
            monthBill.setChargeTime(chargeTime);
            monthBill.setNeedPay(needPay);
            monthBill.setId(UUIDUtil.getUUID());
            monthBill.setRealPay(0L);
            monthRecordService.insertMonthBill(monthBill);
            if (month == 11) {
                monthBill.setId(UUIDUtil.getUUID());
                monthBill.setChargeTime(year + "-12");
                // 生成租户总账单数据
                monthRecordService.insertMonthBill(monthBill);
            }
        }
    }

    /**
     *  依据月账单，计算租户月份的总金额，然后对账户余额和缴费流水进行管理操作
     * @param monthRecords
     */
    private void calculateAccountBalance(List<CmdbBillsMonthRecord> monthRecords) {
        Map<String,String> indexMp = new HashMap<>(32);
        // 封装部门月份账单的总金额
        for(CmdbBillsMonthRecord record : monthRecords) {
            String key = record.getDepartmentId() + "@" + record.getChargeTime();
            if(indexMp.containsKey(key)) {
                String crtPay = indexMp.get(key);
                indexMp.put(key, BigDecimalCalculateUtils.stringAddWithBigDecimal(crtPay,String.valueOf(record.getNeedPay())));
            } else {
                indexMp.put(key,String.valueOf(record.getNeedPay()));
            }
        }
        // 账户余额计算
        for(Map.Entry<String,String> entry : indexMp.entrySet()) {
            String[] strArray = entry.getKey().split("@");
            String department = strArray[0];
            String chargeTime = strArray[1];
            String totalMoney = entry.getValue();
            // 判断是否存在账号
            CmdbBillsAccountBalance account = accountService.selectByDepartment(department);
            // 流水账单中账号的ID
            String accountIdWithRecord;
            if(null != account) {
                Double balance = account.getBalance();
                // 如果账单是第二次生成,已经存在扣款，先做余额的回加    dealType为0表示支出
                List<CmdbBillsAccountRecord> records = accountService.getAccountRecord(account.getId(), chargeTime,"0");
                for(CmdbBillsAccountRecord item : records) {
                    // 交易金额
                    Double amount = item.getAmount();
                    // 删除老旧的交易记录,账号余额回加
                    accountService.deleteAccountRecordById(item.getId());
                    balance = BigDecimalCalculateUtils.doubleAddWithBigDecimal(String.valueOf(balance),String.valueOf(amount));
                }
                balance = BigDecimalCalculateUtils.doubleSubWithBigDecimal(String.valueOf(balance),totalMoney);
                // 更新余额
                account.setBalance(balance);
                accountService.updateAccountBalance(account);
                accountIdWithRecord = account.getId();
            } else {
                // 创建账号
                CmdbBillsAccountBalance newAccount = new CmdbBillsAccountBalance();
                toHandleBillNumber(newAccount, department);
                newAccount.setDepartmentId(department);
                newAccount.setBalance(BigDecimalCalculateUtils.doubleSubWithBigDecimal("0",totalMoney));
                // 新增账号后，会返回新建行的ID
                Result result = accountService.insertAccountBalance(newAccount);
                accountIdWithRecord = result.getResultDes();
            }
            // 添加新帐单的流水记录
            CmdbBillsAccountRecord newRecord = new CmdbBillsAccountRecord();
            newRecord.setAccountId(accountIdWithRecord);
            newRecord.setAmount(Double.parseDouble(totalMoney));
            newRecord.setDealMonth(chargeTime);
            newRecord.setDealType(0);
            newRecord.setPayMethod("system");
            accountService.insertAccountRecord(newRecord);
        }
    }


    private void toHandleBillNumber(CmdbBillsAccountBalance accountBalance, String departmentId) {
        Map<String, Object> orgInfo = cmdbFeginService.getConfigByCode("org_list_info");
        if (orgInfo == null) {
            log.error("未配置部门信息[org_list_info]");
            throw new RuntimeException("未配置部门信息[org_list_info]");
        }
        Map<String, Object> orgInfoValue = JSONObject.parseObject(orgInfo.get("configValue").toString(), Map.class);
        String moduleId = orgInfoValue.get("module_id").toString();
        Map<String, Object> bizInfo = cmdbFeginService.getInstanceDetail(moduleId, departmentId);
        Map<String, Object> moduleColumns = cmdbFeginService.getModuleColumns(moduleId);
        StringBuilder code = new StringBuilder("IT");
        if (!moduleColumns.containsKey("department_type")) {
            log.error("模型ID：[{}]的没有租户类型列信息", moduleId);
            return;
        }
        Map tyeMap = JSONObject.parseObject(JSON.toJSONString(moduleColumns.get("department_type")), Map.class);
        String typeKey = "ref".equals(tyeMap.get("type")) ? tyeMap.get("ref_name").toString(): tyeMap.get("filed_code").toString();
        if (!bizInfo.containsKey(typeKey) || !StringUtils.isNotEmpty(bizInfo.get(typeKey))) {
            log.error("租户ID：[{}]的租户类型为空", departmentId);
            return;
        }
        if (!bizInfo.containsKey("parent_id")) {
            log.error("租户ID：[{}]的父级字段不存在，无法判断一级、二级", departmentId);
            return;
        }
        //专属公司-ET；集团各个部门-GT、IT公司内部-IT、其他-OT
        Map<String, Object> deptTypeConfig = cmdbFeginService.getConfigByCode("department_type_map");
        Map deptTypeMap = JSONObject.parseObject(deptTypeConfig.get("configValue").toString(), Map.class);
        String deptType = bizInfo.get(typeKey).toString();
        if (!deptTypeMap.containsKey(deptType)) {
            log.error("租户类型[{}]的不在 专属公司，集团各个部门，IT公司内部，其他 内", deptTypeMap.toString());
            return;
        }
        String cpCode = deptTypeMap.get(deptType).toString();
//        switch (bizInfo.get(typeKey).toString()) {
//            case "专属公司": cpCode = "ET";break;
//            case "集团各个部门": cpCode = "GT";break;
//            case "IT公司内部": cpCode = "IT";break;
//            case "其他": cpCode = "OT";break;
//            default: break;
//        }
//        if (StringUtils.isEmpty(cpCode)) {
//            log.error("租户类型[{}]的不在 专属公司，集团各个部门，IT公司内部，其他 内", bizInfo.get(typeKey).toString());
//            return;
//        }
        code.append(cpCode);
        //年份(4位数)
        code.append(Calendar.getInstance().get(Calendar.YEAR));
        //一级部门01、二级部门02
        String dept_type = "";
        if (StringUtils.isNotEmpty(bizInfo.get("parent_id"))) {
            dept_type = "0".equals(bizInfo.get("parent_id")) ? "01" : "02";
        } else {
            dept_type = "01";
        }
        code.append(dept_type);
        //3位递增编码(001, 002, ...)
        String number = accountService.selectNextNumber();
        code.append(number);
        accountBalance.setAccountCode(code.toString());
        accountBalance.setNumber(Integer.parseInt(number));
//        if (!bizInfo.containsKey("concat") || !StringUtils.isNotEmpty(bizInfo.get("concat"))) {
//            log.error("租户ID：[{}]的联系人为空", departmentId);
//        } else {
//            accountBalance.setAccountManager(bizInfo.get("concat").toString());
//        }
//
//        if (!bizInfo.containsKey("contact_email_address") || !StringUtils.isNotEmpty(bizInfo.get("contact_email_address"))) {
//            log.error("租户ID：[{}]的联系人邮箱为空", departmentId);
//        } else {
//            accountBalance.setAccountManagerEmail(bizInfo.get("contact_email_address").toString());
//        }
//
//        if (!bizInfo.containsKey("contact_number") || !StringUtils.isNotEmpty(bizInfo.get("contact_number"))) {
//            log.error("租户ID：[{}]的联系人电话为空", departmentId);
//        } else {
//            accountBalance.setAccountManagerPhone(bizInfo.get("contact_number").toString());
//        }
    }

}
