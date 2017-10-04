package controllers;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import annotations.Login;
import exceptions.ControllerException;
import models.core.adminuser.Permission;
import play.Logger;
import play.Play;
import play.cache.Cache;
import play.mvc.After;
import play.mvc.Before;
import play.mvc.Catch;
import play.mvc.Controller;
import play.mvc.Finally;
import tasks.AccessLogTask;

public class AdminActionIntercepter extends Controller {

	@Before()
	private static void actionBeforeProcess() {
		AccessLogTask.record(request);
	}

	@After
	private static void actionAfterProcess() {
	}
	
	@Catch(value = ControllerException.class, priority = 2)
	private static void actionControllerExceptionProcess(ControllerException ce) {
		ce.printStackTrace();
		Logger.error("controller exception %s", ce.getMessage());
		error(ce.getMessage());
	}

	@Catch(value = Throwable.class, priority = 1)
	private static void actionExceptionProcess(Throwable throwable) {
		throwable.printStackTrace();
		Logger.error("exception %s", throwable.getMessage());
		error(throwable.getMessage());
	}
	
	@Finally
    static void log() {
    }
	
}
