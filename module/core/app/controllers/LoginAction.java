package controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

import controllers.wechat.WechatAuthController;
import controllers.wechat.config.WechatConfig;
import models.core.user.User;
import play.Logger;
import play.Play;
import play.cache.Cache;
import play.mvc.Before;
import play.mvc.Controller;
import plugins.router.Get;
import plugins.router.Post;
import utils.UserAgentUtil;

public class LoginAction extends Controller {

	@Get("/user/login")
	public static void login() {
		boolean isDev = Play.mode.isDev();
		render(isDev);
	}
	
	@Get("/user/mini/login")
	public static void miniLogin() {
		render();
	}
	
	@Get("/user/mini/register")
	public static void miniRegister() {
		render();
	}

	@Post("/user/login")
	public static void loginAction(String username, String vcode) {
		Object obj = Cache.get(vcode);
		if(obj == null){
			flash.error("验证码错误");
			params.flash();
			login();
		}
		User user = User.find("username", username).first();
		if(user == null){
			user = new User(username);
			user.save();
		}
		session.put("uid", user.id);
		session.put("nickname", user.nickname);
		session.put("avatar", user.avatar);
		redirect("/");
	}

	@Get("/user/binduser")
	public static void binduser() {
		renderText("TODO");
	}

	@Post("/user/binduser")
	public static void bindUserAction() {
		renderText("TODO");
	}

}
