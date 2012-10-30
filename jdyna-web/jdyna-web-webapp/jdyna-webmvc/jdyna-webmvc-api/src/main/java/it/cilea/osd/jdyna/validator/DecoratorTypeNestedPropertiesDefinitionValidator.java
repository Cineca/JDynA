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
