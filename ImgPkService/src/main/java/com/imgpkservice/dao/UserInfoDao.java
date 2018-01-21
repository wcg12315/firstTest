package com.imgpkservice.dao;

import java.util.List;
import java.util.Map;

import com.imgpkservice.bean.PictureInfo;
import com.imgpkservice.bean.User;

public interface UserInfoDao {
	/**
	 * 按照ID删除用户信息
	 * @param userId
	 * @return
	 */
	int deleteUserInfoByUserId(String userId);

	/**
	 * 新增和更新用户信息
	 * @param record
	 * @return
	 */
	int saveUserInfo(User record);

	/**
	 * 查询单个用户
	 * @param userId
	 * @return
	 */
	List<User> queryUserInfoByUserId(String userId);
	
	/**
	 * 查询所有用户
	 * @return
	 */
	List<User> queryAllUser();

	/**
	 * 保存用户图片
	 * @param record
	 * @return
	 */
	int savePictureInfo(PictureInfo record);

	/**
	 * 根据ID获取用户图片
	 * @param filters
	 * @return
	 */
	List<PictureInfo> queryPictureInfoById(Map<String, Object> filters);
}