package com.aspire.code.generator.service;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ICodeGenertorService
 * Author:   zhu.juwang
 * Date:     2019/4/28 17:17
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public interface ICodeGeneratorService {
    /**
     * 代码生成器
     * @param databaseUrl 数据库连接
     * @param databaseUser 数据库用户名
     * @param databasePwd 数据库密码
     * @param tableNames 需要生成的表名 多个以逗号分隔
     * @param packagePath 生成的包路径
     * @param localPath 生成到的本地路径
     * @return 生成的文件列表
     */
    List<String> generatorCode(String databaseUrl,
                               String databaseUser,
                               String databasePwd,
                               List<String> tableNames,
                               String packagePath,
                               String localPath);

    /**
     * 生成 CURD
     * @param databaseUrl 数据库连接
     * @param databaseUser 数据库用户名
     * @param databasePwd 数据库密码
     * @param tableName 需要生成的表名
     * @param operator 操作方法
     * @return 生成的文件列表
     */
    String generatorCURD(String databaseUrl, String databaseUser, String databasePwd, List<String> tableName, String operator);

    /**
     * 连接数据库
     * @param databaseUrl 数据库连接
     * @param databaseUser 数据库用户名
     * @param databasePwd 数据库密码
     */
    List<String> connection(String databaseUrl, String databaseUser, String databasePwd) throws Exception;
}
