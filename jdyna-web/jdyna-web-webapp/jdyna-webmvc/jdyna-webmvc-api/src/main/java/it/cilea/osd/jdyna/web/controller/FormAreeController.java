package it.cilea.osd.jdyna.web.controller;

import it.cilea.osd.common.controller.BaseFormController;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.service.IPersistenceDynaService;
import it.cilea.osd.jdyna.web.IPropertyHolder;
import it.cilea.osd.jdyna.web.Tab;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;

public class FormAreeController<T extends PropertiesDefinition, H extends IPropertyHolder<T>, A extends Tab<T,H>> extends BaseFormController {
	
	
	private IPersistenceDynaService applicationService;
	
	private Class<A> targetModel;
	
	private Class<T> targetPropertyDefinition;

	public void setApplicationService(IPersistenceDynaService applicationService) {
		this.applicationService = applicationService;
	}

	@Override
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {

	
		A area = null;
		String areaId = request.getParameter("areaId");
		if (areaId != null && (!"".equals(areaId))) {
			
			area = applicationService.get(targetModel, Integer
					.valueOf(areaId));

		} else {
			logger.debug("Instanced a new Area in form aree controller");
			request.setAttribute("rapida", true); 
			area = targetModel.newInstance();
		}
		return area;
	}

	@Override
	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		
		List<T> allTipologieProprieta = applicationService.getListTipologieProprietaFirstLevel(targetPropertyDefinition);
		List<T> maskTipologieProprieta = ((A)command).getMaschera();
		
		if (maskTipologieProprieta!=null)
			allTipologieProprieta.removeAll(maskTipologieProprieta); 

		map.put("listaTipologieArea",allTipologieProprieta);
		return map;
	}

	@Override
	protected ModelAndView processFormSubmission(HttpServletRequest request,
			HttpServletResponse response, Object object, BindException errors)
			throws Exception {
		if (request.getParameter("cancel") != null) {
			A area = (A) object;
			if (area.getId() != null)
				return new ModelAndView(detailsView, "areaId", area.getId());
			else
				return new ModelAndView(listView);
		}
		return super.processFormSubmission(request, response, object, errors);
	}

	@Override
	protected void onBind(HttpServletRequest arg0, Object arg1) throws Exception {
		List<T> mascheraApp = new  LinkedList<T>();
		String mascheraParam = arg0.getParameter("mascheraxxx");
		if (mascheraParam != null && mascheraParam.trim().length() > 0)
		{
			String[] mascheraParamIds = mascheraParam.split(",");
			for (String mascId : mascheraParamIds)
			{
				mascId = mascId.trim();
				mascheraApp.add(applicationService.get(targetPropertyDefinition, Integer.parseInt(mascId)));
			}
		}
		A area = (A) arg1;
		area.setMaschera(mascheraApp);
	}
    

    
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object object, BindException errors)
			throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
        
		A area = (A) object;
		

		boolean firstInsert = area.getId() == null;
		applicationService.saveOrUpdate(targetModel, area);
		
		saveMessage(request, getText("action.area."
				+ (firstInsert ? "created" : "updated"), request.getLocale()));
		model.put("areaId", area.getId());
		return new ModelAndView(detailsView, model);
	}

	public void setTargetModel(Class<A> targetModel) {
		this.targetModel = targetModel;
	}

	public void setTargetPropertyDefinition(Class<T> targetPropertyDefinition) {
		this.targetPropertyDefinition = targetPropertyDefinition;
	}
}
