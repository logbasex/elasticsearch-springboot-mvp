package logbasex.search.db.dto;

import logbasex.search.business.common.ErrorInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;

public class ResponseObject<T> {

	private static final Logger logger = LoggerFactory.getLogger(ResponseObject.class);

	private static Properties properties;

	static {
		try {
			properties = new Properties();
			InputStream is = ErrorInfo.class.getResourceAsStream("/system_messages.properties");
			properties.load(is);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	public ResponseObject(){

	}

	public ResponseObject(T responseData){
		this.responseData = responseData;
	}

	private ErrorInfo error;
	private T responseData;
	private T extraData;
	private String successMessage;
	private String warningMessage;

	public ErrorInfo getError() {
		return error;
	}

	public void setError(ErrorInfo error) {
		this.error = error;
	}

	public T getResponseData() {
		return responseData;
	}

	public void setResponseData(T responseData) {
		this.responseData = responseData;
	}

	public String getSuccessMessage() {
		return successMessage;
	}

	/**
	 * this method to display exactly string as input, not read from properties file
	 * @param message
	 */
	public void setSuccessMessage(String message) {
		this.successMessage = message;
	}

	public void setSuccessMessage(String pKey, Object... objects) {

		try {
			this.successMessage = MessageFormat.format(properties.getProperty(pKey), objects);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}

	public T getExtraData() {
		return extraData;
	}

	public void setExtraData(T extraData) {
		this.extraData = extraData;
	}

	public String getWarningMessage() {
		return warningMessage;
	}

	public void setWarningMessage(String warningMessage) {
		this.warningMessage = warningMessage;
	}
}
