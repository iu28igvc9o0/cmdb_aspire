package com.aspire.ums.cmdb.report.playload;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: Cmdb31ProvinceTable
 * Author:   hangfang
 * Date:     2020/4/20
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Cmdb31ProvinceTable {
    /**
     * 表id
     */
    private String id;
    /**
     * 表归属(reource/yunwei)
     */
    private String resourceOwner;
    /**
     * 表名
     */
    private String name;
    /**
     * 在什么界面显示（update-填报，query-查询）
     */
    private String showPage;
    /**
     * 包含表
     */
    private String contain;
    /**
     * 是否合并一二级标题
     */
    private Integer enableMergeTitle;
    /**
     * 表顺序
     */
    private String sortIndex;
    /**
     *
     */
    private List<Cmdb31ProvinceReportSetting> settingList;

    private List<Cmdb31ProvinceReport> reportList;
}
