package com.aspire.ums.cmdb.helper;

import com.aspire.ums.cmdb.common.Constants;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.common.throwable.CmdbRuntimeException;
import com.aspire.ums.cmdb.config.RequestAuthContext;
import com.aspire.ums.cmdb.module.payload.Module;
import com.aspire.ums.cmdb.mybatis.MybatisExecSql;
import com.aspire.ums.cmdb.mybatis.MybatisScriptHelper;
import com.aspire.ums.cmdb.sqlManage.CmdbSqlManage;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.v2.module.service.ModuleService;
import com.aspire.ums.cmdb.v3.config.payload.CmdbConfig;
import com.aspire.ums.cmdb.v3.config.service.ICmdbConfigService;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCatalog;
import com.aspire.ums.cmdb.v3.module.service.ICmdbV3ModuleCatalogService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.map.LinkedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: JDBCHelper
 * Author:   zhu.juwang
 * Date:     2020/6/16 14:15
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Slf4j
@Component
public class JDBCHelper {
    /**
     * JDBC数据源
     */
    @Autowired
    private DataSource dataSource;
    private Connection connection;
    private final static String IF_TEMPLATE = "<if test=\"{filed} != null and {filed} != ''\"> %s </if>";
    private final static String IF_LIST_TEMPLATE = "<if test=\"{filed} != null \"> %s </if>";
    private final static Pattern PERMISSION_PATTERN = Pattern.compile("\\[<.*?\\>]");

    @Autowired
    private ICmdbConfigService configService;
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private ICmdbV3ModuleCatalogService catalogService;

