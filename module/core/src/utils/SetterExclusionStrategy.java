package utils;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 * Gson 过滤字段
 * @author zp
 *
 */
public class SetterExclusionStrategy implements ExclusionStrategy {
	private String[] fields;
	private Class<?>[] excludeClasses;
	
	public SetterExclusionStrategy(String... fields) {
		this.fields = fields;

	}
	
	public SetterExclusionStrategy(Class<?>... excludeClasses) {
		this.excludeClasses = excludeClasses;

	}

	@Override
	public boolean shouldSkipClass(Class<?> arg0) {
		
		if (this.excludeClasses == null) {
			return false;
		}

		for (Class<?> excludeClass : excludeClasses) {
			if (excludeClass.getName().equals(arg0.getName())) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 过滤字段的方法
	 */
	@Override
	public boolean shouldSkipField(FieldAttributes f) {
		if (fields != null) {
			for (String name : fields) {
				if (f.getName().equals(name)) {
					/** true 代表此字段要过滤 */
					return true;
				}
			}
		}
		return false;
	}
}
