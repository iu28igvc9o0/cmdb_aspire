package com.aspire.ums.cmdb.v2.process.maintenance;

import com.aspire.ums.cmdb.maintenance.payload.Hardware;
import com.aspire.ums.cmdb.maintenance.service.IHardWareService;
import com.aspire.ums.cmdb.util.SpringUtils;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.v2.process.ImportFactory;
import com.aspire.ums.cmdb.v2.process.validate.DictValidator;
import com.aspire.ums.cmdb.v2.process.validate.NumberValidator;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * 类名称: HardWareImportFactory
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/8/1 16:17
 * 版本: v1.0
 */
@Slf4j
@NoArgsConstructor
public class HardWareImportFactory extends ImportFactory {

    private IHardWareService hardWareService;
    private List<Map<String, Object>> payMethods;

    @Override
    public void initBasic() {
        super.initBasic();
        payMethods = getDictList("select dict_code `key`, dict_note `value` from t_cfg_dict where col_name = 'pay_method'");
    }

    @Override
    public void initSpringBean() {
        if (hardWareService == null) {
            hardWareService = SpringUtils.getBean(IHardWareService.class);
        }
    }

    @Override
    public void execute(Map<String, String> dataMap) {
        Hardware hardware = new Hardware();
        String deviceSerialNumber = dataMap.get("设备序列号[必填]");
        if (StringUtils.isEmpty(deviceSerialNumber)) {
            throw new RuntimeException("列[设备序列号]必填");
        }
        String warrantyDate = dataMap.get("出保时间[必填]");
        if (StringUtils.isEmpty(warrantyDate)) {
            throw new RuntimeException("列[出保时间]必填");
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            hardware.setWarrantyDate(sdf.parse(warrantyDate));
        } catch (Exception e) {
            throw new RuntimeException("列[出保时间]日期格式不正确格式：年-月-日");
        }

        String buyMainten = dataMap.get("是否购买维保");
        if (StringUtils.isNotEmpty(buyMainten) && !"是".equals(buyMainten) && !"否".equals(buyMainten)) {
            throw new RuntimeException("列[是否购买维保]值不正确，请填是或否");
        }
        String originBuy = dataMap.get("是否需原厂维保");
        if (!"是".equals(originBuy) && !"否".equals(originBuy)) {
            throw new RuntimeException("列[是否需原厂维保]值不正确，请填是或否");
        }
        String originBuyExplain = dataMap.get("原厂维保购买必要性说明");
        if ("是".equals(originBuy)) {
            if (StringUtils.isEmpty(originBuyExplain)) {
                throw new RuntimeException("列[原厂维保购买必要性说明] 原厂维保为是时必填");
            } else {
                if (!"一旦发生故障容易影响多个业务线".equals(originBuyExplain) &&
                        !"承载业务的重要数据".equals(originBuyExplain) &&
                        !"关键且无冗余".equals(originBuyExplain) &&
                        !"小众且缺乏第三方维保支持".equals(originBuyExplain)) {
                    throw new RuntimeException("列[原厂维保购买必要性说明] 不在可选范围");
                }
            }
        }
        BigDecimal newMoney = null;
        String admin = dataMap.get("维保管理员");

        String preTax = dataMap.get("税前(万)");
        // 小数点只取6位
        int spotIndex = preTax.indexOf(".");
        if(spotIndex != -1 && preTax.substring(spotIndex+1).length() > 6) {
            String tmpStr = preTax.substring(0,spotIndex+7);
            newMoney = new BigDecimal(tmpStr);
        } else {
            newMoney = "".equals(preTax) ? null: new BigDecimal(preTax);
        }
        if (newMoney != null) {
            hardware.setPreTax(newMoney);
        }
        String taxRate = dataMap.get("税率(%)");
        NumberValidator.validateWithScope("税率(%)",taxRate,1,100);

        String afterTax = dataMap.get("税后(万)");
        // 小数点只取6位
        spotIndex = afterTax.indexOf(".");
        if(spotIndex != -1 && afterTax.substring(spotIndex+1).length() > 6) {
            String tmpStr = afterTax.substring(0,spotIndex+7);
            newMoney = new BigDecimal(tmpStr);
        } else {
            newMoney = "".equals(afterTax) ? null: new BigDecimal(afterTax);
        }
// hangfang 2020.07.30 null引用
        if (newMoney != null) {
            hardware.setAfterTax(newMoney);
        }
        String unitPrice = dataMap.get("单价(万)");
        // 小数点只取6位
        spotIndex = unitPrice.indexOf(".");
        if(spotIndex != -1 && unitPrice.substring(spotIndex+1).length() > 6) {
            String tmpStr = unitPrice.substring(0,spotIndex+7);
            newMoney = new BigDecimal(tmpStr);
        } else {
            newMoney = "".equals(unitPrice) ? null: new BigDecimal(unitPrice);
        }
        if (newMoney != null) {
            hardware.setUnitPrice(newMoney);
        }
        String totalPrice = dataMap.get("总价(万)");
        // 小数点只取6位
        spotIndex = totalPrice.indexOf(".");
        if(spotIndex != -1 && totalPrice.substring(spotIndex+1).length() > 6) {
            String tmpStr = totalPrice.substring(0,spotIndex+7);
            newMoney = new BigDecimal(tmpStr);
        } else {
            newMoney = "".equals(totalPrice) ? null: new BigDecimal(totalPrice);
        }
        if (newMoney != null) {
            hardware.setTotalPrice(newMoney);
        }
        String payMethod= dataMap.get("结算方式");
        DictValidator.validator("结算方式", payMethod, this.payMethods);

        String discountAmount = dataMap.get("折扣后金额(万)");
        // 小数点只取6位
        spotIndex = discountAmount.indexOf(".");
        if(spotIndex != -1 && discountAmount.substring(spotIndex+1).length() > 6) {
            String tmpStr = discountAmount.substring(0,spotIndex+7);
            newMoney = new BigDecimal(tmpStr);
        } else {
            newMoney = "".equals(discountAmount) ? null: new BigDecimal(discountAmount);
        }
// hangfang 2020.07.30 null引用
        if (newMoney != null) {
            hardware.setDiscountAmount(newMoney);
        }
        String discountRate = dataMap.get("折扣率(%)");
        NumberValidator.validateWithScope("折扣率(%)",discountRate,1,100);
        hardware.setBuyMainten(buyMainten);
        hardware.setOriginBuy(originBuy);
        hardware.setOriginBuyExplain(originBuyExplain);
        hardware.setAdmin(admin);
        hardware.setTaxRate(taxRate);
        hardware.setPayMethod(payMethod);
        hardware.setDiscountRate(discountRate);
        // 根据设备序列号和出保时间查询项目(以及相关属性)是否存在
        Map<String,Object> tmpMp = hardWareService.queryIsExist(deviceSerialNumber,warrantyDate);
        String flag = tmpMp.get("success").toString();
        String msg = tmpMp.get("message").toString();
        hardware.setProjectInstanceId(msg);
        if ("true".equals(flag)) {
            String id = hardWareService.queryIdByProjectInstanceId(msg);
            if (StringUtils.isNotEmpty(id)) {
                hardware.setId(id);
            }
        } else {
            if("TheProjectNoExistWithDeviceSN".equals(msg)) {
                throw new RuntimeException("根据[设备序列号:" + deviceSerialNumber + ", 出保时间:" + warrantyDate + "]查询不到对应的项目");
            }
        }
        try {
            hardWareService.update(hardware);
        } catch (Exception e) {
            throw new RuntimeException("新增或更新硬件维保查询项目失败:" + e.getMessage(), e);
        }
    }
}
