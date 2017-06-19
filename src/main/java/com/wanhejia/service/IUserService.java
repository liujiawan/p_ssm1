package com.wanhejia.service;
import java.util.List;
import java.util.Map;

import com.wanhejia.base.page.Pagination;
import com.wanhejia.domain.User;
import com.wanhejia.utils.PageData;

public interface IUserService {
	List<PageData> findPage(PageData pd) ;
	List<User> findByNames(String username);
	Pagination<User> findAllAndPage(Map<String, Object> map, Integer pageNo,
			Integer pageSize);
}
