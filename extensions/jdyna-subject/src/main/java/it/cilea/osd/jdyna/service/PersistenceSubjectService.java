package it.cilea.osd.jdyna.service;

import it.cilea.osd.jdyna.dao.SoggettarioDao;
import it.cilea.osd.jdyna.dao.SoggettoDao;
import it.cilea.osd.jdyna.model.Soggettario;
import it.cilea.osd.jdyna.model.Soggetto;

import java.util.List;

public class PersistenceSubjectService extends PersistenceDynaService implements IPersistenceSubjectService {

	
	protected SoggettoDao soggettoDao;	
	protected SoggettarioDao soggettarioDao;

	/** Metodo di inizializzazione dei generics dao utilizzati direttamente in modo tale da non doverli sempre 
	 *  prendere dalla mappa dei dao */
	public void init() {
		super.init();
		soggettoDao = (SoggettoDao) getDaoByModel(Soggetto.class);	
		soggettarioDao = (SoggettarioDao) getDaoByModel(Soggettario.class);
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Soggettario uniqueSoggettarioByName(String name) {
		return soggettarioDao.uniqueByNome(name);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<Soggetto> likeBySoggettario(Integer soggettarioID, String token) {
		return soggettoDao.findLikeBySoggettario(soggettarioID, token + "%");
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Soggetto findSoggetto(Integer sogID, String voceSoggetto) {
		return soggettoDao.uniqueInSoggettario(sogID, voceSoggetto);
	}
}
