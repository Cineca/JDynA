package it.cilea.osd.jdyna.web.controller;

import flexjson.JSONSerializer;
import it.cilea.osd.common.model.Selectable;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.web.ITabService;
import it.cilea.osd.jdyna.widget.WidgetPointer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

public abstract class SearchPointerController<PD extends PropertiesDefinition, T extends Selectable>
        extends ParameterizableViewController
{
    protected final Log logger = LogFactory.getLog(getClass());

    private ITabService applicationService;

    private Class<PD> clazzPD;

    @Override
    public ModelAndView handleRequest(HttpServletRequest request,
            HttpServletResponse response) throws Exception
    {
        Map<String, Object> model = new HashMap<String, Object>();
        String elementID = request.getParameter("elementID");
        String query = request.getParameter("query");

        PD pd = applicationService.get(clazzPD, Integer.parseInt(elementID));
        WidgetPointer widgetPointer = (WidgetPointer) pd.getRendering();
        String filtro = widgetPointer.getFiltro();
        Class target = widgetPointer.getTargetValoreClass();
        List results = getResult(target, filtro, query,
                widgetPointer.getDisplay());

        JSONSerializer serializer = new JSONSerializer();
        serializer.rootName("pointers");
        serializer.exclude("class");
        serializer.deepSerialize(results, response.getWriter());
        response.setContentType("application/json");

        return null;

    }

    protected abstract List<SelectableDTO> getResult(Class<T> target, String filtro,
            String query, String expression);
    

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

    public class SelectableDTO implements Selectable
    {

        private Integer id;

        private String display;

        public SelectableDTO(Integer id, String display)
        {
            this.id = id;
            this.display = display;
        }

        public SelectableDTO(String id, String display)
        {
            this.id = Integer.valueOf(id);
            this.display = display;
        }

        @Override
        public String getIdentifyingValue()
        {
            return String.valueOf(id);
        }

        @Override
        public String getDisplayValue()
        {
            return display;
        }

        public void setId(Integer id)
        {
            this.id = id;
        }

        public Integer getId()
        {
            return id;
        }

        public void setDisplay(String display)
        {
            this.display = display;
        }

        public String getDisplay()
        {
            return display;
        }

    }

}
