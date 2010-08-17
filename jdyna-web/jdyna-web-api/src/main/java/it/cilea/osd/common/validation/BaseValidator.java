package it.cilea.osd.common.validation;

import it.cilea.osd.common.constants.Constants;
import it.cilea.osd.common.model.BaseObject;

import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.springframework.context.MessageSource;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.web.context.support.WebApplicationContextUtils;

public abstract class BaseValidator implements Validator {
	private static Log log = LogFactory.getLog(BaseValidator.class);
	
	private Class clazz;
	
	public boolean supports(Class clazz) {
		return BaseObject.class.isAssignableFrom(clazz);
	}

	/**
	 * Get the FieldError validation message from the underlying MessageSource for the given fieldName.
	 *
	 * @param errors The validation errors.
	 * @param fieldName The fieldName to retrieve the error message from.
	 * @return The validation message or an empty String.
	 */
	protected String getValidationMessage(Errors errors, String fieldName)
	{
	 String message = "";
	 FieldError fieldError = errors.getFieldError(fieldName);	 
	 if (fieldError != null)
	 {		 
		WebContext ctx = WebContextFactory.get();		 
		MessageSource messageSource = WebApplicationContextUtils.getWebApplicationContext(ctx.getServletContext());
		String errore = fieldError.getCode();	
		//il messaggio deve essere ripescato dal resource bundle
		ResourceBundle resourceBundle = ResourceBundle.getBundle(Constants.BUNDLE_KEY, ctx.getHttpServletRequest().getLocale());						 
		message = resourceBundle.getString(errore);		 
	 }
	 return message;
	}
	
	protected String getValidationMessage(String errors)
	{
	 String message = "";	 	 
	 if (errors != null && errors.length()!=0)
	 {		 
		WebContext ctx = WebContextFactory.get();		 
		MessageSource messageSource = WebApplicationContextUtils.getWebApplicationContext(ctx.getServletContext());			
		//il messaggio deve essere ripescato dal resource bundle
		ResourceBundle resourceBundle = ResourceBundle.getBundle(Constants.BUNDLE_KEY, ctx.getHttpServletRequest().getLocale());						 
		message = resourceBundle.getString(errors);		 
	 }
	 return message;
	}
		
	public Class getClazz() {
		return clazz;
	}

	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}
}
