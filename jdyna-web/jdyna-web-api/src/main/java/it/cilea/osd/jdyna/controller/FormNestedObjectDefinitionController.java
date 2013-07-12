/*
 * JDynA, Dynamic Metadata Management for Java Domain Object
 * 
 *  Copyright (c) 2008, CILEA and third-party contributors as
 *  indicated by the @author tags or express copyright attribution
 *  statements applied by the authors.  All third-party contributions are
 *  distributed under license by CILEA.
 * 
 *  This copyrighted material is made available to anyone wishing to use, modify,
 *  copy, or redistribute it subject to the terms and conditions of the GNU
 *  Lesser General Public License v3 or any later version, as published 
 *  by the Free Software Foundation, Inc. <http://fsf.org/>.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 *  for more details.
 * 
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with this distribution; if not, write to:
 *   Free Software Foundation, Inc.
 *   51 Franklin Street, Fifth Floor
 *   Boston, MA  02110-1301  USA
 */
package it.cilea.osd.jdyna.controller;

import it.cilea.osd.common.controller.BaseFormController;
import it.cilea.osd.jdyna.model.ADecoratorTypeDefinition;
import it.cilea.osd.jdyna.model.ANestedPropertiesDefinition;
import it.cilea.osd.jdyna.model.AType;
import it.cilea.osd.jdyna.model.ATypeNestedObject;
import it.cilea.osd.jdyna.model.ATypeWithTypeNestedObjectSupport;
import it.cilea.osd.jdyna.model.Containable;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.web.IPropertyHolder;
import it.cilea.osd.jdyna.web.ITabService;
import it.cilea.osd.jdyna.web.TypedBox;
import it.cilea.osd.jdyna.web.Utils;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

