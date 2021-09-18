package com.aspire.mirror.theme.server.biz.impl;

import com.alibaba.fastjson.JSON;
import com.aspire.mirror.common.auth.GeneralAuthConstraintsAware;
import com.aspire.mirror.common.constant.Constant;
import com.aspire.mirror.common.entity.Page;
import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.common.entity.Result;
import com.aspire.mirror.common.util.DateUtil;
import com.aspire.mirror.common.util.PageUtil;
import com.aspire.mirror.theme.api.dto.model.BizThemeDTO;
import com.aspire.mirror.theme.api.dto.model.BizThemeDimDTO;
import com.aspire.mirror.theme.api.dto.model.BizThemeDimData;
import com.aspire.mirror.theme.api.dto.model.ThemeDataDTO;
import com.aspire.mirror.theme.server.biz.BizThemeBiz;
import com.aspire.mirror.theme.server.biz.handler.ElasticsearchHandler;
import com.aspire.mirror.theme.server.biz.handler.MergeTask;
import com.aspire.mirror.theme.server.biz.helper.Reactor;
import com.aspire.mirror.theme.server.clientservice.elasticsearch.ThemeDataServiceClient;
import com.aspire.mirror.theme.server.dao.BizThemeDao;
import com.aspire.mirror.theme.server.dao.BizThemeDimDao;
import com.aspire.mirror.theme.server.dao.ThemeKeyDao;
import com.aspire.mirror.theme.server.dao.po.BizTheme;
import com.aspire.mirror.theme.server.dao.po.BizThemeDim;
import com.aspire.mirror.theme.server.dao.po.ThemeKey;
import com.aspire.mirror.theme.server.dao.po.transform.BizThemeTransformer;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Future;

/**
 * 业务主题业务层实现
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.theme.server.biz.impl
 * 类名称:    BizThemeBizImpl.java
 * 类描述:    业务主题业务层实现
 * 创建人:    JinSu
 * 创建时间:  2018/10/23 19:37
 * 版本:      v1.0
 */
@Service
public class BizThemeBizImpl implements BizThemeBiz {
    public static final String UP_STATUS_SUCCESS = "0";
    public static final String DELETE_FLAG_DELETED = "1";
    public static final String DELETE_FLAG_EXIST = "0";
    @Value("${index_name:mirror-theme-*}")
    private String indexName;

//    @Autowired
//    private ElasticSearchHelper elasticSearchClient;

    @Autowired
    private ThemeKeyDao themeKeyDao;

    @Autowired
    private ThemeDataServiceClient themeDataService;

    /**
     * 创建主题
     *
     * @param bizThemeDTO 主题对象
     * @return String 主题Id
     */
    @Override
    @Transactional
    public String insert(BizThemeDTO bizThemeDTO) {
        if (null == bizThemeDTO) {
            LOGGER.error("method[insert] param[bizThemeDTO] is null");
            throw new RuntimeException("param[bizThemeDTO] is null");
        }
        String themeId = UUID.randomUUID().toString();
        BizTheme bizTheme = BizThemeTransformer.toPo(bizThemeDTO);
        bizTheme.setThemeId(themeId);
        //设置上传状态为成功
        bizTheme.setUpStatus(UP_STATUS_SUCCESS);
        bizTheme.setDeleteFlag(DELETE_FLAG_EXIST);
        Date now = new Date();
        bizTheme.setCreateTime(now);
        bizTheme.setUpdateTime(now);
        bizTheme.setIndexName("mirror-theme-" + themeId);
//        if (StringUtils.isEmpty(bizTheme.getIndexName())) {
//            bizTheme.setIndexName(indexName);
//        }
        if (StringUtils.isEmpty(bizTheme.getThemeCode())) {
            String themeCode;
            if (StringUtils.isEmpty(bizTheme.getObjectId())) {
                themeCode = MessageFormat.format("02{0}", themeId.replace("-",""));
            } else {
                themeCode = MessageFormat.format("{0}01{1}", bizTheme.getObjectId(), themeId.replace("-",""));
            }
            bizTheme.setThemeCode(themeCode);
        } else {
            BizTheme result = bizThemeDao.selectByThemeCode(bizTheme.getThemeCode());
            if (result != null) {
                LOGGER.error("不允许存在重复主题编码！");
                throw new RuntimeException("不允许存在重复主题编码！");
            }
        }
        List<BizThemeDimDTO> dimDTOList = bizThemeDTO.getDimList();
        //插入维度数据
        String logReg = insertDimList(dimDTOList, themeId);
//        bizTheme.setDimIds(dimIds);
        bizTheme.setLogReg(logReg);
        bizThemeDao.insert(bizTheme);
        //插入主题key
        if (!StringUtils.isEmpty(bizThemeDTO.getThemeKey())) {
            ThemeKey themeKey = new ThemeKey();
            themeKey.setThemeId(themeId);
            themeKey.setDimCode(bizThemeDTO.getThemeKey());
            themeKeyDao.insert(themeKey);
        }
        return themeId;
    }

