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
