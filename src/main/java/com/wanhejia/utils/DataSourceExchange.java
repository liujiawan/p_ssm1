package com.wanhejia.utils;

import org.aspectj.lang.JoinPoint;

public class DataSourceExchange {
	/**
    *
    * @param point
    */
   public void before(JoinPoint point) {

       //获取目标对象的类类型
       Class<?> aClass = point.getTarget().getClass();
       //获取包名用于区分不同数据源
       String whichDataSource = aClass.getName();
       if (!"com.wanhejia.service.impl.UserServiceImpl".equals(whichDataSource)) {
           DataSourceHolder.setDataSources("dataSource1");
       } else {
           DataSourceHolder.setDataSources("dataSource2");
       }

   }


   /**
    * 执行后将数据源置为空
    */
   public void after() {
       DataSourceHolder.setDataSources(null);
   }
}
