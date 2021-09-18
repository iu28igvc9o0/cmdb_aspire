package ${implPackage};
import ${servicePackage}.${serviceName};
import ${entityPackage}.${entityName};
import ${mapperPackage}.${mapperName};
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
* 描述：
* @author
* @date ${date}
*/
@Service
public class ${implName} implements ${serviceName} {

    @Autowired
    private ${mapperName} mapper;

    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    public List<${entityName}> list() {
        return mapper.list();
    }

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回指定ID的数据信息
     */
    public ${entityName} get(${entityName} entity) {
        return mapper.get(entity);
    }

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    public void insert(${entityName} entity) {
        mapper.insert(entity);
    }

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    public void update(${entityName} entity) {
        mapper.update(entity);
    }

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    public void delete(${entityName} entity) {
        mapper.delete(entity);
    }
}