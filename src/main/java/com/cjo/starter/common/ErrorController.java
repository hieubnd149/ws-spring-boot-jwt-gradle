package com.cjo.starter.common;

import java.sql.SQLException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.cjo.starter.common.domain.Response;
import com.cjo.starter.common.exception.UserException;

/**
 * 
 * The class ErrorController<br>
 * <br>
 * Catch all uncaught error around the application.<br>
 * <br>
 * @author Tomas
 * @version 1.0
 * @since Feb 20, 2019
 *
 */
@ControllerAdvice
public class ErrorController extends ResponseEntityExceptionHandler {

	private static final Logger LOG = LogManager.getLogger(ErrorController.class);

	private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;
	
	@Autowired
	private MessageSource messageSource;

	private String getMessage(final String messageCode) {
		try {
			return messageSource.getMessage(messageCode, null, DEFAULT_LOCALE);
		} catch (NoSuchMessageException ex) {
			return null;
		}
	}

	@ExceptionHandler(SQLException.class)
	public ResponseEntity<Object> handleSQLException(HttpServletRequest request, SQLException ex){
		LOG.error("Execute SQL error", ex);
		String message = "Something's wrong (" + HttpStatus.INTERNAL_SERVER_ERROR.value() + ")";

		return new ResponseEntity<Object>(new Response(HttpStatus.INTERNAL_SERVER_ERROR.value(), message), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(
			Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
		int code = status.value();
		
		String message = getMessage(ex.getMessage());
		if (message == null) {
			message = "Something's wrong (" + code + ")";	
		}
		return new ResponseEntity<Object>(new Response(code, message), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * 
	 * Catch defined exception and customize its look <br>
	 * <br>
	 * @param ex
	 * @param request
	 * @return
	 * @author Tomas
	 * @since Feb 20, 2019
	 */
	@ExceptionHandler({ UserException.class })
	public ResponseEntity<Object> handleApplicationExceptionInternal(final RuntimeException ex, final WebRequest request) {
		String message = getMessage(ex.getMessage());
		if (message == null) {
			message = ex.getMessage();	
		}
		return new ResponseEntity<Object>(new Response(HttpStatus.BAD_REQUEST.value(), message), HttpStatus.OK);
	}

	@ExceptionHandler({ NullPointerException.class, IllegalArgumentException.class, IllegalStateException.class })
	public ResponseEntity<Object> handleInternal(final RuntimeException ex, final WebRequest request) {
		return handleExceptionInternal(ex, null, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
	}
	
	@ExceptionHandler({ AccessDeniedException.class })
	public ResponseEntity<Object> handleAccessDeniedInternal(final RuntimeException ex, final WebRequest request) {
		return handleExceptionInternal(ex, null, new HttpHeaders(), HttpStatus.FORBIDDEN, request);
	}
	
}
