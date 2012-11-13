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

import it.cilea.osd.jdyna.dto.DTOAlberoClassificatore;
import it.cilea.osd.jdyna.model.AlberoClassificatorio;
import it.cilea.osd.jdyna.service.ValidatorService.ValidationResult;

import org.springframework.validation.Errors;

public interface IValidatorClassificationService extends IValidatorDynaService {
	
	public Errors validaNomeAlbero(DTOAlberoClassificatore albero, Errors errors);
	
	public ValidationResult validaNomeAlberoInModifica(String albero,Integer idAlbero, Errors errors);
	
	/** Controlla se nell'albero la classificazione ha codice univoco */
	public ValidationResult controllaCodiceClassificazione(AlberoClassificatorio albero,String codice);
	
	public ValidationResult controllaCodiceClassificazioneByIdAlberoIdClassificazioneCodice(Integer alberoId,Integer classificazioneOldId,String newCodice);
	
	public ValidationResult controllaCodiceClassificazioneByIdAlberoECodice(Integer alberoId,String newCodice);
		
}
