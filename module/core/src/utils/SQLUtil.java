package utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.hibernate.SQLQuery;
import org.hibernate.transform.ResultTransformer;

import play.db.jpa.JPA;

public class SQLUtil {
	
	
	/**
	 * 查询单条数据
	 * @param sql
	 * @param obj
	 * @return
	 */
	public static Object queryObj(String sql, Object...obj) {
		Query query  = JPA.em().createNativeQuery(sql);
		int i = 0;
		for(Object param : obj) {
			query.setParameter(i, param);
			i++;
		}
		query = wrapQuery(query);
		return query.getSingleResult();
	}
	
	/**
	 * 查询全部
	 * @param sql
	 * @param max
	 * @param obj
	 * @return
	 */
	public static List queryList(String sql, Object...obj) {
		Query query  = JPA.em().createNativeQuery(sql);
		int i = 0;
		for(Object param : obj) {
			query.setParameter(i, param);
			i++;
		}
		query = wrapQuery(query);
		return query.getResultList();
	}

	/**
	 * 带条数的查询
	 * @param sql
	 * @param max
	 * @param obj
	 * @return
	 */
	public static List queryListMax(String sql, int max, Object...obj) {
		Query query  = JPA.em().createNativeQuery(sql);
		int i = 0;
		for(Object param : obj) {
			query.setParameter(i, param);
			i++;
		}
		query = wrapQuery(query);
		query.setMaxResults(max);
		return query.getResultList();
	}

	/**
	 * 带翻页的查询
	 * @param sql
	 * @param page
	 * @param size
	 * @param obj
	 * @return
	 */
	public static List queryListPage(String sql, int page, int size, Object...obj) {
		Query query  = JPA.em().createNativeQuery(sql);
		int i = 0;
		for(Object param : obj) {
			query.setParameter(i, param);
			i++;
		}
		query = wrapQuery(query);
		query.setFirstResult(page*size).setMaxResults(size);
		return query.getResultList();
	}
	
	private static Query wrapQuery(Query query) {
		query.unwrap(SQLQuery.class).setResultTransformer(new ResultTransformer(){
			@Override
			public Object transformTuple(Object[] tuple, String[] aliases) {   
				Map result = new HashMap(tuple.length);   
				for ( int i=0; i<tuple.length; i++ ) {   
					String alias = aliases[i];   
					if ( alias!=null ) {   
						result.put( alias.toLowerCase(), tuple[i] );   
					}   
				} 

				return result;   
			}

			@Override
			public List transformList(List list) {
				return list;
			}  
		});  
		return query;
	}


}
