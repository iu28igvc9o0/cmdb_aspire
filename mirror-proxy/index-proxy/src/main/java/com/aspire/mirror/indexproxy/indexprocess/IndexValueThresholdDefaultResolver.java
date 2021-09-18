package com.aspire.mirror.indexproxy.indexprocess;

import static com.aspire.mirror.indexproxy.domain.MonitorTriggerRecord.SIGN_EQUAL;
import static com.aspire.mirror.indexproxy.domain.MonitorTriggerRecord.SIGN_GREATER;
import static com.aspire.mirror.indexproxy.domain.MonitorTriggerRecord.SIGN_GREATER_EQUAL;
import static com.aspire.mirror.indexproxy.domain.MonitorTriggerRecord.SIGN_LESS;
import static com.aspire.mirror.indexproxy.domain.MonitorTriggerRecord.SIGN_LESS_EQUAL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.aspire.mirror.indexproxy.domain.MonitorTriggerRecord;
import com.aspire.mirror.indexproxy.exception.IndexProxyException;
import com.aspire.mirror.indexproxy.indexprocess.model.MonitorItemValue;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;


/**
* 默认的指标阈值匹配解析器    <br/>
* Project Name:index-proxy
* File Name:IndexValueThresholdDefaultResolver.java
* Package Name:com.aspire.mirror.indexproxy.indexprocess
* ClassName: IndexValueThresholdDefaultResolver <br/>
* date: 2018年8月16日 下午4:28:29 <br/>
* @author pengguihua
* @version 
* @since JDK 1.6
*/ 
//@Primary
//@Component
class IndexValueThresholdDefaultResolver implements IndexValueThresholdResolver {
	
	@Override
	public List<Pair<MonitorTriggerRecord, Boolean>> resolveThresholds(
						MonitorItemValue itemVal, List<MonitorTriggerRecord> orderedTriggerList) {
		List<Pair<MonitorTriggerRecord, Boolean>> result = new ArrayList<Pair<MonitorTriggerRecord, Boolean>>();
		Object parsedItemValue = parseMonitorItemValue(itemVal);
		boolean isMatch = false;
		for (MonitorTriggerRecord trigger : orderedTriggerList) {
			// 如果匹配到高级别阈值, 低级别的自动设置成不匹配,比如匹配到了严重告警，则不匹配轻微告警
			if (isMatch) {
				result.add(Pair.of(trigger, false));
				continue;
			}
			isMatch = matchTriggerExpression(parsedItemValue, trigger);
			result.add(Pair.of(trigger, isMatch));
		}
		return result;
	}
	
	private Object parseMonitorItemValue(MonitorItemValue itemVal) {
		String valueType = itemVal.getValueType();
		if ("0".equals(valueType) || "FLOAT".equalsIgnoreCase(valueType)) {
			return Float.valueOf(itemVal.getValue());
		}
		if ("3".equals(valueType) || "UINT64".equalsIgnoreCase(valueType)) {
			return Double.valueOf(itemVal.getValue()).longValue(); // 先转double再转long, 因为有的中间件整数值取出来带小数点的0
		}
		if ("4".equals(valueType) || "2".equals(valueType) || "1".equals(valueType)
				 || "TEXT".equalsIgnoreCase(valueType) || "LOG".equalsIgnoreCase(valueType) 
				 || "STR".equalsIgnoreCase(valueType)) {
			return itemVal.getValue();
		}
		return itemVal.getValue();
	}
	
