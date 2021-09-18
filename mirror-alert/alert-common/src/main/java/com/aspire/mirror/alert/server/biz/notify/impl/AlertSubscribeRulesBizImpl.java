package com.aspire.mirror.alert.server.biz.notify.impl;

import com.aspire.mirror.alert.server.biz.notify.AlertSubscribeRulesBiz;
import com.aspire.mirror.alert.server.dao.notify.AlertSubscribeRulesDao;
import com.aspire.mirror.alert.server.dao.alert.po.Alerts;
import com.aspire.mirror.alert.server.dao.notify.po.*;
import com.aspire.mirror.alert.server.biz.alert.AlertsHisBizV2;
import com.aspire.mirror.alert.server.vo.notify.UpdateAlertSubscribeRulesVo;
import com.aspire.mirror.common.entity.PageResponse;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Slf4j
@Service
@Transactional
public class AlertSubscribeRulesBizImpl implements AlertSubscribeRulesBiz {
    @Autowired
    private AlertSubscribeRulesDao alertSubscribeRulesDao;

    /**
     * 订阅告警管理的查询
     *
     * @return
     */
    @Override
    public PageResponse<AlertSubscribeRules> query(AlertSubscribeRules alertSubscribeRules) {
        PageResponse<AlertSubscribeRules> pageResponse = new PageResponse<AlertSubscribeRules>();
        List<AlertSubscribeRules> query = alertSubscribeRulesDao.query(alertSubscribeRules);
        Integer pageWithCount = alertSubscribeRulesDao.findPageWithCount(alertSubscribeRules);
        pageResponse.setCount(pageWithCount);
        pageResponse.setResult(query);
        return pageResponse;
    }

    /**
     * 通知订阅规则管理的查询
     *
     * @return
     */
    @Override
    public PageResponse<AlertSubscribeRules> queryRules(AlertSubscribeRules alertSubscribeRules) {
        PageResponse<AlertSubscribeRules> pageResponse = new PageResponse<AlertSubscribeRules>();
        List<AlertSubscribeRules> query = alertSubscribeRulesDao.queryRules(alertSubscribeRules);
        Integer pageWithCount = alertSubscribeRulesDao.findRulesWithCount(alertSubscribeRules);
        pageResponse.setCount(pageWithCount);
        pageResponse.setResult(query);
        return pageResponse;
    }

    @Override
    public void deteleRules(List<String> idlist) {

        alertSubscribeRulesDao.deteleRules(idlist);

    }

    @Autowired
    private AlertsHisBizV2 alertsHisBizV2;

    @Override
    public void updateRules(List<String> idlist, String isOpen) {

        alertSubscribeRulesDao.updateRules(idlist, isOpen);

    }

    @Override
    public void deleteSubscribeRulesManagement(List<String> idlist) {
        alertSubscribeRulesDao.deleteSubscribeRulesManagement(idlist);
    }

    @Override
    public List<AlertSubscribeRulesManagement> querySubscribeRules() {
        return alertSubscribeRulesDao.querySubscribeRules();
    }

