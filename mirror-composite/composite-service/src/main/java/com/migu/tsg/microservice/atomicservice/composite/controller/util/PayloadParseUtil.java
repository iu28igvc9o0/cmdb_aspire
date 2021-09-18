package com.migu.tsg.microservice.atomicservice.composite.controller.util;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.io.JsonStringEncoder;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.migu.tsg.microservice.atomicservice.composite.Constants;
import com.migu.tsg.microservice.atomicservice.composite.controller.logcontext.LastLogCodeEnum;
import com.migu.tsg.microservice.atomicservice.composite.dao.po.CompositeResource;
import com.migu.tsg.microservice.atomicservice.composite.exception.BaseException;
import com.migu.tsg.microservice.atomicservice.composite.exception.ResultErrorEnum;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.CompRoleCreatePayload;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.OrgsRolesPermissionResponse;
import com.migu.tsg.microservice.atomicservice.composite.vo.rbac.RbacResource;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.RoleDetailParentsResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.ListRolesAssignedResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.AccountDTO;

import net.sf.json.JSONArray;
import org.springframework.util.CollectionUtils;

/**
* 把model抽取出来进行转化
* Project Name:composite-service2
* File Name:PayloadParseUtil.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.controller.util
* ClassName: PayloadParseUtil <br/>
* date: 2017年8月30日 上午10:30:10 <br/>
* @author yangshilei
* @version
* @since JDK 1.6
*/
public class PayloadParseUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(PayloadParseUtil.class);

    /**
     * parse2CompResList:(RoleDetailParentsResponse对象抽取成CompositeResource). <br/>
     * 作者： yangshilei
     *
     * @param rbacParentRoleList
     * @return
     */
    public static List<CompositeResource> parse2CompResList(List<RoleDetailParentsResponse> rbacParentRoleList) {
        return rbacParentRoleList.stream().map(pitem -> {
            CompositeResource compRes = new CompositeResource();
            compRes.setUuid(pitem.getUuid());
            compRes.setName(pitem.getName());
            compRes.setType("role");
            return compRes;
        }).collect(Collectors.toList());
    }
    
    /**
    * parse2RbacResource:(将调用rbac生成的返回体转化成鉴权需要的RbacResource的请求体). <br/>
    * 作者： yangshilei
    * @param listAccountRoles
    * @return
    */   
    public static List<RbacResource> parse2RbacResource(List<ListRolesAssignedResponse> listAccountRoles){
        return listAccountRoles.stream().map(pitem ->{
            RbacResource orgsRes = new RbacResource();
            orgsRes.setName(pitem.getRoleName());
            orgsRes.setUuid(pitem.getRoleUuid());
            orgsRes.setResTypeAction("role:view");
            orgsRes.setSubAccount(pitem.getUser());
            return orgsRes;
        }).collect(Collectors.toList());  
    }
    
    public static List<OrgsRolesPermissionResponse> listAccount2OrgsRoles(
                                            List<ListRolesAssignedResponse> listAccountRoles) {
        return listAccountRoles.stream().map(pitem ->{
            OrgsRolesPermissionResponse orgs = new OrgsRolesPermissionResponse();
//            orgs.setResourceActions(pitem.getResourceActions());
            orgs.setAssignedAt(pitem.getAssignedAt());
            orgs.setNamespace(pitem.getNamespace());
//            orgs.setProjectName(pitem.getProjectName());
//            orgs.setProjectUuid(pitem.getProjectUuid());
            orgs.setRoleName(pitem.getRoleName());
            orgs.setRoleUuid(pitem.getRoleUuid());
//            orgs.setSpaceName(pitem.getSpaceName());
//            orgs.setSpaceUuid(pitem.getSpaceUuid());
            orgs.setUser(pitem.getUser());
            return orgs;
        }).collect(Collectors.toList());
    }
    
    /**
     * 把CompRoleCreatePayload转成CompositeResource对象.<br/>
     *
     * 作者： pengguihua
     * @param payloadList
     * @param isResponse
     * @return
     */
    public static List<CompositeResource> parse2CompResList(List<CompRoleCreatePayload> payloadList,
            boolean isResponse) {
        return payloadList.stream().map(roleItem -> {
            CompositeResource compRes = new CompositeResource();
            compRes.setName(roleItem.getName());
            if (isResponse) {
                compRes.setUuid(roleItem.getUuid());
                compRes.setType(Constants.Resource.RES_ROLE);
            }
            return compRes;
        }).collect(Collectors.toList());
    }

    public static List<CompositeResource> parseAccount2CompResLisst(List<AccountDTO> payloadList){
        return payloadList.stream().map(account -> {
            CompositeResource compRes = new CompositeResource();
            compRes.setName(account.getUsername());
            return compRes;
        }).collect(Collectors.toList());
       
    }
    /**
    * 基于Jackson的对象转换.<br/>
    *
    * 作者： pengguihua
    * @param target
    * @param sourceList
    * @return
    */
    public static <T, S> List<T> jacksonBaseParse(Class<T> target, List<S> sourceList) {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return sourceList.stream().map((sourceItem) -> {
            try {
                String json = mapper.writeValueAsString(sourceItem);
                if (sourceItem instanceof CharSequence) {
                    json = String.valueOf(sourceItem);
                }
                // luowenbo 2020.07.17 JSON注入缺陷
                JsonStringEncoder encoder = JsonStringEncoder.getInstance();
                // quoteAsUTF8 方法将字符创按照 JSON 标准处理并编码为 UTF-8
                byte[] bytes = encoder.quoteAsUTF8(json);
                String newJson = new String(bytes, StandardCharsets.UTF_8);
                newJson = StringEscapeUtils.unescapeJava(newJson);
                return  mapper.readValue(newJson, target);
            } catch (Exception e) {
                throw new BaseException(LastLogCodeEnum.GENERAL_ERROR, ResultErrorEnum.BIZ_OBJECT_PARSE_JSON_FAIL, e);
            }
        }).collect(Collectors.toList());
    }
    
    
    /**
    * 把net.sf.json.JSONArray数组转成基于JACKSON目标对象数组.<br/>
    *
    * 作者： pengguihua
    * @param target
    * @param sourceArr
    * @return
    */
    public static <T> List<T> jacksonJsonArrayParse(Class<T> target, JSONArray sourceArr) {
        List<Object> jsonItemList = new ArrayList<Object>();
        for (int i = 0; i < sourceArr.size(); i++) {
            Object item = sourceArr.get(i);
            jsonItemList.add(item);
        }
        return jacksonBaseParse(target, jsonItemList);
    }

    /**
    * 基于Jackson的对象转换. <br/>
    *
    * 作者： pengguihua
    * @param target
    * @param source
    * @return
    */
    public static <T, S> T jacksonBaseParse(Class<T> target, S source) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            String json = mapper.writeValueAsString(source);
            if (source instanceof CharSequence) {
                json = String.valueOf(source);
            }
            // luowenbo 2020.07.17 JSON注入缺陷
            JsonStringEncoder encoder = JsonStringEncoder.getInstance();
            // quoteAsUTF8 方法将字符创按照 JSON 标准处理并编码为 UTF-8
            byte[] bytes = encoder.quoteAsUTF8(json);
            String newJson = new String(bytes, StandardCharsets.UTF_8);
            newJson = StringEscapeUtils.unescapeJava(newJson);
            return mapper.readValue(newJson, target);
        } catch (Exception e) {
            throw new BaseException(LastLogCodeEnum.GENERAL_ERROR, ResultErrorEnum.BIZ_OBJECT_PARSE_JSON_FAIL, e);
        }
    }

    /**
     * 基于Jackson的对象转换. <br/>
     *
     * 作者： pengguihua
     * @param target
     * @param source
     * @return
     */
     public static <T, S> T fastjsonBaseParse(Class<T> target, S source) {
         try {
             ObjectMapper mapper = new ObjectMapper();
             mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
             String json = mapper.writeValueAsString(source);
             if (source instanceof CharSequence) {
                 json = String.valueOf(source);
             }
             return JSON.parseObject(json,target);
         } catch (Exception e) {
             throw new BaseException(LastLogCodeEnum.GENERAL_ERROR, ResultErrorEnum.BIZ_OBJECT_PARSE_JSON_FAIL, e);
         }
     }
    
    
    /**
    * 把source对象基于jackson转化成json字符串.<br/>
    *
    * 作者： pengguihua
    * @param source
    * @return
    */
    public static String jacksonBase2JsonStr(Object source) {
        if (source instanceof CharSequence) {
            return source.toString();
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.writeValueAsString(source);
        } catch (Exception e) {
            throw new BaseException(LastLogCodeEnum.GENERAL_ERROR, ResultErrorEnum.BIZ_OBJECT_PARSE_JSON_FAIL, e);
        }
    }

    /**
     * bean转换成map
     * @param objList
     * @return
     * @throws Exception
     */
    public static List<Map<String, Object>> objectToMap(List objList) throws Exception {
        List<Map<String, Object>> resultList = new ArrayList<>();
        if(CollectionUtils.isEmpty(objList)){
            return resultList;
        }
        for (Object obj: objList) {
            Map<String, Object> map = new HashMap<String, Object>();
            Field[] declaredFields = obj.getClass().getDeclaredFields();
            for (Field field : declaredFields) {
                field.setAccessible(true);
                map.put(field.getName(), field.get(obj));
            }
            resultList.add(map);
        }
        return resultList;
    }
}
