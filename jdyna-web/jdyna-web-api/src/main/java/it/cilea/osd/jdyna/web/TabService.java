package it.cilea.osd.jdyna.web;

import it.cilea.osd.jdyna.service.PersistenceDynaService;

//TODO
public abstract class TabService extends PersistenceDynaService implements
		ITabService {
	
//	
//
//	public <H extends IPropertyHolder> List<H> getPropertyHolderOnCreation(
//			Class<H> model) {
//
//		log.debug("ricerca delle tipologie di proprieta da richiedere durante la creazione");
//		List<H> results = ((TabDao)getDaoByModel(model)).findPropertyHolderOnCreation();		
//		return results;
//
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	public List<IPropertyHolder> findPropertyHolderInTab(Integer tabId) {
//		log.debug("Call findPropertyHolderInTab with id " + tabId);
//
//		if (tabId == null) {
//			tabId = getList(Tab.class).get(0).getId();
//		}
//		Tab area = get(Tab.class, tabId);
//		TabDao areaDao = (TabDao) getDaoByModel(Tab.class);
//		if (area == null) {
//			tabId = getList(Tab.class).get(0).getId();
//		}
//		List<IPropertyHolder> results = areaDao.findPropertyHolder(tabId);		
//		return results;
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	public <H extends IPropertyHolder> void deletePropertyHolderInTabs(H holder) {
//		TabDao modelDao = (TabDao) getDaoByModel(Tab.class);
//		// trovo le aree dove è mascherata la tipologia di proprietà e rompo
//		// l'associazione
//		List<Tab> aree = modelDao.findTabByHolder(holder);
//		for (Tab area : aree) {
//			area.getMask().remove(holder);
//		}
//	}
//
//	
//	public List<IContainable> getContainableOnCreation(Integer boxId) {
//
//		log.debug("ricerca delle tipologie di proprieta da richiedere durante la creazione");
//		List<IContainable> results = ((PropertyHolderDao)getDaoByModel(Box.class)).findContainableOnCreation(boxId);
//		Collections.sort(results);
//		return results;
//
//	}
//	
//	/**
//	 * {@inheritDoc}
//	 */
//	public <H extends IPropertyHolder> List<IContainable> findContainableInBox(
//			Class<H> boxClass, Integer boxID) {
//		log.debug("Chiamato findTipologieProprietaInArea con arg: " + boxClass
//				+ "  " + boxID);
//
//		if (boxID == null) {
//			boxID = getList(boxClass).get(0).getId();
//		}
//		H area = get(boxClass, boxID);
//		PropertyHolderDao areaDao = (PropertyHolderDao) getDaoByModel(boxClass);
//		if (area == null) {
//			boxID = getList(boxClass).get(0).getId();
//		}
//		List<IContainable> results = areaDao.findContainable(boxID);
//		Collections.sort(results);
//		return results;
//	}
//	
//	
//	/**
//	 * {@inheritDoc}
//	 */
//	public <H extends IPropertyHolder> List<IContainable> findTipologieProprietaInAreaWithRenderingFormula(
//			Class<H> boxClass, Integer boxID) {
//		PropertyHolderDao<H> areaDao = (PropertyHolderDao<H>) getDaoByModel(boxClass);
//		return areaDao.findContainableWithRenderingFormula(boxID);
//	}
//	
//
//	/**
//	 * {@inheritDoc}
//	 */
//	public <H extends IPropertyHolder> void deleteContainableInBox(IContainable tip) {
//		PropertyHolderDao<H> modelDao = (PropertyHolderDao<H>) getDaoByModel(getPropertyHolderClass());
//		// trovo le aree dove è mascherata la tipologia di proprietà e rompo
//		// l'associazione
//		List<H> aree = modelDao.findBoxByContainable(tip);
//		for (H area : aree) {
//			area.getMask().remove(tip);
//		}
//	}
//
//
//	@Override
//	public <P extends Property<TP>, TP extends PropertiesDefinition> List<TP> findTipologieProprietaAssegnabiliAndShowInList(
//			Class<? extends PropertiesDefinition> tipProprieta,
//			ATipologia<TP> tipologia) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//
//	public List<IContainable> getContainableOnCreation() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//
//	public List<IContainable> findContainableInBoxWithRenderingFormula(
//			Class<IPropertyHolder> boxClass, Integer boxId) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//
//	public <H extends IPropertyHolder> Class<H> getPropertyHolderClass() {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