    /**
     * 创建和更新订阅告警信息
     *
     * @param createAlertSubscribeRules
     * @return
     */
    @Override
    @Transactional
    public void CreateSubscribeRules(CreateAlertSubscribeRules createAlertSubscribeRules) {
        CreateAlertSubscribeRules createAlertSubscribeRules1 = new CreateAlertSubscribeRules();
        //创建一个规则id
        String subscribeRules = createAlertSubscribeRules.getSubscribeRules();
        //获取到用户选择的告警信息并把它们存在告警规则管理表中
        String alertIds = createAlertSubscribeRules.getAlertIds();
        String[] alertIdArr = alertIds.split(",");
        List<String> strings = Arrays.asList(alertIdArr);
        String alertSubscribeRulesId = null;
        List alertIdsList = Lists.newArrayList();

        List<Alerts> mapAlertIdList = Lists.newArrayList();
        //4.获取到用户信息并存放到用户信息表中
        List<Reciver> reciverList = createAlertSubscribeRules.getReciverList();
        AlertSubscribeRulesManagement alertSubscribeRulesManagementsub = alertSubscribeRulesDao.selectAlertSubscribeRulesManagementBysubscribeRules(subscribeRules);
        if (null != alertSubscribeRulesManagementsub) {
            String alertSubscribeRulesId1 = alertSubscribeRulesManagementsub.getId();
            for (int i = 0; i < strings.size(); i++) {
                String id = strings.get(i);
                AlertSubscribeRulesManagement alertSubscribeRulesManagement = alertSubscribeRulesDao.querySubscribeRulesManagementByAlertId(id, alertSubscribeRulesId1);
                if (alertSubscribeRulesManagement != null) {
                    alertSubscribeRulesId = alertSubscribeRulesManagement.getAlertSubscribeRulesId();
                } else {
                    alertIdsList.add(id);
                }

            }
            if (alertIdsList != null && alertIdsList.size() > 0) {
                mapAlertIdList = alertSubscribeRulesDao.selectByIds(alertIdsList);
                for (Alerts alert : mapAlertIdList) {
                    AlertSubscribeRulesManagement alertSubscribeRulesManagement1 = new AlertSubscribeRulesManagement();
                    alertSubscribeRulesManagement1.setAlertId(alert.getAlertId());
                    alertSubscribeRulesManagement1.setAlertLevel(alert.getAlertLevel());
                    alertSubscribeRulesManagement1.setIdcType(alert.getIdcType());
                    alertSubscribeRulesManagement1.setMoniIndex(alert.getMoniIndex());
                    alertSubscribeRulesManagement1.setAlertSubscribeRulesId(alertSubscribeRulesId1);
                    alertSubscribeRulesManagement1.setDeviceIp(alert.getDeviceIp());
                    alertSubscribeRulesManagement1.setItemKey(alert.getItemKey());
                    alertSubscribeRulesManagement1.setId(UUID.randomUUID().toString());
                    alertSubscribeRulesDao.insert(alertSubscribeRulesManagement1);
                }
                for (Reciver reciverMan : reciverList) {
                    Reciver reciver = new Reciver();
                    reciver.setEmail(reciverMan.getEmail());
                    reciver.setId(UUID.randomUUID().toString());
                    reciver.setAlertSubscribeRulesId(alertSubscribeRulesId1);
                    reciver.setTelephone(reciverMan.getTelephone());
                    reciver.setNotifyObjInfo(reciverMan.getNotifyObjInfo());
                    log.info("获取到用户以配置的的通知对象信息&……%&……%&===" + reciverList);
                    alertSubscribeRulesDao.updateRevicer(reciver);
                }
                //5.获取到维护用户的信息
                createAlertSubscribeRules1.setUuid(alertSubscribeRulesId1);
                createAlertSubscribeRules1.setSubscribeRules(subscribeRules);
                createAlertSubscribeRules1.setDefensetor(createAlertSubscribeRules.getDefensetor());
                createAlertSubscribeRules1.setCreator(createAlertSubscribeRules.getCreator());
                //6.获取到触发类型
                createAlertSubscribeRules1.setNotifyAlertType(createAlertSubscribeRules.getNotifyAlertType());
                //7.获取到用户的通知方式
                createAlertSubscribeRules1.setNotifyType(createAlertSubscribeRules.getNotifyType());
                //8.获取到邮件的主题，内容和短信的主题和内容
                createAlertSubscribeRules1.setEmialContent(createAlertSubscribeRules.getEmialContent());
                createAlertSubscribeRules1.setSmsContent(createAlertSubscribeRules.getSmsContent());
                createAlertSubscribeRules1.setSubject(createAlertSubscribeRules.getSubject());
                //9。获取到是否重发
                createAlertSubscribeRules1.setIsRecurrenceInterval(createAlertSubscribeRules.getIsRecurrenceInterval());
                //10.获取到重发间隔和间隔时间
                createAlertSubscribeRules1.setRecurrenceInterval(createAlertSubscribeRules.getRecurrenceInterval());
                createAlertSubscribeRules1.setRecurrenceIntervalUtil(createAlertSubscribeRules.getRecurrenceIntervalUtil());
                createAlertSubscribeRules1.setEmailType(createAlertSubscribeRules.getEmailType());
                //11.重发次数
                createAlertSubscribeRules1.setRecurrenceCount(createAlertSubscribeRules.getRecurrenceCount());
                //12.获取到发送的时间段，以及时间间隔的开始时间段和结束时间段
                createAlertSubscribeRules1.setPeriod(createAlertSubscribeRules.getPeriod());
                createAlertSubscribeRules1.setStartPeriod(createAlertSubscribeRules.getStartPeriod());
                createAlertSubscribeRules1.setEndPeriod(createAlertSubscribeRules.getEndPeriod());
                createAlertSubscribeRules1.setIsOpen(createAlertSubscribeRules.getIsOpen());
                createAlertSubscribeRules1.setIsDelete(1);
                log.info("获取到的已配置用户输入的信息&**（……（++++++" + createAlertSubscribeRules1);
                alertSubscribeRulesDao.updateSubscribeRules(createAlertSubscribeRules1);
            } else {
                throw new RuntimeException("告警信息已经订阅过");
            }
        } else {
            List<Alerts> mapList = alertSubscribeRulesDao.selectByIds(Arrays.asList(alertIdArr));
            String uuid = UUID.randomUUID().toString();
            createAlertSubscribeRules1.setUuid(uuid);
            createAlertSubscribeRules1.setSubscribeRules(subscribeRules);
            log.info("%$@^&^$%@^%$===" + mapList);
            for (Alerts alert : mapList) {
                AlertSubscribeRulesManagement alertSubscribeRulesManagement = new AlertSubscribeRulesManagement();
                alertSubscribeRulesManagement.setAlertId(alert.getAlertId());
                alertSubscribeRulesManagement.setAlertLevel(alert.getAlertLevel());
                alertSubscribeRulesManagement.setIdcType(alert.getIdcType());
                alertSubscribeRulesManagement.setMoniIndex(alert.getMoniIndex());
                alertSubscribeRulesManagement.setAlertSubscribeRulesId(uuid);
                alertSubscribeRulesManagement.setDeviceIp(alert.getDeviceIp());
                alertSubscribeRulesManagement.setItemKey(alert.getItemKey());
                alertSubscribeRulesManagement.setId(UUID.randomUUID().toString());
                alertSubscribeRulesDao.insert(alertSubscribeRulesManagement);
            }

            for (Reciver reciverMan : reciverList) {
                Reciver reciver = new Reciver();
                reciver.setEmail(reciverMan.getEmail());
                reciver.setId(UUID.randomUUID().toString());
                reciver.setAlertSubscribeRulesId(uuid);
                reciver.setAlertSubscribeRulesId(uuid);
                reciver.setTelephone(reciverMan.getTelephone());
                reciver.setNotifyObjInfo(reciverMan.getNotifyObjInfo());
                log.info("获取到用户输入的通知对象信息&……%&……%&===" + reciverList);
                alertSubscribeRulesDao.insertRevicer(reciver);
            }
            //5.获取到维护用户的信息
            createAlertSubscribeRules1.setCreator(createAlertSubscribeRules.getCreator());
            createAlertSubscribeRules1.setDefensetor(createAlertSubscribeRules.getDefensetor());
            //6.获取到触发类型
            createAlertSubscribeRules1.setNotifyAlertType(createAlertSubscribeRules.getNotifyAlertType());
            //7.获取到用户的通知方式
            createAlertSubscribeRules1.setNotifyType(createAlertSubscribeRules.getNotifyType());
            //8.获取到邮件的主题，内容和短信的主题和内容
            createAlertSubscribeRules1.setEmialContent(createAlertSubscribeRules.getEmialContent());
            createAlertSubscribeRules1.setSmsContent(createAlertSubscribeRules.getSmsContent());
            createAlertSubscribeRules1.setSubject(createAlertSubscribeRules.getSubject());
            //9。获取到是否重发
            createAlertSubscribeRules1.setIsRecurrenceInterval(createAlertSubscribeRules.getIsRecurrenceInterval());
            //10.获取到重发间隔和间隔时间
            createAlertSubscribeRules1.setRecurrenceInterval(createAlertSubscribeRules.getRecurrenceInterval());
            createAlertSubscribeRules1.setRecurrenceIntervalUtil(createAlertSubscribeRules.getRecurrenceIntervalUtil());
            createAlertSubscribeRules1.setEmailType(createAlertSubscribeRules.getEmailType());
            //11.重发次数
            createAlertSubscribeRules1.setRecurrenceCount(createAlertSubscribeRules.getRecurrenceCount());
            //12.获取到发送的时间段，以及时间间隔的开始时间段和结束时间段
            createAlertSubscribeRules1.setPeriod(createAlertSubscribeRules.getPeriod());
            createAlertSubscribeRules1.setStartPeriod(createAlertSubscribeRules.getStartPeriod());
            createAlertSubscribeRules1.setEndPeriod(createAlertSubscribeRules.getEndPeriod());
            createAlertSubscribeRules1.setIsOpen(createAlertSubscribeRules.getIsOpen());
            createAlertSubscribeRules1.setIsDelete(1);
            log.info("获取到的用户输入的信息&**（……（++++++" + createAlertSubscribeRules1);
            alertSubscribeRulesDao.CreateSubscribeRules(createAlertSubscribeRules1);
            /*  }*/
        }

    }

