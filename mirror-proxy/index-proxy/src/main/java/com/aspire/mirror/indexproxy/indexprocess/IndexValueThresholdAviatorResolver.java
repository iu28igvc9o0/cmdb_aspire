package com.aspire.mirror.indexproxy.indexprocess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.aspire.mirror.indexproxy.domain.MonitorTriggerRecord;
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
@Primary
@Component
class IndexValueThresholdAviatorResolver implements IndexValueThresholdResolver {
	private static final String	CONTAIN_PREFIX	= "::contains::";
	private static final String	REG_SIGN		= "=~";
	
	@Override
	public List<Pair<MonitorTriggerRecord, Boolean>> resolveThresholds(
						MonitorItemValue itemVal, List<MonitorTriggerRecord> orderedTriggerList) {
		List<Pair<MonitorTriggerRecord, Boolean>> result = new ArrayList<Pair<MonitorTriggerRecord, Boolean>>();
		
		// 从高级别到低级别匹配
		String topMatchPriority = null;
		for (MonitorTriggerRecord trigger : orderedTriggerList) {
			// 如果匹配到高级别阈值, 低级别的自动设置成不匹配,比如匹配到了严重告警，则不匹配轻微告警
			if (topMatchPriority != null && !topMatchPriority.equals(trigger.getPriority())) {
				result.add(Pair.of(trigger, false));
				continue;
			}
			
			boolean isMatch = matchTriggerExpression(itemVal, trigger);
			result.add(Pair.of(trigger, isMatch));
			if (isMatch && topMatchPriority == null) { 
				topMatchPriority = trigger.getPriority();
			}
		}
		return result;
	}
	
	private boolean matchTriggerExpression(MonitorItemValue itemVal, MonitorTriggerRecord trigger) {
		String expression = trigger.getExpression();
		// 字符串包含判断
		int containIdx = expression.indexOf(CONTAIN_PREFIX);
		if (containIdx >= 0) {
			String containStr = expression.substring(containIdx + CONTAIN_PREFIX.length());
			return String.valueOf(parseMonitorItemValue(itemVal)).contains(containStr);
		}
		
		// 其它判断
		String itemKey = itemVal.getKey();
		if (!expression.trim().startsWith(itemKey)) {
			expression = itemKey + expression;
		}
		// 处理表达式key中的特殊字符，比如空格，另外key必须以字母开头
		Pair<String, String> itemExpress = filterSpecialChars(itemKey, expression);
		
        Object parseItemVal = null;
        // 如果为正则表达式匹配, 则直接把值转化成字符串
        if (itemExpress.getValue().startsWith(itemExpress.getKey() + REG_SIGN)) {  
        	parseItemVal = itemVal.getValue(); 
        } else {
        	parseItemVal = parseMonitorItemValue(itemVal);
        }
        
//        Map<String, Object> env = new HashMap<>();
//        env.put(itemExpress.getKey(), parseItemVal);
		String express = itemExpress.getValue().replace(itemExpress.getKey(), String.valueOf(parseItemVal));
        // 编译表达式
        Expression compiledExp = AviatorEvaluator.compile(express);
		return Boolean.class.cast(compiledExp.execute());
	}

	/**
	* key中过滤掉特殊字符. <br/>
	*
	* 作者： pengguihua
	* @param itemKey
	* @param expression
	* @return
	*/  
	private Pair<String, String> filterSpecialChars(String itemKey, String expression) {
		String originalKey = itemKey;
		String filterKey = "k_" + itemKey.replaceAll("\\W", "");
		String filterExpress = expression.replace(originalKey, filterKey);
		return Pair.of(filterKey, filterExpress);
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
			return itemVal.getValue().trim();
		}
		return itemVal.getValue();
	}
	
	public static void main(String[] args) {
        String expression = "k_key00=0";
        // 编译表达式
        Expression compiledExp = AviatorEvaluator.compile(expression);
        Map<String, Object> env = new HashMap<String, Object>();
        env.put("3354b59b9bd14ce191e4b362c1dd4c3b", 100);
        env.put("b", 45);
        env.put("c", -199.100);
        // 执行表达式
        Boolean result = (Boolean) compiledExp.execute(env);
        System.out.println(result);  // false
		
//		String email = "killme2008@gmail.com";
//        Map<String, Object> env = new HashMap<String, Object>();
//        env.put("email", email);
////        String username = (String) AviatorEvaluator.execute("email=~/([\\w0-8]+)@\\w+[\\.\\w+]+/ ? $1 : 'unknow' ", env);
////        Boolean isMatch = (Boolean) AviatorEvaluator.execute("email=~/[\\w0-8]+@\\w+[\\.\\w+]+/", env);
//        Expression compiledExp = AviatorEvaluator.compile("email=~/[\\w0-8]+@\\w+[\\.\\w+]+/");
//        System.out.println(compiledExp.execute(env)); // killme2008
//        
////        env.put("a", "pppgh");
////        env.put("b", "pgh2");
//        compiledExp = AviatorEvaluator.compile("string.contains('pppgh', 'pgh')");
//        System.out.println(compiledExp.execute(env)); // killme2008
		
    }
}
