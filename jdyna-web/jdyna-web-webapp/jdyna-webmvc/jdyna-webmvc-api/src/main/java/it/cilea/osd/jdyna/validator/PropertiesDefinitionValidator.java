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

import java.util.List;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import it.cilea.osd.jdyna.model.AWidget;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.service.IValidatorDynaService;
import it.cilea.osd.jdyna.service.ValidatorService.ValidationResult;
import it.cilea.osd.jdyna.widget.AWidgetClassificationTree;
import it.cilea.osd.jdyna.widget.WidgetBoolean;
import it.cilea.osd.jdyna.widget.WidgetDate;
import it.cilea.osd.jdyna.widget.WidgetEmail;
import it.cilea.osd.jdyna.widget.WidgetFormula;
import it.cilea.osd.jdyna.widget.WidgetNumero;
import it.cilea.osd.jdyna.widget.WidgetPointer;
import it.cilea.osd.jdyna.widget.WidgetSoggettario;
import it.cilea.osd.jdyna.widget.WidgetTesto;

public class PropertiesDefinitionValidator extends JDynaBaseValidator {
	
   	private List<String> messages;

    public PropertiesDefinitionValidator(IValidatorDynaService validatorService) {
		super(validatorService);
	}
   	
   	public boolean supports(Class clazz) {
		return PropertiesDefinition.class.isAssignableFrom(clazz);
	}
	
	
	public void validate(Object object, Errors errors) {

		PropertiesDefinition metadato = (PropertiesDefinition) object;

		// lo shortname non puo' essere vuoto

		String shortName = metadato.getShortName();

		// validazione shortname...deve essere unico e non nullo e formato solo da caratteri
		// alfabetici da 'a-zA-Z','_' e '-'
		boolean result = (shortName != null) && shortName.matches("^[a-z_\\-A-Z]*$");
				
		if (result && shortName.length()!=0) {

			ValidationResult result2 = null;

			// verifica se e' unica
			// controllo sul db che non ci siano shortname uguali
			result2 = super.getValidatorService().checkShortName(
					object.getClass(), metadato);
			if (!result2.isSuccess())
				errors.rejectValue("shortName", result2.getMessage());

		} else {
			errors.rejectValue("shortName",
					"error.message.fallita.validazione.shortname.pattern");
		}
	}
	
	public void  validateWidget(Object object, Errors errors) {
		PropertiesDefinition metadato = (PropertiesDefinition) object;
		AWidget aWidget = metadato.getRendering();

		if (WidgetPointer.class.isAssignableFrom(aWidget.getClass())) {
			WidgetPointer widgetPointer = (WidgetPointer) aWidget;
			//si dovrebbe calcolare il display..cioe' vedere se il percorso e' coerente
			//e.g. se il display e' ${nome}[0] su un target di tipo Opera..allora 
			//l'applicazione dovrebbe avvertire che il display non e' coerente
//			String clazz = widgetPointer.getTarget();
//			String display = widgetPointer.getDisplay();
//			try {
//				validatorService.verificaTarget(clazz,display);
//			} catch (ClassNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (InstantiationException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IllegalAccessException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}					
		}
		
		if (aWidget instanceof WidgetTesto) {
			WidgetTesto widgetTesto = (WidgetTesto) aWidget;
			
		}
		if (aWidget instanceof WidgetEmail) {
			WidgetEmail widgetEmail = (WidgetEmail) aWidget;

		}
		if (aWidget instanceof AWidgetClassificationTree) {
			AWidgetClassificationTree widgetClassificazione = (AWidgetClassificationTree) aWidget;

		}
		if (aWidget instanceof WidgetSoggettario) {
			WidgetSoggettario widgetSoggettario = (WidgetSoggettario) aWidget;
			//si deve verificare che l'amministratore selezioni almeno un soggettario dalla lista mostrata
			if (widgetSoggettario.getSoggettari()==null) {
				errors.rejectValue("rendering.soggettari","error.message.fallita.validazione.widget.soggettari");
			}
		}
		if (aWidget instanceof WidgetNumero) {
			WidgetNumero widgetNumero = (WidgetNumero) aWidget;
			//bisogna verificare che l'amministratore inserisce esclusivamente numeri
			//e che min < max
			if (widgetNumero.getMin() != null && widgetNumero.getMax() != null)
			{
				if(widgetNumero.getMin()>widgetNumero.getMax()) {
					errors.rejectValue("rendering.min","error.message.fallita.validazione.widget.numero.min");
				}
				if(widgetNumero.getMax()<widgetNumero.getMin()) {
					errors.rejectValue("rendering.max","error.message.fallita.validazione.widget.numero.max");
				}
			}
		}
		if (aWidget instanceof WidgetDate) {
			WidgetDate widgetData = (WidgetDate) aWidget;
			//verificare che anno minimo sia minore di anno massimo
			if(widgetData.getMinYear()>widgetData.getMaxYear()) {
				errors.rejectValue("rendering.min","error.message.fallita.validazione.widget.data.min");
			}
			if(widgetData.getMaxYear()<widgetData.getMinYear()) {
				errors.rejectValue("rendering.max","error.message.fallita.validazione.widget.data.max");
			}
		}		
		if (aWidget instanceof WidgetFormula) {
			WidgetFormula widgetFormula = (WidgetFormula) aWidget;
			//verificare solamente che i campi non siano vuoti o nulli
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "rendering.expression","field.required");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "rendering.regolaDiRicalcolo","field.required");			
		}

		if (aWidget instanceof WidgetBoolean) {
			
		}
		
	}
	

}
