package com.imgpkservice.bean;

/**
 * 照片Bean
 * @author Administrator
 *
 */
public class PictureInfo {
	private String picId;

	private String userId;

	private String storePath;

	private String relationPath;

	public String getPicId() {
		return picId;
	}

	public void setPicId(String picId) {
		this.picId = picId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getStorePath() {
		return storePath;
	}

	public void setStorePath(String storePath) {
		this.storePath = storePath;
	}

	public String getRelationPath() {
		return relationPath;
	}

	public void setRelationPath(String relationPath) {
		this.relationPath = relationPath;
	}
}