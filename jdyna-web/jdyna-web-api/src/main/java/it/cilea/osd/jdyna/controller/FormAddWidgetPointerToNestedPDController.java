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

import it.cilea.osd.jdyna.model.ADecoratorNestedPropertiesDefinition;
import it.cilea.osd.jdyna.model.ADecoratorTypeDefinition;
import it.cilea.osd.jdyna.model.ANestedPropertiesDefinition;
import it.cilea.osd.jdyna.model.ATypeNestedObject;
import it.cilea.osd.jdyna.model.Containable;
import it.cilea.osd.jdyna.value.PointerValue;
import it.cilea.osd.jdyna.web.IPropertyHolder;
import it.cilea.osd.jdyna.web.Tab;
import it.cilea.osd.jdyna.web.Utils;
import it.cilea.osd.jdyna.widget.WidgetPointer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

public class FormAddWidgetPointerToNestedPDController<TP extends ANestedPropertiesDefinition, DTP extends ADecoratorNestedPropertiesDefinition<TP>, 
    ATN extends ATypeNestedObject<TP>, DTT extends ADecoratorTypeDefinition<ATN, TP>, 
    H extends IPropertyHolder<Containable>, T extends Tab<H>, VPO extends PointerValue>
        extends
        FormAddToNestedDefinitionController<WidgetPointer, TP, DTP, ATN, DTT, H, T>
{

    private Class<VPO> pValueClass;
    
    public FormAddWidgetPointerToNestedPDController(Class<TP> targetModel,
            Class<WidgetPointer> renderingModel, Class<H> boxModel,
            Class<DTT> typeModel, Class<VPO> pValueClass)
    {
        super(targetModel, renderingModel, boxModel, typeModel);
        this.pValueClass = pValueClass;
    }

    @Override
    protected DTP formBackingObject(HttpServletRequest request)
            throws Exception
    {
        DTP tip = (DTP) super.formBackingObject(request);
        ((WidgetPointer<VPO>) tip.getObject().getRendering())
                .setTarget(pValueClass.getCanonicalName());
        return tip;
    }
    
    @Override
    protected ModelAndView onSubmit(HttpServletRequest request,
            HttpServletResponse response, Object command, BindException errors)
            throws Exception {
        String rendering = request.getParameter("renderingparent");
        String boxId = request.getParameter("boxId");
        String tabId = request.getParameter("tabId");
        DTP object = (DTP)command;
        ((WidgetPointer)(object.getReal().getRendering())).setTarget(pValueClass.getCanonicalName());
        DTT rPd = getApplicationService().get(getTypeModel(), Integer.parseInt(rendering));
        
        object.getReal().setAccessLevel(rPd.getAccessLevel());
        getApplicationService().saveOrUpdate(object.getDecoratorClass(), object);      
        
        return new ModelAndView(getSuccessView()+"?pDId="+rPd.getReal().getId()+"&boxId="+boxId+"&tabId="+tabId+ "&path=" + Utils.getAdminSpecificPath(request, null));
    }    
}
