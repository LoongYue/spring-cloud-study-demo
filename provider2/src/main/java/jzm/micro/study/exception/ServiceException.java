package jzm.micro.study.exception;
/**
 * 服务（业务）异常如“ 账号或密码错误 ”，该异常只做INFO级别的日志记录 @see WebMvcConfigurer
 */
public class ServiceException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	private String errorCode;
	
	private String message;
	
    public ServiceException() {
    }

    public ServiceException(String message) {
        super(message);
    }
    
    public ServiceException(String errorCode , String message) {
		super(message);
		this.message = message;
		this.errorCode = errorCode;
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
    
}