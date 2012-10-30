package it.cilea.osd.jdyna.web.controller;

import java.util.HashMap;
import java.util.Map;

import it.cilea.osd.jdyna.model.IPropertiesDefinition;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.web.ITabService;
import it.cilea.osd.jdyna.widget.WidgetPointer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

public class SearchPointerController<PD extends PropertiesDefinition> extends ParameterizableViewController
{
    
    private ITabService applicationService;
    private Class<PD> clazzPD;
    
    @Override
    public ModelAndView handleRequest(HttpServletRequest request,
            HttpServletResponse response) throws Exception
    {
        Map<String, Object> model = new HashMap<String, Object>();
        String elementID = request.getParameter("elementID");
        String query = request.getParameter("query");
        String parentID = request.getParameter("parentID");
        
        PD pd = applicationService.findPropertiesDefinitionByShortName(clazzPD, elementID.substring(elementID.indexOf("_")+1));
        WidgetPointer widgetPointer = (WidgetPointer)pd.getRendering();
        String filtro = widgetPointer.getFiltro();
        Class target = widgetPointer.getTargetValoreClass();
        model.put("results", applicationService.getList(target));
        model.put("test", "test");
        return new ModelAndView(getViewName(),model);
        
    }

    public void setApplicationService(ITabService applicationService)
    {
        this.applicationService = applicationService;
    }

    public ITabService getApplicationService()
    {
        return applicationService;
    }

    public Class<PD> getClazzPD()
    {
        return clazzPD;
    }

    public void setClazzPD(Class<PD> clazzPD)
    {
        this.clazzPD = clazzPD;
    }
    
    
}
