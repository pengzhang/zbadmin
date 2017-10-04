package controllers;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import models.core.user.User;
import play.mvc.Controller;

public class BaseController extends Controller {
	
	protected static User currentUser() {
		String uid = session.get("uid");
		if(StringUtils.isNotEmpty(uid)){
			long id = NumberUtils.toLong(uid, 0L);
			return User.findById(id);
		}
		return null;
	}

}
