import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringEscapeUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.aspire.ums.cmdb.sync.payload.CmdbDeviceAssetDTO;
import com.aspire.ums.cmdb.util.Base64Util;
import com.aspire.ums.cmdb.util.HttpUtil;
import com.aspire.ums.cmdb.util.JsonMapper;
import com.fasterxml.jackson.core.io.JsonStringEncoder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;

/**
 * xxxxxx
 *
 * @author jiangxuwen
 * @date 2020/7/22 14:18
 */
public class Test {

    @org.junit.Test
    public void test() throws Exception {
        String json = "{\"application_module\":\"应用服务\",\"device_type\":\"物理机\",\"server_rack_location\":\"U235\",\"HOST_NAME\":\"host-1244\",\"server_unum\":\"U2356\",\"business_level2\":\"维护管理自用\",\"device_name\":\"100.2.33.56\",\"department2\":\"能力平台事业部\",\"other_ip\":\"102.3.56.33\",\"device_sn\":\"serio-124555\",\"device_class\":\"服务器\",\"life_period\":\"10\",\"bizSystem\":\"08_超级SIM\",\"asset_number\":\"asset-1457878\",\"device_status\":\"上电\",\"update_time\":\"2020-07-22 14:13:40\",\"ip\":\"100.2.33.56\",\"device_model\":\"DS8800\",\"idcType\":\"资源池\",\"department1\":\"中移互联网公司\",\"device_os_type\":\"CENTOS 7.1\",\"insert_time\":\"2020-07-16 19:08:42\",\"device_log_name\":\"logic-23234\",\"id\":\"146d44ff6c014eceb4ce3506dc88290b\",\"carrier_network\":\"\",\"business_level1\":\"_41_资源池\",\"IP_V6\":\"\",\"device_cell\":\"J2356\",\"device_specification\":\"core25/32GRRM/DSD\",\"remark\":\"139|22|45\",\"transfer_cost\":\"1025.56\",\"host_backup\":\"主\",\"business_ip1\":\"102.3.5.33\",\"device_risk_level\":\"高\",\"insert_person\":\"alauda\",\"device_factory_name\":\"思科（中国）创新科技有限公司\",\"mgr_by_pool\":\"是\",\"project_belong_to\":\"139邮箱MDC机房搬迁独立系统南基节点\",\"device_online_time\":\"2020-01-02 00:00:00\",\"resource_plan\":\"计划外\",\"apportionment_time\":\"2020-12-04 00:00:00\",\"resource_applicant\":\"张升民\",\"idc_location\":\"萝岗C栋401机房\",\"business_ip2\":\"10.2.3.3\",\"create_cm_mgr\":\"张升民\",\"device_network_layer\":\"核心层\",\"price\":\"125.23\",\"module_id\":\"10691117a10e4588bdf36225c104c60b\",\"initial_nat_ip\":\"\",\"ip_type\":\"IPV4\",\"business_vlan\":\"Vlan122\",\"network_area\":\"业务区\",\"physical_device_cpu_cores\":\"16\",\"dis_storage\":\"2056\",\"dis_st_dir\":\"/mnt/opt\",\"server_use_type\":\"刀片服务器\",\"local_disk\":\"2048\",\"mount_disk_size\":\"2033\",\"physical_device_memory_size\":\"32G\",\"slot_num\":\"slot223\",\"box_num\":\"doc1222\",\"box_mgd_ip\":\"10.2.33.66\",\"b_card_sn\":\"bcard1022\",\"maintence_purchase_desc\":\"承载业务的重要数据\",\"maintence_term_start_date\":\"2019-12-23 00:00:00\",\"maintence_purchase_flag\":\"是\",\"maintence_factory_contact\":\"020-22335589\",\"maintence_factory_name\":\"德胜技术有限公司\",\"maintence_admin\":\"张升民\",\"maintence_provider_contact\":\"020-22335589\",\"vender_maintence_flag\":\"是\",\"maintence_end_time\":\"2020-02-23 00:00:00\",\"maintence_term_end_date\":\"2019-12-26 00:00:00\",\"maintence_purchase_type\":\"原厂\",\"console_user\":\"console_admin\",\"console_password\":\"console_pwd\",\"console_gateway\":\"20.2.33.5\",\"console_mask\":\"255.255.0.2\",\"console_ip\":\"10.23.5.33\",\"console_vlan\":\"vlan25\",\"snc_agent_status\":\"待检测\",\"zabbix_agent_install_info\":\"\",\"file_beat_health_check_status\":\"待检测\",\"filebeat_log_path\":\"/opt/aspire/log\",\"snc_agent_health_check_status\":\"待检测\",\"zabbix_proxy_ip\":\"\",\"proxy_ip\":\"10.23.55.45\",\"filebeat_install_info\":\"\",\"filebeat_update_time\":\"\",\"zabbix_agent_monitor_flag\":\"是\",\"zabbix_agent_health_check_status\":\"未检测\",\"snc_agent_monitor_flag\":\"是\",\"zabbix_agent_install_status\":\"未安装\",\"snc_agent_install_info\":\"\",\"item_monitor_status\":\"\",\"filebeat_install_status\":\"待检测\",\"snc_agent_run_time\":\"256\",\"filebeat_agent_monitor_flag\":\"是\",\"server_run_time\":\"256\",\"snc_agent_hostname\":\"host-12356\"}";
        // luowenbo 2020.07.17 JSON注入缺陷
        System.out.println(json);
        JsonStringEncoder encoder = JsonStringEncoder.getInstance();
        // quoteAsUTF8 方法将字符创按照 JSON 标准处理并编码为 UTF-8
        byte[] bytes = encoder.quoteAsUTF8(json);
        String newJson = new String(bytes, Charset.forName("utf-8"));
        newJson = StringEscapeUtils.unescapeJava(newJson);
        System.out.println(newJson);
        CmdbDeviceAssetDTO dto = JsonMapper.getInstanceWithSnakeCase().readValue(newJson,
                new TypeReference<CmdbDeviceAssetDTO>() {});
        System.out.println(dto);
    }

