/**
 * 
 */
package com.aspire.mirror.elasticsearch.server.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.search.aggregations.bucket.histogram.Histogram.Bucket;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.InternalNumericMetricsAggregation;
import org.elasticsearch.search.aggregations.metrics.NumericMetricsAggregation;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.aggregations.metrics.cardinality.Cardinality;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.elasticsearch.search.aggregations.metrics.min.Min;
import org.elasticsearch.search.aggregations.metrics.stats.Stats;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;

import com.aspire.mirror.elasticsearch.server.enums.Constants;

/**
* 项目名称: ldap-service <br>
* 包: com.aspire.mirror.elasticsearch.server.util <br>
* 类名称: InstantUtils.java <br>
* 类描述: 日期时间工具类<br>
* 创建人: WangSheng <br>
* 创建时间: 2017年11月7日下午1:19:13 <br>
* 版本: v1.0
*/
public class InstantUtils {

    /**
     * 获取当前时间的UTC格式的日期时间字符串
     * @return UTC格式的日期时间字符串
     */
    public static String now() {
        return Instant.now().toString();
    }
    
    
    public static String getDateAggValues(String count, Bucket bt1,String name) {
    	String value = "";
		if (Constants.AGGREGATION_TYPE_MAX.equals(count)) {

			Max max = bt1.getAggregations().get(name);
			if (max == null || "-Infinity".equals(max.getValueAsString()) || "NaN".equals(max.getValueAsString())) {
				value = "";
			} else {
				value = max.getValueAsString();
			}

		} else if (Constants.AGGREGATION_TYPE_MIN.equals(count)) {
			Min min = bt1.getAggregations().get(name);
			if (min == null || "-Infinity".equals(min.getValueAsString())|| "NaN".equals(min.getValueAsString())) {
				value = "";
			} else {
				value = min.getValueAsString();
			}

		} else if (Constants.AGGREGATION_TYPE_AVG.equals(count)) {
			Avg avg = bt1.getAggregations().get(name);
			if (avg == null || "-Infinity".equals(avg.getValueAsString())|| "NaN".equals(avg.getValueAsString())) {
				value = "";
			} else {
				value = avg.getValueAsString();
			}

		} else if (Constants.AGGREGATION_TYPE_SUM.equals(count)) {
			Sum sum = bt1.getAggregations().get(name);
			if (sum == null || "-Infinity".equals(sum.getValueAsString())|| "NaN".equals(sum.getValueAsString())) {
				value = "";
			} else {
				value = sum.getValueAsString();
			}

		} else if (Constants.AGGREGATION_TYPE_UNIQUECOUNT.equals(count)) {
			Cardinality cardinality = bt1.getAggregations().get(name);
			if ("-Infinity".equals(cardinality.getValueAsString()) || cardinality == null|| "NaN".equals(cardinality.getValueAsString())) {
				value = "";
			} else {
				value = cardinality.getValueAsString();
			}

		}
		
		if(value !="") {
			double val = Double.parseDouble(value);
			if(val<0) {
				return "";
			}
			value = (double)Math.round(val*100)/100 +"";
		}
		return value;
	}
    
    public static String getTermAggValues(String count, Terms.Bucket bt1,String name) {
    	String value = "";
		if (Constants.AGGREGATION_TYPE_MAX.equals(count)) {

			Max max = bt1.getAggregations().get(name);
			if (max == null || "-Infinity".equals(max.getValueAsString()) || "NaN".equals(max.getValueAsString())) {
				value = "";
			} else {
				value = max.getValueAsString();
			}

		} else if (Constants.AGGREGATION_TYPE_MIN.equals(count)) {
			Min min = bt1.getAggregations().get(name);
			if (min == null || "-Infinity".equals(min.getValueAsString())|| "NaN".equals(min.getValueAsString())) {
				value = "";
			} else {
				value = min.getValueAsString();
			}

		} else if (Constants.AGGREGATION_TYPE_AVG.equals(count)) {
			Avg avg = bt1.getAggregations().get(name);
			if (avg == null || "-Infinity".equals(avg.getValueAsString())|| "NaN".equals(avg.getValueAsString())) {
				value = "";
			} else {
				value = avg.getValueAsString();
			}

		} else if (Constants.AGGREGATION_TYPE_SUM.equals(count)) {
			Sum sum = bt1.getAggregations().get(name);
			if (sum == null || "-Infinity".equals(sum.getValueAsString())|| "NaN".equals(sum.getValueAsString())) {
				value = "";
			} else {
				value = sum.getValueAsString();
			}

		} else if (Constants.AGGREGATION_TYPE_UNIQUECOUNT.equals(count)) {
			Cardinality cardinality = bt1.getAggregations().get(name);
			if ("-Infinity".equals(cardinality.getValueAsString()) || cardinality == null|| "NaN".equals(cardinality.getValueAsString())) {
				value = "";
			} else {
				value = cardinality.getValueAsString();
			}

		}
		
		if(value !="") {
			double val = Double.parseDouble(value);
			if(val<0) {
				return "";
			}
			value = (double)Math.round(val*100)/100 +"";
		}
		return value;
	}
    
