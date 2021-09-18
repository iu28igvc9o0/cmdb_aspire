package com.aspire.mirror.inspection.server.biz.impl;

import com.aspire.mirror.common.entity.Page;
import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.util.PageUtil;
import com.aspire.mirror.inspection.api.dto.ReportItemExt;
import com.aspire.mirror.inspection.api.dto.ReportItemValue;
import com.aspire.mirror.inspection.api.dto.model.ReportDTO;
import com.aspire.mirror.inspection.api.dto.model.ReportItemDTO;
import com.aspire.mirror.inspection.api.dto.model.ReportTaskDTO;
import com.aspire.mirror.inspection.server.biz.ReportBiz;
import com.aspire.mirror.inspection.server.clientservice.payload.TriggersDetailResponse;
import com.aspire.mirror.inspection.server.controller.authcontext.RequestAuthContext;
import com.aspire.mirror.inspection.server.dao.ReportDao;
import com.aspire.mirror.inspection.server.dao.ReportItemDao;
import com.aspire.mirror.inspection.server.dao.TaskObjectDao;
import com.aspire.mirror.inspection.server.dao.po.Report;
import com.aspire.mirror.inspection.server.dao.po.ReportItem;
import com.aspire.mirror.inspection.server.dao.po.ReportResultStatistic;
import com.aspire.mirror.inspection.server.dao.po.ReportTask;
import com.aspire.mirror.inspection.server.dao.po.transform.ReportTransformer;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * inspection_report业务层实现类
 * <p>
 * 项目名称:  微服务运维平台
 * 包:       com.aspire.mirror.inspection.server.biz.impl
 * 类名称:    ReportBizImpl.java
 * 类描述:    inspection_report业务逻辑层实现类
 * 创建人:    ZhangSheng
 * 创建时间:  2018-07-27 13:48:08
 */
@Service
public class ReportBizImpl implements ReportBiz {
    private static final String CONTAIN_PREFIX = "::contains::";
    private static final String CRON_PREFIX = "::cron::";

    /**
     * 新增数据
     *
     * @param reportDTO inspection_reportDTO对象
     * @return int 新增数据条数
     */
    public int insert(final ReportDTO reportDTO) {
        if (null == reportDTO) {
            LOGGER.error("method[insert] param[reportDTO] is null");
            throw new RuntimeException("param[reportDTO] is null");
        }
        Report report = ReportTransformer.toPo(reportDTO);

        return reportDao.insert(report);
    }

    /**
     * 批量新增inspection_report数据
     *
     * @param listReportDTO inspection_reportDTO集合对象
     * @return int 新增数据条数
     */
    public int insertByBatch(final List<ReportDTO> listReportDTO) {
        if (CollectionUtils.isEmpty(listReportDTO)) {
            LOGGER.error("method[insertByBatch] param[listReportDTO] is null");
            throw new RuntimeException("param[listReportDTO] is null");
        }
        List<Report> listReport = ReportTransformer.toPo(listReportDTO);
        return reportDao.insertByBatch(listReport);
    }

    /**
     * 根据主键删除数据
     *
     * @param reportId 主键
     * @return int 删除数据条数
     */
    public int deleteByPrimaryKey(final String reportId) {
        if (StringUtils.isEmpty(reportId)) {
            LOGGER.error("method[eleteByPrimaryKey] param[reportId] is null");
            throw new RuntimeException("param[reportId] is null");
        }
        return reportDao.deleteByPrimaryKey(reportId);
    }

    /**
     * 根据主键数组批量删除数据
     *
     * @param reportIdArrays 主键数组
     * @return int 删除数据条数
     */
    public int deleteByPrimaryKeyArrays(final String[] reportIdArrays) {
        if (ArrayUtils.isEmpty(reportIdArrays)) {
            LOGGER.error("method[deleteByPrimaryKeyArrays] param[reportIdArrays] is null");
            throw new RuntimeException("param[reportIdArrays] is null");
        }
        return reportDao.deleteByPrimaryKeyArrays(reportIdArrays);
    }

    /**
     * 根据条件删除数据
     *
     * @param reportDTO inspection_reportDTO对象
     * @return int 删除数据条数
     */
    public int delete(final ReportDTO reportDTO) {
        if (null == reportDTO) {
            LOGGER.error("method[delete] param[reportDTO] is null");
            throw new RuntimeException("param[reportDTO] is null");
        }
        Report report = ReportTransformer.toPo(reportDTO);
        return reportDao.delete(report);
    }

    /**
     * 根据参数选择性更新数据
     *
     * @param reportDTO inspection_reportDTO对象
     * @return int 数据条数
     */
    public int updateByPrimaryKeySelective(final ReportDTO reportDTO) {
        if (null == reportDTO) {
            LOGGER.error("method[updateByPrimaryKey] param[reportDTO] is null");
            throw new RuntimeException("param[reportDTO] is null");
        }
        Report report = ReportTransformer.toPo(reportDTO);
        return reportDao.updateByPrimaryKeySelective(report);
    }

