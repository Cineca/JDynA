package it.cilea.osd.jdyna.service;

import it.cilea.osd.jdyna.dao.SoggettarioDao;
import it.cilea.osd.jdyna.dao.SoggettoDao;
import it.cilea.osd.jdyna.model.Soggettario;
import it.cilea.osd.jdyna.model.Soggetto;

import java.util.List;

public interface IPersistenceSubjectService extends IPersistenceDynaService {


	/**
	 * Restituisce il soggettario definito dal nome passato come parametro. 
	 *  
	 * @see SoggettarioDao#uniqueByNome(String)
	 * @param name
	 * @return
	 */
	public Soggettario uniqueSoggettarioByName(String name);

    /**
     * Effettua una ricerca in like destro, nel soggettario individuato dalla primary key <code>soggettarioID</code>
     * 
     * @param soggettarioID primary key del soggettario
     * @param token la stringa da utilizzare per il match
     * @return la lista dei soggetti che iniziano per il <code>token</code> dato
     */
    //TODO prevedere un limite nel numero di risultati passabile come argomento
	public List<Soggetto> likeBySoggettario(
			Integer soggettarioID, String token);

	

	 /**
	 * Restituisce l'unico soggetto riferito alla voce passata come parametro
	 * nel soggettario dato dall'id passato come parametro
	 * 
	 * @see SoggettoDao#uniqueInSoggettario(Integer, String)
	 * @param sogID la primary key del soggettario
	 * @param voceSoggetto la voce da controllare
	 * @return il soggetto desiderato
	 */	
	public Soggetto findSoggetto(Integer sogID,
			String voceSoggetto);

}
