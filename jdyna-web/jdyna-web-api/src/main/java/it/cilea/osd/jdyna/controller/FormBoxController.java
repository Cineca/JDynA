package it.cilea.osd.jdyna.controller;

import it.cilea.osd.common.controller.BaseFormController;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
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

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;



public abstract class FormBoxController<TP extends PropertiesDefinition, H extends IPropertyHolder<Containable>, T extends Tab<H>> extends BaseFormController {

	/**
     * the applicationService for query the Tab db, injected by Spring IoC
     */
	protected ITabService<H, T> applicationService;
	
	private Class<H> boxClass;

	private Class<TP> tpClass;
	
	public FormBoxController(Class<H> clazzH, Class<TP> clazzTP) {
		this.boxClass = clazzH;
		this.setTpClass(clazzTP);
	}
	
	@Override
	protected Map referenceData(HttpServletRequest request) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		String paramId = request.getParameter("id");
		List<IContainable> owneredContainables = new LinkedList<IContainable>();
		List<IContainable> containables = applicationService.findAllContainables(tpClass);
		if (paramId != null) {
			Integer id = Integer.parseInt(paramId);
			owneredContainables = applicationService.findContainableInPropertyHolder(boxClass, id);
		} 
		map.put("containablesList", containables);
		map.put("owneredContainables", owneredContainables);
		return map;

	}	
	
	
	@Override
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		String paramId = request.getParameter("id");
		H box = null;
		if (paramId == null) {
			box = boxClass.newInstance();
		
		} else {
			box = applicationService.get(boxClass, Integer.parseInt(paramId));
		}
		return box;
	}
	
	@Override
	protected void onBind(HttpServletRequest request, Object command)
			throws Exception {
		// TODO Auto-generated method stub
		super.onBind(request, command);
	}

	public void setApplicationService(ITabService<H, T> applicationService) {
		this.applicationService = applicationService;
	}


	public void setboxClass(Class<H> boxClass) {
		this.boxClass = boxClass;
	}

	public void setTpClass(Class<TP> tpClass) {
		this.tpClass = tpClass;
	}

	
	
}
