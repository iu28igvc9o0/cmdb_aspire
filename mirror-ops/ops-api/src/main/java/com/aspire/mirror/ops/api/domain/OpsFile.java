package com.aspire.mirror.ops.api.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.ops.api.domain
 * 类名称:    OpsFile.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2020/6/9 20:28
 * 版本:      v1.0
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OpsFile extends OpsGroupObject{
    public static final String FILE_VERSION_V1="v1";
    //直接使用
    public static final String FILE_TYPE_1 = "1";
    //拆分文件
    public static final String FILE_TYPE_2 = "2";
    //安全加密下载
    public static final String FILE_TYPE_3 = "3";
    //本地上传
    public static final String GENERATE_TYPE_1 = "1";
    //自动生成
    public static final String GENERATE_TYPE_2 = "2";
    public static final String AUTO_OUPUT_VERSION="stepInst_output_";
    public static final String AUTO_LOG_VERSION="pipelineInst_log_";
    // 汇聚结果，sftp目录
    public static final String INSTANCE_OUTPUT_DIR = "/step_instance_output_package/";

    // 汇聚结果，sftp目录
    /**
     * 文件ID
     */
    @JsonProperty("file_id")
    private Long fileId;
    /**
     * 文件名称
     */
    @JsonProperty("file_name")
    private String fileName;
    /**
     * 文件版本
     */
    @JsonProperty("file_version")
    private String fileVersion;
    /**
     * 文件类型 文件类型 1直接使用、2拆分文件、3安全下载文件
     */
    @JsonProperty("file_type")
    private String fileType;
    /**
     * 文件分类 1基线文件/2账号文件/3巡检报告文件/4汇总结果保存文件/5日志文件/6加密文件/9其他
     */
    @JsonProperty("file_class")
    private String fileClass;
    /**
     * 文件地址
     */
    @JsonProperty("file_path")
    private String filePath;
    /**
     * 文件描述
     */
    @JsonProperty("file_desc")
    private String fileDesc;
    /**
     * 创建人
     */
    private String creater;

    /**
     * 创建时间
     */
    @JsonProperty("create_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改人
     */
    @JsonProperty("last_updater")
    private String lastUpdater;

    /**
     * 修改时间
     */
    @JsonProperty("last_update_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastUpdateTime;

    /**
     * 生成类型 1本地上传/2自动生成
     */
    @JsonProperty("file_generate_type")
    private String fileGenerateType;

    @JsonProperty("process_rule")
    private String processRule;

    /**
     * 关联id(巡检报告文件为巡检报告id,汇总结果/日志文件则是实例id)
     */
    @JsonProperty("relation_id")
    private String relationId;

    /**
     * 扩展一个关联内容名称描述
     */
    @JsonProperty("relation_name")
    private String relationName;
}
