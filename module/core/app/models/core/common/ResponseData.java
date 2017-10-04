package models.core.common;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

public class ResponseData {
	
	 /**
     * 返回成功结果
     * @param status
     * @param message
     * @return
     */
    public static Map<String, Object> response(boolean status, String message) {
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("status", status);
    	map.put("data", "null");
    	map.put("message", message);
    	return map;
    }

	 /**
     * 返回成功结果
     * @param status
     * @param data
     * @param message
     * @return
     */
    public static Map<String, Object> response(boolean status, Object data, String message) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", status);
        if(data==null){
            map.put("data", "null");
        }else{
            map.put("data", data);
        }
        
        if(message==null){
            map.put("message", "");
        }else{
            map.put("message", message);
        }
        return map;
    }
    
    public static Map<String,Object> response(int status,Object data,String message){
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("status", status);
    	map.put("data", "null");
    	map.put("message", message);
    	return map;
    }
}
