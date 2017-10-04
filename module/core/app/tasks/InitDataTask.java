package tasks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import controllers.CRUD;
import controllers.CRUD.ObjectType;
import models.core.adminuser.AdminUser;
import models.core.adminuser.Permission;
import models.core.setting.AdminMenu;
import models.core.setting.SystemSetting;
import play.Play;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.libs.Crypto;
import play.mvc.Router;
import play.mvc.Router.Route;
/**
 * 初始化数据
 * @author zhangpeng
 *
 */
@OnApplicationStart
public class InitDataTask extends Job{
	
	public void doJob(){
		initAdmin();
		initPermissions();
		initSettings();
		allNavMenu();
	}
	
	/**
	 * 初始化超级管理员
	 */
	public static void initAdmin(){
		AdminUser admin = AdminUser.find("username", "admin").first();
		if(admin == null){
			new AdminUser("admin", Crypto.passwordHash("admin")).save();
		}
	}
	
	/**
	 * 初始化HAdmin的权限列表
	 * 扫码Router生成
	 */
	public static void initPermissions(){
		String flag = Play.configuration.getProperty("init.permissions", "false");
		if(flag.equals("true")) {
			Iterator it = Router.routes.iterator();
			while(it.hasNext()){
				Route route = (Route) it.next();
				Permission permission = Permission.find("action", route.action).first();
				if(permission == null){
					new Permission(route.action, route.action, route.path).save();
				}
			}
		}
	}
	
	/**
	 * 初始化配置
	 */
	public static void initSettings() {
		List<SystemSetting> sets = SystemSetting.findAll();

		List<String> keys = new ArrayList<String>();
		for(SystemSetting set : sets) {
			keys.add(set.settingKey);
			Play.configuration.setProperty(set.settingKey, set.settingValue);
		}

		//上传类型
		setSetting(keys,"attachments.type", "local");
		setSetting(keys,"image.server.domain", "http://127.0.0.1:9000");

		//七牛云存储
		setSetting(keys,"qiniu.access_key", "");
		setSetting(keys,"qiniu.secret_key", "");
		setSetting(keys,"qiniu.bucketname", "");
		setSetting(keys,"qiniu.domain", "");

		//微信配置
		setSetting(keys,"wechat.wxpay_appid", "");
		setSetting(keys,"wechat.wxpay_appsecret", "");
		setSetting(keys,"wechat.wxpay_mchid", "");
		setSetting(keys,"wechat.wxpay_key", "");
		setSetting(keys,"wechat.wxpay_curl_proxy_host", "");
		setSetting(keys,"wechat.wxpay_curl_proxy_port", "");
		setSetting(keys,"wechat.wxpay_report_levenl", "");
		setSetting(keys,"wechat.wxpay_sslcert_path", "");
		setSetting(keys,"wechat.wxpay_sslkey_path", "");
		setSetting(keys,"wechat.wxpay_sslrootca_path", "");
		setSetting(keys,"wechat.wxpay_notify_url", "");
		setSetting(keys,"wechat.wxpay_domain", "");
		
		setSetting(keys,"wechat.login", "true");
		setSetting(keys,"wechat.snsapi", "base");
		setSetting(keys,"wechat.loginneed", "true");
		setSetting(keys,"wechat.binduser", "false");
		
		setSetting(keys,"init.permissions", "false");
		
	}

	private static void setSetting(List keys, String key, String value) {
		if(!keys.contains(key)) {
			new SystemSetting(key, value).save();
			Play.configuration.setProperty(key, value);
		}
	}
	
	private static void allNavMenu(){
		List<Class> classes =Play.classloader.getAssignableClasses(CRUD.class);
		for(Class clazz: classes){
			AdminMenu menu = AdminMenu.find("controllerName=?", clazz.getSimpleName()).first();
			if(menu == null){
				new AdminMenu(clazz.getSimpleName(), ObjectType.get(clazz).modelName, 0).save();
			}
		}
	}

}
