package com.wanhejia.shiro;

import java.io.PrintWriter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
/**
 * 重写验证的方法
 * @author jack
 *
 */
public class FormAuthenticationFilter extends org.apache.shiro.web.filter.authc.FormAuthenticationFilter{ 
	   public static final String DEFAULT_CAPTCHA_PARAM = "captcha"; 
	   private String captchaParam = DEFAULT_CAPTCHA_PARAM; 
	   public String getCaptchaParam() { 
	      return captchaParam; 
	   } 
	   public void setCaptchaParam(String captchaParam) { 
	      this.captchaParam = captchaParam; 
	   } 
	   protected String getCaptcha(ServletRequest request) { 
	      return WebUtils.getCleanParam(request, getCaptchaParam()); 
	   } 
	   // 创建 Token 
	   protected MyAuthenticationToken createToken(ServletRequest request,  
	            ServletResponse response) {  
	        String username = getUsername(request);  
	        String password = getPassword(request);  
	        String captcha = getCaptcha(request);  
	        boolean rememberMe = isRememberMe(request);  
	        String host = getHost(request);  
	        return new MyAuthenticationToken(username,  
	                password.toCharArray(), rememberMe, host, captcha);  
	    }  
	  
//	   // 验证码校验
//	   protected void doCaptchaValidate( HttpServletRequest request 
//	      ,CaptchaUsernamePasswordToken token ){ 
//
//	      String captcha = (String)request.getSession().getAttribute( 
//	         com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY); 
//	      
//	      if( captcha!=null && 
//	         !captcha.equalsIgnoreCase(token.getCaptcha()) ){ 
//	         throw new IncorrectCaptchaException ("验证码错误！"); 
//	      } 
//	   } 
	   // 认证
	   protected boolean executeLogin(ServletRequest request, 
	      ServletResponse response) throws Exception { 
	      MyAuthenticationToken token = createToken(request, response); 
	      try { 
	         //doCaptchaValidate( (HttpServletRequest)request,token ); 
	         Subject subject = getSubject(request, response); 
	         subject.login(token); 
	         return onLoginSuccess(token, subject, request, response); 
	      } catch (AuthenticationException e) { 
	         return onLoginFailure(token, e, request, response); 
	      } 
	   } 
//	   protected boolean onLoginSuccess(AuthenticationToken token,
//				Subject subject, ServletRequest request, ServletResponse response)
//				throws Exception {
//			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
//			HttpServletResponse httpServletResponse = (HttpServletResponse) response;
//
//			if (!"XMLHttpRequest".equalsIgnoreCase(httpServletRequest
//					.getHeader("X-Requested-With"))) {// 不是ajax请求
//				issueSuccessRedirect(request, response);
//			} else {
//				httpServletResponse.setCharacterEncoding("UTF-8");
//				PrintWriter out = httpServletResponse.getWriter();
//				out.println("{success:true,message:'登入成功'}");
//				out.flush();
//				out.close();
//			}
//			return false;
//		}
	   
	 }
