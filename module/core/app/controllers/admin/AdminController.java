package controllers.admin;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;

import com.qiniu.util.Json;

import annotations.Api;
import annotations.Check;
import annotations.For;
import annotations.Login;
import annotations.Param;
import annotations.Return;
import controllers.AdminActionIntercepter;
import controllers.CRUD;
import controllers.CRUD.ObjectType;
import models.core.common.ResponseData;
import models.core.common.Simditor;
import controllers.Secure;
import play.CorePlugin;
import play.Play;
import play.PlayPlugin;
import play.cache.Cache;
import play.mvc.Controller;
import play.mvc.With;
import plugins.router.Get;
import utils.PZDate;

@Login
@Check("")
@With({AdminActionIntercepter.class,Secure.class})
public class AdminController extends Controller{

	@Get("/admin/info")
	public static void info() {
		render();
	}

	@Get("/admin/cache/clear")
	public static void clearCache() {
		String key = PZDate.today();
		long access = NumberUtils.toLong(String.valueOf(Cache.get(key)),0L);
		long pc =   NumberUtils.toLong(String.valueOf(Cache.get(key+"_pc")),0L);
		long mobile =   NumberUtils.toLong(String.valueOf(Cache.get(key+"_mobile")),0L);
		
		Cache.clear();
		
		Cache.set(key, access);
		Cache.set(key+"_pc", pc);
		Cache.set(key+"_mobile", mobile);
		flash.success("清除缓存成功");
		redirect("/admin/info");
	}

	@Get("/admin/status")
	public static void adminStatus() {
		PlayPlugin plugin = Play.pluginCollection.getPluginInstance(CorePlugin.class);
		List status = Arrays.asList(plugin.getStatus().split("\n"));
		render(status);
	}

	@Get("/admin/access/count")
	public static void adminAccessCount() {
		renderJSON(Cache.get(PZDate.today()));
	}

	@Get("/admin/config")
	public static void allConfig() {
		Enumeration configs = Play.configuration.propertyNames();
		List<Map<String,String>> list = new ArrayList<>();
		while(configs.hasMoreElements()) {
			Map<String,String> map = new HashMap<String, String>();
			String key = String.valueOf(configs.nextElement());
			map.put(key, Play.configuration.getProperty(key));
			list.add(map);
		}
		render(list);
	}

	@Get("/admin/api/list")
	public static void apiList() {
		List<Map<String,Object>> apis = new ArrayList<>();
		List<Class> classes = Play.classloader.getAssignableClasses(Controller.class);
		for(Class c : classes) {
			Method[] ms = c.getMethods();
			for(Method m : ms) {
				if(m.isAnnotationPresent(Api.class)){
					Map<String,Object> apiMap = new HashMap<>();
					Api api = m.getAnnotation(Api.class);
					apiMap.put("name", api.name());
					apiMap.put("type", api.type());
					apiMap.put("url", api.url());

					List<Map<String,String>> params = new ArrayList<>();
					for(Param p : api.param()) {
						Map<String, String> param = new HashMap<>();
						param.put("clazz", p.clazz().getSimpleName());
						param.put("name", p.name());
						params.add(param);
					}
					apiMap.put("params", params);


					List<Map<String,String>> rets = new ArrayList<>();
					for(Return r : api.ret()) {
						Map<String, String> ret = new HashMap<>();
						if(r.clazz().getSimpleName().equals("InputStream")) {
							ret.put("result", "输出流");
						}else {
							try {
								ret.put("result", Json.encode(ResponseData.response(true, r.clazz().newInstance(), "message")));
							} catch (Exception e) {
								ret.put("result", "数据转换异常");
							}
						}
						rets.add(ret);
					}
					apiMap.put("rets", rets);
					apis.add(apiMap);
				}

			}
		}
		render(apis);
	}

}
