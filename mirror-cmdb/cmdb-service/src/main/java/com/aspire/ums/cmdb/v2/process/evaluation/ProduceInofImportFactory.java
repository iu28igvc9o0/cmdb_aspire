package com.aspire.ums.cmdb.v2.process.evaluation;

import com.aspire.ums.cmdb.maintenance.payload.ProduceInfoResq;
import com.aspire.ums.cmdb.maintenance.service.IProduceInfoService;
import com.aspire.ums.cmdb.schema.service.SchemaService;
import com.aspire.ums.cmdb.util.SpringUtils;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.v2.process.ImportFactory;
import com.aspire.ums.cmdb.v2.process.validate.DictValidator;
import com.aspire.ums.cmdb.v2.process.validate.EmptyValidator;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Data
@Slf4j
@NoArgsConstructor
public class ProduceInofImportFactory extends ImportFactory {
    private IProduceInfoService produceInfoService;
    private SchemaService schemaService;
    private List<Map<String, Object>> produceTypeList;

    @Override
    public void execute(Map<String,String> dataMap) {
        ProduceInfoResq produceInfo = new ProduceInfoResq();
        EmptyValidator.notEmpty("厂商", dataMap.get("厂商"));
        produceInfo.setProduce(dataMap.get("厂商"));
        EmptyValidator.notEmpty("厂商类型", dataMap.get("厂商类型"));
        DictValidator.validator("厂商类型", dataMap.get("厂商类型"), getProduceTypeList());
        produceInfo.setType(dataMap.get("厂商类型"));
        produceInfo.setRemark(dataMap.get("厂商备注"));
        produceInfoService = getProduceInfoService();
        String id = produceInfoService.selectId(produceInfo.getProduce(),produceInfo.getType());
        if (StringUtils.isEmpty(id)) {
            // 新增厂商信息
            produceInfoService.insert(produceInfo);
        } else {
            // 更新厂商信息
            produceInfoService.update(produceInfo);
        }
    }

    private List<Map<String, Object>> getProduceTypeList() {
        if (produceTypeList == null) {
            produceTypeList = new ArrayList<>();
            String sql = "select dict_code `key`, dict_note `value` from t_cfg_dict t where t.col_name='produceType';";
            List<Map<String, Object>> list = getSchemaService().executeSql(sql);
            if (list != null && list.size() > 0) {
                produceTypeList.addAll(list);
            }
        }
        return produceTypeList;
    }

    @Override
    public void initSpringBean() {
        if (schemaService == null) {
            schemaService = SpringUtils.getBean(SchemaService.class);
        }
        if (produceInfoService == null) {
            produceInfoService = SpringUtils.getBean(IProduceInfoService.class);
        }
    }
}
