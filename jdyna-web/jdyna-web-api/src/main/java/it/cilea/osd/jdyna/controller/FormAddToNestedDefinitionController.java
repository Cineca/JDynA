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
import it.cilea.osd.jdyna.model.AWidget;
import it.cilea.osd.jdyna.model.Containable;
import it.cilea.osd.jdyna.web.IPropertyHolder;
import it.cilea.osd.jdyna.web.Tab;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

public class FormAddToNestedDefinitionController<W extends AWidget, TP extends ANestedPropertiesDefinition, 
    DTP extends ADecoratorNestedPropertiesDefinition<TP>, ATN extends ATypeNestedObject<TP>, 
    DTT extends ADecoratorTypeDefinition<ATN, TP>, H extends IPropertyHolder<Containable>, T extends Tab<H>>
        extends FormDecoratorPropertiesDefinitionController<W, TP, DTP, H, T>
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
    protected DTP formBackingObject(HttpServletRequest request)
            throws Exception {
        
        DTT nested = null;
        String rendering = request.getParameter("renderingparent");
        if (rendering != null) {
            nested = getApplicationService().get(typeModel, Integer.parseInt(rendering));         
        }
        DTP object = super.formBackingObject(request);        
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
        DTT rPd = getApplicationService().get(typeModel, Integer.parseInt(rendering));
        
        object.getReal().setAccessLevel(rPd.getAccessLevel());
        getApplicationService().saveOrUpdate(object.getDecoratorClass(), object);      
        
        return new ModelAndView(getSuccessView()+"?pDId="+rPd.getReal().getId()+"&boxId="+boxId+"&tabId="+tabId);
    }

    public Class<DTT> getTypeModel()
    {
        return typeModel;
    }

    public void setTypeModel(Class<DTT> typeModel)
    {
        this.typeModel = typeModel;
    }

}
