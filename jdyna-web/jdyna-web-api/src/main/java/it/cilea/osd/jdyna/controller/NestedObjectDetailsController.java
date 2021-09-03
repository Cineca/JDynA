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
package it.cilea.osd.jdyna.controller;

import it.cilea.osd.common.controller.BaseAbstractController;
import it.cilea.osd.jdyna.model.ADecoratorTypeDefinition;
import it.cilea.osd.jdyna.model.ANestedObjectWithTypeSupport;
import it.cilea.osd.jdyna.model.ANestedPropertiesDefinition;
import it.cilea.osd.jdyna.model.ANestedProperty;
import it.cilea.osd.jdyna.model.ATypeNestedObject;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.model.Property;
import it.cilea.osd.jdyna.web.ITabService;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;

public class NestedObjectDetailsController<SP extends Property<STP>, STP extends PropertiesDefinition, P extends ANestedProperty<TP>, TP extends ANestedPropertiesDefinition, T extends ANestedObjectWithTypeSupport<P, TP, SP, STP>, AT extends ATypeNestedObject<TP>, DTP extends ADecoratorTypeDefinition<AT, TP>> extends BaseAbstractController 
{
    
    private ITabService applicationService;

    private Class<T> modelClazz;
    private Class<DTP> decoratorClazz;
    
