package com.aspire.mirror.alert.server.util;

import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class TransformUtils {

    public static <T, S> List<T> transform(Class<T> target, final List<S> listAlertsDTO) {
        if (CollectionUtils.isEmpty(listAlertsDTO)) {
            return Lists.newArrayList();
        }
        List<T> listAlerts = Lists.newArrayList();
        for (S alertsDTO : listAlertsDTO) {
            T alertsDetailResponse = null;
            try {
                alertsDetailResponse = target.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            BeanUtils.copyProperties(alertsDTO, alertsDetailResponse);
            listAlerts.add(alertsDetailResponse);
        }
        return listAlerts;
    }

    public static <T, S> T transform(Class<T> target, S alert) {
        if (null == alert) {
            return null;
        }
        T alertsDetailResponse = null;
        try {
            alertsDetailResponse = target.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        BeanUtils.copyProperties(alert, alertsDetailResponse);
        return alertsDetailResponse;
    }
}
