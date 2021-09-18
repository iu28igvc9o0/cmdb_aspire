package com.aspire.ums.cmdb.example.mapper;

import java.util.List;

import com.aspire.ums.cmdb.example.entity.User;

public interface UserMapper {
	
	List<User> getAll();
	
	User getOne(Long id);

	void insert(User user);

	void update(User user);

	void delete(Long id);

}

/**package com.aspire.ums.cmdb.example.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.aspire.ums.cmdb.example.entity.User;
import com.aspire.ums.cmdb.example.entity.UserSexEnum;

public interface UserMapper {

    @Select("SELECT * FROM t_users")
    @Results({
        @Result(property = "userSex",  column = "user_sex", javaType = UserSexEnum.class),
        @Result(property = "nickName", column = "nick_name")
    })
    List<User> getAll();

    @Select("SELECT * FROM t_users WHERE id = #{id}")
    @Results({
        @Result(property = "userSex",  column = "user_sex", javaType = UserSexEnum.class),
        @Result(property = "nickName", column = "nick_name")
    })
    User getOne(Long id);

    @Insert("INSERT INTO t_users(userName,passWord,user_sex) VALUES(#{userName}, #{passWord}, #{userSex})")
    void insert(User user);

    @Update("UPDATE t_users SET userName=#{userName},nick_name=#{nickName} WHERE id =#{id}")
    void update(User user);

    @Delete("DELETE FROM t_users WHERE id =#{id}")
    void delete(Long id);

}*/