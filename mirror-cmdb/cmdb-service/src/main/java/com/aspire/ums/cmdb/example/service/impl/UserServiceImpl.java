package com.aspire.ums.cmdb.example.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aspire.ums.cmdb.example.entity.User;
import com.aspire.ums.cmdb.example.entity.UserSexEnum;
import com.aspire.ums.cmdb.example.mapper.UserMapper;
import com.aspire.ums.cmdb.example.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserMapper userMapper;
	
	@Override
	public List<User> getAll() {
		// TODO Auto-generated method stub
		return userMapper.getAll();
	}

	@Override
	public User getOne(Long id) {
		// TODO Auto-generated method stub
		return userMapper.getOne(id);
	}

	@Override
	public void insert(User user) {
		User user1 = new User();
		user1.setUserName("user1");
		user1.setPassWord("aaaa1111");
		user1.setUserSex(UserSexEnum.MAN);
		userMapper.insert(user1);
		
		User user2 = new User();
		user2.setUserName("user2");
		user2.setPassWord("aaaa1111aaaa1111aaaa1111aaaa1111aaaa1111aaaa1111aaaa1111aaaa1111aaaa1111");
		user2.setUserSex(UserSexEnum.MAN);
		userMapper.insert(user2);

	}

	@Override
	public void update(User user) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub

	}

}
