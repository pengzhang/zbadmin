package exceptions;

import play.exceptions.PlayException;
/**
 * ControllerException 控制层异常.
 * 继承自PlayException.
 * 
 * @author zp
 */
public class ControllerException extends PlayException {
	
	private static final long serialVersionUID = 3583566093089790852L;
	public ControllerException() {
		super();
	}
	public ControllerException(String message) {
		super(message);
	}
	public ControllerException(String message, Throwable cause) {
		super(message, cause);
	}
	@Override
	public String getErrorTitle() {
		return "controller exception";
	}
	@Override
	public String getErrorDescription() {
		return "controller exception";
	}
}