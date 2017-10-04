package utils;

import org.apache.commons.lang.StringUtils;

import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.UserAgent;
import play.mvc.Http.Request;

public class UserAgentUtil {
	
	/**
	 * 客户端是否电脑
	 * @param request
	 * @return
	 */
	public static boolean isComputer(Request request) {
		UserAgent userAgent = new UserAgent(request.headers.get("user-agent").value());
		DeviceType dt = userAgent.getOperatingSystem().getDeviceType();
		if(dt.equals(DeviceType.COMPUTER)) {
			return true;
		}
		return false;
	}
	
	public static boolean isMobile(Request request) {
		UserAgent userAgent = new UserAgent(request.headers.get("user-agent").value());
		DeviceType dt = userAgent.getOperatingSystem().getDeviceType();
		if(dt.equals(DeviceType.MOBILE)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 是否微信客户端
	 * @param request
	 * @return
	 */
	public static boolean isWechat(Request request) {
		String userAgentStr = StringUtils.defaultIfBlank(request.headers.get("user-agent").value(),HConstant.DEFAULTUSERAGENT);
		if (userAgentStr.contains("MicroMessenger")) {
			return true;
		}
		return false;
	}

}
