package com.migu.tsg.microservice.atomicservice.composite.controller.authcontext;

import com.migu.tsg.microservice.atomicservice.composite.Constants;

/**
* 请求中资源约束键名称枚举
* Project Name:composite-service
* File Name:ConstraintNameEnum.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.controller.model
* ClassName: ConstraintNameEnum <br/>
* date: 2017年8月23日 下午2:09:14 <br/>
* 请求中资源约束键名称枚举
* @author pengguihua
* @version
* @since JDK 1.6
*/

public enum RequestConstraintEnum {
    cluster_name(Constants.RbacResource.CLUSTER),
    region(Constants.RbacResource.CLUSTER),
    region_name(Constants.RbacResource.CLUSTER),
    space_name(Constants.RbacResource.SPACE),
    knamespace_name(Constants.RbacResource.KNAMESPACE),
    project_name(Constants.RbacResource.PROJECT),
    project_template(Constants.RbacResource.PROJECT_TEMPLATE),
    application(Constants.RbacResource.APPLICATION),
    application_name(Constants.RbacResource.APPLICATION),
    registry_name(Constants.RbacResource.REGISTRY),
    priv_registry(Constants.RbacResource.PRIVATE_REGISTRY),
    registry_project(Constants.RbacResource.REGISTRY_PROJECT),
    priv_regis_proj(Constants.RbacResource.PRIVATE_REGISTRY_PROJECT),
    cloud_account_name(Constants.RbacResource.CMP_CLOUD_ACCOUNT),
    dashboard_name(Constants.RbacResource.DASHBOARD),
    build_config_name(Constants.RbacResource.BUILD_CONFIG),
    sync_config_name(Constants.RbacResource.SYNC_REGISTRY_CONFIG),
    pipeline_config_name(Constants.RbacResource.PIPELINE),
    pipeline_task_type(Constants.RbacResource.PIPELINE_TASK_TYPE),
    subaccount_username(Constants.RbacResource.SUBACCOUNT),
	res_service(Constants.RbacResource.SERVICE),
    service_name(Constants.RbacResource.SERVICE),
//    cluster_node_name(""),  snapshots
    repository_name(Constants.RbacResource.REPOSITORY),
    volume_name(Constants.RbacResource.VOLUME),
    snapshots_name(Constants.RbacResource.SNAPSHOT),
    subnet_name(Constants.RbacResource.SUBNET),
    load_balancer_name(Constants.RbacResource.LOADBALANCER),
    job_config_name(Constants.RbacResource.JOB_CONFIG),
    notification_name(Constants.RbacResource.NOTIFICATION),
    
    certificate_name(Constants.RbacResource.CERTIFICATE),
    envfile_resource(Constants.RbacResource.ENVFILE),
    app_name(Constants.RbacResource.APPLICATION), 
    role_name(Constants.RbacResource.ROLE);

    private final String resType;
    RequestConstraintEnum(String resType) {
        this.resType = resType;
    }
    
    /**
    * 获取资源类型. <br/>
    *
    * 作者： pengguihua
    * @return
    */
    public String getResType() {
        return resType;
    }

    /**
    * 根据枚举名称获取对象.<br/>
    *
    * 作者： pengguihua
    * @param matchName
    * @return
    */
    public static RequestConstraintEnum getEnumByName(final String matchName) {
        for (RequestConstraintEnum item : RequestConstraintEnum.values()) {
            if (item.name().equals(matchName)) {
                return item;
            }
        }
        return null;
    }
}
