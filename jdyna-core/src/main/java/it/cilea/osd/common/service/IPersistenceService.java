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
package it.cilea.osd.common.service;

import it.cilea.osd.common.dao.impl.ApplicationDao;
import it.cilea.osd.common.model.Identifiable;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;

public interface IPersistenceService {

	public abstract <T, PK extends Serializable> T get(Class<T> modelClass,
			PK pkey);

	public abstract <T extends Identifiable> void saveOrUpdate(
			Class<T> modelClass, T transientObject);

	public abstract <T extends Identifiable> T merge(T oggetto, Class<T> classe);

	public abstract <T extends Identifiable> T refresh(T oggetto,
			Class<T> classe);

	/** Rimuove un oggetto 
	 * 
	 * @param model : la classe dell'oggetto da cancellare
	 * @param pkey : l'id dell'oggetto da cancellare
	 * */
	public abstract <P, PK extends Serializable> void delete(Class<P> model,
			PK pkey);
	
	public abstract <T> long count(Class<T> classe);

	 /**
     * Verifica l'esistenza dell'istanza con id <code>id</code> della classe
     * <code>model</code> del modello 
     *  
     * @param model classe del modello di cui si verifica l'esistenza dell'istanza
     * @param id primary key
     * @return <code>true</code> se esiste un oggetto con l'id fornito
     *
     */
	public abstract <PK extends Serializable> boolean exist(Class model, PK id);
	
	/**
	 * Restituisce la lista degli oggetti che hanno la classe passata come parametro
	 * @param <T>
	 * @param model - la classe degli oggetti richiesti
	 * @return tutti gli oggetti di un certo tipo
	 */
	public abstract <T> List<T> getList(Class<T> model);
	
	/**
	 * Restituisce la lista degli oggetti che hanno la classe passata come parametro
	 * impaginandoli.
	 * 
	 * @param <T> - il tipo generico degli oggetti richiesti
	 * @param model - la classe degli oggetti richiesti
	 * @param sort
	 * @param inverse
	 * @param page
	 * @param maxResults 
	 * @return
	 */
	public abstract <T> List<T> getPaginateList(Class<T> model, String sort,
			boolean inverse, int page, int maxResults);
	
	/**
	 * Elimina l'oggetto dalla sessione di hibernate
	 * 
	 * @see ApplicationDao#evict(Identifiable)
	 * @see Session#evict(Object)
	 * @param identifiable
	 */
	public void evict(Identifiable identifiable);
	
	
}