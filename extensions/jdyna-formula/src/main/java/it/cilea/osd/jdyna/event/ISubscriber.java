package it.cilea.osd.jdyna.event;


/**
 *	Interfaccia per i sottoscrittori, ogni oggetto che vuole essere un subscriber deve
 *  implementare questa interfaccia.
 *  
 *   @author pascarelli
 */
public  interface ISubscriber<E extends IEvent> {
	/** 
	 * 	Metodo invocato dal gestore di eventi su tutti i subscriber
	 *  
	 *  @param arg0 la classe dell'evento che e' stato scatenato
	 *  @param arg1 l'instanza contenente i dettagli dell'evento occorso
	 *  
	 *  @author pascarelli
	 *  */
	public void receive(Class<E> classeEvento, E evento);
}