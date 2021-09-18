package com.migu.tsg.microservice.atomicservice.composite.dao.po;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestConstraintEnum;
import com.migu.tsg.microservice.atomicservice.composite.vo.rbac.RbacResource;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.CollectionUtils;

/**
* 数据库表Resources_resource的映射model
* Project Name:composite-service
* File Name:CompositeResource.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.dao.po
* ClassName: CompositeResource <br/>
* date: 2017年9月1日 下午11:59:02 <br/>
* 数据库表Resources_resource的映射model
* @author pengguihua
* @version 
* @since JDK 1.6
*/
@NoArgsConstructor
@Data
@ToString
public class CompositeResource {
    private int id;
    private String type;
    private String uuid;
    private String namespace;
    private String name;
    private String regionId    = "";
    private String spaceUuid   = "";
    private String projectUuid = "";
    private String knamespaceUuid = "";
    private String createdBy;
    private Date createdAt;

    private String projectName;
    private String regionName;
    private String spaceName;
    
    private String knamespaceName;

    private String orderBy = "name";
    private static final int QUERYSIZE = 100;
    
    public Date getCreatedAt() {
        if (this.createdAt == null) {
            return null;
        }
        return (Date) this.createdAt.clone();
    }
    public void setCreatedAt(final Date createdAt) {
        if (createdAt == null) {
            this.createdAt = null;
        } else {
            this.createdAt = (Date) createdAt.clone();
        }
    }
    
    /**
     * 保持和jakiro一致，如表的type要保持大写
     * setType:(这里用一句话描述这个方法的作用). <br/>
     * 作者： baiwp
     * @param type
     */
    public void setType (String type) {
        if (null != type) {
            this.type = type.toUpperCase(Locale.getDefault());
        }
    }
    
    public CompositeResource(String uuid, String type, String namespace, String name, String createdBy, 
            String projectUuid, String spaceUuid, String regionUuid) {
        this.uuid = uuid;
        if (null != type) {
            this.type = type.toUpperCase(Locale.getDefault());
        }
        this.namespace = namespace;
        this.name = name;
        this.createdBy = createdBy;
        this.projectUuid = projectUuid == null ? "" : projectUuid;
        this.spaceUuid = spaceUuid == null ? "" : spaceUuid;
        this.regionId = regionUuid == null ? "" : regionUuid;
    }
    
    public Map<String, String> getFlattenConst() {
        return getFlattenConst(true, true, true, true);
    }
    
    /**
    * 获取对象自身扁平约束.<br/>
    *
    * 作者： pengguihua
    * @param addProject
    * @param addRegion
    * @param addSpace
    * @return
    */
    public Map<String, String> getFlattenConst(
            boolean addProject, boolean addRegion, boolean addSpace, boolean addKnamespace) {
        
        Map<String, String> resConstMap = new HashMap<String, String>();
        if (addProject && StringUtils.isNotBlank(projectName)) {
            resConstMap.put("res:" + RequestConstraintEnum.project_name.getResType(), projectName);
        }
        if (addRegion && StringUtils.isNotBlank(regionName)) {
            resConstMap.put("res:" + RequestConstraintEnum.region_name.getResType(), regionName);
        }
        if (addSpace && StringUtils.isNotBlank(spaceName)) {
            resConstMap.put("res:" + RequestConstraintEnum.space_name.getResType(), spaceName);
        }
        if (addKnamespace && StringUtils.isNotBlank(knamespaceName)) {
            resConstMap.put("res:" + RequestConstraintEnum.knamespace_name.getResType(), spaceName);
        }
        return resConstMap;
    }

    /**
     * 获取资源对象的扁平表示，示例如下 "uuid": "123-321-123", "name": "my-web", "res:cluster":
     * "dev-cluster", "res:space": "dev-space"
     *
     * 作者： pengguihua
     *
     * @return
     */
    public RbacResource getFlatten() {
        RbacResource rabcRes = new RbacResource();
        rabcRes.setUuid(uuid);
        rabcRes.setName(name);
        rabcRes.setConsCluster(regionName);
        rabcRes.setConsSpace(spaceName);
        rabcRes.setConsProject(projectName);
        rabcRes.setConsKnamespace(knamespaceName);
        return rabcRes;
    }

