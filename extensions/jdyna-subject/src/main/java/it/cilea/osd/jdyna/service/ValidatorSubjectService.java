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
package it.cilea.osd.jdyna.service;

import it.cilea.osd.jdyna.model.Soggettario;

public class ValidatorSubjectService extends ValidatorService implements IValidatorSubjectService {
	
	private IPersistenceSubjectService applicationService;
	
	public ValidatorSubjectService(IPersistenceDynaService applicationService) {
		super(applicationService);		
	}
		
	/** Controlla se la voce del soggetto e' univoca sul soggettario padre */
	public ValidationResult controllaVoceSuSoggettario(Soggettario soggettario,String voce) {
		if(applicationService.findSoggetto(soggettario.getId(), voce)!=null) {					
			return new ValidationResult(
					"error.message.anagrafica.soggettario.soggetto.esistente",
					false,"Error");		
		}
		return new ValidationResult();
	}
	
	/** Controlla se il nome del soggettario e' univoco nel db*/
	public ValidationResult controllaNomeSuSoggettario(Soggettario soggettario,String nome) {
		Soggettario soggettarioDaNome=applicationService.uniqueSoggettarioByName(nome);
		/* se non ha l'id sto in creazione, se lo trovo col nome e0 duplicato*/
		if(soggettario.getId()==null&&soggettarioDaNome!=null){
			return new ValidationResult(
					"error.message.anagrafica.soggettario.esistente",
					false,"Error");	
		}
		if(soggettario.getId()!=null && soggettarioDaNome!=null){
			if(soggettario.getId().equals(soggettarioDaNome.getId())){
				/* se sono in questo caso hibernate mi dara' poi un'eccezione nel saveOrUpdate faccio l'evict*/
				applicationService.evict(soggettarioDaNome);
			}
			else {
				return new ValidationResult(
						"error.message.anagrafica.soggettario.esistente",
						false,"Error");	
			}
		
		}
		return new ValidationResult();
	
	}
}
