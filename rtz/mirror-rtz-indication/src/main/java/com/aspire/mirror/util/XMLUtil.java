package com.aspire.mirror.util;

import com.aspire.common.EnvConfigProperties;
import com.aspire.common.HttpUtil;
import com.aspire.mirror.entity.*;
import com.aspire.mirror.service.IIndicationLimitService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Slf4j
@Data
@Component
public class XMLUtil {

    private final static String THEME_MAPPER_XML_NAME = "theme_mapper.xml";
    private static List<ThemeEntity> THEME_ENTITY_LIST;
    public static List<IndicationEntity> ALL_INDICATION_LIST;


    @PostConstruct
    public static void init() {
        Document document = getDocument();
        THEME_ENTITY_LIST = parseDocToThemeEntity(document);
        ALL_INDICATION_LIST = new ArrayList<IndicationEntity>();
        loadIndication(false);
    }

    public static List<ThemeEntity> getThemeList() {
        if (THEME_ENTITY_LIST.size() == 0) {
            Document document = getDocument();
            THEME_ENTITY_LIST = parseDocToThemeEntity(document);
        }
        return THEME_ENTITY_LIST;
    }

    public static List<ThemeEntity> getThemeList(String themeType) {
        List<ThemeEntity> themeList = new ArrayList<ThemeEntity>();
        for (ThemeEntity themeEntity : getThemeList()) {
            if (themeType.equals(themeEntity.getType())) {
                themeList.add(themeEntity);
            }
        }
        return themeList;
    }

    private static EnvConfigProperties getEnvConfig() {
        return SpringUtil.getBean(EnvConfigProperties.class);
    }

    private static IIndicationLimitService getIndicationLimitService() {
        return SpringUtil.getBean(IIndicationLimitService.class);
    }

    public static void loadIndication(boolean isSync) throws RuntimeException {
        try {
            ALL_INDICATION_LIST.clear();
            List<ThemeEntity> entityList = getThemeList();
            for (ThemeEntity themeEntity : entityList) {
                JSONObject params = new JSONObject();
                params.accumulate("theme_code", themeEntity.getThemeCode());
                params.accumulate("page_size", "10000");
                Object result = HttpUtil.postMethod(getEnvConfig().getInterFace().getIndicationList(), null, params);
                JSONObject resultJson = null;
                if (result != null) {
                    resultJson = JSONObject.fromObject(result);
                }
                if (resultJson == null) {
                    throw new RuntimeException("获取指标列表失败,请检测指标接口.");
                }
                JSONArray listArray = resultJson.getJSONArray("result");
                IndicationEntity indicationEntity;
                for (Object object : listArray) {
                    JSONObject item = JSONObject.fromObject(object);
                    String key = item.getString("key");
                    /*
                     * 在产品化中定义指标时, 需要严格按照规则进行定义 key 使用"_"进行分割, 每一段意义如下：
                     * key[0]: 指标分组 全国/各省
                     * key[1]: 指标显示位置 概览/非概览
                     * key[2]: 指标类型 机顶盒/网关
                     * key[3]: 指标计算颗粒度 月/日/实时/小时/分
                     * key[4]: 指标计算频率 月/日/实时/小时/分
                     * key[5]: 指标单位 万/%
                     * key[6]: 指标排序, 页面中指标显示的顺序, 按照此值进行排序
                     *
                     * 指标名称相同的指标, 视为同一个指标. 通过indicationGroup判断是全国指标, 还是省份指标
                     */
                    String[] keys = key.split("_");
                    indicationEntity = new IndicationEntity();
                    indicationEntity.setIndicationId(item.getString("item_id"));
                    indicationEntity.setIndicationName(item.getString("name"));
                    indicationEntity.setIndicationOwner(themeEntity.getOwner());
                    indicationEntity.setSysCode(themeEntity.getSysCode());
                    indicationEntity.setThemeCode(themeEntity.getThemeCode());
                    indicationEntity.setIndicationGroup(keys[0]);
                    indicationEntity.setIndicationPosition(keys[1]);
                    indicationEntity.setIndicationCatalog(keys[2]);
                    indicationEntity.setIndicationCycle(keys[3]);
                    indicationEntity.setIndicationFrequency(keys[4]);
                    indicationEntity.setIndicationUnit(keys[5]);
                    indicationEntity.setIndicationOrder(Integer.parseInt(keys[6]));
                    if (item.containsKey("biz_theme_exp")) {
                        indicationEntity.setBizThemeExp(item.getString("biz_theme_exp"));
                    }
                    if (isSync) {
                        // 解析指标阈值
                        if (item.containsKey("biz_theme_exp")) {
                            JSONArray expArray = JSONArray.fromObject(item.get("biz_theme_exp"));
                            if (expArray != null && expArray.size() > 0) {
                                for (Object expObj : expArray) {
                                    JSONObject expJson = JSONObject.fromObject(expObj);
                                    if (IndicationConst.INDICATION_ITEM_NAME_EXCEPTION_REASON.equals(expJson.get("name").toString())) {
                                        indicationEntity.setLimitEntity(parseIndicationLimit(expJson.get("exp").toString(), item.getString("item_id")));
                                        break;
                                    }
                                }
                            }
                        }
                    } else {
                        if(item.getString("item_id").equals("871cd9df-b882-4ebf-87a2-07b529bedc33")) {
                            log.info("cccc");
                        }
                        List<IndicationLimitEntity> limits = getIndicationLimitService().getIndicationLimitByIndicationId(item.getString("item_id"));
                        if (limits.size() > 0) {
                            indicationEntity.setLimitEntity(limits.get(0));
                        }
                    }
                    boolean exists = false;
                    for (IndicationEntity entity : ALL_INDICATION_LIST) {
                        if (entity.getIndicationId().equals(indicationEntity.getIndicationId())) {
                            exists = true;
                            break;
                        }
                    }
                    if (!exists) {
                        ALL_INDICATION_LIST.add(indicationEntity);
                        log.info(
                                "Indication data {} add to ALL_INDICATION_LIST.",
                                indicationEntity.toString());
                    }
                }
            }
            //排序
            Comparator<IndicationEntity> comparator = new Comparator<IndicationEntity>() {
                public int compare(IndicationEntity entity1, IndicationEntity entity2) {
                    if (!entity1.getThemeCode().equals(entity1.getThemeCode())) {
                        return entity1.getThemeCode().compareTo(entity2.getThemeCode());
                    } else if (!entity1.getIndicationGroup().equals(entity2.getIndicationGroup())) {
                        return entity2.getIndicationGroup().compareTo(entity1.getIndicationGroup());
                    } else if (!entity1.getIndicationOrder().equals(entity2.getIndicationOrder())) {
                        // luowenbo 20200730 != 变为 equals,封装类用!=不通知存在不同方法的判断
                        return entity1.getIndicationOrder() - entity2.getIndicationOrder();
                    } else {
                        return 0;
                    }
                }
            };
            Collections.sort(ALL_INDICATION_LIST, comparator);
        } catch (Exception e) {
            log.error("Load indication list error.", e);
        }
    }

