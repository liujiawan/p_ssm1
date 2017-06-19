package com.wanhejia.base.page.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import com.wanhejia.base.page.MysqlDialect;
import com.wanhejia.base.page.Pagination;
import com.wanhejia.utils.LoggerUtils;
import com.wanhejia.utils.StringUtils;
public class BaseDao extends SqlSessionDaoSupport {
	//private String NAMESPACE;
	final static  Class<? extends Object> SELF = BaseDao.class;
	protected final Log logger = LogFactory.getLog(BaseDao.class);
	/**默认的查询Sql id*/
	/**默认的查询Count sql id**/
	@Resource  
    private SqlSessionTemplate sqlSessionTemplate;  
	@Resource
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory){
	        super.setSqlSessionFactory(sqlSessionFactory);
	}
	/**
	 * 根据Sql id 去查询 分页对象
	 * @param sqlId			对应mapper.xml 里的Sql Id
	 * @param params		参数<String,Object>
	 * @param pageNo		number
	 * @param pageSize		size
	 * @return
	 */
	public Pagination findByPageBySqlId(String sqlId,
			Map<String, Object> params, Integer pageNo, Integer pageSize) {
		pageNo = null == pageNo ? 1 : pageNo;
		pageSize = null == pageSize ? 10 : pageSize;
		Pagination page = new Pagination();
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		Configuration c = sqlSessionTemplate.getConfiguration();
		int offset = (page.getPageNo() - 1) * page.getPageSize();
		String page_sql = String.format(" limit %s , %s", offset,pageSize);
		params.put("page_sql", page_sql);
		BoundSql boundSql = c.getMappedStatement(sqlId).getBoundSql(params);
		String sqlcode = boundSql.getSql();
		LoggerUtils.fmtDebug(SELF, "findByPageBySqlId sql : %s",sqlcode );
		String countCode = "",countId = "";
		BoundSql countSql = null;
		/**
		 * sql id 和 count id 用同一个
		 */
		if (StringUtils.isBlank(sqlId)) {
			countCode = sqlcode;
			countSql = boundSql;
		} else {
			countId = sqlId;
			Map<String,Object> countMap = new HashMap<String,Object>();
			countMap.putAll(params);
			countMap.remove("page_sql");//去掉，分页的参数。
			countSql = c.getMappedStatement(countId).getBoundSql(countMap);
			countCode = countSql.getSql();
		}
		try {
			Connection conn = sqlSessionTemplate.getConnection();
			List<?> resultList = sqlSessionTemplate.selectList(sqlId, params);
			page.setList(resultList);
			PreparedStatement ps = getPreparedStatement(countCode, countSql
					.getParameterMappings(), params, conn);
			ps.execute();
			ResultSet set = ps.getResultSet();
			while (set.next()) {
				page.setTotalCount(set.getInt(1));
			}
		} catch (Exception e) {
			LoggerUtils.error(SELF, "jdbc.error.code.findByPageBySqlId",e);
		}
		return page;
	}

	/**
	 * 根据Sql ID 查询List，而不要查询分页的页码
	 * @param sqlId			对应mapper.xml 里的Sql Id[主语句]
	 * @param params		参数<String,Object>
	 * @param pageNo		number
	 * @param pageSize		size
	 * @return
	 */
	public List findList(String sqlId, Map<String, Object> params,
			Integer pageNo, Integer pageSize) {
		pageNo = null == pageNo ? 1 : pageNo;
		pageSize = null == pageSize ? 10 : pageSize;
		int offset = (pageNo - 1) * pageSize;
		String page_sql = String.format(" limit %s , %s", offset,pageSize);
		params.put("page_sql", page_sql);
		//sqlId = String.format("%s.%s", NAMESPACE,sqlId) ;
		List resultList = sqlSessionTemplate.selectList(sqlId, params);
		return resultList;
	}
	/**
	 * 分页
	 * 
	 * @param sqlId
	 *            主语句
	 * @param countId
	 *            Count 语句
	 * @param params
	 *            参数
	 * @param pageNo
	 *            第几页
	 * @param pageSize每页显示多少条
	 * @param requiredType	返回的类型[可以不传参]
	 * @return
	 */
	public Pagination findPage(String sqlId, String countId,
			Map<String, Object> params, Integer pageNo, Integer pageSize) {
		pageNo = null == pageNo ? 1 : pageNo;
		pageSize = null == pageSize ? 10 : pageSize;
		Pagination page = new Pagination();
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		Configuration c = sqlSessionTemplate.getConfiguration();
		int offset = (page.getPageNo() - 1) * page.getPageSize();
		String page_sql = String.format(" limit  %s , %s ", offset,pageSize);
		params.put("page_sql", page_sql);
		//sqlId =  String.format("%s.%s", NAMESPACE,sqlId) ;
		BoundSql boundSql = c.getMappedStatement(sqlId).getBoundSql(params);
		String sqlcode = boundSql.getSql();
		LoggerUtils.fmtDebug(SELF, "findPage sql : %s",sqlcode );
		String countCode = "";
		BoundSql countSql = null;
		if (StringUtils.isBlank(countId)) {
			countCode = sqlcode;
			countSql = boundSql;
		} else {
			//countId = String.format("%s.%s", NAMESPACE,countId) ;
			countSql = c.getMappedStatement(countId).getBoundSql(params);
			countCode = countSql.getSql();
		}
		try {
			Connection conn = sqlSessionTemplate.getConfiguration().getEnvironment().getDataSource().getConnection();    
			//Connection conn = sqlSessionTemplate.getConnection();
			List resultList = sqlSessionTemplate.selectList(sqlId, params); 
			page.setList(resultList);
			/**
			 * 处理Count
			 */
			PreparedStatement ps = getPreparedStatement4Count(countCode, countSql
					.getParameterMappings(), params, conn);
			ps.execute();
			ResultSet set = ps.getResultSet();

			while (set.next()) {
				page.setTotalCount(set.getInt(1));
			}
		} catch (Exception e) {
			LoggerUtils.error(SELF, "jdbc.error.code.findByPageBySqlId",e);
		}
		return page;

	}
	
	/**
	 * 组装
	 * @param sql
	 * @param parameterMappingList
	 * @param params
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	private PreparedStatement getPreparedStatement(String sql,
			List<ParameterMapping> parameterMappingList,
			Map<String, Object> params, Connection conn) throws SQLException {
		/**
		 * 分页根据数据库分页
		 */
		MysqlDialect o = new MysqlDialect();

		PreparedStatement ps = conn.prepareStatement(o.getCountSqlString(sql));
		int index = 1;
		for (int i = 0; i < parameterMappingList.size(); i++) {
			ps.setObject(index++, params.get(parameterMappingList.get(i)
					.getProperty()));
		}
		return ps;
	}
	/**
	 * 分页查询Count 直接用用户自己写的Count sql
	 * @param sql
	 * @param parameterMappingList
	 * @param params
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	private PreparedStatement getPreparedStatement4Count(String sql,
			List<ParameterMapping> parameterMappingList,
			Map<String, Object> params, Connection conn) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(StringUtils.trim(sql));
		int index = 1;
		for (int i = 0; i < parameterMappingList.size(); i++) {
			ps.setObject(index++, params.get(parameterMappingList.get(i)
					.getProperty()));
		}
		return ps;
	}
	/**
	 * 保存对象
	 * @param str
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public Object save(String str, Object obj) throws Exception {
		return sqlSessionTemplate.insert(str, obj);
	}
	
	/**
	 * 批量更新
	 * @param str
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public Object batchSave(String str, List objs )throws Exception{
		return sqlSessionTemplate.insert(str, objs);
	}
	
	/**
	 * 修改对象
	 * @param str
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public Object update(String str, Object obj) {
		return sqlSessionTemplate.update(str, obj);
	}

	/**
	 * 批量更新
	 * @param str
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public void batchUpdate(String str, List objs ){
		//批量执行器
		try{
			if(objs!=null){
				for(int i=0,size=objs.size();i<size;i++){
					sqlSessionTemplate.update(str, objs.get(i));
				}
				sqlSessionTemplate.flushStatements();
				sqlSessionTemplate.commit();
				sqlSessionTemplate.clearCache();
			}
		}finally{
			sqlSessionTemplate.close();
		}
	}
	
	/**
	 * 批量更新
	 * @param str
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public Object batchDelete(String str, List objs ){
		try{
			return sqlSessionTemplate.delete(str, objs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 删除对象 
	 * @param str
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public Object delete(String str, Object obj) {
		try{
			return sqlSessionTemplate.delete(str, obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	 
	/**
	 * 查找对象
	 * @param str
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public Object findForObject(String str, Object obj)  {
		try{
			return sqlSessionTemplate.selectOne(str, obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 查找对象
	 * @param str
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public Object findForList(String str, Object obj)  {
		try{
			return sqlSessionTemplate.selectList(str, obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Object findForMap(String str, Object obj, String key) {
		try{
			return sqlSessionTemplate.selectMap(str, obj, key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	  * 获取相关的数据库连接
	  */
	 public Connection getConnection() {
	  return getSqlSession().getConnection();
	 }
}
