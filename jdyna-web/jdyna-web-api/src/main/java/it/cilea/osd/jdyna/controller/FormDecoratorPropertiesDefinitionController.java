package it.cilea.osd.jdyna.controller;

import it.cilea.osd.common.controller.BaseFormController;
import it.cilea.osd.jdyna.model.AValue;
import it.cilea.osd.jdyna.model.AWidget;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.web.ADecoratorPropertiesDefinition;
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

public abstract class FormDecoratorPropertiesDefinitionController<W extends AWidget, TP extends PropertiesDefinition, DTP extends ADecoratorPropertiesDefinition<TP>, H extends IPropertyHolder<Containable>, T extends Tab<H>>
		extends BaseFormController {

	private ITabService applicationService;
	
	private Class<TP> targetModel;
	private Class<W> renderingModel;

	public FormDecoratorPropertiesDefinitionController(Class<TP> targetModel,
			Class<W> renderingModel) {
		this.targetModel = targetModel;
		this.renderingModel = renderingModel;
	}

	@Override
	protected Map referenceData(HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String paramBoxId = request.getParameter("boxId");
		String paramTabId = request.getParameter("tabId");
		map.put("tabId", paramTabId);
		map.put("boxId", paramBoxId);
		return map;
	}
	
	
	@Override
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		DTP decorator = null;
		TP object = null;
		String paramId = request.getParameter("pDId");
		if (paramId == null || paramId.isEmpty()) {
			object = targetModel.newInstance();
			W widget = renderingModel.newInstance();
			object.setRendering(widget);
			decorator = (DTP) object.getDecoratorClass().newInstance();
			decorator.setReal(object);
		} else {
			Integer id = Integer.parseInt(paramId);
			object = applicationService.get(targetModel, id);
			decorator = (DTP) applicationService.findContainableByDecorable(
					object.getDecoratorClass(), id);
		}
		return decorator;
	}
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		DTP object = (DTP) command;
		applicationService.saveOrUpdate(object.getDecoratorClass(), object);
		return new ModelAndView(getSuccessView());
	}

	public void setApplicationService(ITabService applicationService) {
		this.applicationService = applicationService;
	}

	public ITabService getApplicationService() {
		return applicationService;
	}

	public Class<TP> getTargetModel() {
		return targetModel;
	}

}
