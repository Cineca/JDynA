package it.cilea.osd.jdyna.web.controller;

import it.cilea.osd.common.controller.BaseAbstractController;
import it.cilea.osd.jdyna.components.IComponent;
import it.cilea.osd.jdyna.web.Box;
import it.cilea.osd.jdyna.web.ITabService;
import it.cilea.osd.jdyna.web.Tab;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public abstract class AjaxOpenTabController<B extends Box, T extends Tab>
        extends BaseAbstractController
{

    private ITabService applicationService;

    private Class<T> tabClazz;

    private String specificPath;

    private Map<String, IComponent> components;

    public AjaxOpenTabController(Class<T> tabClazz)
    {
        this.tabClazz = tabClazz;
    }
    
    public ModelAndView loadTabs(HttpServletRequest request,
            HttpServletResponse response)
    {
        Map<String, Object> model = new HashMap<String, Object>();
        String tabId = request.getParameter("tabId");
        String objectId = request.getParameter("objectId");

        T tab = applicationService.get(tabClazz, Integer.parseInt(tabId));

        List<B> boxs = tab.getMask();
        boolean visible = false;
        for (B box : boxs)
        {

            if (!box.isUnrelevant() && !isBoxHidden(request, Integer.parseInt(objectId), box))
            {
                visible = true;
                break;
            }

        }
        
                
        model.put("areashortName", tab.getShortName());
        model.put("areaid", tab.getId());
        model.put("areatitle", tab.getTitle());        
        model.put("authority", request.getParameter("authority"));
        model.put("specificPath", getSpecificPath());
        model.put("showtab", visible);
        return new ModelAndView(detailsView, model);

    }

    public abstract boolean isBoxHidden(HttpServletRequest request, Integer objectID, B box);

    public ITabService getApplicationService()
    {
        return applicationService;
    }

    public void setApplicationService(ITabService applicationService)
    {
        this.applicationService = applicationService;
    }

    public Map<String, IComponent> getComponents()
    {
        return components;
    }

    public void setComponents(Map<String, IComponent> components)
    {
        this.components = components;
    }

    public String getSpecificPath()
    {
        return specificPath;
    }

    public void setSpecificPath(String specificPath)
    {
        this.specificPath = specificPath;
    }


}
