package com.aspire.ums.cmdb.sync.service.producer;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import com.aspire.ums.cmdb.config.KafkaConfigConstant;
import com.aspire.ums.cmdb.sync.entity.CmdbSyncFieldMapping;
import com.aspire.ums.cmdb.sync.payload.CmdbDeviceAssetDTO;
import com.aspire.ums.cmdb.sync.service.ICmdbSyncFieldMappingService;
import com.aspire.ums.cmdb.util.JsonMapper;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.CaseFormat;
import com.google.common.collect.Maps;

/**
 * cmdb资产表同步.
 *
 * @author jiangxuwen
 * @date 2020/5/13 20:44
 */
@Service("cmdbDeviceAssetProducerService")
public class CmdbDeviceAssetProducerServiceImpl extends AbstractKafkaProducerService<CmdbDeviceAssetDTO> {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ICmdbInstanceService instanceService;

    @Autowired
    private ICmdbSyncFieldMappingService cmdbSyncFieldMappingService;

    @Override
    public CmdbDeviceAssetDTO getObjectData(String moduleId, String objectId) {
        Map<String, Object> instanceMap = instanceService.getInstanceDetail(moduleId, objectId);
        try {
            return convert(instanceMap);
        } catch (Exception e) {
            logger.error("", e);
        }
        return null;
    }

    private CmdbDeviceAssetDTO convert(Map<String, Object> instanceMap) throws Exception {

        CmdbSyncFieldMapping query = new CmdbSyncFieldMapping();
        query.setMappingType(KafkaConfigConstant.MAPPING_TYPE_CMDB_DEVICE_ASSET);
        query.setMappingKey(KafkaConfigConstant.MAPPING_KEY_FIELD);
        List<CmdbSyncFieldMapping> deviceAssetMappings = cmdbSyncFieldMappingService.list(query);

        query.setMappingType(KafkaConfigConstant.MAPPING_TYPE_MAINTENCE);
        query.setMappingKey(KafkaConfigConstant.MAPPING_KEY_FIELD);
        List<CmdbSyncFieldMapping> maintenceMappings = cmdbSyncFieldMappingService.list(query);

        Map<String, Object> keyValuePairs = Maps.newHashMap();
        deviceAssetMappings.forEach(e -> {
            String dtoField = e.getSrcField();
            String mapField = e.getDestField();
            if (instanceMap.containsKey(mapField)) {
                // 字段名 下划线转为驼峰风格
                String fieldName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, dtoField);
                // Field field = ReflectionUtils.findField(dto.getClass(), fieldName);
                Object value = instanceMap.get(mapField);
                keyValuePairs.put(fieldName, value);
            }
        });

        maintenceMappings.forEach(e -> {
            String dtoField = e.getSrcField();
            String mapField = e.getDestField();
            if (instanceMap.containsKey(mapField)) {
                // 字段名 下划线转为驼峰风格
                String fieldName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, dtoField);
                // Field field = ReflectionUtils.findField(dto.getClass(), fieldName);
                Object value = instanceMap.get(mapField);
                keyValuePairs.put(fieldName, value);
            }
        });
        String json = JsonMapper.getInstance().toJson(instanceMap);
        CmdbDeviceAssetDTO dto = JsonMapper.getInstanceWithSnakeCase().readValue(json, new TypeReference<CmdbDeviceAssetDTO>() {});

        BeanInfo beanInfo = Introspector.getBeanInfo(dto.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            String fieldName = property.getName();
            Object fieldValue = PropertyUtils.getProperty(dto, fieldName);
            System.out.println("fieldName==" + fieldName);
            System.out.println("fieldValue==" + fieldValue);
            if (fieldValue == null && keyValuePairs.containsKey(fieldName)) {
                fieldValue = keyValuePairs.get(fieldName);
                PropertyUtils.setProperty(dto, fieldName, fieldValue);
            }
        }
        return dto;
    }

    private void setValue(Field field, Object target, Object value) {
        if (field != null) {
            ReflectionUtils.makeAccessible(field);
            if (field.getGenericType().toString().equals("class java.lang.Integer")) {
                value = Integer.parseInt(value.toString());
            }
            if (field.getGenericType().toString().equals("class java.lang.Boolean")) {
                value = BooleanUtils.toBoolean(value.toString());
            }
            if (field.getGenericType().toString().equals("class java.lang.Short")) {
                value = Short.valueOf(value.toString());
            }
            if (field.getGenericType().toString().equals("class java.lang.Long")) {
                value = Long.valueOf(value.toString());
            }
            ReflectionUtils.setField(field, target, value);
        }
    }
}
