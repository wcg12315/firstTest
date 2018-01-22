package com.imgpkservice.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.imgpkservice.bean.PictureScoreInfo;
import com.imgpkservice.bean.User;

public interface UserService {
	/**
	 * 保存用户数据
	 * @param user
	 * @return
	 */
	int addUser(User user);

	/**
	 * 获取用户列表
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	List<User> findAllUser(int pageNum, int pageSize);

	/**
	 * 保存图片信息
	 * @param userId
	 * @param file
	 * @return
	 */
	Object saveImg(String userId, MultipartFile file);

	/**
	 * 获取图片路径
	 * @param filePath
	 * @return
	 */
	String queryImgPath(String filePath);

	/**
	 * 分页获取排行榜数据
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	List<PictureScoreInfo> queryAllRankInfos(int pageNum, int pageSize);

	/**
	 * 获取明细数据
	 * @param userId
	 * @return
	 */
	PictureScoreInfo showMyBestDetails(String userId);
}