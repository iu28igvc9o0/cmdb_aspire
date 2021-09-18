package com.migu.tsg.microservice.atomicservice.composite.controller.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

/**
 * JSON工具类
 * 
 * @company 卓望技术有限公司
 * @author heyifan
 * @email heyf@jee-soft.cn
 * @date 2018年4月20日
 */
public class JsonUtil2 {
	private static final ObjectMapper mapper = new ObjectMapper();

	static {
		// 忽略未知属性
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	/**
	 * 获取ObjectMapper
	 * @return ObjectMapper
	 */
	public static ObjectMapper getMapper(){
		return mapper;
	}

	/**
	 * 字符串转对象
	 * @param json	json格式字符串
	 * @param cls	对象类型
	 * @return		对象
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public static <C> C toBean(String json, Class<C> cls) throws JsonParseException, JsonMappingException, IOException{
		return mapper.readValue(json, cls);
	}

	/**
	 * JsonNode转对象
	 * @param jsonNode	JsonNode
	 * @param cls		对象类型
	 * @return			对象
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static <C> C toBean(JsonNode jsonNode, Class<C> cls) throws JsonParseException, JsonMappingException, IOException{
		Assert.notNull(jsonNode, "jsonNode can not be empty.");
		return mapper.convertValue(jsonNode, cls);
	}

	/**
	 * 对象转换为JsonNode
	 * @param obj	对象
	 * @return		JsonNode
	 * @throws IOException
	 */
	public static JsonNode toJsonNode(Object obj) throws IOException{
		if(BeanUtils.isEmpty(obj)){
			return null;
		}
		return mapper.convertValue(obj, JsonNode.class);
	}

	/**
	 * 字符串转JsonNode
	 * @param json	字符串
	 * @return		JsonNode
	 * @throws IOException
	 */
	public static JsonNode toJsonNode(String json) throws IOException{
		return mapper.readTree(json);
	}

	/**
	 * 字符串转对象列表
	 * @param json		json格式字符串
	 * @param typeRef	对象类型引用
	 * @return			对象列表
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static <C> C toBean(String json, TypeReference<C> typeRef) throws JsonParseException, JsonMappingException, IOException{
		C list = mapper.readValue(json, typeRef);
		return list;
	}

	/**
	 * 对象转字符串
	 * @param obj	对象
	 * @return		json格式字符串
	 * @throws IOException
	 */
	public static String toJson(Object obj) throws IOException{
		return mapper.writeValueAsString(obj);
	}

	/**
	 * 字符串转Map
	 * @param json	json格式字符串
	 * @return		Map
	 * @throws IOException
	 */


	@SuppressWarnings("unchecked")
	public static <T> Map<String, T> toMap(String json) throws IOException{
		Map<String, T> map = (Map<String, T>) mapper.readValue(json, Map.class);
		return map;
	}

	/**
	 * 根据键获取值。
	 * @param obj
	 * @param key
	 * @param defaultValue
	 * @return  String
	 * @throws IOException 
	 */
	public static String getString(ObjectNode obj,String key,String defaultValue){
		if(!isContainsKey(obj,key)){
			return defaultValue;
		}
		try {
			if(obj.get(key).isObject()||obj.get(key).isArray()){
				return toJson(obj.get(key));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		JsonNode jsonNode = obj.get(key);
		if(jsonNode.isNull()) {
			return defaultValue;
		}
		return jsonNode.asText();
	}

	/**
	 * 根据键获取值。
	 * @param obj
	 * @param key
	 * @return  String
	 * @throws IOException 
	 */
	public static String getString(ObjectNode obj,String key){
		return getString(obj, key, "");
	}

	/**
	 * 根据键获取int值。
	 * @param obj
	 * @param key
	 * @return  int
	 */
	public static int getInt(ObjectNode obj,String key){
		if(!isContainsKey(obj,key)){
			return 0;
		}
		return obj.get(key).asInt();
	}

	/**
	 * 根据键获取int值。
	 * @param obj
	 * @param key
	 * @param defaultValue
	 * @return  int
	 */
	public static int getInt(ObjectNode obj,String key,int defaultValue){
		if(!isContainsKey(obj,key)) {
			return defaultValue;
		}
		return obj.get(key).asInt();
	}

	public static boolean getBoolean(ObjectNode obj,String key){
		if(!isContainsKey(obj,key)) {
			return false;
		}
		return obj.get(key).asBoolean();
	}

	/**
	 * 根据键获取boolean值。
	 * @param obj
	 * @param key
	 * @param defaultValue
	 * @return  boolean
	 */
	public static boolean getBoolean(ObjectNode obj,String key,boolean defaultValue){

		if(!isContainsKey(obj,key)) {
			return defaultValue;
		}
		return obj.get(key).asBoolean();
	}



	/**
	 * json对象中是否包含某属性
	 * @param obj
	 * @param key
	 * @return
	 */
	public static boolean isContainsKey(ObjectNode obj,String key){
		if(obj != null && key != null){
			for(Iterator<String> iterator = obj.fieldNames();iterator.hasNext();){  
				String name = iterator.next();
				if(key.equals(name)){
					return true;
				}
			}  
		}
		return false;
	}

	/**
	 * 替换掉包含富文本的json 字符串中特殊的字符
	 * @param str
	 * @return
	 */
	public static String escapeSpecialChar(String str){
		StringBuffer sb = new StringBuffer();      
		for (int i=0; i<str.length(); i++) { 

			char c = str.charAt(i);      
			switch (c) {      
			case '\"':      
				sb.append("\\\"");      
				break;      
			case '\\':      
				sb.append("\\\\");      
				break;      
			case '/':      
				sb.append("\\/");      
				break;      
			case '\b':      
				sb.append("\\b");      
				break;      
			case '\f':      
				sb.append("\\f");      
				break;      
			case '\n':      
				sb.append("\\n");      
				break;      
			case '\r':      
				sb.append("\\r");      
				break;      
			case '\t':      
				sb.append("\\t");      
				break;      
			default:      
				sb.append(c);   
			}
		}

		return sb.toString();      
	}

	/**
	 * 删除的空项，主要controller请求返回的时候 如果数据有{a:null}换转换失败
	 * @param jsonObject 
	 * void
	 * @exception 
	 * @since  1.0.0
	 */
	public static void removeNull(ObjectNode jsonObject){
		Iterator<Entry<String, JsonNode>> newSet= jsonObject.fields();
		while (newSet.hasNext())  
		{  
			Entry<String, JsonNode> ent = newSet.next();  
			JsonNode val = jsonObject.get(ent.getKey());
			if(val instanceof NullNode){
				(jsonObject).put(ent.getKey(), "");
			}
		}  
	}

	/**
	 * 删除的空项，主要controller请求返回的时候 如果数据有{a:null}换转换失败
	 * @param jsonArray 
	 * void
	 * @exception 
	 * @since  1.0.0
	 */
	public static void removeNull(ArrayNode jsonArray){
		for (int i = 0; i < jsonArray.size(); i++) {
			removeNull((ObjectNode)jsonArray.get(i));
		}
	}

	/**
	 * <pre>
	 * JSONArray转成JSONObject
	 * eg:
	 * [{id:"1",name:"a"},{id:"2",name:"b"}] 当keyName设置为id是，转换成 
	 * {1:{id:"1",name:"a"},2:{id:"2",name:"b"}}
	 * </pre>
	 * @param jsonArray
	 * @param keyName	:以哪个字段为key
	 * @return 
	 * JSONObject
	 * @throws IOException 
	 * @exception 
	 * @since  1.0.0
	 */
	@SuppressWarnings("deprecation")
	public static ObjectNode arrayToObject(ArrayNode jsonArray,String keyName) throws IOException{
		ObjectNode jsonObject = getMapper().createObjectNode();
		for(int i=0;i<jsonArray.size();i++){
			JsonNode temp= toJsonNode(jsonArray.get(i));
			jsonObject.put(temp.get(keyName).asText(), temp);
		}
		return jsonObject;
	}

	/**
	 * <pre>
	 * 把jsonObject 转到jsonArray,通常用于以下这种情况
	 * 为了保证jsonArray里的某个值是唯一的所以先用jsonObject来保存着
	 * eg:
	 * {a:{id:1,name:a},b:{id:2,name:b}} 
	 * 转成：[{id:1,name:a},{id:2,name:b}]
	 * </pre>
	 * @param jsonObject
	 * @return 
	 * JSONArray
	 * @exception 
	 * @since  1.0.0
	 */
	public static ArrayNode objectToArray(JsonNode jsonObject){
		ArrayNode jsonArray = getMapper().createArrayNode();
		Iterator<Entry<String, JsonNode>> newSet= jsonObject.fields();
		while (newSet.hasNext())  
		{  
			Entry<String, JsonNode> ent = newSet.next();  
			jsonArray.add(jsonObject.get(ent.getKey()));
		}  
		return jsonArray;
	}

	/**
	 * 判断JSON是否为空
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isEmpty(Object obj) {
		if (obj == null){
			return true;
		}
		if (obj instanceof ObjectNode) {
			return ((ObjectNode) obj).isObject();
		}
		if (obj instanceof ArrayNode) {
			return ((ArrayNode) obj).isArray();
		}
		return NullNode.getInstance().equals(obj);
	}

	/**
	 * ArrayNode转 list
	 * @param jsonArray
	 * @return
	 * @throws IOException
	 */
	public static List<ObjectNode> arrayToList(ArrayNode jsonArray) throws IOException{
		List<ObjectNode> list=new ArrayList<>();
		for(int i=0;i<jsonArray.size();i++){
			JsonNode temp= toJsonNode(jsonArray.get(i));
			list.add( (ObjectNode) temp);
		}
		return list;
	}

	/**
	 * list 转ArrayNode
	 * @param list
	 * @return
	 * @throws IOException
	 */
	public static <T> ArrayNode listToArrayNode (List<T> list) throws IOException{
		if (BeanUtils.isEmpty(list)) {
			return null;
		}

		ArrayNode aryNode = JsonUtil2.getMapper().createArrayNode();
		if(BeanUtils.isNotEmpty(list)) {
			for(T uv : list) {
				aryNode.add(JsonUtil2.toJsonNode(uv));
			}
		}
		return aryNode;
	}


	/**
	 * list 转List<ObjectNode>
	 * @param list
	 * @return
	 * @throws IOException
	 */
	public static <T> List<ObjectNode> listToListNode (List<T> list) throws IOException{
		if (BeanUtils.isEmpty(list)) {
			return null;
		}
		List<ObjectNode> nodeList = new ArrayList<>();
		if(BeanUtils.isNotEmpty(list)) {
			for(T uv : list) {
				nodeList.add((ObjectNode)JsonUtil2.toJsonNode(uv));
			}
		}
		return nodeList;
	}


	public static <K, V> Map<K, V> parseMap(String jsonStr, Class<K> keyClass,
											Class<V> valueClass) {
		if (!StringUtils.hasText(jsonStr)) {
			return new LinkedHashMap<>(0);
		}
		if (keyClass == null) {
			throw new IllegalArgumentException("键Class不能为null");
		}
		if (valueClass == null) {
			throw new IllegalArgumentException("值Class不能为null");
		}

		MapType mapType = mapper.getTypeFactory()
				.constructMapType(LinkedHashMap.class, keyClass, valueClass);

		try {
			return mapper.readValue(jsonStr, mapType);
		} catch (Exception e) {
			throw new IllegalArgumentException("解析Json字符串转成Map失败", e);
		}
	}

	/**
	 * 解析Json字符串转成List
	 *
	 * @param jsonStr   Json字符串
	 * @param elemClass 元素Class
	 * @return List<T>
	 */
	public static <T> List<T> parseList(String jsonStr, Class<T> elemClass) {
		if (!StringUtils.hasText(jsonStr)) {
			return new ArrayList<>(0);
		}
		if (elemClass == null) {
			throw new IllegalArgumentException("List的元素Class不能为null");
		}

		CollectionType coType = mapper.getTypeFactory()
				.constructCollectionType(ArrayList.class, elemClass);

		try {
			return mapper.readValue(jsonStr, coType);
		} catch (Exception e) {
			throw new IllegalArgumentException("解析Json字符串转成List失败", e);
		}
	}

	/**
	 * 解析Json字符串转成Set
	 *
	 * @param jsonStr   Json字符串
	 * @param elemClass 元素Class
	 * @return Set<T>
	 */
	public static <T> Set<T> parseSet(String jsonStr, Class<T> elemClass) {
		if (!StringUtils.hasText(jsonStr)) {
			return new LinkedHashSet<>(0);
		}
		if (elemClass == null) {
			throw new IllegalArgumentException("Set的元素Class不能为null");
		}

		CollectionType coType = mapper.getTypeFactory()
				.constructCollectionType(LinkedHashSet.class, elemClass);

		try {
			return mapper.readValue(jsonStr, coType);
		} catch (Exception e) {
			throw new IllegalArgumentException("解析Json字符串转成Set失败", e);
		}
	}

}
