package com.migu.tsg.microservice.atomicservice.composite;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Constants {
    public static final int OP_LEVEL_TRIVIAL = 0;
    public static final int OP_LEVEL_ATTENTION = 1;
    public static final int OP_LEVEL_IMPORTANT = 2;

    public static class Rbac{
        /**
         *操作角色类型
         */
        public static final Integer ROLE_TYPE_OPERATE = 0;
        /**
         *资源角色类型
         */
        public static final Integer ROLE_TYPE_RESOURCE = 1;

        /**
         *特殊角色类型
         */
        public static final String ACTION_TYPE_SPECIAL = "0";

        /**
         *正常操作类型
         */
        public static final String ACTION_TYPE_NOMAL = "1";

        /**
         * 临时部门类型
         */
        public static final Integer DEPARTMENT_TYPE_TEMP = 0;
        /**
         * 正式部门类型
         */
        public static final Integer DEPARTMENT_TYPE_FORMAL = 1;

    }
    public static class Theme {
        /**
         * 默认状态-临时
         */
        public static final String STATUS = "1";
        /**
         * 默认上报开关-开启
         */
        public static final String UP_SWiTCH = "0";
    }

    public static class Task {
        /**
         * 默认状态 -开启
         */
        public static final String STATUS = "OFF";

        /**
         * 任务关联对象类型 1：设备2：业务系统
         */
        public static final String DEVICE_TYPE = "1";
    }

    public static class Template {
        /**
         * 默认状态
         */
        public static final String STATUS = "ON";
        /**
         * 默认优先级
         */
        public static final String PRIORITY = "5";
        /**
         * 模板关联对象类型 1：设备2：业务系统
         */
        public static final String DEVICE_TYPE = "1";

        public static final String SYS_TYPE = "2";

        public static final String OPS_TYPE = "3";
        /**
         * 默认功能类型巡检
         */
        public static final String FUN_TYPE_INSP = "2";

        /**
         * 默认类型硬件
         */
        public static final String TYPE_HARD = "1";
        /**
         * 默认监控类型系统
         */
        public static final String MON_TYPE_SYS = "1";

        public static final String MON_TYPE_BIZ= "2";
        /**
         * 临时状态
         */
        public static final String TEMP_STATUS = "0";
        /**
         * 正式状态
         */
        public static final String FORMAL_STATUS = "1";

        /**
         * 默认接入系统类型
         */
        public static final String SYSTYPE_ZABBIX = "ZABBIX";
    }

    public static class Item {
        /**
         * 默认状态
         */
        public static final String STATUS = "ON";
        /**
         * 默认类型
         */
        public static final String TYPE = "SCRIPT";
        /**
         * 默认接入系统类型
         */
        public static final String SYSTYPE_ZABBIX = "ZABBIX";

        public static final String SYSTYPE_MIRROR = "MIRROR";
    }

    public static class Resource {
        public static final String RES_TASK = "task";
        public static final String RES_TEMPLATE = "template";
        public static final String RES_THEME = "theme";
        public static final String RES_SERVICE = "service";
        public static final String RES_SERVICE_CAAS = "service_caas";
        public static final String RES_REGION = "region";
        public static final String RES_REGION_ZONE = "region_zone";
        public static final String RES_ENVFILE = "env_file";
        public static final String RES_VOLUME = "volume";
        public static final String RES_SNAPSHOT = "snapshot";
        public static final String RES_APPLICATION = "application";
        public static final String RES_APPLICATION_CAAS = "application_caas";
        public static final String RES_APPLICATION_TEMPLATE = "application_temp";
        public static final String RES_NOTIFICATION = "notification";
        public static final String RES_ALARM = "alarm";
        public static final String RES_REPO = "repo";
        public static final String RES_SYNC_REGISTRY_CONFIG = "sync_regis_conf";
        public static final String RES_PRIVATE_REGISTRY = "priv_registry";
        public static final String RES_PRIVATE_REGISTRY_REPO = "priv_regis_repo";
        public static final String RES_PRIVATE_REGISTRY_PROJECT = "priv_regis_proj";
        public static final String RES_IP = "ip";
        public static final String RES_LB = "load_balancer";
        public static final String RES_LBS = "load_balancers";
        public static final String RES_PRIVATE_BUILD_CONFIG = "builds_config";
        public static final String RES_PRIVATE_BUILD = "priv_build";
        public static final String RES_PIPELINE = "pipeline";
        public static final String RES_CMP_CLOUD_ACCOUNT = "cloud_account";
        public static final String RES_CONFIG = "config";
        public static final String RES_DASHBOARD = "dashboard";
        public static final String RES_SPACE = "space";
        public static final String RES_KNAMESPACE = "knamespace";
        public static final String RES_ROLE = "role";
        public static final String RES_SUBNET = "subnet";
        public static final String RES_PRIVATE_IP = "private_ip";
        public static final String RES_PROJECT = "project";
        public static final String RES_PROJECT_TEMPLATE = "project_template";
        public static final String RES_CERTIFICATE = "certificate";
        public static final String RES_JOB_CONFIG = "job_config";
        public static final String RES_JOB_HISTORY = "job_history";
        public static final String RES_NODE = "node";
        public static final String RES_ORGANIZATION = "organization";
    }

    public static class RbacResource {
        public static final String SERVICE = "service";
        public static final String CLUSTER = "cluster";
        public static final String ENVFILE = "env_file";
        public static final String VOLUME = "volume";
        public static final String SNAPSHOT = "snapshot";
        public static final String APPLICATION = "application";
        public static final String APPLICATION_TEMPLATE = "application_template";
        public static final String NOTIFICATION = "notification";
        public static final String ALARM = "alarm";
        public static final String SYNC_REGISTRY_CONFIG = "sync_config";
        public static final String REGISTRY = "registry";
        public static final String PRIVATE_REGISTRY = "registry";
        public static final String REGISTRY_PROJECT = "registry_project";
        public static final String PRIVATE_REGISTRY_PROJECT = "priv_regis_proj";
        public static final String REPOSITORY = "repository";
        public static final String PRIVATE_REGISTRY_REPO = "repository";
        public static final String IP = "ip";
        public static final String LB = "load_balancer";
        public static final String LBS = "load_balancers";
        public static final String BUILD_CONFIG = "build_config";
        public static final String PRIVATE_BUILD_CONFIG = "build_config";
        public static final String PRIVATE_BUILD = "build_history";
        public static final String PIPELINE = "pipeline_config";
        public static final String PIPELINE_TASK_TYPE = "pipeline_task_type";
        public static final String CMP_CLOUD_ACCOUNT = "cloud_account";
        public static final String CONFIG = "configuration_file";
        public static final String DASHBOARD = "dashboard";
        public static final String SPACE = "space";
        public static final String KNAMESPACE = "knamespace";
        public static final String ROLE = "role";
        public static final String SUBNET = "subnet";
        public static final String PRIVATE_IP = "private_ip";
        public static final String PROJECT = "project";
        public static final String PROJECT_TEMPLATE = "project_template";
        public static final String CERTIFICATE = "certificate";
        public static final String LOADBALANCER = "load_balancer";
        public static final String JOB_CONFIG = "job_config";
        public static final String JOB_HISTORY = "job_history";
        public static final String SUBACCOUNT = "subaccount";
        public static final String ORGANIZATION = "organization";
        public static final String PIPELINE_TASK = "pipeline_task";
        public static final String PIPELINE_HISTORY = "pipeline_history";
        public static final String SYNC_HISTORY = "sync_history";
        public static final String DASHBOARD_PANEL = "dashboard_panel";
        public static final String CLOUD_INSTANCE = "cloud_instance";
        public static final String EVENT = "event";
        public static final String CLUSTER_NODE = "cluster_node";
    }

    public static final Map<String, String> RESOURCE_TYPE_MAP = Collections
            .unmodifiableMap(new HashMap<String, String>() {
                private static final long serialVersionUID = 3673638937284577764L;

                {
                    put(Resource.RES_SERVICE, RbacResource.SERVICE);
                    put(Resource.RES_REGION, RbacResource.CLUSTER);
                    put(Resource.RES_ENVFILE, RbacResource.ENVFILE);
                    put(Resource.RES_VOLUME, RbacResource.VOLUME);
                    put(Resource.RES_SNAPSHOT, RbacResource.SNAPSHOT);
                    put(Resource.RES_APPLICATION, RbacResource.APPLICATION);
                    put(Resource.RES_APPLICATION_TEMPLATE, RbacResource.APPLICATION_TEMPLATE);
                    put(Resource.RES_NOTIFICATION, RbacResource.NOTIFICATION);
                    put(Resource.RES_ALARM, RbacResource.ALARM);
                    put(Resource.RES_SYNC_REGISTRY_CONFIG, RbacResource.SYNC_REGISTRY_CONFIG);
                    put(Resource.RES_PRIVATE_REGISTRY, RbacResource.PRIVATE_REGISTRY);
                    put(Resource.RES_PRIVATE_REGISTRY_PROJECT, RbacResource.PRIVATE_REGISTRY_PROJECT);
                    put(Resource.RES_PRIVATE_REGISTRY_REPO, RbacResource.REPOSITORY);
                    put(Resource.RES_IP, RbacResource.IP);
                    put(Resource.RES_LB, RbacResource.LB);
                    put(Resource.RES_LBS, RbacResource.LBS);
                    put(Resource.RES_PRIVATE_BUILD_CONFIG, RbacResource.PRIVATE_BUILD_CONFIG);
                    put(Resource.RES_PRIVATE_BUILD, RbacResource.PRIVATE_BUILD);
                    put(Resource.RES_PIPELINE, RbacResource.PIPELINE);
                    put(Resource.RES_CMP_CLOUD_ACCOUNT, RbacResource.CMP_CLOUD_ACCOUNT);
                    put(Resource.RES_CONFIG, RbacResource.CONFIG);
                    put(Resource.RES_DASHBOARD, RbacResource.DASHBOARD);
                    put(Resource.RES_SPACE, RbacResource.SPACE);
                    put(Resource.RES_ROLE, RbacResource.ROLE);
                    put(Resource.RES_NODE, RbacResource.CLUSTER_NODE);
                    put(Resource.RES_SUBNET, RbacResource.SUBNET);
                    put(Resource.RES_PRIVATE_IP, RbacResource.PRIVATE_IP);
                    put(Resource.RES_PROJECT, RbacResource.PROJECT);
                    put(Resource.RES_PROJECT_TEMPLATE, RbacResource.PROJECT_TEMPLATE);
                    put(Resource.RES_CERTIFICATE, RbacResource.CERTIFICATE);
                    put(Resource.RES_JOB_CONFIG, RbacResource.JOB_CONFIG);
                    put(Resource.RES_JOB_HISTORY, RbacResource.JOB_HISTORY);
                }
            });

    public static final Map<String, Map<String, String>> RESOURCE_ACTION_MAP = Collections
            .unmodifiableMap(new HashMap<String, Map<String, String>>() {
                private static final long serialVersionUID = -4892737838101934463L;

                {
                    put(RbacResource.SERVICE, new HashMap<String, String>() {
                        private static final long serialVersionUID = 3774108904494007603L;

                        {
                            put(Action.CREATE, RbacResource.SERVICE + ":" + Action.CREATE);
                            put(Action.VIEW, RbacResource.SERVICE + ":" + Action.VIEW);
                            put(Action.UPDATE, RbacResource.SERVICE + ":" + Action.UPDATE);
                            put(Action.DESTROY, RbacResource.SERVICE + ":" + Action.DESTROY);
                            put(Action.START, RbacResource.SERVICE + ":" + Action.START);
                            put(Action.STOP, RbacResource.SERVICE + ":" + Action.STOP);
                        }
                    });
                    put(RbacResource.APPLICATION, new HashMap<String, String>() {
                        private static final long serialVersionUID = -7748529813129645198L;

                        {
                            put(Action.CREATE, RbacResource.APPLICATION + ":" + Action.CREATE);
                            put(Action.VIEW, RbacResource.APPLICATION + ":" + Action.VIEW);
                            put(Action.UPDATE, RbacResource.APPLICATION + ":" + Action.UPDATE);
                            put(Action.DESTROY, RbacResource.APPLICATION + ":" + Action.DESTROY);
                            put(Action.START, RbacResource.APPLICATION + ":" + Action.START);
                            put(Action.STOP, RbacResource.APPLICATION + ":" + Action.STOP);
                        }
                    });
                    put(RbacResource.CLUSTER, new HashMap<String, String>() {
                        private static final long serialVersionUID = -2475573399452185804L;

                        {
                            put(Action.CREATE, RbacResource.CLUSTER + ":" + Action.CREATE);
                            put(Action.VIEW, RbacResource.CLUSTER + ":" + Action.VIEW);
                            put(Action.UPDATE, RbacResource.CLUSTER + ":" + Action.UPDATE);
                            put(Action.DESTROY, RbacResource.CLUSTER + ":" + Action.DESTROY);
                            put(Action.CLEAR, RbacResource.CLUSTER + ":" + Action.CLEAR);
                            put(Action.DEPLOY, RbacResource.CLUSTER + ":" + Action.DEPLOY);
                        }
                    });
                    put(RbacResource.CLUSTER_NODE, new HashMap<String, String>() {
                        private static final long serialVersionUID = 7402692308594091443L;

                        {
                            put(Action.CREATE, RbacResource.CLUSTER_NODE + ":" + Action.CREATE);
                            put(Action.VIEW, RbacResource.CLUSTER_NODE + ":" + Action.VIEW);
                            put(Action.UPDATE, RbacResource.CLUSTER_NODE + ":" + Action.UPDATE);
                            put(Action.DESTROY, RbacResource.CLUSTER_NODE + ":" + Action.DESTROY);
                            put(Action.CLEAR, RbacResource.CLUSTER_NODE + ":" + Action.CLEAR);
                            put(Action.DEPLOY, RbacResource.CLUSTER_NODE + ":" + Action.DEPLOY);
                            put(Action.START, RbacResource.CLUSTER_NODE + ":" + Action.START);
                            put(Action.STOP, RbacResource.CLUSTER_NODE + ":" + Action.STOP);
                        }
                    });
                    put(RbacResource.LB, new HashMap<String, String>() {
                        private static final long serialVersionUID = 6121837456859719286L;

                        {
                            put(Action.CREATE, RbacResource.LB + ":" + Action.CREATE);
                            put(Action.VIEW, RbacResource.LB + ":" + Action.VIEW);
                            put(Action.DESTROY, RbacResource.LB + ":" + Action.DESTROY);
                            put(Action.BIND, RbacResource.LB + ":" + Action.BIND);
                            put(Action.RELEASE, RbacResource.LB + ":" + Action.RELEASE);
                            put(Action.UPDATE, RbacResource.LB + ":" + Action.UPDATE);
                            put(Action.SET_DOMAIN_SUFFIX, RbacResource.LB + ":" + Action.SET_DOMAIN_SUFFIX);
                        }
                    });
                    put(RbacResource.IP, new HashMap<String, String>() {
                        private static final long serialVersionUID = 75316488569522235L;

                        {
                            put(Action.CREATE, RbacResource.IP + ":" + Action.CREATE);
                            put(Action.VIEW, RbacResource.IP + ":" + Action.VIEW);
                            put(Action.DESTROY, RbacResource.IP + ":" + Action.DESTROY);
                            put(Action.BIND, RbacResource.IP + ":" + Action.BIND);
                            put(Action.RELEASE, RbacResource.IP + ":" + Action.RELEASE);
                        }
                    });
                    put(RbacResource.EVENT, new HashMap<String, String>() {
                        private static final long serialVersionUID = -3274741291049415806L;

                        {
                            put(Action.VIEW, RbacResource.EVENT + ":" + Action.VIEW);
                        }
                    });
                    put(RbacResource.VOLUME, new HashMap<String, String>() {
                        private static final long serialVersionUID = -6870187816162337836L;

                        {
                            put(Action.CREATE, RbacResource.VOLUME + ":" + Action.CREATE);
                            put(Action.VIEW, RbacResource.VOLUME + ":" + Action.VIEW);
                            put(Action.DESTROY, RbacResource.VOLUME + ":" + Action.DESTROY);
                            put(Action.MOUNT, RbacResource.VOLUME + ":" + Action.MOUNT);
                        }
                    });
                    put(RbacResource.SNAPSHOT, new HashMap<String, String>() {
                        private static final long serialVersionUID = 3831718082217752862L;

                        {
                            put(Action.CREATE, RbacResource.SNAPSHOT + ":" + Action.CREATE);
                            put(Action.VIEW, RbacResource.SNAPSHOT + ":" + Action.VIEW);
                            put(Action.DESTROY, RbacResource.SNAPSHOT + ":" + Action.DESTROY);
                        }
                    });
                    put(RbacResource.PRIVATE_REGISTRY, new HashMap<String, String>() {
                        private static final long serialVersionUID = -6326725170784168981L;

                        {
                            put(Action.CREATE, RbacResource.PRIVATE_REGISTRY + ":" + Action.CREATE);
                            put(Action.VIEW, RbacResource.PRIVATE_REGISTRY + ":" + Action.VIEW);
                            put(Action.DESTROY, RbacResource.PRIVATE_REGISTRY + ":" + Action.DESTROY);
                        }
                    });
                    put(RbacResource.PRIVATE_REGISTRY_PROJECT, new HashMap<String, String>() {
                        private static final long serialVersionUID = -232158991436719231L;

                        {
                            put(Action.CREATE, RbacResource.PRIVATE_REGISTRY_PROJECT + ":" + Action.CREATE);
                            put(Action.VIEW, RbacResource.PRIVATE_REGISTRY_PROJECT + ":" + Action.VIEW);
                            put(Action.DESTROY, RbacResource.PRIVATE_REGISTRY_PROJECT + ":" + Action.DESTROY);
                        }
                    });
                    put(RbacResource.PRIVATE_REGISTRY_REPO, new HashMap<String, String>() {
                        private static final long serialVersionUID = -2260306148929685532L;

                        {
                            put(Action.CREATE, RbacResource.PRIVATE_REGISTRY_REPO + ":" + Action.CREATE);
                            put(Action.VIEW, RbacResource.PRIVATE_REGISTRY_REPO + ":" + Action.VIEW);
                            put(Action.UPDATE, RbacResource.PRIVATE_REGISTRY_REPO + ":" + Action.UPDATE);
                            put(Action.DESTROY, RbacResource.PRIVATE_REGISTRY_REPO + ":" + Action.DESTROY);
                            put(Action.PUSH, RbacResource.PRIVATE_REGISTRY_REPO + ":" + Action.PUSH);
                            put(Action.PULL, RbacResource.PRIVATE_REGISTRY_REPO + ":" + Action.PULL);
                        }
                    });
                    put(RbacResource.CMP_CLOUD_ACCOUNT, new HashMap<String, String>() {
                        private static final long serialVersionUID = -6601886255624981293L;

                        {
                            put(Action.CREATE, RbacResource.CMP_CLOUD_ACCOUNT + ":" + Action.CREATE);
                            put(Action.VIEW, RbacResource.CMP_CLOUD_ACCOUNT + ":" + Action.VIEW);
                            put(Action.UPDATE, RbacResource.CMP_CLOUD_ACCOUNT + ":" + Action.UPDATE);
                            put(Action.DESTROY, RbacResource.CMP_CLOUD_ACCOUNT + ":" + Action.DESTROY);
                            put(Action.SYNC, RbacResource.CMP_CLOUD_ACCOUNT + ":" + Action.SYNC);
                        }
                    });
                    put(RbacResource.CLOUD_INSTANCE, new HashMap<String, String>() {
                        private static final long serialVersionUID = -566670898905938983L;

                        {
                            put(Action.CREATE, RbacResource.CLOUD_INSTANCE + ":" + Action.CREATE);
                            put(Action.VIEW, RbacResource.CLOUD_INSTANCE + ":" + Action.VIEW);
                        }
                    });
                    put(RbacResource.DASHBOARD, new HashMap<String, String>() {
                        private static final long serialVersionUID = -6561737408001025667L;

                        {
                            put(Action.CREATE, RbacResource.DASHBOARD + ":" + Action.CREATE);
                            put(Action.VIEW, RbacResource.DASHBOARD + ":" + Action.VIEW);
                            put(Action.UPDATE, RbacResource.DASHBOARD + ":" + Action.UPDATE);
                            put(Action.DESTROY, RbacResource.DASHBOARD + ":" + Action.DESTROY);
                        }
                    });
                    put(RbacResource.DASHBOARD_PANEL, new HashMap<String, String>() {
                        private static final long serialVersionUID = -2094384434839103826L;

                        {
                            put(Action.CREATE, RbacResource.DASHBOARD_PANEL + ":" + Action.CREATE);
                            put(Action.VIEW, RbacResource.DASHBOARD_PANEL + ":" + Action.VIEW);
                            put(Action.UPDATE, RbacResource.DASHBOARD_PANEL + ":" + Action.UPDATE);
                            put(Action.DESTROY, RbacResource.DASHBOARD_PANEL + ":" + Action.DESTROY);
                        }
                    });
                    put(RbacResource.CONFIG, new HashMap<String, String>() {
                        private static final long serialVersionUID = -385387948385982828L;

                        {
                            put(Action.CREATE, RbacResource.CONFIG + ":" + Action.CREATE);
                            put(Action.VIEW, RbacResource.CONFIG + ":" + Action.VIEW);
                            put(Action.UPDATE, RbacResource.CONFIG + ":" + Action.UPDATE);
                            put(Action.DESTROY, RbacResource.CONFIG + ":" + Action.DESTROY);
                        }
                    });
                    put(RbacResource.PRIVATE_BUILD_CONFIG, new HashMap<String, String>() {
                        private static final long serialVersionUID = 1916171977807129019L;

                        {
                            put(Action.CREATE, RbacResource.PRIVATE_BUILD_CONFIG + ":" + Action.CREATE);
                            put(Action.VIEW, RbacResource.PRIVATE_BUILD_CONFIG + ":" + Action.VIEW);
                            put(Action.UPDATE, RbacResource.PRIVATE_BUILD_CONFIG + ":" + Action.UPDATE);
                            put(Action.DESTROY, RbacResource.PRIVATE_BUILD_CONFIG + ":" + Action.DESTROY);
                            put(Action.TRIGGER, RbacResource.PRIVATE_BUILD_CONFIG + ":" + Action.TRIGGER);
                        }
                    });
                    put(RbacResource.PRIVATE_BUILD, new HashMap<String, String>() {
                        private static final long serialVersionUID = -6957381792310331894L;

                        {
                            put(Action.VIEW, RbacResource.PRIVATE_BUILD + ":" + Action.VIEW);
                            put(Action.DESTROY, RbacResource.PRIVATE_BUILD + ":" + Action.DESTROY);
                            put(Action.UPDATE, RbacResource.PRIVATE_BUILD + ":" + Action.UPDATE);
                        }
                    });
                    put(RbacResource.SYNC_REGISTRY_CONFIG, new HashMap<String, String>() {
                        private static final long serialVersionUID = 6542963651174746520L;

                        {
                            put(Action.CREATE, RbacResource.SYNC_REGISTRY_CONFIG + ":" + Action.CREATE);
                            put(Action.VIEW, RbacResource.SYNC_REGISTRY_CONFIG + ":" + Action.VIEW);
                            put(Action.UPDATE, RbacResource.SYNC_REGISTRY_CONFIG + ":" + Action.UPDATE);
                            put(Action.DESTROY, RbacResource.SYNC_REGISTRY_CONFIG + ":" + Action.DESTROY);
                            put(Action.TRIGGER, RbacResource.SYNC_REGISTRY_CONFIG + ":" + Action.TRIGGER);
                        }
                    });
                    put(RbacResource.SYNC_HISTORY, new HashMap<String, String>() {
                        private static final long serialVersionUID = -9075471773067563431L;

                        {
                            put(Action.VIEW, RbacResource.SYNC_HISTORY + ":" + Action.VIEW);
                            put(Action.UPDATE, RbacResource.SYNC_HISTORY + ":" + Action.UPDATE);
                            put(Action.DESTROY, RbacResource.SYNC_HISTORY + ":" + Action.DESTROY);
                        }
                    });
                    put(RbacResource.PIPELINE, new HashMap<String, String>() {
                        private static final long serialVersionUID = 5532113284583882643L;

                        {
                            put(Action.CREATE, RbacResource.PIPELINE + ":" + Action.CREATE);
                            put(Action.VIEW, RbacResource.PIPELINE + ":" + Action.VIEW);
                            put(Action.UPDATE, RbacResource.PIPELINE + ":" + Action.UPDATE);
                            put(Action.DESTROY, RbacResource.PIPELINE + ":" + Action.DESTROY);
                            put(Action.TRIGGER, RbacResource.PIPELINE + ":" + Action.TRIGGER);
                        }
                    });
                    put(RbacResource.PIPELINE_HISTORY, new HashMap<String, String>() {
                        private static final long serialVersionUID = 6434064816800829479L;

                        {
                            put(Action.VIEW, RbacResource.PIPELINE_HISTORY + ":" + Action.VIEW);
                            put(Action.DESTROY, RbacResource.PIPELINE_HISTORY + ":" + Action.DESTROY);
                            put(Action.STOP, RbacResource.PIPELINE_HISTORY + ":" + Action.STOP);
                        }
                    });
                    put(RbacResource.PIPELINE_TASK, new HashMap<String, String>() {
                        private static final long serialVersionUID = -6579820945764351081L;

                        {
                            put(Action.CONTROL, RbacResource.PIPELINE_TASK + ":" + Action.CONTROL);
                        }
                    });
                    put(RbacResource.SPACE, new HashMap<String, String>() {
                        private static final long serialVersionUID = 1349166617396004102L;

                        {
                            put(Action.CREATE, RbacResource.SPACE + ":" + Action.CREATE);
                            put(Action.VIEW, RbacResource.SPACE + ":" + Action.VIEW);
                            put(Action.UPDATE, RbacResource.SPACE + ":" + Action.UPDATE);
                            put(Action.DESTROY, RbacResource.SPACE + ":" + Action.DESTROY);
                            put(Action.CONSUME, RbacResource.SPACE + ":" + Action.CONSUME);
                        }
                    });
                    put(RbacResource.APPLICATION_TEMPLATE, new HashMap<String, String>() {
                        private static final long serialVersionUID = 8854161063552558699L;

                        {
                            put(Action.CREATE, RbacResource.APPLICATION_TEMPLATE + ":" + Action.CREATE);
                            put(Action.VIEW, RbacResource.APPLICATION_TEMPLATE + ":" + Action.VIEW);
                            put(Action.UPDATE, RbacResource.APPLICATION_TEMPLATE + ":" + Action.UPDATE);
                            put(Action.DESTROY, RbacResource.APPLICATION_TEMPLATE + ":" + Action.DESTROY);
                        }
                    });
                    put(RbacResource.ENVFILE, new HashMap<String, String>() {
                        private static final long serialVersionUID = 7890268547080019152L;

                        {
                            put(Action.CREATE, RbacResource.ENVFILE + ":" + Action.CREATE);
                            put(Action.VIEW, RbacResource.ENVFILE + ":" + Action.VIEW);
                            put(Action.UPDATE, RbacResource.ENVFILE + ":" + Action.UPDATE);
                            put(Action.DESTROY, RbacResource.ENVFILE + ":" + Action.DESTROY);
                        }
                    });
                    put(RbacResource.NOTIFICATION, new HashMap<String, String>() {
                        private static final long serialVersionUID = -5023656017494243245L;

                        {
                            put(Action.CREATE, RbacResource.NOTIFICATION + ":" + Action.CREATE);
                            put(Action.VIEW, RbacResource.NOTIFICATION + ":" + Action.VIEW);
                            put(Action.UPDATE, RbacResource.NOTIFICATION + ":" + Action.UPDATE);
                            put(Action.DESTROY, RbacResource.NOTIFICATION + ":" + Action.DESTROY);
                        }
                    });
                    put(RbacResource.ALARM, new HashMap<String, String>() {
                        private static final long serialVersionUID = 4806314806558921103L;

                        {
                            put(Action.CREATE, RbacResource.ALARM + ":" + Action.CREATE);
                            put(Action.VIEW, RbacResource.ALARM + ":" + Action.VIEW);
                            put(Action.UPDATE, RbacResource.ALARM + ":" + Action.UPDATE);
                            put(Action.DESTROY, RbacResource.ALARM + ":" + Action.DESTROY);
                        }
                    });
                    put(RbacResource.ROLE, new HashMap<String, String>() {
                        private static final long serialVersionUID = 5687882212203505526L;

                        {
                            put(Action.CREATE, RbacResource.ROLE + ":" + Action.CREATE);
                            put(Action.VIEW, RbacResource.ROLE + ":" + Action.VIEW);
                            put(Action.UPDATE, RbacResource.ROLE + ":" + Action.UPDATE);
                            put(Action.DESTROY, RbacResource.ROLE + ":" + Action.DESTROY);
                            put(Action.ASSIGN, RbacResource.ROLE + ":" + Action.ASSIGN);
                            put(Action.REVOKE, RbacResource.ROLE + ":" + Action.REVOKE);
                        }
                    });
                    put(RbacResource.SUBACCOUNT, new HashMap<String, String>() {
                        private static final long serialVersionUID = 5945867875772682699L;

                        {
                            put(Action.CREATE, RbacResource.SUBACCOUNT + ":" + Action.CREATE);
                            put(Action.VIEW, RbacResource.SUBACCOUNT + ":" + Action.VIEW);
                            put(Action.UPDATE_PASSWORD, RbacResource.SUBACCOUNT + ":" + Action.UPDATE_PASSWORD);
                            put(Action.DESTROY, RbacResource.SUBACCOUNT + ":" + Action.DESTROY);
                        }
                    });
                    put(RbacResource.ORGANIZATION, new HashMap<String, String>() {
                        private static final long serialVersionUID = 4498670966255011021L;

                        {
                            put(Action.VIEW, RbacResource.ORGANIZATION + ":" + Action.VIEW);
                            put(Action.UPDATE, RbacResource.ORGANIZATION + ":" + Action.UPDATE);
                            put(Action.SYNC_USERS, RbacResource.ORGANIZATION + ":" + Action.SYNC_USERS);
                        }
                    });
                    put(RbacResource.SUBNET, new HashMap<String, String>() {
                        private static final long serialVersionUID = -3941260542938602688L;

                        {
                            put(Action.CREATE, RbacResource.SUBNET + ":" + Action.CREATE);
                            put(Action.VIEW, RbacResource.SUBNET + ":" + Action.VIEW);
                            put(Action.UPDATE, RbacResource.SUBNET + ":" + Action.UPDATE);
                            put(Action.DESTROY, RbacResource.SUBNET + ":" + Action.DESTROY);
                            put(Action.CLEAR, RbacResource.SUBNET + ":" + Action.CLEAR);
                        }
                    });
                    put(RbacResource.PRIVATE_IP, new HashMap<String, String>() {
                        private static final long serialVersionUID = -3395448544993784230L;

                        {
                            put(Action.CREATE, RbacResource.PRIVATE_IP + ":" + Action.CREATE);
                            put(Action.VIEW, RbacResource.PRIVATE_IP + ":" + Action.VIEW);
                            put(Action.DESTROY, RbacResource.PRIVATE_IP + ":" + Action.DESTROY);
                        }
                    });
                    put(RbacResource.PROJECT, new HashMap<String, String>() {
                        private static final long serialVersionUID = -4296550325379440684L;

                        {
                            put(Action.CREATE, RbacResource.PROJECT + ":" + Action.CREATE);
                            put(Action.VIEW, RbacResource.PROJECT + ":" + Action.VIEW);
                            put(Action.DESTROY, RbacResource.PROJECT + ":" + Action.DESTROY);
                        }
                    });
                    put(RbacResource.PROJECT_TEMPLATE, new HashMap<String, String>() {
                        private static final long serialVersionUID = -312810501236166496L;

                        {
                            put(Action.VIEW, RbacResource.PROJECT_TEMPLATE + ":" + Action.VIEW);
                            put(Action.CREATE, RbacResource.PROJECT_TEMPLATE + ":" + Action.CREATE);
                            put(Action.DESTROY, RbacResource.PROJECT_TEMPLATE + ":" + Action.DESTROY);
                        }
                    });
                    put(RbacResource.CERTIFICATE, new HashMap<String, String>() {
                        private static final long serialVersionUID = -1630708834962329559L;

                        {
                            put(Action.VIEW, RbacResource.CERTIFICATE + ":" + Action.VIEW);
                            put(Action.CREATE, RbacResource.CERTIFICATE + ":" + Action.CREATE);
                            put(Action.BIND, RbacResource.CERTIFICATE + ":" + Action.BIND);
                            put(Action.RELEASE, RbacResource.CERTIFICATE + ":" + Action.RELEASE);
                            put(Action.DESTROY, RbacResource.CERTIFICATE + ":" + Action.DESTROY);
                        }
                    });
                    put(RbacResource.JOB_CONFIG, new HashMap<String, String>() {
                        private static final long serialVersionUID = 2889441747285923245L;

                        {
                            put(Action.CREATE, RbacResource.JOB_CONFIG + ":" + Action.CREATE);
                            put(Action.VIEW, RbacResource.JOB_CONFIG + ":" + Action.VIEW);
                            put(Action.DESTROY, RbacResource.JOB_CONFIG + ":" + Action.DESTROY);
                            put(Action.UPDATE, RbacResource.JOB_CONFIG + ":" + Action.UPDATE);
                            put(Action.TRIGGER, RbacResource.JOB_CONFIG + ":" + Action.TRIGGER);
                        }
                    });
                    put(RbacResource.JOB_HISTORY, new HashMap<String, String>() {
                        private static final long serialVersionUID = -5797141111677397884L;

                        {
                            put(Action.VIEW, RbacResource.JOB_HISTORY + ":" + Action.VIEW);
                            put(Action.DESTROY, RbacResource.JOB_HISTORY + ":" + Action.DESTROY);
                            put(Action.UPDATE, RbacResource.JOB_HISTORY + ":" + Action.UPDATE);
                            put(Action.STOP, RbacResource.JOB_HISTORY + ":" + Action.STOP);
                        }
                    });
                }
            });

    public static class Action {
        public static final String CREATE = "create";
        public static final String VIEW = "view";
        public static final String UPDATE = "update";
        public static final String DESTROY = "delete";
        public static final String START = "start";
        public static final String STOP = "stop";
        public static final String CLEAR = "clear";
        public static final String DEPLOY = "deploy";
        public static final String BIND = "bind";
        public static final String RELEASE = "release";
        public static final String SET_DOMAIN_SUFFIX = "set_domain_suffix";
        public static final String MOUNT = "mount";
        public static final String PUSH = "push";
        public static final String PULL = "pull";
        public static final String SYNC = "sync";
        public static final String TRIGGER = "trigger";
        public static final String CONTROL = "control";
        public static final String CONSUME = "consume";
        public static final String ASSIGN = "assign";
        public static final String REVOKE = "revoke";
        public static final String SYNC_USERS = "sync_users";
        public static final String UPDATE_PASSWORD = "update_password";
    }

    public static class ConstraintKey {
        public static final String KEY_CLUSTER = "cluster_name";
        public static final String KEY_CLUSTER_NAME = "cluster_name";
        public static final String KEY_REGION = "region";
        public static final String KEY_REGION_NAME = "region_name";
        public static final String KEY_SPACE = "space_name";
        public static final String KEY_KNAMESPACE_NAME = "knamespace_name";
        public static final String KEY_PROJECT = "project_name";
        public static final String KEY_APPLICATION = "application_name";
        public static final String KEY_REGISTRY = "registry_name";
        public static final String KEY_PRI_REGISTRY = "priv_registry";
        public static final String KEY_REGISTRY_PROJECT = "registry_project";
        public static final String KEY_CLOUD_ACCOUNT = "cloud_account_name";
        public static final String KEY_DASHBOARD = "dashboard_name";
        public static final String KEY_BUILD_CONFIG = "build_config_name";
        public static final String KEY_SYNC_CONFIG = "sync_config_name";
        public static final String KEY_PIPELINE = "pipeline_config_name";
        public static final String KEY_PIPELINE_TASK_TYPE = "pipeline_task_type";
        public static final String KEY_SUBACCOUNT = "subaccount_username";
        public static final String KEY_SERVICE = "service_name";
        public static final String KEY_CLUSTER_NODE = "cluster_node_name";
        public static final String KEY_REPOSITORY_NAME = "repository_name";
        public static final String KEY_VOLUME_NAME = "volume_name";
        public static final String KEY_SUBNET_NAME = "subnet_name";
        public static final String KEY_LOADBALANCER_NAME = "load_balancer_name";
        public static final String KEY_JOB_CONFIG = "job_config_name";
    }

    public static final Map<String, String> CONSTRAINT_KEY_MAP = Collections
            .unmodifiableMap(new HashMap<String, String>() {
                private static final long serialVersionUID = -6003388516488993113L;

                {
                    put(ConstraintKey.KEY_CLUSTER, "res,cluster");
                    put(ConstraintKey.KEY_REGION, "res,cluster");
                    put(ConstraintKey.KEY_REGION_NAME, "res,cluster");
                    put(ConstraintKey.KEY_SPACE, "res,space");
                    put(ConstraintKey.KEY_PROJECT, "res,project");
                    put(ConstraintKey.KEY_APPLICATION, "res,application");
                    put(ConstraintKey.KEY_REGISTRY, "res,registry");
                    put(ConstraintKey.KEY_REGISTRY_PROJECT, "res,registry_project");
                    put(ConstraintKey.KEY_CLOUD_ACCOUNT, "res,cloud_account");
                    put(ConstraintKey.KEY_DASHBOARD, "res,dashboard");
                    put(ConstraintKey.KEY_BUILD_CONFIG, "res,build_config");
                    put(ConstraintKey.KEY_SYNC_CONFIG, "res,sync_config");
                    put(ConstraintKey.KEY_PIPELINE, "res,pipeline_config");
                    put(ConstraintKey.KEY_PIPELINE_TASK_TYPE, "res,pipeline_task_type");
                    put(ConstraintKey.KEY_SUBACCOUNT, "res,subaccount");
                    put(ConstraintKey.KEY_REPOSITORY_NAME, "res,repository");
                    put(ConstraintKey.KEY_VOLUME_NAME, "res,volume");
                    put(ConstraintKey.KEY_SUBNET_NAME, "res,subnet");
                    put(ConstraintKey.KEY_LOADBALANCER_NAME, "res,load_balancer");
                    put(ConstraintKey.KEY_JOB_CONFIG, "res,job_config");
                }
            });

    public enum EventOperator {
        add(OP_LEVEL_TRIVIAL),
        create(OP_LEVEL_TRIVIAL),
        delete(OP_LEVEL_TRIVIAL),
        update(OP_LEVEL_ATTENTION),
        destroy(OP_LEVEL_ATTENTION),
        start(OP_LEVEL_TRIVIAL),
        stop(OP_LEVEL_ATTENTION),
        destroy_build(OP_LEVEL_ATTENTION),
        added(OP_LEVEL_TRIVIAL),
        left(OP_LEVEL_TRIVIAL),
        permission_grant(OP_LEVEL_TRIVIAL),
        permission_revoke(OP_LEVEL_TRIVIAL),
        remove(OP_LEVEL_ATTENTION),
        upgrade(OP_LEVEL_ATTENTION),
        trigger(OP_LEVEL_TRIVIAL);

        private final int level;

        EventOperator(int level) {
            this.level = level;
        }

        public int getOperationLevel() {
            return level;
        }
    }

    public interface ResourceColumn {
        String TYPE = "type";
        String UUID = "uuid";
        String NAME = "name";
        String NAMESPACE = "namespace";
        String SPACE_UUID = "space_uuid";
        String SPACE_NAME = "space_name";
        String PROJECT_UUID = "project_uuid";
        String PROJECT_NAME = "project_name";
    }
}
