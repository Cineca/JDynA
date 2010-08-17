package it.cilea.osd.jdyna.web.controller;

import it.cilea.osd.common.controller.BaseAbstractController;
import it.cilea.osd.jdyna.web.IPropertyHolder;
import it.cilea.osd.jdyna.web.ITabService;
import it.cilea.osd.jdyna.web.Tab;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

public class TabController<H extends IPropertyHolder> extends BaseAbstractController{

	protected ITabService applicationService;


	public void setApplicationService(ITabService applicationService) {
		this.applicationService = applicationService;
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
		//devo mettere nel modello le tipologiediproprieta progetto che non matchano
		//con quelle mascherate
		Map<String, Object> model = new HashMap<String, Object>();
		String areaId = request.getParameter("areaId");
		Integer paramTypeAreaId = Integer.valueOf(areaId);
		Tab area = applicationService.get(Tab.class, paramTypeAreaId);		

	    List<H> listaTipArea = applicationService.findPropertyHolderInTab(paramTypeAreaId);
		
		model.put("areaId", paramTypeAreaId);
		model.put("area", area);
		model.put("listaTipologieArea", listaTipArea);
		model.put("addModeType", "display");
		

		return new ModelAndView(detailsView, model);
	
	}

	
	private ModelAndView handleList(HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();

		List<Tab> listaAree = applicationService.getList(Tab.class); 
		
		model.put("listaAree",listaAree);

		return new ModelAndView(listView, model);
	}
	

	private ModelAndView handleDelete(HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		String areaId = request.getParameter("areaId");
		Integer paramTypeAreaId = Integer.valueOf(areaId);
		try {
			applicationService.delete(Tab.class, paramTypeAreaId);		
			saveMessage(request, getText("action.area.deleted", request.getLocale()));
		}
		catch (Exception e) {
			saveMessage(request, getText("action.area.deleted.noSuccess", request.getLocale()));			
		}
		return new ModelAndView(listView, model);
	}
}
