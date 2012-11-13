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

import it.cilea.osd.jdyna.model.ADecoratorTypeDefinition;
import it.cilea.osd.jdyna.model.ANestedPropertiesDefinition;
import it.cilea.osd.jdyna.model.ATypeNestedObject;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.service.IValidatorDynaService;
import it.cilea.osd.jdyna.service.ValidatorService.ValidationResult;

import org.springframework.validation.Errors;

public class DecoratorTypeNestedPropertiesDefinitionValidator extends
        JDynaBaseValidator
{

    public DecoratorTypeNestedPropertiesDefinitionValidator(
            IValidatorDynaService validatorService)
    {
        super(validatorService);
    }

    @Override
    public boolean supports(Class clazz)
    {
        return ADecoratorTypeDefinition.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors)
    {
        ADecoratorTypeDefinition metadato = (ADecoratorTypeDefinition) target;

        // lo shortname non puo' essere vuoto

        String shortName = metadato.getShortName();

        // validazione shortname...deve essere unico e non nullo e formato solo
        // da caratteri
        // alfabetici da 'a-zA-Z','_' e '-'
        boolean result = (shortName != null)
                && shortName.matches("^[a-z_\\-A-Z]*$");

        if (result && shortName.length() != 0)
        {

            ValidationResult result2 = null;

            // verifica se e' unica
            // controllo sul db che non ci siano shortname uguali
            ATypeNestedObject<ANestedPropertiesDefinition> object = (ATypeNestedObject) metadato
                    .getObject();
            result2 = getValidatorService().checkShortName(object.getClass(),
                    object);
            if (!result2.isSuccess())
            {
                errors.rejectValue("shortName", result2.getMessage());
            }

            if (object.getMask() != null && !object.getMask().isEmpty())
            {
                int i = 0;
                for (ANestedPropertiesDefinition anpd : object.getMask())
                {
                    shortName = anpd.getShortName();

                    // validazione shortname...deve essere unico e non nullo e
                    // formato solo da caratteri
                    // alfabetici da 'a-zA-Z','_' e '-'
                    result = (shortName != null)
                            && shortName.matches("^[a-z_\\-A-Z]*$");
                    if (result && shortName.length() != 0)
                    {
                        result2 = getValidatorService().checkShortName(
                                anpd.getClass(), (PropertiesDefinition) anpd);
                        if (!result2.isSuccess())
                        {
                            errors.rejectValue(
                                    "real.mask[" + i + "].shortName",
                                    result2.getMessage());
                        }
                    }
                    else {
                        errors.rejectValue(
                                "real.mask[" + i + "].shortName",
                                "error.message.validation.shortname.pattern");
                    }
                    i++;
                }
            }

        }
        else
        {
            errors.rejectValue("shortName",
                    "error.message.validation.shortname.pattern");
        }

    }

}
