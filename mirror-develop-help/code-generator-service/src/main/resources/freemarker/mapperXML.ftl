<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="${mapperPackage}.${mapperName}" >
    <!-- 定义返回结果结果集 -->
    <resultMap id="resultMap" type="${entityPackage}.${entityName}" >
    <#if filedList?exists>
    <#list filedList as filed>
        <result column="${filed.dbColumn}" property="${filed.javaFiled}" jdbcType="${filed.dbColumnType}" />
    </#list>
    </#if>
    </resultMap>

    <!-- list查询 -->
    <select id="list" resultMap="resultMap">
        SELECT
        <#if filedList?exists>
        <#list filedList as filed>
        `${filed.dbColumn}`<#if filed_index lt filedList?size - 1 >,</#if>
        </#list>
        </#if>
        FROM ${tableName}
    </select>

    <!-- list查询 -->
    <select id="listByEntity" resultMap="resultMap" parameterType="${entityPackage}.${entityName}">
        SELECT
        <#if filedList?exists>
            <#list filedList as filed>
        `${filed.dbColumn}`<#if filed_index lt filedList?size - 1 >,</#if>
            </#list>
        </#if>
        FROM ${tableName}
        WHERE 1=1
        <#if filedList?exists>
            <#list filedList as filed>
        <#noparse><if test="</#noparse>${filed.javaFiled} != null and ${filed.javaFiled} != ''<#noparse>"></#noparse>
        AND `${filed.dbColumn}` = <#noparse>#{</#noparse>${filed.javaFiled}<#noparse>}</#noparse>
        <#noparse></if></#noparse>
            </#list>
        </#if>
    </select>

    <!-- Get one 查询 -->
    <select id="get" parameterType="${entityPackage}.${entityName}" resultMap="resultMap">
        SELECT
        <#if filedList?exists>
        <#list filedList as filed>
        `${filed.dbColumn}`<#if filed_index lt filedList?size - 1 >,</#if>
        </#list>
        </#if>
        FROM ${tableName}
        WHERE 1=1
        <#if filedList?exists>
        <#list filedList as filed>
        <#if filed.isKey?exists && filed.isKey == 'true'>
        AND `${filed.dbColumn}` = <#noparse>#{</#noparse>${filed.javaFiled}<#noparse>}</#noparse>
        </#if>
        </#list>
        </#if>
    </select>

    <!-- 新增记录 -->
    <insert id="insert" parameterType="${entityPackage}.${entityName}">
        INSERT INTO ${tableName}
        (
        <#if filedList?exists>
            <#list filedList as filed>
            `${filed.dbColumn}`<#if filed_index lt filedList?size - 1 >,</#if>
            </#list>
        </#if>
        ) VALUES (
        <#if filedList?exists>
            <#list filedList as filed>
            <#noparse>#{</#noparse>${filed.javaFiled}<#noparse>, jdbcType=</#noparse>${filed.dbColumnType}<#noparse>}</#noparse><#if filed_index lt filedList?size - 1 >,</#if>
            </#list>
        </#if>
        )
    </insert>

    <!-- 修改记录 -->
    <update id="update" parameterType="${entityPackage}.${entityName}">
        UPDATE ${tableName} SET
        <#if filedList?exists>
        <#list filedList as filed>
        <#if !filed.isKey?exists >
        `${filed.dbColumn}` = <#noparse>#{</#noparse>${filed.javaFiled}<#noparse>}</#noparse><#if filed_index lt filedList?size - 1 >,</#if>
        </#if>
        </#list>
        </#if>
        WHERE
        <#if filedList?exists>
        <#list filedList as filed>
        <#if filed.isKey?exists && filed.isKey == 'true'>
        <#if filed_index gt 0 && filed_index lt filedList?size - 1 > and </#if>`${filed.dbColumn}` = <#noparse>#{</#noparse>${filed.javaFiled}<#noparse>}</#noparse><#if filed_index gt 0 && filed_index lt filedList?size - 1 >,</#if>
        </#if>
        </#list>
        </#if>
    </update>

    <!-- 删除记录 -->
    <delete id="delete" parameterType="${entityPackage}.${entityName}">
        DELETE FROM ${tableName}
        WHERE
        <#if filedList?exists>
        <#list filedList as filed>
        <#if filed.isKey?exists && filed.isKey == 'true'>
        <#if filed_index gt 0 && filed_index lt filedList?size - 1 > and </#if> `${filed.dbColumn}` = <#noparse>#{</#noparse>${filed.javaFiled}<#noparse>}</#noparse><#if filed_index gt 0 && filed_index lt filedList?size - 1 >,</#if>
        </#if>
        </#list>
        </#if>
    </delete>
</mapper>