    /**
     * 修改主题
     *
     * @param bizThemeDTO 修改主题
     */
    @Override
    @Transactional
    public void updateByPrimaryKey(BizThemeDTO bizThemeDTO) {
        BizTheme bizTheme = BizThemeTransformer.toPo(bizThemeDTO);
        List<BizThemeDimDTO> dimDTOList = bizThemeDTO.getDimList();

        // 维度列表传null 则根据主题ID删除主题维度数据
        if (dimDTOList != null) {
            bizThemeDimDao.deleteByThemeId(bizThemeDTO.getThemeId());
        }
        //插入维度数据
        String logReg = insertDimList(dimDTOList, bizThemeDTO.getThemeId());
//            bizTheme.setDimIds(dimIds);
        bizTheme.setLogReg(logReg);
        if (StringUtils.isEmpty(bizTheme.getThemeCode())) {
            String themeCode = MessageFormat.format("{0}01{1}", bizTheme.getObjectId(), bizTheme.getThemeId());
            bizTheme.setThemeCode(themeCode);
        }
        bizTheme.setUpdateTime(new Date());
        bizThemeDao.updateByPrimaryKey(bizTheme);
        //插入主题key
        themeKeyDao.deleteByThemeId(bizThemeDTO.getThemeId());
        if (!StringUtils.isEmpty(bizThemeDTO.getThemeKey())) {
            ThemeKey themeKey = new ThemeKey();
            themeKey.setThemeId(bizThemeDTO.getThemeId());
            themeKey.setDimCode(bizThemeDTO.getThemeKey());
            themeKeyDao.insert(themeKey);
        }
    }

    private String insertDimList(List<BizThemeDimDTO> dimDTOList, String themeId) {
//        List<Integer> dimIds = Lists.newArrayList();
        StringBuilder logReg = new StringBuilder();
        if (!CollectionUtils.isEmpty(dimDTOList)) {
            List<BizThemeDim> bizThemeDimList = Lists.newArrayList();
            for (BizThemeDimDTO bizThemeDimDTO : dimDTOList) {
                //设置日志正则表达式
//                logReg.append(bizThemeDimDTO.getDimReg());
                if (bizThemeDimDTO.getMatchFlag().equals("1")) {
                    logReg.append("%{").append(bizThemeDimDTO.getDimReg()).append(":").append(bizThemeDimDTO
                            .getDimCode()
                    ).append("}");
                } else {
                    //非字段直接拼装
                    String dimReg = bizThemeDimDTO.getDimReg();
                    for (String UnescapedReg : Constant.ESCAPE_DIM_REG_ARRAY) {

                        if (UnescapedReg.equals(dimReg)) {
//                        bizThemeDim.setDimReg("\\" + bizThemeDim.getDimReg());
                            dimReg = "\\" + dimReg;
                            break;
                        }
                    }
                    logReg.append(dimReg);
                }
                BizThemeDim bizThemeDim = new BizThemeDim();
                BeanUtils.copyProperties(bizThemeDimDTO, bizThemeDim);
                //维度表设置主题ID
                bizThemeDim.setThemeId(themeId);
                bizThemeDimList.add(bizThemeDim);
            }
            bizThemeDimDao.insertBatch(bizThemeDimList);
            return logReg.toString();
        }
        return "";
    }

