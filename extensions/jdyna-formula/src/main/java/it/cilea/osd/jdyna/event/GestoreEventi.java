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
package it.cilea.osd.jdyna.event;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author bollini
 */
public class GestoreEventi {

	protected Log log = LogFactory.getLog(getClass());

	/** contiene la mappa dei subscriber */
	private Map<Class<? extends IEvent>, Set<ISubscriber>> subscribers = 
		new HashMap<Class<? extends IEvent>, Set<ISubscriber>>();

	/**
	 * Registra un subscriber per una determinata classe di eventi
	 * 
	 * @param subscriber
	 *            sottoscrittore dell'evento
	 * @param eventClass
	 *            classe dell'evento
	 * 
	 */
	public <E extends IEvent> void addSubscriber(ISubscriber<E> subscriber,
			Class<E> eventClass) {
		// recupero l'elenco degli attuali subscribers per la tipologia di
		// evento
		Set<ISubscriber> eventSubscribers = subscribers.get(eventClass);
		if (eventSubscribers == null) {
			eventSubscribers = new HashSet<ISubscriber>();
			subscribers.put(eventClass, eventSubscribers);
		}

		// aggiungo in nuovo abbonato
		eventSubscribers.add(subscriber);
	}

	/**
	 * Annulla la sottoscrizione
	 **/
	public <E extends IEvent> void removeSubscriber(ISubscriber subscriber,
			Class<E> eventClass) {
		// recupero l'elenco degli attuali subscribers per la tipologia di
		// evento
		Set<ISubscriber> eventSubscribers = subscribers.get(eventClass);
		eventSubscribers.remove(subscriber);
	}

	/**
	 * Notifica i subscribers della ricezione di un determinato evento
	 * 
	 * @param eventClass
	 *            la classe dell'evento occorso
	 * @param evento
	 *            i dettagli dell'evento occorso
	 * 
	 **/
	public <E extends IEvent> void notifica(Class<E> eventClass, E evento) {
		// notifica ogni subscriber abbonato a quel tipo di evento
		Set<ISubscriber> eventSubscribers = subscribers.get(eventClass);

		if (eventSubscribers == null)
			return;
		// itera tra gli eventi trovati per notificare i sottoscrittori
		for (ISubscriber subscriber : eventSubscribers) {
			subscriber.receive(eventClass, evento);
		}
	}
}
