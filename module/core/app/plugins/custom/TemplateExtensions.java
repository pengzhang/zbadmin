package plugins.custom;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import play.i18n.Messages;
import play.templates.JavaExtensions;

public class TemplateExtensions extends JavaExtensions {

	public static String cutString(String str, int length) {
		if (length < 0) {
			return "";
		}
		if (length > str.length()) {
			return str.substring(0, str.length());
		}
		return str.substring(0, length) + "...";
	}
	
	public static String cutString(String str, int beginIndex, int endIndex) {
		if (beginIndex < 0) {
			return "";
		}
		if (endIndex > str.length()) {
			return str.substring(beginIndex, str.length());
		}
		int subLen = endIndex - beginIndex;
		if (subLen < 0) {
			return "";
		}
		return str.substring(beginIndex, endIndex) + "...";
	}
	
	public static String rmb(String str) {
		return String.format("%.2f", Float.parseFloat(str));
	}
	
	public static String i18n(String str) {
		String[] messages = str.split("[.]");
		StringBuilder sb = new StringBuilder();
		for(String msg : messages){
			sb.append(Messages.get(msg));
		}
		return sb.toString();
	}
	
	public static String date(String str) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date(Long.parseLong(str)));
	}

	public static String relativeDate(Date date) {
		return RelativeDateFormat.format(date);
	}

	public static String firstImage(String str) {
		String image = "";
		try {
			Document doc = Jsoup.parseBodyFragment(str);
			image = doc.getElementsByTag("img").first().attr("src");
		} catch (Throwable t) {
			image = "";
		}
		return image;
	}
	
	public static String htmlText(String str) {
		String text = "";
		try {
			Document doc = Jsoup.parseBodyFragment(str);
			text = doc.text();
		} catch (Throwable t) {
			text = "";
		}
		return text;
	}

	public static String dateFormatYY(Long str) {

		try {
			Calendar currenDate = Calendar.getInstance();
			currenDate.setTime(new Date());
			int currentYear = currenDate.get(Calendar.YEAR);

			Date date = new Date(str);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			if (cal.get(Calendar.YEAR) == currentYear) {

				return DateFormatUtils.format(date, "MM-dd HH:mm");
			} else {
				return DateFormatUtils.format(date, "yy-MM-dd HH:mm");
			}
		} catch (Throwable t) {
			return DateFormatUtils.format(new Date(), "yy-MM-dd HH:mm");
		}

	}
}