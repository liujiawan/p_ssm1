package com.wanhejia.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wanhejia.base.page.Pagination;
import com.wanhejia.domain.User;
import com.wanhejia.service.IUserService;
import com.wanhejia.utils.PageData;

/**
 * 
 * [描述信息] <br>
 * @author LiuJiawan <br>
 * @date 2016年6月30日 下午12:02:13 <br> <br>
 */
@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private IUserService userService;
	@RequestMapping(value = "list")
	public String index(HttpServletRequest request,Integer pageNo) {
		//PageData pd = new PageData(request);
		//pd.put("locked", "1");
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("locked", "1");
		//List<PageData> list=userService.findPage(pd);
		Pagination<User> list=userService.findAllAndPage(map,pageNo,10);
		request.setAttribute("list", list);
		return "/WEB-INF/jsp/user/list";
	}
}
