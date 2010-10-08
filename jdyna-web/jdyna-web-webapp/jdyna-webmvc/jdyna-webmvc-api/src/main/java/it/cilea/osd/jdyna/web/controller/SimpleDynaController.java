package it.cilea.osd.jdyna.web.controller;

import it.cilea.osd.common.controller.BaseAbstractController;
import it.cilea.osd.common.util.displaytag.DisplayTagData;
import it.cilea.osd.jdyna.model.ATipologia;
import it.cilea.osd.jdyna.model.AnagraficaSupport;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.model.Property;
import it.cilea.osd.jdyna.web.Containable;
import it.cilea.osd.jdyna.web.IContainable;
import it.cilea.osd.jdyna.web.IPropertyHolder;
import it.cilea.osd.jdyna.web.ITabService;
import it.cilea.osd.jdyna.web.Tab;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

public abstract class SimpleDynaController <P extends Property<TP>, TP extends PropertiesDefinition, H extends IPropertyHolder<Containable>, T extends Tab<H>>
	extends BaseAbstractController {

	private final int PAGE_SIZE = 20;
	
	protected ITabService applicationService;
	
	protected Class<? extends AnagraficaSupport<P, TP>> objectClass;
	
	protected Class<TP> tpClass;
	
	protected Class<? extends ATipologia<TP>> typeClass;
	
	protected Class<T> tabClass;
	
	protected Class<H> propertyHolderClass;
	
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

	public SimpleDynaController(Class<? extends AnagraficaSupport<P, TP>> anagraficaObjectClass, Class<TP> classTP, Class<T> classT, Class<H> classH) throws InstantiationException, IllegalAccessException {
		objectClass = anagraficaObjectClass;
		tpClass = classTP;		
		tabClass = classT;
		propertyHolderClass = classH;
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

    protected Integer getTabId(HttpServletRequest request)
    {
        String param = request.getParameter("tabId");
        try {
            return Integer.valueOf(param);
        }
        catch (NumberFormatException e) {
            return null;
        }
    }

    protected Integer getAnagraficaId(HttpServletRequest request)
    {
        String param = request.getParameter("anagraficaId");
        try {
            return Integer.valueOf(param);
        }
        catch (NumberFormatException e) {
            return null;
        }
    }
	
	protected ModelAndView handleDetails(HttpServletRequest request) throws SQLException {
		
		Map<String, Object> model = new HashMap<String, Object>();
		Integer objectId = getAnagraficaId(request);
		
		AnagraficaSupport<P,TP> jdynaObject = applicationService.get(objectClass, objectId);
		if(jdynaObject==null){
			throw new RuntimeException("La url non corrisponde a nessun oggetto valido nella piattaforma");
		}
	
		//this map contains key-values pairs, key = box shortname and values = collection of metadata
		Map<String, List<IContainable>> mapBoxToContainables = new HashMap<String, List<IContainable>>();
		Map<String, Map<String,IContainable>> mapBoxToMapContainables = new HashMap<String, Map<String,IContainable>>();
		
		//collection of edit tabs (all edit tabs created on system associate to visibility)
		List<T> tabs = findTabsWithVisibility(request);
		
		Integer tabId = getTabId(request);	

        if (tabId == null && tabs != null && tabs.size() > 0)
        {
            tabId = tabs.get(0).getId();
        }
        
        if (tabId == null)
        {
            throw new RuntimeException(
                    "No tabs to display contact administrator");
        }

        T t = applicationService.get(tabClass, tabId);
        if (!tabs.contains(t))
        {
            throw new RuntimeException(
                    "You not have needed authorization level to display this tab");
        }

        // collection of boxs
        List<H> propertyHolders = applicationService.findPropertyHolderInTab(
                tabClass, tabId);

		//this piece of code get containables object from boxs and put them on map
		List<IContainable> pDInTab = new LinkedList<IContainable>();
		for (H iph : propertyHolders) {
			
				List<IContainable> temp = applicationService
						.findContainableInPropertyHolder(propertyHolderClass,
								iph.getId());
				Map<String, IContainable> tempMap = new HashMap<String, IContainable>();
				for(IContainable tt : temp) {
					tempMap.put(tt.getShortName(), tt);
				}				
				mapBoxToContainables.put(iph.getShortName(), temp);
				mapBoxToMapContainables.put(iph.getShortName(), tempMap);
				pDInTab.addAll(temp);
			
		}
		jdynaObject.inizializza();
		
		
		
		model.put("propertiesHolders", propertyHolders);
		model.put("propertiesDefinitionsInTab", pDInTab);
		model.put("propertiesDefinitionsInHolder", mapBoxToContainables);
		model.put("mapPropertiesDefinitionsInHolder", mapBoxToMapContainables);
		model.put("tabList", tabs);
		model.put("tabId", tabId);  	    
	 	model.put("path", modelPath);     
		model.put("anagraficaObject", jdynaObject);				
		model.put("addModeType", "display");
		
		return new ModelAndView(detailsView, model);
	}

	
	protected abstract List<T> findTabsWithVisibility(HttpServletRequest request) throws SQLException;
		

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
		
		TP tipProp = applicationService.findPropertiesDefinitionByShortName(
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
		String paramId = request.getParameter("anagraficaid");
		Integer epiobjectID = Integer.valueOf(paramId);
		/* uso il delete controllato */
		AnagraficaSupport<P,TP> a= applicationService.get(objectClass,epiobjectID);
		
		try {
			applicationService.delete(objectClass,epiobjectID);
			saveMessage(request, getText(i18nPrefix + ".deleted", request
					.getLocale()));
		}
		catch (Exception e){
			log.error("Non e' stato possibile eliminare l'oggetto " + e);
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

	public ITabService getApplicationService() {
		return applicationService;
	}
}
