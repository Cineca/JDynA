/*
 * JDynA, Dynamic Metadata Management for Java Domain Object
 *
 * Copyright (c) 2008, CILEA and third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by CILEA.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License v3 or any later version, as published 
 * by the Free Software Foundation, Inc. <http://fsf.org/>.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 *
 */
package it.cilea.osd.jdyna.dao;

import it.cilea.osd.common.dao.PaginableObjectDao;

import java.io.Serializable;
import java.util.List;

public interface AnagraficaSupportDao<T, PK extends Serializable> extends PaginableObjectDao<T,PK>	{
		
	public List<T> paginateByTipologiaProprieta(Integer tipologiaId, String sort, boolean inverse, int firstResult,
			int maxResults);

	/**
	 * Metodo di supporto per la paginazione, serve ad includere anche gli
	 * oggetti che non hanno proprietà della tipologia specificata. Restituisce
	 * un estratto della lista di epi object senza proprietà della TP
	 * specificata secondo i parametri di paginazione impostati
	 * 
	 * @param tipologiaId
	 * @param string
	 * @param inverse
	 * @param start
	 * @param maxResults
	 * @return
	 */
	public List<T> paginateEmptyById(Integer tipologiaId, String string,
			boolean inverse, int start, int maxResults);

	/**
	 * Metodo di paginazione/ordinamento in base al nome della tipologia
	 * (ATipologia) di oggetto
	 */
	public List<T> paginateByTipologia(String sort, boolean inverse, int firstResult,
			int maxResults);

	/**
	 * Conta il numero di EPIObject <b>senza</b> proprietà della TP specificata
	 * @param tipologiaId
	 * @return
	 */
	public long countEmptyByTipologiaProprieta(Integer tipologiaId);

	/**
	 * Conta il numero di EPIObject <b>con</b> proprietà della TP specificata
	 * @param tipologiaId
	 * @return
	 */
	public long countNotEmptyByTipologiaProprieta(Integer tipologiaId);
}
