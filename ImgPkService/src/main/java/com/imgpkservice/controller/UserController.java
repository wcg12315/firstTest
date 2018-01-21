package com.imgpkservice.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.gson.Gson;
import com.imgpkservice.bean.User;
import com.imgpkservice.service.UserService;

@Controller
@RequestMapping(value = "/user")
public class UserController {

	@Autowired
	private UserService userService;

	private final ResourceLoader resourceLoader;

	@Autowired
	public UserController(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/add", produces = { "application/json;charset=UTF-8" })
	public int addUser(User user) {
		return userService.addUser(user);
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/all", produces = { "application/json;charset=UTF-8" })
	public String findAllUser(@RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
		return new Gson().toJson(userService.findAllUser(pageNum, pageSize));
	}

	/**
	 * 文件上传具体实现方法;
	 * 
	 * @param file
	 * @return
	 */
	@RequestMapping("/upload")
	@ResponseBody
	public Object picUpload(ModelMap mv, @RequestParam(value = "pk_image", required = false) MultipartFile file,
			HttpServletRequest request, HttpServletResponse response) {

		MultipartHttpServletRequest params = ((MultipartHttpServletRequest) request);
		String userId = params.getParameter("userId");
		return userService.saveImg(userId, file);
	}// 显示图片的方法关键 匹配路径像 localhost:8080/b7c76eb3-5a67-4d41-ae5c-1642af3f8746.png

	@RequestMapping(method = RequestMethod.GET, value = "/{filePath:.+}")
	@ResponseBody
	public ResponseEntity<?> getFile(@PathVariable String filePath) {

		try {
			String storePath = userService.queryImgPath(filePath);
			return ResponseEntity.ok(resourceLoader.getResource("file:" + storePath));
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}
}