package com.wanhejia.domain;

import java.sql.Timestamp;
public class User {
	 private Long id;
	 private String username;//用户名
	 private String accountname;//登录名
	 private String password;//密码；
	 private String credentialsSalt;
	 private String description;
	 private String locked;
	 private Timestamp creacreateTime;//创建时间
	 private int deletestatus;//逻辑删除状态0:存在1:删除
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAccountname() {
		return accountname;
	}
	public void setAccountname(String accountname) {
		this.accountname = accountname;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCredentialsSalt() {
		return credentialsSalt;
	}
	public void setCredentialsSalt(String credentialsSalt) {
		this.credentialsSalt = credentialsSalt;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLocked() {
		return locked;
	}
	public void setLocked(String locked) {
		this.locked = locked;
	}
	public Timestamp getCreacreateTime() {
		return creacreateTime;
	}
	public void setCreacreateTime(Timestamp creacreateTime) {
		this.creacreateTime = creacreateTime;
	}
	public int getDeletestatus() {
		return deletestatus;
	}
	public void setDeletestatus(int deletestatus) {
		this.deletestatus = deletestatus;
	}
	 
	 
	 
	 
}
