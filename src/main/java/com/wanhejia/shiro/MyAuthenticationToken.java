package com.wanhejia.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;
/**
 *重写UsernamPasswordToken
 * @author jack
 *
 */
public class MyAuthenticationToken extends UsernamePasswordToken{
	private String captcha;  //验证码
    public MyAuthenticationToken(String username, char[] password,  
            boolean rememberMe, String host, String captcha) {  
        super(username, password, rememberMe, host);  
        this.captcha = captcha;  
    }  
    public String getCaptcha() {  
        return captcha;  
    }  
    public void setCaptcha(String captcha) {  
        this.captcha = captcha;  
    }  
}
