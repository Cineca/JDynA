package it.cilea.osd.jdyna.web.controller;

import it.cilea.osd.common.controller.BaseAbstractController;
import it.cilea.osd.common.util.displaytag.DisplayTagData;
import it.cilea.osd.jdyna.model.ATipologia;
import it.cilea.osd.jdyna.model.AnagraficaSupport;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.model.Property;
import it.cilea.osd.jdyna.web.IBoxService;
import it.cilea.osd.jdyna.web.IContainable;
import it.cilea.osd.jdyna.web.IPropertyHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

public class SimpleDynaController <P extends Property<TP>, TP extends PropertiesDefinition, H extends IPropertyHolder>
	extends BaseAbstractController {

	private final int PAGE_SIZE = 20;
	
	protected IBoxService applicationService;
	
	protected Class<? extends AnagraficaSupport<P, TP>> objectClass;
	
	protected Class<TP> tpClass;
	
	protected Class<? extends ATipologia<TP>> typeClass;
	private String modelPath;
	
	private String i18nPrefix = "action";

	public void setTypeClass(Class<? extends ATipologia<TP>> typeClass) {
		this.typeClass = typeClass;
	}
	
	public SimpleDynaController(Class<? extends AnagraficaSupport<P, TP>> anagraficaObjectClass) throws InstantiationException, IllegalAccessException {
		objectClass = anagraficaObjectClass;
		tpClass = objectClass.newInstance().getClassPropertiesDefinition();		
	}
	
	public SimpleDynaController(Class<? extends AnagraficaSupport<P, TP>> anagraficaObjectClass, Class<TP> classTP) throws InstantiationException, IllegalAccessException {
		objectClass = anagraficaObjectClass;
		tpClass = classTP;		
	}
	
	public void setApplicationService(IBoxService applicationService) {
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

	protected ModelAndView handleDetails(HttpServletRequest request) {
		
		Map<String, Object> model = new HashMap<String, Object>();
		String paramObjectId = request.getParameter("id");
		Integer objectId = Integer.valueOf(paramObjectId);
		AnagraficaSupport<P,TP> jdynaObject = (AnagraficaSupport<P,TP>) applicationService.get(objectClass, objectId);
		if(jdynaObject==null){
			throw new RuntimeException("La url non corrisponde a nessun oggetto valido nella piattaforma");
		}
		//FIXME verificare questa parte di codice se effettivamente serve a qualcosa
		Integer ID = null;
		Class<H> areaClass = applicationService.getPropertyHolderClass();
		List<H> listaAllAree =  (List<H>) (applicationService.getList(areaClass));
		

		if (request.getParameter("areaId") != null) {
			ID = Integer.valueOf(request.getParameter("areaId"));
			model.put("first", false);
		} else {		
			if(listaAllAree!=null && !listaAllAree.isEmpty()) {
				H area = listaAllAree.get(0);
				if(area!=null) {
					ID = area.getId();
				}
			}
			else {
				//restituisco le tipo
				model.put("tipologieInArea", applicationService.getListTipologieProprietaFirstLevel(jdynaObject.getClassPropertiesDefinition()));				
				model.put("anagraficaObject", jdynaObject);								
				return new ModelAndView(detailsView, model);
			}
			model.put("first", true);
		}

		model.put("aree", listaAllAree);
		// passo alla view l'ID dell'area selezionata
		model.put("areaId", ID);
		// Passo alla view le tipologie di proprietà da visualizzare nell'area
		List<IContainable> area =null;

		area = applicationService.findContainableInBox(areaClass, ID);
	  
   	    jdynaObject.inizializza();
   	    
	 	model.put("path", modelPath);
	    
	    Class<TP> tipologiaProprieta = jdynaObject.getClassPropertiesDefinition(); 
		List<TP> tpColumnListEpiObject = applicationService.getValoriDaMostrare(tipologiaProprieta);
				
		model.put("showInColumnList", tpColumnListEpiObject);
			
	    model.put("tipologieInArea", area);
	     
		model.put("anagraficaObject", jdynaObject);
				
		model.put("addModeType", "display");
		
		return new ModelAndView(detailsView, model);
	}
	



	protected ModelAndView handleList(HttpServletRequest request) {
		String paramSort = request.getParameter("sort");
		String paramPage = request.getParameter("page");
		String paramDir = request.getParameter("dir");

		String sort = paramSort != null ? paramSort : "id";
		String dir = paramDir != null ? paramDir : "asc";
		int page = paramPage != null ? Integer.parseInt(paramPage) : 1;

		long count = applicationService.count(objectClass);
		
		Map<String, Object> model = new HashMap<String, Object>();
		
		List<? extends AnagraficaSupport<P, TP>> objectlist;
		
		TP tipProp = applicationService.findTipologiaProprietaByShortName(
				tpClass, sort);
		if (tipProp != null) {
			objectlist = applicationService
					.getPaginateListByTipologiaProprieta(objectClass, tipProp
							.getId(), "desc".equals(dir), page, PAGE_SIZE);
		} else {
			objectlist = applicationService.getPaginateList(objectClass, sort,
					"desc".equals(dir), page, PAGE_SIZE);
		}
		
		List<TP> listTipologieShowInColumn = applicationService.getValoriDaMostrare(tpClass);
		
		DisplayTagData displayList = new DisplayTagData(count, objectlist, sort, dir,
				page, PAGE_SIZE);
		
		model.put("objectList", displayList);
		model.put("showInColumnList", listTipologieShowInColumn);
				
		return new ModelAndView(listView, model);
	}
	
	

	protected ModelAndView handleDelete(HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		String paramId = request.getParameter("id");
		Integer epiobjectID = Integer.valueOf(paramId);
		/* uso il delete controllato */
		AnagraficaSupport<P,TP> a= applicationService.get(objectClass,epiobjectID);
		
		try {
			applicationService.delete(objectClass,epiobjectID);
			saveMessage(request, getText(i18nPrefix + ".deleted", request
					.getLocale()));
		}
		catch (Exception e){
			log.error("Non è stato possibile eliminare l'oggetto " + e);
			saveMessage(request, getText(i18nPrefix + ".notdeleted", request
					.getLocale()));		
			return new ModelAndView(detailsView+"?id="+epiobjectID, model);
		}
				
		return redirect(a,request);		
	}

	/** Redirige sulla pagina del dettaglio */
	public ModelAndView redirect(AnagraficaSupport<P, TP> epi,HttpServletRequest request) {
		return new ModelAndView(listView, null);
	}
	
	public String getI18nPrefix() {
		return i18nPrefix;
	}

	public void setI18nPrefix(String prefix) {
		i18nPrefix = prefix;
	}

	public void setModelPath(String modelPath) {
		this.modelPath = modelPath;
	}
}
