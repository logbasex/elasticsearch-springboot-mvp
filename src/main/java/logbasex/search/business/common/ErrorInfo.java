package logbasex.search.business.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ErrorInfo {
	private static final long serialVersionUID = 2274535794300239705L;
	
	private static final Logger logger = LoggerFactory.getLogger(ErrorInfo.class);
	
	public static Properties properties;
	
	static {
		try {
			properties = new Properties();
			InputStream is = ErrorInfo.class.getResourceAsStream("/error_info.properties");
			properties.load(is);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	public static final int BAD_REQUEST_ERROR_CODE = 1010;
	
	
	public static final int UNKNOWN_ERROR_CODE = 1001;
	public static final int USER_NOT_FOUND_ERROR_CODE = 1002;
	public static final int DOCUMENT_ID_NOT_FOUND_CODE = 1003;
	
	
	private int code;
	private List<String> messages = new ArrayList<>();
	
	public static ErrorInfo newInstance(int code, String message) {
		return new ErrorInfo(code, message);
	}
	
	public ErrorInfo(int code, String... messages) {
		this.code = code;
		for (String message : messages) {
			this.messages.add(message);
		}
	}
	
	public int getCode() {
		return code;
	}
	
	public String getMessage() {
		return messages.size() > 0 ? messages.get(0) : null;
	}
	
	// ===============================================================================================
	// special code
	// ===============================================================================================
	
	public static final ErrorInfo INTERNAL_SERVER_ERROR = new ErrorInfo(UNKNOWN_ERROR_CODE,
			properties.getProperty("server.error"));
	
	public static final ErrorInfo USER_NOT_FOUND_ERROR = new ErrorInfo(USER_NOT_FOUND_ERROR_CODE,
			properties.getProperty("user.not.found.error"));
	
	public static final ErrorInfo BAD_REQUEST = new ErrorInfo(BAD_REQUEST_ERROR_CODE,
			properties.getProperty("request.error"));
	
	public static final ErrorInfo DOCUMENT_ID_NOT_FOUND = new ErrorInfo(DOCUMENT_ID_NOT_FOUND_CODE,
			properties.getProperty("document.id.not.found"));
}
