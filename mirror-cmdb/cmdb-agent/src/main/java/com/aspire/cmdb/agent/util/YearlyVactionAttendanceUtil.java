package com.aspire.cmdb.agent.util;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.aspire.ums.cmdb.v2.restful.mapper.SlaMsgRecordMapper;
import com.google.common.collect.Lists;

/**
 * 计算两日期时间之间的小时差
 */
@Component
public class YearlyVactionAttendanceUtil {
    private static final Logger logger = LoggerFactory.getLogger(YearlyVactionAttendanceUtil.class);
    @Autowired
    private SlaMsgRecordMapper slaMsgRecordMapper;
    private static Map<String, List<Date>> vacationMap;
    private static Map<String, List<Date>> attendanceMap;
    private String SHORT_DATE_FORMAT = "yyyy-MM-dd";

    @Scheduled(cron = "0 30 1 * * ?")
    public void refresh() {
        List<Map<String,Object>> daysList = slaMsgRecordMapper.listNoWorkingDays();
        if (CollectionUtils.isEmpty(daysList)) {
            return;
        }
        if (vacationMap == null || attendanceMap == null) {
            return;
        }
        vacationMap.clear();
        attendanceMap.clear();
        fillVacationAttendanceCache(daysList);
    }

    // @PostConstruct
    private void init() {
        vacationMap = new HashMap<>();
        attendanceMap = new HashMap<>();
        List<Map<String,Object>> daysList = slaMsgRecordMapper.listNoWorkingDays();
        if (CollectionUtils.isNotEmpty(daysList)) {
            fillVacationAttendanceCache(daysList);
        }
    }

    private void fillVacationAttendanceCache(List<Map<String,Object>> daysList) {
        for (Map<String,Object> day : daysList) {
            String year = day.get("year").toString();
            try {
                Date concreDate = new SimpleDateFormat(SHORT_DATE_FORMAT).parse(day.get("day").toString());
                switch (Integer.parseInt(day.get("type").toString())) {
                    case 0: // 节假日
                        if (vacationMap.containsKey(year)) {
                            List<Date> vacationList = vacationMap.get(year);
                            vacationList.add(concreDate);
                        } else {
                            List<Date> vacationList = Lists.newArrayList();
                            vacationList.add(concreDate);
                            vacationMap.put(year, vacationList);
                        }
                        break;
                    case 1: // 出勤日
                        if (attendanceMap.containsKey(year)) {
                            List<Date> attendanceList = attendanceMap.get(year);
                            attendanceList.add(concreDate);
                        } else {
                            List<Date> attendanceList = Lists.newArrayList();
                            attendanceList.add(concreDate);
                            attendanceMap.put(year, attendanceList);
                        }
                        break;
                }
            } catch (ParseException e) {
                logger.error("日期格式化出错!", e);
            }
        }
    }

    /**
     *  计算放假的天数
     */
    public long calculateHoliday(){
        LocalDateTime nowTime = LocalDateTime.now();
        int rs = 0;
        // 当天判断
        if(isDayDutyOff(Date.from(nowTime.atZone(ZoneId.systemDefault()).toInstant()))) {
            rs++;
        }
        long sub = 1L;
        while(true) {
            nowTime = nowTime.minusDays(sub);
            if(isDayDutyOff(Date.from(nowTime.atZone(ZoneId.systemDefault()).toInstant()))) {
                rs++;
            } else {
                break;
            }
        }
        return rs*24;
    }

    /**
     *  time + N ： 时间增加 N 天
     * @param startDate 开始时间
     * @param N 天数
     * @param isRest 是否工作日
     * @return
     */
    public LocalDateTime predictTime(LocalDateTime startDate, int N, Boolean isRest) {
        if(N == 0) {
            return startDate;
        }
        if(!isRest) {
            return startDate.plusDays(N);
        }
        int day = 0;
        LocalDateTime endDate = startDate;
        while(true) {
            if(isDayDutyOff(Date.from(endDate.atZone(ZoneId.systemDefault()).toInstant()))) {
                endDate = endDate.plusDays(1L);
                continue;
            }
            if(day == N) {
                break;
            }
            day++;
            endDate = endDate.plusDays(1L);
        }
        return endDate;
    }

    /**
     * 判断当天是否休息
     * 只接收 yyyy-MM-dd 日期
     * @param shortDate
     * @return
     */
    private boolean isDayDutyOff(Date shortDate) {
        String year = new SimpleDateFormat(SHORT_DATE_FORMAT).format(shortDate).substring(0, 4);
        List<Date> vacationList = vacationMap.get(year);
        if (CollectionUtils.isNotEmpty(vacationList)) {
            for (Date holiday : vacationList) {
                int c = shortDate.compareTo(holiday);
                if (c == 0) { // 节假日
                    return true;
                }
            }
        }
        List<Date> attendanceList = attendanceMap.get(year);
        if (CollectionUtils.isNotEmpty(attendanceList)) {
            for (Date dutyDay : attendanceList) {
                int c = shortDate.compareTo(dutyDay);
                if (c == 0) { // 工作日
                    return false;
                }
            }
        }
        // 判断是否属于周六/周日
        Calendar cal = Calendar.getInstance();
        cal.setTime(shortDate);
        int week = cal.get(Calendar.DAY_OF_WEEK) - 1;
        boolean flag = (week == 0 || week == 6);
        return flag;
    }