    @Override
    public void UpdateSubscribeRules(UpdateAlertSubscribeRulesVo updateAlertSubscribeRulesVo) {

        List<String> id = Lists.newArrayList();
        id.add(updateAlertSubscribeRulesVo.getUuid());
        alertSubscribeRulesDao.deleteAlertSubscribeRules(id);
        alertSubscribeRulesDao.deleteReciver(id);
        alertSubscribeRulesDao.deleteAlertSubscribeRulesManagement(id);
        CreateAlertSubscribeRules createAlertSubscribeRules1 = new CreateAlertSubscribeRules();
        //1.创建一个规则id
        String uuid = UUID.randomUUID().toString();
        createAlertSubscribeRules1.setUuid(uuid);
        updateAlertSubscribeRulesVo.setUuid(uuid);
        //2.获取到用户新输入的规则名称
        createAlertSubscribeRules1.setSubscribeRules(updateAlertSubscribeRulesVo.getSubscribeRules());
        //3.获取到用户选择的告警信息并把它们存在告警规则管理表中
        String alertIds = updateAlertSubscribeRulesVo.getAlertIds();
        String[] alertIdArr = alertIds.split(",");
        List<Alerts> mapList = alertSubscribeRulesDao.selectByIds(Arrays.asList(alertIdArr));
        log.info("%$@^&^$%@^%$===" + mapList);
        for (Alerts alert : mapList) {
            AlertSubscribeRulesManagement alertSubscribeRulesManagement = new AlertSubscribeRulesManagement();
            alertSubscribeRulesManagement.setAlertId(alert.getAlertId());
            alertSubscribeRulesManagement.setAlertLevel(alert.getAlertLevel());
            alertSubscribeRulesManagement.setIdcType(alert.getIdcType());
            alertSubscribeRulesManagement.setMoniIndex(alert.getMoniIndex());
            alertSubscribeRulesManagement.setAlertSubscribeRulesId(uuid);
            alertSubscribeRulesManagement.setDeviceIp(alert.getDeviceIp());
            alertSubscribeRulesManagement.setItemKey(alert.getItemKey());
            alertSubscribeRulesManagement.setId(UUID.randomUUID().toString());
            alertSubscribeRulesDao.insert(alertSubscribeRulesManagement);
        }
        //4.获取到用户信息并存放到用户信息表中
        List<Reciver> reciverList = updateAlertSubscribeRulesVo.getReciverList();
        log.info("获取到用户输入的通知对象信息&……%&……%&===" + reciverList);
        for (Reciver reciverMan : reciverList) {
            Reciver reciver = new Reciver();
            reciver.setEmail(reciverMan.getEmail());
            reciver.setId(UUID.randomUUID().toString());
            reciver.setAlertSubscribeRulesId(uuid);
            reciver.setTelephone(reciverMan.getTelephone());
            reciver.setNotifyObjInfo(reciverMan.getNotifyObjInfo());
            alertSubscribeRulesDao.insertRevicer(reciver);
        }
        //5.获取到维护用户的信息
        createAlertSubscribeRules1.setCreator(updateAlertSubscribeRulesVo.getCreator());

        //6.获取到触发类型
        createAlertSubscribeRules1.setNotifyAlertType(updateAlertSubscribeRulesVo.getNotifyAlertType());
        createAlertSubscribeRules1.setDefensetor(updateAlertSubscribeRulesVo.getDefensetor());
        //7.获取到用户的通知方式
        createAlertSubscribeRules1.setNotifyType(updateAlertSubscribeRulesVo.getNotifyType());
        //8.获取到邮件的主题，内容和短信的主题和内容
        createAlertSubscribeRules1.setEmialContent(updateAlertSubscribeRulesVo.getEmialContent());
        createAlertSubscribeRules1.setSmsContent(updateAlertSubscribeRulesVo.getSmsContent());
        createAlertSubscribeRules1.setSubject(updateAlertSubscribeRulesVo.getSubject());
        createAlertSubscribeRules1.setIsOpen(updateAlertSubscribeRulesVo.getIsOpen());
        createAlertSubscribeRules1.setEmailType(updateAlertSubscribeRulesVo.getEmailType());
        //9。获取到是否重发
        createAlertSubscribeRules1.setIsRecurrenceInterval(updateAlertSubscribeRulesVo.getIsRecurrenceInterval());
        //10.获取到重发间隔和间隔时间
        createAlertSubscribeRules1.setRecurrenceInterval(updateAlertSubscribeRulesVo.getRecurrenceInterval());
        createAlertSubscribeRules1.setRecurrenceIntervalUtil(updateAlertSubscribeRulesVo.getRecurrenceIntervalUtil());
        //11.重发次数
        createAlertSubscribeRules1.setRecurrenceCount(updateAlertSubscribeRulesVo.getRecurrenceCount());
        //12.获取到发送的时间段，以及时间间隔的开始时间段和结束时间段
        createAlertSubscribeRules1.setPeriod(updateAlertSubscribeRulesVo.getPeriod());
        createAlertSubscribeRules1.setStartPeriod(updateAlertSubscribeRulesVo.getStartPeriod());
        createAlertSubscribeRules1.setEndPeriod(updateAlertSubscribeRulesVo.getEndPeriod());
        createAlertSubscribeRules1.setIsDelete(1);
        log.info("获取到的用户输入的信息&**（……（++++++" + createAlertSubscribeRules1);
        alertSubscribeRulesDao.CreateSubscribeRules(createAlertSubscribeRules1);

    }

