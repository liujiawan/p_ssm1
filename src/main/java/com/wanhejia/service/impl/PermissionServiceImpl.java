package com.wanhejia.service.impl;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import com.wanhejia.base.page.dao.BaseDao;
import com.wanhejia.service.IPermissionService;
@Service
public class PermissionServiceImpl extends BaseDao implements IPermissionService {
	@SuppressWarnings("unchecked")
	@Override
	public Set<String> findPermissionByUserId(Long userId) {
		List<String> list= (List<String>) super.findForList("com.wanhejia.dao.IPermissionDao.findPermissionByUserId", userId);
		Set<String> set=new HashSet<String>(list);
		return set;
	}

}
