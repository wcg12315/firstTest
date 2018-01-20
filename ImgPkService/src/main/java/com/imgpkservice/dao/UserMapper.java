package com.imgpkservice.dao;

import java.util.List;

import com.imgpkservice.bean.User;

public interface UserMapper {
	int deleteByPrimaryKey(Integer userId);

	int insert(User record);

	int insertSelective(User record);

	User selectByPrimaryKey(Integer userId);

	int updateByPrimaryKeySelective(User record);

	int updateByPrimaryKey(User record);

	// 这个方式我自己加的
	List<User> selectAllUser();
}