    @org.junit.Test
    public void testJsonPath() throws Exception {
        String json = FileUtils.readFileToString(new File("C:\\Users\\jiangxuwen7515\\Desktop\\subnet.json"),
                StandardCharsets.UTF_8.name());

        // String json = "{\n" + " \"data\": {\n" + " \"list\": [\n" + " {\n"
        // + " \"OPENSTACK_ADMIN\": [\n" + " {\n"
        // + " \"_object_id\": \"OPENSTACK_ADMIN\",\n"
        // + " \"_object_version\": 3,\n"
        // + " \"_orderIP_controller\": \"00000000000000000BN11T\",\n"
        // + " \"_ts\": 1585843813,\n" + " \"_version\": 1,\n"
        // + " \"controller\": \"10.3.220.87\",\n"
        // + " \"creator\": \"easyops\",\n"
        // + " \"ctime\": \"2020-04-03 00:10:13\",\n"
        // + " \"instanceId\": \"5a2510927b43b\",\n" + " \"name\": \"LGKVM\",\n"
        // + " \"org\": 130667\n" + " }\n" + " ],\n"
        // + " \"OPENSTACK_NETWORK\": [\n" + " {\n"
        // + " \"_object_id\": \"OPENSTACK_NETWORK\",\n"
        // + " \"_object_version\": 14,\n" + " \"_ts\": 1585844018,\n"
        // + " \"_version\": 1,\n" + " \"createdAt\": \"2019-04-18 09:27:43\",\n"
        // + " \"creator\": \"easyops\",\n"
        // + " \"ctime\": \"2020-04-03 00:13:38\",\n"
        // + " \"hname\": \"LGKVM\",\n"
        // + " \"id\": \"933622bc-3839-438d-b1bb-7f59a5570e6f\",\n"
        // + " \"instanceId\": \"5a251155e821f\",\n"
        // + " \"isAdminStateUp\": \"是\",\n" + " \"isRouterExternal\": \"否\",\n"
        // + " \"isShared\": \"否\",\n" + " \"mtu\": \"1500\",\n"
        // + " \"name\": \"933622bc-3839-438d-b1bb-7f59a5570e6f-fio-test-LGKVM\",\n"
        // + " \"org\": 130667,\n" + " \"providerNetworkType\": \"vlan\",\n"
        // + " \"providerSegmentationId\": \"1200\",\n"
        // + " \"sname\": \"fio-test\",\n" + " \"status\": \"ACTIVE\",\n"
        // + " \"updatedAt\": \"2019-04-18 09:27:46\"\n" + " }\n"
        // + " ],\n" + " \"_object_id\": \"OPENSTACK_SUBNET\",\n"
        // + " \"_object_version\": 14,\n" + " \"_pre_ts\": 1585844017,\n"
        // + " \"_ts\": 1598941111,\n" + " \"_version\": 2,\n"
        // + " \"allocationPools\": [\n" + " {\n"
        // + " \"start\": \"192.168.100.1\"\n" + " }\n" + " ],\n"
        // + " \"cidr\": \"192.168.100.0/24\",\n" + " \"createdAt\": \"2019-04-18 09:27:46\",\n"
        // + " \"creator\": \"easyops\",\n" + " \"ctime\": \"2020-04-03 00:13:37\",\n"
        // + " \"hname\": \"LGKVM\",\n" + " \"id\": \"629736c9-3e52-4f77-a988-739e9d65cc1a\",\n"
        // + " \"instanceId\": \"5a251155700ed\",\n" + " \"ipVersion\": \"4\",\n"
        // + " \"modifier\": \"easyops\",\n" + " \"mtime\": \"2020-09-01 14:18:31\",\n"
        // + " \"name\": \"629736c9-3e52-4f77-a988-739e9d65cc1a-subnet-fio-LGKVM\",\n"
        // + " \"org\": 130667,\n" + " \"sname\": \"subnet-fio\",\n"
        // + " \"updatedAt\": \"2019-04-18 09:27:46\"\n" + " }\n" + " ],\n"
        // + " \"total\": 1,\n" + " \"page\": 1,\n" + " \"page_size\": 200\n" + " },\n"
        // + " \"code\": 0,\n" + " \"error\": \"\",\n" + " \"message\": \"\"\n" + "}";
        json = json.replace("\n", "");
        JSONObject jsonObject = JSON.parseObject(json);

        List<Map<String, Object>> list = (List<Map<String, Object>>) JSONPath.eval(jsonObject, "$.data.list.OPENSTACK_ADMIN");
        System.out.println("============ OPENSTACK_ADMIN =========");
        for (Map<String, Object> map : list) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                System.out.println(entry.getKey() + "==" + entry.getValue());
            }
        }
        List<Map<String, Object>> list2 = (List<Map<String, Object>>) JSONPath.eval(jsonObject, "$.data.list.OPENSTACK_NETWORK");
        System.out.println("============ OPENSTACK_NETWORK =========");
        for (Map<String, Object> map : list2) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                System.out.println(entry.getKey() + "==" + entry.getValue());
            }
        }
        // [?(@.category == 'reference' || @.price > 10)]
        List<Map<String, Object>> list3 = (List<Map<String, Object>>) JSONPath.eval(jsonObject,
                "$.data.list[_object_id='OPENSTACK_SUBNET']");
        System.out.println("============ OPENSTACK_SUBNET =========");
        for (Map<String, Object> map : list3) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (entry.getValue() instanceof JSONArray) {
                    continue;
                }
                System.out.println(entry.getKey() + "==" + entry.getValue());
                // System.out.println(entry.getValue().getClass());
            }
        }
        List<Map<String, Object>> list4 = (List<Map<String, Object>>) JSONPath.eval(jsonObject, "$.data.list.allocationPools");
        System.out.println("============ allocationPools =========");
        for (Map<String, Object> map : list4) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (entry.getValue() instanceof JSONArray) {
                    continue;
                }
                System.out.println(entry.getKey() + "==" + entry.getValue());
                // System.out.println(entry.getValue().getClass());
            }
        }
        System.out.println("=============objectId=========");
        List<String> objectIds = (List<String>) JSONPath.eval(jsonObject, "$.data.list[*]._object_id");
        System.out.println(objectIds);
        System.out.println("=============cidrs=========");
        List<String> cidrs = (List<String>) JSONPath.eval(jsonObject, "$.data.list[*].cidr");
        System.out.println(cidrs);
    }

    @org.junit.Test
    public void testListJson() throws Exception {
        String json = FileUtils.readFileToString(new File("C:\\Users\\jiangxuwen7515\\Desktop\\subnet.json"),
                StandardCharsets.UTF_8.name());
        JSONObject jsonObject = JSON.parseObject(json);
        // List<Map<String, Object>> targetList = Lists.newArrayList();
        // List<Map<String, Object>> result = (List<Map<String, Object>>) JSONPath.eval(jsonObject,
        // "$.data.list[_object_id='OPENSTACK_SUBNET']");
        // System.out.println("============ OPENSTACK_SUBNET =========");
        // for (Map<String, Object> map : result) {
        // Map<String, Object> target = Maps.newHashMap();
        // for (Map.Entry<String, Object> entry : map.entrySet()) {
        // if (entry.getValue() instanceof JSONArray) {
        // System.out.println("========= json Array============");
        // JSONArray jsonArray = (JSONArray) entry.getValue();
        // analysisJSONArray(jsonArray, target);
        // } else {
        // System.out.println("========= json text ============");
        // // System.out.println(entry.getKey()+"=="+entry.getValue());
        // target.put(entry.getKey(), entry.getValue());
        //
        // }
        //
        // // System.out.println(entry.getKey()+"=="+entry.getValue());
        // // System.out.println(entry.getValue().getClass());
        // }
        // targetList.add(target);
        // }
        List<Map<String, Object>> result = (List<Map<String, Object>>) JSONPath.eval(jsonObject,
                "$.data.list[_object_id='OPENSTACK_SUBNET']");
        // JSON.

        Map<String, Object> target = toMap(jsonObject);
        System.out.println(target);

        Map<String, Object> data = (Map<String, Object>) target.get("data");
        List<Map<String, Object>> list = (List<Map<String, Object>>) data.get("list");
        for (Map<String, Object> map : list) {
            List<Map<String, Object>> openAdminMap = (List<Map<String, Object>>) map.get("OPENSTACK_ADMIN");
            List<Map<String, Object>> openNetworkMap = (List<Map<String, Object>>) map.get("OPENSTACK_NETWORK");
            map.remove("OPENSTACK_ADMIN");
            map.remove("OPENSTACK_NETWORK");
            System.out.println("map===" + map);
            System.out.println("openAdminMap=" + openAdminMap);
            System.out.println("openNetworkMap=" + openNetworkMap);
        }

        // for (Map.Entry<String, Object> entry : target.entrySet()) {
        // System.out.println(entry.getValue().getClass());
        // printMap(entry.getKey(), entry.getValue());
        // }
    }

    private static void printMap(String key, Object value) {
        System.out.println(key + "===");
        if (value instanceof Integer) {
            System.out.println(value.toString());
        } else if (value instanceof Map) {
            for (Map.Entry<String, Object> entry : ((Map<String, Object>) value).entrySet()) {

                printMap(entry.getKey(), entry.getValue());
            }
        } else if (value instanceof List) {
            for (Object object : (List) value) {
                printMap(key, object);
            }
        } else {
            print(value.toString());
        }
    }

    /**
     * 递归解析JSONObject转换成map 存在的问题：如果json数据中存在一样的key，则后面的值会覆盖前面的key
     * 
     * @param jsonObject
     * @return
     */
    public static Map<String, Object> analysis(JSONObject jsonObject) {
        Map<String, Object> result = new HashMap<>();
        Set<String> keys = jsonObject.keySet();
        keys.parallelStream().forEach(key -> {
            Object value = jsonObject.get(key);
            if (value instanceof JSONObject) {
                JSONObject valueJsonObject = (JSONObject) value;
                result.putAll(analysis(valueJsonObject));
            } else if (value instanceof JSONArray) {
                JSONArray jsonArray = (JSONArray) value;
                if (jsonArray.size() == 0) {
                    return;
                }
                analysisJSONArray(jsonArray, result);
            } else {
                result.put(key, value);
            }
        });
        return result;
    }

    /**
     * JSONObject转为map
     * 
     * @param object
     *            json对象
     * @return 转化后的Map
     */
    public static Map<String, Object> toMap(JSONObject object) {
        Map<String, Object> map = new HashMap<String, Object>();

        for (Iterator<?> it = object.keySet().iterator(); it.hasNext();) {
            String key = (String) it.next();
            Object value;
            try {
                value = object.get(key);
                if (value instanceof JSONArray) {
                    value = toList((JSONArray) value);
                }

                else if (value instanceof JSONObject) {
                    value = toMap((JSONObject) value);
                }
                map.put(key, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return map;
    }

    /**
     * JSONArray转为List
     * 
     * @param array
     *            json数组
     * @return 转化后的List
     */
    public static List<Object> toList(JSONArray array) {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < array.size(); i++) {
            Object value;
            try {
                value = array.get(i);
                if (value instanceof JSONArray) {
                    value = toList((JSONArray) value);
                }

                else if (value instanceof JSONObject) {
                    value = toMap((JSONObject) value);
                }
                list.add(value);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return list;
    }

    /**
     * 递归解析JSONArray
     * 
     * @param jsonArray
     * @param map
     */
    public static void analysisJSONArray(JSONArray jsonArray, Map<String, Object> map) {
        jsonArray.parallelStream().forEach(json -> {
            if (json instanceof JSONObject) {
                JSONObject valueJsonObject = (JSONObject) json;
                map.putAll(analysis(valueJsonObject));
            } else if (json instanceof JSONArray) {
                JSONArray tmpJsonArray = (JSONArray) json;
                if (tmpJsonArray.size() == 0) {
                    return;
                }
                analysisJSONArray(tmpJsonArray, map);
            }

        });
    }

    public static boolean isJSONObj(Object json) {
        return json instanceof JSONObject;
    }

    public static boolean isJSONArray(Object json) {
        return json instanceof JSONArray;
    }

    @org.junit.Test
    public void testJsonPath2() {
        String sjson = readtxt();
        print("--------------------------------------getJsonValue--------------------------------------");
        getJsonValue(sjson);
    }

    private static String readtxt() {
        // TODO Auto-generated method stub
        StringBuilder sb = new StringBuilder();
        try {
            FileReader fr = new FileReader("C:\\Users\\jiangxuwen7515\\Desktop\\jsonPath.json");
            BufferedReader bfd = new BufferedReader(fr);
            String s = "";
            while ((s = bfd.readLine()) != null) {
                sb.append(s);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(sb.toString());
        return sb.toString();
    }

    private static void getJsonValue(String json) {
        // The authors of all books：获取json中store下book下的所有author值
        List<String> authors1 = JsonPath.read(json, "$.store.book[*].author");

        // All authors：获取所有json中所有author的值
        List<String> authors2 = JsonPath.read(json, "$..author");

        // All things, both books and bicycles
        // //authors3返回的是net.minidev.json.JSONArray：获取json中store下的所有value值，不包含key，如key有两个，book和bicycle
        List<Object> authors3 = JsonPath.read(json, "$.store.*");

        // The price of everything：获取json中store下所有price的值
        List<Object> authors4 = JsonPath.read(json, "$.store..price");

        // The third book：获取json中book数组的第3个值
        List<Object> authors5 = JsonPath.read(json, "$..book[2]");

        // The first two books：获取json中book数组的第1和第2两个个值
        List<Object> authors6 = JsonPath.read(json, "$..book[0,1]");

        // All books from index 0 (inclusive) until index 2 (exclusive)：获取json中book数组的前两个区间值
        List<Object> authors7 = JsonPath.read(json, "$..book[:2]");

        // All books from index 1 (inclusive) until index 2 (exclusive)：获取json中book数组的第2个值
        List<Object> authors8 = JsonPath.read(json, "$..book[1:2]");

        // Last two books：获取json中book数组的最后两个值
        List<Object> authors9 = JsonPath.read(json, "$..book[-2:]");

        // Book number two from tail：获取json中book数组的第3个到最后一个的区间值
        List<Object> authors10 = JsonPath.read(json, "$..book[2:]");

        // All books with an ISBN number：获取json中book数组中包含isbn的所有值
        List<Object> authors11 = JsonPath.read(json, "$..book[?(@.isbn)]");

        // All books in store cheaper than 10：获取json中book数组中price<10的所有值
        List<Object> authors12 = JsonPath.read(json, "$.store.book[?(@.price < 10)]");

        // All books in store that are not "expensive"：获取json中book数组中price<=expensive的所有值
        List<Object> authors13 = JsonPath.read(json, "$..book[?(@.price <= $['expensive'])]");

        // All books matching regex (ignore case)：获取json中book数组中的作者以REES结尾的所有值（REES不区分大小写）
        List<Object> authors14 = JsonPath.read(json, "$..book[?(@.author =~ /.*REES/i)]");

        // Give me every thing：逐层列出json中的所有值，层级由外到内
        List<Object> authors15 = JsonPath.read(json, "$..*");

        // The number of books：获取json中book数组的长度
        List<Object> authors16 = JsonPath.read(json, "$..book.length()");
        print("**************authors1**************");
        print(authors1);
        print("**************authors2**************");
        print(authors2);
        print("**************authors3**************");
        printOb(authors3);
        print("**************authors4**************");
        printOb(authors4);
        print("**************authors5**************");
        printOb(authors5);
        print("**************authors6**************");
        printOb(authors6);
        print("**************authors7**************");
        printOb(authors7);
        print("**************authors8**************");
        printOb(authors8);
        print("**************authors9**************");
        printOb(authors9);
        print("**************authors10**************");
        printOb(authors10);
        print("**************authors11**************");
        printOb(authors11);
        print("**************authors12**************");
        printOb(authors12);
        print("**************authors13**************");
        printOb(authors13);
        print("**************authors14**************");
        printOb(authors14);
        print("**************authors15**************");
        printOb(authors15);
        print("**************authors16**************");
        printOb(authors16);

        getJsonValue2(json);
    }

    /**
     * 读取json的一种写法，得到匹配表达式的所有值
     */
    private static void getJsonValue0(String json) {
        // TODO Auto-generated method stub
        List<String> authors = JsonPath.read(json, "$.store.book[*].author");
        // System.out.println(authors.size());
        print(authors);

    }

    /**
     * 读取json的一种写法，得到某个具体值
     */
    private static void getJsonValue1(String json) {
        Object document = Configuration.defaultConfiguration().jsonProvider().parse(json);

        String author0 = JsonPath.read(document, "$.store.book[0].author");
        String author1 = JsonPath.read(document, "$.store.book[1].author");
        print(author0);
        print(author1);

    }

    /**
     * 读取json的一种写法
     */
    private static void getJsonValue2(String json) {
        ReadContext ctx = JsonPath.parse(json);

        List<String> authorsOfBooksWithISBN = ctx.read("$.store.book[?(@.isbn)].author");

        List<Map<String, Object>> expensiveBooks = JsonPath.using(Configuration.defaultConfiguration()).parse(json)
                .read("$.store.book[?(@.price > 10)]", List.class);
        print(authorsOfBooksWithISBN);
        print("****************Map****************");
        printListMap(expensiveBooks);
    }

    /**
     * 读取json的一种写法 得到的值是一个String，所以不能用List存储
     */
    private static void getJsonValue3(String json) {
        // Will throw an java.lang.ClassCastException
        // List<String> list = JsonPath.parse(json).read("$.store.book[0].author");
        // 由于会抛异常，暂时注释上面一行，要用的话，应使用下面的格式

        // Works fine
        String author = JsonPath.parse(json).read("$.store.book[0].author");
        print(author);
    }

    /**
     * 读取json的一种写法 支持逻辑表达式，&&和||
     */
    private static void getJsonValue4(String json) {
        List<Map<String, Object>> books1 = JsonPath.parse(json).read("$.store.book[?(@.price < 10 && @.category == 'fiction')]");
        List<Map<String, Object>> books2 = JsonPath.parse(json).read("$.store.book[?(@.category == 'reference' || @.price > 10)]");
        print("****************books1****************");
        printListMap(books1);
        print("****************books2****************");
        printListMap(books1);
    }

    private static void print(List<String> list) {
        for (Iterator<String> it = list.iterator(); it.hasNext();) {
            System.out.println(it.next());
        }
    }

    private static void printOb(List<Object> list) {
        for (Iterator<Object> it = list.iterator(); it.hasNext();) {
            print("****");
            System.out.println(it.next());
        }
    }

    private static void printListMap(List<Map<String, Object>> list) {
        for (Iterator<Map<String, Object>> it = list.iterator(); it.hasNext();) {
            Map<String, Object> map = it.next();
            print("****");
            for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext();) {
                System.out.println(iterator.next());
            }

        }
    }

    private static void print(String s) {
        System.out.println(s);
    }

    @org.junit.Test
    public void test1() throws Exception{
        String name = Base64Util.getBase64("dw_jzwgxt");
        String tokenUrl = "http://10.1.203.99:8998/api/userToken/v1/user/getUserToken?name=" + name;
        String resultToken = HttpUtil.post(tokenUrl, "");
        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(resultToken);
        String jwtToken = jsonObject.getString("token");
        System.out.println(jwtToken);
    }
    
    @org.junit.Test
    public void sendOrder(){
    
    }
}