	private boolean matchTriggerExpression(Object itemValue, MonitorTriggerRecord trigger) {
		String expression = trigger.getExpression();
		String matchSign = null;
		for (String sign : MonitorTriggerRecord.VALID_SIGNS) {
			if (expression.indexOf(sign) == 0) {
				matchSign = sign;
				break;
			}
		}
		// 验证表达式的符号
		if (matchSign == null) {
			String tip = String.format("The expression '%s' of the trigger with id %s is invalid", 
										expression, trigger.getTriggerId());
			throw new IndexProxyException(tip);
		}
		// 验证表达式的阈值
		String thresholdValue = expression.substring(expression.indexOf(matchSign) + matchSign.length());
		if (StringUtils.isBlank(thresholdValue)) {
			String tip = String.format("The threshold value of the expression '%s' of the trigger"
									 + " with id %s can't be blank", expression, trigger.getTriggerId());
			throw new IndexProxyException(tip);
		}
		// 验证阈值的值类型
		if (Long.class == itemValue.getClass() || Float.class == itemValue.getClass()) {
			if (!NumberUtils.isDigits(thresholdValue)) {
				String tip = String.format("The threshold value of the expression '%s' of the trigger"
						 				 + " with id %s is invalid", expression, trigger.getTriggerId());
				throw new IndexProxyException(tip);
			}
		}
		
		// 验证字符串的值，只容许 = 符号
		if (itemValue.getClass() == String.class 
				&& (SIGN_GREATER.equals(matchSign) 
						|| SIGN_GREATER_EQUAL.equals(matchSign)
						|| SIGN_LESS_EQUAL.equals(matchSign)
						|| SIGN_LESS.equals(matchSign))) {
			String tip = String.format("The threshold value of the expression '%s' of the trigger"
					 				 + " with id %s is invalid", expression, trigger.getTriggerId());
			throw new IndexProxyException(tip);
		}
		
		
		if (String.class == itemValue.getClass()) {
			if (thresholdValue.trim().equals(String.class.cast(itemValue))) {
				return true;
			}
			return false;
		}
		
		if (Long.class == itemValue.getClass()) {
			Long parsedItemVal = Long.class.cast(itemValue);
			Long parsedThresholdVal = Long.valueOf(thresholdValue);
			if (SIGN_GREATER.equals(matchSign) 
					&& parsedItemVal.longValue() > parsedThresholdVal.longValue()) {
				return true;
			} else if (SIGN_GREATER_EQUAL.equals(matchSign)
					&& parsedItemVal.longValue() >= parsedThresholdVal.longValue()) {
				return true;
			} else if (SIGN_EQUAL.equals(matchSign)
					&& parsedItemVal.longValue() == parsedThresholdVal.longValue()) {
				return true;
			} else if (SIGN_LESS_EQUAL.equals(matchSign)
					&& parsedItemVal.longValue() <= parsedThresholdVal.longValue()) {
				return true;
			} else if (SIGN_LESS.equals(matchSign)
					&& parsedItemVal.longValue() < parsedThresholdVal.longValue()) {
				return true;
			}
			return false;
		}
		
		if (Float.class == itemValue.getClass()) {
			Float parsedItemVal = Float.class.cast(itemValue);
			Float parsedThresholdVal = Float.valueOf(thresholdValue);
			if (SIGN_GREATER.equals(matchSign) 
					&& parsedItemVal.floatValue() > parsedThresholdVal.floatValue()) {
				return true;
			} else if (SIGN_GREATER_EQUAL.equals(matchSign)
					&& parsedItemVal.floatValue() >= parsedThresholdVal.floatValue()) {
				return true;
			} else if (SIGN_EQUAL.equals(matchSign)
					&& parsedItemVal.floatValue() == parsedThresholdVal.floatValue()) {
				return true;
			} else if (SIGN_LESS_EQUAL.equals(matchSign)
					&& parsedItemVal.floatValue() <= parsedThresholdVal.floatValue()) {
				return true;
			} else if (SIGN_LESS.equals(matchSign)
					&& parsedItemVal.floatValue() < parsedThresholdVal.floatValue()) {
				return true;
			}
			return false;
		}
		
		return false;
	}
	
	public static void main(String[] args) {
        String expression = "custom_cpu_avg5_pused+(30-56)>100";
        // 编译表达式
        Expression compiledExp = AviatorEvaluator.compile(expression);
        Map<String, Object> env = new HashMap<String, Object>();
        env.put("custom_cpu_avg5_pused", 180.3);
        env.put("b", 45);
        env.put("c", -199.100);
        // 执行表达式
        Boolean result = (Boolean) compiledExp.execute(env);
        System.out.println(result);  // false
    }
}
