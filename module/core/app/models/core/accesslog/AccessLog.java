package models.core.accesslog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import play.db.jpa.Model;
import utils.Json;

@Entity
@Table(name = "access_log")
@org.hibernate.annotations.Table(comment = "管理员用户管理", appliesTo = "access_log")
public class AccessLog extends Model{
	
	@Column(name="access_date", columnDefinition = "varchar(100) comment '访问日期'")
	public String accessDate;
	@Column(columnDefinition = "bigint comment '访问总数'")
	public long total;
	@Column(columnDefinition = "bigint comment 'PC访问总数'")
	public long pc;
	@Column(columnDefinition = "bigint comment '移动端访问总数'")
	public long mobile;
	
	public AccessLog() {
	}
	
	public AccessLog(String accessDate) {
		this.accessDate = accessDate;
	}
	
	public static Map<String, String> getAccessLogChart(){
		Map<String, String> chart = new HashMap<>();
		List<AccessLog> accessLogs = AccessLog.find("order by accessDate ASC").fetch(15);
		
		List<String> title = new ArrayList<>();
		for(AccessLog log : accessLogs) {
			title.add(log.accessDate);
		}
		
		List<Long> data = new ArrayList<>();
		for(AccessLog log : accessLogs) {
			data.add(log.total);
		}
		
		chart.put("title", Json.toJson(title));
		chart.put("data", Json.toJson(data));
		return chart;
	}

}
