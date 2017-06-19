package com.wanhejia.service;

import java.util.Set;

public interface IPermissionService {

	Set<String> findPermissionByUserId(Long userId);

}
