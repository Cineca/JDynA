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