public class FormNestedObjectDefinitionController<H extends IPropertyHolder<Containable>, PD extends PropertiesDefinition, NPD extends ANestedPropertiesDefinition, TNO extends ATypeNestedObject<NPD>, ATD extends ADecoratorTypeDefinition<TNO, NPD>>
        extends BaseFormController
{

    private final String TYPO_ADDTEXT = "text";

    private final String TYPO_ADDDATE = "date";

    private final String TYPO_ADDLINK = "link";

    private final String TYPO_ADDFILE = "file";

    private final String TYPO_ADDPOINTERRP = "pointerrp";

    private final String TYPO_ADDPOINTEROU = "pointerou";

    private final String TYPO_ADDPOINTERPJ = "pointerpj";

    private String addTextView;

    private String addDateView;

    private String addLinkView;

    private String addFileView;

    private String addPointerRPView;

    private String addPointerOUView;

    private String addPointerPJView;

    private ITabService applicationService;

    private Class<TNO> targetClass;

    private Class<ATD> decoratorClass;

    private Class<H> boxModel;

    private Class<AType<PD>> typoModel;

    private String specificPartPath;

    @Override
    protected Map referenceData(HttpServletRequest request) throws Exception
    {
        Map<String, Object> map = new HashMap<String, Object>();
        String paramBoxId = request.getParameter("boxId");
        String paramTabId = request.getParameter("tabId");

        map.put("specificPartPath", Utils.getAdminSpecificPath(request, null));
        map.put("tabId", paramTabId);
        map.put("boxId", paramBoxId);
        return map;
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request)
            throws Exception
    {
        ATD decorator = null;
        TNO object = null;
        String paramId = request.getParameter("pDId");
        if (paramId == null || paramId.isEmpty())
        {
            decorator = (ATD) (super.formBackingObject(request));
            object = targetClass.newInstance();
            decorator.setReal(object);
        }
        else
        {
            Integer id = Integer.parseInt(paramId);
            decorator = (ATD) applicationService.findContainableByDecorable(
                    getCommandClass(), id);
        }
        return decorator;
    }

    protected ModelAndView onSubmit(HttpServletRequest request,
            HttpServletResponse response, Object command, BindException errors)
            throws Exception
    {
        String boxId = request.getParameter("boxId");
        String tabId = request.getParameter("tabId");
        ATD object = (ATD) command;
        getApplicationService().saveOrUpdate(decoratorClass, object);
        if (boxId != null && !boxId.isEmpty())
        {
            if (boxModel != null)
            {
                H box = getApplicationService().get(boxModel,
                        Integer.parseInt(boxId));
                if(!(box.getMask().contains(object))) {
                    box.getMask().add(object);
                    getApplicationService().saveOrUpdate(boxModel, box);
                }
                if (TypedBox.class.isAssignableFrom(boxModel))
                {
                    TypedBox tbox = (TypedBox) box;
                    ATypeWithTypeNestedObjectSupport<PD, TNO, NPD> typo = (ATypeWithTypeNestedObjectSupport<PD, TNO, NPD>) tbox
                            .getTypeDef();
                    if(!(typo.getTypeNestedDefinitionMask().contains(object.getReal()))) {
                        typo.getTypeNestedDefinitionMask().add(object.getReal());
                        getApplicationService().saveOrUpdate(typoModel, typo);
                    }
                }
            }   

        }
        Map<String, String> maprequest = request.getParameterMap();

        if (maprequest.containsKey(TYPO_ADDTEXT))
        {
            return new ModelAndView(addTextView.trim() + "?renderingparent="
                    + object.getId() + "&boxId=" + boxId + "&tabId=" + tabId
                    + "&path=" + Utils.getAdminSpecificPath(request, null));
        }
        if (maprequest.containsKey(TYPO_ADDDATE))
        {
            return new ModelAndView(addDateView.trim() + "?renderingparent="
                    + object.getId() + "&boxId=" + boxId + "&tabId=" + tabId
                    + "&path=" + Utils.getAdminSpecificPath(request, null));
        }
        if (maprequest.containsKey(TYPO_ADDLINK))
        {
            return new ModelAndView(addLinkView.trim() + "?renderingparent="
                    + object.getId() + "&boxId=" + boxId + "&tabId=" + tabId
                    + "&path=" + Utils.getAdminSpecificPath(request, null));
        }
        if (maprequest.containsKey(TYPO_ADDFILE))
        {
            return new ModelAndView(addFileView.trim() + "?renderingparent="
                    + object.getId() + "&boxId=" + boxId + "&tabId=" + tabId
                    + "&path=" + Utils.getAdminSpecificPath(request, null));
        }
        if (maprequest.containsKey(TYPO_ADDPOINTERRP))
        {
            return new ModelAndView(addPointerRPView.trim()
                    + "?renderingparent=" + object.getId() + "&boxId=" + boxId
                    + "&tabId=" + tabId + "&path="
                    + Utils.getAdminSpecificPath(request, null));
        }
        if (maprequest.containsKey(TYPO_ADDPOINTERPJ))
        {
            return new ModelAndView(addPointerPJView.trim()
                    + "?renderingparent=" + object.getId() + "&boxId=" + boxId
                    + "&tabId=" + tabId + "&path="
                    + Utils.getAdminSpecificPath(request, null));
        }
        if (maprequest.containsKey(TYPO_ADDPOINTEROU))
        {
            return new ModelAndView(addPointerOUView.trim()
                    + "?renderingparent=" + object.getId() + "&boxId=" + boxId
                    + "&tabId=" + tabId + "&path="
                    + Utils.getAdminSpecificPath(request, null));
        }

        return new ModelAndView(getSuccessView() + "?id=" + boxId + "&tabId="
                + tabId + "&path=" + Utils.getAdminSpecificPath(request, null));

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

    public Class<ATD> getDecoratorClass()
    {
        return decoratorClass;
    }

    public void setDecoratorClass(Class<ATD> decoratorClass)
    {
        this.decoratorClass = decoratorClass;
    }

    public String getAddPointerRPView()
    {
        return addPointerRPView;
    }

    public void setAddPointerRPView(String addPointerRPView)
    {
        this.addPointerRPView = addPointerRPView;
    }

    public String getAddPointerOUView()
    {
        return addPointerOUView;
    }

    public void setAddPointerOUView(String addPointerOUView)
    {
        this.addPointerOUView = addPointerOUView;
    }

    public String getAddPointerPJView()
    {
        return addPointerPJView;
    }

    public void setAddPointerPJView(String addPointerPJView)
    {
        this.addPointerPJView = addPointerPJView;
    }

    public Class<H> getBoxModel()
    {
        return boxModel;
    }

    public void setBoxModel(Class<H> boxModel)
    {
        this.boxModel = boxModel;
    }

    public Class<AType<PD>> getTypoModel()
    {
        return typoModel;
    }

    public void setTypoModel(Class<AType<PD>> typoModel)
    {
        this.typoModel = typoModel;
    }

}
