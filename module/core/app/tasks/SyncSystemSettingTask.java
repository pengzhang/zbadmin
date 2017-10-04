package tasks;

import java.util.List;

import models.core.setting.SystemSetting;
import play.Logger;
import play.Play;
import play.jobs.Every;
import play.jobs.Job;
import play.jobs.OnApplicationStart;

/**
 * 5分钟同步系统配置
 * @author zp
 *
 */
@Every("30mn")
public class SyncSystemSettingTask extends Job{

	public void doJob() {
		try {
			List<SystemSetting> sets = SystemSetting.all().fetch();
			for(SystemSetting set : sets) {
				Play.configuration.setProperty(set.settingKey, set.settingValue);
			}
		}catch(Exception e) {
			Logger.info("syn system setting task error: %s", e.getMessage());
		}
	}

}