    @Override
    public AlertSubscribeRulesDetailShow GetSubscribeRulesById(String id) {
        AlertSubscribeRulesDetailShow alertSubscribeRulesDetailShow = new AlertSubscribeRulesDetailShow();
        List<Reciver> recivers = alertSubscribeRulesDao.GetSubscribeRulesRevicerById(id);
        AlertSubscribeRulesDetail alertSubscribeRulesDetailShow1 = alertSubscribeRulesDao.GetSubscribeRulesById(id);
        List<AlertSubscribeRulesManagement> alertSubscribeRulesManagements = alertSubscribeRulesDao.querySubscribeRulesManagementById(id);
        alertSubscribeRulesDetailShow.setAlertSubscribeRulesDetail(alertSubscribeRulesDetailShow1);
        alertSubscribeRulesDetailShow.setReciverList(recivers);
        alertSubscribeRulesDetailShow.setAlertSubscribeRulesManagementList(alertSubscribeRulesManagements);
        return alertSubscribeRulesDetailShow;
    }

    @Override
    public List<ExportAlertSubscribeRulesManagement> export(List<String> idlist) {
        return alertSubscribeRulesDao.export(idlist);
    }


    public  List<AlertSubscribeRules> queryAlertSubscribeRules(){
        return alertSubscribeRulesDao.queryAlertSubscribeRules();
    }
}

