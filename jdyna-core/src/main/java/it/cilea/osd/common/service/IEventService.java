package it.cilea.osd.common.service;

import it.cilea.osd.common.event.IEvent;
import it.cilea.osd.common.event.ISubscriber;

public interface IEventService {
	/**
	 * Wrapper per la registrazione dei subscribers al sistema di notifica
	 * @param <E> la classe di eventi a cui il subscriber è interessato
	 * @param subscriber
	 * @param eventClass
	 */
	public <E extends IEvent> void addSubscriber(ISubscriber<E> subscriber,
			Class<E> eventClass);
	
	/**
	 * Wrapper per la rimozione dei subscribers dal sistema di notifica
	 * @param <E> la classe di eventi a cui il subscriber non è più interessato
	 * @param subscriber
	 * @param eventClass
	 */
	public <E extends IEvent> void removeSubscriber(ISubscriber<E> subscriber,
			Class<E> eventClass);
}
