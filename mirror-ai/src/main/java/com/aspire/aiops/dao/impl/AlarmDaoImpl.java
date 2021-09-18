package com.aspire.aiops.dao.impl;

import com.aspire.aiops.dao.AlarmDao;
import com.aspire.aiops.utils.DbPoolUtil;
import com.aspire.aiops.domain.ActiveAlarm;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aspire.aiops.utils.ResourceUtil;
import com.aspire.aiops.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.aspire.aiops.utils.TimeUtil;

/**
 * @author ：Vincent Hu
 * @date ：Created in 6/18/2019 11:06
 * @description：${description}
 * @modified By：
 * @version: $version$
 */
public class AlarmDaoImpl implements AlarmDao{

    private DbPoolUtil dbPool = DbPoolUtil.getInstance();
    private static final Logger log = LoggerFactory.getLogger(AlarmDaoImpl.class);

    /*private static String selectAlarmTimeSql = "SELECT alert_id, item_id, alert_start_time FROM alert_alerts WHERE " +
            " item_id IS NOT NULL AND "+
            " alert_start_time <= FROM_UNIXTIME(UNIX_TIMESTAMP(?), '%Y-%m-%d %H:%i:%S') AND " +
            " alert_start_time >= FROM_UNIXTIME(UNIX_TIMESTAMP(?)-7200, '%Y-%m-%d %H:%i:%S')" +
            "ORDER BY alert_start_time DESC";*/

    private static String where = " WHERE item_id IS NOT NULL AND "+
            " cur_moni_time > FROM_UNIXTIME(UNIX_TIMESTAMP(?)-1800, '%Y-%m-%d %H:%i:%S') AND " +
            " cur_moni_time < FROM_UNIXTIME(UNIX_TIMESTAMP(?), '%Y-%m-%d %H:%i:%S')";
            //"ORDER BY alert_start_time DESC";

    private static String selectItemsSql = "SELECT DISTINCT item_id FROM alert_alerts_detail "+where;

    private static String selectTimeSql = "SELECT MIN(cur_moni_time) AS minTime, MAX(cur_moni_time) AS maxTime FROM alert_alerts_detail "+where;

    //private static String sampleWhere = " WHERE item_id IS NOT NULL ORDER BY alert_start_time DESC LIMIT 50";

    private static String sampleWhere = " WHERE item_id IS NOT NULL AND "+
            " cur_moni_time >= FROM_UNIXTIME((SELECT UNIX_TIMESTAMP(max(cur_moni_time)) FROM alert_alerts_detail)-1800, '%Y-%m-%d %H:%i:%S') AND " +
            " cur_moni_time <= FROM_UNIXTIME((SELECT UNIX_TIMESTAMP(max(cur_moni_time)) FROM alert_alerts_detail), '%Y-%m-%d %H:%i:%S')";

    private static String selectSampleItemsSql = "SELECT DISTINCT item_id FROM alert_alerts_detail "+sampleWhere;

    private static String selectSampleTimeSql = "SELECT (SELECT FROM_UNIXTIME((SELECT UNIX_TIMESTAMP(max(cur_moni_time)) FROM alert_alerts_detail)-7200, '%Y-%m-%d %H:%i:%S')) AS minTime, MAX(cur_moni_time) AS maxTime FROM alert_alerts ";

    private static Integer timeInterval = Integer.parseInt(ResourceUtil.loadResource("config.properties").getProperty("time.interval"));

    /*查询时间区间*/
    public Map<String,Long> selectTime(String time) throws Exception{

        Connection con = null;
        PreparedStatement stmt = null;
        //System.out.println(stmt);
        ResultSet resultSet = null;
        Map<String, Long> map = new HashMap<>();
        if(!map.isEmpty()) {
            try {
                con = dbPool.getConnection();
                stmt = con.prepareStatement(selectTimeSql);
        /*System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println(selectTimeSql);
        System.out.println(time);
        log.info(selectTimeSql);*/
                stmt.setString(1,time); //small time
                stmt.setString(2,time);
                resultSet = stmt.executeQuery();
                while (resultSet.next()) {
                    map.put("startTime", TimeUtil.time2Seconds(resultSet.getString(1)));
                    log.info("startTime: " + resultSet.getString(1));
                    map.put("endTime", TimeUtil.time2Seconds(resultSet.getString(2)));
                    log.info("endTime: " + resultSet.getString(2));
                }

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (resultSet != null){
                    try {
                        resultSet.close();
                    } catch (Exception e) {

                    }
                }
                if (stmt != null){
                    try {
                        stmt.close();
                    } catch (Exception e) {

                    }
                }
                if (con != null){
                    try {
                        con.close();
                    } catch (Exception e) {

                    }
                }
            }
        }
        return map;
    }

