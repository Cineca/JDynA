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
