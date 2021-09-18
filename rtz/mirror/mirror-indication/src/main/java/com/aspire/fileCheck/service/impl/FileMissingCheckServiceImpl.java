package com.aspire.fileCheck.service.impl;

import com.aspire.fileCheck.dao.IFileMissingCheckDAO;
import com.aspire.fileCheck.entity.FileIndicationEntity;
import com.aspire.fileCheck.service.IFileIndicationPeriodConfigService;
import com.aspire.fileCheck.service.IFileMissingCheckService;
import com.aspire.mirror.util.TimeUtil;
import com.google.common.collect.Maps;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FileMissingCheckServiceImpl implements IFileMissingCheckService {
    @Autowired
    private IFileMissingCheckDAO fileMissingCheckDao;
    @Autowired
    private IFileIndicationPeriodConfigService iFileIndicationPeriodConfigService;

    @Override
    public List<Map<String, String>> getFileMissingCheckData(Map<String, Object> param) {
        return fileMissingCheckDao.getFileMissingCheckData(param);
    }
    public Integer getFileMissingCheckDataCount(Map<String, Object> param) {
        return fileMissingCheckDao.getFileMissingCheckDataCount(param);
    }

    @Override
    public JSONObject getAllFileMissingData(String type, String province, String date, String period, String missing, String fileIndication,
                                            Integer currentPage, Integer pageSize) {
        JSONObject returnJSON = new JSONObject();
        province = "000".equals(province) ? "" : province;
        // 组装日期
        if (StringUtils.isNotBlank(date)) {
            date = TimeUtil.clearDateStrPattern(date);
        }
        Integer newMissing = "0".equals(missing) ? null : Integer.parseInt(missing);
        Map<String,Object> param = Maps.newHashMap();
        param.put("province",province);
        param.put("type",type);
        param.put("period",period);
        param.put("date",date);
        param.put("missing",newMissing);
        final List<FileIndicationEntity> FileIndicationList = iFileIndicationPeriodConfigService.getFileIndication(type);
        for (FileIndicationEntity indicationEntity : FileIndicationList) {
            if (StringUtils.isNotEmpty(fileIndication) && indicationEntity.getFileIndicationId() != Integer.parseInt(fileIndication)) {
                continue;
            }
            String name = indicationEntity.getFileIndicationName();
            param.put("fileIndicationId",indicationEntity.getFileIndicationId());
            param.put("startIndex", (currentPage -1) * pageSize * 2);
            param.put("pageSize", pageSize * 2);
            List<Map<String, String>> fileMissingIntegrityCheckData = getFileMissingCheckData(param);
            Integer totalCount = getFileMissingCheckDataCount(param);
            int totalPage = totalCount / pageSize / 2;
            totalPage = totalCount % (pageSize * 2) == 0 ? totalPage : totalPage + 1;
            JSONObject indicationJson = new JSONObject();
            indicationJson.put("indication", JSONObject.fromObject(indicationEntity));
            indicationJson.put("totalCount", totalCount);
            indicationJson.put("totalPage", totalPage);
            indicationJson.put("data", fileMissingIntegrityCheckData);
            indicationJson.put("startIndex", (currentPage -1) * pageSize * 2);
            int rowCount = fileMissingIntegrityCheckData.size() % 2 == 0 ? fileMissingIntegrityCheckData.size() / 2 :
                    (fileMissingIntegrityCheckData.size() / 2 + 1);
            indicationJson.put("rowCount", rowCount);
            indicationJson.put("pageSize", pageSize);
            returnJSON.put(name, indicationJson);
        }
        return returnJSON;
    }

    @Override
    public List<Map<String, String>> getMissingList(String uploadDate, String type) {
        return fileMissingCheckDao.getMissingList(uploadDate, type);
    }

    @Override
    public List<Map<String, String>> getFileCheckResultForMail(Map<String, Object> param) {
        return fileMissingCheckDao.getFileCheckResultForMail(param);
    }
}
