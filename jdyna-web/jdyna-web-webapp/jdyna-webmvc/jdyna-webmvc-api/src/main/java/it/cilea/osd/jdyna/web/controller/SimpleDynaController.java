/*
 * JDynA, Dynamic Metadata Management for Java Domain Object
 * 
 *  Copyright (c) 2008, CILEA and third-party contributors as
 *  indicated by the @author tags or express copyright attribution
 *  statements applied by the authors.  All third-party contributions are
 *  distributed under license by CILEA.
 * 
 *  This copyrighted material is made available to anyone wishing to use, modify,
 *  copy, or redistribute it subject to the terms and conditions of the GNU
 *  Lesser General Public License v3 or any later version, as published 
 *  by the Free Software Foundation, Inc. <http://fsf.org/>.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 *  for more details.
 * 
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with this distribution; if not, write to:
 *   Free Software Foundation, Inc.
 *   51 Franklin Street, Fifth Floor
 *   Boston, MA  02110-1301  USA
 */
package it.cilea.osd.jdyna.web.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import it.cilea.osd.common.controller.BaseAbstractController;
import it.cilea.osd.common.util.displaytag.DisplayTagData;
import it.cilea.osd.jdyna.components.IComponent;
import it.cilea.osd.jdyna.model.AType;
import it.cilea.osd.jdyna.model.AnagraficaSupport;
import it.cilea.osd.jdyna.model.Containable;
import it.cilea.osd.jdyna.model.IContainable;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.model.Property;
import it.cilea.osd.jdyna.web.IPropertyHolder;
import it.cilea.osd.jdyna.web.ITabService;
import it.cilea.osd.jdyna.web.Tab;
import it.cilea.osd.jdyna.web.Utils;

