package it.cilea.osd.jdyna.service;

import it.cilea.osd.common.event.JPAEvent;
import it.cilea.osd.jdyna.dao.OggettoDaRicalcolareDao;
import it.cilea.osd.jdyna.model.AnagraficaObject;
import it.cilea.osd.jdyna.model.AnagraficaSupport;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.model.Property;
import it.cilea.osd.jdyna.util.OggettoDaRicalcolare;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PersistenceFormulaService extends PersistenceDynaService implements IPersistenceFormulaService {

	
	protected OggettoDaRicalcolareDao oggettoDaRicalcolareDao;
	
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
