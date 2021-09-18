package ${entityPackage};
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 描述：
* @author
* @date ${date}
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ${entityName} {

<#if filedList?exists>
    <#list filedList as filed>
    /**
     * ${filed.columnComment!}
     */
    private ${filed.javaFiledType} ${filed.javaFiled};
    </#list>
</#if>
}