    public static String getAggValues(String count, Terms.Bucket bt1,String name) {
    	String value = "";
		if (Constants.AGGREGATION_TYPE_MAX.equals(count)) {

			Max max = bt1.getAggregations().get(name);
			if (max == null || "-Infinity".equals(max.getValueAsString()) || "NaN".equals(max.getValueAsString())) {
				value = "";
			} else {
				value = max.getValueAsString();
			}

		} else if (Constants.AGGREGATION_TYPE_MIN.equals(count)) {
			Min min = bt1.getAggregations().get(name);
			if (min == null || "-Infinity".equals(min.getValueAsString())|| "NaN".equals(min.getValueAsString())) {
				value = "";
			} else {
				value = min.getValueAsString();
			}

		} else if (Constants.AGGREGATION_TYPE_AVG.equals(count)) {
			Avg avg = bt1.getAggregations().get(name);
			if (avg == null || "-Infinity".equals(avg.getValueAsString())|| "NaN".equals(avg.getValueAsString())) {
				value = "";
			} else {
				value = avg.getValueAsString();
			}

		} else if (Constants.AGGREGATION_TYPE_SUM.equals(count)) {
			Sum sum = bt1.getAggregations().get(name);
			if (sum == null || "-Infinity".equals(sum.getValueAsString())|| "NaN".equals(sum.getValueAsString())) {
				value = "";
			} else {
				value = sum.getValueAsString();
			}

		} else if (Constants.AGGREGATION_TYPE_UNIQUECOUNT.equals(count)) {
			Cardinality cardinality = bt1.getAggregations().get(name);
			if ("-Infinity".equals(cardinality.getValueAsString()) || cardinality == null|| "NaN".equals(cardinality.getValueAsString())) {
				value = "";
			} else {
				value = cardinality.getValueAsString();
			}

		}
		
		if(value !="") {
			double val = Double.parseDouble(value);
			if(val<0) {
				return "";
			}
			value = (double)Math.round(val*100)/100 +"";
		}
		return value;
	}
    
    public static Double  getValue(InternalNumericMetricsAggregation.SingleValue value){
    	Double val = null;
    	if (value == null || "-Infinity".equals(value.getValueAsString()) || "NaN".equals(value.getValueAsString())) {
			value = null;
		}else {
			val = (double)Math.round(value.value()*100)/100;
			if(val<0) {
				return null;
			}
		}
    	return val;
    	
    }
    
    public static Double  getNumericValue(NumericMetricsAggregation.SingleValue value){
    	Double val = null;
    	if (value == null || "-Infinity".equals(value.getValueAsString()) || "NaN".equals(value.getValueAsString())) {
			value = null;
		}else {
			val = (double)Math.round(value.value()*100)/100;
			if(val<0) {
				return null;
			}
		}
    	return val;
    	
    }
    
    public static float getOperValue(long val,long sum){
    	if(sum==0 || val==0) {
    		return 0;
    	}
    	float v = BigDecimal.valueOf((float)val*100/sum).setScale(2, BigDecimal.ROUND_HALF_DOWN).floatValue();
    	return v;
    }
    
    
    public static String getStatsValues(String count, Stats stats) {
    	String value = "";
		if (Constants.AGGREGATION_TYPE_MAX.equals(count)) {

			if (stats.getMaxAsString() == null || "-Infinity".equals(stats.getMaxAsString()) || "NaN".equals(stats.getMaxAsString())) {
				value = "";
			} else {
				value = stats.getMaxAsString();
			}

		} else if (Constants.AGGREGATION_TYPE_MIN.equals(count)) {
			if (stats.getMinAsString() == null || "-Infinity".equals(stats.getMinAsString())|| "NaN".equals(stats.getMinAsString())) {
				value = "";
			} else {
				value =stats.getMinAsString();
			}

		} else if (Constants.AGGREGATION_TYPE_AVG.equals(count)) {
			if (stats.getAvgAsString() == null || "-Infinity".equals(stats.getAvgAsString())|| "NaN".equals(stats.getAvgAsString())) {
				value = "";
			} else {
				value = stats.getAvgAsString();
			}

		}
		if(value !="") {
			double val = Double.parseDouble(value);
			if(val<0) {
				return "";
			}
			value = (double)Math.round(val*100)/100 +"";
		}
		return value;
	}
    
    public static Map<String, Object>  getObjectMap(Object obj) throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    	Map<String, Object> map = new HashMap<String, Object>();  
    	 
    	BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());   
    	PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();   
    	for (PropertyDescriptor property : propertyDescriptors) {   
    	    String fieldName = property.getName();  
    	    String classStri = "class";
    	    if (fieldName.compareToIgnoreCase(classStri) == 0) {  
    	        continue; 
    	    } 
    	    Method getter = property.getReadMethod(); 
    	    Object value = getter!=null ? getter.invoke(obj) : null; 
    	    map.put(fieldName, value); 
    	} 
    	return map;
    }
}
