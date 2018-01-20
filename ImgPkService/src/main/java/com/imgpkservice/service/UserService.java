package com.imgpkservice.service;

import java.util.List;

import com.imgpkservice.bean.User;

public interface UserService {

	int addUser(User user);

	List<User> findAllUser(int pageNum, int pageSize);
}