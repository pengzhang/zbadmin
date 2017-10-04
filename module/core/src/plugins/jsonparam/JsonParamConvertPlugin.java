package plugins.jsonparam;

import java.lang.reflect.Method;

import play.PlayPlugin;
import play.mvc.Http;
import utils.JsonParamUtils;

public class JsonParamConvertPlugin extends PlayPlugin{

	public void beforeActionInvocation(Method actionMethod) {
		Http.Request request = Http.Request.current();
		String contentType = request.contentType;
		if( contentType != null && contentType.contains("application/json")){
			JsonParamUtils.converJsonParams(request.params.get("body"));
		}
    }
}