    /**
     * 删除主题
     *
     * @param themeId 主题ID
     */
    @Override
    public void deleteByPrimaryKey(String themeId) {
        BizTheme bizTheme = new BizTheme();
        bizTheme.setThemeId(themeId);
        bizTheme.setUpdateTime(new Date());
        bizTheme.setDeleteFlag(DELETE_FLAG_DELETED);
        bizThemeDao.updateByPrimaryKey(bizTheme);
    }

    /**
     * 分页查询
     *
     * @param pageRequest 分页查询对象
     * @return PageResponse<BizThemeDTO> 分页查询返回对象
     */
    @Override
    public PageResponse<BizThemeDTO> pageList(PageRequest pageRequest) {

        Page page = PageUtil.convert(pageRequest);
        int count = bizThemeDao.pageListCount(page);
        PageResponse<BizThemeDTO> pageResponse = new PageResponse<BizThemeDTO>();
        pageResponse.setCount(count);
        if (count > 0) {
            List<BizTheme> listBizTheme = bizThemeDao.pageList(page);
            List<BizThemeDTO> listDTO = BizThemeTransformer.fromPo(listBizTheme);
            pageResponse.setResult(listDTO);
        }
        return pageResponse;
    }

    /**
     * 查询主题列表
     *
     * @param param 主题查询参数
     * @return ist<BizThemeDTO> 主题列表
     */
    @Override
    public List<BizThemeDTO> select(BizThemeDTO param) {
        BizTheme bizTheme = BizThemeTransformer.toPo(param);
        List<BizTheme> bizThemeList = bizThemeDao.select(bizTheme);
        return BizThemeTransformer.fromPo(bizThemeList);
    }

    /**
     * 单条数据查询
     *
     * @param themeId 主题ID
     * @return BizThemeDTO 主题数据
     */
    @Override
    public BizThemeDTO selectByPrimaryKey(String themeId, GeneralAuthConstraintsAware authParam) {
        BizTheme bizTheme = bizThemeDao.selectByPrimaryKey(themeId, authParam);
        if (bizTheme != null) {
            BizThemeDTO bizThemeDTO = BizThemeTransformer.fromPo(bizTheme);
//            String[] dimIds = bizTheme.getDimIds().split(":");
            List<BizThemeDim> listDim = bizThemeDimDao.selectByThemeId(themeId);
            List<BizThemeDimDTO> dimDTOList = Lists.newArrayList();
            for (BizThemeDim dim : listDim) {
                BizThemeDimDTO bizThemeDimDTO = new BizThemeDimDTO();
                BeanUtils.copyProperties(dim, bizThemeDimDTO);
                dimDTOList.add(bizThemeDimDTO);
            }
            bizThemeDTO.setDimList(dimDTOList);
            ThemeKey themeKey = themeKeyDao.selectByThemeId(themeId);
            if (themeKey != null && themeKey.getDimCode() != null) {
                bizThemeDTO.setThemeKey(themeKey.getDimCode());
            }
            return bizThemeDTO;
        }
        return null;
    }

    /**
     * 接收主题数据
     *
     * @param themeDataDTO 主题数据对象
     * @return Result 接收结果
     */
    @Override
    public Result createThemeData(ThemeDataDTO themeDataDTO) {
        themeDataDTO.setMessage(JSON.toJSONString(themeDataDTO));
        themeDataDTO.setReceiveTime(DateUtil.format(new Date(), DateUtil.DATE_TIME_FORMAT));
        //多线程处理
        Future<String> f = Reactor.getInstance().submit(new MergeTask(themeDataDTO, elasticsearchHandler,
                bizThemeDao, bizThemeDimDao));
        try {
            String res = f.get();
            if (!StringUtils.isEmpty(res)) {
                return new Result("2", res);
            }
            return Result.success();
        } catch (Exception e) {
            LOGGER.error("主题数据解析异常！", e);
            return new Result("500", "主题数据解析异常！");
        }
    }

