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

import it.cilea.osd.jdyna.model.Soggettario;
import it.cilea.osd.jdyna.service.IValidatorSubjectService;
import it.cilea.osd.jdyna.service.ValidatorService.ValidationResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

public class SoggettarioValidator extends JDynaBaseValidator {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	private IValidatorSubjectService validatorService;
	
	public SoggettarioValidator(IValidatorSubjectService validatorService) {
		super(validatorService);	
		this.validatorService = validatorService;
	}
	
	public boolean supports(Class clazz) {
		return Soggettario.class.isAssignableFrom(clazz);
	}
	
	public void validate(Object object, Errors errors) {			
		log.debug("SoggettoValidator.java-----validate");		
		Soggettario soggetto = (Soggettario) object;						
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nome",
		"error.message.fallita.campo.obbligatorio");	
		validateVoce(soggetto,errors);
	}
				 

	
	/** Verifica se la voce e' stata inserita ed e' unica sul soggettario.
	 */
	public void validateVoce(Soggettario soggettario,Errors errors)  {
			ValidationResult result = validatorService.controllaNomeSuSoggettario(soggettario, soggettario.getNome());
			if (!result.isSuccess()) {					
				errors.rejectValue("nome",result.getMessage());
			}			
			
	}

}
