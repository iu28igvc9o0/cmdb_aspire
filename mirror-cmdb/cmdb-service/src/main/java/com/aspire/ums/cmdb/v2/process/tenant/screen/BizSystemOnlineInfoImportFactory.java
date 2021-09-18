package com.aspire.ums.cmdb.v2.process.tenant.screen;

import com.aspire.ums.cmdb.common.Constants;
import com.aspire.ums.cmdb.index.payload.ItCloudScreenRequest;
import com.aspire.ums.cmdb.index.payload.ScreenCheckScore;
import com.aspire.ums.cmdb.index.payload.ScreenOnlineInfo;
import com.aspire.ums.cmdb.util.SpringUtils;
import com.aspire.ums.cmdb.v2.index.serivce.ItCloudScreenCheckScoreService;
import com.aspire.ums.cmdb.v2.index.serivce.ItCloudScreenOnlineInfoService;
import com.aspire.ums.cmdb.v2.process.validate.DateValidator;
import com.aspire.ums.cmdb.v2.process.validate.EmptyValidator;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName BizSystemOnlineInfoImportFactory
 * @Description 业务系统上线信息导入模板
 * @Author luowenbo
 * @Date 2020/4/23 17:01
 * @Version 1.0
 */
public class BizSystemOnlineInfoImportFactory extends AbstractTenantScreenFactory{

    private ItCloudScreenOnlineInfoService screenOnlineInfoService;
    private ItCloudScreenCheckScoreService itCloudScreenCheckScoreService;

    @Override
    public void initSpringBean() {
        if(screenOnlineInfoService == null) {
            screenOnlineInfoService = SpringUtils.getBean(ItCloudScreenOnlineInfoService.class);
        }
        if(itCloudScreenCheckScoreService == null) {
            itCloudScreenCheckScoreService = SpringUtils.getBean(ItCloudScreenCheckScoreService.class);
        }
    }

    @Override
    public void execute(Map<String, String> dataMap) {
        if(null != dataMap) {
            // 获取导入传入的参数
            String systemTitle = super.getProcessParams().get("systemTitle");
            String monthlyTime = super.getProcessParams().get("monthlyTime");
            ScreenOnlineInfo onlineInfo = new ScreenOnlineInfo();
            // 封装实体对象
            onlineInfo.setSystemTitleId(systemTitle);
            onlineInfo.setMonthlyTime(monthlyTime);
            String columnValue;
            for (String key : dataMap.keySet()) {
                columnValue = dataMap.get(key);
                if (key.contains("必填")) {
                    EmptyValidator.notEmpty(key, columnValue);
                }
                if("业务系统[必填]".equals(key)) {
                    onlineInfo.setBizSystem(columnValue);
                    continue;
                }
                if("二级部门".equals(key)) {
                    onlineInfo.setDepartment2(columnValue);
                    continue;
                }
                if("一级部门".equals(key)) {
                    onlineInfo.setDepartment1(columnValue);
                    continue;
                }
                if("预计部署上线时间".equals(key)) {
                    DateValidator.validate(key,columnValue,new String[]{Constants.DATE_PATTERN_YYYY_MM_DD});
                    onlineInfo.setTargetDeployDate(columnValue);
                    continue;
                }
                if("实际部署上线时间".equals(key)) {
                    DateValidator.validate(key,columnValue,new String[]{Constants.DATE_PATTERN_YYYY_MM_DD});
                    onlineInfo.setActualDeployDate(columnValue);
                    continue;
                }
                if("预计上线通知时间".equals(key)) {
                    DateValidator.validate(key,columnValue,new String[]{Constants.DATE_PATTERN_YYYY_MM_DD});
                    onlineInfo.setTargetInformDate(columnValue);
                    continue;
                }
                if("实际上线通知时间".equals(key)) {
                    DateValidator.validate(key,columnValue,new String[]{Constants.DATE_PATTERN_YYYY_MM_DD});
                    onlineInfo.setActualInformDate(columnValue);
                    continue;
                }
            }
            try {
                // 考虑到可能会重复导入，先将之前的数据删除，再重新新增
                ItCloudScreenRequest req = new ItCloudScreenRequest();
                req.setMonthlyTime(monthlyTime);
                req.setDepartment1(onlineInfo.getDepartment1());
                req.setDepartment2(onlineInfo.getDepartment2());
                req.setBizSystem(onlineInfo.getBizSystem());
                screenOnlineInfoService.delete(req);
                screenOnlineInfoService.insert(onlineInfo);
            } catch (Exception e) {
                throw new RuntimeException("新增业务系统上线信息失败:" + e.getMessage(), e);
            }
        }
    }

