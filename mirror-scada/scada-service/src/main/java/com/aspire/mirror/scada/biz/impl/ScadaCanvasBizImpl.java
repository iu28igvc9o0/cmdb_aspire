package com.aspire.mirror.scada.biz.impl;

import com.aspire.mirror.common.entity.Page;
import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.common.util.PageUtil;
import com.aspire.mirror.scada.api.dto.model.ScadaCanvasDTO;
import com.aspire.mirror.scada.biz.ScadaCanvasBiz;
import com.aspire.mirror.scada.dao.ScadaCanvasDao;
import com.aspire.mirror.scada.dao.po.ScadaCanvas;
import com.aspire.mirror.scada.dao.po.transform.ScadaCanvasTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 业务层实现类
 * <p>
 * 项目名称:  微服务运维平台
 * 包:     com.aspire.mirror.scada.biz.impl
 * 类名称:     CanvasBizImpl
 * 类描述:     业务层实现类
 * 创建时间:     2019-06-11 11:32:23
 *
 * @author JinSu
 */
@Service("scadaCanvasBiz")
@Transactional
public class ScadaCanvasBizImpl implements ScadaCanvasBiz {
    /**
     * 系统日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ScadaCanvasBizImpl.class);

    /**
     * dao
     */
    @Autowired
    private ScadaCanvasDao scadaCanvasDao;

    @Autowired
    private ExportScadaImage exportScadaImage;

    /**
     * 新增视图
     *
     * @param scadaCanvasDTO 动作DTO对象
     * @return String 数据ID
     */
    @Override
    public String insert(ScadaCanvasDTO scadaCanvasDTO) {
        if (null == scadaCanvasDTO) {
            LOGGER.error("method[insert] param[canvasDTO] is null");
            throw new RuntimeException("param[canvasDTO] is null");
        }

        ScadaCanvas scadaCanvas = ScadaCanvasTransformer.toPo(scadaCanvasDTO);
        if (scadaCanvas.getPageType() == null) {
            scadaCanvas.setPageType(0);
        }
        String scadaCanvasId = UUID.randomUUID().toString();
        scadaCanvas.setId(scadaCanvasId);
        Date now = new Date();
        scadaCanvas.setCreateTime(now);
        scadaCanvas.setUpdateTime(now);
        scadaCanvasDao.insert(scadaCanvas);
        scadaCanvasDTO.setId(scadaCanvasId);
        exportScadaImage.createScadaImage(scadaCanvasDTO);
        return scadaCanvasId;
    }

    /**
     * 根据主键删除视图
     *
     * @param scadaCanvasId 主键
     * @return int 删除数据条数
     */
    @Override
    public void deleteByPrimaryKey(final String scadaCanvasId) {
        if (StringUtils.isEmpty(scadaCanvasId)) {
            LOGGER.error("method[eleteByPrimaryKey] param[scadaCanvasId] is null");
            throw new RuntimeException("param[scadaCanvasId] is null");
        }
        scadaCanvasDao.deleteByPrimaryKey(scadaCanvasId);
    }

    /**
     * 根据主键更新视图
     *
     * @param scadaCanvasDTO 动作DTO对象
     * @return int 数据条数
     */
    @Override
    public int updateByPrimaryKey(final ScadaCanvasDTO scadaCanvasDTO) {
        ScadaCanvas scadaCanvas = ScadaCanvasTransformer.toPo(scadaCanvasDTO);
        scadaCanvas.setUpdateTime(new Date());
        if (!StringUtils.isEmpty(scadaCanvasDTO.getContent())) {
            exportScadaImage.createScadaImage(scadaCanvasDTO);
        }
        return scadaCanvasDao.updateByPrimaryKey(scadaCanvas);
    }

    /**
     * 根据主键查询视图
     *
     * @param scadaCanvasId 主键
     * @return CanvasDTO 返回对象
     */
    @Override
    public ScadaCanvasDTO selectByPrimaryKey(final String scadaCanvasId) {
        ScadaCanvas scadaCanvas = scadaCanvasDao.selectByPrimaryKey(scadaCanvasId); //第1个使用地方
        return ScadaCanvasTransformer.fromPo(scadaCanvas);
    }

//    @Override
//    public ResMap getScadaCanvasInfoByPrecinctId(String precinctId, Integer pictureType) {
//        ScadaCanvas scadaCanvas = scadaCanvasDao.getScadaCanvasInfoByPrecinctId(precinctId, pictureType); //第3个使用地方
//        if (null == scadaCanvas) {
//            return ResMap.success();
//        }
//        return ResMap.success(ScadaCanvasTransformer.fromPo(scadaCanvas));
//    }

