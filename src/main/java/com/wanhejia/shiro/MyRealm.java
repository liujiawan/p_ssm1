package com.wanhejia.shiro;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import com.wanhejia.domain.User;
import com.wanhejia.service.IPermissionService;
import com.wanhejia.service.IUserService;
/**
 * 自定义Realm,进行数据源配置
 * [描述信息] <br>
 * @author LiuJiawan <br>
 * @date 2016年6月30日 下午12:02:31 <br> <br>
 */
public class MyRealm extends AuthorizingRealm {
	@Resource
	private IUserService  userService;
	@Resource
	private IPermissionService permissionService;
	/**
	 * 只有需要验证权限时才会调用, 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.在配有缓存的情况下，只加载一次（进行权限的加载）
	 * [描述信息] <br>
	 * @author LiuJiawan <br>
	 * @date 2016年6月30日 下午12:02:31 <br> <br>
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
			Long userId=TokenManager.getUserId();
			//List<Resources> rs = resourcesMapper.findUserResourcess(userId);
			// 权限信息对象info,用来存放查出的用户的所有的角色（role）及权限（permission）
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			// 用户的角色集合
			// info.addRole("default");
			// 用户的角色集合
			// info.setRoles(user.getRolesName());
			// 用户的角色对应的所有权限，如果只使用角色定义访问权限
			Set<String> permissions = permissionService.findPermissionByUserId(userId);
			info.setStringPermissions(permissions);
			return info;
	}
	/**
	 * 进行用户登录的验证
	 * [描述信息] <br>
	 * @author LiuJiawan <br>
	 * @date 2016年6月30日 下午12:02:31 <br> <br>
	 */
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		MyAuthenticationToken myAuthenticationToken =(MyAuthenticationToken) token;
		List<User> userFormMaps =userService.findByNames(myAuthenticationToken.getUsername());
		if (userFormMaps.size() != 0) {
			if ("2".equals(userFormMaps.get(0).getLocked())) {
				throw new LockedAccountException(); // 帐号锁定
			}
			SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(userFormMaps.get(0), // 用户名
					userFormMaps.get(0).getPassword(), // 密码
					ByteSource.Util.bytes(myAuthenticationToken.getUsername() + "" + userFormMaps.get(0).getCredentialsSalt()),// salt=username+salt
					getName() // realm name
			);
			// 当验证都通过后，把用户信息放在session里
			return authenticationInfo;
		} else {
			throw new UnknownAccountException();// 没找到帐号
		}

	}
	/**
     * 更新用户授权信息缓存.
     */
	public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
		super.clearCachedAuthorizationInfo(principals);
	}
	/**
     * 更新用户信息缓存.
     */
	public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
		super.clearCachedAuthenticationInfo(principals);
	}

	/**
	 * 清除用户授权信息缓存.
	 */
	public void clearAllCachedAuthorizationInfo() {
		getAuthorizationCache().clear();
	}

	/**
	 * 清除用户信息缓存.
	 */
	public void clearAllCachedAuthenticationInfo() {
		getAuthenticationCache().clear();
	}
	
	/**
	 * 清空所有缓存
	 */
	public void clearCache(PrincipalCollection principals) {
		super.clearCache(principals);
	}
	/**
	 * 清空所有认证缓存
	 */
	public void clearAllCache() {
		clearAllCachedAuthenticationInfo();
		clearAllCachedAuthorizationInfo();
	}

}