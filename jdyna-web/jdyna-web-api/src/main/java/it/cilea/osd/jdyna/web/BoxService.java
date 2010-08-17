package it.cilea.osd.jdyna.web;

import it.cilea.osd.jdyna.dao.BoxDao;

import java.util.Collections;
import java.util.List;

public abstract class BoxService extends TabService implements IBoxService {
	
	

	public List<IContainable> getContainableOnCreation(Integer boxId) {

		log.debug("ricerca delle tipologie di proprieta da richiedere durante la creazione");
		List<IContainable> results = ((BoxDao)getDaoByModel(Box.class)).findContainableOnCreation(boxId);
		Collections.sort(results);
		return results;

	}
	
	/**
	 * {@inheritDoc}
	 */
	public <H extends IPropertyHolder> List<IContainable> findContainableInBox(
			Class<H> boxClass, Integer boxID) {
		log.debug("Chiamato findTipologieProprietaInArea con arg: " + boxClass
				+ "  " + boxID);

		if (boxID == null) {
			boxID = getList(boxClass).get(0).getId();
		}
		H area = get(boxClass, boxID);
		BoxDao areaDao = (BoxDao) getDaoByModel(boxClass);
		if (area == null) {
			boxID = getList(boxClass).get(0).getId();
		}
		List<IContainable> results = areaDao.findContainable(boxID);
		Collections.sort(results);
		return results;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public <H extends IPropertyHolder> List<IContainable> findTipologieProprietaInAreaWithRenderingFormula(
			Class<H> boxClass, Integer boxID) {
		BoxDao<H> areaDao = (BoxDao<H>) getDaoByModel(boxClass);
		return areaDao.findContainableWithRenderingFormula(boxID);
	}
	

	/**
	 * {@inheritDoc}
	 */
	public <H extends IPropertyHolder> void deleteContainableInBox(IContainable tip) {
		BoxDao<H> modelDao = (BoxDao<H>) getDaoByModel(getPropertyHolderClass());
		// trovo le aree dove è mascherata la tipologia di proprietà e rompo
		// l'associazione
		List<H> aree = modelDao.findBoxByContainable(tip);
		for (H area : aree) {
			area.getMaschera().remove(tip);
		}
	}
	
	
	
}
