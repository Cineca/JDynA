package it.cilea.osd.jdyna.controller;

import it.cilea.osd.common.controller.BaseAbstractController;
import it.cilea.osd.jdyna.web.Containable;
import it.cilea.osd.jdyna.web.IContainable;
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
 * Abstract SpringMVC controller is used to list, delete and view detail of box
 * 
 * @author pascarelli
 * 
 */
public abstract class BoxsController<H extends IPropertyHolder<Containable>, T extends Tab<H>> extends BaseAbstractController {
	
	
	private Class<H> boxClass;
	
	private ITabService<H, T> applicationService;
	
	
	public BoxsController(Class<H> boxClass) {
		this.boxClass = boxClass;
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
		Integer paramTypeBoxId = Integer.valueOf(tabId);
		H box = applicationService.get(boxClass, paramTypeBoxId);		

	    List<IContainable> containableList = applicationService.findContainableInPropertyHolder(boxClass, box.getId());
				
		model.put("box", box);
		model.put("containableList", containableList);
		
		return new ModelAndView(detailsView, model);

	}
	

	protected ModelAndView handleDelete(HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		String boxId = request.getParameter("id");
		Integer paramTypeBoxId = Integer.valueOf(boxId);
		try {
			applicationService.delete(boxClass, paramTypeBoxId);	
			saveMessage(request, getText("action.box.deleted"));
		}
		catch (Exception e) {
			saveMessage(request, getText("action.box.deleted.noSuccess"));			
		}
		return new ModelAndView(listView, model);
	}


	private ModelAndView handleList(HttpServletRequest arg0) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		List<H> boxs = new LinkedList<H>();
		boxs = applicationService.getList(boxClass);
		model.put("listTab", boxs);
		return new ModelAndView(listView,model);
	}


	public void setApplicationService(ITabService<H, T> applicationService) {
		this.applicationService = applicationService;
	}


	public ITabService<H, T> getApplicationService() {
		return applicationService;
	}
}
