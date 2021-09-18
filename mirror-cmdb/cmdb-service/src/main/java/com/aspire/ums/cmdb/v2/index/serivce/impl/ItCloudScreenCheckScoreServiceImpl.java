package com.aspire.ums.cmdb.v2.index.serivce.impl;

import com.aspire.ums.cmdb.index.payload.ItCloudScreenRequest;
import com.aspire.ums.cmdb.index.payload.ScreenCheckScore;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.aspire.ums.cmdb.v2.index.mapper.ItCloudScreenCheckScoreMapper;
import com.aspire.ums.cmdb.v2.index.serivce.ItCloudScreenCheckScoreService;
import com.aspire.ums.cmdb.v2.index.util.ScreenDataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * @ClassName ItCloudScreenCheckScoreServiceImpl
 * @Description 租户扣分实体——服务层
 * @Author luowenbo
 * @Date 2020/4/9 14:33
 * @Version 1.0
 */
@Service
public class ItCloudScreenCheckScoreServiceImpl implements ItCloudScreenCheckScoreService {
    @Autowired
    private ItCloudScreenCheckScoreMapper mapper;

    @Override
    public void insert(ScreenCheckScore scs) {
        if(scs != null) {
            scs.setId(UUIDUtil.getUUID());
            scs.setInsertTime(new Date());
            scs.setIsDelete("0");
            mapper.insert(scs);
        }
    }

    @Override
    public void batchInsert(List<ScreenCheckScore> list) {
        if(list != null && !list.isEmpty()) {
            for(ScreenCheckScore item : list) {
                item.setId(UUIDUtil.getUUID());
                item.setInsertTime(new Date());
                item.setIsDelete("0");
            }
            mapper.batchInsert(list);
        }
    }

    @Override
    public void delete(ItCloudScreenRequest req) {
        mapper.delete(req);
    }

    @Override
    public ScreenCheckScore getOne(ItCloudScreenRequest req) {
        return mapper.getOne(req);
    }

    @Override
    public Map<String, Object> getCheckScoreList(ItCloudScreenRequest req) {
        // 初始化数据
        String monthTime = req.getMonthlyTime();
        req.setMonthArray(ScreenDataUtil.calMonthArray(monthTime));
        List<Map<String, Object>> tmpList = mapper.getCheckScoreByDepDetail(req);
        int year = Integer.parseInt(monthTime.split("-")[0]);
        int month = Integer.parseInt(monthTime.split("-")[1]);
        Map<String, Object> refMp = new LinkedHashMap<>();
        // 如果某个月份没有扣分数据，进行填充0，初始化
        for(int i=1;i<=month;i++){
            Map<String, Object> tmp = new HashMap<>();
            String monthStr = i<10 ? "0" + i : "" + i;
            tmp.put("month",monthStr + "月");
            tmp.put("cpuMax","0");tmp.put("inform","0");tmp.put("deploy","0");tmp.put("total","0");
            String key = year + "-" + monthStr;
            refMp.put(key,tmp);
        }
        for(Map<String, Object> item : tmpList) {
            // 当前指标的扣分项
            String score = String.format("%.1f",new BigDecimal(item.get("score").toString()).doubleValue());
            // 获取月份要返回给前端的数据
            Map<String, String> crtTmp = (Map<String, String>) refMp.get(item.get("monthTime"));
            // 指标类型
            String scoreType = item.get("scoreType").toString();
            if("cpuMax".equals(scoreType)) {
                String percent = String.format("%.2f",new BigDecimal(item.get("percent").toString()).doubleValue());
                crtTmp.put("cpuMax",percent);
            } else {
                String preTotal = crtTmp.get("total");
                String preScore = crtTmp.get(scoreType);
                String crtScore = item.get("score").toString();
                crtTmp.put(scoreType,ScreenDataUtil.scoreAdd(preScore,crtScore));
                crtTmp.put("total",ScreenDataUtil.scoreAdd(preTotal,score,"","ALL"));
            }
        }
        List<Map<String, String>> rsList = new LinkedList<>();
        String deployT = "0",informT = "0",allT = "0",avg = "0";
        int size = 0 ;
        // 获取租户的考核起始时间
        String department1 = (req.getDepartment2() == null || "".equals(req.getDepartment2())) ? null : req.getDepartment1();
        String department2 = (req.getDepartment2() == null || "".equals(req.getDepartment2())) ? req.getDepartment1() : req.getDepartment2();
        Map<String,Object> assessMp = mapper.getAssessStartMonth(department1,department2);
        String assessMonth = (null == assessMp || assessMp.get("assessMonth") == null) ? "1" : assessMp.get("assessMonth").toString();
        String assessST = (Integer.parseInt(assessMonth) < 10 ? "0" + Integer.parseInt(assessMonth) : "" + Integer.parseInt(assessMonth)) + "月";
        for(Object value : refMp.values()) {
            Map<String, String> mp = (Map<String, String>) value;
            rsList.add(mp);
            String monthPattern = mp.get("month");
            // 只有当比assessST字符串大于等于才表示是考核开始时间之后的时间
            if(monthPattern.compareTo(assessST) >= 0) {
                size++;
                avg = ScreenDataUtil.scoreAdd(avg,mp.get("cpuMax"));
            }
            deployT = ScreenDataUtil.scoreAdd(deployT,mp.get("deploy"));
            informT = ScreenDataUtil.scoreAdd(informT,mp.get("inform"));
            allT = ScreenDataUtil.scoreAdd(allT,mp.get("total"));
        }
        // 求出月平均值
        String currentAvg = ScreenDataUtil.dev(size+"",avg);
        Map<String, String> finalMp = new HashMap<>();
        finalMp.put("month","累计平均值");finalMp.put("cpuMax",currentAvg);finalMp.put("total",allT);
        finalMp.put("deploy",deployT);finalMp.put("inform",informT);
        rsList.add(finalMp);
        Map<String, Object> rs = new HashMap<>();
        // 年累计扣分,只有当当前是12月份才会有该值
        String finalCpuMaxScore = "0";
        if(month == 12) {
            finalCpuMaxScore = ScreenDataUtil.calCpuMaxScore(currentAvg);
        }
        rs.put("data",rsList);
        rs.put("final",finalCpuMaxScore);
        return rs;
    }

    @Override
    public List<Map<String, Object>> getCpuMaxByCal(ItCloudScreenRequest req) {
        return mapper.getCpuMaxByCal(req);
    }
}
