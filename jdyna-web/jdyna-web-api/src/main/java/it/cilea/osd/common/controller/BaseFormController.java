package it.cilea.osd.common.controller;

import it.cilea.osd.common.constants.Constants;

import java.beans.PropertyEditor;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.mvc.SimpleFormController;

/**
 * Base form controller.
 *  
 */
public class BaseFormController extends SimpleFormController {
	private static Log log = LogFactory.getLog(BaseFormController.class);

	private Map<Class, PropertyEditor> customPropertyEditors;
	
	protected String detailsView;

	protected String listView;
	
	protected String errorView;

	public BaseFormController() {
		super();
	}
	
	public void setDetailsView(String detailsView) {
		this.detailsView = detailsView;
	}

	public void setListView(String listView) {
		this.listView = listView;
	}
	
	public void setErrorView(String errorView) {
		this.errorView = errorView;
	}

	
	public void saveMessage(HttpServletRequest request, String msg) {
		List messages = (List) request.getSession().getAttribute(
				Constants.MESSAGES_KEY);

		if (messages == null) {
			messages = new ArrayList();
		}

		messages.add(msg);
		request.getSession().setAttribute(Constants.MESSAGES_KEY, messages);
	}

	/**
	 * Convenience method for getting a i18n key's value. Calling
	 * getMessageSourceAccessor() is used because the RequestContext variable is
	 * not set in unit tests b/c there's no DispatchServlet Request.
	 * 
	 * @param msgKey
	 * @param locale
	 *            the current locale
	 * @return
	 */
	public String getText(String msgKey, Locale locale) {
		return getMessageSourceAccessor().getMessage(msgKey, locale);
	}

	/**
	 * Convenient method for getting a i18n key's value with a single string
	 * argument.
	 * 
	 * @param msgKey
	 * @param arg
	 * @param locale
	 *            the current locale
	 * @return
	 */
	public String getText(String msgKey, String arg, Locale locale) {
		return getText(msgKey, new Object[] { arg }, locale);
	}

	/**
	 * Convenience method for getting a i18n key's value with arguments.
	 * 
	 * @param msgKey
	 * @param args
	 * @param locale
	 *            the current locale
	 * @return
	 */
	public String getText(String msgKey, Object[] args, Locale locale) {
		return getMessageSourceAccessor().getMessage(msgKey, args, locale);
	}
		
	@Override
	/**
	 * Registra i custom property editors come definiti nella mappa 
	 * customPropertyEditors
	 */
	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) {
		if (customPropertyEditors != null)
		{
			for (Class propertyClass : customPropertyEditors.keySet()) {
				log.debug("Register custom editor "+ customPropertyEditors.get(propertyClass).getClass() + " for the "+propertyClass);
				binder.registerCustomEditor(propertyClass, customPropertyEditors
						.get(propertyClass));
			}
		}
	}
	
	public void setCustomPropertyEditors(
			Map<Class, PropertyEditor> customPropertyEditors) {
		this.customPropertyEditors = customPropertyEditors;
	}
}
