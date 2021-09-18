package com.aspire.ums.cmdb.mapper;


import com.aspire.ums.cmdb.sync.entity.User;
import com.migu.tsg.microservice.atomicservice.ldap.dto.ListPagenationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.object.BatchSqlUpdate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class OrgSyncManager {
    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * 获取同步关联表的所有属性
     * @param syncSource
     * @param desTableName
     * @return
     */
    public List<Map<String, Object>> getFieldRelation(String syncSource,String desTableName) {

        String sql = "select * from org_sync_relation where SYNC_SOURCE = '"+syncSource+"' and SYNC_DESTINATION_TABLE='"+desTableName+"'";
        System.out.println(sql);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("SYNC_SOURCE", syncSource);
        paramMap.put("SYNC_DESTINATION_TABLE", desTableName);
        NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(jdbcTemplate);
        List<Map<String, Object>> columnsMap = jdbc.queryForList(sql,paramMap);

        return columnsMap;
    }
    /**
     * 判断同步的部门数据是否存在
     */
    public boolean getCountById(String uuid) {
        int i = 0;
        String sql = "select count(*) from department  where source_uuid = '"+uuid+"'";
        System.out.println(sql);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("source_uuid", uuid);
       i = jdbcTemplate.queryForObject(sql,Integer.class);
        return i==1?true:false;
    }

    /**
     * 插入组织新增数据
     * @param deptList
     * @return
     */
    public int insertOrg(List<Map<String,String>> deptList,String syncSrc){
        int x = 0;
        //取当前时间
        Date nowdate=new Date();
        //转换时间格式
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp sqlTime = Timestamp.valueOf(simpleDate.format(nowdate));
        DataSource dataSource = jdbcTemplate.getDataSource();
        BatchSqlUpdate bsu = new BatchSqlUpdate(dataSource,"INSERT INTO department (uuid, name, descr,created_at,updated_at,parent_id,is_lv_one_company,namespace,deleted,source_uuid,SYNC_SOURCE,dept_type) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)");
        bsu.setBatchSize(512);
        bsu.setTypes(new int[]{Types.CHAR,Types.CHAR, Types.VARCHAR,
                                Types.TIMESTAMP,Types.TIMESTAMP,Types.CHAR,Types.INTEGER,Types.CHAR,Types.INTEGER,Types.CHAR,Types.CHAR, Types.TINYINT});
        for (Map  m: deptList) {
            bsu.update(new Object[]{
                    m.get("uuid"),
                    m.get("name"),
                    m.get("descr"),
                    m.get("createdAt").equals("")? sqlTime: m.get("createdAt"),
                    m.get("updatedAt").equals("")? sqlTime: m.get("updatedAt"),
                    m.get("is_lv_one_company").equals("false")?m.get("parent_id"):"1001",
                    m.get("is_lv_one_company").equals("false")?0:1,
                    m.get("namespace"),
                    m.get("deleted").equals("false")?0:1,
                    m.get("uuid"),
                    syncSrc,
                    1
            });
        }
        bsu.flush();
       //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        return x;
    }
    /**
     * 修改组织新增数据
     * @param deptList
     * @return
     */
    public void updateOrg(List<Map<String,String>> deptList){
        //取当前时间
        Date nowdate=new Date();
        //转换时间格式
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp sqlTime = Timestamp.valueOf(simpleDate.format(nowdate));
        DataSource dataSource = jdbcTemplate.getDataSource();
        BatchSqlUpdate bsu = new BatchSqlUpdate(dataSource,"UPDATE department SET name =?, descr =?,created_at =?,updated_at =?,parent_id =?,is_lv_one_company =?,namespace =?,deleted =?,source_uuid=? WHERE source_uuid = ?  " );
        bsu.setBatchSize(512);
        bsu.setTypes(new int[]{Types.CHAR, Types.VARCHAR,
                Types.TIMESTAMP,Types.TIMESTAMP,Types.CHAR,Types.INTEGER,Types.CHAR,Types.INTEGER,Types.CHAR,Types.CHAR});
        for (Map  m: deptList) {
            bsu.update(new Object[]{
                    m.get("name"),
                    m.get("descr"),
                    m.get("createdAt").equals("")? sqlTime: m.get("createdAt"),
                    m.get("updatedAt").equals("")? sqlTime: m.get("updatedAt"),
                    m.get("parent_id"),
                    m.get("is_lv_one_company").equals("false")?0:1,
                    m.get("namespace"),
                    m.get("deleted").equals("false")?0:1,
                    m.get("uuid"),
                    m.get("uuid")
            });
        }

        bsu.flush();
    }

    /**
     * 插入用户新增数据
     * @param userList
     * @return
     */
    public void insertUser(List<Map<String,String>> userList,String syncSrc){
        //取当前时间
        Date nowdate=new Date();
        //转换时间格式
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp sqlTime = Timestamp.valueOf(simpleDate.format(nowdate));
        DataSource dataSource = jdbcTemplate.getDataSource();
        BatchSqlUpdate bsu = new BatchSqlUpdate(dataSource,"INSERT INTO user (uuid, name, descr,created_at,updated_at,mail,source_uuid,ldap_status,password,tenantCode,code,ldapId,mobile,dept_Id,sync_source) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        bsu.setBatchSize(512);
        bsu.setTypes(new int[]{Types.CHAR,Types.CHAR, Types.VARCHAR,
                Types.TIMESTAMP,Types.TIMESTAMP,Types.VARCHAR,Types.CHAR,
                Types.INTEGER, Types.VARCHAR, Types.VARCHAR,
                Types.VARCHAR,Types.CHAR,Types.VARCHAR,Types.CHAR,Types.CHAR});
        for (Map  m: userList) {
            bsu.update(new Object[]{
                    m.get("uuid"),
                    m.get("name"),
                    syncSrc,
                    m.get("createdAt").equals("")? sqlTime: m.get("createdAt"),
                    m.get("updatedAt").equals("")? sqlTime: m.get("updatedAt"),
                    m.get("mail"),
                    m.get("source_uuid"),
                    m.get("status").equals("ONLINE")?1:0,
                    m.get("password"),
                    m.get("tenantCode"),
                    m.get("code"),
                    m.get("ldapId"),
                    m.get("mobile"),
                    m.get("dept_Id"),
                    syncSrc
            });
        }
        bsu.flush();
        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    }
    /**
     * 修改用户新增数据
     * @param userList
     * @return
     */
    public void updateUser(List<Map<String,String>> userList,String syncSrc){
        //取当前时间
        Date nowdate=new Date();
        //转换时间格式
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp sqlTime = Timestamp.valueOf(simpleDate.format(nowdate));
        DataSource dataSource = jdbcTemplate.getDataSource();
        BatchSqlUpdate bsu = new BatchSqlUpdate(dataSource,"UPDATE user SET uuid=?, name=?, descr=?,created_at=?,updated_at=?,mail=?,source_uuid=?,ldap_status=?,password=?,tenantCode=?,code=?,ldapId=?,mobile=?,dept_Id=?,SYNC_SOURCE=?, del_status=? WHERE source_uuid=?");
        bsu.setBatchSize(512);
        bsu.setTypes(new int[]{Types.CHAR,Types.CHAR, Types.VARCHAR,
                Types.TIMESTAMP,Types.TIMESTAMP,Types.VARCHAR,Types.CHAR,
                Types.INTEGER, Types.VARCHAR, Types.VARCHAR,
                Types.VARCHAR,Types.CHAR,Types.VARCHAR,Types.CHAR,Types.CHAR,Types.CHAR,Types.INTEGER});
        for (Map  m: userList) {
            bsu.update(new Object[]{
                    m.get("uuid"),
                    m.get("name"),
                    syncSrc,
                    m.get("createdAt").equals("")? sqlTime: m.get("createdAt"),
                    m.get("updatedAt").equals("")? sqlTime: m.get("updatedAt"),
                    m.get("mail"),
                    m.get("source_uuid"),
                    m.get("status").equals("ONLINE")?1:0,
                    m.get("password"),
                    m.get("tenantCode"),
                    m.get("code"),
                    m.get("ldapId"),
                    m.get("mobile"),
                    m.get("dept_Id"),
                    syncSrc,
                    m.get("uuid"),
                    m.get("delStatus")
            });
            if(!StringUtils.isEmpty(m.get("dept_Id"))){

            }
        }
        bsu.flush();
        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    }
    /*`department_user` (
  `dept_id` char(36) COLLATE utf8_bin NOT NULL COMMENT '部门ID',
  `user_id` char(36) COLLATE utf8_bin NOT NULL COMMENT '用户ID'*/
    /*
     /**
     * 插入用户组织关联表
     * @param userList
     * @return
     */
    public void insertUserDept(List<Map<String,String>> userList,String syncSrc){
        DataSource dataSource = jdbcTemplate.getDataSource();
        BatchSqlUpdate bsu = new BatchSqlUpdate(dataSource,"INSERT INTO department_user (user_id, dept_id) VALUES(?, ?)");
        bsu.setBatchSize(512);
        bsu.setTypes(new int[]{Types.CHAR,Types.CHAR});
        for (Map  m: userList) {
            bsu.update(new Object[]{
                    m.get("source_uuid"),
                    m.get("dept_Id")
            });
        }
        bsu.flush();

    }
    /**
     * 插入用户组织关联表
     * @param userList
     * @return
     */
    public void updateUserDept(List<Map<String,String>> userList,String syncSrc){
        DataSource dataSource = jdbcTemplate.getDataSource();
        BatchSqlUpdate bsu = new BatchSqlUpdate(dataSource,"UPDATE department_user SET user_id=?, dept_id=? WHERE user_id = ?");
        bsu.setBatchSize(512);
        bsu.setTypes(new int[]{Types.CHAR,Types.CHAR,Types.CHAR});
        for (Map  m: userList) {
            bsu.update(new Object[]{
                    m.get("source_uuid"),
                    m.get("dept_Id"),
                    m.get("source_uuid")
            });
        }
        bsu.flush();
        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    }

    /**
     * 获取UMS用户表 已经同步的数据做删除对比
     *
     */
    public List<Map<String, Object>> getUMSUsers(String syncSource){
        String sql = "select source_uuid,created_at,updated_at from user where descr = '"+syncSource+"'";
        System.out.println(sql);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("descr", syncSource);
        /*paramMap.put("SYNC_DESTINATION_TABLE", desTableName);*/
        NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(jdbcTemplate);
        List<Map<String, Object>> columnsMap = jdbc.queryForList(sql,paramMap);

        return columnsMap;
    }

    /**
     * 更新用户状态为删除
     * @param delList
     * @param syncSource
     */
    public void delUser(List<String> delList, String syncSource) {
        //取当前时间
        Date nowdate=new Date();
        //转换时间格式
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp sqlTime = Timestamp.valueOf(simpleDate.format(nowdate));
        DataSource dataSource = jdbcTemplate.getDataSource();
        BatchSqlUpdate bsu = new BatchSqlUpdate(dataSource,"UPDATE user SET updated_at=?,del_status=? WHERE source_uuid=?");
        bsu.setBatchSize(512);
        bsu.setTypes(new int[]{Types.TIMESTAMP,Types.INTEGER,Types.CHAR});
        for (String  m: delList) {
            bsu.update(new Object[]{
                    sqlTime,
                    1,
                    m,
            });
        }
        bsu.flush();
    }

    /**
     * 获取已经同步的用户id
     * @param syncSource 同步来源
     * @return
     */
    public List<String> getUuidsByYyncSource(String syncSource){
        String sql = "select source_uuid from user where descr = '"+syncSource+"'";
        System.out.println(sql);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("descr", syncSource);
        NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(jdbcTemplate);
        List<String> uuids = jdbc.queryForList(sql,paramMap,String.class);

        return uuids;
    }
    /**
     * 获取已经同步的用户id
     * @param sourceUUid 同步来源
     * @return
     */
    public  List<String> getCodeById(String sourceUUid){
        String sql = "select code from user where source_uuid = '"+sourceUUid+"'";
        System.out.println(sql);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("source_uuid", sourceUUid);
        NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(jdbcTemplate);
        List<String> uuids = jdbc.queryForList(sql,paramMap,String.class);

        return uuids;
    }
    /**
     * 获取已经同步的用户id
     * @param syncSource 同步来源
     * @return
     */
    public List<Map<String, Object>> getUuidsAndDeptIdByYyncSource(String syncSource){
        String sql = "select source_uuid,dept_Id from user where descr = '"+syncSource+"'";
        System.out.println(sql);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("descr", syncSource);
        NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(jdbcTemplate);
        List<Map<String, Object>> columnsMap = jdbc.queryForList(sql,paramMap);

        return columnsMap;
    }
    /**
     * 获取已经同步的组织id
     * @param syncSource 同步来源
     * @return
     */
    public List<String> getOrgidsByYyncSource(String syncSource){
        String sql = "select source_uuid from department where sync_source = '"+syncSource+"'";
        System.out.println(sql);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("sync_source", syncSource);
        NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(jdbcTemplate);
        List<String> orgIds = jdbc.queryForList(sql,paramMap,String.class);

        return orgIds;
    }

    public void delUserDept(List<String> delList) {
        DataSource dataSource = jdbcTemplate.getDataSource();
        BatchSqlUpdate bsu = new BatchSqlUpdate(dataSource,"DELETE FROM department_user WHERE user_id = ?");
        bsu.setBatchSize(512);
        bsu.setTypes(new int[]{Types.CHAR});
        for (String  m: delList) {
            bsu.update(new Object[]{
                    m,
            });
        }
        bsu.flush();

    }
    //TODO
    public void modiUserDept(HashMap<String,String> modiUDMap) {
        DataSource dataSource = jdbcTemplate.getDataSource();
        BatchSqlUpdate bsu = new BatchSqlUpdate(dataSource,"UPDATE department_user dept_id=? SET  WHERE user_id = ?");
        bsu.setBatchSize(512);
        bsu.setTypes(new int[]{Types.CHAR});
      /*  for (String  m: delList) {
            bsu.update(new Object[]{
                    m,
            });
        }*/
        if(modiUDMap.size()!=0){
            modiUDMap.entrySet()
                    .stream()
                    .forEach(entry -> bsu.update(new Object[]{
                            entry.getKey(),entry.getValue()
                    }));
        }
        bsu.flush();

    }

    public String setLastSyncTimeString (String syncSource,String desTableName) {
        //转换时间格式
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        //避免同步过程中有信息变化
        c.set(Calendar.HOUR_OF_DAY, (c.get(Calendar.HOUR_OF_DAY) - 1));
        Timestamp sqlTime = Timestamp.valueOf(simpleDate.format(c.getTime()));
        String sql = "UPDATE org_sync_relation SET LAST_SYNC_TIME= '"+sqlTime+"' WHERE SYNC_SOURCE = '"+syncSource+"' and SYNC_DESTINATION_TABLE='"+desTableName+"'";
        System.out.println(sql);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("SYNC_SOURCE", syncSource);
        paramMap.put("SYNC_DESTINATION_TABLE", desTableName);
        paramMap.put("LAST_SYNC_TIME", sqlTime);
        NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(jdbcTemplate);
        jdbc.update(sql,paramMap);
        return null;
    }




    public List<Map<String, Object>> getuser() {

        String sql = "select tenantCode,status,updatedAt,createdAt,code,organizationCode,username,name,description,email,phone,password from user";
        System.out.println(sql);
        Map<String, Object> paramMap = new HashMap<>();
        NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(jdbcTemplate);
        List<Map<String, Object>> columnsMap = jdbc.queryForList(sql,paramMap);

        return columnsMap;
    }

    /**
     * 根据同步来源获取已经同步的账号
     * @return
     */
    public List<String> getAccountsBySyncSrc(String syncSrc){
        String sql = "select source_account from sync_accounts where sync_src = '"+syncSrc+"'";
        System.out.println(sql);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("sync_src", syncSrc);
        NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(jdbcTemplate);
        List<String> accounts = jdbc.queryForList(sql,paramMap,String.class);
        return accounts;
    }

    /**
     * 批量删除 账号
     * @param acs
     * @param syncSrc
     */
    public void delAccountsInSyncAccounts(List<String>  acs, String syncSrc){
        DataSource dataSource = jdbcTemplate.getDataSource();
        BatchSqlUpdate bsu = new BatchSqlUpdate(dataSource,"UPDATE  sync_accounts SET deleted = 1 WHERE source_account= ? AND sync_src = ?");
        bsu.setBatchSize(512);
        bsu.setTypes(new int[]{Types.CHAR,Types.CHAR});
        for (String s:
                acs) {
            bsu.update(new Object[]{
                    s,
                    syncSrc
            });
        }

        bsu.flush();
    }

    /**
     * 批量插入 同步新增的账号
     */
    public void insertAccounts(List<String>  acs,String sync){
        DataSource dataSource = jdbcTemplate.getDataSource();
        BatchSqlUpdate bsu = new BatchSqlUpdate(dataSource,"INSERT INTO sync_accounts (source_account, sync_src, deleted) VALUES(?, ?, ?)");
        bsu.setBatchSize(512);
        bsu.setTypes(new int[]{Types.CHAR,Types.CHAR, Types.INTEGER});
        if(acs.size()!=0){
            acs
                    .stream()
                    .forEach(entry ->{
                        bsu.update(new Object[]{
                                entry,
                                sync,
                                0
                        });
                    });
        }
        bsu.flush();
    }


    public List<User> getMysqlUserList() {
        String sql = "SELECT uuid, ldapId loginName, mobile, NAME, mail email, NULL PASSWORD, ldap_password_updatetime createDate, 'EPC' `from` FROM USER WHERE descr = 'EPC'";
        System.out.println(sql);
        Map<String, Object> paramMap = new HashMap<>();
        NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(jdbcTemplate);
        List<Map<String, Object>> list = jdbc.queryForList(sql,paramMap);
        List<User> userList = new LinkedList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        if (list != null && list.size() > 0) {
            for (Map<String, Object> objectMap : list) {
                User user = new User();
                if (objectMap.get("uuid") != null) {
                    user.setId(objectMap.get("uuid").toString());
                }
                if (objectMap.get("loginName") != null) {
                    user.setLoginName(objectMap.get("loginName").toString());
                }
                if (objectMap.get("mobile") != null) {
                    user.setMobile(objectMap.get("mobile").toString());
                }
                if (objectMap.get("name") != null) {
                    user.setName(objectMap.get("name").toString());
                }
                if (objectMap.get("email") != null) {
                    user.setEmail(objectMap.get("email").toString());
                }
                if (objectMap.get("password") != null) {
                    user.setPassword(objectMap.get("password").toString());
                }
                if (objectMap.get("createDate") != null) {
                    try {
                        user.setCreateDate(sdf.parse(objectMap.get("createDate").toString()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if (objectMap.get("from") != null) {
                    user.setFrom(objectMap.get("from").toString());
                }
                userList.add(user);
            }
        }
        for (User u:
             userList) {
             u.setDeptId(getDeptIdByUserId(u.getId()));
        }
        return userList;
    }

    public List<String> getUserDelList(String descr) {
        String sql = "select source_account from sync_accounts where deleted = 1 and sync_src = '"+descr+"'";
        System.out.println(sql);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("sync_src", descr);
        NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(jdbcTemplate);
        List<String> accounts = jdbc.queryForList(sql,paramMap,String.class);
        return accounts;
    }

    /**
     * 填充部门id 如果为空 则默认放置到中国移动1001 下
     * @param userId
     * @return
     */
    public String getDeptIdByUserId(String userId){
        String sql = "select dept_id from department_user where user_id = '"+userId+"'";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("user_id", userId);
        NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(jdbcTemplate);
        String d = "1001";
        List<String> ds = jdbc.queryForList(sql,paramMap,String.class);
        if(com.aspire.ums.cmdb.common.StringUtils.isNotEmpty(ds)){
           d = ds.get(0);
        }
        d = StringUtils.isEmpty(d)?"1001":d;
        return d;
    }


}