    /**
     * 根据主键更新数据
     *
     * @param reportDTO inspection_reportDTO对象
     * @return int 数据条数
     */
    public int updateByPrimaryKey(final ReportDTO reportDTO) {
        if (null == reportDTO) {
            LOGGER.error("method[updateByPrimaryKey] param[reportDTO] is null");
            throw new RuntimeException("param[reportDTO] is null");
        }
        if (StringUtils.isEmpty(reportDTO.getReportId())) {
            LOGGER.warn("method[updateByPrimaryKey] param[reportId] is null");
            throw new RuntimeException("param[reportId] is null");
        }
        Report report = ReportTransformer.toPo(reportDTO);
        return reportDao.updateByPrimaryKey(report);
    }


    /**
     * 根据主键数组查询
     *
     * @param reportIdArrays 主键数组
     * @return List<ReportDTO> 返回集合对象
     */
    public List<ReportDTO> selectByPrimaryKeyArrays(final String[] reportIdArrays) {
        if (ArrayUtils.isEmpty(reportIdArrays)) {
            LOGGER.warn("method[selectByPrimaryKeyArrays] param[reportIdArrays] is null");
            return Collections.emptyList();
        }
        List<Report> listReport = reportDao.selectByPrimaryKeyArrays(reportIdArrays);
        return ReportTransformer.fromPo(listReport);
    }

    /**
     * 根据dto实体查询列表
     *
     * @param reportDTO inspection_reportDTO对象
     * @return List<Report>  返回集合
     */
    public List<ReportDTO> select(final ReportDTO reportDTO) {
        if (null == reportDTO) {
            LOGGER.warn("select Object reportDTO is null");
            return Collections.emptyList();
        }
        Report report = ReportTransformer.toPo(reportDTO);
        List<Report> listReport = reportDao.select(report);
        return ReportTransformer.fromPo(listReport);
    }

    /**
     * 根据DTO实体查询条数
     *
     * @param reportDTO inspection_reportDTO对象
     * @return int 数据条数
     */
   /* public int selectCount(final ReportDTO reportDTO){
        if(null == reportDTO){
            LOGGER.warn("selectCount Object reportDTO is null");
        }
        Report report = ReportTransformer.toPo(reportDTO);
        return reportDao.selectCount(report);
    }*/
//--------------------------------------------------以下部分手写---------------------------------------------------------->

    /**
     * 根据主键查询
     *
     * @param reportId 主键
     * @return ReportDTO 返回对象
     */
    public ReportDTO selectByPrimaryKey(final String reportId) {
        if (StringUtils.isEmpty(reportId)) {
            LOGGER.warn("method[selectByPrimaryKey] param[reportId] is null");
            return new ReportDTO();
        }
        Report report = reportDao.selectByPrimaryKey(reportId);

        return ReportTransformer.fromPo(report);
    }

    /**
     * 根据分页查询对象查询分页信息
     */
  /*  @Override
	public List<ReportDTO> pageList(final PageRequest pageRequest) {
    	Page page =PageUtil.convert(pageRequest);
    	String queryName="com.aspire.mirror.inspection.server.dao.ReportDao.pageList";
    	Integer pageNo =page.getPageNo();
		Integer pageSize =page.getPageSize();
		Integer startIndex = (pageNo-1)*pageSize;
		List<Report> reports=null;
		try {
			reports=sessionTemplate.selectList(queryName,page,new RowBounds(startIndex, pageSize));
		}catch (Exception e) {
			LOGGER.warn("com.aspire.mirror.inspection.server.biz.impl.ReportBizImpl--->pageList Object reportDTO is
			null");
		}
    	List<ReportDTO> reportDTOs =ReportTransformer.fromPo(reports);
		return reportDTOs;
	}*/

    /**
     * 根据巡检报告查询条件查询总数
     */
    @Override
    public int pageCount(final PageRequest pageRequest) {
        Page page = PageUtil.convert(pageRequest);
        int count = 0;
        try {
            count = reportDao.selectCount(page);
        } catch (Exception e) {
            LOGGER.warn("com.aspire.mirror.inspection.server.biz.impl.ReportBizImpl--->pageCount Object reportDTO is " +
                    "null");
        }
        return count;
    }

    /**
     * key中过滤掉特殊字符. <br/>
     * <p>
     * 作者： pengguihua
     *
     * @param itemKey
     * @param expression
     * @return
     */
    private Pair<String, String> filterSpecialChars(String itemKey, String expression) {
        String originalKey = itemKey;
        String filterKey = "k_" + itemKey.replaceAll("\\W", "");
        String filterExpress = expression.replace(originalKey, filterKey);
        return Pair.of(filterKey, filterExpress);
    }

