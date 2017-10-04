package exceptions;

import play.exceptions.PlayException;
/**
 * ParamException参数异常.
 * 继承自PlayException.
 * 
 * @author zp
 */
public class ParamException extends PlayException {
	
	private static final long serialVersionUID = 3583566093089790852L;
	public ParamException() {
		super();
	}
	public ParamException(String message) {
		super(message);
	}
	public ParamException(String message, Throwable cause) {
		super(message, cause);
	}
	@Override
	public String getErrorTitle() {
		return "param exception";
	}
	@Override
	public String getErrorDescription() {
		return "check param format and param whether null";
	}
}