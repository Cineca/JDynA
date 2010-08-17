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
