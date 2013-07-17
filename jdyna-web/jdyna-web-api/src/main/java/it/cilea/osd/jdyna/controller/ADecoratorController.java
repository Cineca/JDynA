package it.cilea.osd.jdyna.controller;

import it.cilea.osd.common.controller.BaseAbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

public abstract class ADecoratorController extends BaseAbstractController
{

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request,
            HttpServletResponse response) throws Exception
    {      
        
        ModelAndView retValue = null;
        if ("details".equals(method))
            retValue = handleDetails(request);
        else if ("delete".equals(method))
            retValue = handleDelete(request);
        else if ("list".equals(method))
            retValue = handleList(request);
        return retValue;
    }

    protected abstract ModelAndView handleList(HttpServletRequest request);

    protected abstract ModelAndView handleDelete(HttpServletRequest request);

    protected abstract ModelAndView handleDetails(HttpServletRequest request);

}