    @Override
    @Transactional
    public void regenerate(String reportId, Map<String, String> triggerMap) {
        List<ReportItem> reportItemList = reportItemDao.selectByReportId(reportId);
        if (!CollectionUtils.isEmpty(reportItemList)) {
            for (ReportItem reportItem : reportItemList) {
                ReportItemExt reportItemExt = new ReportItemExt();
                reportItemExt.setReportItemId(reportItem.getReportItemId());
                reportItemExt.setExpression(triggerMap.get(reportItem.getItemId()));
                reportItemDao.updateReportItemExtByReportItemId(reportItemExt);
                if (triggerMap.get(reportItem.getItemId()) != null && !CollectionUtils.isEmpty(reportItem.getReportItemValueList())) {
                    // 字符串包含判断
                    int containIdx = triggerMap.get(reportItem.getItemId()).indexOf(CONTAIN_PREFIX);
                    int cronIndex = triggerMap.get(reportItem.getItemId()).indexOf(CRON_PREFIX);
                    for (ReportItemValue reportItemValue : reportItem.getReportItemValueList()) {
                        if (StringUtils.isEmpty(reportItemValue.getResultValue())) {
                            //无结果
                            reportItemValue.setResultStatus("2");
                        } else {
                            reportItemValue.setResultStatus(ReportItemDTO.STATUS_NORMAL);
                            if (containIdx >= 0) {
                                String containStr = triggerMap.get(reportItem.getItemId()).substring(containIdx + CONTAIN_PREFIX.length());
                                //值包含逗号，或处理
                                String[] containArray = containStr.split(",");
                                for (String containItem : containArray) {
                                    if (reportItemValue.getResultValue().contains(containItem)) {
                                        reportItemValue.setResultStatus(ReportItemDTO.STATUS_EXCEPTION);
                                        break;
                                    }
                                }
//                                if (reportItemValue.getResultValue().contains(containStr)) {
//                                    reportItemValue.setResultStatus("1");
//                                } else {
//                                    reportItemValue.setResultStatus("0");
//                                }
                            } else if (cronIndex >= 0) {
                                reportItemValue.setResultStatus(ReportItemDTO.STATUS_NORMAL);
                                String cronStr = triggerMap.get(reportItem.getItemId()).substring(cronIndex + CRON_PREFIX.length());
                                Pattern p = Pattern.compile(cronStr);
                                Matcher m = p.matcher(reportItemValue.getResultValue());
                                while (m.find()) {
                                    reportItemValue.setResultStatus(ReportItemDTO.STATUS_EXCEPTION);
                                }
                            } else {
                                //值包含逗号  或处理
                                Pattern pattern = Pattern.compile("-?[0-9]+\\.?[0-9]*");
                                String sourceValue = reportItemValue.getResultValue().trim();
                                Boolean isNum = true;
                                if (!pattern.matcher(sourceValue).matches()) {
                                    // 非数字值，做字符串比较
                                    sourceValue = String.format("'%s'", sourceValue);
                                    isNum = false;
                                }

                                String[] expressionValueArray = TriggersDetailResponse.getExpressionValue(triggerMap.get(reportItem.getItemId())).split(",");
                                String match = TriggersDetailResponse.getMatch(triggerMap.get(reportItem.getItemId()));
                                if (match.equals("!=")) {
                                    Boolean isMatch = true;
                                    for (String expressionVlaue : expressionValueArray) {
                                        if (!isNum) {
                                            expressionVlaue = String.format("'%s'", expressionVlaue);
                                        }
                                        Expression compiledExp = AviatorEvaluator.compile(sourceValue + match + expressionVlaue);
                                        if (!Boolean.class.cast(compiledExp.execute())) {
                                            isMatch = false;
                                            break;
                                        }
                                    }
                                    if (isMatch) {
                                        reportItemValue.setResultStatus(ReportItemDTO.STATUS_EXCEPTION);
                                    }
                                } else {
                                    for (String expressionVlaue : expressionValueArray) {
                                        if (!isNum) {
                                            expressionVlaue = String.format("'%s'", expressionVlaue);
                                        }
                                        Expression compiledExp = AviatorEvaluator.compile(sourceValue + match + expressionVlaue);
                                        boolean isMatch = Boolean.class.cast(compiledExp.execute());
                                        if (isMatch) {
                                            reportItemValue.setResultStatus(ReportItemDTO.STATUS_EXCEPTION);
                                            break;
                                        }
                                    }
                                }
//                                Expression compiledExp = AviatorEvaluator.compile(reportItemValue.getResultValue().trim() + triggerMap.get(reportItem.getItemId()));
//                                boolean isMatch = Boolean.class.cast(compiledExp.execute());

//                                if (isMatch) {
//                                    reportItemValue.setResultStatus("1");
//                                } else {
//                                    reportItemValue.setResultStatus("0");
//                                }
                            }
                        }
                        reportItemDao.updateReportItemValueStatus(reportItemValue);
                    }
                    reportItemDao.updateStatusByUniqueKeys(reportItem);
                }
            }
            Report report = new Report();
            report.setReportId(reportId);
            ReportResultStatistic statisticValue = reportItemDao.selectNumStatistic(reportId);
            String result = "扫描机器{0}台，扫描项共计{1}，结果项共计{2} 异常项<span style=\"color: red;font-weight: 800;" +
                    "\">{3}</span>，正常项<span " +
                    "style=\"color: green;font-weight: 800;\">{4}</span>，{5}项未取值,{6}项人工处理";
            result = MessageFormat.format(result, statisticValue.getDeviceNum(), statisticValue.getItemNum(), statisticValue.getResultNum(),
                    statisticValue.getExceptionNum(), statisticValue.getNormalNum(),
                    statisticValue.getNoResultNum(), statisticValue.getArtificialJudgmentNum());
            report.setResult(result);
            reportDao.updateByPrimaryKeySelective(report);
        }
    }

