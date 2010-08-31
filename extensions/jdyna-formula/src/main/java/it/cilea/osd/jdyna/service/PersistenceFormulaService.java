package it.cilea.osd.jdyna.service;

import it.cilea.osd.jdyna.dao.OggettoDaRicalcolareDao;
import it.cilea.osd.jdyna.event.GestoreEventi;
import it.cilea.osd.jdyna.event.IEvent;
import it.cilea.osd.jdyna.event.ISubscriber;
import it.cilea.osd.jdyna.event.JPAEvent;
import it.cilea.osd.jdyna.model.AnagraficaObject;
import it.cilea.osd.jdyna.model.AnagraficaSupport;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.model.Property;
import it.cilea.osd.jdyna.util.OggettoDaRicalcolare;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PersistenceFormulaService extends PersistenceDynaService implements IPersistenceFormulaService, IEventService {

	
	protected OggettoDaRicalcolareDao oggettoDaRicalcolareDao;
	
    private GestoreEventi gestoreEventi;
    
    private boolean flagGestoreEventi;
    
    protected final Log log = LogFactory.getLog(getClass());
    
	{
		log.debug("inizializzazione classe BASEApplicationService");
	}
	
    public GestoreEventi getGestoreEventi() {
		return gestoreEventi;
	}


	public void setGestoreEventi(GestoreEventi gestoreEventi) {
		this.gestoreEventi = gestoreEventi;		
	}
	
	   /**
	 * {@inheritDoc}
	 */
	public <E extends IEvent> void addSubscriber(ISubscriber<E> subscriber,
			Class<E> eventClass) {
		getGestoreEventi().addSubscriber(subscriber, eventClass);
	}


	/**
	 * {@inheritDoc}
	 */
	public <E extends IEvent> void removeSubscriber(ISubscriber<E> subscriber,
			Class<E> eventClass) {
		getGestoreEventi().removeSubscriber(subscriber, eventClass);
	}
	
	
	/** Metodo di inizializzazione dei generics dao utilizzati direttamente in modo tale da non doverli sempre 
	 *  prendere dalla mappa dei dao */
	public void init() {
	
		oggettoDaRicalcolareDao = (OggettoDaRicalcolareDao) getDaoByModel(OggettoDaRicalcolare.class);
	
	}

	
	/**
	 * {@inheritDoc}
	 */
	public void deleteOggettoDaRicalcolareByOggetto(Integer oggettoId,
			String oggettoClass) {
		oggettoDaRicalcolareDao.deleteOggettoDaRicalcolareByOggetto(oggettoId,
				oggettoClass);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public <TP extends PropertiesDefinition, P extends Property<TP>> Set<AnagraficaSupport<P, TP>> findOggettoDaRicalcolareByEvento(
			JPAEvent evento) {
		// prendo gli oggetti che hanno proprieta da ricalcolare
		List<OggettoDaRicalcolare> oggettiConFormule = oggettoDaRicalcolareDao
				.findOggettoDaRicalcolareByDatiEvento(evento.getClazz(), evento
						.getId());
		log.debug("sono stati trovati " + oggettiConFormule.size()
				+ " oggetti su cui ricalcolare per l'evento " + evento);

		Set<AnagraficaSupport<P, TP>> oggettoConFormule = new HashSet<AnagraficaSupport<P, TP>>();
		for (OggettoDaRicalcolare ogg : oggettiConFormule) {
			try {
				oggettoConFormule.add((AnagraficaObject<P, TP>) get(Class
						.forName(ogg.getOggettoClass()), ogg.getOggettoId()));
			} catch (ClassNotFoundException e) {
				log
						.warn(
								"la classe indicata nell'oggetto da ricalcolare non è stata trovata",
								e);
			} catch (ClassCastException e) {
				log
						.warn(
								"la classe indicata nell'oggetto da ricalcolare non implementa AnagraficaSupport",
								e);
			}
		}

		return oggettoConFormule;
	}


}