    /**
     * 获取主题数据
     *
     * @param themeId 主题id
     * @param host 主机IP
     * @param bizCode 业务编码
     * @param themeCode 主题编码
     * @return
     */
    @Override
    public Map<String, Object> getThemeData(String themeId, String host, String bizCode, String themeCode) {
        BizTheme bizTheme = bizThemeDao.selectByPrimaryKey(themeId, new GeneralAuthConstraintsAware());
        String indexName;
        if (bizTheme != null && !StringUtils.isEmpty(bizTheme.getIndexName())) {
            indexName = bizTheme.getIndexName();
        } else {
            return Maps.newHashMap();
        }
        //瓶装查询参数
        Date startDate;
        final Date curDate = new Date();
        if (!StringUtils.isEmpty(bizTheme.getLastUpTime())) {
            startDate = bizTheme.getLastUpTime();
        } else {
            startDate = curDate;
        }
        long startTime = startDate.getTime() - 100 * 1000;
        final long endTime = curDate.getTime() + 100 * 1000;
        Map<String, Object> resMap = themeDataService.getThemeData(indexName, startTime, endTime, host, bizCode, themeCode);
        if (resMap != null) {
            resMap.put("dim_list", dimIdsToDimNamesByResMap(resMap, bizTheme));
        }
        return resMap;
    }

    @Override
    @Deprecated
    public Map<String, Object> thematicHistoryInfo(String indexName, String themeCode, Long startTime, Long endTime) {
        final Map<String, Object> res = new HashMap<>();
        List<Object[]> itemValuesList = themeDataService.themeHistoryInfo(indexName, themeCode, startTime, endTime);
        res.put("lstDetail", itemValuesList);
        return res;
    }

    @Override
    public Map<String, Object> getThemeDataHis(String themeId) {
        BizTheme bizTheme = bizThemeDao.selectByPrimaryKey(themeId, new GeneralAuthConstraintsAware());
//        String indexName = bizTheme.getIndexName();
        Map<String, Object> resMap = themeDataService.getThemeDataHis(bizTheme);
        if (resMap != null) {
            resMap.put("dim_list", dimIdsToDimNamesByResMap(resMap, bizTheme));
        }
        return resMap;
    }

    // 根据elSearch返回的map结果转换对应的维度中文信息
    public List<BizThemeDimData> dimIdsToDimNamesByResMap(Map<String, Object> resMap, BizTheme bizTheme) {
        final List<BizThemeDimData> dimNameAndValueList = new ArrayList<BizThemeDimData>();
        List<BizThemeDim> bizThemeDimList = bizThemeDimDao.selectByThemeId(bizTheme.getThemeId());
        for (BizThemeDim bizThemeDim : bizThemeDimList) {
            if (resMap.get(bizThemeDim.getDimCode()) != null) {
                final BizThemeDimData jkzbDimTmp = new BizThemeDimData();
                jkzbDimTmp.setDimName(bizThemeDim.getDimName());
                jkzbDimTmp.setDimValue((String) resMap.get(bizThemeDim.getDimCode()));
                try {
                    final String dimType = bizThemeDim.getDimType();
                    if (!StringUtils.isEmpty(dimType)
                            && "3".equalsIgnoreCase(dimType) && !bizThemeDim.getDimCode().equals("timestamp")) {
                        String dateDimValue = (String) resMap.get(bizThemeDim.getDimCode());
                        final Date transfTimestamp = DateUtil.getDate(dateDimValue,
                                "yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                        dateDimValue = DateUtil.format(transfTimestamp, "yyyyMMddHHmmss");

                        jkzbDimTmp.setDimValue(dateDimValue);
                    }
                } catch (final Exception e) {
                    LOGGER.error("dimIdsToDimNamesByResMap()--> type conversion error:", e);
                }
                dimNameAndValueList.add(jkzbDimTmp);
            }

        }
        return dimNameAndValueList;
    }

    Logger LOGGER = LoggerFactory.getLogger(BizThemeBizImpl.class);

    @Autowired
    private BizThemeDao bizThemeDao;

    @Autowired
    private ElasticsearchHandler elasticsearchHandler;

    @Autowired
    private BizThemeDimDao bizThemeDimDao;

}
