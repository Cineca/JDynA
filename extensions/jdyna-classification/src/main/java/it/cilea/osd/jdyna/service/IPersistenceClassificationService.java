package it.cilea.osd.jdyna.service;

import it.cilea.osd.jdyna.dao.AlberoClassificatorioDao;
import it.cilea.osd.jdyna.model.AlberoClassificatorio;
import it.cilea.osd.jdyna.model.Classificazione;

import java.util.List;


public interface IPersistenceClassificationService extends IPersistenceDynaService {


	
	/**
	 * Restiuisce l'unico albero identificato dal nome passato come parametro
	 * 
	 * @see AlberoClassificatorioDao#uniqueByNome(String)
	 * @param name - il nome dell'albero
	 * @return
	 */
	public AlberoClassificatorio getAlberoByName(String name);

	/**
	 * 
	 * 
	 * @param alberoPK
	 * @param codice
	 * @param attivo true se si vogliono recuperare solo le classificazioni attive, false per tutte
	 * @return
	 */
	//TODO verificare se è più conveniente utilizzare i metodi di getTop e getSub del singolo albero/classificazione
	public List<Classificazione> getSubClassificazioni(
			Integer alberoPK, String codice, boolean attivo);

	

	/**
	 * Restituisce l'unica classificazione con quel codice in quell'albero
	 * 
	 * @see ClassificazioneDao#uniqueByCodiceInAlbero(String, AlberoClassificatorio)
	 * @param resultAlbero - l'albero su cui ricercare la classificazione
	 * @param resultCodice - il codice della classificazione
	 * @return la classificazione desiderata
	 */
	public Classificazione getClassificazioneByCodice(
			String resultAlbero, String resultCodice);
	
	/**
	 * @see #getClassificazioneByCodice(String, String)
	 * @param albero
	 * @param codice
	 * @return
	 */
	@Deprecated	
	public Classificazione getClassificazioneByCodice(
			AlberoClassificatorio albero, String codice);
}
