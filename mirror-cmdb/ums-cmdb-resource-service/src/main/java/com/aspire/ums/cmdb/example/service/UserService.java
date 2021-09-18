package com.aspire.ums.cmdb.example.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.aspire.ums.cmdb.example.entity.User;

public interface UserService {
	
	List<User> getAll();
	
	User getOne(Long id);

	void insert(User user);

	void update(User user);

	void delete(Long id);
}
