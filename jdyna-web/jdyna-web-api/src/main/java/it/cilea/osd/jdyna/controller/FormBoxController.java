package it.cilea.osd.jdyna.controller;

import it.cilea.osd.common.controller.BaseFormController;
import it.cilea.osd.jdyna.model.ANestedPropertiesDefinition;
import it.cilea.osd.jdyna.model.ATypeNestedObject;
import it.cilea.osd.jdyna.model.Containable;
import it.cilea.osd.jdyna.model.IContainable;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.web.IPropertyHolder;
import it.cilea.osd.jdyna.web.ITabService;
import it.cilea.osd.jdyna.web.Tab;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;



public abstract class FormBoxController<TP extends PropertiesDefinition, H extends IPropertyHolder<Containable>, T extends Tab<H>, ATTP extends ANestedPropertiesDefinition, TTP extends ATypeNestedObject<ATTP>> extends BaseFormController {

	/**
     * the applicationService for query the Tab db, injected by Spring IoC
     */
	protected ITabService applicationService;
	
	private Class<H> boxClass;

	private Class<TP> tpClass;
	
	private Class<TTP> ttpClass;
	
	public FormBoxController(Class<H> clazzH, Class<TP> clazzTP, Class<TTP> clazzTTP) {
		this.boxClass = clazzH;
		this.setTpClass(clazzTP);
		this.setTtpClass(clazzTTP);
	}
	
	@Override
	protected Map referenceData(HttpServletRequest request) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		String paramId = request.getParameter("id");
		
		List<IContainable> owneredContainables = new LinkedList<IContainable>();
		
		List<TP> modelJdynaContainables = applicationService.getList(tpClass);
		List<TTP> ttps = applicationService.getList(ttpClass);
		List<IContainable> containables = new LinkedList<IContainable>();
		for(TP tp : modelJdynaContainables) {
			IContainable ic = applicationService.findContainableByDecorable(tp.getDecoratorClass(), tp.getId());
			if(ic!=null) {
				containables.add(ic);
			}
		}
		
		for(TTP ttp : ttps) {
            IContainable ic = applicationService.findContainableByDecorable(ttp.getDecoratorClass(), ttp.getId());
            if(ic!=null) {
                containables.add(ic);
            }
        }
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
	

	public void setApplicationService(ITabService applicationService) {
		this.applicationService = applicationService;
	}


	public void setboxClass(Class<H> boxClass) {
		this.boxClass = boxClass;
	}

	public void setTpClass(Class<TP> tpClass) {
		this.tpClass = tpClass;
	}

    public void setTtpClass(Class<TTP> ttpClass)
    {
        this.ttpClass = ttpClass;
    }

    public Class<TTP> getTtpClass()
    {
        return ttpClass;
    }

	
	
}
