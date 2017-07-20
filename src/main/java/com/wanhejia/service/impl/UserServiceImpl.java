package com.wanhejia.service.impl;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.wanhejia.base.page.Pagination;
import com.wanhejia.base.page.dao.BaseDao;
import com.wanhejia.domain.User;
import com.wanhejia.service.IUserService;
import com.wanhejia.utils.PageData;
import org.springframework.transaction.annotation.Transactional;

/**
 * 继承BaseDao泛型是IUserDao;
 * @author jack
 *
 */
@Service("userService")
public class UserServiceImpl extends BaseDao implements IUserService {
	public List<PageData> findPage(PageData pd) {
		try {
			return (List<PageData>) super.findForList("com.wanhejia.dao.IUserDao.findAll", pd);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public List<User> findByNames(String username) {
			return (List<User>) super.findForList("com.wanhejia.dao.IUserDao.findByNames", username);
	}
	public Pagination<User> findAllAndPage(Map<String, Object> map,
			Integer pageNo, Integer pageSize) {
		return super.findPage("com.wanhejia.dao.IUserDao.findAll", "com.wanhejia.dao.IUserDao.findCount", map, pageNo, pageSize);
	}
	@Transactional
	public Integer updateUser(User user) {
		//如果我这样写报错，执行回滚return Integer.parseInt((String) super.update("com.wanhejia.dao.IUserDao.updateUser",user));
		return (Integer)super.update("com.wanhejia.dao.IUserDao.updateUser",user);
	}
	//这种情况下插入成功，但是会出现异常
	public Integer updateUser1(User user) {
		//如果我这样写报错，执行回滚return Integer.parseInt((String) super.update("com.wanhejia.dao.IUserDao.updateUser",user));
		return Integer.parseInt((String) super.update("com.wanhejia.dao.IUserDao.updateUser",user));
	}
	@Transactional
	//这种情况下插入不成功
	public Integer updateUser2(User user) {
		//如果我这样写报错，执行回滚return Integer.parseInt((String) super.update("com.wanhejia.dao.IUserDao.updateUser",user));
		return Integer.parseInt((String) super.update("com.wanhejia.dao.IUserDao.updateUser",user));
	}
}
