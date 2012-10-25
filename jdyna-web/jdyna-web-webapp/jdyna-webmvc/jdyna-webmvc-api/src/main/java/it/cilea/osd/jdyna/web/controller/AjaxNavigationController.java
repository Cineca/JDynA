package it.cilea.osd.jdyna.web.controller;

import it.cilea.osd.common.controller.BaseAbstractController;
import it.cilea.osd.jdyna.components.IComponent;
import it.cilea.osd.jdyna.web.Box;
import it.cilea.osd.jdyna.web.ITabService;
import it.cilea.osd.jdyna.web.Tab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public abstract class AjaxNavigationController<B extends Box, T extends Tab>
        extends BaseAbstractController
{
    private ITabService applicationService;

    private Class<T> tabClazz;

    private String specificPartPath;
    
    private Map<String, IComponent> components;

    public AjaxNavigationController(Class<T> tabClazz)
    {
        this.tabClazz = tabClazz;
    }
    
    public ModelAndView loadNavigation(HttpServletRequest request,
            HttpServletResponse response) throws Exception
    {
        Map<String, Object> model = new HashMap<String, Object>();
        String tabId = request.getParameter("tabId");
        Integer id = Integer.parseInt(request.getParameter("objectId"));

        // retrieve sub-page active links
        Map<String, List<String[]>> navigation = new HashMap<String, List<String[]>>();
        List<String[]> sublinkstoexport = new ArrayList<String[]>();

        T tab = applicationService.get(tabClazz, Integer.parseInt(tabId));
        
        List<String[]> sublinks = new ArrayList<String[]>();
        
        List<B> boxs = tab.getMask();
        for (B box : boxs)
        {

            IComponent comp = null;
            boolean componentsWorked = false;
            if (components != null)
            {
                comp = components.get(box.getShortName());

            }
            if (comp != null)
            {
                List<String[]> compSubLinks = comp.sublinks(request, response);
                sublinks.addAll(compSubLinks);
                sublinkstoexport.addAll(compSubLinks);
                componentsWorked = true;
            }

            if (!box.isUnrelevant() && !isBoxHidden(request, id, box))
            {
                

                if (!componentsWorked)
                {
                    int countBoxPublicMetadata = countBoxPublicMetadata(
                            request, id, box, true);
                    sublinks.add(new String[] {
                            box.getShortName(),
                            box.getTitle()
                                    + (countBoxPublicMetadata == 0 ? "" : " ("
                                            + countBoxPublicMetadata + ")"),
                            box.getShortName() });
                }
            }

        }

        navigation.put(tab.getShortName(), sublinks);

        model.put("navigation", navigation);
        model.put("sublinktoexport", sublinkstoexport);
        model.put("areashortName", tab.getShortName());
        model.put("areaid", tab.getId());
        model.put("areatitle", tab.getTitle());
        model.put("currentOpenedTabId", request.getParameter("currentOpenedTabId"));
        model.put("authority", request.getParameter("authority"));
        model.put("specificPartPath", getSpecificPartPath());
        return new ModelAndView(detailsView, model);

    }

    public abstract int countBoxPublicMetadata(HttpServletRequest request, Integer objectID, B box,
            boolean b);    

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

    public String getSpecificPartPath()
    {
        return specificPartPath;
    }

    public void setSpecificPartPath(String specificPath)
    {
        this.specificPartPath = specificPath;
    }

}
