package com.wanhejia.service;

import java.util.List;
import java.util.Map;

import com.wanhejia.domain.Resources;

public interface  IResourcesService {

	List<Resources> findAll(Map<String,Object> map);

}
