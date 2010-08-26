package it.cilea.osd.jdyna.controller;

import it.cilea.osd.common.controller.BaseAbstractController;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.web.Containable;
import it.cilea.osd.jdyna.web.IContainable;
import it.cilea.osd.jdyna.web.IPropertyHolder;
import it.cilea.osd.jdyna.web.ITabService;
import it.cilea.osd.jdyna.web.Tab;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

public class DecoratorPropertiesDefinitionController<TP extends PropertiesDefinition, H extends IPropertyHolder<Containable>, T extends Tab<H>> extends BaseAbstractController {

    private Class<TP> targetModel;
    private Class<H> holderModel;
    
	protected ITabService<H, T> applicationService;


	public DecoratorPropertiesDefinitionController(Class<TP> targetModel, Class<H> holderModel)
    {
        this.targetModel = targetModel;
        this.holderModel = holderModel;
    }

    public void setApplicationService(ITabService<H, T> applicationService) {
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
        Map<String, Object> model = new HashMap<String, Object>();
        String paramTipologiaProprietaId = request.getParameter("id");
        Integer tipologiaProprietaId = Integer.valueOf(paramTipologiaProprietaId);
        PropertiesDefinition propertiesDefinition = applicationService.get(targetModel, tipologiaProprietaId);
        
        model.put("tipologiaProprieta", propertiesDefinition);
        model.put("addModeType", "display");
        return new ModelAndView(detailsView, model);
    }

	private ModelAndView handleList(HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		List<? extends PropertiesDefinition> listTipologiaProprieta = applicationService.getListTipologieProprietaFirstLevel(targetModel);	 
		model.put("tipologiaProprietaList", listTipologiaProprieta);		
		return new ModelAndView(listView, model);
	}
	

	private ModelAndView handleDelete(HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		String paramOTipologiaProprietaId = request.getParameter("id");
		Integer tipologiaProprietaId = Integer.decode(paramOTipologiaProprietaId);
		
		try {

			PropertiesDefinition tip = applicationService.get(targetModel, tipologiaProprietaId);
			
			//cancello tutte le proprietà salvate in passato
			applicationService.deleteAllProprietaByTipologiaProprieta(tip.getPropertyHolderClass(), tip);
			//cancello se fanno parte di qualche property holder		
			IContainable containable = applicationService.findContainableByDecorable(tip.getDecoratorClass(),tipologiaProprietaId);
			applicationService.deleteContainableInPropertyHolder(holderModel,containable);
			//cancello il decorator e in cascata la tipologia di proprieta
			applicationService.delete(tip.getDecoratorClass(), (Containable)containable);
		} catch (Exception ecc) {

			return new ModelAndView(errorView, model);
		}
		
		
		saveMessage(request, getText("action.tipologiaProprietaOpera.deleted", request
				.getLocale()));
		
		
		return new ModelAndView(listView, model);
	}
}