    Map<String, Map<String, String>> getRightMap() {
        // 权限字段对应表
        Map<String, Map<String, String>> rightMap = new HashMap<String, Map<String, String>>();
        log.info("Begin init module_right_grant. Load cmdb_config key -> module_right_grant");
        CmdbConfig rightConfig = configService.getConfigByCode("module_right_grant");
        if (rightConfig !=null && StringUtils.isNotEmpty(rightConfig.getConfigValue())) {
            JSONObject rightObject = JSONObject.fromObject(rightConfig.getConfigValue());
            Iterator iterator = rightObject.keys();
            while (iterator.hasNext()) {
                String moduleKey = iterator.next().toString();
                JSONObject moduleRight = rightObject.getJSONObject(moduleKey);
                Iterator rightIterator = moduleRight.keys();
                Map<String, String> mapper = new HashMap<>();
                while (rightIterator.hasNext()) {
                    String rightKey = rightIterator.next().toString();
                    String value = moduleRight.getString(rightKey);
                    mapper.put(rightKey, value);
                }
                if (MapUtils.isNotEmpty(mapper)) {
                    rightMap.put(moduleKey, mapper);
                }
            }
        }
        return rightMap;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = dataSource.getConnection();
            }
        } catch (SQLException e) {
            log.error("获取连接池失败.", e);
            throw new CmdbRuntimeException("获取连接池失败:" + e.getMessage());
        }
        return connection;
    }

    /**
     * 查询数据列表
     * @param sqlSource
     * @param parameters
     * @return
     */
    public <T> List<Map<String, T>> getQueryList(CmdbSqlManage sqlSource, String whereString, String orderString, String limitString, Map<String, Object> parameters) {
        String querySql = sqlSource.getChartSql();
        StringBuilder finalSqlBuilder = new StringBuilder();
        boolean isAuthentication = null == sqlSource.getNeedAuth() ? false : sqlSource.getNeedAuth().equals(1);
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            querySql = querySql.replace(Constants.INNER_WHERE_STRING, StringUtils.isNotEmpty(whereString) ? whereString : "");
            querySql = querySql.replace(Constants.INNER_ORDER_STRING, StringUtils.isNotEmpty(orderString) ? orderString : "");
            querySql = querySql.replace(Constants.INNER_LIMIT_STRING, StringUtils.isNotEmpty(limitString) ? limitString : "");
            sqlSource.setChartSql(querySql);
            if (isAuthentication) {
                // todo 追加权限代码
                querySql = this.grantAuthentication(sqlSource);
            } else {
                // 如果有内置权限标识 则直接过滤
                querySql = querySql.replace(Constants.INNER_AUTH_STRING, "");
            }
            finalSqlBuilder.append(querySql);
            // 解析 执行查询
            preparedStatement = this.getQueryPreparedStatement(finalSqlBuilder.toString(), parameters);
            resultSet = preparedStatement.executeQuery();
            List<Map<String,T>> returnList = new LinkedList<>();
            ResultSetMetaData md = resultSet.getMetaData();
            int columnCount = md.getColumnCount();
            while (resultSet.next()) {
                Map<String, T> rowData = new LinkedMap();
                for (int i = 1; i <= columnCount; i++) {
                    T value = null;
                    if (resultSet.getObject(i) != null) {
                        value = (T) resultSet.getObject(i);
                    }
                    rowData.put(md.getColumnLabel(i), value);
                }
                returnList.add(rowData);
            }
            return returnList;
        } catch (SQLException e) {
            log.error("Query SQL Error -> {}", finalSqlBuilder);
            throw new CmdbRuntimeException("Query SQL Error, Cause:" + e.getLocalizedMessage());
        } finally {
            this.closePreparedStatement(preparedStatement);
            this.closeResultSet(resultSet);
            if (this.connection != null) {
                this.closeConnection();
            }
        }
    }


    /**
     * 查询数据列表
     * @param sqlSource
     * @param parameters
     * @return
     */
    public <T> List<T> getQueryFiled(CmdbSqlManage sqlSource, String whereString, String orderString, String limitString, Map<String, Object> parameters) {
        String querySql = sqlSource.getChartSql();
        StringBuilder finalSqlBuilder = new StringBuilder();
        boolean isAuthentication = sqlSource.getNeedAuth() == 1;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            if (isAuthentication) {
                // todo 追加权限代码
                querySql = this.grantAuthentication(sqlSource);
            } else {
                // 如果有内置权限标识 则直接过滤
                querySql = querySql.replace(Constants.INNER_AUTH_STRING, "");
            }
            querySql = querySql.replace(Constants.INNER_WHERE_STRING, StringUtils.isNotEmpty(whereString) ? whereString : "");
            querySql = querySql.replace(Constants.INNER_ORDER_STRING, StringUtils.isNotEmpty(orderString) ? orderString : "");
            querySql = querySql.replace(Constants.INNER_LIMIT_STRING, StringUtils.isNotEmpty(limitString) ? limitString : "");
            finalSqlBuilder.append(querySql);
            // 解析 执行查询
            preparedStatement = this.getQueryPreparedStatement(finalSqlBuilder.toString(), parameters);
            resultSet = preparedStatement.executeQuery();
            List<T> returnList = new LinkedList<>();
            ResultSetMetaData md = resultSet.getMetaData();
            int columnCount = md.getColumnCount();
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    if (resultSet.getObject(i) != null) {
                        returnList.add((T)resultSet.getObject(i));
                    }
                }
            }
            return returnList;
        } catch (SQLException e) {
            log.error("Query SQL Error -> {}", finalSqlBuilder);
            throw new CmdbRuntimeException("Query SQL Error, Cause:" + e.getLocalizedMessage());
        } finally {
            this.closePreparedStatement(preparedStatement);
            this.closeResultSet(resultSet);
            if (this.connection != null) {
                this.closeConnection();
            }
        }
    }

    /**
     * 查询数据对象
     * @param sqlSource 查询SQL
     * @param parameters 入参
     * @return
     */
    public <T> Map<String, T> getQueryMap(CmdbSqlManage sqlSource, String whereString, Map<String, Object> parameters) {
        String querySql = sqlSource.getChartSql();
        StringBuilder finalSqlBuilder = new StringBuilder();
        boolean isAuthentication = sqlSource.getNeedAuth() == 1;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            if (isAuthentication) {
                // todo 追加权限代码
                querySql = this.grantAuthentication(sqlSource);
            } else {
                // 如果有内置权限标识 则直接过滤
                querySql = querySql.replace(Constants.INNER_AUTH_STRING, "");
            }
            querySql = querySql.replace(Constants.INNER_WHERE_STRING, StringUtils.isNotEmpty(whereString) ? whereString : "");
            querySql = querySql.replace(Constants.INNER_ORDER_STRING, "");
            querySql = querySql.replace(Constants.INNER_LIMIT_STRING, "");
            finalSqlBuilder.append(querySql);
            // 解析 执行查询
            preparedStatement = this.getQueryPreparedStatement(finalSqlBuilder.toString(), parameters);
            resultSet = preparedStatement.executeQuery();
            Map<String,T> returnMap = new LinkedMap();
            ResultSetMetaData md = resultSet.getMetaData();
            int columnCount = md.getColumnCount();
            if (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    T value = null;
                    if (resultSet.getObject(i) != null) {
                        value = (T) resultSet.getObject(i);
                    }
                    returnMap.put(md.getColumnLabel(i), value);
                }
            }
            return returnMap;
        } catch (SQLException e) {
            log.error("Query SQL Error -> {}", finalSqlBuilder);
            throw new CmdbRuntimeException("Query SQL Error, Cause:" + e.getLocalizedMessage());
        } finally {
            this.closePreparedStatement(preparedStatement);
            this.closeResultSet(resultSet);
            if (this.connection != null) {
                this.closeConnection();
            }
        }
    }

    /**
     * 查询数量
     * @param sqlSource 查询SQL
     * @param parameters 入参
     * @return
     */
    public Integer getInt(CmdbSqlManage sqlSource, String whereString, Map<String, Object> parameters) {
        String querySql = sqlSource.getChartSql();
        StringBuilder finalSqlBuilder = new StringBuilder();
        boolean isAuthentication = sqlSource.getNeedAuth() == 1;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            querySql = querySql.replace(Constants.INNER_WHERE_STRING, StringUtils.isNotEmpty(whereString) ? whereString : "");
            querySql = querySql.replace(Constants.INNER_ORDER_STRING, "");
            querySql = querySql.replace(Constants.INNER_LIMIT_STRING, "");
            sqlSource.setChartSql(querySql);
            if (isAuthentication) {
                // todo 追加权限代码
                querySql = this.grantAuthentication(sqlSource);
            } else {
                // 如果有内置权限标识 则直接过滤
                querySql = querySql.replace(Constants.INNER_AUTH_STRING, "");
            }
            finalSqlBuilder.append("select count(1) from (").append(querySql).append(") t_x_m ");
            // 解析 执行查询
             preparedStatement = this.getQueryPreparedStatement(finalSqlBuilder.toString(), parameters);
             resultSet = preparedStatement.executeQuery();
            int count = 0;
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
            return count;
        } catch (SQLException e) {
            log.error("Query SQL Error -> {}", finalSqlBuilder);
            throw new CmdbRuntimeException("Query SQL Error, Cause:" + e.getLocalizedMessage());
        } finally {
            this.closePreparedStatement(preparedStatement);
//            this.closeResultSet(resultSet);
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                log.error("Close jdbc ResultSet error. Cause:", e);
            } finally {
                resultSet = null;
            }
            if (this.connection != null) {
                this.closeConnection();
            }
        }
    }

    /**
     * 查询数据
     * @param sqlSource
     * @param parameters
     * @return
     */
    public <T> Result<Map<String, T>> getPagedList(CmdbSqlManage sqlSource, String whereString, String orderString, String limitString, Map<String, Object> parameters, Integer currentPage, Integer pageSize) {
        StringBuilder finalSqlBuilder = new StringBuilder();
        String querySql = sqlSource.getChartSql();
        boolean isAuthentication = sqlSource.getNeedAuth() == 1;
        try {
            if (isAuthentication) {
                // todo 追加权限代码
                querySql = this.grantAuthentication(sqlSource);
            } else {
                // 如果有内置权限标识 则直接过滤
                querySql = querySql.replace(Constants.INNER_AUTH_STRING, "");
            }
            finalSqlBuilder.append("select * from (").append(querySql).append(") t_x_m ");
            if (currentPage != null && pageSize != null) {
                limitString = " limit " + ((currentPage - 1) * pageSize) + ", " + pageSize;
            }
            sqlSource.setChartSql(finalSqlBuilder.toString());
            List<Map<String, T>> list = getQueryList(sqlSource, whereString, orderString, limitString, parameters);
            finalSqlBuilder = new StringBuilder();
            sqlSource.setChartSql(querySql);
            Integer totalCount = getInt(sqlSource, whereString, parameters);
            return new Result<>(totalCount, list);
        } catch (Exception e) {
            log.error("Query SQL Error -> {}", finalSqlBuilder);
            throw new CmdbRuntimeException("Query SQL Error, Cause:" + e.getLocalizedMessage());
        } finally {
            if (this.connection != null) {
                this.closeConnection();
            }
        }
    }

    /**
     * 执行修改语句
     * @param cmdbSqlManage
     * @param parameters
     */
    public Integer executeUpdate(CmdbSqlManage cmdbSqlManage, Map<String, Object> parameters) {
        StringBuilder finalSqlBuilder = new StringBuilder();
        PreparedStatement preparedStatement = null;
        try {
            String executeSql = cmdbSqlManage.getChartSql();
            if (cmdbSqlManage.getNeedAuth() == 1) {
                // todo 追加权限代码
                executeSql = this.grantAuthentication(cmdbSqlManage);
            } else {
                // 如果有内置权限标识 则直接过滤
                executeSql = executeSql.replace(Constants.INNER_AUTH_STRING, "");
            }
            preparedStatement = getQueryPreparedStatement(executeSql, parameters);
            return preparedStatement.executeUpdate();
        } catch (Exception e) {
            log.error("Query SQL Error -> {}", finalSqlBuilder);
            throw new CmdbRuntimeException("Query SQL Error, Cause:" + e.getLocalizedMessage());
        } finally {
            this.closePreparedStatement(preparedStatement);
        }
    }

    /**
     * 获取 PreparedStatement 查询对象
     * @param querySql 查询SQL
     * @param parameters 查询参数
     * @return
     * @throws SQLException
     */
    private PreparedStatement getQueryPreparedStatement(String querySql, Map<String, Object> parameters) throws SQLException {
        List<String> paramList = null;
        Map<String, Object> metaParams = null;
        // 判断查询SQL是否是Mybatis标注SQL
        if (MybatisScriptHelper.checkMybatisScript(querySql)) {
            final MybatisExecSql execScriptSql = MybatisScriptHelper.getExecScriptSql(querySql, parameters);
            // 查询SQL中的动态参数集合
            paramList = execScriptSql.getParams();
            // mybatis解析后的参数值集合
            metaParams = execScriptSql.getMetaParams();
            // 最终的查询SQL
            querySql = execScriptSql.getSql();
        }
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = getConnection().prepareStatement(querySql);
            // 如果存在动态参数, 则赋值
            if (paramList != null && paramList.size() > 0) {
                for (int i = 1; i <= paramList.size(); i ++ ) {
                    if (metaParams == null) {
                        throw new SQLException("Query metaParams missing. Query error.");
                    }
                    if (!metaParams.containsKey(paramList.get(i-1))) {
                        throw new SQLException("Query parameters missing parameter[" + paramList.get(i-1) + "]. Query error.");
                    }
                    preparedStatement.setObject(i, metaParams.get(paramList.get(i-1)));
                }
            }
        } catch (Exception e) {
            this.closePreparedStatement(preparedStatement);
            log.error("Get PreparedStatement Error -> {}", e.getLocalizedMessage(), e);
            throw new CmdbRuntimeException("Get PreparedStatement Error, Cause:" + e.getLocalizedMessage());
        }
        return preparedStatement;
    }

    /**
     * [<idcType>]
     * 获取 组装 数据权限
     * @param sqlManage 原始查询SQL
     * @return
     */
    private String grantAuthentication(CmdbSqlManage sqlManage) {
        if (sqlManage.getChartSql().contains(Constants.INNER_AUTH_STRING)) {
            return handleAllPermission(sqlManage);
        } else {
            return handlePartPermission(sqlManage);
        }
    }

    /**
     * 获取上下文中附带的用户权限
     * @return
     */
    private Map<String, List<String>> getResFilterConfig() {
        Map<String, List<String>> resFilterConfig = null;
        if (RequestAuthContext.currentRequestAuthContext() != null && RequestAuthContext.currentRequestAuthContext().getUser() != null) {
            resFilterConfig = RequestAuthContext.currentRequestAuthContext().getUser().getResFilterConfig();
        }
//        resFilterConfig = new HashMap<>();
//        List<String> roomList = new ArrayList<>();
//        roomList.add("2006");
//        roomList.add("模块5");
//        roomList.add("模块6");
//        roomList.add("2012");
//        resFilterConfig.put("room_name", roomList);
//        List<String> idcNameList = new ArrayList<>();
//        idcNameList.add("哈尔滨资源池");
//        idcNameList.add("信息港资源池");
//        resFilterConfig.put("idcType_name", idcNameList);
//        List<String> nameList = new ArrayList<>();
//        nameList.add("资源001");
//        nameList.add("2021");
//        nameList.add("信息港资源池权限");
//        resFilterConfig.put("name", nameList);
//        List<String> idcList = new ArrayList<>();
//        idcList.add("6d40d7d3-90a7-11e9-bb30-0242ac110002");
//        idcList.add("6d40db4e-90a7-11e9-bb30-0242ac110002");
//        resFilterConfig.put("idcType", idcList);
//        resFilterConfig.put("roomId", new ArrayList<>());
        log.info("获取请求头中用户数据权限为: {}", resFilterConfig);
        return resFilterConfig;
    }

    private String handlePartPermission(CmdbSqlManage sqlManage) {
        String sourceSql = sqlManage.getChartSql();
        // 先查找需要替换权限的内置标识
        Matcher m = PERMISSION_PATTERN.matcher(sourceSql);
        List<String> innerRightKeys = new ArrayList<>();
        while (m.find()) {
            String sourceKey = m.group();
            // 内置条件、排序、分页不处理
            if (sourceKey.toLowerCase(Locale.ENGLISH).equals(Constants.INNER_LIMIT_STRING) ||
                    sourceKey.toLowerCase(Locale.ENGLISH).equals(Constants.INNER_WHERE_STRING) ||
                    sourceKey.toLowerCase(Locale.ENGLISH).equals(Constants.INNER_ORDER_STRING)) {
                continue;
            }
            innerRightKeys.add(sourceKey.replace("[<", "").replace(">]", ""));
        }
        Map<String, List<String>> resFilterConfig = getResFilterConfig();
        // 如果对应的模型分组, 不参与权限过滤
        Map<String, String> rightMapper = getModuleCatalogRightMapper(sqlManage.getAuthModuleId());
        for (String innerRightKey : innerRightKeys) {
            StringBuilder builder = new StringBuilder();
            if (rightMapper == null) {
                sourceSql = sourceSql.replace("[<" + innerRightKey + ">]", builder.toString());
                continue;
            }
            if (!resFilterConfig.containsKey(innerRightKey) || resFilterConfig.get(innerRightKey).size() == 0) {
                sourceSql = sourceSql.replace("[<" + innerRightKey + ">]", builder.toString());
                continue;
            }
            if (!rightMapper.containsKey(innerRightKey)) {
                throw new RuntimeException("请先在cmdb_config表key=module_right_grant缺少[模型ID:" + sqlManage.getAuthModuleId() + "," + innerRightKey + ":" + innerRightKey + "]权限配置.");
            }
            String mapperFiled = rightMapper.get(innerRightKey);
            builder.append("and ").append(mapperFiled).append(" in (");
            for (String value : resFilterConfig.get(innerRightKey)) {
                builder.append("'").append(value).append("',");
            }
            builder.deleteCharAt(builder.length()-1);
            builder.append(")");
            sourceSql = sourceSql.replace("[<" + innerRightKey + ">]", builder.toString());
        }
        return sourceSql;
    }

    private String handleAllPermission(CmdbSqlManage sqlManage) {
        Map<String, List<String>> resFilterConfig = getResFilterConfig();
        String sourceSql = sqlManage.getChartSql();
        StringBuilder builder = new StringBuilder();
        if (resFilterConfig == null || resFilterConfig.size() == 0) {
            return sourceSql.replace("[<auth>]", builder.toString());
        }
        // 如果没有划分权限, 则不参与权限过滤
        Map<String, String> rightMapper = getModuleCatalogRightMapper(sqlManage.getAuthModuleId());
        if (rightMapper == null || rightMapper.size() == 0) {
            return sourceSql.replace("[<auth>]", builder.toString());
        }
        Iterator rightIterator = rightMapper.keySet().iterator();
        while (rightIterator.hasNext()) {
            String rightKey = rightIterator.next().toString();
            if (!resFilterConfig.containsKey(rightKey) || resFilterConfig.get(rightKey).size() == 0) {
                continue;
            }
            String moduleKey = rightMapper.get(rightKey);
            builder.append("and ").append(moduleKey).append(" in (");
            for (String value : resFilterConfig.get(rightKey)) {
                builder.append("'").append(value).append("',");
            }
            builder.deleteCharAt(builder.length()-1);
            builder.append(")");
        }
        sourceSql = sourceSql.replace("[<auth>]", builder.toString());
        return sourceSql;
    }

    /**
     * 判断对应的资源模块, 是否有检测的权限key
     * @param authModuleId 模型ID
     * @return
     */
    public Map<String, String> getModuleCatalogRightMapper(String authModuleId) {
        Map<String, String> rights = null;
        Map<String, Map<String, String>> rightMap = getRightMap();
        // 先判断模型权限
        if (rightMap.containsKey(authModuleId)) {
            rights = rightMap.get(authModuleId);
        }
        if (rights == null) {
            log.warn("模型ID[{}]未配置任何权限过滤字段. 可在cmdb_config表config_code=module_right_grant中进行设置.", authModuleId);
        }
        return rights;
    }

    /**
     * 关闭连接
     */
    private void closeConnection() {
        // hangfang 2020.07.30 资源未释放：数据库
//        try {
//            if (this.connection != null) {
//                this.connection.close();
//            }
//        } catch (SQLException e) {
//            log.error("Close jdbc connection error. Cause:", e);
//            throw new CmdbRuntimeException("Close jdbc connection error. Cause:" + e.getLocalizedMessage());
//        } finally {
//            connection = null;
//        }
    }

    /**
     * 关闭PreparedStatement查询
     */
    private void closePreparedStatement(PreparedStatement preparedStatement) {
        try {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        } catch (SQLException e) {
            log.error("Close jdbc PreparedStatement error. Cause:", e);
        } finally {
            preparedStatement = null;
        }
    }

    /**
     * 关闭PreparedStatement查询
     */
    private void closeResultSet(ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            log.error("Close jdbc ResultSet error. Cause:", e);
        } finally {
            resultSet = null;
        }
    }

    /**
     * 根据操作符号获取对应sql
     * @param operator 操作类型
     * @param filedName 字段名称
     * @param withIf 是否需要添加if判断
     * @return
     */
    public String getOperatorSql(String filedName, String operator, boolean withIf) {
        switch (operator) {
            case "=" : return this.eqSql(filedName, withIf);
            case "!=" : return this.nEqSql(filedName, withIf);
            case "like" : return this.likeSql(filedName, withIf);
            case "in" : return this.inSql(filedName, withIf);
            case "not in" : return this.notInSql(filedName, withIf);
            case "between" : return this.betweenSql(filedName, withIf);
            default: return "";
        }

    }
    /**
     * LIKE 查询语句
     * @param filedName 字段名称
     * @return
     */
    public String likeSql(String filedName, boolean withIf) {
        String sqlString = " and `{filed}` like concat('%',#{{filed}},'%')";
        if (withIf) {
            sqlString = String.format(IF_TEMPLATE, sqlString);
        }
        return sqlString.replaceAll("\\{filed\\}", filedName);
    }
    /**
     * 小于等于 查询语句
     * @param filedName 字段名称
     * @return
     */
    public String lteSql(String filedName, boolean withIf) {
        String sqlString = " and `{filed}` <![CDATA[<=]]> #{{filed}} ";
        if (withIf) {
            sqlString = String.format(IF_TEMPLATE, sqlString);
        }
        return sqlString.replaceAll("\\{filed\\}", filedName);
    }
    /**
     * 大于等于 查询语句
     * @param filedName 字段名称
     * @return
     */
    public String gteSql(String filedName, boolean withIf) {
        String sqlString = " and `{filed}` <![CDATA[>=]]> #{{filed}} ";
        if (withIf) {
            sqlString = String.format(IF_TEMPLATE, sqlString);
        }
        return sqlString.replaceAll("\\{filed\\}", filedName);
    }

    /**
     * 等于 查询语句
     * @param filedName 字段名称
     * @return
     */
    public String eqSql(String filedName, boolean withIf) {
        String sqlString = " and `{filed}` = #{{filed}} ";
        if (withIf) {
            sqlString = String.format(IF_TEMPLATE, sqlString);
        }
        return sqlString.replaceAll("\\{filed\\}", filedName);
    }

    /**
     * 等于 查询语句
     * @param filedName 字段名称
     * @return
     */
    public String nEqSql(String filedName, boolean withIf) {
        String sqlString = " and `{filed}` != #{{filed}} ";
        if (withIf) {
            sqlString = String.format(IF_TEMPLATE, sqlString);
        }
        return sqlString.replaceAll("\\{filed\\}", filedName);
    }
    /**
     * in 查询语句
     * @param filedName 字段名称
     * @return
     */
    public String inSql(String filedName, boolean withIf) {
        String sqlString = "   and `{filed}` in " +
                "   <foreach collection=\"{filed}\" item=\"item\" separator=\",\" open=\"(\" close=\")\"> " +
                "       #{item} " +
                "   </foreach> ";
        if (withIf) {
            sqlString = String.format(IF_LIST_TEMPLATE, sqlString);
        }
        return sqlString.replaceAll("\\{filed\\}", filedName);
    }

    /**
     * in 查询语句
     * @param filedName 字段名称
     * @return
     */
    public String notInSql(String filedName, boolean withIf) {
        String sqlString = "   and `{filed}` not in " +
                "   <foreach collection=\"{filed}\" item=\"item\" separator=\",\" open=\"(\" close=\")\"> " +
                "       #{item} " +
                "   </foreach> " ;
        if (withIf) {
            sqlString = String.format(IF_LIST_TEMPLATE, sqlString);
        }
        return sqlString.replaceAll("\\{filed\\}", filedName);
    }

    /**
     * in 查询语句
     * @param filedName 字段名称
     * @return
     */
    public String betweenSql(String filedName, boolean withIf) {
        String sqlString = "   and `{filed}` between #{{filed}_start} and #{{filed}_end} ";
        if (withIf) {
            sqlString = String.format(IF_LIST_TEMPLATE, sqlString);
        }
        return sqlString.replaceAll("\\{filed\\}", filedName);
    }

    /**
     * 多个字段模糊 查询语句
     * @param filed 条件字段
     * @param filedNameList 查询字段
     * @return
     */
    public String containSql(String filed,String[] filedNameList, boolean withIf) {
        StringBuilder sb = new StringBuilder(" and (");
        for (int i = 0; i < filedNameList.length; i++) {
            String sql1;
            if (i == 0) {
                sql1 = " `{filedName}` like concat('%',#{{filed}},'%')";
            } else {
                sql1 = " or `{filedName}` like concat('%',#{{filed}},'%')";
            }
            if (withIf) {
                String sql2 = sql1.replaceAll("\\{filedName\\}", filedNameList[i]);
                sb.append(sql2);
            }
        }
        sb.append(")");
        String sqlString = String.format(IF_TEMPLATE, sb.toString());
        return sqlString.replaceAll("\\{filed\\}", filed);
    }
}
