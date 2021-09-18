package com.aspire.fileCheck.service;

import net.sf.json.JSONObject;

import java.util.List;
import java.util.Map;

public interface IFileMissingCheckService {

    List<Map<String, String>> getFileMissingCheckData(Map<String,Object> param);

    /**
     * 获取所有指标的缺失文件列表
     * @param type
     * @param province
     * @param date
     * @param period
     * @param missing
     * @return
     */
    JSONObject getAllFileMissingData(String type, String province, String date, String period, String missing, String fileIndication, Integer currentPage, Integer pageSize);

    /**
     * 查询所有缺失的数据
     * @param uploadDate 上传时间
     * @param type 指标分类
     * @return
     */
    List<Map<String, String>> getMissingList(String uploadDate, String type);

    List<Map<String, String>> getFileCheckResultForMail(Map<String,Object> param);
}