    /**
    * 获取资源记录UUID列表.<br/>
    *
    * 作者： pengguihua
    * @param resList
    * @return
    */
    public static List<String> getUuidList(List<CompositeResource> resList) {
        List<String> uuidList = new ArrayList<String>();
        for (CompositeResource resItem : resList) {
            uuidList.add(resItem.getUuid());
        }
        return uuidList;
    }

    /**
    * 获取逗号分割的UUID串.<br/>
    *
    * 作者： pengguihua
    * @param resList
    * @return
    */
    public static String getJoinUuid(List<CompositeResource> resList) {
        List<String> uuidList = getUuidList(resList);
        return StringUtils.join(uuidList, ",");
    }
    
    /**
     * 获取逗号分割的UUID串.<br/>
     *
     * 作者： qianchunhui
     * @param resList
     * @return
     */
     public static List<String> getJoinUuidData(List<CompositeResource> resList) {
     	int size = resList.size();
 		int count = 0;
 		if (size == 0) {
 			return new ArrayList<String>();
 		}
 		List<String> result = new ArrayList<String>();
 		if (size > QUERYSIZE) {
 			count = size / QUERYSIZE;
 		}
 		for (int x = 0; x < count; x++) {
 			StringBuffer sb = new StringBuffer();
 			for (int i = QUERYSIZE * x; i < QUERYSIZE * (x + 1); i++) {
 				sb.append(resList.get(i).getUuid());
 				sb.append(",");
 			}
 			result.add(sb.substring(0, sb.length() - 1));
 		}
 		if (count * QUERYSIZE != size) {
 			StringBuffer sbLatest = new StringBuffer();
 			for (int i = count * QUERYSIZE; i < size; i++) {
 				sbLatest.append(resList.get(i).getUuid());
 				sbLatest.append(",");
 			}
 			result.add(sbLatest.substring(0, sbLatest.length() - 1));
 		}
 		return result;
     }


        /**
     * 获取逗号分割的UUID串.<br/>
     *
     * 作者： qianchunhui
     * @param resList
     * @return
     */
     public static List<String> getJoinUuidDataString(List<String> resList) {
     	int size = resList.size();
 		int count = 0;
 		if (size == 0) {
 			return new ArrayList<String>();
 		}
 		List<String> result = new ArrayList<String>();
 		if (size > QUERYSIZE) {
 			count = size / QUERYSIZE;
 		}
 		for (int x = 0; x < count; x++) {
 			StringBuffer sb = new StringBuffer();
 			for (int i = QUERYSIZE * x; i < QUERYSIZE * (x + 1); i++) {
 				sb.append(resList.get(i));
 				sb.append(",");
 			}
 			result.add(sb.substring(0, sb.length() - 1));
 		}
 		if (count * QUERYSIZE != size) {
 			StringBuffer sbLatest = new StringBuffer();
 			for (int i = count * QUERYSIZE; i < size; i++) {
 				sbLatest.append(resList.get(i));
 				sbLatest.append(",");
 			}
 			result.add(sbLatest.substring(0, sbLatest.length() - 1));
 		}
 		return result;
     }
    
    /**
    * 批量把资源转成RABC鉴权的格式.<br/>
    *
    * 作者： pengguihua
    * @param resList
    * @return
    */
    public static List<RbacResource> toFlattenList(List<CompositeResource> resList) {
        List<RbacResource> flattenList = new ArrayList<RbacResource>();
        if (!CollectionUtils.isEmpty(resList)) {
            for (CompositeResource resItem : resList) {
                flattenList.add(resItem.getFlatten());
            }
        }
        return flattenList;
    }
}