    @Override
    public List<ScadaCanvasDTO> findScadaCanvasList(ScadaCanvasDTO scadaCanvasDTO) {
        ScadaCanvas scadaCanvas = ScadaCanvasTransformer.toPo(scadaCanvasDTO);
        List<ScadaCanvas> scadaCanvasList = scadaCanvasDao.findScadaCanvasList(scadaCanvas); //第4个使用地方
        return ScadaCanvasTransformer.fromPo(scadaCanvasList);
    }


//    //特殊任务，将文件名和节点id关联
//    @Override
//    public String batchFindPrecinctId() {
//        //遍历指定目录下的所有文件名
//        File dirPath = new File("d:/667788/");
//        String[] fileList = dirPath.list();
//        System.out.println("===============文件总数：" + fileList.length + "==================");
//        for (String fileName : fileList) {
//            //System.out.println("====================="+fileName+"====================");
//            String precinctId = "";
//            String precinctName = "";
////            List<Map<String, Object>> dataList = scadaCanvasDao.findPrecinctIdByPrecinctName(fileName);
////
////            //有可能会匹配出多个结果，所以要遍历所有map，取出key最长的那个，即是最匹配贴切的那个
////            //把查询结果处理成一个map
////            if (dataList != null) {
////                HashMap<String, String> dataMap = new HashMap<>();
////                for (Map m : dataList) {
////                    //System.out.println("key: "+"precinct_name"+"******"+"value: "+m.get("precinct_name"));
////                    //System.out.println("key: "+"precinct_id"+"******"+"value: "+m.get("precinct_id"));
////                    precinctId = (String) m.get("precinct_id");
////                    precinctName = (String) m.get("precinct_name");
////                }
////            }
//
//            System.out.println(fileName + "=" + precinctId + "=" + precinctName);
//        }
//
//
//        //一次性读取文件内容
//        //参考 https://www.cnblogs.com/qianjinyan/p/10395200.html
//
//        return null;
//    }


    //特殊任务，视图批量插入数据库
//    @Override
//    public String batchSaveScadaCanvas() throws Exception {
//        File file = new File("c:/Users/zhujiahao/Desktop/卓望工作任务/动环相关工作/组态模块/批量导入视图/已处理的视图文件和节点id对应.txt");
//        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
//        String s = null;
//        while ((s = bufferedReader.readLine()) != null) {
//            String[] array = s.split("=");
//
//            //获取试图名称
//            String scadaName = array[0].replace(".xml", "").replace("附件1：", "");
//
//            //获取节点id
//            String precinctId = "";
//            String precinctName = "";
//            if (array.length >= 2) {
//                precinctId = array[1];
//                precinctName = array[2];
//            }
//
//            System.out.println("文件名：" + scadaName + "******节点id：" + precinctId + "******节点名称：" + precinctName);
//        }
//
//
//        return null;
//    }

    @Override
    public ScadaCanvasDTO findScadaCanvasByName(String name) {
        ScadaCanvas scadaCanvas = scadaCanvasDao.findScadaCanvasByName(name);
        if (scadaCanvas != null) {
            ScadaCanvasDTO scadaCanvasDTO = new ScadaCanvasDTO();
            BeanUtils.copyProperties(scadaCanvas, scadaCanvasDTO);
            return scadaCanvasDTO;
        }
        return null;
    }

    @Override
    public PageResponse<ScadaCanvasDTO> pageList(PageRequest pageRequest) {
        Page page = PageUtil.convert(pageRequest);
        int count = scadaCanvasDao.pageListCount(page);
        PageResponse<ScadaCanvasDTO> pageResponse = new PageResponse<>();
        pageResponse.setCount(count);
        if (count > 0) {
            List<ScadaCanvas> canvasList = scadaCanvasDao.pageList(page);
            List<ScadaCanvasDTO> listDTO = ScadaCanvasTransformer.fromPo(canvasList);
            pageResponse.setResult(listDTO);
        }
        return pageResponse;
    }
}
