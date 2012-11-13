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
package it.cilea.osd.jdyna.validator;

import it.cilea.osd.jdyna.dto.AnagraficaObjectDTO;
import it.cilea.osd.jdyna.dto.IAnagraficaObjectDTO;
import it.cilea.osd.jdyna.dto.ValoreDTO;
import it.cilea.osd.jdyna.model.AWidget;
import it.cilea.osd.jdyna.model.AnagraficaObject;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.model.Property;
import it.cilea.osd.jdyna.util.ValidationMessage;
import it.cilea.osd.jdyna.web.IPropertyHolder;
import it.cilea.osd.jdyna.web.ITabService;

import java.util.List;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class AnagraficaObjectDTOValidator<P extends Property<TP>, TP extends PropertiesDefinition, H extends IPropertyHolder, EO extends AnagraficaObject<P, TP>>
        implements Validator
{

    protected ITabService applicationService;

    protected Class<TP> clazzTipologiaProprieta;

    protected Class<EO> clazzAnagraficaObject;

    public void setApplicationService(ITabService applicationService)
    {
        this.applicationService = applicationService;
    }

    public boolean supports(Class arg0)
    {
        return AnagraficaObjectDTO.class.isAssignableFrom(arg0);
    }

    public void validate(Object commandObject, Errors errors)
    {
        AnagraficaObjectDTO dto = (AnagraficaObjectDTO) commandObject;
        List<TP> tipologieDaValidare;
        if (dto.getObjectId() != null)
        { // edit
            tipologieDaValidare = applicationService
                    .getList(clazzTipologiaProprieta);
        }
        else
        { // creation
            tipologieDaValidare = applicationService
                    .getTipologiaOnCreation(clazzTipologiaProprieta);
        }
        validate(dto, errors, tipologieDaValidare, "");
    }

    public void validate(IAnagraficaObjectDTO dto, Errors errors,
            List<TP> tipologieDaValidare, String propertyPathPrefix)
    {
        for (TP tipologia : tipologieDaValidare)
        {
            AWidget widget = tipologia.getRendering();

            int sizeDTO = 0;
            int idx = 0;
            if (dto.getAnagraficaProperties().get(tipologia.getShortName()) != null)
            {
                for (ValoreDTO valoreDTO : dto.getAnagraficaProperties().get(
                        tipologia.getShortName()))
                {

                    Object valore = valoreDTO == null ? null : valoreDTO
                            .getObject();
                    if (valore != null)
                    {

                        sizeDTO++;

                        ValidationMessage errorMsg = widget.valida(valore);
                        if (errorMsg != null)
                        {
                            errors.rejectValue(
                                    propertyPathPrefix
                                            + "anagraficaProperties["
                                            + tipologia.getShortName() + "]["
                                            + idx + "]", errorMsg.getI18nKey(),
                                    errorMsg.getParameters(),
                                    tipologia.getLabel());
                        }
                    }

                    idx++;
                }

                if (sizeDTO == 0 && tipologia.isMandatory())
                {
                    errors.rejectValue(
                            propertyPathPrefix + "anagraficaProperties["
                                    + tipologia.getShortName() + "][0]",
                            "field.required", tipologia.getLabel());
                }
            }
        }
    }

    public void setClazzTipologiaProprieta(Class<TP> clazzTipologiaProprieta)
    {
        this.clazzTipologiaProprieta = clazzTipologiaProprieta;
    }

    public void setClazzAnagraficaObject(Class<EO> clazzAnagraficaObject)
    {
        this.clazzAnagraficaObject = clazzAnagraficaObject;
    }
}
