package ${mapperPackage};
import java.util.List;
import ${entityPackage}.${entityName};
import org.apache.ibatis.annotations.Mapper;

/**
* 描述：
* @author
* @date ${date}
*/
@Mapper
public interface ${mapperName} {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<${entityName}> list();

    /**
     * 获取所有实例
     * @param entity 实例信息
     * @return 返回所有实例数据
     */
    List<${entityName}> listByEntity(${entityName} entity);

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    ${entityName} get(${entityName} entity);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(${entityName} entity);

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    void update(${entityName} entity);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(${entityName} entity);
}