public abstract class SimpleDynaController <A extends AnagraficaSupport<P, TP>, P extends Property<TP>, TP extends PropertiesDefinition, H extends IPropertyHolder<Containable>, T extends Tab<H>>
	extends BaseAbstractController {

	private final int PAGE_SIZE = 20;
	
	protected ITabService applicationService;
	
	protected Class<A> objectClass;
	
	protected Class<TP> tpClass;
	
	protected Class<? extends AType<TP>> typeClass;
	
	protected Class<T> tabClass;
	
	protected Class<H> propertyHolderClass;
	
	protected String modelPath;
	
	private String i18nPrefix = "action";

    private Map<String, IComponent> components;

    private String specificPartPath;
    
    public void setComponents(Map<String, IComponent> components)
    {
        this.components = components;
    }

    
	public void setTypeClass(Class<? extends AType<TP>> typeClass) {
		this.typeClass = typeClass;
	}
	
	public SimpleDynaController(Class<A> anagraficaObjectClass) throws InstantiationException, IllegalAccessException {
		objectClass = anagraficaObjectClass;
		tpClass = objectClass.newInstance().getClassPropertiesDefinition();		
	}
	
	public SimpleDynaController(Class<A> anagraficaObjectClass, Class<TP> classTP) throws InstantiationException, IllegalAccessException {
		objectClass = anagraficaObjectClass;
		tpClass = classTP;		
	}

	public SimpleDynaController(Class<A> anagraficaObjectClass, Class<TP> classTP, Class<T> classT, Class<H> classH) throws InstantiationException, IllegalAccessException {
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
			retValue = handleDetails(request, response);		
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

    protected Integer getAnagraficaId(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        String param = request.getParameter("id");
        try {
            return Integer.valueOf(param);
        }
        catch (NumberFormatException e) {
            return null;
        }
    }
	    
	protected ModelAndView handleDetails(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Map<String, Object> model = new HashMap<String, Object>();
		Integer objectId = getAnagraficaId(request, response);
		
		AnagraficaSupport<P,TP> jdynaObject = null;
		if(objectId!=null) {
			jdynaObject = applicationService.get(objectClass, objectId);
		}
		else {
			jdynaObject = objectClass.newInstance();
		}
				
	    // this map contains key-values pairs, key = box shortname and values =
        // collection of metadata
        Map<String, List<IContainable>> mapBoxToContainables = new HashMap<String, List<IContainable>>();
        Map<String, Map<String,IContainable>> mapBoxToMapContainables = new HashMap<String, Map<String,IContainable>>();
        List<IContainable> pDInTab = new LinkedList<IContainable>();
        List<H> propertyHolders = new LinkedList<H>();
        List<H> authorizedPropertyHolders = new LinkedList<H>();
        List<T> tabs = findTabsWithVisibility(request, model, response);
        Integer tabId = getTabId(request);
        boolean showError = false;
        try
        {
            
            T t = null; 
                
            if (tabId == null)
            {                
                if(tabs!=null && !tabs.isEmpty()) {
                    t = tabs.get(0);
                    tabId = t.getId();
                }
            }
            else {
                t = applicationService.get(tabClass,
                        tabId);                
            }

            if (tabId == null)
            {
                Exception ex = new RuntimeException(
                        "No tabs to display contact administrator");
                showError = true;
                showInvalidIDError(request, response, ""+objectId);
                throw ex;
            }

            if (!tabs.contains(t))
            {
                Exception ex = new RuntimeException(
                        "You not have needed authorization level to display this tab");
                showAuthorizeError(request, response, ex, ""+objectId);
                showError = true;
                throw ex;
            }

            // collection of boxs
            propertyHolders = t.getMask();
            for(H hh : propertyHolders) {
                if(authorize(request, response, hh)) {
                    authorizedPropertyHolders.add(hh);
                }
            }

            if(authorizedPropertyHolders.isEmpty()) {
                Exception ex = new RuntimeException(
                        "You have permission to see this tab but all box are hidden, maybe it is a wrong configuration. Contact administrator");
                showAuthorizeError(request, response, ex,
                        "" + objectId);
                showError = true;
                throw ex;
            }
            
            String openbox = extractAnchorId(request);
            // this piece of code get containables object from boxs and put them
            // on map
            for (H box : authorizedPropertyHolders)
            {
                String boxExternalPage = box.getExternalJSP();
                List<IContainable> temp = applicationService
                .<H, T, TP>findContainableInPropertyHolder(propertyHolderClass,
                        box.getId());       
                Map<String, IContainable> tempMap = new HashMap<String, IContainable>();
                
                if (components != null && boxExternalPage!=null && !boxExternalPage.isEmpty())
                {
                    IComponent comp = components.get(box.getShortName());
                    if (comp != null)
                    {
                        comp.setShortName(box.getShortName());
                        comp.evalute(request, response);
                    }
                }

                if (box.getShortName().equals(openbox))
                {
                    if (box.isCollapsed())
                    {
                        box.setCollapsed(false);                        
                    }
                }
                
                for(IContainable tt : temp) {
                    tempMap.put(tt.getShortName(), tt);
                }           

                mapBoxToContainables.put(box.getShortName(), temp);
                mapBoxToMapContainables.put(box.getShortName(), tempMap);
                pDInTab.addAll(temp);
            }
            jdynaObject.inizializza();

        }
        catch (Exception e)
        {
            log.error("Original URL:" + Utils.getOriginalURL(request), e);
            if(!showError) {
                showInternalError(request, response, e, ""+objectId);
            }
            throw new RuntimeException(e);
        }
        
        Collections.sort(propertyHolders);
        Collections.sort(authorizedPropertyHolders);
        model.put("propertiesHolders", authorizedPropertyHolders);
        model.put("propertiesDefinitionsInHolder", mapBoxToContainables);
        model.put("mapPropertiesDefinitionsInHolder", mapBoxToMapContainables);
        model.put("tabList", tabs);
        model.put("tabId", tabId);
        model.put("path", modelPath);
        model.put("anagraficaObject", jdynaObject);
        model.put("addModeType", "display");
        model.put("specificPartPath", getSpecificPartPath());        
		return new ModelAndView(getDetailsView(), model);
	}
	
	protected abstract boolean authorize(HttpServletRequest request, HttpServletResponse response, H box) throws Exception;

    protected abstract String extractAnchorId(HttpServletRequest request);
	protected abstract Integer getRealPersistentIdentifier(String persistentIdentifier);
	
    protected Integer extractEntityId(HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        Integer entityId = -1;
        //try to manage bad request, catch all generic Exception
        try {
            String path = request.getPathInfo().substring(1); // remove first /
            String[] splitted = path.split("/");
            request.setAttribute("authority", splitted[1]);
            entityId = getRealPersistentIdentifier(splitted[1]);
            request.setAttribute("entityID", entityId);
        }
        catch(Exception ex) {
            log.warn(ex.getMessage() + ":"+ Utils.getOriginalURL(request));
            showIntegrityError(request, response, ex, ""+entityId);
            throw new RuntimeException("Entity page not found:"+ Utils.getOriginalURL(request));
        }
        
        if (entityId == -1)
        {
            log.warn("Entity page not found:"+ Utils.getOriginalURL(request));
            showInvalidIDError(request, response, ""+entityId);
            throw new RuntimeException("Entity page not found:"+ Utils.getOriginalURL(request));
        }  
        
        return entityId;
    }

    protected void showIntegrityError(HttpServletRequest request,
            HttpServletResponse response, Exception ex, String objectId)
            throws IOException, ServletException
    {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Indicating the request sent by the client was syntactically incorrect");
    }
    
    protected void showInternalError(HttpServletRequest request,
            HttpServletResponse response, Exception ex, String objectId)
            throws IOException, ServletException
    {
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Indicating an error inside the HTTP server");
    }
    
    protected void showInvalidIDError(HttpServletRequest request,
            HttpServletResponse response, String objectId)
            throws IOException, ServletException
    {
        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Indicating that the requested resource is not available");
    } 
    
    protected abstract void showAuthorizeError(HttpServletRequest request, HttpServletResponse response, Exception ex, String objectId) throws IOException, ServletException;

    protected abstract List<T> findTabsWithVisibility(HttpServletRequest request, Map<String, Object> model, HttpServletResponse response) throws SQLException, Exception;
		
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
				
		return new ModelAndView(getListView(), model);
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
		    String path = Utils.getAdminSpecificPath(request, null);
			log.error("Delete failed " + e);
			saveMessage(request, getText(i18nPrefix + ".notdeleted", request
					.getLocale()));		
			return new ModelAndView(getDetailsView()+"?id="+epiobjectID+"&path="+path, model);
		}
				
		return redirect(a,request);		
	}

	/** Redirect to the details page */
	public ModelAndView redirect(AnagraficaSupport<P, TP> epi,HttpServletRequest request) {
	    String path = Utils.getAdminSpecificPath(request, null);
		return new ModelAndView(getListView()+"?path="+path, null);
	}
	
    protected A extractObject(HttpServletRequest request, HttpServletResponse response) throws Exception
    {

        Integer objectId = extractEntityId(request, response);

        A dyn = null;
        try
        {
            dyn = applicationService.get(objectClass,
                    objectId);
        }
        catch (NumberFormatException e)
        {
        }

        if (dyn == null)
        {
            showInvalidIDError(request, response, ""+objectId);
            throw new RuntimeException(Utils.getOriginalURL(request));
        }
        
        return dyn;
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


    public Map<String, IComponent> getComponents()
    {
        return components;
    }


    public void setSpecificPartPath(String specificPartPath)
    {
        this.specificPartPath = specificPartPath;
    }


    public String getSpecificPartPath()
    {
        return specificPartPath;
    }
}
