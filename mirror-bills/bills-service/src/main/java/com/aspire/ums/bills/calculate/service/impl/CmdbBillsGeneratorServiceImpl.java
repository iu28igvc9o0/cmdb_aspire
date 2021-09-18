package com.aspire.ums.bills.calculate.service.impl;

import com.aspire.ums.bills.calculate.service.CmdbBillsGeneratorService;
import com.aspire.ums.bills.config.BillLogAnnotation;
import com.aspire.ums.bills.schedule.BillSchedule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @projectName: CmdbBillsGeneratorServiceImpl
 * @description: 类
 * @author: luowenbo
 * @create: 2020-08-12 20:19
 **/
@Service
@Slf4j
public class CmdbBillsGeneratorServiceImpl implements CmdbBillsGeneratorService {

    @Autowired
    private BillSchedule billSchedule;

    @Override
    public Map<String, Object> manuallyGeneratedMonthBills(String chargeTime) {
        Map<String, Object> rs = new HashMap<>();
        billSchedule.monthBillGeneration(chargeTime);
        rs.put("flag",true);
        rs.put("message","generated month bills success.");
        return rs;
    }

    @Override
    public Map<String, Object> manuallyGeneratedDayBills(String chargeTime) {
        Map<String, Object> rs = new HashMap<>();
        billSchedule.dailyBillGeneration(chargeTime);
        rs.put("flag",true);
        rs.put("message","Generated day bills success.");
        return rs;
    }

    @Override
    public Map<String, Object> manuallyGeneratedDayBillsByMonth(String chargeTime) {
        Map<String, Object> rs = new HashMap<>();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date cTime = new Date();
        try {
            cTime = sdf.parse(chargeTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(cTime);
        int dayOfMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i= 1 ;i <= dayOfMonth;i++) {
            String day = "";
            day += chargeTime;
            day += "-";
            if (i<10) {
                day +="0";
            }
            day += "" + i;
            log.info(">>>>>>>>开始统计日期{}日报",day);
            billSchedule.dailyBillGeneration(day);
            log.info(">>>>>>>>统计日期{}结束",day);
        }
        rs.put("flag",true);
        rs.put("message","Generated day bills success.");
        return rs;
    }
}
