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
package it.cilea.osd.jdyna.web.controller;

import it.cilea.osd.common.controller.BaseFormController;
import it.cilea.osd.jdyna.dto.AnagraficaObjectAreaDTO;
import it.cilea.osd.jdyna.dto.ValoreDTO;
import it.cilea.osd.jdyna.dto.ValoreDTOPropertyEditor;
import it.cilea.osd.jdyna.editor.FilePropertyEditor;
import it.cilea.osd.jdyna.model.ANestedObjectWithTypeSupport;
import it.cilea.osd.jdyna.model.ANestedPropertiesDefinition;
import it.cilea.osd.jdyna.model.ANestedProperty;
import it.cilea.osd.jdyna.model.AnagraficaObject;
import it.cilea.osd.jdyna.model.Containable;
import it.cilea.osd.jdyna.model.IContainable;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.model.Property;
import it.cilea.osd.jdyna.util.AnagraficaUtils;
import it.cilea.osd.jdyna.web.IPropertyHolder;
import it.cilea.osd.jdyna.web.ITabService;
import it.cilea.osd.jdyna.web.Tab;

import java.beans.PropertyEditor;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

public class FormAnagraficaController<P extends Property<TP>, TP extends PropertiesDefinition, H extends IPropertyHolder<Containable>, T extends Tab<H>, EO extends AnagraficaObject<P, TP>, ANO extends ANestedObjectWithTypeSupport<AP, ATP, P, TP>, AP extends ANestedProperty<ATP>, ATP extends ANestedPropertiesDefinition>
        extends BaseFormController
{
    private ITabService applicationService;

    private Class<TP> clazzTipologiaProprieta;

    private Class<EO> clazzAnagraficaObject;

    private Class<T> clazzTab;

    private Class<H> clazzBox;
    
    private Class<ANO> clazzANO;

    public void setClazzAnagraficaObject(Class<EO> clazzAnagraficaObject)
    {
        this.clazzAnagraficaObject = clazzAnagraficaObject;
    }

    public void setClazzTipologiaProprieta(Class<TP> clazzTipologiaProprieta)
    {
        this.clazzTipologiaProprieta = clazzTipologiaProprieta;
    }

    public void setClazzTab(Class<T> clazzTab)
    {
        this.clazzTab = clazzTab;
    }

    public void setClazzBox(Class<H> clazzBox)
    {
        this.clazzBox = clazzBox;
    }

    public void setApplicationService(ITabService applicationService)
    {
        this.applicationService = applicationService;
    }

    public ITabService getApplicationService()
    {
        return applicationService;
    }

    @Override
    protected ServletRequestDataBinder createBinder(HttpServletRequest request,
            Object command) throws Exception
    {
        ServletRequestDataBinder servletRequestDataBinder = super.createBinder(
                request, command);
        AnagraficaObjectAreaDTO commandDTO = (AnagraficaObjectAreaDTO) command;
        for (String shortName : commandDTO.getAnagraficaProperties().keySet())
        {
            TP tipologiaProprieta = applicationService
                    .findPropertiesDefinitionByShortName(
                            clazzTipologiaProprieta, shortName);
            PropertyEditor propertyEditor = tipologiaProprieta.getRendering()
                    .getPropertyEditor(applicationService);
            if (propertyEditor instanceof FilePropertyEditor)
            {
                ((FilePropertyEditor) propertyEditor)
                        .setExternalAuthority(String.valueOf(commandDTO
                                .getParentId()));
                ((FilePropertyEditor) propertyEditor)
                        .setInternalAuthority(String.valueOf(tipologiaProprieta
                                .getId()));
            }

            String path = "anagraficaProperties[" + shortName + "]";
            servletRequestDataBinder.registerCustomEditor(ValoreDTO.class,
                    path, new ValoreDTOPropertyEditor(propertyEditor));
            // per le checkbox
            servletRequestDataBinder.registerCustomEditor(Object.class, path
                    + ".object", propertyEditor);
            logger.debug("Registrato Wrapper del property editor: "
                    + propertyEditor + " per il path: " + path);
            logger.debug("Registrato property editor: " + propertyEditor
                    + " per il path: " + path + ".object");

        }
        return servletRequestDataBinder;
    }

    @Override
    protected Map referenceData(HttpServletRequest request, Object command,
            Errors errors) throws Exception
    {
        Map<String, Object> map = new HashMap<String, Object>();
        AnagraficaObjectAreaDTO anagraficaObjectDTO = (AnagraficaObjectAreaDTO) command;
        EO epiObject = applicationService.get(clazzAnagraficaObject,
                anagraficaObjectDTO.getObjectId());

        List<H> propertyHolders = applicationService.findPropertyHolderInTab(
                clazzTab, anagraficaObjectDTO.getTabId());

        List<IContainable> tipProprietaInArea = null;

        for (IPropertyHolder<Containable> iph : propertyHolders)
        {
            tipProprietaInArea = applicationService
                    .<H, T, TP> findContainableInPropertyHolder(clazzBox,
                            iph.getId());
        }
        map.put("propertiesHolders", propertyHolders);
        map.put("propertiesDefinitionsInTab", tipProprietaInArea);
        List<T> aree = applicationService.getList(clazzTab);
        map.put("tabList", aree);
        map.put("simpleNameAnagraficaObject",
                clazzAnagraficaObject.getSimpleName());
        return map;
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request)
            throws Exception
    {
        String paramAreaId = request.getParameter("tabId");
        String paramAnagraficaObjectId = request.getParameter("id");
        
        List<T> aree = applicationService.getList(clazzTab);
        Integer areaId;
        if (paramAreaId == null)
        {
            areaId = aree.get(0).getId();
        }
        else
        {
            areaId = Integer.parseInt(paramAreaId);
        }

        Integer anagraficaId = Integer.parseInt(paramAnagraficaObjectId);

        EO epiObject = applicationService.get(clazzAnagraficaObject,
                anagraficaId);
        if (epiObject == null)
        {
            epiObject = clazzAnagraficaObject.newInstance();
        }
        List<H> propertyHolders = applicationService.findPropertyHolderInTab(
                clazzTab, areaId);

        List<IContainable> tipProprietaInArea = new LinkedList<IContainable>();

        for (IPropertyHolder<Containable> iph : propertyHolders)
        {
            tipProprietaInArea = applicationService
                    .<H, T, TP> findContainableInPropertyHolder(clazzBox,
                            iph.getId());
        }
        AnagraficaObjectAreaDTO anagraficaObjectDTO = new AnagraficaObjectAreaDTO();
        anagraficaObjectDTO.setTabId(areaId);
        anagraficaObjectDTO.setObjectId(anagraficaId);

        List<TP> realTPS = new LinkedList<TP>();
        for (IContainable c : tipProprietaInArea)
        {
            realTPS.add((TP) applicationService.findContainableByDecorable(
                    clazzTipologiaProprieta.newInstance().getDecoratorClass(),
                    c.getId()));
        }
        AnagraficaUtils.fillDTO(anagraficaObjectDTO, epiObject, realTPS);
        return anagraficaObjectDTO;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request,
            HttpServletResponse response, Object object, BindException errors)
            throws Exception
    {
        AnagraficaObjectAreaDTO anagraficaObjectDTO = (AnagraficaObjectAreaDTO) object;

        String exitPage = "redirect:details.htm?id="
                + anagraficaObjectDTO.getObjectId() + "&areaId="
                + anagraficaObjectDTO.getTabId();

        if (request.getParameter("cancel") != null)
        {
            return new ModelAndView(exitPage);
        }

        EO myObject = applicationService.get(clazzAnagraficaObject,
                anagraficaObjectDTO.getObjectId());

        List<H> propertyHolders = applicationService.findPropertyHolderInTab(
                clazzTab, anagraficaObjectDTO.getTabId());

        List<IContainable> tipProprietaInArea = new LinkedList<IContainable>();

        for (IPropertyHolder<Containable> iph : propertyHolders)
        {
            tipProprietaInArea = applicationService
                    .<H, T, TP> findContainableInPropertyHolder(clazzBox,
                            iph.getId());
        }

        List<TP> realTPS = new LinkedList<TP>();
        for (IContainable c : tipProprietaInArea)
        {
            realTPS.add((TP) applicationService.findContainableByDecorable(
                    clazzTipologiaProprieta.newInstance().getDecoratorClass(),
                    c.getId()));
        }

        AnagraficaUtils.reverseDTO(anagraficaObjectDTO, myObject, realTPS);

        myObject.pulisciAnagrafica();
        applicationService.saveOrUpdate(clazzAnagraficaObject, myObject);
        T area = applicationService.get(clazzTab,
                anagraficaObjectDTO.getTabId());
        final String areaTitle = area.getTitle();
        saveMessage(
                request,
                getText("action.opera.anagrafica.edited",
                        new Object[] { areaTitle }, request.getLocale()));

        return new ModelAndView(exitPage);
    }

    @Override
    protected void onBind(HttpServletRequest request, Object command,
            BindException errors) throws Exception
    {
        super.onBind(request, command, errors);

    }

    @Override
    protected void onBindAndValidate(HttpServletRequest request,
            Object command, BindException errors) throws Exception
    {
        super.onBindAndValidate(request, command, errors);

        AnagraficaObjectAreaDTO dto = (AnagraficaObjectAreaDTO) command;
        EO epiObject = applicationService.get(clazzAnagraficaObject,
                dto.getObjectId());
        List<H> propertyHolders = applicationService.findPropertyHolderInTab(
                clazzTab, dto.getTabId());

        List<IContainable> tipProprietaInArea = new LinkedList<IContainable>();

        for (IPropertyHolder<Containable> iph : propertyHolders)
        {
            tipProprietaInArea = applicationService
                    .<H, T, TP> findContainableInPropertyHolder(clazzBox,
                            iph.getId());
        }
        List<TP> realTPS = new LinkedList<TP>();
        for (IContainable c : tipProprietaInArea)
        {
            realTPS.add((TP) applicationService.findContainableByDecorable(
                    clazzTipologiaProprieta.newInstance().getDecoratorClass(),
                    c.getId()));
        }
        AnagraficaUtils.reverseDTO(dto, epiObject, realTPS);
        AnagraficaUtils.fillDTO(dto, epiObject, realTPS);
    }

    public Class<TP> getClazzTipologiaProprieta()
    {
        return clazzTipologiaProprieta;
    }

    public Class<EO> getClazzAnagraficaObject()
    {
        return clazzAnagraficaObject;
    }

    public Class<T> getClazzTab()
    {
        return clazzTab;
    }

    public Class<H> getClazzBox()
    {
        return clazzBox;
    }

    public Class<ANO> getClazzANO()
    {
        return clazzANO;
    }

    public void setClazzANO(Class<ANO> clazzANO)
    {
        this.clazzANO = clazzANO;
    }
}
