package com.aspire.ums.cmdb.v3.screen.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @projectName: CmdbScreenProblemInfoRequest
 * @description: 类
 * @author: luowenbo
 * @create: 2020-07-03 14:52
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CmdbScreenProblemInfoRequest {
    /*
    *  用户登录账号
    * */
    private String loginName;
    /*
    *  是否是管理员
    * */
    private Boolean isAdmin;
    /*
    * 标题（模糊查询）
    * */
    private String title;
    /*
    *  类型
    * */
    private String type;
    /*
    *  分类
    * */
    private String classify;
    /*
    * 日期
    * */
    private String createTime;
    private Integer pageNo;
    private Integer pageSize;
}
