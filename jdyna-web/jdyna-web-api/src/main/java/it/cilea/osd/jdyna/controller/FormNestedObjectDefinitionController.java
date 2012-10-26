/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 * 
 * http://www.dspace.org/license/
 * 
 * The document has moved 
 * <a href="https://svn.duraspace.org/dspace/licenses/LICENSE_HEADER">here</a>
 */
package it.cilea.osd.jdyna.controller;

import it.cilea.osd.common.controller.BaseFormController;
import it.cilea.osd.jdyna.model.ADecoratorTypeDefinition;
import it.cilea.osd.jdyna.model.ANestedPropertiesDefinition;
import it.cilea.osd.jdyna.model.ATypeNestedObject;
import it.cilea.osd.jdyna.web.ITabService;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

public class FormNestedObjectDefinitionController<PD extends ANestedPropertiesDefinition, TP extends ATypeNestedObject<PD>, ATN extends ADecoratorTypeDefinition<TP, PD>> extends BaseFormController
{

    private final String TYPO_ADDTEXT = "text";
    private final String TYPO_ADDDATE = "date";
    private final String TYPO_ADDLINK = "link";
    private final String TYPO_ADDFILE = "file";
    private final String TYPO_ADDPOINTER = "pointer";

    private String addTextView;
    private String addDateView;
    private String addLinkView;
    private String addFileView;
    private String addPointerView;

    private ITabService applicationService;

    private Class<TP> targetClass;
    private Class<ATN> decoratorClass;

    private String specificPartPath;

    
    @Override
    protected Map referenceData(HttpServletRequest request) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String paramBoxId = request.getParameter("boxId");
        String paramTabId = request.getParameter("tabId");
        
        map.put("specificPartPath", getSpecificPartPath());
        map.put("tabId", paramTabId);
        map.put("boxId", paramBoxId);
        return map;
    }
    
    @Override
    protected Object formBackingObject(HttpServletRequest request)
            throws Exception
    {
        ATN decorator = null;
        TP object = null;
        String paramId = request.getParameter("pDId");
        if (paramId == null || paramId.isEmpty())
        {
            decorator = (ATN) (super
                    .formBackingObject(request));
            object = targetClass.newInstance();
            decorator.setReal(object);
        }
        else
        {
            Integer id = Integer.parseInt(paramId);
            decorator = (ATN) applicationService
                    .findContainableByDecorable(getCommandClass(), id);
        }
        return decorator;
    }

    protected ModelAndView onSubmit(HttpServletRequest request,
            HttpServletResponse response, Object command, BindException errors)
            throws Exception
    {
        String boxId = request.getParameter("boxId");
        String tabId = request.getParameter("tabId");
        ATN object = (ATN) command;
        getApplicationService().saveOrUpdate(decoratorClass,
                object);

        Map<String, String> maprequest = request.getParameterMap();

        if (maprequest.containsKey(TYPO_ADDTEXT))
        {
            return new ModelAndView(addTextView.trim() + "?renderingparent="
                    + object.getId() + "&boxId=" + boxId
                    + "&tabId=" + tabId);
        }
        if (maprequest.containsKey(TYPO_ADDDATE))
        {
            return new ModelAndView(addDateView.trim() + "?renderingparent="
                    + object.getId() + "&boxId=" + boxId
                    + "&tabId=" + tabId);
        }
        if (maprequest.containsKey(TYPO_ADDLINK))
        {
            return new ModelAndView(addLinkView.trim() + "?renderingparent="
                    + object.getId() + "&boxId=" + boxId
                    + "&tabId=" + tabId);
        }
        if (maprequest.containsKey(TYPO_ADDFILE))
        {
            return new ModelAndView(addFileView.trim() + "?renderingparent="
                    + object.getId() + "&boxId=" + boxId
                    + "&tabId=" + tabId);
        }
        if (maprequest.containsKey(TYPO_ADDPOINTER))
        {
            return new ModelAndView(addPointerView.trim() + "?renderingparent="
                    + object.getId() + "&boxId=" + boxId
                    + "&tabId=" + tabId);
        }

        return new ModelAndView(getSuccessView() + "?id=" + boxId + "&tabId="
                + tabId);

    }
   
    public Class getTargetClass()
    {
        return targetClass;
    }

    public void setTargetClass(Class targetClass)
    {
        this.targetClass = targetClass;
    }

    public String getSpecificPartPath()
    {
        return specificPartPath;
    }

    public void setSpecificPartPath(String specificPartPath)
    {
        this.specificPartPath = specificPartPath;
    }

    public String getAddTextView()
    {
        return addTextView;
    }

    public void setAddTextView(String addTextView)
    {
        this.addTextView = addTextView;
    }

    public String getAddDateView()
    {
        return addDateView;
    }

    public void setAddDateView(String addDateView)
    {
        this.addDateView = addDateView;
    }

    public String getAddLinkView()
    {
        return addLinkView;
    }

    public void setAddLinkView(String addLinkView)
    {
        this.addLinkView = addLinkView;
    }

    public String getAddFileView()
    {
        return addFileView;
    }

    public void setAddFileView(String addFileView)
    {
        this.addFileView = addFileView;
    }

    public ITabService getApplicationService()
    {
        return applicationService;
    }

    public void setApplicationService(ITabService applicationService)
    {
        this.applicationService = applicationService;
    }

    public Class<ATN> getDecoratorClass()
    {
        return decoratorClass;
    }

    public void setDecoratorClass(Class<ATN> decoratorClass)
    {
        this.decoratorClass = decoratorClass;
    }

    public String getAddPointerView()
    {
        return addPointerView;
    }

    public void setAddPointerView(String addPointerView)
    {
        this.addPointerView = addPointerView;
    }

 
}
