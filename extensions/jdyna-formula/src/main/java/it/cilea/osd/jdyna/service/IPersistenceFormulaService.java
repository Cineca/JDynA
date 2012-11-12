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

import it.cilea.osd.jdyna.event.JPAEvent;
import it.cilea.osd.jdyna.model.AnagraficaSupport;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.model.Property;

import java.util.Set;

public interface IPersistenceFormulaService extends IPersistenceDynaService, IEventService {

	/**
	 * Trova gli oggetti da ricalcolare utilizzando le informazioni clazz e id
	 * del JPAEvent. NB: al momento la tipologia di evento non viene utilizzata,
	 * il dato e' stato memorizzato solo per la comodita' di sfruttare la
	 * defininizione del JPAEvent all'interno della definizione di
	 * OggettoDaRicalcolare
	 */
	public <TP extends PropertiesDefinition, P extends Property<TP>> Set<AnagraficaSupport<P, TP>> findOggettoDaRicalcolareByEvento(
			JPAEvent evento);
	
	/** Cancella gli oggetti da ricalcolare 
	 * 
	 * @param id - l'id dell'oggetto
	 * @param canonicalName - il nome della classe
	 * */
	void deleteOggettoDaRicalcolareByOggetto(Integer id, String canonicalName);


}
