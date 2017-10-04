package plugins.custom;
import java.io.PrintWriter;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import groovy.lang.Closure;
import play.Play;
import play.cache.Cache;
import play.templates.FastTags;
import play.templates.GroovyTemplate.ExecutableTemplate;

public class TagExtension extends FastTags {
	public static void _site_name(Map<?, ?> args, Closure body, PrintWriter out, ExecutableTemplate template, int fromLine) {
		out.print(StringUtils.defaultIfEmpty((String) Cache.get("site_name"), "HM55"));
	}

	public static void _site_domain(Map<?, ?> args, Closure body, PrintWriter out, ExecutableTemplate template, int fromLine) {
		out.print(StringUtils.defaultIfEmpty((String) Cache.get("site_domain"), "http://www.hm55.cn"));
	}

	public static void _site_desc(Map<?, ?> args, Closure body, PrintWriter out, ExecutableTemplate template, int fromLine) {
		out.print(StringUtils.defaultIfEmpty((String) Cache.get("site_desc"), ""));
	}

	public static void _site_keyword(Map<?, ?> args, Closure body, PrintWriter out, ExecutableTemplate template, int fromLine) {
		out.print(StringUtils.defaultIfEmpty((String) Cache.get("site_keyword"), ""));
	}

	public static void _service_tel(Map<?, ?> args, Closure body, PrintWriter out, ExecutableTemplate template, int fromLine) {
		out.print(StringUtils.defaultIfEmpty((String) Cache.get("service_tel"), "010-88888888"));
	}

	public static void _service_email(Map<?, ?> args, Closure body, PrintWriter out, ExecutableTemplate template, int fromLine) {
		out.print(StringUtils.defaultIfEmpty((String) Cache.get("service_email"), "service@hm55.cn"));
	}

	public static void _shop_service_tel(Map<?, ?> args, Closure body, PrintWriter out, ExecutableTemplate template, int fromLine) {
		out.print(StringUtils.defaultIfEmpty((String) Cache.get("shop_service_tel"), "010-88888888"));
	}

	public static void _shop_service_email(Map<?, ?> args, Closure body, PrintWriter out, ExecutableTemplate template, int fromLine) {
		out.print(StringUtils.defaultIfEmpty((String) Cache.get("shop_service_email"), "service@hm55.cn"));
	}

	public static void _qiniu_domain(Map<?, ?> args, Closure body, PrintWriter out, ExecutableTemplate template, int fromLine) {
		String qiniu_domain = (String) Cache.get("qiniu_domain");
		if (StringUtils.isEmpty(qiniu_domain)) {
			qiniu_domain = Play.configuration.getProperty("qiniu.upload.domain");
		}
		out.print(qiniu_domain);
	}

	public static void _weibo(Map<?, ?> args, Closure body, PrintWriter out, ExecutableTemplate template, int fromLine) {
		out.print("/qrcode?code=" + StringUtils.defaultIfEmpty((String) Cache.get("weibo"), "http://weibo.com"));
	}

	public static void _wechat(Map<?, ?> args, Closure body, PrintWriter out, ExecutableTemplate template, int fromLine) {
		out.print("/qrcode?code=" + StringUtils.defaultIfEmpty((String) Cache.get("wechat"), "http://wx.qq.com"));
	}
}