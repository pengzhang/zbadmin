package controllers.wechat.config;

import play.Play;

public class WechatConfig {
	
	public static String APPID = Play.configuration.getProperty("wechat.wxpay_appid"); 
	public static String APPSECRET = Play.configuration.getProperty("wechat.wxpay_appsecret"); 
	
}
