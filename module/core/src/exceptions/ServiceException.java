package exceptions;

import play.exceptions.PlayException;
/**
 * ServiceException 业务异常.
 * 继承自PlayException.
 * 
 * @author zp
 */
public class ServiceException extends PlayException {
	
	private static final long serialVersionUID = 3583566093089790852L;
	public ServiceException() {
		super();
	}
	public ServiceException(String message) {
		super(message);
	}
	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}
	@Override
	public String getErrorTitle() {
		return "service exception";
	}
	@Override
	public String getErrorDescription() {
		return "service logic error";
	}
}