package com.aspire.mirror.alert.server.biz.third.impl;

import com.aspire.mirror.alert.server.biz.third.TodayWaringMessageService;
import com.aspire.mirror.alert.server.dao.third.AlertsTodayWaringMessageDao;
import com.aspire.mirror.alert.server.dao.third.TodayWaringMessageDao;
import com.aspire.mirror.alert.server.vo.third.TodayWarningMessageVo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Service
@Slf4j
public class TodayWaringMessageServiceImpl implements TodayWaringMessageService {
    @Autowired
    private TodayWaringMessageDao todayWaringMessageDao;
    @Autowired
    private AlertsTodayWaringMessageDao alertsTodayWaringMessageDao;

    @Override
    public List<TodayWarningMessageVo> getSummaryByIdctype(Map<String, Object> param) {
        //根据时间去查询出当前待确认和确认的告警信息
        List todayWarningMessageDTOList = new ArrayList();
        List<Map<String, Object>> summaryByIdctype = todayWaringMessageDao.getSummaryByIdctype(param);
        if (!CollectionUtils.isEmpty(summaryByIdctype)) {
            for (int i = 0; i < summaryByIdctype.size(); i++) {
                if ("toBeConfirmed".equals(summaryByIdctype.get(i).get("operateStatus")) ||
                        "confirmed".equals(summaryByIdctype.get(i).get("operateStatus"))) {
                    TodayWarningMessageVo todayWarningMessageVo = new TodayWarningMessageVo();
                    if ("toBeConfirmed".equals(summaryByIdctype.get(i).get("operateStatus"))) {
                        todayWarningMessageVo.setName("待确认");
                    } else if ("confirmed".equals(summaryByIdctype.get(i).get("operateStatus"))) {
                        todayWarningMessageVo.setName("已确认");
                    }
                    todayWarningMessageVo.setSum(summaryByIdctype.get(i).get("codeCount") == null ? 0 : Integer.valueOf(String.valueOf(summaryByIdctype.get(i).get("codeCount"))));
                    todayWarningMessageVo.setElementary(summaryByIdctype.get(i).get("lCount") == null ? 0 : Integer.valueOf(String.valueOf(summaryByIdctype.get(i).get("lCount"))));
                    todayWarningMessageVo.setMediumGrade(summaryByIdctype.get(i).get("mCount") == null ? 0 : Integer.valueOf(String.valueOf(summaryByIdctype.get(i).get("mCount"))));
                    todayWarningMessageVo.setSenior(summaryByIdctype.get(i).get("hCount") == null ? 0 : Integer.valueOf(String.valueOf(summaryByIdctype.get(i).get("hCount"))));
                    todayWarningMessageVo.setGreat(summaryByIdctype.get(i).get("sCount") == null ? 0 : Integer.valueOf(String.valueOf(summaryByIdctype.get(i).get("sCount"))));
                    todayWarningMessageDTOList.add(todayWarningMessageVo);
                }
                continue;
            }
        }
        List<Map<String, Object>> hisSummaryByIdctyp = null;
        int i = 0;int a = 0;int b = 0;int c = 0;int d = 0;
        TodayWarningMessageVo todayWarningMessageVo = new TodayWarningMessageVo();
        try {
            hisSummaryByIdctyp = alertsTodayWaringMessageDao.getHisSummaryByIdctyp(param);
            if (!CollectionUtils.isEmpty(hisSummaryByIdctyp)) {
                for (int j = 0; j < hisSummaryByIdctyp.size(); j++) {

                    //清除人
                    if (!"".equals(hisSummaryByIdctyp.get(j).get("clear_user")) && null != hisSummaryByIdctyp.get(j).get("clear_user")) {
                        todayWarningMessageVo.setName("已清除");
                        //获取到清除告警信息的总数
                        i += Integer.parseInt(hisSummaryByIdctyp.get(j).get("codeCount").toString());
                        a += Integer.parseInt(hisSummaryByIdctyp.get(j).get("lCount").toString());
                        b += Integer.parseInt(hisSummaryByIdctyp.get(j).get("mCount").toString());
                        c += Integer.parseInt(hisSummaryByIdctyp.get(j).get("hCount").toString());
                        d += Integer.parseInt(hisSummaryByIdctyp.get(j).get("sCount").toString());
                    } else {
                        TodayWarningMessageVo todayWarningMessageVo1 = new TodayWarningMessageVo();
                        todayWarningMessageVo1.setName("已解除");
                        //获取到告警信息的总数
                        todayWarningMessageVo1.setSum(hisSummaryByIdctyp.get(j).get("codeCount") == null ? 0 : Integer.valueOf(String.valueOf(hisSummaryByIdctyp.get(j).get("codeCount"))));
                        todayWarningMessageVo1.setElementary(hisSummaryByIdctyp.get(j).get("lCount") == null ? 0 : Integer.valueOf(String.valueOf(hisSummaryByIdctyp.get(j).get("lCount"))));
                        todayWarningMessageVo1.setMediumGrade(hisSummaryByIdctyp.get(j).get("mCount") == null ? 0 : Integer.valueOf(String.valueOf(hisSummaryByIdctyp.get(j).get("mCount"))));
                        todayWarningMessageVo1.setSenior(hisSummaryByIdctyp.get(j).get("hCount") == null ? 0 : Integer.valueOf(String.valueOf(hisSummaryByIdctyp.get(j).get("hCount"))));
                        todayWarningMessageVo1.setGreat(hisSummaryByIdctyp.get(j).get("sCount") == null ? 0 : Integer.valueOf(String.valueOf(hisSummaryByIdctyp.get(j).get("sCount"))));
                        todayWarningMessageDTOList.add(todayWarningMessageVo1);
                    }
                }
            }
            if(!CollectionUtils.isEmpty(hisSummaryByIdctyp)){
            todayWarningMessageVo.setSum(i);
            todayWarningMessageVo.setElementary(a);
            todayWarningMessageVo.setMediumGrade(b);
            todayWarningMessageVo.setSenior(c);
            todayWarningMessageVo.setGreat(d);
            todayWarningMessageDTOList.add(todayWarningMessageVo);
            }

        } catch (Exception e) {
            log.info("eror" + e);
        }
        return todayWarningMessageDTOList;
    }


