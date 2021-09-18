package com.aspire.ums.cmdb.v3.screen.payload;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 描述：
* @author
* @date 2020-07-03 10:17:19
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbScreenProblemInfo {

    /**
     * ID
     */
    private String id;
    /**
     * 登录账号
     */
    private String loginName;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 一级部门
     */
    private String department1;
    /**
     * 二级部门
     */
    private String department2;
    /**
     * 用户电话
     */
    private String mobile;
    /**
     * 用户邮箱
     */
    private String email;
    /**
     * 标题
     */
    private String title;
    /**
     * 类型 建议 || 问题
     */
    private String type;
    /**
     * 分类
     */
    private String classify;
    /**
     * 问题描述
     */
    private String problemDesc;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private String createTime;
    /**
     * 状态 待处理 || 已处理 || 追问 || 完成
     */
    private String status;
    /**
     * 是否删除
     */
    private String isDelete;

    private List<CmdbScreenAnswerInfo> answerInfoList;
}