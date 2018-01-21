package com.imgpkservice.service.impl;

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

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageHelper;
import com.imgpkservice.bean.PictureInfo;
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
				/*
				 * 这段代码执行完毕之后，图片上传到了工程的跟路径； 大家自己扩散下思维，如果我们想把图片上传到 d:/files大家是否能实现呢？ 等等;
				 * 这里只是简单一个例子,请自行参考，融入到实际中可能需要大家自己做一些思考，比如： 1、文件路径； 2、文件名； 3、文件格式; 4、文件大小的限制;
				 */
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
		List<PictureInfo> pictureInfos = userInfoDao.queryPictureInfoById(filters);
		if (CollectionUtils.isNotEmpty(pictureInfos)) {
			return pictureInfos.get(0).getStorePath();
		}
		return null;
	}
}