    private String specificPartPath;
    private String specificContextPath;
    
    
    public NestedObjectDetailsController(Class<T> model, Class<DTP> decoratorClazz)
    {
        this.modelClazz = model;
        this.decoratorClazz = decoratorClazz;
    }
    
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ModelAndView retValue = null;                
        if ("delete".equals(method))
            retValue = handleDelete(request);
        else if ("list".equals(method))
            retValue = handleList(request);
        else if ("misc".equals(method))
            retValue = handleMisc(request);        
        else if ("reorder".equals(method))
            retValue = handleReorder(request);
        return retValue;
    }

	private ModelAndView handleList(HttpServletRequest request)
    {
        Map<String, Object> model = new HashMap<String, Object>();   
        String parentStringID = request.getParameter("parentID");
        String typeNestedStringID = request.getParameter("typeNestedID");
        String limitString = request.getParameter("limit");        
        String pageString = request.getParameter("pageCurrent");
        String editmode = request.getParameter("editmode");
        String admin = request.getParameter("admin");
        String externalJSP = request.getParameter("externalJSP");
        Boolean edit = false;
        if(editmode!=null && !editmode.isEmpty()) {
            edit = Boolean.parseBoolean(editmode);            
        }
        
        Integer parentID = Integer.parseInt(parentStringID);
        Integer typeNestedID = Integer.parseInt(typeNestedStringID);        
        Integer limit = Integer.parseInt(limitString);
        Integer page = Integer.parseInt(pageString);
        
        List<T> results = null;
        long countAll = 0;
        if(edit) {
            results = applicationService.getNestedObjectsByParentIDAndTypoIDLimitAt(parentID, typeNestedID, modelClazz, limit, page*limit);
            countAll = applicationService.countNestedObjectsByParentIDAndTypoID(parentID, typeNestedID, modelClazz);
        }
        else {
            results = applicationService.getActiveNestedObjectsByParentIDAndTypoIDLimitAt(parentID, typeNestedID, modelClazz, limit, page*limit);
            countAll = applicationService.countActiveNestedObjectsByParentIDAndTypoID(parentID, typeNestedID, modelClazz);
        }
 
        Collections.sort(results);
        
        model.put("decoratorPropertyDefinition", applicationService.findContainableByDecorable(decoratorClazz, typeNestedID));
        model.put("results", results);           
        model.put("limit", limit);
        model.put("pageCurrent", page);   
        model.put("editmode", edit);
        model.put("parentID", parentID);
        model.put("totalHit", (int)countAll);        
        model.put("hitPageSize", results.size());
        model.put("specificPartPath", specificPartPath);
        model.put("specificContextPath", specificContextPath);
        model.put("admin", admin);
        if(externalJSP!=null && !externalJSP.isEmpty()) {
        	return new ModelAndView(externalJSP, model);
        }
        return new ModelAndView(getDetailsView(), model);
    }

    private ModelAndView handleDelete(HttpServletRequest request)
    {
        Map<String, Object> model = new HashMap<String, Object>();
        String elementID = request.getParameter("elementID");

        if(elementID!=null && !elementID.isEmpty()) {
            String nestedID = elementID.substring(elementID.lastIndexOf("_")+1);            
            applicationService.delete(modelClazz, Integer.parseInt(nestedID));            
        }    
                
        String parentStringID = request.getParameter("parentID");
        String typeNestedStringID = request.getParameter("typeNestedID");
        String admin = request.getParameter("admin");
        String editmode = request.getParameter("editmode");
        Boolean edit = false;
        if(editmode!=null && !editmode.isEmpty()) {
            edit = Boolean.parseBoolean(editmode);            
        }
                
        Integer parentID = Integer.parseInt(parentStringID);
        Integer typeNestedID = Integer.parseInt(typeNestedStringID);        
        Integer limit = 5;
        Integer page = 0;
        
        List<T> results = null;
        long countAll = 0;
        if(edit) {
            results = applicationService.getNestedObjectsByParentIDAndTypoIDLimitAt(parentID, typeNestedID, modelClazz, limit, page*limit);
            countAll = applicationService.countNestedObjectsByParentIDAndTypoID(parentID, typeNestedID, modelClazz);
        }
        else {
            results = applicationService.getActiveNestedObjectsByParentIDAndTypoIDLimitAt(parentID, typeNestedID, modelClazz, limit, page*limit);
            countAll = applicationService.countActiveNestedObjectsByParentIDAndTypoID(parentID, typeNestedID, modelClazz);
        }
               
        Collections.sort(results);
        model.put("decoratorPropertyDefinition", applicationService.findContainableByDecorable(decoratorClazz, typeNestedID));
        model.put("results", results);           
        model.put("limit", limit);
        model.put("pageCurrent", page);   
        model.put("editmode", edit);
        model.put("parentID", parentID);
        model.put("totalHit", (int)countAll);
        model.put("hitPageSize", results.size());
        model.put("specificPartPath", specificPartPath);
        model.put("specificContextPath", specificContextPath);
        model.put("admin", admin);
        return new ModelAndView(getDetailsView(), model);
    }
    
    private ModelAndView handleMisc(HttpServletRequest request) {

        Map<String, Object> model = new HashMap<String, Object>();
        String elementID = request.getParameter("elementID");
        //manage additional operation, now implemented only to on/off preferred option
        if(elementID!=null && !elementID.isEmpty()) {
            String nestedID = elementID.substring(elementID.lastIndexOf("_")+1);            
            T tNested = getApplicationService().get(getModelClazz(), Integer.parseInt(nestedID));
            String preferred = request.getParameter("preferred");
            //manage preferred option
            if(StringUtils.isNotBlank(preferred)) {
                tNested.setPreferred(Boolean.parseBoolean(preferred));
                getApplicationService().saveOrUpdate(getModelClazz(), tNested);
            }
        }    
                
        String parentStringID = request.getParameter("parentID");
        String typeNestedStringID = request.getParameter("typeNestedID");
        String admin = request.getParameter("admin");
        String editmode = request.getParameter("editmode");
        Boolean edit = false;
        if(editmode!=null && !editmode.isEmpty()) {
            edit = Boolean.parseBoolean(editmode);            
        }
                
        Integer parentID = Integer.parseInt(parentStringID);
        Integer typeNestedID = Integer.parseInt(typeNestedStringID);        
        Integer limit = 5;
        Integer page = 0;
        
        List<T> results = null;
        long countAll = 0;
        if(edit) {
            results = getApplicationService().getNestedObjectsByParentIDAndTypoIDLimitAt(parentID, typeNestedID, getModelClazz(), limit, page*limit);
            countAll = getApplicationService().countNestedObjectsByParentIDAndTypoID(parentID, typeNestedID, getModelClazz());
        }
        else {
            results = getApplicationService().getActiveNestedObjectsByParentIDAndTypoIDLimitAt(parentID, typeNestedID, getModelClazz(), limit, page*limit);
            countAll = getApplicationService().countActiveNestedObjectsByParentIDAndTypoID(parentID, typeNestedID, getModelClazz());
        }
               
        Collections.sort(results);
        model.put("decoratorPropertyDefinition", getApplicationService().findContainableByDecorable(getDecoratorClazz(), typeNestedID));
        model.put("results", results);           
        model.put("limit", limit);
        model.put("pageCurrent", page);   
        model.put("editmode", edit);
        model.put("parentID", parentID);
        model.put("totalHit", (int)countAll);
        model.put("hitPageSize", results.size());
        model.put("specificPartPath", getSpecificPartPath());
        model.put("specificContextPath", getSpecificContextPath());
        model.put("admin", admin);
        return new ModelAndView(getDetailsView(), model);
        
    }

    private ModelAndView handleReorder(HttpServletRequest request) {
        Map<String, Object> model = new HashMap<String, Object>();
        String sTargetID = request.getParameter("targetID");
        String sContentID = request.getParameter("contentID");

        if(StringUtils.isNotBlank(sTargetID) && StringUtils.isNotBlank(sContentID)) {
            // switch positiondef between two nesteds
            Integer targetID = Integer.parseInt(StringUtils.substringAfterLast(sTargetID, "_"));
            Integer contentID = Integer.parseInt(StringUtils.substringAfterLast(sContentID, "_"));
            T target = getApplicationService().get(getModelClazz(), targetID);
            T content = getApplicationService().get(getModelClazz(), contentID);
            Integer targetPositionDef = target.getPositionDef();
            target.setPositionDef(content.getPositionDef());
            content.setPositionDef(targetPositionDef);
            getApplicationService().saveOrUpdate(getModelClazz(), target);
            getApplicationService().saveOrUpdate(getModelClazz(), content);
        }

        String parentStringID = request.getParameter("parentID");
        String typeNestedStringID = request.getParameter("typeNestedID");
        String admin = request.getParameter("admin");
        String editmode = request.getParameter("editmode");
        Boolean edit = false;
        if(editmode != null && !editmode.isEmpty()) {
            edit = Boolean.parseBoolean(editmode);
        }

        Integer parentID = Integer.parseInt(parentStringID);
        Integer typeNestedID = Integer.parseInt(typeNestedStringID);
        Integer limit = 5;
        Integer page = 0;

        List<T> results = null;
        long countAll = 0;
        if(edit) {
            results = applicationService.getNestedObjectsByParentIDAndTypoIDLimitAt(parentID, typeNestedID, modelClazz, limit, page*limit);
            countAll = applicationService.countNestedObjectsByParentIDAndTypoID(parentID, typeNestedID, modelClazz);
        }
        else {
            results = applicationService.getActiveNestedObjectsByParentIDAndTypoIDLimitAt(parentID, typeNestedID, modelClazz, limit, page*limit);
            countAll = applicationService.countActiveNestedObjectsByParentIDAndTypoID(parentID, typeNestedID, modelClazz);
        }

        Collections.sort(results);
        model.put("decoratorPropertyDefinition", applicationService.findContainableByDecorable(decoratorClazz, typeNestedID));
        model.put("results", results);
        model.put("limit", limit);
        model.put("pageCurrent", page);
        model.put("editmode", edit);
        model.put("parentID", parentID);
        model.put("totalHit", (int)countAll);
        model.put("hitPageSize", results.size());
        model.put("specificPartPath", specificPartPath);
        model.put("specificContextPath", specificContextPath);
        model.put("admin", admin);
        return new ModelAndView(getDetailsView(), model);
    }

    public void setApplicationService(ITabService applicationService)
    {
        this.applicationService = applicationService;
    }

    public ITabService getApplicationService()
    {
        return applicationService;
    }

    public void setSpecificPartPath(String specificPartPath)
    {
        this.specificPartPath = specificPartPath;
    }

    public String getSpecificPartPath()
    {
        return specificPartPath;
    }

    public void setSpecificContextPath(String specificContextPath)
    {
        this.specificContextPath = specificContextPath;
    }

    public String getSpecificContextPath()
    {
        return specificContextPath;
    }

	public Class<T> getModelClazz() {
		return modelClazz;
	}

	public Class<DTP> getDecoratorClazz() {
		return decoratorClazz;
	}


}
