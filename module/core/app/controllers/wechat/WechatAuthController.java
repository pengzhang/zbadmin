package controllers.wechat;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.google.gson.Gson;

import controllers.wechat.config.WechatConfig;
import models.core.user.User;
import play.Logger;
import play.Play;
import play.cache.Cache;
import play.libs.WS;
import play.mvc.Controller;
import plugins.router.Get;
import utils.EmojiFilter;
import utils.Json;

public class WechatAuthController extends Controller{

	/**
	 * 用户静默登录
	 */
	@Get("/wechat/snsapi/base")
	public static void snsapi_base(String appid, String redirect_url, String state){
		redirect(wechatUrl(appid,redirect_url,"snsapi_base",state));
	}
	
	/**
	 * 用户授权登录
	 */
	@Get("/wechat/snsapi/userinfo")
	public static void snsapi_userinfo(String appid, String redirect_url, String state){
		redirect(wechatUrl(appid,redirect_url,"snsapi_userinfo",state));
	}
	
	/**
	 * @param code
	 * @param state
	 * 公众号获取openid并更新用户中心
	 */
	@Get("/wechat/openid/?")
	public static void jsapipayOpenid(String code, String state ) {
		Logger.info("jsapipay request params:[code:%s, state:%s]", code, state);

		//获取openid
		Map<String,String> result = WechatAuthController.getUserInfo(WechatConfig.APPID, WechatConfig.APPSECRET , code);
		
		//判断result,用户是否注册
		Logger.info("wechat result:[%s]", Json.toJson(result));
		User user = User.find("openid", result.get("openid")).first();
		String nickname = EmojiFilter.filterEmoji(result.get("nickname"));
		Logger.info("wechat emoji nickname:%s", result.get("nickname"));
		Logger.info("wechat filter emoji nickname:%s", nickname);
		if(user == null){
			Logger.info("new user nickname:%s, openid:%s", nickname, result.get("openid"));
			user = new User();
			user.avatar = result.get("headimgurl");
			user.nickname = nickname;
			user.sex = String.valueOf(result.get("sex"));
			user.city = result.get("city");
			user.country = result.get("country");
			user.openid = result.get("openid");
			user.unionid = result.get("unionid");
			user = user.save();
		}else{
			Logger.info("update user nickname:%s, openid:%s", nickname, user.openid);
			user.nickname = nickname;
			user.avatar = result.get("headimgurl");
			user.save();
		}
		session.put("uid", user.id);
		session.put("openid", user.openid);
		session.put("nickname", user.nickname);
		session.put("avatar", user.avatar);
		
		Logger.info("state url:%s", (String) Cache.get(state));
		String url = StringUtils.defaultIfEmpty((String) Cache.get(state), "/");
		redirect(url);
	}
	
	/**
	 * 获取授权的AccessToken和OpenId
	 * @param appid
	 * @param secret
	 * @param code
	 * @return
	 */
	public static Map<String,String> authToken(String appid, String secret, String code){
		String url = wechatAuthTokenUrl(appid, secret, code);
		String result = WS.url(url).get().getString();
		Logger.info("wechat authToken result, %s", Json.toJson(result));
		return new Gson().fromJson(result, Map.class);
	}
	
	/**
	 * 获取授权的用户信息
	 * @param appid
	 * @param secret
	 * @param code
	 * @return
	 */
	public static Map<String,String> getUserInfo(String access_token, String openid){
		String url = wechatGetUserInfoUrl(access_token, openid);
		String result = WS.url(url).get().getString();
		Logger.info("wechat get userinfo result, %s", Json.toJson(result));
		return new Gson().fromJson(result, Map.class);
	}
	
	/**
	 * 获取授权的用户信息(二合一)
	 * @param appid
	 * @param secret
	 * @param code
	 * @return
	 */
	public static Map<String,String> getUserInfo(String appid, String secret, String code){
		Map<String,String> token = authToken(appid, secret, code);
		return getUserInfo(token.get("access_token"), token.get("openid"));
	}
	
	
	
	/**
	 * 组装微信授权URL
	 * @param appid
	 * @param redirect_url
	 * @param scope
	 * @return
	 */
	private static String wechatUrl(String appid, String redirect_url, String scope, String state){
		String wechatUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appid+"&redirect_uri="+redirect_url+"&response_type=code&scope="+scope+"&state="+state+"#wechat_redirect";
		return wechatUrl;
	}
	
	/**
	 * 组装授权的AccessToken和OpenId的URL
	 * @param appid
	 * @param secret
	 * @param code
	 * @return
	 */
	private static String wechatAuthTokenUrl(String appid, String secret, String code){
		String wechatAuthTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+appid+"&secret="+secret+"&code="+code+"&grant_type=authorization_code";
		return wechatAuthTokenUrl;
	}
	
	/**
	 * 组装获取用户信息的URL
	 * @param appid
	 * @param secret
	 * @param code
	 * @return
	 */
	private static String wechatGetUserInfoUrl(String access_token, String openid){
		String wechatGetUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token="+access_token+"&openid="+openid+"&lang=zh_CN";
		return wechatGetUserInfoUrl;
	}
	
	
	
}
