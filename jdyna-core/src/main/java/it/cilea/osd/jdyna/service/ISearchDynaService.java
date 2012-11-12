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

import it.cilea.osd.jdyna.dto.DTOAutocomplete;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.queryParser.ParseException;

public interface ISearchDynaService {
	
	/**
	 * Ricerca per autocompletamento usato nelle jsp con ajaxtag.
	 * 
	 * @return lista di oggetti {@link DTOAutocomplete} che rappresentano il dto
	 *         per gli oggetti esposti nella tendina.
	 **/
	public List<DTOAutocomplete> searchWithReturnDtoAutocomplete(String filtro, String query, Class model,String display) throws ParseException, IOException;
	
	
	
	/**
	 * 
	 * 
	 * Permette di effettuare una ricerca libera su una particolare classe
	 * 
	 * 
	 * @param <T>
	 * 			  classe degli oggetti da ricercare
	 * @param field 
	 * 			  indice di ricerca da utilizzare come default (se non
	 *            specificato nella query)
	 * @param query
	 *            di ricerca secondo la sintassi di lucene
	 * @param model
	 *            classe degli oggetti da ricercare
	 * @return
	 * @throws ParseException
	 */
	public <T> List<T> search(String field, String query, Class<T> model) throws ParseException;
	
	
	/**
	 * Permette di effettuare una ricerca libera su una particolare classe di
	 * oggetti inpaginando i risultati secondo un particolare criterio di
	 * ordinamento.
	 * 
	 * @param <T>
	 *            classe degli oggetti da ricercare
	 * @param field
	 *            indice di ricerca da utilizzare come default (se non
	 *            specificato nella query)
	 * @param query
	 *            di ricerca secondo la sintassi di lucene
	 * @param model
	 *            classe degli oggetti da ricercare
	 * @param sort
	 *            l'indice da utilizzare come criterio di ordinamento
	 * @param page
	 *            il numero di pagina da visualizzare (l'offset di paginazione e'
	 *            dato da [page-1] * maxResults)
	 * @param maxResults
	 *            il numero massimo di risultati
	 * @return
	 * @throws ParseException
	 */
	public <T> List<T> search(String field, String query, Class<T> model, String sort, String dir, int page, int maxResults) throws ParseException;
	
	
	public <T> List searchWithProjection(String field, String query, Class<T> model) throws ParseException;
	
	/**
	 * Permette di effettuare una ricerca libera su una particolare classe di
	 * oggetti.
	 * 
	 * @param <T>
	 *            classe degli oggetti da ricercare
	 * @param field
	 *            indice di ricerca da utilizzare come default (se non
	 *            specificato nella query)
	 * @param query
	 *            di ricerca secondo la sintassi di lucene
	 * @param model
	 *            classe degli oggetti da ricercare
	 * @param maxResults
	 *            numero massimo di risultati da considerare (-1 per illimitati)
	 * @return
	 * @throws ParseException
	 */
	public <T> List searchWrapperWithProjection(String field, String query, Class<T> model, int maxResults) throws ParseException;
	
	/**
	 * Permette di effettuare una ricerca per prefix (con eventuale filtro) su una particolare classe di
	 * oggetti.
	 * Ad esempio: 
	 * <ul> 
	 * <li>prefix = wil</li>
	 * <li>filtro = tipologie.nome:cliente</li>
	 * <li>model = ContattoFisico.class</li>
	 * </ul>
	 * restituira' tutti i contatti fisici della tipologia "cliente" e con un termine indicizzato che inizi per wil.
	 * Equivale alla query lucene: wil* +tipologie.nome:cliente <br>
	 * <b>NB</b> il numero di risultati e' limitato a 10
	 * 
	 * @param <T>
	 *            classe degli oggetti da ricercare
	 * @param field
	 *            indice di ricerca da utilizzare come default (se null o vuoto viene utilizzato l'indice denominato default)
	 * @param filtro
	 * 			query in sintassi lucene per filtrare i risultati           
	 * @param prefix
	 *            prefisso da ricercare
	 * @param model
	 *            classe degli oggetti da ricercare
	 * @return
	 * @throws ParseException
	 * @throws IOException 
	 */
	public <T> List<T> searchWithFiltro(String field, String filtro, String prefix, Class<T> model) throws ParseException, IOException;

	/**
	 * Determina il numero di risultati che si avranno per la query
	 * 
	 * @param <T>
	 *            classe degli oggetti da ricercare
	 * @param field
	 *            indice di ricerca da utilizzare come default (se non
	 *            specificato nella query)
	 * @param query
	 *            di ricerca secondo la sintassi di lucene
	 * @param model
	 *            classe degli oggetti da ricercare
	 * @return il numero di risultati
	 * @throws ParseException
	 */
	public <T> int count(String field, String query, Class<T> model) throws ParseException;
	
	/**
	 * Delete and create index (FIXME this method must be override in your project)
	 * */
	public void purgeAndCreateIndex();
}
