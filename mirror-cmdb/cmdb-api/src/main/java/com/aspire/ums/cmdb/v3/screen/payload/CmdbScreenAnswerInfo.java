package com.aspire.ums.cmdb.v3.screen.payload;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 描述：
* @author
* @date 2020-07-03 10:17:18
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbScreenAnswerInfo {

    /**
     * ID
     */
    private String id;
    /**
     * 问题ID
     */
    private String problemId;
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
     * 回答内容
     */
    private String answerContent;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private String createTime;
    /*
    *  是否是管理员
    * */
    private String isAdmin;
    /**
     * 是否删除
     */
    private String isDelete;
}