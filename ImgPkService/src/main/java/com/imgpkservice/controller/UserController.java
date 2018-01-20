package com.imgpkservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.imgpkservice.bean.User;
import com.imgpkservice.service.UserService;

@Controller
@RequestMapping(value = "/user")
public class UserController {

	@Autowired
	private UserService userService;

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/add", produces = { "application/json;charset=UTF-8" })
	public int addUser(User user) {
		return userService.addUser(user);
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/all", produces = { "application/json;charset=UTF-8" })
	public String findAllUser(@RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {
		return new Gson().toJson(userService.findAllUser(pageNum, pageSize));
	}
}