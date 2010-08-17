package it.cilea.osd.common.controller;

import it.cilea.osd.common.constants.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.mvc.AbstractController;

public abstract class BaseAbstractController extends AbstractController {

	protected Log log = LogFactory.getLog(getClass());

	protected String method;

	protected String detailsView;

	protected String listView;
	
	protected String errorView;

	public void setMethod(String method) {
		this.method = method;
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
	
	public void setDetailsView(String detailsView) {
		this.detailsView = detailsView;
	}

	public void setListView(String listView) {
		this.listView = listView;
	}

	public String getErrorView() {
		return errorView;
	}

	public void setErrorView(String errorView) {
		this.errorView = errorView;
	}

}
