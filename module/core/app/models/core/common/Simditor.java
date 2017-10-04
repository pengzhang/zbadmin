package models.core.common;

/**
 * Simditor编辑器 
 * 上传图片
 * 
 * @author zp
 * 
 */
public class Simditor {

	public Boolean success;
	public String msg;
	public String file_path;

	public Simditor() {
		super();
	}
	
	public Simditor(Boolean success, String msg, String file_path) {
		this.success = success;
		this.msg = msg;
		this.file_path = file_path;
	}

}
