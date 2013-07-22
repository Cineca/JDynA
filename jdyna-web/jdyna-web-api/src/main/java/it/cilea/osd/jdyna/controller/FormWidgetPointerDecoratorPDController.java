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

import it.cilea.osd.jdyna.model.ADecoratorPropertiesDefinition;
import it.cilea.osd.jdyna.model.AType;
import it.cilea.osd.jdyna.model.Containable;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.value.PointerValue;
import it.cilea.osd.jdyna.web.IPropertyHolder;
import it.cilea.osd.jdyna.web.Tab;
import it.cilea.osd.jdyna.web.TypedBox;
import it.cilea.osd.jdyna.web.Utils;
import it.cilea.osd.jdyna.widget.WidgetPointer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

public class FormWidgetPointerDecoratorPDController<TP extends PropertiesDefinition, DTP extends ADecoratorPropertiesDefinition<TP>, 
    H extends IPropertyHolder<Containable>, T extends Tab<H>, PV extends PointerValue>
        extends FormDecoratorPropertiesDefinitionController<WidgetPointer, TP, DTP, H, T>
{

    private Class<PV> pValueClass;
    
    private Class<AType<TP>> typoModel;
    
    public FormWidgetPointerDecoratorPDController(Class<TP> targetModel,
            Class<WidgetPointer> renderingModel, Class<H> boxModel, Class<PV> crisModel)
    {
        super(targetModel, renderingModel, boxModel);
        this.pValueClass = crisModel;
    }

    @Override
    protected DTP formBackingObject(HttpServletRequest request)
            throws Exception
    {
        DTP tip = (DTP) super.formBackingObject(request);
        ((WidgetPointer<PV>) tip.getObject().getRendering())
                .setTarget(pValueClass.getCanonicalName());
        return tip;
    }
    
    @Override
    protected ModelAndView onSubmit(HttpServletRequest request,
            HttpServletResponse response, Object command, BindException errors)
            throws Exception
    {

        String boxId = request.getParameter("boxId");
        String tabId = request.getParameter("tabId");

        DTP object = (DTP) command;
        getApplicationService().saveOrUpdate(object.getDecoratorClass(), object);

        if (boxId != null && !boxId.isEmpty())
        {
            H box = getApplicationService().get(getBoxModel(),
                    Integer.parseInt(boxId));
            box.getMask().add(object);
            getApplicationService().saveOrUpdate(getBoxModel(), box);
            if(TypedBox.class.isAssignableFrom(getBoxModel())) {
                TypedBox tbox = (TypedBox)box;
                AType<TP> typo = tbox.getTypeDef();                
                if(!(typo.getMask().contains(object.getReal()))) {
                    typo.getMask().add(object.getReal());
                    getApplicationService().saveOrUpdate(typoModel, typo);
                }
                getApplicationService().saveOrUpdate(typoModel, typo);
            }
        }
        return new ModelAndView(getSuccessView() + "?id=" + boxId + "&tabId="
                + tabId + "&path=" + Utils.getAdminSpecificPath(request, null));
    }

    public Class<AType<TP>> getTypoModel()
    {
        return typoModel;
    }

    public void setTypoModel(Class<AType<TP>> typoModel)
    {
        this.typoModel = typoModel;
    }

}
