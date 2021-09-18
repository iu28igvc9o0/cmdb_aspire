<#if operator == 'select' || operator == 'all'>
    <!-- SELECT 语句 -->
    SELECT
    <#if filedList?exists>
    <#list filedList as filed>
    `${filed.dbColumn}`<#if filed_index lt filedList?size - 1 >,</#if>
    </#list>
    </#if>
    FROM ${tableName}
</#if>

<#if operator == 'insert' || operator == 'all'>
    <!-- INSERT 语句 -->
    INSERT INTO ${tableName}(
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
</#if>

<#if operator == 'update' || operator == 'all'>
    <!-- UPDATE 语句 -->
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
</#if>

<#if operator == 'delete' || operator == 'all'>
    <!-- DELETE 语句 -->
    DELETE FROM ${tableName}
    WHERE
    <#if filedList?exists>
    <#list filedList as filed>
    <#if filed.isKey?exists && filed.isKey == 'true'>
    <#if filed_index gt 0 && filed_index lt filedList?size - 1 > and </#if>`${filed.dbColumn}` = <#noparse>#{</#noparse>${filed.javaFiled}<#noparse>}</#noparse><#if filed_index gt 0 && filed_index lt filedList?size - 1 >,</#if>
    </#if>
    </#list>
    </#if>
</#if>