    public double workHous(Date start, Date end) throws ParseException {
        SimpleDateFormat shortDateFormat = new SimpleDateFormat(SHORT_DATE_FORMAT);
        Date shortStart = shortDateFormat.parse(shortDateFormat.format(start));
        Date shortEnd = shortDateFormat.parse(shortDateFormat.format(end));
        // 开始时间是否属于休息日
        boolean isStartDutyOff = isDayDutyOff(shortStart);
        boolean isEndDutyOff = isDayDutyOff(shortEnd);
        if (isStartDutyOff) {
            if (!isEndDutyOff) { // 开始时间在休息日里，结束时间不在休息日里（开始那天不计算小时数，结束那天计算小时数）
                Calendar cal = Calendar.getInstance();
                cal.setTime(shortStart);
                cal.add(Calendar.DAY_OF_MONTH, +1);
                Date validShortStart = cal.getTime();
                Date validShortStartTemp = validShortStart;

                int skipDay = 0;
                while (validShortStartTemp.compareTo(end) != 1) {
                    if (isDayDutyOff(validShortStartTemp)) {
                        skipDay += 1;
                    }
                    cal.add(Calendar.DAY_OF_MONTH, +1);
                    validShortStartTemp = cal.getTime();
                }
                return ((end.getTime() - validShortStart.getTime()) / (60 * 60 * 1000.00)) - skipDay * 24;
            } else { //开始时间在休息日里，结束时间也在休息日里（开始那天不计算小时数，结束那天也不计算小时数，看中间有多少个工作日）
                Calendar cal = Calendar.getInstance();
                cal.setTime(shortStart);
                cal.add(Calendar.DAY_OF_MONTH, +1);
                Date validShortStart = cal.getTime();
                // 工作日天数
                int dutyDays = 0;
                while (validShortStart.compareTo(end) != 1) {
                    if (!isDayDutyOff(validShortStart)) {
                        dutyDays += 1;
                    }
                    cal.add(Calendar.DAY_OF_MONTH, +1);
                    validShortStart = cal.getTime();
                }
                return dutyDays * 24;
            }
        } else {
            if (isEndDutyOff) { //开始时间不在休息日里，结束时间在休息日里
                int dutyDays = 0;
                Calendar cal = Calendar.getInstance();
                cal.setTime(shortStart);
                cal.add(Calendar.DAY_OF_MONTH, +1);
                Date validShortStart = cal.getTime();
                while (validShortStart.compareTo(end) != 1) {
                    if (!isDayDutyOff(validShortStart)) {
                        dutyDays += 1;
                    }
                    cal.add(Calendar.DAY_OF_MONTH, +1);
                    validShortStart = cal.getTime();
                }
                Calendar ca = Calendar.getInstance();
                ca.setTime(start);
//                int startHour = ca.get(Calendar.HOUR_OF_DAY);
                // 数据要力求精准
                Calendar startCal = Calendar.getInstance();
                startCal.setTime(shortStart);
                startCal.add(Calendar.DAY_OF_MONTH, +1);
                Date startDateEndTime = startCal.getTime();
                return ((startDateEndTime.getTime() - start.getTime())  / (60 * 60 * 1000.00)) + dutyDays * 24;
            } else { //开始时间在不在休息日里，结束时间也不在休息日里
                int skipDay = 0;
                Calendar cal = Calendar.getInstance();
                cal.setTime(shortStart);
                cal.add(Calendar.DAY_OF_MONTH, +1);
                Date validShortStart = cal.getTime();
                while (validShortStart.compareTo(end) != 1) {
                    if (isDayDutyOff(validShortStart)) {
                        skipDay += 1;
                    }
                    cal.add(Calendar.DAY_OF_MONTH, +1);
                    validShortStart = cal.getTime();
                }
                return ((end.getTime() - start.getTime()) / (60 * 60 * 1000.00)) - skipDay * 24;
            }
        }
    }

    public long workMinute(Date start, Date end){
        try {
            BigDecimal hours = new BigDecimal(workHous(start, end));
            BigDecimal m = new BigDecimal(60);
            return hours.multiply(m).longValue();
        } catch (Exception e) {
            System.out.println("计算时间间隔出错!");
            e.printStackTrace();
        }
        return 0L;
    }
    
    public long noWorkTimeMillis(Date start, Date end){
        try {
            BigDecimal hours = new BigDecimal(workHous(start, end));
            BigDecimal m = new BigDecimal(3600000);
            long du = (end.getTime()-start.getTime());
            long time = du-hours.multiply(m).longValue();
            return time;
        } catch (Exception e) {
            System.out.println("计算时间间隔出错!");
            e.printStackTrace();
        }
        return 0L;
    }
}
