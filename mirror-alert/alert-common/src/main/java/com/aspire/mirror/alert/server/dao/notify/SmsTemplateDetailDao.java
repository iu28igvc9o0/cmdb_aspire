package com.aspire.mirror.alert.server.dao.notify;


import com.aspire.mirror.alert.server.dao.notify.po.SmsTemplateDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author menglinjie
 */
@Mapper
public interface SmsTemplateDetailDao {

    int deleteByPrimaryKey(String id);

    int insert(SmsTemplateDetail record);

    SmsTemplateDetail selectByPrimaryKey(String id);

    List<SmsTemplateDetail> selectAll();

    int updateByPrimaryKey(SmsTemplateDetail record);

    void deleteByTemplateId(String templateId);

    Integer countFindDetailListByCondition(@Param("templateId") String templateId, @Param("content")String content);

    List<SmsTemplateDetail> findDetailListByCondition(@Param("templateId")String templateId,
                                                    @Param("content")String content,
                                                    @Param("startNo") Integer startNo,
                                                    @Param("pageSize") Integer pageSize);

	void editTemplateContent(SmsTemplateDetail templateDetail);

	void deleteTemplateContent(SmsTemplateDetail templateDetail);
}