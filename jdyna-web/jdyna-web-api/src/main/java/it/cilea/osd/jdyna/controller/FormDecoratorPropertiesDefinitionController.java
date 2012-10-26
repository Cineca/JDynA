package it.cilea.osd.jdyna.controller;

import it.cilea.osd.common.controller.BaseFormController;
import it.cilea.osd.jdyna.model.ADecoratorPropertiesDefinition;
import it.cilea.osd.jdyna.model.AWidget;
import it.cilea.osd.jdyna.model.Containable;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.web.IPropertyHolder;
import it.cilea.osd.jdyna.web.ITabService;
import it.cilea.osd.jdyna.web.Tab;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

public class FormDecoratorPropertiesDefinitionController<W extends AWidget, TP extends PropertiesDefinition, DTP extends ADecoratorPropertiesDefinition<TP>, H extends IPropertyHolder<Containable>, T extends Tab<H>>
        extends BaseFormController
{

    private ITabService applicationService;

    private Class<TP> targetModel;

    private Class<W> renderingModel;
    
    private Class<H> boxModel;

    private String specificPartPath;

    public FormDecoratorPropertiesDefinitionController(Class<TP> targetModel,
            Class<W> renderingModel, Class<H> boxModel)
    {
        this.targetModel = targetModel;
        this.renderingModel = renderingModel;
        this.boxModel = boxModel;
    }

    @Override
    protected Map referenceData(HttpServletRequest request) throws Exception
    {
        Map<String, Object> map = new HashMap<String, Object>();
        String paramBoxId = request.getParameter("boxId");
        String paramTabId = request.getParameter("tabId");
        map.put("tabId", paramTabId);
        map.put("boxId", paramBoxId);
        map.put("specificPartPath", getSpecificPartPath());
        return map;
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request)
            throws Exception
    {
        DTP decorator = null;
        TP object = null;
        String paramId = request.getParameter("pDId");
        if (paramId == null || paramId.isEmpty())
        {
            object = targetModel.newInstance();
            W widget = renderingModel.newInstance();
            object.setRendering(widget);
            decorator = (DTP) object.getDecoratorClass().newInstance();
            decorator.setReal(object);
        }
        else
        {
            Integer id = Integer.parseInt(paramId);
            object = applicationService.get(targetModel, id);
            decorator = (DTP) applicationService.findContainableByDecorable(
                    object.getDecoratorClass(), id);
        }
        return decorator;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request,
            HttpServletResponse response, Object command, BindException errors)
            throws Exception
    {

        String boxId = request.getParameter("boxId");
        String tabId = request.getParameter("tabId");
        
        DTP object = (DTP) command;
                
        applicationService.saveOrUpdate(object.getDecoratorClass(), object);
                
        if(boxId!=null && !boxId.isEmpty()) {
            H box = getApplicationService().get(boxModel, Integer.parseInt(boxId));
            box.getMask().add(object);
            getApplicationService().saveOrUpdate(boxModel, box);    
        }        
        return new ModelAndView(getSuccessView()+"?id="+boxId+"&tabId="+tabId);
    }

    public void setApplicationService(ITabService applicationService)
    {
        this.applicationService = applicationService;
    }

    public ITabService getApplicationService()
    {
        return applicationService;
    }

    public Class<TP> getTargetModel()
    {
        return targetModel;
    }

    public String getSpecificPartPath()
    {
        return specificPartPath;
    }

    public void setSpecificPartPath(String specificPartPath)
    {
        this.specificPartPath = specificPartPath;
    }

    public Class<W> getRenderingModel()
    {
        return renderingModel;
    }

    public Class<H> getBoxModel()
    {
        return boxModel;
    }
}
