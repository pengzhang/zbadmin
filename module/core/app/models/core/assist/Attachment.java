package models.core.assist;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import models.BaseModel;
import play.db.jpa.Blob;

/**
 * 资源附件
 * 
 * @author zp
 * 
 */
@Entity
@Table(name = "attachment")
@org.hibernate.annotations.Table(comment = "资源附件", appliesTo = "attachment")
public class Attachment extends BaseModel {

	public Blob file;

	@Column(columnDefinition = "varchar(255) comment '文件名称'")
	public String name;
	
	@Column(columnDefinition = "varchar(255) comment '存储类型'")
	public String store_type;
	
	@Column(columnDefinition = "varchar(500) comment '存储Url'")
	public String store_url;

}
