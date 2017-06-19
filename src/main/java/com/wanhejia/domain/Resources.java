package com.wanhejia.domain;

import java.util.List;

public class Resources {
  private Long id;
  private Long parentId;//父类id（对应id）
  private String resUrl;//路径
  private String name;//名称
  private String resKey;//为了保证唯一性
  private String remark;//备注
  private List<Resources> children;
public List<Resources> getChildren() {
	return children;
}
public void setChildren(List<Resources> children) {
	this.children = children;
}
public Long getId() {
	return id;
}
public void setId(Long id) {
	this.id = id;
}
public Long getParentId() {
	return parentId;
}
public void setParentId(Long parentId) {
	this.parentId = parentId;
}
public String getResUrl() {
	return resUrl;
}
public void setResUrl(String resUrl) {
	this.resUrl = resUrl;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getRemark() {
	return remark;
}
public String getResKey() {
	return resKey;
}
public void setResKey(String resKey) {
	this.resKey = resKey;
}
public void setRemark(String remark) {
	this.remark = remark;
}
 
}