    private static IndicationLimitEntity parseIndicationLimit(
            String reasonExp, String indicationId) {
        List<String> conditions = new ArrayList<String>();
        conditions.add(">");
        conditions.add("<");
        conditions.add("=");
        conditions.add("!");
        IndicationLimitEntity limitEntity = new IndicationLimitEntity("NaN",
                "NaN", "NaN", "NaN", "NaN", "NaN");
        limitEntity.setIndicationId(indicationId);
        String[] exps = reasonExp.split(";");
        for (String exp : exps) {
            exp = exp.replaceAll("'", "");
            String[] strs = exp.split("\\?");
            String value = reSplit(conditions, strs[0]);
            String reason = strs[1].replaceAll(":", "").trim();
            if (reason.equals("超过上限")) {
                limitEntity.setMaxValue(value);
            }
            if (reason.equals("低于下限")) {
                limitEntity.setMinValue(value);
            }
            if (reason.equals("超过变动值上限")) {
                limitEntity.setChangeValueMax(value);
            }
            if (reason.equals("低于变动值下限")) {
                limitEntity.setChangeValueMin(value);
            }
            if (reason.equals("超过变动率上限")) {
                limitEntity.setChangeRateMax(value);
            }
            if (reason.equals("低于变动率下限")) {
                limitEntity.setChangeRateMin(value);
            }
        }
        return limitEntity;
    }

    private static String reSplit(List<String> conditions, String value) {
        for (String condition : conditions) {
            if (value.contains(condition)) {
                String[] array = value.split(condition);
                return reSplit(conditions, array[array.length - 1]);
            }
        }
        return value;
    }

    /**
     * 根据主题获取指标列表
     *
     * @param themeCode
     * @return
     */
    public static List<IndicationEntity> getIndicationList(String themeCode) {
        List<IndicationEntity> returnList = new ArrayList<IndicationEntity>();
        for (IndicationEntity entity : ALL_INDICATION_LIST) {
            if (themeCode.equals(entity.getThemeCode())) {
                returnList.add(entity);
            }
        }
        return returnList;
    }

