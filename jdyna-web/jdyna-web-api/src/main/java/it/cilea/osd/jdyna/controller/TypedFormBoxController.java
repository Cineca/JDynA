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

import it.cilea.osd.jdyna.model.ANestedPropertiesDefinition;
import it.cilea.osd.jdyna.model.AType;
import it.cilea.osd.jdyna.model.ATypeNestedObject;
import it.cilea.osd.jdyna.model.ATypeWithTypeNestedObjectSupport;
import it.cilea.osd.jdyna.model.Containable;
import it.cilea.osd.jdyna.model.IContainable;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.web.IPropertyHolder;
import it.cilea.osd.jdyna.web.Tab;
import it.cilea.osd.jdyna.web.TypedBox;
import it.cilea.osd.jdyna.web.Utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

public class TypedFormBoxController<TP extends PropertiesDefinition, H extends IPropertyHolder<Containable>, T extends Tab<H>, ATTP extends ANestedPropertiesDefinition, TTP extends ATypeNestedObject<ATTP>, A extends AType<TP>>
        extends FormBoxController<TP, H, T, ATTP, TTP>
{

    private Class<A> typoClass;
   
    public TypedFormBoxController(Class<H> clazzH, Class<TP> clazzTP,
            Class<TTP> clazzTTP, Class<A> typoClass)
    {
        super(clazzH, clazzTP, clazzTTP);
        this.typoClass = typoClass;
    }
    
    
    @Override
    protected Map referenceData(HttpServletRequest request) throws Exception
    {

        Map<String, Object> map = new HashMap<String, Object>();
        String paramId = request.getParameter("id");
        String tabId = request.getParameter("tabId");

        List<IContainable> owneredContainables = new LinkedList<IContainable>();

        String shortName = Utils.getAdminSpecificPath(request, null);
        A dot = applicationService.findTypoByShortName(typoClass, shortName);
        
        List<TP> modelJdynaContainables = dot.getMask();
        List<TTP> ttps = ((ATypeWithTypeNestedObjectSupport)dot).getTypeNestedDefinitionMask();
        List<IContainable> containables = new LinkedList<IContainable>();
        for (TP tp : modelJdynaContainables)
        {
            IContainable ic = applicationService.findContainableByDecorable(
                    tp.getDecoratorClass(), tp.getId());
            if (ic != null)
            {
                containables.add(ic);
            }
        }

        for (TTP ttp : ttps)
        {
            IContainable ic = applicationService.findContainableByDecorable(
                    ttp.getDecoratorClass(), ttp.getId());
            if (ic != null)
            {
                containables.add(ic);
            }
        }
        if (paramId != null)
        {
            Integer id = Integer.parseInt(paramId);
            owneredContainables = applicationService
                    .findContainableInPropertyHolder(getBoxClass(), id);
        }

        map.put("tabId", tabId);
        map.put("containablesList", containables);
        map.put("owneredContainables", owneredContainables);
        map.put("specificPartPath", shortName);
        String adminSpecificPath = Utils.getAdminSpecificPath(request, null);
        map.put("metadataWithPolicySingle", applicationService.likeAllPropertiesDefinitionWithPolicySingle(getTpClass(), adminSpecificPath));
        map.put("metadataWithPolicyGroup", applicationService.likeAllPropertiesDefinitionWithPolicyGroup(getTpClass(), adminSpecificPath));
        return map;

    }
    
    @Override
    protected Object formBackingObject(HttpServletRequest request)
            throws Exception
    {       
        String shortName = Utils.getAdminSpecificPath(request, null);
        A dot = applicationService.findTypoByShortName(typoClass, shortName);
        TypedBox<IContainable, A, TP> box = (TypedBox<IContainable, A, TP>) super
                .formBackingObject(request);
        box.setTypeDef(dot);
        return box;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request,
            HttpServletResponse response, Object command, BindException errors)
            throws Exception
    {

        H object = (H) command;
        String tabId = request.getParameter("tabId");
        applicationService.saveOrUpdate(getBoxClass(), object);
        saveMessage(
                request,
                getText("action.box.edited",
                        new Object[] { object.getShortName() },
                        request.getLocale()));
        
        String shortName = Utils.getAdminSpecificPath(request, null);        
        return new ModelAndView(getSuccessView() + "?path=" + shortName + "&id=" + object.getId());
    }
    
}
