package com.imgpkservice.bean;

/**
 * 用户Bean
 * @author Administrator
 *
 */
public class User {
	private String userId;

	private String userName;

	private String imgPath;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
}