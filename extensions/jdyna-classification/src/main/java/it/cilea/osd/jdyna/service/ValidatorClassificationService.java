package it.cilea.osd.jdyna.service;

import it.cilea.osd.jdyna.dto.DTOAlberoClassificatore;
import it.cilea.osd.jdyna.model.AlberoClassificatorio;
import it.cilea.osd.jdyna.model.Classificazione;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

/** Classe di utilita' per la validazione delle singole proprieta', 
 *  offre anche altri metodi per validazioni specifiche 
 *  
 *  @author pascarelli
 *  */
public class ValidatorClassificationService extends ValidatorService implements IValidatorClassificationService {


	private IPersistenceClassificationService applicationService;
	
	public ValidatorClassificationService(IPersistenceClassificationService applicationService) {
		super(applicationService);		
	}

	public Errors validaNomeAlbero(DTOAlberoClassificatore albero, Errors errors) {
		if(applicationService.getAlberoByName(albero.getNome())!=null) {
			errors.rejectValue("nome", "error.albero.nome.esistente");
		}
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nome", "error.albero.nome.non.inserito");
		return errors;
	}
	
	public ValidationResult validaNomeAlberoInModifica(String albero,Integer idAlbero, Errors errors) {
		AlberoClassificatorio alberoClassificatorio=(AlberoClassificatorio)applicationService.getAlberoByName(albero);
		if(alberoClassificatorio==null){
			return new ValidationResult();
		}
		if(alberoClassificatorio.getId().equals(idAlbero)){
			/* se sono in questo caso hibernate mi dara' poi un'eccezione nel saveOrUpdate faccio l'evict*/
		//	applicationService.evict(soggettarioDaNome);
			return new ValidationResult();
		}
		else {
			return new ValidationResult("error.albero.nome.non.inserito",false,"Error");
		}
		
		
	}
	/** Controlla se nell'albero la classificazione ha codice univoco */
	public ValidationResult controllaCodiceClassificazione(AlberoClassificatorio albero,String codice) {
		if(applicationService.getClassificazioneByCodice(albero.getNome(),codice)!=null)
			return new ValidationResult("error.message.fallita.validazione.codice",false,"Error");
		return new ValidationResult();
	}
	
	public ValidationResult controllaCodiceClassificazioneByIdAlberoIdClassificazioneCodice(Integer alberoId,Integer classificazioneOldId,String newCodice) {
		AlberoClassificatorio albero = applicationService.get(AlberoClassificatorio.class, alberoId);
		Classificazione classificazione = applicationService.getClassificazioneByCodice(albero.getNome(),newCodice);
		if(classificazione!=null && !classificazione.getId().equals(classificazioneOldId))
			return new ValidationResult("error.message.fallita.validazione.codice",false,"Error");
		return new ValidationResult();
	}
	
	public ValidationResult controllaCodiceClassificazioneByIdAlberoECodice(Integer alberoId,String newCodice) {
		AlberoClassificatorio albero = applicationService.get(AlberoClassificatorio.class, alberoId);
		Classificazione classificazione = applicationService.getClassificazioneByCodice(albero.getNome(),newCodice);
		if(classificazione!=null)
			return new ValidationResult("error.message.fallita.validazione.codice",false,"Error");
		return new ValidationResult();
	}
}