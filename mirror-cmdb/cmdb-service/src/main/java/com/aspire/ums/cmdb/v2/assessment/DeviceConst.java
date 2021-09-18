package com.aspire.ums.cmdb.v2.assessment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: DeviceConst
 * Author:   hangfang
 * Date:     2019/6/28
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class DeviceConst {

    //录入待审批
    public static final Integer DEVICE_STATUS_UNAPPROVE = -1;

    //保存中
    public static final Integer DEVICE_STATUS_SAVE = 2;

    //审批通过
    public static final Integer DEVICE_STATUS_PASS = 1 ;

    //审批驳回
    public static final Integer DEVICE_STATUS_REFUSE = 0;

    //审批状态
    public static final List<Integer> APPROVESTATUS = new ArrayList<>(Arrays.asList(
            DEVICE_STATUS_UNAPPROVE,
            DEVICE_STATUS_SAVE,
            DEVICE_STATUS_PASS,
            DEVICE_STATUS_REFUSE));
}
