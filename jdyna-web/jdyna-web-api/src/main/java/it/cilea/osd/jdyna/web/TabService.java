package it.cilea.osd.jdyna.web;

import it.cilea.osd.jdyna.dao.ContainableDao;
import it.cilea.osd.jdyna.dao.PropertiesDefinitionDao;
import it.cilea.osd.jdyna.dao.PropertyHolderDao;
import it.cilea.osd.jdyna.dao.TabDao;
import it.cilea.osd.jdyna.model.Containable;
import it.cilea.osd.jdyna.model.IContainable;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.service.PersistenceDynaService;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


/**
 * Real service to manage Tab, Box and Containable objects
 * 
 * @author Pascarelli Andrea
 *
 */
public abstract class TabService extends PersistenceDynaService implements
		ITabService {
	
	/**
	 * {@inheritDoc}
	 */
	public <H extends IPropertyHolder<Containable>, T extends Tab<H>> List<H> findPropertyHolderInTab(Class<T> classTab, Integer tabId) {
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
	public <H extends IPropertyHolder<Containable>, T extends Tab<H>> void deletePropertyHolderInTabs(Class<T> classTab, H holder) {
		TabDao<H, T> tabDao = (TabDao<H,T>) getDaoByModel(classTab);
		// trovo le aree dove e' mascherata la tipologia di proprieta' e rompo
		// l'associazione
		List<T> tabs = tabDao.findTabsByHolder(holder);
		for (T tab : tabs) {
			tab.getMask().remove(holder);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public <H extends IPropertyHolder<Containable>, T extends Tab<H>> List<IContainable> findContainableInPropertyHolder(
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
//		Don't use this, hard-use on jsp
//		findOtherContainablesInBoxByConfiguration(area.getShortName(), results);
		Collections.sort(results);
		return results;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Deprecated
	public <TP extends PropertiesDefinition> List<IContainable> findAllContainables(Class<TP> classTipologiaProprieta) throws InstantiationException, IllegalAccessException {
		List<IContainable> containables = new LinkedList<IContainable>();
		List<TP> listTP = new LinkedList<TP>();
		PropertiesDefinitionDao<TP> modelTipologiaProprietaDao = (PropertiesDefinitionDao<TP>) getDaoByModel(classTipologiaProprieta);
		listTP.addAll(modelTipologiaProprietaDao.findAll());
		
		for(TP tp : listTP) {				
			ContainableDao<Containable> dao =  (ContainableDao<Containable>) getDaoByModel(tp.getDecoratorClass());			
			containables.add(dao.uniqueContainableByDecorable(tp.getId()));
		}
		findOtherContainables(containables);
		Collections.sort(containables);
		return containables;
	}
	
    public <TP extends PropertiesDefinition> List<IContainable> newFindAllContainables(Class<TP> classTipologiaProprieta) throws InstantiationException, IllegalAccessException {
        List<IContainable> containables = new LinkedList<IContainable>();
        List<TP> listTP = new LinkedList<TP>();
        PropertiesDefinitionDao<TP> modelTipologiaProprietaDao = (PropertiesDefinitionDao<TP>) getDaoByModel(classTipologiaProprieta);
        listTP.addAll(modelTipologiaProprietaDao.findAll());
        
        for(TP tp : listTP) {               
            ContainableDao<Containable> dao =  (ContainableDao<Containable>) getDaoByModel(tp.getDecoratorClass());         
            containables.add(dao.uniqueContainableByDecorable(tp.getId()));
        }
        findOtherContainables(containables, classTipologiaProprieta.getName());
        Collections.sort(containables);
        return containables;
    }	

	/** Extends this method to add other containables object type */
	@Deprecated
	protected abstract void findOtherContainables(List<IContainable> containables);
	
	protected abstract void findOtherContainables(List<IContainable> containables, String extraPrefixConfiguration);
		
	/** Extends this method to add other containables object type */
	@Deprecated
	public abstract void findOtherContainablesInBoxByConfiguration(String holderName,
			List<IContainable> containables);
	/** Extends this method to add other containables object type */
    public abstract void findOtherContainablesInBoxByConfiguration(String holderName,
            List<IContainable> containables, String extraPrefixConfiguration);
	
	/**
	 * {@inheritDoc}
	 */
	public <H extends IPropertyHolder<Containable>, T extends Tab<H>> void deleteContainableInPropertyHolder(Class<H> clazzH, IContainable containable) {
		PropertyHolderDao<H> modelDao = (PropertyHolderDao<H>) getDaoByModel(clazzH);
		List<H> boxs = modelDao.findHolderByContainable(containable);
		for (H box : boxs) {
			box.getMask().remove(containable);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IContainable findContainableByDecorable(Class decoratorClass,
			Integer decorable) {
		ContainableDao modelDao = (ContainableDao) getDaoByModel(decoratorClass);
		IContainable result = modelDao.uniqueContainableByDecorable(decorable);
		return result;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public <H extends IPropertyHolder<Containable>, T extends Tab<H>> T getTabByShortName(Class<T> clazzTab,
			String title) {
		TabDao<H, T> tabDao = (TabDao<H,T>) getDaoByModel(clazzTab);
		return tabDao.uniqueTabByShortName(title);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <H extends IPropertyHolder<Containable>> H getBoxByShortName(Class<H> clazzBox,
			String title) {
		PropertyHolderDao<H> boxDao = (PropertyHolderDao<H>) getDaoByModel(clazzBox);
		return boxDao.uniqueBoxByShortName(title);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <TP extends PropertiesDefinition> List<IContainable> getContainableOnCreation(Class<TP> model) {
		log.debug("find all containable on object creation");
		List<TP> results = getTipologiaOnCreation(model);
		List<IContainable> iResult = new LinkedList<IContainable>();
		for(TP rpPd : results) {
			iResult.add(findContainableByDecorable(rpPd.getDecoratorClass(), rpPd.getId()));
		}
		getOtherContainableOnCreation(iResult);
		return iResult;
	}

	/** Extends this method to add other containables on creation */
	protected abstract void getOtherContainableOnCreation(List<IContainable> containables);
}
