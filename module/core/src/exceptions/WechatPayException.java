package exceptions;

import play.exceptions.PlayException;

public class WechatPayException extends PlayException{
	
	private static final long serialVersionUID = 3583566093089790852L;
	
	public WechatPayException(){
		super();
	}
	
	public WechatPayException(String message){
		super(message);
	}
	
	public WechatPayException(String message,Throwable cause){
		super(message,cause);
	}
	@Override
	public String getErrorTitle() {
		return "Wechat Data "+this.getMessage();
	}

	@Override
	public String getErrorDescription() {
		return "Wechat Data "+this.getMessage();
	}
	
}
