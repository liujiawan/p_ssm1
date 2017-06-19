package com.wanhejia.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.wanhejia.base.page.dao.BaseDao;
import com.wanhejia.domain.Resources;
import com.wanhejia.service.IResourcesService;
@Service
public class ResourcesServiceImpl  extends BaseDao  implements IResourcesService{
	@Override
	public List<Resources> findAll(Map<String,Object> map) {
		return (List<Resources>) super.findForList("com.wanhejia.dao.IResourcesDao.findAllResource", map);
		
	}

}
