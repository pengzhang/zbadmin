package utils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.Map;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import exceptions.ParamException;
import play.mvc.Scope.Params;
/**
 * JSON参数的转换成系统可以识别的参数形式
 * @author zhangpeng
 *
 */
public class JsonParamUtils {

	/**
	 * 处理request body为json转换
	 * 
	 * @param <T>
	 * @param body
	 * @param clazz
	 * @return
	 */
	public static <T> T converJsonParams(InputStream body, Class<T> clazz) throws ParamException {
		try {
			T t = new GsonBuilder().create().fromJson(new InputStreamReader(body), clazz);
			Field[] fields = t.getClass().getDeclaredFields();
			for (Field f : fields) {
				Params.current().put(f.getName(), String.valueOf(f.get(t)));
			}
			return t;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ParamException("conver json params error");
		}
	}

	/**
	 * 处理 request body 转换为 Params
	 * 
	 * @param body
	 * @throws ParamException
	 */
	public static void converJsonParams(InputStream body) throws ParamException {
		try {
			if (body != null) {
				Map<String, String> t = new GsonBuilder().create().fromJson(new InputStreamReader(body), new TypeToken<Map<String, String>>() {
				}.getType());
				if (t != null) {
					for (String key : t.keySet()) {
						Params.current().put(key, String.valueOf(t.get(key)));
					}
				}
			}
		} catch(IllegalStateException ie){
			ie.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
			throw new ParamException("conver json params error");
		}
	}
	
	public static void converJsonParams(String body) throws ParamException {
		try {
			if (body != null) {
				Map<String, String> t = new GsonBuilder().create().fromJson(body, new TypeToken<Map<String, String>>() {
				}.getType());
				if (t != null) {
					for (String key : t.keySet()) {
						Params.current().put(key, String.valueOf(t.get(key)));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ParamException("conver json params error");
		}
	}
}