    /*查询告警item*/
    public List<Integer> selectItems(String time)throws Exception{
        Connection con = null;
        PreparedStatement stmt = null;


        ResultSet resultSet = null;
        List<Integer> list = new ArrayList();
        try {
            con = dbPool.getConnection();
            stmt = con.prepareStatement(selectItemsSql);
            stmt.setString(1,time);
            stmt.setString(2,time);
            resultSet = stmt.executeQuery();

            while(resultSet.next()){
                list.add(resultSet.getInt("item_id"));
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (resultSet != null){
                try {
                    resultSet.close();
                } catch (Exception e) {

                }
            }
            if (stmt != null){
                try {
                    stmt.close();
                } catch (Exception e) {

                }
            }
            if (con != null){
                try {
                    con.close();
                } catch (Exception e) {

                }
            }
        }
        return list;
    }


    private static String selectItemsSql2 = "SELECT alert_id, item_id, alert_start_time, cur_moni_time FROM alert_alerts "+where;

    public List<Map<String, Object>> selectItems2(String time) throws Exception{
        Connection con = null;
        PreparedStatement stmt = null;


        ResultSet resultSet = null;
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            con = dbPool.getConnection();
            stmt = con.prepareStatement(selectItemsSql2);
            stmt.setString(1,time);
            stmt.setString(2,time);
            resultSet = stmt.executeQuery();
            while(resultSet.next()){
                Map<String, Object> map = new HashMap<>();
                map.put("alertId",resultSet.getString("alert_id"));
                map.put("itemId",resultSet.getString("item_id"));
                map.put("alertStartTime",resultSet.getString("alert_start_time"));
                map.put("curMoniTime",resultSet.getString("cur_moni_time"));
                list.add(map);
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (resultSet != null){
                try {
                    resultSet.close();
                } catch (Exception e) {

                }
            }
            if (stmt != null){
                try {
                    stmt.close();
                } catch (Exception e) {

                }
            }
            if (con != null){
                try {
                    con.close();
                } catch (Exception e) {

                }
            }
        }
        return list;

    }

    /*public List<ActiveAlarm> selectAlarmTime(String time) throws Exception{

        Connection con = dbPool.getConnection();
        PreparedStatement stmt = con.prepareStatement(selectAlarmTimeSql);
        stmt.setString(1,time);
        stmt.setString(2,time);

        ResultSet resultSet = null;
        try {
            resultSet = stmt.executeQuery();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        //println(res)
        List<ActiveAlarm> ret = new ArrayList();
        while(resultSet.next()){
            *//*System.out.println(resultSet.getString(1));
            System.out.println(resultSet.getString(2));
            System.out.println(resultSet.getString(3));*//*
            ActiveAlarm activeAlarm = new ActiveAlarm();
            activeAlarm.setAlertId(resultSet.getString("alert_id"));
            activeAlarm.setAlertTime(resultSet.getString("alert_start_time"));
            activeAlarm.setItemid(resultSet.getString("item_id"));
            ret.add(activeAlarm);
        }

        try {
            stmt.close();
            con.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        return ret;
    }*/

    public Map<String,Long> selectSampleTime() throws Exception{
        Connection con = null;
        PreparedStatement stmt = null;


        /*System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println(selectTimeSql);
        System.out.println(time);
        log.info(selectTimeSql);*/
        //System.out.println(stmt);

        ResultSet resultSet = null;
        Map<String, Long> map = new HashMap<>();
        if(!map.isEmpty()) {
            try {
                con = dbPool.getConnection();
                stmt = con.prepareStatement(selectSampleTimeSql);
                resultSet = stmt.executeQuery();
                while (resultSet.next()) {
                    map.put("startTime", TimeUtil.time2Seconds(resultSet.getString(1)));
                    log.info("startTime: " + resultSet.getString(1));
                    map.put("endTime", TimeUtil.time2Seconds(resultSet.getString(2)));
                    log.info("endTime: " + resultSet.getString(2));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (resultSet != null){
                    try {
                        resultSet.close();
                    } catch (Exception e) {

                    }
                }
                if (stmt != null){
                    try {
                        stmt.close();
                    } catch (Exception e) {

                    }
                }
                if (con != null){
                    try {
                        con.close();
                    } catch (Exception e) {

                    }
                }
            }
        }

        return map;
    }


    public List<Integer> selectSampleItems()throws Exception{
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;
        List<Integer> list = new ArrayList();
        try {
            con = dbPool.getConnection();
            stmt = con.prepareStatement(selectSampleItemsSql);
            resultSet = stmt.executeQuery();
            while(resultSet.next()){
                list.add(resultSet.getInt("item_id"));
            }
            stmt.close();
            con.close();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (resultSet != null){
                try {
                    resultSet.close();
                } catch (Exception e) {

                }
            }
            if (stmt != null){
                try {
                    stmt.close();
                } catch (Exception e) {

                }
            }
            if (con != null){
                try {
                    con.close();
                } catch (Exception e) {

                }
            }
        }
        return list;
    }


}
