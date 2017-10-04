package models.core.common;

import java.util.List;
import play.db.Model;

/**
 * 分页数据封装
 * 
 * @author zp
 * 
 */
public class PageData {

	public long total;
	public List<play.db.Model> rows;

	public PageData() {
	}

	public PageData(long total, List<Model> rows) {
		this.total = total;
		this.rows = rows;
	}

}