    /**
     * 每行处理完以后, 回调事件,覆盖。计算实际时间和预计时间比较，得出扣分项
     */
    public void afterRowHandler(Map<String, String> dataMap) {
        String systemTitle = super.getProcessParams().get("systemTitle");
        String monthlyTime = super.getProcessParams().get("monthlyTime");
        String bizSystem = dataMap.get("业务系统[必填]");
        String department1 = dataMap.get("一级部门");
        String department2 = dataMap.get("二级部门");
        String targetDeployDate = dataMap.get("预计部署上线时间");
        String actualDeployDate = dataMap.get("实际部署上线时间");
        String targetInformDate = dataMap.get("预计上线通知时间");
        String actualInformDate = dataMap.get("实际上线通知时间");
        // 先删除原有数据
        ItCloudScreenRequest req = new ItCloudScreenRequest();
        req.setDepartment1(department1);
        req.setDepartment2(department2);
        req.setMonthlyTime(monthlyTime);
        req.setBizSystem(bizSystem);
        req.setScoreType("deploy");
        itCloudScreenCheckScoreService.delete(req);
        req.setScoreType("inform");
        itCloudScreenCheckScoreService.delete(req);
        // 添加新数据
        List<ScreenCheckScore> scsList = new ArrayList<>();
        Map<String,String> deployMp = judgeDateScoreIfDeal(targetDeployDate,actualDeployDate);
        Map<String,String> informMp = judgeDateScoreIfDeal(targetInformDate,actualInformDate);
        if("true".equals(deployMp.get("flag"))) {
            ScreenCheckScore scs = new ScreenCheckScore(req);
            scs.setScoreType("deploy");
            scs.setSystemTitleId(systemTitle);
            scs.setStandardValue(targetDeployDate);
            scs.setAssessedValue(actualDeployDate);
            scs.setScore(calOnlineDateScore(deployMp.get("data")));
            scs.setDescription("上线部署延迟");
            scsList.add(scs);
        }
        if("true".equals(informMp.get("flag"))) {
            ScreenCheckScore scs = new ScreenCheckScore(req);
            scs.setScoreType("inform");
            scs.setSystemTitleId(systemTitle);
            scs.setStandardValue(targetInformDate);
            scs.setAssessedValue(actualInformDate);
            scs.setScore(calOnlineDateScore(informMp.get("data")));
            scs.setDescription("上线通知延迟");
            scsList.add(scs);
        }
        itCloudScreenCheckScoreService.batchInsert(scsList);
    }

    // 判断是否存在扣分，扣分则入库，不扣分则不作入库处理
    private Map<String,String> judgeDateScoreIfDeal(String targetDate,String actualDate) {
        Map<String,String> rs = new HashMap<>();
        //设置转换的日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try{
            //目标时间
            Date target = sdf.parse(targetDate);
            //实际时间
            Date actual = sdf.parse(actualDate);
            //得到相差的天数 betweenDate
            long betweenDate = (target.getTime() - actual.getTime())/(60*60*24*1000);
            if(betweenDate < 0 && Math.abs(betweenDate) > 30) {
                // 30 天内不扣分，超过30天，按照每超过一天扣0.1，最多扣1分。（date - 30）* 0.1
                rs.put("flag","true");
                rs.put("data",String.valueOf(Math.abs(betweenDate)));
                return rs;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        rs.put("flag","false");
        rs.put("data",null);
        return rs;
    }

    // 计算上线时间信息的扣分规则
    private String calOnlineDateScore(String betweenDate){
        // 30 天内不扣分，超过30天，按照每超过一天扣0.1，最多扣1分。（date - 30）* 0.1
        BigDecimal thirty = new BigDecimal("30");
        BigDecimal a = new BigDecimal(betweenDate);
        BigDecimal b = a.subtract(thirty);
        BigDecimal c = new BigDecimal("0.1");
        BigDecimal d = b.multiply(c);
        BigDecimal one = new BigDecimal("1");
        if(d.compareTo(one) == 1) {
            return "1.0";
        }
        return d.toPlainString();
    }

}
