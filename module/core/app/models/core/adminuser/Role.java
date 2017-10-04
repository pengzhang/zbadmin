package models.core.adminuser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import models.BaseModel;

@Entity
@Table(name = "role")
@org.hibernate.annotations.Table(comment = "用户角色", appliesTo = "role")
public class Role extends BaseModel implements Serializable {

	@Column(columnDefinition = "varchar(100) comment '角色名称'")
	public String role_name;
	
	@ManyToMany(fetch=FetchType.LAZY, cascade=CascadeType.REFRESH)
	public List<Permission> permissions = new ArrayList<Permission>();

	public Role() {
		super();
	}

	public Role(String role_name) {
		super();
		this.role_name = role_name;
	}

	@Override
	public String toString() {
		return role_name;
	}
}
