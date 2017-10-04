package tasks;

import models.core.accesslog.AccessLog;
import play.cache.Cache;
import play.jobs.Every;
import play.jobs.Job;
import play.mvc.Http.Request;
import utils.PZDate;
import utils.UserAgentUtil;

/**
 * 5分钟同步访问量数据库
 * 
 * @author zp
 *
 */
@Every("1mn")
public class AccessLogTask extends Job {

	public void doJob() {
		try {
			String key = PZDate.today();
			AccessLog access = AccessLog.find("accessDate=?", key).first();
			if (access == null) {
				new AccessLog(key).save();
			} else {
				Long total = Cache.get(key, Long.class);
				Long pc = Cache.get(key + "_pc", Long.class);
				Long mobile = Cache.get(key + "_mobile", Long.class);
				if (total != null) {
					if (access.total > total) {
						access.total += total;
						Cache.set(key, access.total);
					} else {
						access.total = total;
					}
				}

				if (pc != null) {
					if (access.pc > pc) {
						access.pc += pc;
						Cache.set(key + "_pc", access.pc);
					} else {
						access.pc = pc;
					}
				}

				if (mobile != null) {
					if (access.mobile > mobile) {
						access.mobile += mobile;
						Cache.set(key + "_mobile", access.mobile);
					} else {
						access.mobile = mobile;
					}
				}

				access.save();
			}
		} catch (Exception e) {
		}
	}

	public static void record(Request request) {
		String key = PZDate.today();
		if (Cache.get(key) == null) {
			Cache.set(key, 0);
		}
		Cache.incr(key);

		if (UserAgentUtil.isComputer(request)) {
			record_pc();
		} else {
			record_mobile();
		}
	}

	private static void record_pc() {
		String key = PZDate.today();
		if (Cache.get(key + "_pc") == null) {
			Cache.set(key + "_pc", 0);
		}
		Cache.incr(key + "_pc");
	}

	private static void record_mobile() {
		String key = PZDate.today();
		if (Cache.get(key + "_mobile") == null) {
			Cache.set(key + "_mobile", 0);
		}
		Cache.incr(key + "_mobile");
	}

}
