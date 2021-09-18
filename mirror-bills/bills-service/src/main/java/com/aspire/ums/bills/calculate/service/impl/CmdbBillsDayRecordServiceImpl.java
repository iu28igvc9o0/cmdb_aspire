package com.aspire.ums.bills.calculate.service.impl;

import com.aspire.ums.bills.calculate.mapper.CmdbBillsDayRecordMapper;
import com.aspire.ums.bills.calculate.payload.CmdbBillsDayRecord;
import com.aspire.ums.bills.calculate.service.CmdbBillsDayRecordService;
import com.aspire.ums.bills.schedule.BillSchedule;
import com.aspire.ums.bills.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * @projectName: CmdbBillsDayRecordServiceImpl
 * @description: 类
 * @author: luowenbo
 * @create: 2020-08-03 15:43
 **/
@Service
public class CmdbBillsDayRecordServiceImpl implements CmdbBillsDayRecordService {

    @Autowired
    private CmdbBillsDayRecordMapper dayRecordMapper;
    @Autowired
    private BillSchedule billSchedule;

    @Override
    public void insert(CmdbBillsDayRecord record) {
        if(null != record) {
            record.setId(UUIDUtil.getUUID());
            record.setInsertPerson("System");
            record.setInsertTime(new Date());
            dayRecordMapper.insert(record);
        }
    }

    @Override
    public void batchInsert(List<CmdbBillsDayRecord> list) {
        if(!CollectionUtils.isEmpty(list)) {
            for(CmdbBillsDayRecord item : list) {
                item.setId(UUIDUtil.getUUID());
                item.setInsertPerson("System");
                item.setInsertTime(new Date());
            }
            dayRecordMapper.batchInsert(list);
        }
    }

    @Override
    public List<CmdbBillsDayRecord> getDayBillsRecordWithMonth(String chargeTime) {
        return dayRecordMapper.getDayBillsRecordWithMonth(chargeTime);
    }
}