    /**
     * 获取指标集合
     *
     * @param indicationOwner
     * @param indicationPosition
     * @param indicationFrequency
     */
    public static List<IndicationEntity> getIndicationList(String indicationOwner, String indicationCatalog,
                                                           String indicationPosition, String indicationFrequency) {
        List<IndicationEntity> returnList = new ArrayList<IndicationEntity>();
        returnList.addAll(ALL_INDICATION_LIST);
        Iterator iterator = returnList.iterator();
        while (iterator.hasNext()) {
            IndicationEntity entity = (IndicationEntity) iterator.next();
            if (StringUtils.isNotEmpty(indicationOwner) && !indicationOwner.equals(entity.getIndicationOwner())) {
                iterator.remove();
                continue;
            }
            if (StringUtils.isNotEmpty(indicationCatalog) && !indicationCatalog.equals(entity.getIndicationCatalog())) {
                iterator.remove();
            }
            if (StringUtils.isNotEmpty(indicationPosition) && !indicationPosition.equals(entity.getIndicationPosition())) {
                iterator.remove();
                continue;
            }
            if (StringUtils.isNotEmpty(indicationFrequency) && !indicationFrequency.equals(entity.getIndicationFrequency())) {
                iterator.remove();
                continue;
            }
        }
        return returnList;
    }

    /**
     * 根据指标名称 获取指标信息
     * @param indicationGroup
     * @param indicationName
     * @return
     */
    public static IndicationEntity getEntityByName(String indicationGroup, String indicationName) {
        IndicationEntity indicationEntity = null;
        for (IndicationEntity entity : ALL_INDICATION_LIST) {
            if (indicationGroup.equals(entity.getIndicationGroup()) && entity.getIndicationName().equals(indicationName)) {
                indicationEntity = entity;
                break;
            }
        }
        return indicationEntity;
    }

    /**
     * 根据指标名称 获取指标信息
     *
     * @param indicationId
     * @return
     */
    public static IndicationEntity getEntityById(String indicationId) {
        IndicationEntity indicationEntity = null;
        for (IndicationEntity entity : ALL_INDICATION_LIST) {
            if (entity.getIndicationId().equals(indicationId)) {
                indicationEntity = entity;
                break;
            }
        }
        return indicationEntity;
    }

    /**
     * 获取xml document对象
     *
     * @return
     */
    private static Document getDocument() {
        Document document = null;
        InputStream inputStream = null;
        try {
            SAXReader reader = new SAXReader();
            inputStream = XMLUtil.class.getClassLoader().getResourceAsStream(THEME_MAPPER_XML_NAME);
            document = reader.read(inputStream);
        } catch (DocumentException e) {
            log.error("Load xml file [{}] error.", THEME_MAPPER_XML_NAME, e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("Close input stream error.", e);
                }
                inputStream = null;
            }
        }
        return document;
    }

    private static List<ThemeEntity> parseDocToThemeEntity(Document document) {
        List<ThemeEntity> entityList = new LinkedList<ThemeEntity>();
        try {
            Element element = document.getRootElement();
            List<Element> themeElements = element.elements();
            if (themeElements != null) {
                for (Element elt : themeElements) {
                    String themeCode = elt.attributeValue("themeCode");
                    String sysCode = elt.attributeValue("sysCode");
                    String type = elt.attributeValue("type");
                    String owner = elt.attributeValue("owner");
                    String wsdl = "";
                    if (IndicationConst.INDICATION_OWNER_ALL.equals(owner)) {
                        wsdl = getEnvConfig().getFamilyOpen().getNational();
                    }
                    if (IndicationConst.INDICATION_OWNER_REAL.equals(owner)) {
                        wsdl = getEnvConfig().getFamilyOpen().getNationalReal();
                    }
                    List<ItemEntity> commonItems = getItemEntityList(elt.element("common"));
                    CommonItemEntity commonEntity = new CommonItemEntity(commonItems);
                    List<ItemEntity> itemList = getItemEntityList(elt);
                    ThemeEntity themeEntity = new ThemeEntity(themeCode, sysCode, type, owner, wsdl, commonEntity, itemList);
                    entityList.add(themeEntity);
                }
            }
        } catch (Exception e) {
            log.error("Load theme list error.", e);
        }
        return entityList;
    }

    private static List<ItemEntity> getItemEntityList(Element parentElement) {
        List<ItemEntity> entityList = new LinkedList<ItemEntity>();
        List<Element> itemElementList = parentElement.elements("item");
        if (itemElementList != null) {
            for (Element element : itemElementList) {
                String name = element.attributeValue("name");
                String type = element.attributeValue("type");
                String handler = getElementText(element, "handler");
                String wsdl = getElementText(element, "wsdl");
                String method = getElementText(element, "method");
                String timeType = getElementText(element, "timeType");
                String mirrorColumn = getElementText(element, "mirrorColumn");
                ItemEntity itemEntity = new ItemEntity(name, type, handler, wsdl, method, timeType, mirrorColumn);
                entityList.add(itemEntity);
            }
        }
        return entityList;
    }

    private static String getElementText(Element parentElt, String elementName) {
        Element element = parentElt.element(elementName);
        String result = null;
        if (element != null) {
            result = element.getTextTrim();
        }
        return result;
    }
}
