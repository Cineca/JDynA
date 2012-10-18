package it.cilea.osd.jdyna.controller;

import it.cilea.osd.jdyna.model.ADecoratorNestedPropertiesDefinition;
import it.cilea.osd.jdyna.model.ADecoratorTypeDefinition;
import it.cilea.osd.jdyna.model.ANestedPropertiesDefinition;
import it.cilea.osd.jdyna.model.ATypeNestedObject;
import it.cilea.osd.jdyna.model.AWidget;
import it.cilea.osd.jdyna.model.Containable;
import it.cilea.osd.jdyna.web.IPropertyHolder;
import it.cilea.osd.jdyna.web.Tab;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

public class FormAddToNestedDefinitionController<W extends AWidget, TP extends ANestedPropertiesDefinition, DTP extends ADecoratorNestedPropertiesDefinition<TP>, ATN extends ATypeNestedObject<TP>, DTT extends ADecoratorTypeDefinition<ATN, TP>, H extends IPropertyHolder<Containable>, T extends Tab<H>> extends FormDecoratorPropertiesDefinitionController<W, TP, DTP, H, T>
{

    private Class<DTT> typeModel;
    
    public FormAddToNestedDefinitionController(Class<TP> targetModel,
            Class<W> renderingModel, Class<H> boxModel, Class<DTT> typeModel)
    {
        super(targetModel, renderingModel, boxModel);
        this.typeModel = typeModel;
    }
    
    @Override
    protected Map referenceData(HttpServletRequest request) throws Exception {
        
        Map<String, Object> map =  super.referenceData(request);
        map.put("renderingparent", request.getParameter("renderingparent"));
        map.put("boxId", request.getParameter("boxId"));
        map.put("tabId", request.getParameter("tabId"));
        return map;
    }
    
    @Override
    protected Object formBackingObject(HttpServletRequest request)
            throws Exception {
        
        DTT nested = null;
        String rendering = request.getParameter("renderingparent");
        if (rendering != null) {
            nested = getApplicationService().get(typeModel, Integer.parseInt(rendering));         
        }
        DTP object = (DTP)super.formBackingObject(request);     
        nested.getReal().getMask().add(object.getReal());
        return object;
        
    }
    
    
    @Override
    protected ModelAndView onSubmit(HttpServletRequest request,
            HttpServletResponse response, Object command, BindException errors)
            throws Exception {
        String rendering = request.getParameter("renderingparent");
        String boxId = request.getParameter("boxId");
        String tabId = request.getParameter("tabId");
        DTP object = (DTP)command;
        getApplicationService().saveOrUpdate(object.getDecoratorClass(), object);      
        DTT rPd = getApplicationService().get(typeModel, Integer.parseInt(rendering));
        return new ModelAndView(getSuccessView()+"?pDId="+rPd.getReal().getId()+"&boxId="+boxId+"&tabId="+tabId);
    }

}
