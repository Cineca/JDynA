package it.cilea.osd.jdyna.service;

import it.cilea.osd.jdyna.dao.AlberoClassificatorioDao;
import it.cilea.osd.jdyna.dao.ClassificazioneDao;
import it.cilea.osd.jdyna.model.AlberoClassificatorio;
import it.cilea.osd.jdyna.model.Classificazione;

import java.util.List;

public class PersistenceClassificationService extends PersistenceDynaService
		implements IPersistenceClassificationService {

	protected ClassificazioneDao classificazioneDao;

	protected AlberoClassificatorioDao alberoClassificatorioDao;

	/**
	 * Metodo di inizializzazione dei generics dao utilizzati direttamente in
	 * modo tale da non doverli sempre prendere dalla mappa dei dao
	 */
	public void init() {

		super.init();
		classificazioneDao = (ClassificazioneDao) getDaoByModel(Classificazione.class);
		alberoClassificatorioDao = (AlberoClassificatorioDao) getDaoByModel(AlberoClassificatorio.class);

	}


	/**
	 * {@inheritDoc}
	 */
	public Classificazione getClassificazioneByCodice(
			AlberoClassificatorio albero, String codice) {
		return classificazioneDao.uniqueByCodiceInAlbero(codice, albero);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Classificazione getClassificazioneByCodice(String resultAlbero,
			String resultCodice) {
		return classificazioneDao.uniqueByCodiceInAlberoWithName(resultCodice,
				resultAlbero);
	}

	
	/**
	 * {@inheritDoc}
	 */
	public List<Classificazione> getSubClassificazioni(Integer alberoPK,
			String codice, boolean attivo) {
		if (alberoPK == null)
			throw new RuntimeException(
					"Deve essere specificato un albero classificatorio");

		if (attivo) {
			if (codice == null) {
				return classificazioneDao
						.findTopClassificazioniAttive(alberoPK);
			} else {
				return classificazioneDao.findSubClassAttiveByPadreCodice(
						alberoPK, codice);
			}
		} else {
			if (codice == null) {
				return classificazioneDao.findTopClassificazioni(alberoPK);
			} else {
				return classificazioneDao.findSubClassByPadreCodice(alberoPK,
						codice);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public AlberoClassificatorio getAlberoByName(String name) {
		return alberoClassificatorioDao.uniqueByNome(name);
	}

}
