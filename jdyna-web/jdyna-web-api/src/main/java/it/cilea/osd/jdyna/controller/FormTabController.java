package it.cilea.osd.jdyna.controller;

import it.cilea.osd.common.controller.BaseFormController;
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

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

public abstract class FormTabController<H extends IPropertyHolder<Containable>, T extends Tab<H>>
		extends BaseFormController {

	/**
	 * the applicationService for query the Tab db, injected by Spring IoC
	 */
	protected ITabService applicationService;



	private Class<T> tabClass;

	private Class<H> boxClass;
	
	public FormTabController(Class<T> clazzT, Class<H> clazzB) {
		this.tabClass = clazzT;
		this.boxClass = clazzB;
	}

	@Override
	protected Map referenceData(HttpServletRequest request) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		String paramId = request.getParameter("id");
		List<H> owneredContainers = new LinkedList<H>();
		List<H> containers = applicationService.getList(boxClass);
		if (paramId != null) {
			Integer id = Integer.parseInt(paramId);
			owneredContainers = applicationService.findPropertyHolderInTab(
					tabClass, id);
		}
		map.put("boxsList", containers);
		map.put("owneredBoxs", owneredContainers);
		return map;
	}

	@Override
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		String paramId = request.getParameter("id");
		T tab = null;
		if (paramId == null) {
			tab = tabClass.newInstance();
		
		} else {
			tab = applicationService.get(tabClass, Integer.parseInt(paramId));
		}
		return tab;
	}
	
	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		T object = (T)command;		
		applicationService.saveOrUpdate(tabClass, object);
		return new ModelAndView(getSuccessView());
	}
	
	public void setApplicationService(ITabService applicationService) {
		this.applicationService = applicationService;
	}

	public void setTabClass(Class<T> tabClass) {
		this.tabClass = tabClass;
	}

	public void setBoxClass(Class<H> boxClass) {
		this.boxClass = boxClass;
	}

}
