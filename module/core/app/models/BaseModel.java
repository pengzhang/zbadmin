package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import annotations.Exclude;
import play.db.jpa.Model;

/**
 * Base Model
 * @author zp
 *
 */
@MappedSuperclass
public class BaseModel extends Model{
	
	@Exclude
	@Column(columnDefinition="bigint default 0 comment '租户:0-平台 '")
	public long tenantId = 0;
	
	@Exclude
	@Column(columnDefinition="tinyint default 0 comment '状态:0-未删除 ,1-已删除'")
	public boolean status = false;
	
	@Exclude
	@Column(name="create_date",columnDefinition="datetime comment '创建日期'")
	public Date createDate = new Date();
	
	@Exclude
	@Column(name="update_date",columnDefinition="datetime comment '更新日期'")
	public Date updateDate = new Date();
	
}
