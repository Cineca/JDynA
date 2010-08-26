package it.cilea.osd.jdyna.controller;

import it.cilea.osd.common.controller.BaseAbstractController;
import it.cilea.osd.jdyna.web.Containable;
import it.cilea.osd.jdyna.web.IPropertyHolder;
import it.cilea.osd.jdyna.web.ITabService;
import it.cilea.osd.jdyna.web.Tab;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

/**
 * Abstract SpringMVC controller is used to list, delete and view detail of tab
 * 
 * @author pascarelli
 * 
 */
public abstract class TabsController<H extends IPropertyHolder<Containable>, T extends Tab<H>> extends BaseAbstractController {
	
	
	private Class<T> tabsClass;
	
	private ITabService<H, T> applicationService;
	
	
	public TabsController(Class<T> tabsClass) {
		this.tabsClass = tabsClass;
	}
		
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView retValue = null;
		if ("details".equals(method))
			retValue = handleDetails(request);
		else if ("delete".equals(method))
			retValue = handleDelete(request);
		else if ("list".equals(method))
			retValue = handleList(request);
		return retValue;
	}

		
	private ModelAndView handleDetails(HttpServletRequest request) {
		
		Map<String, Object> model = new HashMap<String, Object>();
		String tabId = request.getParameter("id");
		Integer paramTypeTabId = Integer.valueOf(tabId);
		T tab = applicationService.get(tabsClass, paramTypeTabId);		

	    List<H> containerList = applicationService.findPropertyHolderInTab(tabsClass, tab.getId());
		
		model.put("id", paramTypeTabId);
		model.put("tab", tab);
		model.put("containerList", containerList);
		
		return new ModelAndView(detailsView, model);

	}
	

	private ModelAndView handleDelete(HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		String tabId = request.getParameter("id");
		Integer paramTypeTabId = Integer.valueOf(tabId);
		try {
			applicationService.delete(tabsClass, paramTypeTabId);		
			saveMessage(request, getText("action.tab.deleted", request.getLocale()));
		}
		catch (Exception e) {
			saveMessage(request, getText("action.tab.deleted.noSuccess", request.getLocale()));			
		}
		return new ModelAndView(listView, model);
	}


	private ModelAndView handleList(HttpServletRequest arg0) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		List<T> tabs = new LinkedList<T>();
		tabs = applicationService.getList(tabsClass);
		model.put("listTab", tabs);
		return new ModelAndView(listView,model);
	}


	public void setApplicationService(ITabService<H, T> applicationService) {
		this.applicationService = applicationService;
	}


	public ITabService<H, T> getApplicationService() {
		return applicationService;
	}
}
