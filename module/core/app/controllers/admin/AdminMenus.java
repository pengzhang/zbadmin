package controllers.admin;

import java.util.List;

import annotations.Check;
import annotations.For;
import annotations.Login;
import controllers.AdminActionIntercepter;
import controllers.CRUD;
import controllers.Secure;
import models.core.setting.AdminMenu;
import play.cache.Cache;
import play.mvc.With;

@Login
@Check("")
@For(AdminMenu.class)
@With({AdminActionIntercepter.class,Secure.class})
public class AdminMenus extends CRUD {
	
	/**
	 * 对比菜单顺序
	 * @param modelName 
	 * @param otherModelName
	 * @return 对比结果
	 */
	public static int menuCompareTo(String modelName, String otherModelName) {
		Object mn = 0;
    	Object omn = 0;
    	if(Cache.get(modelName) == null || Cache.get(otherModelName) == null) {
    		List<AdminMenu> menus = AdminMenu.all().fetch();
    		for(AdminMenu menu : menus) {
    			Cache.set(menu.modelName, menu.seq, "5mn");
    			if(modelName.equals(menu.modelName)) {
    				mn = menu.seq;
    			}
    			if(otherModelName.equals(menu.modelName)) {
    				omn = menu.seq;
    			}
    		}
    	}else {
    		 mn = Cache.get(modelName);
        	 omn = Cache.get(otherModelName);
    	}
    	if((int)mn > (int) omn) {
    		return 1;
    	}else if((int)mn == (int) omn) {
    		return 0;
    	}else {
    		return -1;
    	}
	}

}
