package utils;
import com.google.gson.ExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
/**
 * Gson 处理循环引用问题
 * @author zp
 *
 */
public class Json {
	
	/**
	 * 去除导致循环引用的字段
	 * @param obj
	 * @param excludeFields
	 * @return
	 */
	public static String toJson(Object obj,String...excludeFields){
		ExclusionStrategy excludeStrategy = new SetterExclusionStrategy(excludeFields);
		Gson gson = new GsonBuilder().serializeNulls().setExclusionStrategies(excludeStrategy)
				.create();
        return gson.toJson(obj);
	}
	
	public static String toJson(Object obj,Class...excludeClasses){
		ExclusionStrategy excludeStrategy = new SetterExclusionStrategy(excludeClasses);
		Gson gson = new GsonBuilder().setExclusionStrategies(excludeStrategy)
				.create();
		return gson.toJson(obj);
	}
	
	public static String toJson(Object obj){
		return new Gson().toJson(obj);
	}

}
