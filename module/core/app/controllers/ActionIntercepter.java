package controllers;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

import annotations.DefaultPageParam;
import annotations.Login;
import controllers.wechat.WechatAuthController;
import controllers.wechat.config.WechatConfig;
import play.Logger;
import play.Play;
import play.cache.Cache;
import play.mvc.After;
import play.mvc.Before;
import play.mvc.Catch;
import play.mvc.Controller;
import play.mvc.Finally;
import tasks.AccessLogTask;
import utils.UserAgentUtil;

public class ActionIntercepter extends Controller {

	@Before()
	static void actionBeforeProcess() {
		AccessLogTask.record(request);

		//是否必须微信访问
		String wxLoginNeed = Play.configuration.getProperty("wechat.loginneed", "false");
		if(wxLoginNeed.equals("true")) {
			if(!UserAgentUtil.isWechat(request)) {
				error(666, "请使用微信登录");
			}
		}
	}

	@Before
	static void devDefaultUser() {
		if(Play.mode.isDev()) {
			session.put("uid", 1);
		}
	}

	@Before
	static void defaultPageParam() throws Exception {
		Class controller = Class.forName("controllers." + request.action.substring(0, request.action.lastIndexOf(".")));
		Method[] methods = controller.getMethods();
		for(Method method : methods) {
			if(method.isAnnotationPresent(DefaultPageParam.class)  && request.actionMethod.equals(method.getName())) {
				String page = request.params.get("page");
				if(page == null) {
					request.params.put("page", "1");
				}else if(Integer.parseInt(page) < 1) {
					request.params.put("page", "1");
				}

				String size = request.params.get("size");
				if(size == null) {
					request.params.put("size", "10");
				}else if(Integer.parseInt(size) < 1) {
					request.params.put("size", "10");
				}
			}
		}
	}


	@Before()
	static void checkLogin() throws Exception {
		Class controller = Class.forName("controllers." + request.action.substring(0, request.action.lastIndexOf(".")));
		if(controller.isAnnotationPresent(Login.class)) {
			boolean flag = true;
			String[] except = ((Login) controller.getAnnotation(Login.class)).unless();//排除的Action
			for(String action : except){
				if(request.actionMethod.equals(action)){
					flag = false;
				}else {
					flag = true;
				}
			}

			String[] only = ((Login) controller.getAnnotation(Login.class)).only();
			for(String action : only){
				if(request.actionMethod.equals(action)){
					flag = true;
				}else {
					flag = false;
				}
			}
			
			if(flag) {
				checkLoginAction();
			}
		}
	}
	
	static void checkLoginAction() {
		String wxLogin = Play.configuration.getProperty("wechat.login", "true");
		if (UserAgentUtil.isWechat(request) && wxLogin.equals("true")) {

			// 如果openid为空,要求用户授权
			if (StringUtils.isEmpty(session.get("openid"))) {

				String returnUrl = request.getBase() + "/wechat/openid";
				Logger.info("get wechat opendid, request url:%s", returnUrl);

				// 记录用户请求的URL
				String state = RandomStringUtils.randomAlphanumeric(20);
				Cache.set(state, request.url, "5mn");

				try {
					returnUrl = URLEncoder.encode(returnUrl, "utf-8");
				} catch (UnsupportedEncodingException e) {
					Logger.info("wechat auth redirect url encode error,%s", e.getMessage());
				}

				Logger.info("wechat auth redirect url,%s", returnUrl);
				//微信静默登录和授权登录
				String wxSnsApi = Play.configuration.getProperty("wechat.snsapi", "base");
				if(wxSnsApi.equals("base")) {
					WechatAuthController.snsapi_base(WechatConfig.APPID, returnUrl, state);
				}else {
					WechatAuthController.snsapi_userinfo(WechatConfig.APPID, returnUrl, state);
				}
			}else {
				//是否要求微信绑定用户
				String binduser = Play.configuration.getProperty("wechat.binduser", "false");
				if(binduser.equals("true")) {
					LoginAction.binduser();
				}
			}
		} else {
			if(StringUtils.isEmpty(session.get("uid"))) {
				if(request.isAjax()) {
					todo();
				}else {
					LoginAction.login();
				}
				
			}
		}
	}

	@After
	static void actionAfterProcess() {

	}

	@Catch(value = Throwable.class, priority = 1)
	static void actionExceptionProcess(Throwable throwable) {
		throwable.printStackTrace();
		Logger.error("exception %s", throwable.getMessage());
		error(throwable.getMessage());
	}

	@Finally
	static void log() {
	}


}
