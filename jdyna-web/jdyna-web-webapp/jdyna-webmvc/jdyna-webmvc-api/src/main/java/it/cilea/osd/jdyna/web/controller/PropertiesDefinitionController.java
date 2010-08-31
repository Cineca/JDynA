package it.cilea.osd.jdyna.web.controller;

import it.cilea.osd.common.controller.BaseAbstractController;
import it.cilea.osd.jdyna.model.AlberoClassificatorio;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.model.Soggettario;
import it.cilea.osd.jdyna.service.IPersistenceDynaService;
import it.cilea.osd.jdyna.web.Containable;
import it.cilea.osd.jdyna.web.IPropertyHolder;
import it.cilea.osd.jdyna.web.ITabService;
import it.cilea.osd.jdyna.web.Tab;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

public class PropertiesDefinitionController<TP extends PropertiesDefinition, H extends IPropertyHolder<Containable>, T extends Tab<H>> extends BaseAbstractController {

    private Class<TP> targetModel;
    private Class<H> holderModel;
    
	private IPersistenceDynaService applicationService;



	public PropertiesDefinitionController(Class<TP> targetModel, Class<H> propertyHolder)
    {
        this.targetModel = targetModel;
        this.holderModel = propertyHolder;
    }

    public void setApplicationService(IPersistenceDynaService applicationService) {
		this.applicationService = applicationService;
	}

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView retValue = null;
		if ("details".equals(method))
			retValue = handleDetails(request);
		else if ("details2".equals(method))
			retValue = handleDetails2(request);
		else if ("delete".equals(method))
			retValue = handleDelete(request);
		else if ("list".equals(method))
			retValue = handleList(request);
		return retValue;
	}

	private ModelAndView handleDetails(HttpServletRequest request) {
	    Map<String, Object> model = new HashMap<String, Object>();
        String paramTipologiaProprietaId = request.getParameter("id");
                
        
        //FIXME se null vuol dire che stavo uscendo dal flusso senza salvare (ho messo il controllo qui ma va rifatto il flusso)
        if(paramTipologiaProprietaId==null || paramTipologiaProprietaId.equals("null")) {
        	return handleList(request);
        }
        
        Integer tipologiaProprietaId = Integer.valueOf(paramTipologiaProprietaId);

        TP propertiesDefinition = applicationService.get(targetModel, tipologiaProprietaId);
       
        /*Se si è voluto creare un soggettario come particolare widget testo, porto con me l'id
         * del soggettario per potere procedere alla scelta di un nome e voce per il soggettario*/
        String paramId = request.getParameter("soggettarioId");
        if (paramId != null){
        	Integer Id= Integer.valueOf(paramId);
        	Soggettario soggettario = applicationService.get(Soggettario.class, Id);
        	model.put("soggettarioId", soggettario.getId());
        }
        
        paramId = request.getParameter("alberoId");
        if (paramId != null){
        	Integer Id = Integer.valueOf(paramId);
        	AlberoClassificatorio albero = applicationService.get(AlberoClassificatorio.class, Id);
        	model.put("alberoId", albero.getId());
        }
        
        model.put("tipologiaProprieta", propertiesDefinition);
        model.put("addModeType", "display");
        
        return new ModelAndView(detailsView, model);
    }

	private ModelAndView handleDetails2(HttpServletRequest request) {
        Map<String, Object> model = new HashMap<String, Object>();
        String paramTipologiaProprietaId = request.getParameter("id");
        Integer tipologiaProprietaId = Integer.valueOf(paramTipologiaProprietaId);
        TP propertiesDefinition = applicationService.get(targetModel, tipologiaProprietaId);
        
        model.put("tipologiaProprieta", propertiesDefinition);
        model.put("addModeType", "display");
        return new ModelAndView(detailsView, model);
    }

	private ModelAndView handleList(HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		List<TP> listTipologiaProprieta = applicationService.getListTipologieProprietaFirstLevel(targetModel);	 
		model.put("tipologiaProprietaList", listTipologiaProprieta);		
		return new ModelAndView(listView, model);
	}
	

	private ModelAndView handleDelete(HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		String paramOTipologiaProprietaId = request.getParameter("id");
		Integer tipologiaProprietaId = Integer.decode(paramOTipologiaProprietaId);
		
		try {

			TP tip = applicationService.get(targetModel, tipologiaProprietaId);
			//cancello tutte le proprietà salvate in passato
			applicationService.deleteAllProprietaByTipologiaProprieta(targetModel, tip);
						
			//cancello la tipologia di proprieta
			applicationService.delete(targetModel, tipologiaProprietaId);
		} catch (Exception ecc) {

			return new ModelAndView(errorView, model);
		}
		
		
		saveMessage(request, getText("action.tipologiaProprietaOpera.deleted", request
				.getLocale()));
		
		
		return new ModelAndView(listView, model);
	}
}
