package exceptions;

import play.exceptions.PlayException;
/**
 * InitializationException 初始化异常.
 * 继承自PlayException.
 * 
 * @author zp
 */
public class InitializationException extends PlayException {
	
	private static final long serialVersionUID = 3583566093089790852L;
	public InitializationException() {
		super();
	}
	public InitializationException(String message) {
		super(message);
	}
	public InitializationException(String message, Throwable cause) {
		super(message, cause);
	}
	@Override
	public String getErrorTitle() {
		return "initialization exception";
	}
	@Override
	public String getErrorDescription() {
		return "initialization error";
	}
}