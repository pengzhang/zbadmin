package models.core.setting;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import annotations.Exclude;
import models.BaseModel;

@Entity
@Table(name="admin_menu")
@org.hibernate.annotations.Table(comment="后台菜单", appliesTo = "admin_menu")
public class AdminMenu extends BaseModel {

	@Column(name="controller_name", columnDefinition="varchar(255) comment 'Controller名称'")
	public String controllerName;

	@Exclude
	@Column(name="model_name", columnDefinition="varchar(255) comment 'model名称'")
	public String modelName;

	@Column(columnDefinition="int default 0 comment '顺序'")
	public int seq = 0;

	public AdminMenu() {
	}

	public AdminMenu(String controllerName, String modelName, int seq) {
		this.controllerName = controllerName;
		this.modelName = modelName;
		this.seq = seq;
	}

	@Override
	public String toString() {
		return controllerName;
	}

}
