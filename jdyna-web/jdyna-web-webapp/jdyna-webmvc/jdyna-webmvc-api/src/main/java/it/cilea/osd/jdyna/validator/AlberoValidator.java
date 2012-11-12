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

import it.cilea.osd.jdyna.dto.DTOAlberoClassificatore;
import it.cilea.osd.jdyna.service.IValidatorClassificationService;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.annotations.Param;
import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.spring.SpringCreator;
import org.springframework.validation.Errors;

@RemoteProxy(creator = SpringCreator.class, creatorParams = @Param(name = "beanName", value = "classificazioneValidator"))
public class AlberoValidator extends JDynaBaseValidator {
	
	protected final Log log = LogFactory.getLog(getClass());
		
    private List<String> messages;

    private IValidatorClassificationService validatorService;
    
    public AlberoValidator(IValidatorClassificationService validatorService) {
		super(validatorService);
		this.validatorService = validatorService;
	}  
    
	public boolean supports(Class clazz) {
		return DTOAlberoClassificatore.class.isAssignableFrom(clazz);
	}
	
	
	public void validate(Object object, Errors errors) {			
		log.debug("AlberoValidator.java-----validate");		
		DTOAlberoClassificatore classificazione = (DTOAlberoClassificatore) object;
					
		validatorService.validaNomeAlbero(classificazione, errors);
		
	}
				 
    
	public List<String> getMessages() {
		if (messages == null) return new ArrayList<String>();
        return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}
	

}
