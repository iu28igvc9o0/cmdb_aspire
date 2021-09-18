package com.aspire.mirror.service.impl;

import com.aspire.common.EnvConfigProperties;
import com.aspire.common.HttpUtil;
import com.aspire.mirror.dao.IIndicationLimitDao;
import com.aspire.mirror.entity.IndicationEntity;
import com.aspire.mirror.entity.IndicationLimitEntity;
import com.aspire.mirror.service.IIndicationLimitService;
import com.aspire.mirror.util.SpringUtil;
import com.aspire.mirror.util.XMLUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jw.zhu
 * Date: 2018/10/29
 * 软探针异常指标监控系统
 * com.aspire.mirror.service.impl.IndicationLimitServiceImpl
 */
@Service
public class IndicationLimitServiceImpl implements IIndicationLimitService {
    @Autowired
    private IIndicationLimitDao limitDao;
    @Override
    public IndicationLimitEntity getIndicationLimitByLimitId(Integer indicationLimitId) {
        return limitDao.getIndicationLimitByLimitId(indicationLimitId);
    }

    @Override
    public List<IndicationLimitEntity> getIndicationLimitByIndicationId(String indicationId) {
        return limitDao.getIndicationLimitByIndicationId(indicationId);
    }

    @Override
    public List<IndicationLimitEntity> getAll() {
        return limitDao.getAll();
    }

    @Override
	@Transactional(rollbackFor=Exception.class)
	public int saveMerits(List<IndicationLimitEntity> merits,boolean isSync) {
		if(!isSync) {
			 EnvConfigProperties envConfigProperties = SpringUtil.getBean(EnvConfigProperties.class);
			for (IndicationLimitEntity in : merits) {
				StringBuilder sb = new StringBuilder();
				IndicationEntity inBean = XMLUtil.getEntityById(in.getIndicationId());
				int unit = 1;
				if(inBean.getIndicationUnit().equals("万")) {
					unit = 10000;
				}
				if(!in.getMaxValue().equalsIgnoreCase("nan")) {
					sb.append("A>").append(new BigDecimal(in.getMaxValue()).multiply(new BigDecimal(unit))).append("?'超过上限':'';");
				}
				if(!in.getMinValue().equalsIgnoreCase("nan")) {
					sb.append("A<=").append(new BigDecimal(in.getMinValue()).multiply(new BigDecimal(unit))).append("?'低于下限':'';");
				}
				if(!in.getChangeValueMax().equalsIgnoreCase("nan")) {
					sb.append("A-B>").append(new BigDecimal(in.getChangeValueMax()).multiply(new BigDecimal(unit))).append("?'超过变动值上限':'';");
				}
				if(!in.getChangeValueMin().equalsIgnoreCase("nan")) {
					sb.append("A-B<").append(new BigDecimal(in.getChangeValueMin()).multiply(new BigDecimal(unit))).append("?'低于变动值下限':'';");
				}
				if(!in.getChangeRateMax().equalsIgnoreCase("nan")) {
					sb.append("(A-B)/B*100>").append(in.getChangeRateMax()).append("?'超过变动率上限':'';");
				}
				if(!in.getChangeRateMin().equalsIgnoreCase("nan")) {
					sb.append("(A-B)/B*100<").append(in.getChangeRateMin()).append("?'低于变动率下限':'';");
				}
				if(sb.length()>0) {
					String str = sb.toString();
					str = str.substring(0, str.length()-1);
					String bizThemeExp = inBean.getBizThemeExp();
					JSONArray exceptionArray = JSONArray.fromObject(bizThemeExp);
					if(exceptionArray.size()>0){
					  for(int j=0;j<exceptionArray.size();j++){
					    JSONObject job = exceptionArray.getJSONObject(j);  // 遍历 jsonarray 数组，把每一个对象转成 json 对象
					    if(job.getString("code").equals("exceptionReason")) {
					    	job.put("exp", str);
					    	JSONObject params = new JSONObject();
					    	 params.put("biz_theme_exp", "\""+exceptionArray.toString()+"\"");
					    	 HttpUtil.putMethod(envConfigProperties.getInterFace().getUpdateItems()+in.getIndicationId()+"?resource_type=template&action=update&namespace=alauda"
					    			 	, null,
									params);
					    	break;
					    }
					  }
					}
				}
			}
		}
		limitDao.saveGateWayMerit(merits);
		if(!isSync) {
			XMLUtil.loadIndication(false);
		}
		return 0;
	}

    @Override
    public IndicationLimitEntity getIndicationByIndicationIdAndProvinceCode(Integer indicationId, String provinceCode) {
        return limitDao.getIndicationByIndicationIdAndProvinceCode(indicationId, provinceCode);
    }
}