    public static void main(String[] args) {
//        Expression compiledExp = AviatorEvaluator.compile("关闭==开启");
//        String aa = "027";
//        Pattern pattern = Pattern.compile("-?[0-9]+\\.?[0-9]*");
//        String sourceValue = "027";
//        Boolean isNum = true;
//        if (!pattern.matcher(sourceValue).matches()) {
//            // 非数字值，做字符串比较
//            sourceValue = String.format("'%s'", sourceValue);
//            isNum = false;
//        }
//
//        String[] expressionValueArray = TriggersDetailResponse.getExpressionValue("!=027,077").split(",");
//        for (String expressionVlaue : expressionValueArray) {
//            if (!isNum) {
//                expressionVlaue = String.format("'%s'", expressionVlaue);
//            }
//            String match = TriggersDetailResponse.getMatch("!=027,077");
        //027  077 正常
        Expression compiledExp = AviatorEvaluator.compile("027 != 027");
        boolean isMatch = Boolean.class.cast(compiledExp.execute());
        if (isMatch) {
            System.out.println("异常");
//                break;
        }
//        }
//
//        boolean isMatch = Boolean.class.cast(AviatorEvaluator.execute( String.format("'%s'", aa)+ "==" + String.format("'%s'", bb)));
//        System.out.println(isMatch);
    }

    @Override
    public int updateReportFilePath(String reportId, String filePath) {
        return reportDao.updateReportFilePath(reportId, filePath);
    }

    /**
     * 根据任务id查询巡检报告
     */
    @Override
    public List<ReportTaskDTO> selectByPage(final PageRequest pageRequest) {
        List<ReportTaskDTO> reprotDTOList = null;
        Page page = PageUtil.convert(pageRequest);
        Map<String, List<String>> resFilterConfig = RequestAuthContext.currentRequestAuthContext().getUser().getResFilterConfig();
        page.getParams().put("resFilterMap", resFilterConfig);
        List<ReportTask> reportList = null;
        String queryName = "com.aspire.mirror.inspection.server.dao.ReportDao.selectByPage";
        Integer pageNo = page.getPageNo();
        Integer pageSize = page.getPageSize();
        Integer startIndex = (pageNo - 1) * pageSize;
        try {
            reportList = sessionTemplate.selectList(queryName, page, new RowBounds(startIndex, pageSize));
            reprotDTOList = ReportTransformer.fromPoTask(reportList);
        } catch (Exception e) {
            LOGGER.warn("selectByTaskId Object reportDTO is null");
        }
        return reprotDTOList;
    }

    /**
     * 根据任务id统计
     */
    @Override
    public int selectCount(final PageRequest pageRequest) {
        int count = 0;
        Map<String, List<String>> resFilterConfig = RequestAuthContext.currentRequestAuthContext().getUser().getResFilterConfig();
        Page page = PageUtil.convert(pageRequest);
        page.getParams().put("resFilterMap", resFilterConfig);
        try {
            count = reportDao.selectCount(page);
        } catch (Exception e) {
            LOGGER.warn("selectCount Object reportDTO is null");
        }
        return count;
    }

    /**
     * slf4j
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportBizImpl.class);

    /**
     * 数据访问层接口
     */
    @Autowired
    private ReportDao reportDao;

    @Autowired
    private ReportItemDao reportItemDao;
    @Autowired
    private SqlSessionTemplate sessionTemplate;

    @Autowired
    private TaskObjectDao taskObjectDao;
} 
