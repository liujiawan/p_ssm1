package com.wanhejia.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.util.ByteSource;

import java.util.concurrent.atomic.AtomicInteger;
/**
 * 进行输入用户名和密码的验证（如果输入失败次数大于5会有提示信息）
 * [描述信息] <br>
 * @author LiuJiawan <br>
 * @date 2016年6月30日 下午12:05:57 <br> <br>
 */
public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {
    private Cache<String, AtomicInteger> passwordRetryCache;

    public RetryLimitHashedCredentialsMatcher(CacheManager cacheManager) {
        passwordRetryCache = cacheManager.getCache("passwordRetryCache");
    }
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token,
        AuthenticationInfo info) {
        String username = (String) token.getPrincipal();
        // retry count + 1
        AtomicInteger retryCount = passwordRetryCache.get(username);
        //如果失败则进行+1操作
        if (retryCount == null) {
            retryCount = new AtomicInteger(0);
            passwordRetryCache.put(username, retryCount);
        }
        if (retryCount.incrementAndGet() > 5) {
            // if retry count > 5 throw
            throw new ExcessiveAttemptsException();
        }
        boolean matches = super.doCredentialsMatch(token, info);
        if (matches) {
            passwordRetryCache.remove(username);
        }
        return matches;
    }

	public String buildCredentials(String userName, String password,String credentialsSalt) {
		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(userName,password,ByteSource.Util.bytes(userName + credentialsSalt),userName);
		AuthenticationToken token = new UsernamePasswordToken(userName, password);
		return super.hashProvidedCredentials(token, authenticationInfo).toString();
    }
}
