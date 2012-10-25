package it.cilea.osd.jdyna.controller;

import it.cilea.osd.common.controller.BaseAbstractController;
import it.cilea.osd.jdyna.model.ADecoratorTypeDefinition;
import it.cilea.osd.jdyna.model.ANestedObjectWithTypeSupport;
import it.cilea.osd.jdyna.model.ANestedPropertiesDefinition;
import it.cilea.osd.jdyna.model.ANestedProperty;
import it.cilea.osd.jdyna.model.ATypeNestedObject;
import it.cilea.osd.jdyna.web.ITabService;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

public class NestedObjectDetailsController<P extends ANestedProperty<TP>, TP extends ANestedPropertiesDefinition, T extends ANestedObjectWithTypeSupport<P, TP>, AT extends ATypeNestedObject<TP>, DTP extends ADecoratorTypeDefinition<AT, TP>> extends BaseAbstractController 
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
        return retValue;
    }


    private ModelAndView handleList(HttpServletRequest request)
    {
        Map<String, Object> model = new HashMap<String, Object>();   
        String parentStringID = request.getParameter("parentID");
        String typeNestedStringID = request.getParameter("typeNestedID");
        String limitString = request.getParameter("limit");        
        String pageString = request.getParameter("pageCurrent");
        String offsetString = request.getParameter("offset");
        String editmode = request.getParameter("editmode");
        String admin = request.getParameter("admin");
        Boolean edit = false;
        if(editmode!=null && !editmode.isEmpty()) {
            edit = Boolean.parseBoolean(editmode);            
        }
        
        Integer parentID = Integer.parseInt(parentStringID);
        Integer typeNestedID = Integer.parseInt(typeNestedStringID);        
        Integer limit = Integer.parseInt(limitString);
        Integer page = Integer.parseInt(pageString);
        Integer offset = Integer.parseInt(offsetString);
        
        List<T> results = null;
        Long countAll = null;
        if(edit) {
            results = applicationService.getNestedObjectsByParentIDAndTypoIDLimitAt(parentID, typeNestedID, modelClazz, limit, page*limit);
            countAll = applicationService.countNestedObjectsByParentIDAndTypoID(parentID, typeNestedID, modelClazz);
        }
        else {
            results = applicationService.getActiveNestedObjectsByParentIDAndTypoIDLimitAt(parentID, typeNestedID, modelClazz, limit, page*limit);
            countAll = applicationService.countActiveNestedObjectsByParentIDAndTypoID(parentID, typeNestedID, modelClazz);
        }

        
        
        model.put("decoratorPropertyDefinition", applicationService.findContainableByDecorable(decoratorClazz, typeNestedID));
        model.put("results", results);           
        model.put("offset", offset + limit);
        model.put("limit", limit);
        model.put("pageCurrent", page);   
        model.put("editmode", edit);
        model.put("parentID", parentID);
        model.put("totalHit", countAll.intValue());
        model.put("hitPageSize", results.size());
        model.put("specificPartPath", specificPartPath);
        model.put("specificContextPath", specificContextPath);
        model.put("admin", admin);
        return new ModelAndView(detailsView, model);
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
        Integer offset = 0;
        
        
        List<T> results = null;
        Long countAll = null;
        if(edit) {
            results = applicationService.getNestedObjectsByParentIDAndTypoIDLimitAt(parentID, typeNestedID, modelClazz, limit, page*limit);
            countAll = applicationService.countNestedObjectsByParentIDAndTypoID(parentID, typeNestedID, modelClazz);
        }
        else {
            results = applicationService.getActiveNestedObjectsByParentIDAndTypoIDLimitAt(parentID, typeNestedID, modelClazz, limit, page*limit);
            countAll = applicationService.countActiveNestedObjectsByParentIDAndTypoID(parentID, typeNestedID, modelClazz);
        }
        
        
        
        
        model.put("decoratorPropertyDefinition", applicationService.findContainableByDecorable(decoratorClazz, typeNestedID));
        model.put("results", results);           
        model.put("offset", offset + limit);
        model.put("limit", limit);
        model.put("pageCurrent", page);   
        model.put("editmode", edit);
        model.put("parentID", parentID);
        model.put("totalHit", countAll.intValue());
        model.put("hitPageSize", results.size());
        model.put("specificPartPath", specificPartPath);
        model.put("specificContextPath", specificContextPath);
        model.put("admin", admin);
        return new ModelAndView(detailsView, model);
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


}