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

import annotations.Upload;
import models.BaseModel;
import play.data.validation.MaxSize;
import play.data.validation.Password;

@Entity
@Table(name = "admin_user")
@org.hibernate.annotations.Table(comment = "管理员用户管理", appliesTo = "admin_user")
public class AdminUser extends BaseModel implements Serializable {

	@Column(columnDefinition = "varchar(100) comment '用户名'")
	public String username;
	@Password
	@Column(columnDefinition = "varchar(100) comment '密码'")
	public String password;

	@Column(columnDefinition = "varchar(100) comment '用户邮箱'")
	public String email;

	@Column(columnDefinition = "varchar(30) comment '手机号'")
	public String mobile;

	@Upload
	@Column(columnDefinition = "varchar(1000) comment '用户头像'")
	public String avatar;
	
	@ManyToMany(fetch=FetchType.LAZY, cascade=CascadeType.REFRESH)
	public List<Role> roles = new ArrayList<Role>();

	public AdminUser() {
		super();
	}

	public AdminUser(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public AdminUser(String username, String password, String avatar) {
		super();
		this.username = username;
		this.password = password;
		this.avatar = avatar;
	}

	@Override
	public String toString() {
		return username;
	}
}
