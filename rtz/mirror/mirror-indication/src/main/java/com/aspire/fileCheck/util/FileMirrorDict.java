package com.aspire.fileCheck.util;

/**
 * 文件检测状态字典表
 */
public class FileMirrorDict {
    //文件上传延时状态
    public static final Integer LAZY_STATUS_ONE = 1;
    public static final Integer LAZY_STATUS_ONETOTWO = 2;
    public static final Integer LAZY_STATUS_TWOTOSIX = 3;
    public static final Integer LAZY_STATUS_SIX = 33;

    //文件缺失完整性状态
    public static final String MISSING = "缺失";
    public static final String NORMAL = "正常";
    public static final String INTEGRITY = "完整";

    // 字典表类型
    public static final String PERIOD = "PERIOD";
    public static final String LAZY_STATUS = "LAZY_STATUS";
    public static final String FILE_STATE_STATUS = "FILE_STATE_STATUS";
}
