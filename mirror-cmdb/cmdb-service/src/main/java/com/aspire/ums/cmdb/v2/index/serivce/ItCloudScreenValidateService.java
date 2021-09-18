package com.aspire.ums.cmdb.v2.index.serivce;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.index.payload.ItCloudScreenExportRequest;
import com.aspire.ums.cmdb.index.payload.ItCloudScreenValidateRequest;
import com.aspire.ums.cmdb.index.payload.ScreenExportLogo;
import com.aspire.ums.cmdb.index.payload.ScreenValidate;

import java.util.List;

/**
 * @ClassName ItCloudScreenValidateService
 * @Description 大屏验证服务接口类
 * @Author luowenbo
 * @Date 2020/5/6 14:30
 * @Version 1.0
 */
public interface ItCloudScreenValidateService {
    /*
     *  新增
     * */
    void insert(ScreenValidate obj);

    /*
     *  查询，按照新增时间列出前5条
     * */
    List<ScreenValidate> listAll();

    /*
    *  验证数据是否全部导入
    * */
    JSONObject validateIsImport(ItCloudScreenValidateRequest req);

    /*
     *  验证数据是否完整（均值CPU、内存和均峰值CPU、内存得全部同时存在）
     * */
    JSONObject validateIsComplete(ItCloudScreenValidateRequest req);

    /*
     *  验证数据是否准确(每天的均峰值都要比均值大)
     * */
    JSONObject validateDataIsCorrect(ItCloudScreenValidateRequest req);

    /*
    *  excel文件生成，且上传到FTP服务器
    * */
    void createExcel(ItCloudScreenExportRequest req);

    /*
     *  判断用户选中的条件，是否已经生成EXCEL
     * */
    JSONObject judgeIfHasCreate(ItCloudScreenExportRequest req);

    /*
     *  判断用户选中的条件，是否可以下载EXCEL
     * */
    JSONObject judgeIfHasExist(ItCloudScreenExportRequest req);

    /*
     *  创建Excel数据的标识
     * */
    void createFileUnique(ItCloudScreenExportRequest req);
}
