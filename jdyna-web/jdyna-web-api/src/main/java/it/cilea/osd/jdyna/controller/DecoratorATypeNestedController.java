package it.cilea.osd.jdyna.controller;

import it.cilea.osd.common.controller.BaseAbstractController;
import it.cilea.osd.jdyna.model.ANestedObject;
import it.cilea.osd.jdyna.model.ANestedPropertiesDefinition;
import it.cilea.osd.jdyna.model.ANestedProperty;
import it.cilea.osd.jdyna.model.ATypeNestedObject;
import it.cilea.osd.jdyna.model.Containable;
import it.cilea.osd.jdyna.model.IContainable;
import it.cilea.osd.jdyna.web.IPropertyHolder;
import it.cilea.osd.jdyna.web.ITabService;
import it.cilea.osd.jdyna.web.Tab;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

public class DecoratorATypeNestedController<A extends ATypeNestedObject<NTP>, NP extends ANestedProperty<NTP>, NTP extends ANestedPropertiesDefinition, H extends IPropertyHolder<Containable>, T extends Tab<H>, ANO extends ANestedObject<NP, NTP>> extends BaseAbstractController {

    private Class<A> targetModel;
    private Class<ANO> objectModel;
    private Class<H> holderModel;
    
	protected ITabService applicationService;


	public DecoratorATypeNestedController(Class<A> targetModel, Class<ANO> objectModel, Class<H> holderModel)
    {
        this.targetModel = targetModel;
        this.objectModel = objectModel;
        this.holderModel = holderModel;
    }

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
        Map<String, Object> model = new HashMap<String, Object>();        
        String typeObjectId = request.getParameter("id");
        Integer paramTypeObjectId = Integer.valueOf(typeObjectId);
        A tipologia = applicationService.get(targetModel, paramTypeObjectId);    
              
        
        //daMostrare contiene le tipologie dei valori da mostrare
        model.put("mostraValoriList", tipologia.getMaschera());
        model.put("sizeMostraValori", tipologia.getMaschera().size());
        model.put("id", paramTypeObjectId);
        model.put("tipologiaObject", tipologia);
        model.put("addModeType", "display");
        return new ModelAndView(detailsView, model);
    
    }

	private ModelAndView handleList(HttpServletRequest request) {
	    Map<String, Object> model = new HashMap<String, Object>();
        List<A> listaTipologie = applicationService.getList(targetModel);         
        model.put("tipologiaList", listaTipologie);
        return new ModelAndView(listView, model);
	}
	

	private ModelAndView handleDelete(HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		String paramOTipologiaProprietaId = request.getParameter("pDId");
		String boxId = request.getParameter("boxId");
		String tabId = request.getParameter("tabId");
		Integer tipologiaProprietaId = Integer.decode(paramOTipologiaProprietaId);
		
		try {

			A tip = applicationService.get(targetModel, tipologiaProprietaId);
			
			applicationService.deleteNestedObjectByTypeID(objectModel, tipologiaProprietaId);			
			
			IContainable containable = applicationService.findContainableByDecorable(tip.getDecoratorClass(),tipologiaProprietaId);
            applicationService.<H, T>deleteContainableInPropertyHolder(holderModel,containable);
			applicationService.delete(tip.getDecoratorClass(), containable.getId());
						
			saveMessage(request, getText("action.propertiesdefinition.deleted", request
					.getLocale()));
		} catch (Exception ecc) {			
			saveMessage(request, getText("action.propertiesdefinition.deleted.noSuccess", request
					.getLocale()));			
		}
		
		return new ModelAndView(listView+"?id="+boxId+"&tabId="+tabId, model);
	}
}

