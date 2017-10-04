package models.core.setting;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import models.BaseModel;

@Entity
@Table(name="system_setting")
@org.hibernate.annotations.Table(comment="系统设置", appliesTo = "system_setting")
public class SystemSetting extends BaseModel{
	
	@Column(name="setting_key", columnDefinition="varchar(255) comment '系统设置的Key'")
	public String settingKey;
	@Column(name="setting_value", columnDefinition="varchar(255) comment '系统设置的值'")
	public String settingValue;
	
	public SystemSetting() {
		super();
	}

	public SystemSetting(String settingKey) {
		super();
		this.settingKey = settingKey;
	}

	public SystemSetting(String settingKey, String settingValue) {
		super();
		this.settingKey = settingKey;
		this.settingValue = settingValue;
	}

	@Override
	public String toString() {
		return settingKey;
	}
	
	
}
