package it.cilea.osd.jdyna.web;

import it.cilea.osd.jdyna.dao.TabDao;
import it.cilea.osd.jdyna.service.PersistenceDynaService;

import java.util.Collections;
import java.util.List;

public abstract class TabService extends PersistenceDynaService implements
		ITabService {

	public <H extends IPropertyHolder> List<H> getPropertyHolderOnCreation(
			Class<H> model) {

		log.debug("ricerca delle tipologie di proprieta da richiedere durante la creazione");
		List<H> results = ((TabDao)getDaoByModel(model)).findPropertyHolderOnCreation();
		Collections.sort(results);
		return results;

	}

	/**
	 * {@inheritDoc}
	 */
	public List<IPropertyHolder> findPropertyHolderInTab(Integer tabId) {
		log.debug("Call findPropertyHolderInTab with id " + tabId);

		if (tabId == null) {
			tabId = getList(Tab.class).get(0).getId();
		}
		Tab area = get(Tab.class, tabId);
		TabDao areaDao = (TabDao) getDaoByModel(Tab.class);
		if (area == null) {
			tabId = getList(Tab.class).get(0).getId();
		}
		List<IPropertyHolder> results = areaDao.findPropertyHolder(tabId);
		Collections.sort(results);
		return results;
	}

	/**
	 * {@inheritDoc}
	 */
	public <H extends IPropertyHolder> void deletePropertyHolderInTabs(H holder) {
		TabDao modelDao = (TabDao) getDaoByModel(Tab.class);
		// trovo le aree dove è mascherata la tipologia di proprietà e rompo
		// l'associazione
		List<Tab> aree = modelDao.findTabByHolder(holder);
		for (Tab area : aree) {
			area.getMaschera().remove(holder);
		}
	}

}
