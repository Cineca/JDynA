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
