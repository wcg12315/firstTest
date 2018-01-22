package com.imgpkservice.service.impl;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageHelper;
import com.imgpkservice.bean.PictureInfo;
import com.imgpkservice.bean.PictureScoreInfo;
import com.imgpkservice.bean.User;
import com.imgpkservice.dao.UserInfoDao;
import com.imgpkservice.service.UserService;

/**
 * Created by Administrator on 2017/8/16.
 */
@Service(value = "userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserInfoDao userInfoDao;// 这里会报错，但是并不会影响

	@Override
	public int addUser(User user) {
		downloadPicture(user);
		return userInfoDao.saveUserInfo(user);
	}

	/**
	 * 根据url下载图片
	 * 
	 * @param urlList
	 * @param path
	 */
	private void downloadPicture(User user) {
		URL url = null;
		try {
			url = new URL(user.getImgPath());
			DataInputStream dataInputStream = new DataInputStream(url.openStream());

			// 保存到本地
			String path = "D:/Files/" + user.getUserId() + "/";
			String storePath = path + user.getUserId() + ".jpg";
			FileOutputStream fileOutputStream = new FileOutputStream(new File(storePath));
			ByteArrayOutputStream output = new ByteArrayOutputStream();

			byte[] buffer = new byte[1024];
			int length;

			while ((length = dataInputStream.read(buffer)) > 0) {
				output.write(buffer, 0, length);
			}
			fileOutputStream.write(output.toByteArray());
			dataInputStream.close();
			fileOutputStream.close();

			PictureInfo pictureInfo = new PictureInfo();
			pictureInfo.setPicId(user.getUserId() + ".jpg");
			pictureInfo.setRelationPath(storePath);
			pictureInfo.setStorePath(storePath);
			pictureInfo.setUserId(user.getUserId());
			userInfoDao.savePictureInfo(pictureInfo);

			// 评分
			getImagePixel(storePath, pictureInfo.getPicId());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * 这个方法中用到了我们开头配置依赖的分页插件pagehelper 很简单，只需要在service层传入参数，然后将参数传递给一个插件的一个静态方法即可；
	 * pageNum 开始页数 pageSize 每页显示的数据条数
	 */
	@Override
	public List<User> findAllUser(int pageNum, int pageSize) {
		// 将参数传给这个方法就可以实现物理分页了，非常简单。
		PageHelper.startPage(pageNum, pageSize);
		return userInfoDao.queryAllUser();
	}

	@Override
	public Object saveImg(String userId, MultipartFile file) {
		if (!file.isEmpty()) {
			try {
				// 保存到本地
				String path = "D:/Files/" + userId + "/";
				File folders = new File(path);
				if (!folders.exists()) {
					System.out.println("创建文件夹结果：" + folders.mkdirs());
				}

				String finalName = file.getOriginalFilename();
				String storePath = path + file.getOriginalFilename();
				String relationPath = "/" + userId + "/" + file.getOriginalFilename();

				BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(storePath)));
				System.out.println(file.getName());
				out.write(file.getBytes());
				out.flush();
				out.close();

				PictureInfo pictureInfo = new PictureInfo();
				pictureInfo.setPicId(finalName);
				pictureInfo.setRelationPath(relationPath);
				pictureInfo.setStorePath(storePath);
				pictureInfo.setUserId(userId);
				userInfoDao.savePictureInfo(pictureInfo);

				// 评分
				getImagePixel(storePath, finalName);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return "上传失败," + e.getMessage();
			} catch (IOException e) {
				e.printStackTrace();
				return "上传失败," + e.getMessage();
			}

			return "上传成功";

		} else {
			return "上传失败，因为文件是空的.";
		}
	}

	@Override
	public String queryImgPath(String filePath) {
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put("picId", filePath);

		// 根据ID获取本地路径，之后可以快捷呈现出来
		List<PictureInfo> pictureInfos = userInfoDao.queryPictureInfoById(filters);
		if (CollectionUtils.isNotEmpty(pictureInfos)) {
			return pictureInfos.get(0).getStorePath();
		}
		return null;
	}

	/**
	 * 读取一张图片的RGB值
	 * 
	 * @throws Exception
	 */
	private void getImagePixel(String storePath, String picId) {
		File file = new File(storePath);
		BufferedImage bi = null;
		try {
			// 加载图片数据
			bi = ImageIO.read(file);
			int width = bi.getWidth();
			int height = bi.getHeight();
			int minx = bi.getMinX();
			int miny = bi.getMinY();

			// 读取二进制
			byte[] datas = FileUtils.readFileToByteArray(file);
			int size = datas.length;

			// 大小超过一兆算满分
			if (size > 1000000) {
				size = 10;
			} else {
				size = size % 10;
			}

			int colour = 0;
			int shadow = 0;
			int activity = 0;
			int theme = 0;
			int multiple = 0;

			// TODO:根据RGB计算分数，以后可以优化，这一块是核心
			int[] rgb = new int[3];
			for (int i = minx; i < width; i++) {
				for (int j = miny; j < height; j++) {
					int pixel = bi.getRGB(i, j); // 下面三行代码将一个数字转换为RGB数字
					rgb[0] = (pixel & 0xff0000) >> 16;
					rgb[1] = (pixel & 0xff00) >> 8;
					rgb[2] = (pixel & 0xff);

					if (rgb[0] >= 0 && rgb[0] < 131 && rgb[1] >= 0 && rgb[1] < 175 && rgb[2] >= 0 && rgb[2] < 155) {
						colour++;
					} else if (rgb[0] >= 131 && rgb[0] < 200 && rgb[1] >= 175 && rgb[1] < 200 && rgb[2] >= 154
							&& rgb[2] < 173) {
						shadow++;
					} else if (rgb[0] >= 200 && rgb[0] < 249 && rgb[1] >= 200 && rgb[1] < 205 && rgb[2] >= 101
							&& rgb[2] < 154) {
						activity++;
					} else if (rgb[0] >= 249 && rgb[0] < 252 && rgb[1] >= 0 && rgb[1] < 67 && rgb[2] >= 0
							&& rgb[2] < 101) {
						theme++;
					} else {
						multiple++;
					}
				}
			}
			colour = colour % 10;
			shadow = shadow % 10;
			activity = activity % 10;
			theme = theme % 10;
			multiple = multiple % 10;

			if (colour == 0) {
				colour = 8;
			}
			if (shadow == 0) {
				shadow = 8;
			}
			if (activity == 0) {
				activity = 8;
			}
			if (theme == 0) {
				theme = 8;
			}
			if (multiple == 0) {
				multiple = 8;
			}

			// 保存得分
			PictureScoreInfo scoreInfo = new PictureScoreInfo();
			scoreInfo.setActivity(activity);
			scoreInfo.setColour(colour);
			scoreInfo.setMultiple(multiple);
			scoreInfo.setPicId(picId);
			scoreInfo.setShadow(shadow);
			scoreInfo.setSize(size);
			scoreInfo.setTheme(theme);
			userInfoDao.savePictureScoreInfo(scoreInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<PictureScoreInfo> queryAllRankInfos(int pageNum, int pageSize) {
		// 将参数传给这个方法就可以实现物理分页了，非常简单。
		PageHelper.startPage(pageNum, pageSize);
		return userInfoDao.queryAllRankInfos();
	}

	@Override
	public PictureScoreInfo showMyBestDetails(String userId) {
		Map<String, Object> filters = new HashMap<String, Object>();
		filters.put("userId", userId);
		List<PictureScoreInfo> scoreInfos = userInfoDao.queryUserBestRankInfos(filters);
		if (CollectionUtils.isNotEmpty(scoreInfos)) {
			return scoreInfos.get(0);
		} else {
			return new PictureScoreInfo();
		}
	}
}