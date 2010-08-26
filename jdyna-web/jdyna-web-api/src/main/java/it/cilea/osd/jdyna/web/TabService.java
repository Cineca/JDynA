package it.cilea.osd.jdyna.web;

import it.cilea.osd.jdyna.dao.ContainableDao;
import it.cilea.osd.jdyna.dao.PropertiesDefinitionDao;
import it.cilea.osd.jdyna.dao.PropertyHolderDao;
import it.cilea.osd.jdyna.dao.TabDao;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.service.PersistenceDynaService;

import java.util.LinkedList;
import java.util.List;

//TODO
public abstract class TabService<H extends IPropertyHolder<Containable>, T extends Tab<H>> extends PersistenceDynaService implements
		ITabService<H, T> {
	
	/**
	 * {@inheritDoc}
	 */
	public List<H> findPropertyHolderInTab(Class<T> classTab, Integer tabId) {
		log.debug("Call findPropertyHolderInTab with id " + tabId);

		if (tabId == null) {
			tabId = getList(classTab).get(0).getId();
		}
		T area = get(classTab, tabId);
		TabDao<H, T> tabDao = (TabDao<H,T>) getDaoByModel(classTab);
		if (area == null) {
			tabId = getList(classTab).get(0).getId();
		}
		List<H> results = tabDao.findPropertyHolderInTab(tabId);		
		return results;
	}

	/**
	 * {@inheritDoc}
	 */
	public void deletePropertyHolderInTabs(Class<T> classTab, H holder) {
		TabDao<H, T> tabDao = (TabDao<H,T>) getDaoByModel(classTab);
		// trovo le aree dove è mascherata la tipologia di proprietà e rompo
		// l'associazione
		List<T> tabs = tabDao.findTabsByHolder(holder);
		for (T tab : tabs) {
			tab.getMask().remove(holder);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public List<IContainable> findContainableInPropertyHolder(
			Class<H> boxClass, Integer boxID) {
		log.debug("Chiamato findContainable in box con arg: " + boxClass
				+ "  " + boxID);

		if (boxID == null) {
			boxID = getList(boxClass).get(0).getId();
		}
		H area = get(boxClass, boxID);
		PropertyHolderDao<H> boxDao = (PropertyHolderDao<H>) getDaoByModel(boxClass);
		if (area == null) {
			boxID = getList(boxClass).get(0).getId();
		}
		List<IContainable> results = boxDao.findContainableByHolder(boxID);		
		return results;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public <TP extends PropertiesDefinition> List<IContainable> findAllContainables(Class<TP> classTipologiaProprieta) throws InstantiationException, IllegalAccessException {
		List<IContainable> containables = new LinkedList<IContainable>();
		List<TP> listTP = new LinkedList<TP>();
		PropertiesDefinitionDao<TP> modelTipologiaProprietaDao = (PropertiesDefinitionDao<TP>) getDaoByModel(classTipologiaProprieta);
		listTP.addAll(modelTipologiaProprietaDao.findAllTipologieProprietaFirstLevel());
		
		for(TP tp : listTP) {				
			ContainableDao<Containable> dao =  (ContainableDao<Containable>) getDaoByModel(tp.getDecoratorClass());			
			containables.add(dao.uniqueContainableByDecorable(tp.getId()));
		}
		findOtherContainables(containables);
		return containables;
	}
	

	/** Extends this method to add other containables object type */
	protected abstract void findOtherContainables(List<IContainable> containables);
	
	/**
	 * {@inheritDoc}
	 */
	public void deleteContainableInPropertyHolder(Class<H> clazzH, IContainable containable) {
		PropertyHolderDao<H> modelDao = (PropertyHolderDao<H>) getDaoByModel(clazzH);
		List<H> boxs = modelDao.findBoxByContainable(containable);
		for (H box : boxs) {
			box.getMask().remove(containable);
		}
	}
	
	@Override
	public IContainable findContainableByDecorable(Class decoratorClass,
			Integer decorable) {
		ContainableDao modelDao = (ContainableDao) getDaoByModel(decoratorClass);
		IContainable result = modelDao.uniqueContainableByDecorable(decorable);
		return result;
	}

}
