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

import it.cilea.osd.jdyna.dao.AlberoClassificatorioDao;
import it.cilea.osd.jdyna.dao.ClassificazioneDao;
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
	//TODO verificare se e' piu' conveniente utilizzare i metodi di getTop e getSub del singolo albero/classificazione
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
