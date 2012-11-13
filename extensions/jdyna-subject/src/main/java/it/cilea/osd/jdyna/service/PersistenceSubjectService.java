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
