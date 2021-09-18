package com.aspire.ums.cmdb.mybatis;

import org.apache.ibatis.scripting.xmltags.OgnlCache;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @desc:
 * @author: zhangliu
 * @date: 2019/7/19
 */
public class Test {
    public static void main(String args[]) {

//
//        String xml = "select \n" +
//                "    CITY_PAC_NAME \"市\",\n" +
//                "  COUNTY_PAC_NAME \"区县\",\n" +
//                "  TOWNSHIP_PAC_NAME  \"镇\",\n" +
//                "  COUNTRY_PAC_NAME \"村\",\n" +
//                "  VILLAGE_PAC_NAME \"村小组\",\n" +
//                "SUBSTR(HOUSEHOLDER_NAME,1,1)||'**' \"户主名称\",                                                                                      \n" +
//                " '#'||SUBSTR(IDENTITY_CARD,1,4)||'****'||SUBSTR(IDENTITY_CARD,-4) \"户主证件号码\", \n" +
//                "  NVL(HOUSEHOLD_SIZE,0) \"家庭人口数\",\n" +
//                "  '#'||HOUSEHOLD_PRO  \"户属性\",                                                                                            \n" +
//                "  '#'||GEN_HOUSEHOLD_PRO \"一般户属性\",\n" +
//                "  CASE WHEN  JUDGE_OK='1'   THEN '是'  ELSE ''  END AS \"综合判断：无问题\", \n" +
//                "  CASE WHEN  JUDGE_NP='1'   THEN '是'  ELSE ''  END AS  \"综合判断：建议纳贫\",\n" +
//                "  CASE WHEN  JUDGE_FP='1'   THEN '是'  ELSE ''  END AS  \"综合判断：建议返贫\",\n" +
//                "  CASE WHEN  JUDGE_NB='1'   THEN '是'  ELSE ''  END AS  \"综合判断：建议纳保\",\n" +
//                "  CASE WHEN  JUDGE_QXBZ='1' THEN '是'  ELSE ''  END AS  \"综合判断：建议缺项保障\",\n" +
//                "  CASE WHEN  TWO_TROUBLE='1'     THEN '是'  ELSE ''  END AS \"问题类型：两不愁问题\",\n" +
//                "  CASE WHEN  DRINK_PROBLEM='1'   THEN '是'  ELSE ''  END AS \"问题类型：饮水问题\",\n" +
//                "  CASE WHEN  HOUSE_PROBLEM='1'   THEN '是'  ELSE ''  END AS \"问题类型：住房保障问题\", \n" +
//                "  CASE WHEN  EDU_PROBLEM='1'     THEN '是'  ELSE ''  END AS \"问题类型：教育保障问题\",\n" +
//                "  CASE WHEN  MED_PROBLEM='1'     THEN '是'  ELSE ''  END AS \"问题类型：基本医疗保障问题\", \n" +
//                "  CASE WHEN  IND_PROBLEM='1'     THEN '是'  ELSE ''  END AS \"问题类型：产业扶贫问题\", \n" +
//                "  CASE WHEN  EMP_PROBLEM='1'     THEN '是'  ELSE ''  END AS \"问题类型：就业扶贫问题\",\n" +
//                "  CASE WHEN  INCOME_PROBLEM='1'  THEN '是'  ELSE ''  END AS \"问题类型：收入问题\", \n" +
//                "  CASE WHEN  COMPR_PROBLEM='1'   THEN '是'  ELSE ''  END AS \"问题类型：综合保障性扶贫问题\",\n" +
//                "  SIGN_ENTRY_PERSON  \"入户调查员\",\n" +
//                "  EXPLAIN  \"逐项说明具体情况\",\n" +
//                "  CASE WHEN IS_WORRY_EAT = 1 THEN  '是'   WHEN IS_WORRY_EAT = 0 THEN   '否'   ELSE ''  END AS  \"家里是否有饭吃有菜吃都能吃得饱\",\n" +
//                "  CASE WHEN IS_WORRY_CLOTHES  = 1 THEN  '是'   WHEN IS_WORRY_CLOTHES  = 0 THEN   '否'   ELSE ''  END AS  \"衣服被子和鞋是否够用\",\n" +
//                "  CASE WHEN IS_WORRY_DRINK_NEEDS=1 THEN '是' WHEN IS_WORRY_DRINK_NEEDS=0 THEN '否'  ELSE ''  END AS \"每人每天供水量满足基本生活需求\",\n" +
//                "  CASE WHEN IS_REPLACE_WATER=1 THEN '是' WHEN IS_REPLACE_WATER=0 THEN '否'  ELSE ''  END AS \"是否集中供水枯水期是否有替代水\", \n" +
//                "  CASE WHEN IS_WORRY_WATER_HANDY=1 THEN '是' WHEN IS_WORRY_WATER_HANDY=0 THEN '否'  ELSE ''  END AS \"取水是否方便(分散供水填)\",\n" +
//                "  CASE WHEN IS_WORRY_WATER_QUALITY=1 THEN '是' WHEN IS_WORRY_WATER_QUALITY=0 THEN '否'  ELSE ''  END AS \"水质情况(分散供水填)\",\n" +
//                "  CASE WHEN HOU_DANGER_LEVEL='01' THEN '非危房不需要鉴定'\n" +
//                "       WHEN HOU_DANGER_LEVEL='02' THEN 'A级'\n" +
//                "       WHEN HOU_DANGER_LEVEL='03' THEN 'B级'\n" +
//                "       WHEN HOU_DANGER_LEVEL='04' THEN 'C级'\n" +
//                "       WHEN HOU_DANGER_LEVEL='05' THEN 'D级'\n" +
//                "       WHEN HOU_DANGER_LEVEL='06' THEN '需鉴定但未鉴定'\n" +
//                "  ELSE ''  \n" +
//                "  END AS \"危房鉴定级别\",\n" +
//                "  TO_CHAR(HOU_REMAKE_MOVE_INTO_DATE,'yyyymmdd') \"危房改造入住时间\",                                                                                     \n" +
//                "  HOU_REMAKE_COST  \"改造补助（万元）\",\n" +
//                "  CASE WHEN IS_HOU_LOW_ASK=1 THEN '是' WHEN IS_HOU_LOW_ASK=0 THEN '否'  ELSE ''  END AS \"2018年(含)前改造房屋已达要求\", \n" +
//                "  CASE WHEN IS_HOU_REMAKE_FINISH=1 THEN '是' WHEN IS_HOU_REMAKE_FINISH=0 THEN '否'  ELSE ''  END AS \"对超标房屋是否整改完成\",\n" +
//                "  CASE WHEN IS_HOU_DEBT=1 THEN '是' WHEN IS_HOU_DEBT=0 THEN '否'  ELSE ''  END AS \"补助资金合标已拨不存在大额负债\",\n" +
//                "  CASE WHEN IS_HOU_QUALITY_SAFE=1 THEN '是' WHEN IS_HOU_QUALITY_SAFE=0 THEN '否'  ELSE ''  END AS \"改造房屋是否已竣工验收\",\n" +
//                "  CASE WHEN IS_HOU_DANGER_REMOVE=1 THEN '是' WHEN IS_HOU_DANGER_REMOVE=0 THEN '否'  ELSE ''  END AS \"符合拆除条件的危旧房已拆除\",\n" +
//                "  CASE WHEN IS_HOU_REMAKE_MOVE_INTO=1 THEN '是' WHEN IS_HOU_REMAKE_MOVE_INTO=0 THEN '否'  ELSE ''  END AS \"2018年前改造的房屋已入住\",\n" +
//                "  CASE WHEN IS_HOU_REMAKE_PROGRESS=1 THEN '是' WHEN IS_HOU_REMAKE_PROGRESS=0 THEN '否'  ELSE ''  END AS \"2019年改造房屋5月底前已动工\" ,\n" +
//                "  CASE WHEN IS_EDU_CHILD=1 THEN '是' WHEN IS_EDU_CHILD=0 THEN '否'  ELSE ''  END AS \"义务教育适龄孩子均上学\",\n" +
//                "  '#'||EDU_TYPE  \"教育补助类型\",\n" +
//                "  CASE WHEN IS_EDU_IN_PLACE=1 THEN '是' WHEN IS_EDU_IN_PLACE=0 THEN '否'  ELSE ''  END AS \"教育扶贫政策是否落实到位\",\n" +
//                "  CASE WHEN IS_EDU_FIT_RAIN_PLAN=1 THEN '是' WHEN IS_EDU_FIT_RAIN_PLAN=0 THEN '否'  ELSE ''  END AS \"是否有符合雨露计划的孩子\",\n" +
//                "  CASE WHEN IS_EDU_ENJOY_RAIN_PLAN=1 THEN '是' WHEN IS_EDU_ENJOY_RAIN_PLAN=0 THEN '否'  ELSE ''  END AS \"是否参加雨露计划并享受补贴\",\n" +
//                "  CASE WHEN IS_MED_ILLNESS=1 THEN '是' WHEN IS_MED_ILLNESS=0 THEN '否'  ELSE ''  END AS \"家庭是否有残障大病慢性病人员\", \n" +
//                "  '#'||IS_MED_TYPE  \"基本医疗保障类型\",\n" +
//                "  CASE WHEN IS_MED_FULLY_SUBSIDY=1 THEN '是' WHEN IS_MED_FULLY_SUBSIDY=0 THEN '否'  ELSE ''  END AS \"贫困户参保个人已全额补助\",\n" +
//                "  CASE WHEN IS_MED_CURE_SCALE=1 THEN '是' WHEN IS_MED_CURE_SCALE=0 THEN '否'  ELSE ''  END AS \"个人自付住院医疗救助比例达标\",\n" +
//                "  CASE WHEN IS_MED_REIM_SCALE=1 THEN '是' WHEN IS_MED_REIM_SCALE=0 THEN '否'  ELSE ''  END AS \"住院合规医疗费用报销比例达标\",\n" +
//                "  CASE WHEN IS_MED_EXCESSIVE=1 THEN '是' WHEN IS_MED_EXCESSIVE=0 THEN '否'  ELSE ''  END AS \"不存在过度医疗保障情况\",\n" +
//                "  CASE WHEN IS_MED_ONE_STOP=1 THEN '是' WHEN IS_MED_ONE_STOP=0 THEN '否'  ELSE ''  END AS \"定点医院是否享受“一站式”结算\",\n" +
//                "  CASE WHEN IS_MED_SERVE_NUM=1 THEN '是' WHEN IS_MED_SERVE_NUM=0 THEN '否'  ELSE ''  END AS \"贫困家庭已落实家庭医生\", \n" +
//                "  '#'||IS_IND_TYPE  \"产业扶贫类型\",\n" +
//                "  CASE WHEN IS_IND_REALITY=1 THEN '是' WHEN IS_IND_REALITY=0 THEN '否'  ELSE ''  END AS \"项目安排考虑了贫困户愿望\",\n" +
//                "  CASE WHEN IS_IND_LEARNING=1 THEN '是' WHEN IS_IND_LEARNING=0 THEN '否'  ELSE ''  END AS \"贫困户入股后是否参与学习\",\n" +
//                "  CASE WHEN IS_IND_SATISFIED=1 THEN '满意' WHEN IS_IND_SATISFIED=0 THEN '不满意'  ELSE ''  END AS \"对产业帮扶举措的满意度\",\n" +
//                "  CASE WHEN IS_IND_REWARD=1 THEN '是' WHEN IS_IND_REWARD=0 THEN '否'  ELSE ''  END AS \"年度纯收入达标奖励已落实\",\n" +
//                "  CASE WHEN IS_IND_CREDIT_USE=1 THEN '是' WHEN IS_IND_CREDIT_USE=0 THEN '否'  ELSE ''  END AS \"扶贫小额信贷不存在其他用途\",\n" +
//                "  CASE WHEN IS_IND_CREDIT_OVERDUE=1 THEN '是' WHEN IS_IND_CREDIT_OVERDUE=0 THEN '否'  ELSE ''  END AS \"扶贫小额信贷不存在逾期未还\",\n" +
//                "  NVL (EMP_LABOUR_NUM,0)  \"家庭劳动力人数\",\n" +
//                "  CASE WHEN IS_EMP_WORK=1 THEN '是' WHEN IS_EMP_WORK=0 THEN '否'  ELSE ''  END AS \"是否务工\",\n" +
//                "  decode(EMP_WORK_NUM_L,0,'',EMP_WORK_NUM_L)  \"长期务工人数\" ,\n" +
//                "  decode(EMP_WORK_MONTH_NUM,0,'',EMP_WORK_MONTH_NUM)  \"务工月数\",\n" +
//                "  decode(EMP_WORK_NUM_S,0,'',EMP_WORK_NUM_S)  \"短期务工人数\",\n" +
//                "  decode(EMP_WORK_DAY_NUM,0,'',EMP_WORK_DAY_NUM)  \"务工天数\",\n" +
//                "  decode(EMP_WORK_MONTH_SUM,0,'',EMP_WORK_MONTH_SUM)   \"合计务工月数(人累加)\" , \n" +
//                "  CASE WHEN IS_EMP_WORK_TRAINING=1 THEN '是' ELSE ''  END AS \"农业实用技术培训\",\n" +
//                "  CASE WHEN IS_EMP_TECH_TRAINING=1 THEN '是' ELSE ''  END AS \"务工培训\",\n" +
//                "  CASE WHEN IS_EMP_ZERO_PROBLEM=1 THEN '是' WHEN IS_EMP_ZERO_PROBLEM=0 THEN '否'  ELSE ''  END AS \"是否介绍外出解决“零就业家庭”\",\n" +
//                "  '#'||EMP_POSITION  \"就业岗位\",\n" +
//                "  EMP_POSITION_OTH \"就业岗位--其他\",\n" +
//                "  EMP_EMPLOY_MONTH_NUM  \"聘用月数\",\n" +
//                "  CASE WHEN IS_EMP_MONEY_PROBLEM=1 THEN '是' WHEN IS_EMP_MONEY_PROBLEM=0 THEN '否'  ELSE ''  END AS \"公益岗位不存在仅领工资不务工\",\n" +
//                "  '#'||IS_ENS_TYPE \"综合保障性扶贫类型\",\n" +
//                "  CASE WHEN IS_ENS_BRING_INTO=1 THEN '是' WHEN IS_ENS_BRING_INTO=0 THEN '否'  ELSE ''  END AS \"是否将符合低保贫困户纳入低保\",\n" +
//                "  '#'||IS_OTH_TYPE  \"其他问题\",\n" +
//                "  AVE_INCOME  \"家庭人均纯收入\",\n" +
//                "  CASE WHEN  RESULT_JUDGE='01' THEN '无问题'\n" +
//                "       WHEN  RESULT_JUDGE='02' THEN '建议纳贫'\n" +
//                "       WHEN  RESULT_JUDGE='03' THEN '建议返贫'\n" +
//                "       WHEN  RESULT_JUDGE='04' THEN '建议纳保'\n" +
//                "       WHEN  RESULT_JUDGE='05' THEN '建议缺项保障'\n" +
//                "  ELSE ''  \n" +
//                "  END AS \"综合判断\",\n" +
//                "  decode(EMP_WORK_NUM_L,0,'',EMP_WORK_NUM_L)  \"长期务工人数1\" ,\n" +
//                "  decode(EMP_WORK_MONTH_NUM,0,'',EMP_WORK_MONTH_NUM)  \"务工月数1\",\n" +
//                "  decode(EMP_WORK_NUM_S,0,'',EMP_WORK_NUM_S)  \"短期务工人数1\",\n" +
//                "  decode(EMP_WORK_DAY_NUM,0,'',EMP_WORK_DAY_NUM)  \"务工天数1\",\n" +
//                "  decode(EMP_WORK_MONTH_SUM,0,'',EMP_WORK_MONTH_SUM)   \"合计务工月数(人累加)1\" \n" +
//                "  from  \n" +
//                "  (select\n" +
//                "   ID ,\n" +
//                "  CASE WHEN  RESULT_JUDGE='01' THEN '1'   ELSE ''   END AS \"JUDGE_OK\",\n" +
//                "  CASE WHEN  RESULT_JUDGE='02' THEN '1'   ELSE ''   END AS \"JUDGE_NP\",\n" +
//                "  CASE WHEN  RESULT_JUDGE='03' THEN '1'   ELSE ''   END AS \"JUDGE_FP\",\n" +
//                "  CASE WHEN  RESULT_JUDGE='04' THEN '1'   ELSE ''   END AS \"JUDGE_NB\",\n" +
//                "  CASE WHEN  RESULT_JUDGE='05' THEN '1'   ELSE ''   END AS \"JUDGE_QXBZ\",\n" +
//                "  CASE WHEN   IS_WORRY_EAT = 0 THEN  '1'\n" +
//                "       WHEN IS_WORRY_CLOTHES  = 0 THEN  '1' \n" +
//                "     ELSE ''  \n" +
//                "  END AS \"TWO_TROUBLE\",\n" +
//                "  CASE WHEN   IS_WORRY_DRINK_NEEDS=0 THEN  '是'\n" +
//                "         WHEN IS_REPLACE_WATER=0  and (IS_WORRY_WATER_HANDY=0 or IS_WORRY_WATER_QUALITY=0) THEN '是' \n" +
//                "     ELSE ''  \n" +
//                "  END AS \"DRINK_PROBLEM\",\n" +
//                "  CASE WHEN HOU_DANGER_LEVEL='04' THEN '1' \n" +
//                "       WHEN HOU_DANGER_LEVEL='05' THEN '1' \n" +
//                "       WHEN HOU_DANGER_LEVEL='06' \n" +
//                "            and (HOUSEHOLD_PRO like '%01%' or \n" +
//                "                 HOUSEHOLD_PRO like '%02%' or \n" +
//                "                 HOUSEHOLD_PRO like '%03%' or \n" +
//                "                 HOUSEHOLD_PRO like '%04%' or\n" +
//                "            GEN_HOUSEHOLD_PRO is not null) THEN '1' \n" +
//                "       WHEN (IS_HOU_LOW_ASK=0  or  \n" +
//                "             IS_HOU_REMAKE_FINISH=0 or                                         \n" +
//                "             IS_HOU_DEBT=0 or                                          \n" +
//                "             IS_HOU_QUALITY_SAFE=0 or                                         \n" +
//                "             IS_HOU_DANGER_REMOVE=0 or                                         \n" +
//                "             IS_HOU_REMAKE_MOVE_INTO=0 or                                         \n" +
//                "             IS_HOU_REMAKE_PROGRESS=0  ) THEN '1' \n" +
//                "     ELSE ''  \n" +
//                "  END AS \"HOUSE_PROBLEM\", \n" +
//                "  CASE WHEN IS_EDU_CHILD=0 THEN '1' \n" +
//                "       WHEN IS_EDU_IN_PLACE=0 and (HOUSEHOLD_PRO like '%01%' or HOUSEHOLD_PRO like '%02%') THEN '1' \n" +
//                "      ELSE ''  \n" +
//                "  END AS \"EDU_PROBLEM\",\n" +
//                "  CASE WHEN (IS_MED_ILLNESS=1 and IS_MED_TYPE is null) THEN '1' \n" +
//                "       WHEN (IS_MED_FULLY_SUBSIDY=0  or    \n" +
//                "             IS_MED_CURE_SCALE=0     or \n" +
//                "             IS_MED_REIM_SCALE=0     or \n" +
//                "             IS_MED_EXCESSIVE=0      or\n" +
//                "             IS_MED_ONE_STOP=0       or\n" +
//                "             IS_MED_SERVE_NUM=0    ) THEN '1' \n" +
//                "   ELSE ''  \n" +
//                "  END AS \"MED_PROBLEM\", \n" +
//                "  CASE WHEN (HOUSEHOLD_PRO like '%01%' or HOUSEHOLD_PRO like '%02%') and\n" +
//                "            (IS_IND_REALITY=0         or\n" +
//                "             IS_IND_LEARNING=0        or\n" +
//                "             IS_IND_SATISFIED=0       or\n" +
//                "             IS_IND_REWARD=0          or\n" +
//                "             IS_IND_CREDIT_USE=0      or\n" +
//                "             IS_IND_CREDIT_OVERDUE=0    ) THEN '1' \n" +
//                "      ELSE ''  \n" +
//                "  END AS \"IND_PROBLEM\", \n" +
//                "  CASE WHEN (HOUSEHOLD_PRO like '%01%' or HOUSEHOLD_PRO like '%02%') and\n" +
//                "            (IS_EMP_ZERO_PROBLEM=0  or IS_EMP_MONEY_PROBLEM=0 )   THEN '1' \n" +
//                "      ELSE ''  \n" +
//                "  END AS \"EMP_PROBLEM\", \n" +
//                "  CASE WHEN  AVE_INCOME <![CDATA[ < ]]> 3755    THEN '1' \n" +
//                "      ELSE ''  \n" +
//                "  END AS \"INCOME_PROBLEM\",  \n" +
//                "    CASE WHEN  IS_ENS_BRING_INTO=0   THEN '1' \n" +
//                "      ELSE ''  \n" +
//                "  END AS \"COMPR_PROBLEM\"\n" +
//                "  from TR_HOUSEHOLD_TAG_D\n" +
//                "  where DELETED=0  \n" +
//                "  AND FILE_TIME=453\n" +
//                "  ) a, TR_HOUSEHOLD_TAG_D b\n" +
//                "where a.id=b.id\n" +
//                "  and b.DELETED=123\n" +
//                "  AND FILE_TIME=20190627" ;

        String xml="SELECT device_sum,normal_sum,vm_sum,phy_sum,host_sum,mon\n" +
                "FROM `device_inspection_day`\n" +
                "WHERE 1=1 <if test=\"month!=null and month!=''\"> and mon = ${month}</if>\n" +
                "<if test=\"deviceTypeList!=null\"> and device_type in " +
                "<foreach collection=\"deviceTypeList\" item=\"item\" separator=\",\" open=\"(\" close=\")\">" +
                "${item} \n" +
                "</foreach></if>";

        final boolean ismybatis = MybatisScriptHelper.checkMybatisScript(xml);
        System.out.println("#######是否为mybatis执行的sql:" + ismybatis);


        //获取参数列表
//        Set<String> scriptParams = MybatisScriptHelper.getScriptParams(xml);
//        scriptParams.stream().forEach(System.out::println);


        final Map<String, Object> prama = new HashMap<>();
        prama.put("month", "2");
        prama.put("deviceTypeList", Arrays.asList("1","2"));


        Long start = System.currentTimeMillis();
        final MybatisExecSql execScriptSql = MybatisScriptHelper.getExecScriptSql(xml, prama);
        System.out.println("#######执行的sql:" + execScriptSql.getSql());
        System.out.println("#######执行的sql的参数列表:" + Arrays.toString(execScriptSql.getParams().toArray()));
        System.out.println("#######耗时:" + (System.currentTimeMillis() - start));

        Object par="{_parameter={email3=aaa, email2=1, email=1}, _databaseId=null}";
        Map<String,Object> map = new HashMap<>();
        map.put("email3","aaa");
        map.put("email2","1");
        map.put("email","1");

        Object value = OgnlCache.getValue("email2 !=null ", map);
        System.out.println(value);

    }
}
