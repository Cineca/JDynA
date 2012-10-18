package it.cilea.osd.jdyna.controller;

import it.cilea.osd.jdyna.model.Containable;
import it.cilea.osd.jdyna.web.AbstractEditTab;
import it.cilea.osd.jdyna.web.AbstractTab;
import it.cilea.osd.jdyna.web.IPropertyHolder;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

public class TabController<H extends IPropertyHolder<Containable>, T extends AbstractTab<H>, ET extends AbstractEditTab<H, T>> extends ATabsController<H, T>
{

    private Class<ET> editTabClass;
    
    public TabController(Class<T> tabsClass, Class<ET> editTabsClass)
    {
        super(tabsClass);
        this.editTabClass = editTabsClass;
    }
    
    @Override
    protected ModelAndView handleDelete(HttpServletRequest request) {
        
        String tabId = request.getParameter("id");
        Integer paramIntegerId = Integer.parseInt(tabId);
        getApplicationService().decoupleEditTabByDisplayTab(paramIntegerId,editTabClass);
        return super.handleDelete(request);
        
        
    }

}
