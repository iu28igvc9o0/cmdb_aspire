package com.aspire.mirror.webservice;

import com.cmcc.family.familySDK.NationalFactory;
import com.cmcc.family.familySDK.api.national.NationalOsgiAlarmInfo;
import com.cmcc.family.familySDK.api.national.NationalOsgiInfo;
import com.cmcc.family.familySDK.api.national.NationalProgramInfo;
import com.cmcc.family.familySDK.api.national.NationalUserInfo;

/**
 * Created with IntelliJ IDEA.
 * User: jw.zhu
 * Date: 2018/11/6
 * 调用family SDK接口工厂类
 * com.aspire.mirror.webservice.IPlatformFactory
 */
public class IPlatformFactory {

    public static NationalUserInfo getNationalUserInfo() {
        return NationalFactory.getNationalUserInfo();
    }

    public static NationalProgramInfo getNationalProgramInfo() {
        return NationalFactory.getNationalProgramInfo();
    }

    public static NationalOsgiInfo getNationalOsgiInfo() {
        return NationalFactory.getNationalOsgiInfo();
    }

    public static NationalOsgiAlarmInfo getNationalOsgiAlarmInfo() {
        return NationalFactory.getNationalOsgiAlarmInfo();
    }
}
