package models.core.adminuser;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import models.BaseModel;

@Entity
@Table(name = "permission")
@org.hibernate.annotations.Table(comment = "用户权限", appliesTo = "permission")
public class Permission extends BaseModel implements Serializable {

	@Column(columnDefinition = "varchar(100) comment '权限名称'")
	public String permission_name;

	@Column(columnDefinition = "varchar(100) comment 'Action'")
	public String action;

	@Column(columnDefinition = "varchar(1000) comment 'ActionURL'")
	public String action_url;

	public Permission() {
		super();
	}

	public Permission(String permission_name, String action, String action_url) {
		super();
		this.permission_name = permission_name;
		this.action = action;
		this.action_url = action_url;
	}

	@Override
	public String toString() {
		return permission_name ;
	}


}
