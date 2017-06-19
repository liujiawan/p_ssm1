package com.wanhejia.service.impl;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.wanhejia.base.page.Pagination;
import com.wanhejia.base.page.dao.BaseDao;
import com.wanhejia.domain.User;
import com.wanhejia.service.IUserService;
import com.wanhejia.utils.PageData;
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
	
}