    @Override
    public List<TodayWarningMessageVo> getSummaryByDeviceMfrs(Map<String, Object> param) {
        List<Map<String, Object>> summaryByyDeviceMfrs = todayWaringMessageDao.getSummaryByDeviceMfrs(param);
        return util(summaryByyDeviceMfrs);
    }

    @Override
    public List<TodayWarningMessageVo> getSummaryByDeviceTypeAndSoyrceRoomAndDeviceClass(Map<String, Object> param) {
        List summaryByIdctypeList = new ArrayList();
        List<Map<String, Object>> summaryByDeviceTypeAndSoyrceRoomAndDeviceClass = todayWaringMessageDao.getSummaryByDeviceTypeAndSoyrceRoomAndDeviceClass(param);
        if (!CollectionUtils.isEmpty(summaryByDeviceTypeAndSoyrceRoomAndDeviceClass)) {
            for (int i = 0; i < summaryByDeviceTypeAndSoyrceRoomAndDeviceClass.size(); i++) {
                TodayWarningMessageVo todayWarningMessageVo = new TodayWarningMessageVo();
                if ("X86服务器".equals(summaryByDeviceTypeAndSoyrceRoomAndDeviceClass.get(i).get("device_class"))) {
                    todayWarningMessageVo.setName("裸金属");
                    todayWarningMessageVo.setSum(summaryByDeviceTypeAndSoyrceRoomAndDeviceClass.get(i).get("codeCount") == null ? 0 : Integer.valueOf(String.valueOf(summaryByDeviceTypeAndSoyrceRoomAndDeviceClass.get(i).get("codeCount"))));
                    todayWarningMessageVo.setElementary(summaryByDeviceTypeAndSoyrceRoomAndDeviceClass.get(i).get("lCount") == null ? 0 : Integer.valueOf(String.valueOf(summaryByDeviceTypeAndSoyrceRoomAndDeviceClass.get(i).get("lCount"))));
                    todayWarningMessageVo.setMediumGrade(summaryByDeviceTypeAndSoyrceRoomAndDeviceClass.get(i).get("mCount") == null ? 0 : Integer.valueOf(String.valueOf(summaryByDeviceTypeAndSoyrceRoomAndDeviceClass.get(i).get("mCount"))));
                    todayWarningMessageVo.setSenior(summaryByDeviceTypeAndSoyrceRoomAndDeviceClass.get(i).get("hCount") == null ? 0 : Integer.valueOf(String.valueOf(summaryByDeviceTypeAndSoyrceRoomAndDeviceClass.get(i).get("hCount"))));
                    todayWarningMessageVo.setGreat(summaryByDeviceTypeAndSoyrceRoomAndDeviceClass.get(i).get("sCount") == null ? 0 : Integer.valueOf(String.valueOf(summaryByDeviceTypeAndSoyrceRoomAndDeviceClass.get(i).get("sCount"))));
                    summaryByIdctypeList.add(todayWarningMessageVo);
                } else {
                    todayWarningMessageVo.setName((String) summaryByDeviceTypeAndSoyrceRoomAndDeviceClass.get(i).get("device_class") == null ? null : (String) summaryByDeviceTypeAndSoyrceRoomAndDeviceClass.get(i).get("device_class"));
                    todayWarningMessageVo.setSum(summaryByDeviceTypeAndSoyrceRoomAndDeviceClass.get(i).get("codeCount") == null ? 0 : Integer.valueOf(String.valueOf(summaryByDeviceTypeAndSoyrceRoomAndDeviceClass.get(i).get("codeCount"))));
                    todayWarningMessageVo.setElementary(summaryByDeviceTypeAndSoyrceRoomAndDeviceClass.get(i).get("lCount") == null ? 0 : Integer.valueOf(String.valueOf(summaryByDeviceTypeAndSoyrceRoomAndDeviceClass.get(i).get("lCount"))));
                    todayWarningMessageVo.setMediumGrade(summaryByDeviceTypeAndSoyrceRoomAndDeviceClass.get(i).get("mCount") == null ? 0 : Integer.valueOf(String.valueOf(summaryByDeviceTypeAndSoyrceRoomAndDeviceClass.get(i).get("mCount"))));
                    todayWarningMessageVo.setSenior(summaryByDeviceTypeAndSoyrceRoomAndDeviceClass.get(i).get("hCount") == null ? 0 : Integer.valueOf(String.valueOf(summaryByDeviceTypeAndSoyrceRoomAndDeviceClass.get(i).get("hCount"))));
                    todayWarningMessageVo.setGreat(summaryByDeviceTypeAndSoyrceRoomAndDeviceClass.get(i).get("sCount") == null ? 0 : Integer.valueOf(String.valueOf(summaryByDeviceTypeAndSoyrceRoomAndDeviceClass.get(i).get("sCount"))));
                    summaryByIdctypeList.add(todayWarningMessageVo);
                }
            }
        }
        return summaryByIdctypeList;
    }

