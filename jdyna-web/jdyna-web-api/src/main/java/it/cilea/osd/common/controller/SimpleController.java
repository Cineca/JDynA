package it.cilea.osd.common.controller;

import it.cilea.osd.common.service.IPersistenceService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

public class SimpleController extends BaseAbstractController {

	public SimpleController(Class<?> objectClass) {
		this.objectClass = objectClass;	
	}

	
	protected IPersistenceService applicationService;
	protected Class<?> objectClass;
	private String i18nPrefix = "action";

	public void setI18nPrefix(String prefix) {
		i18nPrefix = prefix;
	}

	public void setApplicationService(IPersistenceService applicationService) {
		this.applicationService = applicationService;
	}

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView retValue = null;
		if ("details".equals(method))
			retValue = handleDetails(request);
		else if ("list".equals(method))
			retValue = handleList(request);
		else if ("delete".equals(method))
			retValue = handleDelete(request);
		return retValue;
	}

	protected ModelAndView handleDetails(HttpServletRequest request) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		Map<String, Object> model = new HashMap<String, Object>();
		String paramId = request.getParameter("id");
		Integer objectId = Integer.valueOf(paramId);
			
		model.put("domainObject", applicationService.get(objectClass, objectId));		
		return new ModelAndView(detailsView,model);
	}

	protected ModelAndView handleList(HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		
		List<?> list = applicationService.getList(objectClass);
		model.put("objectList", list);
		return new ModelAndView(listView, model);
	}
	
	private ModelAndView handleDelete(HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		String paramId = request.getParameter("id");
		Integer id = Integer.valueOf(paramId);
		
	
		try {
			applicationService.delete(objectClass, id);
			saveMessage(request, getText(i18nPrefix + ".deleted", request
					.getLocale()));
		}
		catch (Exception e) {			
			saveMessage(request, getText(i18nPrefix + ".notdeleted", request
					.getLocale()));
		
			return new ModelAndView(errorView, model);
		}
		return new ModelAndView(listView, model);
	}
}
