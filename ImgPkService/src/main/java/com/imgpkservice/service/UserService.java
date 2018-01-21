package com.imgpkservice.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.imgpkservice.bean.User;

public interface UserService {

	int addUser(User user);

	List<User> findAllUser(int pageNum, int pageSize);

	Object saveImg(String userId, MultipartFile file);

	String queryImgPath(String filePath);
}