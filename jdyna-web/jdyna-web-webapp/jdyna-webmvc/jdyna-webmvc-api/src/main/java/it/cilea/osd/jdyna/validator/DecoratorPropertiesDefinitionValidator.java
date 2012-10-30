package it.cilea.osd.jdyna.validator;

import it.cilea.osd.jdyna.model.ADecoratorPropertiesDefinition;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.service.IValidatorDynaService;
import it.cilea.osd.jdyna.service.ValidatorService.ValidationResult;

import org.springframework.validation.Errors;

public class DecoratorPropertiesDefinitionValidator extends JDynaBaseValidator
{
    public DecoratorPropertiesDefinitionValidator(IValidatorDynaService validatorService)
    {
        super(validatorService);
    }

    public boolean supports(Class clazz)
    {
        return ADecoratorPropertiesDefinition.class.isAssignableFrom(clazz);
    }

    public void validate(Object object, Errors errors)
    {

        ADecoratorPropertiesDefinition metadato = (ADecoratorPropertiesDefinition) object;

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
            result2 = super.getValidatorService().checkShortName(
                    metadato.getObject().getClass(), (PropertiesDefinition)metadato.getObject());
            if (!result2.isSuccess())
                errors.rejectValue("shortName", result2.getMessage());

        }
        else
        {
            errors.rejectValue("shortName",
                    "error.message.validation.shortname.pattern");
        }
    }
    
}