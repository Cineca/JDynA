package it.cilea.osd.jdyna.controller;

import it.cilea.osd.common.controller.BaseFormController;
import it.cilea.osd.jdyna.model.Containable;
import it.cilea.osd.jdyna.util.TabUtils;
import it.cilea.osd.jdyna.web.AbstractEditTab;
import it.cilea.osd.jdyna.web.AbstractTab;
import it.cilea.osd.jdyna.web.IPropertyHolder;
import it.cilea.osd.jdyna.web.ITabService;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

public abstract class AFormEditTabController<H extends IPropertyHolder<Containable>, T extends AbstractTab<H>, ET extends AbstractEditTab<H, T>>
        extends BaseFormController
{

    /**
     * the applicationService for query the Tab db, injected by Spring IoC
     */
    protected ITabService applicationService;

    protected TabUtils tabUtils;

    protected Class<T> tabClass;

    protected Class<H> boxClass;

    protected Class<ET> tabEditClass;
    
    private String specificPartPath;

    public AFormEditTabController(Class<T> clazzT, Class<H> clazzB, Class<ET> clazzET)
    {
        this.tabClass = clazzT;
        this.boxClass = clazzB;
        this.tabEditClass = clazzET;
    }

    public String getSpecificPartPath()
    {
        return specificPartPath;
    }

    public void setSpecificPartPath(String specificPartPath)
    {
        this.specificPartPath = specificPartPath;
    }

    @Override
    protected Map referenceData(HttpServletRequest request) throws Exception {

        Map<String, Object> map = new HashMap<String, Object>();
        String paramId = request.getParameter("id");
        List<H> owneredContainers = new LinkedList<H>();
        List<H> containers = applicationService.getList(boxClass);
        if (paramId != null) {
            Integer id = Integer.parseInt(paramId);
            owneredContainers = applicationService.findPropertyHolderInTab(
                    tabEditClass, id);
        }
        map.put("boxsList", containers);
        map.put("owneredBoxs", owneredContainers);
        map.put("specificPartPath", getSpecificPartPath());
        return map;
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request)
            throws Exception {
        String paramId = request.getParameter("id");
        ET tab = null;
        if (paramId == null) {
            tab = tabEditClass.newInstance();
        
        } else {
            tab = applicationService.get(tabEditClass, Integer.parseInt(paramId));
        }
        return tab;
    }
    
    
    @Override
    protected ModelAndView onSubmit(HttpServletRequest request,
            HttpServletResponse response, Object command, BindException errors)
            throws Exception {
        ET object = (ET)command;      
        applicationService.saveOrUpdate(tabEditClass, object);
        return new ModelAndView(getSuccessView());
    }
    
    public void setApplicationService(ITabService applicationService) {
        this.applicationService = applicationService;
    }

    public void setTabClass(Class<T> tabClass) {
        this.tabClass = tabClass;
    }

    public void setBoxClass(Class<H> boxClass) {
        this.boxClass = boxClass;
    }

    public TabUtils getTabUtils()
    {
        return tabUtils;
    }

    public void setTabUtils(TabUtils tabUtils)
    {
        this.tabUtils = tabUtils;
    }

}