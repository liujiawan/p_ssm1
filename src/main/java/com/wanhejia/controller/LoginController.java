package com.wanhejia.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.wanhejia.domain.Resources;
import com.wanhejia.domain.User;
import com.wanhejia.service.IResourcesService;
import com.wanhejia.shiro.TokenManager;
import com.wanhejia.utils.TreeUtil;
/**
 * 
 * [描述信息] <br>
 * @author LiuJiawan <br>
 * @date 2016年7月1日 下午4:22:53 <br> <br>
 */
@Controller
@RequestMapping("/")
public class LoginController  {
	@Resource
	private IResourcesService resourcesService;
	/**
	 * 登录之后验证
	 * @param username
	 * @param password
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "login")
	public String login(String username, String password, HttpServletRequest request) {
		request.removeAttribute("error");
		Object failureMsg = request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
		if (failureMsg != null) {
			String msg = (String)failureMsg;
			if (msg.equals(UnknownAccountException.class.getName())) {
				request.setAttribute("error", "用户名或密码错误！");
			}else if (msg.equals(IncorrectCredentialsException.class.getName())) {
				request.setAttribute("error", "用户名或密码错误！");
			}else if(msg.equals(AuthenticationException.class.getName())){
				request.setAttribute("error", "用户名或密码不能为空!");
			}
		}
		if(SecurityUtils.getSubject().isAuthenticated()){
			request.setAttribute("hadLogin", "1");
			return "/login/login";
		}
		return "/login/login";
	}
	/**
	 * 兼容ie9(包含)
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "ie")
	public String ie(HttpServletRequest request) {
		return "/ie";
	}
	/**
	 * 兼容ie9(包含)
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "unauthorized")
	public String unauthorized(HttpServletRequest request) {
		return "/unauthorized";
	}
	/**
	 * 欢迎光临
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "index")
	public String index(HttpServletRequest request) {
		User user=TokenManager.getToken();
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("userId", user.getId());
		List<Resources> list = resourcesService.findAll(map);
		TreeUtil treeUtil = new TreeUtil();
		List<Resources> ns = treeUtil.getChildResourcess(list, 0);
		request.setAttribute("list", ns);
		return "/index/index";
	}
	
//	/**
//	 * 首页
//	 * @param request
//	 * @return
//	 */
//	@RequestMapping(value = "index")
//	public String index(HttpServletRequest request) {
//		return "/WEB-INF/jsp/index";
//	}
	/**
	 * 退出
	 * @return
	 */
	@RequestMapping(value = "logout", method = RequestMethod.GET)
	public String logout() {
		// 使用权限管理工具进行用户的退出，注销登录
		SecurityUtils.getSubject().logout(); // session
		return "redirect:login.shtml";
	}
}