    private List util(List<Map<String, Object>> summaryByyDeviceMfrs) {
        List summaryByIdctypeList = new ArrayList();
        if (!CollectionUtils.isEmpty(summaryByyDeviceMfrs)) {
            for (int i = 0; i < summaryByyDeviceMfrs.size(); i++) {
                TodayWarningMessageVo todayWarningMessageDTO = new TodayWarningMessageVo();
                todayWarningMessageDTO.setName((String) summaryByyDeviceMfrs.get(i).get("device_mfrs"));
                todayWarningMessageDTO.setSum(summaryByyDeviceMfrs.get(i).get("codeCount") == null ? 0 : Integer.valueOf(String.valueOf(summaryByyDeviceMfrs.get(i).get("codeCount"))));
                todayWarningMessageDTO.setElementary(summaryByyDeviceMfrs.get(i).get("lCount") == null ? 0 : Integer.valueOf(String.valueOf(summaryByyDeviceMfrs.get(i).get("lCount"))));
                todayWarningMessageDTO.setMediumGrade(summaryByyDeviceMfrs.get(i).get("mCount") == null ? 0 : Integer.valueOf(String.valueOf(summaryByyDeviceMfrs.get(i).get("mCount"))));
                todayWarningMessageDTO.setSenior(summaryByyDeviceMfrs.get(i).get("hCount") == null ? 0 : Integer.valueOf(String.valueOf(summaryByyDeviceMfrs.get(i).get("hCount"))));
                todayWarningMessageDTO.setGreat(summaryByyDeviceMfrs.get(i).get("sCount") == null ? 0 : Integer.valueOf(String.valueOf(summaryByyDeviceMfrs.get(i).get("sCount"))));
                summaryByIdctypeList.add(todayWarningMessageDTO);
            }
        }
        return summaryByIdctypeList;
    }
}
